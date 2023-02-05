package com.odk.odcinterview.Payload;

import com.odk.odcinterview.Model.Note;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteResponse {
    Note contenu;
    String postulant;
    String utilisateur;
    private boolean isNoted=false;
}
