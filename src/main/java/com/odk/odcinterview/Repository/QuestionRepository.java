package com.odk.odcinterview.Repository;

import com.odk.odcinterview.Model.Critere;
import com.odk.odcinterview.Model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {

    Question findQuestionById(Long id);
    Question findQuestionByQuestionNom(String nom);

    Boolean existsQuestionByQuestionNom(String string);

    List<Question> findQuestionByCritere(Critere critere);
}
