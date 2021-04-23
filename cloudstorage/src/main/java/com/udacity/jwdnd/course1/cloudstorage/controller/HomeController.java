package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final FileService fileService;
    private final UserService userService;
    private final NoteService noteService;

    public HomeController(FileService fileService, UserService userService, NoteService noteService) {
        this.fileService = fileService;
        this.userService = userService;
        this.noteService = noteService;
    }

    @GetMapping()
    public String homeView(Model model, Authentication authentication) {

        Integer currentUserId = userService.getUser(authentication.getName()).getUserId();
        model.addAttribute("files", fileService.getFilesForUser(currentUserId));
        model.addAttribute("notes", noteService.getNotesForUser(currentUserId));

        return "home";
    }
}
