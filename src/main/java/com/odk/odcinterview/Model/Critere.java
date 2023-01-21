package com.odk.odcinterview.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;
@Data
@RequiredArgsConstructor
@Entity
public class Critere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String critereNom;
    private int barem;
    private boolean elimination;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Question> questionList;
    @ManyToOne
    @JsonIgnore
    private Entretien entretien;
}
