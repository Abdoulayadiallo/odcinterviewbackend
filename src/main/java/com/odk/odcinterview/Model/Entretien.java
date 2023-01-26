package com.odk.odcinterview.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
@Data
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
    private String nombreParticipant;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Critere> critereList;
    @OneToMany(mappedBy = "entretien" ,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Participant> participants;
    @ManyToOne
    private Etat etat;
}
