package com.odk.odcinterview.Service;

import com.odk.odcinterview.Model.Question;
import com.odk.odcinterview.Payload.NombreQuestionResponse;

import java.util.List;

public interface QuestionService {
    Question saveQuestion(Question question);
    Question updateQuestion(Question question,Long id);
    void deleteQuestion(Question question);
    List<Question> readQuestions();
    Question readQuestionByid(Long id);

    boolean existQuestionBynom(String nom);

    NombreQuestionResponse getNombreQuestionRepond(Long idPostulant);
}
