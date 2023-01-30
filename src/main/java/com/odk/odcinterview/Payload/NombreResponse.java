package com.odk.odcinterview.Payload;

import com.odk.odcinterview.Model.Postulant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NombreResponse {
    List<Postulant> contenu;
    private float pourcentage;
    private float nombreParGenre;
    private float TotalListe;


}
