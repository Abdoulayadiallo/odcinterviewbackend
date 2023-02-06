package com.odk.odcinterview.Service.impl;

import com.odk.odcinterview.Model.*;
import com.odk.odcinterview.Payload.NombreQuestionResponse;
import com.odk.odcinterview.Repository.CritereRepository;
import com.odk.odcinterview.Repository.NoteRepository;
import com.odk.odcinterview.Repository.PostulantRepository;
import com.odk.odcinterview.Repository.QuestionRepository;
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

    public QuestionServiceImpl(QuestionRepository questionRepository,
                               PostulantRepository postulantRepository,
                               CritereRepository critereRepository,
                               NoteRepository noteRepository) {
        this.questionRepository = questionRepository;
        this.postulantRepository = postulantRepository;
        this.critereRepository = critereRepository;
        this.noteRepository = noteRepository;
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
    public NombreQuestionResponse getNombreQuestionRepond(Long idPostulant) {
        Postulant postulant = postulantRepository.findPostulantById(idPostulant);
        Entretien entretien = postulant.getEntretien();
        NombreQuestionResponse nombreQuestionResponse = new NombreQuestionResponse();
        List<Critere> criteres = critereRepository.findCritereByEntretien(entretien);
        if (!criteres.isEmpty()){
            List<Critere> criteresNote = new ArrayList<>();
            for (Critere critere:criteres){
                List<Note> notes = noteRepository.findNoteByCritere(critere);
                for(Note note: notes){
                    if(note!=null){
                        criteresNote.add(note.getCritere());
                    }
                }
            }

            int nombretotal =criteres.size();
            int nombrecriterenote =criteresNote.size();
            nombreQuestionResponse.setContenu(criteres);
            nombreQuestionResponse.setNombreParCritereNote(nombretotal);
            nombreQuestionResponse.setTotalListe(nombrecriterenote);
            nombreQuestionResponse.setPourcentage(nombrecriterenote * 100f/nombretotal);
        }
        nombreQuestionResponse.setContenu(criteres);
        nombreQuestionResponse.setNombreParCritereNote(0);
        nombreQuestionResponse.setTotalListe(criteres.size());
        nombreQuestionResponse.setPourcentage(0f);

        return nombreQuestionResponse;
    }
}
