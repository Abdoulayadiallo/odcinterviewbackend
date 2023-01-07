package com.odk.odcinterview.Model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Questionnaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String questionnaireNom;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Question> questionList;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Critere> critereList;
}
