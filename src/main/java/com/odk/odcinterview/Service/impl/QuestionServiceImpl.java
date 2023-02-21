package com.odk.odcinterview.Service.impl;

import com.odk.odcinterview.Model.*;
import com.odk.odcinterview.Payload.NombreQuestionResponse;
import com.odk.odcinterview.Repository.*;
import com.odk.odcinterview.Service.QuestionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final PostulantRepository postulantRepository;
    private final CritereRepository critereRepository;
    private final NoteRepository noteRepository;
    private final EntretienRepository entretienRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository,
                               PostulantRepository postulantRepository,
                               CritereRepository critereRepository,
                               NoteRepository noteRepository,
                               EntretienRepository entretienRepository) {
        this.questionRepository = questionRepository;
        this.postulantRepository = postulantRepository;
        this.critereRepository = critereRepository;
        this.noteRepository = noteRepository;
        this.entretienRepository = entretienRepository;
    }

    @Override
    public Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }

    @Override
    public Question updateQuestion(Question question, Long id) {
        Question question1 = questionRepository.findQuestionById(id);
        question1.setQuestionNom(question.getQuestionNom());
        question1.setType(question.getType());
        return questionRepository.save(question1);
    }

    @Override
    public void deleteQuestion(Question question) {
        questionRepository.delete(question);
    }

    @Override
    public List<Question> readQuestions() {
        return questionRepository.findAll();
    }

    @Override
    public Question readQuestionByid(Long id) {
        return questionRepository.findQuestionById(id);
    }

    @Override
    public boolean existQuestionBynom(String nom) {
        return questionRepository.existsQuestionByQuestionNom(nom);
    }

    @Override
    public List<Question> getQuestionByCritere(Long idCritere) {
        Critere critere = critereRepository.findCritereById(idCritere);
        return questionRepository.findQuestionByCritere(critere);
    }

    @Override
    public List<Question> getQuestionByEntretien(Long idEntretien) {
        List<Question> questions= questionRepository.findQuestionByEntretien(idEntretien);
        return questions;
    }

    @Override
    public NombreQuestionResponse getNombreQuestionRepond(Long idPostulant) {
        List<Note> note = noteRepository.CritereNoteByPostulant(idPostulant);
        NombreQuestionResponse nombreQuestionResponse = new NombreQuestionResponse();
        Entretien entretien = entretienRepository.findEntretienByPostulants(postulantRepository.findPostulantById(idPostulant));
        List<Critere> criteres = critereRepository.findCritereByEntretien(entretien);
            int nombretotal =criteres.size();
            int nombrecriterenote = note.size();
            nombreQuestionResponse.setContenu(criteres);
            nombreQuestionResponse.setNombreParCritereNote(nombretotal);
            nombreQuestionResponse.setTotalListe(nombrecriterenote);
            nombreQuestionResponse.setPourcentage(nombrecriterenote * 100f/nombretotal);
        return nombreQuestionResponse;
    }
}
