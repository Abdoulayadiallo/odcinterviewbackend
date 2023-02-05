package com.odk.odcinterview.Model;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int point;
    private String commentaire;
    @OneToOne
    @JoinColumn(name = "critere_id")
    private Critere critere;
    @ManyToOne
    private Postulant postulant;
    @ManyToOne
    private Utilisateur utilisateur;
}
