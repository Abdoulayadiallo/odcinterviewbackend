package com.odk.odcinterview.Service.impl;

import com.odk.odcinterview.Model.Question;
import com.odk.odcinterview.Repository.QuestionRepository;
import com.odk.odcinterview.Service.QuestionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
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
}
