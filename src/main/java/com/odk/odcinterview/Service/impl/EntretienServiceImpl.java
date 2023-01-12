package com.odk.odcinterview.Service.impl;

import com.odk.odcinterview.Model.Critere;
import com.odk.odcinterview.Model.Entretien;
import com.odk.odcinterview.Repository.CritereRepository;
import com.odk.odcinterview.Repository.EntretienRepository;
import com.odk.odcinterview.Service.EntretienService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class EntretienServiceImpl implements EntretienService {
    private final EntretienRepository entretienRepository;
    private final CritereRepository critereRepository;


    @Override
    public Entretien saveEntretien(Entretien entretien, Long idCritere) {
        Critere critere = critereRepository.findCritereById(idCritere);
        List<Critere> critereList = new ArrayList<>();
        critereList.add(critere);
        entretien.setCritereList(critereList);
        Date date = new Date();
        entretien.setDateCreation(date);
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
}
