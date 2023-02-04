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
public class Entretien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String entretienNom;
    private Date dateDebut;
    private Date dateFin;
    private Date dateCreation;
    private String description;
    private String nombreParticipant;
    @OneToMany(mappedBy = "entretien",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Critere> critere;
    @OneToMany(mappedBy = "entretien" ,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Participant> participants;
    @ManyToOne
    private Etat etat;
}
