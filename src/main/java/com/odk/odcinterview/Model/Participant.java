package com.odk.odcinterview.Model;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    @OneToOne
    private Utilisateur utilisateurs;
    @OneToOne
    private Postulant postulant;
    @ManyToOne()
    private Entretien entretien;
}
