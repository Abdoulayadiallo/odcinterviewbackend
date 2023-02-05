package com.odk.odcinterview.Payload;

import com.odk.odcinterview.Model.Critere;
import com.odk.odcinterview.Model.Postulant;
import com.odk.odcinterview.Model.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NombreQuestionResponse {
    List<Critere> contenu;
    private float pourcentage;
    private int nombreParCritereNote;
    private int TotalListe;


}
