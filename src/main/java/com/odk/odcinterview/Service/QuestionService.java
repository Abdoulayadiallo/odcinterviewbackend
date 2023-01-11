package com.odk.odcinterview.Service;

import com.odk.odcinterview.Model.Question;

import java.util.List;

public interface QuestionService {
    Question saveQuestion(Question question);
    Question updateQuestion(Question question,Long id);
    void deleteQuestion(Question question);
    List<Question> readQuestions();
    Question readQuestionByid(Long id);
}
