package com.odk.odcinterview.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Critere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String critereNom;
    private int barem;
    private boolean elimination;
    @OneToMany(mappedBy = "critere")
    private List<Question> questionList = new ArrayList<>();
    @ManyToOne
    @JsonIgnore
    private Entretien entretien;
}
