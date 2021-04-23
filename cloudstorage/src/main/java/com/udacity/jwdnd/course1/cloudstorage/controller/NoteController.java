package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Files;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;

    @Autowired
    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping("home/notes")
    public String createNote(@ModelAttribute Notes note,
                             RedirectAttributes redirectAttributes, Authentication authentication) {

        Integer currentUserId = userService.getUser(authentication.getName()).getUserId();
        note.setUserId(currentUserId);

        try {
            if (note.getNoteId() == null) {
                noteService.createNote(note);
                redirectAttributes.addAttribute("success", true);
                redirectAttributes.addAttribute("message", "New note " +
                        note.getNoteTitle() + " created!");
            }
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", true);
            redirectAttributes.addAttribute("message", "Error creating note " +
                    note.getNoteTitle() + "!");
        }
        return "redirect:/home";
    }


    @GetMapping("home/notes/delete/{noteId}")
    public String deleteFile(@PathVariable("noteId") Integer noteId,
                             RedirectAttributes redirectAttributes,
                             Authentication authentication) {

        Integer currentUserId = userService.getUser(authentication.getName()).getUserId();
        String noteTitle = null;

        try {

            Notes note = noteService.getNoteByNoteId(noteId);
            noteTitle = note.getNoteTitle();
            noteService.deleteNote(noteId, currentUserId);
            redirectAttributes.addAttribute("success", true);
            redirectAttributes.addAttribute("message", "You successfully deleted " + noteTitle + " !");

        } catch (Exception e) {

            redirectAttributes.addAttribute("error", true);
            redirectAttributes.addAttribute("message",
                    "There was an error deleting your file " +
                            noteTitle + "!");
        }

        return "redirect:/home";

    }
}
