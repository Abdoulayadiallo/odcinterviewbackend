package com.odk.odcinterview.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Entretien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String entretienNom;
    private Date dateDebut;
    private Date dateFin;
    private String nombreParticipant;
    @OneToOne
    @JoinColumn(name = "questionnaire_id")
    private Questionnaire questionnaire;

}
