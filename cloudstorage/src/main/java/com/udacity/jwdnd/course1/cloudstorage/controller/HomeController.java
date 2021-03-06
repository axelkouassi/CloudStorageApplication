package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final CredentialService credentialService;

    @Autowired
    EncryptionService encryptionService;

    public HomeController(FileService fileService, UserService userService, NoteService noteService,
                          CredentialService credentialService/*, EncryptionService encryptionService*/) {
        this.fileService = fileService;
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        //this.encryptionService = encryptionService;
    }

    @GetMapping()
    public String homeView(Model model, Authentication authentication) {

        Integer currentUserId = userService.getUser(authentication.getName()).getUserId();
        //String currentUserPassword = encryptionService.decryptValue(authentication.getCredentials(),);
        model.addAttribute("files", fileService.getFilesForUser(currentUserId));
        model.addAttribute("notes", noteService.getNotesForUser(currentUserId));
        model.addAttribute("credentials", credentialService.getCredentialsForUser(currentUserId));
        model.addAttribute("encryptionService", encryptionService);

        return "home";
    }
}
