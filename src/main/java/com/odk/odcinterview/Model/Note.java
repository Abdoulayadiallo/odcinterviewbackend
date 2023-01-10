package com.odk.odcinterview.Model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
@Data
@RequiredArgsConstructor
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
}
