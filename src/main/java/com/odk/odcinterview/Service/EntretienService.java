package com.odk.odcinterview.Service;

import com.odk.odcinterview.Model.Entretien;

import java.util.List;

public interface EntretienService {
    Entretien saveEntretien(Entretien entretien,Long idCritere);
    Entretien updateEntretien(Entretien entretien,Long id);
    void deleteEntretien(Entretien entretien);
    List<Entretien> readEntretiens();
    Entretien readEntretienByid(Long id);
}
