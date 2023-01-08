package com.odk.odcinterview.Model;

import javax.persistence.*;
import java.util.List;

@Entity
public class ListePostulant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ListeNom;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Postulant> Postulant;
    @OneToOne
    private Entretien entretien;
}
