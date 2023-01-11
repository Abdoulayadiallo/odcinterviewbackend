package com.odk.odcinterview.Service.impl;

import com.odk.odcinterview.Model.*;
import com.odk.odcinterview.Repository.ParticipantRepository;
import com.odk.odcinterview.Repository.RoleRepository;
import com.odk.odcinterview.Repository.UtilisateurRepository;
import com.odk.odcinterview.Service.AccountService;
import com.odk.odcinterview.Service.ParticipantService;
import com.odk.odcinterview.util.Constants;
import com.odk.odcinterview.util.EmailConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private ParticipantRepository participantRepository;
    @Autowired
    private ParticipantService participantService;

    public Utilisateur saveUser(String nom, String prenom, String email, String numero, String genre) {
        String password = nom.substring(0, 1) + prenom.substring(0, 1) + "@ODC2023";
        String username = nom.substring(0, 1) + prenom.substring(0, 1) + nom;
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setPassword(encryptedPassword);
        utilisateur.setNom(nom);
        utilisateur.setPrenom(prenom);
        utilisateur.setUsername(username);
        utilisateur.setEmail(email);
        utilisateur.setGenre(genre);
        if (utilisateur.getDateCreation() == null) {
            utilisateur.setDateCreation(new Date());
        }
        Role role = roleRepository.findByRoleName(Erole.JURY);
        utilisateur.setRole(role);
        Participant participant= new Participant();
        participant.setNom(utilisateur.getNom());
        participant.setPrenom(utilisateur.getPrenom());
        participant.setEmail(utilisateur.getEmail());
        participant.setStatus(Estatus.Jury);
        participantService.saveParticipant(participant);
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
        String username = request.get("username");
        String email = request.get("email");
        String bio = request.get("bio");
        utilisateur.setNom(nom);
        utilisateur.setNumero(numero);
        utilisateur.setNumero(prenom);
        utilisateur.setUsername(username);
        utilisateur.setEmail(email);
        utilisateur.setBio(bio);
        utilisateurRepository.save(utilisateur);
        mailSender.send(emailConstructor.constructUpdateUserProfileEmail(utilisateur));
        return utilisateur;
    }

    @Override
    public void deleteUser(Utilisateur utilisateur) {
        utilisateurRepository.delete(utilisateur);
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
    public Utilisateur findByEmail(String userEmail) {
        return utilisateurRepository.findByEmail(userEmail);
    }
}