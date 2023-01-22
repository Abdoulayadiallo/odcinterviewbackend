package com.odk.odcinterview.Service.impl;

import com.odk.odcinterview.Model.Critere;
import com.odk.odcinterview.Model.Entretien;
import com.odk.odcinterview.Model.Etat;
import com.odk.odcinterview.Repository.CritereRepository;
import com.odk.odcinterview.Repository.EntretienRepository;
import com.odk.odcinterview.Repository.EtatRepository;
import com.odk.odcinterview.Service.EntretienService;
import lombok.AllArgsConstructor;
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
    public List<Entretien> readEntretiens() {
        return entretienRepository.findAll();
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

            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        //System.err.println("helllle");

    }

}
