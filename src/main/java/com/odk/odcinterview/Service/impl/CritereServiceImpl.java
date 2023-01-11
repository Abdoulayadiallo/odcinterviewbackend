package com.odk.odcinterview.Service.impl;

import com.odk.odcinterview.Model.Critere;
import com.odk.odcinterview.Model.Question;
import com.odk.odcinterview.Repository.CritereRepository;
import com.odk.odcinterview.Repository.QuestionRepository;
import com.odk.odcinterview.Service.CritereService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CritereServiceImpl implements CritereService {
    private final CritereRepository critereRepository;
    private final QuestionRepository questionRepository;

    public CritereServiceImpl(CritereRepository critereRepository,
                              QuestionRepository questionRepository) {
        this.critereRepository = critereRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public Critere saveCritere(Critere critere,Long questionId) {
        Question question = questionRepository.findQuestionById(questionId);
        critere.setQuestionList((List<Question>) question);
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
}
