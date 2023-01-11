package com.odk.odcinterview.Service;

import com.odk.odcinterview.Model.Critere;
import com.odk.odcinterview.Model.Note;

import java.util.List;

public interface NoteService {
    Note saveNote(Note note, Long critereId);
    Note updateNote(Note note,Long id);
    void deleteNote(Note note);
    List<Note> readNotes();
    Note readNoteByid(Long id);
}
