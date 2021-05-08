package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.udacity.jwdnd.course1.cloudstorage.constants.Constants.*;

@Controller
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;

    @Autowired
    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }


    //Create or edit note
    @PostMapping("home/notes")
    public String createNote(@ModelAttribute Notes note,
                             RedirectAttributes redirectAttributes, Authentication authentication) {

        Integer currentUserId = userService.getUser(authentication.getName()).getUserId();
        note.setUserId(currentUserId);

        try {
            if (note.getNoteId() == null) {
                noteService.createNote(note);
                redirectAttributes.addAttribute("success", true);
                redirectAttributes.addAttribute("message", NEW_NOTE +
                        note.getNoteTitle() + CREATED);
            }
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", true);
            redirectAttributes.addAttribute("message", NEW_NOTE_ERROR +
                    note.getNoteTitle() + "!");
        }
        return "redirect:/home";
    }

    //Edit note
    @PostMapping("home/notes/edit")
    public String editNote(@ModelAttribute Notes note,
                           RedirectAttributes redirectAttributes, Authentication authentication) {
        Integer currentUserId = userService.getUser(authentication.getName()).getUserId();
        note.setUserId(currentUserId);
        try {
            noteService.editNote(note);
            redirectAttributes.addAttribute("success", true);
            redirectAttributes.addAttribute("message", NOTE +
                    note.getNoteTitle() + EDITED);
            return "redirect:/home";
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", true);
            redirectAttributes.addAttribute("message", NOTE_EDIT_ERROR +
                    note.getNoteTitle() + "!");
            return "redirect:/home";
        }
    }



    //Delete note
    @GetMapping("home/notes/delete/{noteId}")
    public String deleteNote(@PathVariable("noteId") Integer noteId,
                             RedirectAttributes redirectAttributes,
                             Authentication authentication) {

        Integer currentUserId = userService.getUser(authentication.getName()).getUserId();
        String noteTitle = null;

        try {

            Notes note = noteService.getNoteByNoteId(noteId);
            noteTitle = note.getNoteTitle();
            noteService.deleteNote(noteId, currentUserId);
            redirectAttributes.addAttribute("success", true);
            redirectAttributes.addAttribute("message", NOTE_DELETE_SUCCESS + noteTitle + " !");

        } catch (Exception e) {

            redirectAttributes.addAttribute("error", true);
            redirectAttributes.addAttribute("message",
                    NOTE_DELETE_ERROR +
                            noteTitle + "!");
        }

        return "redirect:/home";

    }
}
