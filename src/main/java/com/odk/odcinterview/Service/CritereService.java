package com.odk.odcinterview.Service;

import com.odk.odcinterview.Model.Critere;

import java.util.List;

public interface CritereService {
    Critere saveCritere(Critere critere,Long IdEntretien, Long questionId);
    Critere updateCritere(Critere critere,Long id);
    void deleteCritere(Critere critere);
    List<Critere> readCriteres();
    Critere readCritereByid(Long id);

    Critere saveCritereSimple(Critere critere);
    Boolean existCritereByNom(String nom);
}
