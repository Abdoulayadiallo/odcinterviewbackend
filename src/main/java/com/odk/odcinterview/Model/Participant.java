package com.odk.odcinterview.Model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
@Data
@RequiredArgsConstructor
@Entity
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    @Enumerated(EnumType.STRING)
    private Estatus status;
    @ManyToMany
    @JoinTable(name = "Participant_notification",
            joinColumns = @JoinColumn(name = "participant_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "notification_id", referencedColumnName = "id"))
    private Collection<Notification> notifications;
    @ManyToMany
    @JoinTable(name = "Participant_note",
            joinColumns = @JoinColumn(name = "participant_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "note_id", referencedColumnName = "id"))
    private Collection<Note> notes;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Utilisateur> utilisateurs;
}
