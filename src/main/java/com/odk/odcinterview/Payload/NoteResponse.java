package com.odk.odcinterview.Payload;

import com.odk.odcinterview.Model.Note;
import com.odk.odcinterview.Model.Participant;
import com.odk.odcinterview.Model.Postulant;
import com.odk.odcinterview.Model.Utilisateur;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteResponse {
    Note contenu;
    Postulant postulant;
    Utilisateur utilisateur;
    private boolean isNoted=false;
}
