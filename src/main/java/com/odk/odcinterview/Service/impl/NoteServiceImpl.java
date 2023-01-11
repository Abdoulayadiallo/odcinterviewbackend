package com.odk.odcinterview.Service.impl;

import com.odk.odcinterview.Model.Note;
import com.odk.odcinterview.Service.NoteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    @Override
    public Note saveNote(Note note, Long critereId, Long juryId,Long postulantId) {
        return null;
    }

    @Override
    public Note updateNote(Note note, Long id) {
        return null;
    }

    @Override
    public void deleteNote(Note note) {

    }

    @Override
    public List<Note> readNotes() {
        return null;
    }

    @Override
    public Note readNoteByid(Long id) {
        return null;
    }
}
