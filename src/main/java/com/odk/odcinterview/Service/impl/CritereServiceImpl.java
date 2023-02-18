package com.odk.odcinterview.Service.impl;

import com.odk.odcinterview.Model.Critere;
import com.odk.odcinterview.Model.Entretien;
import com.odk.odcinterview.Model.Question;
import com.odk.odcinterview.Repository.CritereRepository;
import com.odk.odcinterview.Repository.EntretienRepository;
import com.odk.odcinterview.Repository.QuestionRepository;
import com.odk.odcinterview.Service.CritereService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CritereServiceImpl implements CritereService {
    private final CritereRepository critereRepository;
    private final QuestionRepository questionRepository;
    private final EntretienRepository entretienRepository;

    public CritereServiceImpl(CritereRepository critereRepository,
                              QuestionRepository questionRepository,
                              EntretienRepository entretienRepository) {
        this.critereRepository = critereRepository;
        this.questionRepository = questionRepository;
        this.entretienRepository = entretienRepository;
    }

    @Override
    public Critere saveCritere(Critere critere,Long IdEntretien, Long questionId) {
        Question question = questionRepository.findQuestionById(questionId);
        Entretien entretien = entretienRepository.findEntretienById(IdEntretien);
        List<Question> questionList= new ArrayList<>();
        questionList.add(question);
        critere.setQuestion(questionList);
        critere.setEntretien(entretien);
        return critereRepository.save(critere);
    }

    @Override
    public Critere updateCritere(Critere critere, Long id) {
        Critere critere1 = critereRepository.findCritereById(id);
        critere1.setCritereNom(critere.getCritereNom());
        critere1.setBarem(critere.getBarem());
        critere1.setElimination(critere.isElimination());
        critereRepository.save(critere1);
        return critere1;
    }

    @Override
    public void deleteCritere(Critere critere) {
        critereRepository.delete(critere) ;
    }

    @Override
    public List<Critere> readCriteres() {
        return critereRepository.findAll();
    }

    @Override
    public Critere readCritereByid(Long id) {
        return critereRepository.findCritereById(id);
    }

    @Override
    public Critere saveCritereSimple(Critere critere) {
        return critereRepository.save(critere);
    }

    @Override
    public Boolean existCritereByNom(String nom) {
        return critereRepository.existsCritereByCritereNom(nom);
    }
}
