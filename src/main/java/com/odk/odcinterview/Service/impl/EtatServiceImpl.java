package com.odk.odcinterview.Service.impl;


import com.odk.odcinterview.Model.Etat;
import com.odk.odcinterview.Repository.EtatRepository;
import com.odk.odcinterview.Service.EtatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EtatServiceImpl implements EtatService {

    @Autowired
    EtatRepository etatRepository;

    @Override
    public Etat Create(Etat etat) {
        return etatRepository.save(etat);
    }


    @Override
    public List<Etat> GetAll() {
        return etatRepository.findAll();
    }

    @Override
    public Etat Update(long id, Etat etat) {

        Etat et = etatRepository.findById(id).orElse(null);
        return etatRepository.save(et);
    }

    @Override
    public String Delete(long id) {
        etatRepository.deleteById(id);
        return " Supprimé avec succes";
    }

    @Override
    public Etat GetById(long id) {
        return etatRepository.findById(id).get();
    }

    @Override
    public Etat recupereParStatut(String statut) {
        // TODO Auto-generated method stub
        return etatRepository.findByStatus(statut);
    }
}
