package com.odk.odcinterview.Service.impl;

import com.odk.odcinterview.Model.*;
import com.odk.odcinterview.Payload.EntretienResponse;
import com.odk.odcinterview.Payload.PostulantResponse;
import com.odk.odcinterview.Repository.*;
import com.odk.odcinterview.Service.EntretienService;
import com.odk.odcinterview.util.Constants;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class EntretienServiceImpl implements EntretienService {
    private final EntretienRepository entretienRepository;
    private final CritereRepository critereRepository;
    private final EtatRepository etatRepository;
    private final UtilisateurRepository utilisateurRepository;
   // private final ParticipantRepository participantRepository;

    public void creerEtatselonDate(Entretien entretien){
        Etat encour=etatRepository.findByStatus("EN COUR");
        Etat termine=etatRepository.findByStatus("TERMINE");
        Etat avenir=etatRepository.findByStatus("A VENIR");
        Date today = new Date();
        entretien.setDateCreation(today);

        // List<Critere> critereList = new ArrayList<>();
        //critereList.add(critere);
        //entretien.setCritereList(critereList);
        if (today.after(entretien.getDateDebut()) && today.before(entretien.getDateFin())) {
            entretien.setEtat(encour);
            entretienRepository.save(entretien);
        }else if (today.before(entretien.getDateDebut())) {
            entretien.setEtat(avenir);
            entretienRepository.save(entretien);
        }else if (today.after(entretien.getDateFin())) {
            entretien.setEtat(termine);
            entretienRepository.save(entretien);
        }
    }
    @Override
    public Entretien saveEntretien(Entretien entretien) {
        //Critere critere = critereRepository.findCritereById(idCritere);
        //
        creerEtatselonDate(entretien);
        return entretienRepository.save(entretien);
    }

    @Override
    public Entretien updateEntretien(Entretien entretien, Long id) {
        Entretien entretien1= entretienRepository.findEntretienById(id);
        entretien1.setEntretienNom(entretien.getEntretienNom());
        entretien1.setDateDebut(entretien.getDateDebut());
        entretien1.setDateFin(entretien.getDateFin());
        entretien1.setDescription(entretien.getDescription());
        entretien1.setNombreParticipant(entretien.getNombreParticipant());
        creerEtatselonDate(entretien1);
        return entretienRepository.save(entretien1);
    }

    @Override
    public void deleteEntretien(Entretien entretien) {
         entretienRepository.delete(entretien);
    }

    @Override
    public EntretienResponse readEntretiens(int pageNo,int pageSize,String sortBy, String sortDir,String username,String nomEntretien) {
        //Recuperer l' utilisateur par son id
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username);
        //Faire le trie par ordre croissant
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        //Une Pageable pour parametre le nombre de page,le nombre d'element d'une page,le trie
        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);
        Page<Entretien> entretiens;
        //Si l'username est null on recupere tous les entretien;
        if(nomEntretien != null && username==null)
            entretiens = entretienRepository.findEntretiensByEntretienNomContaining(nomEntretien,pageable);
        else if(username != null && nomEntretien==null)
            //Sinon on recupere les entretiens de l'utilisateur
            entretiens = entretienRepository.findEntretiensByUtilisateurs(utilisateur,pageable);
        else
            entretiens = entretienRepository.findAll(pageable);
        List<Entretien> listOfEntretien = entretiens.getContent();
        //
        EntretienResponse entretienResponse = new EntretienResponse();
        entretienResponse.setContenu(listOfEntretien);
        entretienResponse.setPageNo(entretiens.getNumber());
        entretienResponse.setPageSize(entretiens.getSize());
        entretienResponse.setTotalElements(entretiens.getTotalElements());
        entretienResponse.setTotalPages(entretiens.getTotalPages());
        entretienResponse.setLast(entretiens.isLast());
        entretienResponse.setUsername(username);
        return entretienResponse;
    }

    @Override
    public Entretien readEntretienByid(Long id) {
        return entretienRepository.findEntretienById(id);
    }

    @Scheduled(fixedDelayString = "1000")
    public void etatEntretien(){
        List<Entretien> allEntretiens=entretienRepository.findAll();


        Date today = new Date();
        //
        Etat encour=etatRepository.findByStatus("EN COUR");
        Etat termine=etatRepository.findByStatus("TERMINE");
        Etat avenir=etatRepository.findByStatus("A VENIR");




        for(Entretien entretien:allEntretiens){
            try {
                if (today.after(entretien.getDateDebut()) && today.before(entretien.getDateFin())) {
                    entretien.setEtat(encour);
                    entretienRepository.save(entretien);
                    System.out.println(entretien.getEtat()  + "afficher encour");
                }else if (today.before(entretien.getDateDebut())) {
                    entretien.setEtat(avenir);
                    entretienRepository.save(entretien);
                    System.out.println(entretien.getEtat()  + "afficher avenir");

                }else if (today.after(entretien.getDateFin())) {
                    entretien.setEtat(termine);
                    entretienRepository.save(entretien);
                    System.out.println(entretien.getEtat()+"afficher terminé");

                }
                System.out.println(entretien);

            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        //System.err.println("helllle");

    }
    @Override
    public String saveEntretienImage(MultipartFile multipartFile, Long idEntretien) {
        byte[] bytes;
        try {
            Files.deleteIfExists(Paths.get(Constants.INTERVIEW_FOLDER + "/" + idEntretien + ".png"));
            bytes = multipartFile.getBytes();
            Path path = Paths.get(Constants.INTERVIEW_FOLDER + idEntretien + ".png");
            Files.write(path, bytes);
            return "Interview picture saved to server";
        } catch (IOException e) {
            return "Interview picture Saved";
        }
    }

}
