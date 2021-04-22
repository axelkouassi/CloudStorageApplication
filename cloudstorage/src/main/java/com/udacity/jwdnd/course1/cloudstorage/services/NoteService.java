package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private NoteMapper noteMapper;

    public int createNote(Notes note) {
        return noteMapper.insert(note);
    }

    public List<Notes> getAllNotes() {
        return this.noteMapper.getAllNotes();
    }

    public List<Notes> getNotesByUserId(Integer userId) {
        return this.noteMapper.getNotesByUserId(userId);
    }

    public List<Notes> getNoteByNoteId(Integer noteId) {
        return this.noteMapper.getNoteByNoteId(noteId);
    }

    public void deleteNote(Notes note, Integer id) {
        this.noteMapper.delete(id);
    }

    public void editNote(Notes note) {
        this.noteMapper.update(note);
    }
}
