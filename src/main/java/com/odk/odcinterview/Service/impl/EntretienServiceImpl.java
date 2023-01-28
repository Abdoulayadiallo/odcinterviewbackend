package com.odk.odcinterview.Service.impl;

import com.odk.odcinterview.Model.*;
import com.odk.odcinterview.Payload.EntretienResponse;
import com.odk.odcinterview.Payload.PostulantResponse;
import com.odk.odcinterview.Repository.*;
import com.odk.odcinterview.Service.EntretienService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
    private final ParticipantRepository participantRepository;


    @Override
    public Entretien saveEntretien(Entretien entretien) {
        //Critere critere = critereRepository.findCritereById(idCritere);
        //
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

        return entretienRepository.save(entretien);
    }

    @Override
    public Entretien updateEntretien(Entretien entretien, Long id) {
        Entretien entretien1= entretienRepository.findEntretienById(id);
        entretien1.setEntretienNom(entretien.getEntretienNom());
        entretien1.setNombreParticipant(entretien.getNombreParticipant());
        return entretienRepository.save(entretien1);
    }

    @Override
    public void deleteEntretien(Entretien entretien) {
         entretienRepository.delete(entretien);
    }

    @Override
    public EntretienResponse readEntretiens(int pageNo,int pageSize,String sortBy, String sortDir,String username) {
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username);
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);
        Page<Entretien> entretiens;
        if(username==null)
            entretiens = entretienRepository.findAll(pageable);
        else
            entretiens = entretienRepository.findEntretienByParticipantsContaining(utilisateur.getParticipant(),pageable);
        List<Entretien> listOfEntretien = entretiens.getContent();
        EntretienResponse entretienResponse = new EntretienResponse();
        entretienResponse.setContenu(listOfEntretien);
        entretienResponse.setPageNo(entretiens.getNumber());
        entretienResponse.setPageSize(entretiens.getSize());
        entretienResponse.setTotalElements(entretiens.getTotalElements());
        entretienResponse.setTotalPages(entretiens.getTotalPages());
        entretienResponse.setLast(entretiens.isLast());
        return entretienResponse;
    }

    @Override
    public Entretien readEntretienByid(Long id) {
        return entretienRepository.findEntretienById(id);
    }

    @Scheduled(fixedRateString = "1000")
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
                }else if (today.before(entretien.getDateDebut())) {
                    entretien.setEtat(avenir);
                    entretienRepository.save(entretien);
                }else if (today.after(entretien.getDateFin())) {
                    entretien.setEtat(termine);
                    entretienRepository.save(entretien);
                }
                System.out.println(entretien);

            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        //System.err.println("helllle");

    }

}
