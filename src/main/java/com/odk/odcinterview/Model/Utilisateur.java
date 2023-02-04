package com.odk.odcinterview.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String image;
    private String nom;
    private String prenom;
    @Column(unique = true,nullable = false)
    private String email;
    @Column(unique = true,nullable = false)
    private String username;
    private String numero;
    private String genre;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Column(columnDefinition = "text")
    private String bio;
    private Date dateCreation;
    @OneToOne()
    @JsonIgnore
    @JoinColumn(name = "participant_id")
    private Participant participant;
    @ManyToOne(fetch = FetchType.EAGER)
    Role role;

    public Utilisateur(Long id, String image, String nom, String prenom, String email,String numero,String genre,String password) {
        this.id=id;
        this.image=image;
        this.nom=nom;
        this.prenom=prenom;
        this.email=email;
        this.numero=numero;
        this.genre=genre;
        this.password=password;
    }
}
