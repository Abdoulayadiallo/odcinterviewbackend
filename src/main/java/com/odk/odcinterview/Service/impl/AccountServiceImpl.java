package com.odk.odcinterview.Service.impl;

import com.odk.odcinterview.Model.*;
import com.odk.odcinterview.Payload.JuryResponse;
import com.odk.odcinterview.Repository.EntretienRepository;
import com.odk.odcinterview.Repository.RoleRepository;
import com.odk.odcinterview.Repository.UtilisateurRepository;
import com.odk.odcinterview.Service.AccountService;
import com.odk.odcinterview.util.Constants;
import com.odk.odcinterview.util.EmailConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;


@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    UtilisateurRepository utilisateurRepository;

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private EmailConstructor emailConstructor;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private EntretienRepository entretienRepository;

    public Utilisateur saveUser(String nom, String prenom, String email, String numero, String genre, Long idEntretien) {
        Entretien entretien = entretienRepository.findEntretienById(idEntretien);
        LocalDate date = LocalDate.now();
        String password = nom.substring(0, 1) + prenom.substring(0, 1) + "@ODC" + date.getYear();
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setPassword(encryptedPassword);
        utilisateur.setNom(nom);
        utilisateur.setPrenom(prenom);
        utilisateur.setNumero(numero);
        // declaration d'une liste pour les nom utilisateur existant
        //List<String> existingUsernames = new ArrayList<String>();
        // initialisation de la valeur
        //int sequence=1;
        String username = nom.substring(0, 1) + prenom.substring(0, 1) + nom;
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();

        if (utilisateurs.contains(utilisateurRepository.findByUsername(username))) {
            int sequenceNum = 1;
            String newUsername = username + sequenceNum;
            while (utilisateurs.contains(utilisateurRepository.findByUsername(newUsername))) {
                sequenceNum++;
                newUsername = username + sequenceNum;
            }
            username = newUsername;
        }
        utilisateur.setUsername(username);
        utilisateur.setEmail(email);
        utilisateur.setGenre(genre);
        if (utilisateur.getDateCreation() == null) {
            utilisateur.setDateCreation(new Date());

        }
        Role role = roleRepository.findByRoleName(Erole.JURY);
        utilisateur.setRole(role);
        utilisateur.setEntretien(entretien);
        utilisateurRepository.save(utilisateur);
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(Constants.TEMP_USER.toPath());
            String fileName = utilisateur.getId() + ".png";
            Path path = Paths.get(Constants.USER_FOLDER + fileName);
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mailSender.send(emailConstructor.constructNewUserEmail(utilisateur, password));
        return utilisateur;
    }

    public Utilisateur saveAdmin(Utilisateur utilisateur) {
        Role role = roleRepository.findByRoleName(Erole.ADMIN);
        utilisateur.setUsername("ADMIN");
        utilisateur.setRole(role);
        String encryptedPassword = bCryptPasswordEncoder.encode(utilisateur.getPassword());
        utilisateur.setPassword(encryptedPassword);
        if (utilisateur.getDateCreation() == null) {
            utilisateur.setDateCreation(new Date());
        }
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(Constants.TEMP_USER.toPath());
            String fileName = utilisateur.getId() + ".png";
            Path path = Paths.get(Constants.USER_FOLDER + fileName);
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void updateUserPassword(Utilisateur utilisateur, String newpassword) {
        String pwencode = bCryptPasswordEncoder.encode(newpassword);
        utilisateur.setPassword(pwencode);
        utilisateurRepository.save(utilisateur);
        mailSender.send(emailConstructor.constructResetPasswordEmail(utilisateur, newpassword));
    }

    @Override
    public Utilisateur updateUser(Utilisateur utilisateur, HashMap<String, String> request) {
        String nom = request.get("nom");
        String numero = request.get("numero");
        String prenom = request.get("prenom");
        //String username = request.get("username");
        String email = request.get("email");
        String bio = request.get("bio");
        utilisateur.setNom(nom);
        utilisateur.setNumero(numero);
        utilisateur.setPrenom(prenom);
        //utilisateur.setUsername(username);
        utilisateur.setEmail(email);
        utilisateur.setBio(bio);
        utilisateurRepository.save(utilisateur);
        mailSender.send(emailConstructor.constructUpdateUserProfileEmail(utilisateur));
        return utilisateur;
    }

    @Override
    public void deleteUser(Utilisateur utilisateur) {
        try {
            Files.deleteIfExists(Paths.get(Constants.USER_FOLDER + "/" + utilisateur.getId() + ".png"));
            utilisateurRepository.delete(utilisateur);
        } catch (Exception e) {
        }
    }

    @Override
    public void resetPassword(Utilisateur utilisateur) {
        String password = RandomStringUtils.randomAlphanumeric(10);
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        utilisateur.setPassword(encryptedPassword);
        utilisateurRepository.save(utilisateur);
        mailSender.send(emailConstructor.constructResetPasswordEmail(utilisateur, password));
    }

    @Override
    public List<Utilisateur> getUsersListByUsername(String username) {
        return utilisateurRepository.findByUsernameContaining(username);
    }


    @Override
    public Utilisateur findByUsername(String username) {
        return utilisateurRepository.findByUsername(username);
    }

    @Override
    public List<Utilisateur> userList() {
        return utilisateurRepository.findAll();
    }

    @Override
    public JuryResponse juryList(int pageNo, int pageSize, String sortBy, String sortDir, String keyword) {
        Role juryrole = roleRepository.findByRoleName(Erole.JURY);
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Utilisateur> utilisateurs;
        if (keyword == null)
            utilisateurs = utilisateurRepository.findUtilisteurByRole(juryrole, pageable);
        else
            utilisateurs = utilisateurRepository.findAllJUryByKeyword(keyword, pageable);
        List<Utilisateur> jurylist = utilisateurs.getContent();
        JuryResponse juryResponse = new JuryResponse();
        juryResponse.setContenu(jurylist);
        juryResponse.setPageNo(utilisateurs.getNumber());
        juryResponse.setPageSize(utilisateurs.getSize());
        juryResponse.setTotalPages(utilisateurs.getTotalPages());
        juryResponse.setTotalElements(utilisateurs.getTotalElements());
        juryResponse.setLast(utilisateurs.isLast());
        juryResponse.setKeyword(keyword);
        return juryResponse;
    }

    @Override
    public JuryResponse juryListByEntretien(Long idEntretien, int pageNo, int pageSize, String sortBy, String sortDir, String keyword) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Utilisateur> utilisateurs;
        if (keyword == null)
            utilisateurs = utilisateurRepository.findUtilisateurByEntretien(idEntretien, pageable);
        else
            utilisateurs = utilisateurRepository.findUtilisateurEntretien0rByKeyword(idEntretien, keyword, pageable);

        List<Utilisateur> jurylist = utilisateurs.getContent();
        JuryResponse juryResponse = new JuryResponse();
        juryResponse.setContenu(jurylist);
        juryResponse.setPageNo(utilisateurs.getNumber());
        juryResponse.setPageSize(utilisateurs.getSize());
        juryResponse.setTotalPages(utilisateurs.getTotalPages());
        juryResponse.setTotalElements(utilisateurs.getTotalElements());
        juryResponse.setLast(utilisateurs.isLast());
        juryResponse.setKeyword(keyword);
        return juryResponse;
    }

    @Override
    public Utilisateur findByEmail(String userEmail) {
        return utilisateurRepository.findByEmail(userEmail);
    }

    @Override
    public String saveUserImage(MultipartFile multipartFile, Long userImageId) {
        byte[] bytes;
        try {
            Files.deleteIfExists(Paths.get(Constants.USER_FOLDER + "/" + userImageId + ".png"));
            bytes = multipartFile.getBytes();
            Path path = Paths.get(Constants.USER_FOLDER + userImageId + ".png");
            Files.write(path, bytes);
            return "User picture saved to server";
        } catch (IOException e) {
            return "User picture Saved";
        }
    }

    @Override
    public Utilisateur findUserById(Long id) {
        return utilisateurRepository.findUtilisteurById(id);
    }


}
