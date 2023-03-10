package com.odk.odcinterview.Service;

import com.odk.odcinterview.Model.Critere;
import com.odk.odcinterview.Model.Note;
import com.odk.odcinterview.Payload.NoteResponse;

import java.util.List;

public interface NoteService {
    Note saveNote(Note note, Long critereId,Long postulantId,String jury);
    Note updateNote(Note note,Long id);
    void deleteNote(Note note);
    List<Note> readNotes();
    Note readNoteByid(Long id);

    NoteResponse GetNoteByCritere(Long IdCritere,Long idJury,Long idPostulant);
    List<Note> GetNotePostulant(Long idPostulant);
}
