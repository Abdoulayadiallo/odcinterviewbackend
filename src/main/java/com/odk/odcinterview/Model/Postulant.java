package com.odk.odcinterview.Model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Postulant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String numero;
    private String numeroMTCL;
    private String genre;
    private String status;

}
