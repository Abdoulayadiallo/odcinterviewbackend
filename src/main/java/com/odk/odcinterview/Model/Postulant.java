package com.odk.odcinterview.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Postulant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String numero;
    private String genre;
    private String numeroMTCL;
    private String resultatFinal;
    private String noteFinal;
    private int rang;
    @Enumerated(EnumType.STRING)
    private DesisionFinal decisionFinal;
    private String commentaireFinal;
    private Date dateCreation;
    @OneToMany(mappedBy = "postulant" ,cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Note> notes;
    @ManyToOne
    private Entretien entretien;


}
