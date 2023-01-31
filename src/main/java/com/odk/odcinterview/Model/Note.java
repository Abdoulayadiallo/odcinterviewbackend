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
    @ManyToMany
    @JoinTable(name = "Participant_note",
            joinColumns = @JoinColumn(name = "note_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id", referencedColumnName = "id"))
    private Collection<Participant> participants;
}
