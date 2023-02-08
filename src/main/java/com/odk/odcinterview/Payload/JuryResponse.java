package com.odk.odcinterview.Payload;

import com.odk.odcinterview.Model.Postulant;
import com.odk.odcinterview.Model.Utilisateur;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JuryResponse {
    List<Utilisateur> contenu;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
    private String Keyword;
    private float pourcentage;
    private int nombreParGenre;
    private int TotalListe;

}
