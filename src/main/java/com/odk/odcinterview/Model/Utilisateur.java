package com.odk.odcinterview.Model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String image;
    private String nom;
    private String prenom;
    private String email;
    private String username;
    private String numero;
    private String genre;
    private String password;
    private String bio;
    private Date dateCreation;
    @OneToOne
    @JoinColumn(name = "participant_id")
    private Participant participant;
    @ManyToOne
    Role role;

}
