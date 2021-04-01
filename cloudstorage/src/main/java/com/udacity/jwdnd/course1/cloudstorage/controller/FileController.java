package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Files;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.stream.Collectors;

@Controller
public class FileController {

    private final UserService userService;
    private final FileService fileService;

    public FileController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @PostMapping("/home/file")
    public ModelAndView uploadFile(@RequestParam("fileUpload") MultipartFile multipartFile,
                                   Authentication authentication, Model model) throws IOException{

        User user = this.userService.getUser(authentication.getName());
        Integer userId = user.getUserId();

        //Display error message if no file uploaded
        if (multipartFile.isEmpty()) {
            model.addAttribute("success", false);
            model.addAttribute("error", true);
            model.addAttribute("message", "Please, choose a file to upload!");
            return new ModelAndView("home");
        }

        try {
            fileService.createFile(multipartFile, userId);
            model.addAttribute("success", true);
            model.addAttribute("message", "New file added successfully!");
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Error adding file!" + e.getMessage());
        }
        return new ModelAndView("home");
    }

    @PostMapping("home/file/delete")
    public ModelAndView deleteFile(@ModelAttribute Files fileDelete, Authentication authentication, Model model) {
        User user = this.userService.getUser(authentication.getName());
        Integer userId = user.getUserId();
        try {
            fileService.deleteFile(fileDelete, userId);
            model.addAttribute("success", true);
            model.addAttribute("message", "file Deleted!");
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Error deleting file!" + e.getMessage());
        }
        return new ModelAndView("result");

    }

    @GetMapping("home/file/download/{fileId}")
    public ResponseEntity<Resource> download(@PathVariable("fileId") Integer fileId) {
        Files file = fileService.getFileByFileId(fileId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(httpHeaders.CONTENT_DISPOSITION, "attachment; filename = " + file.getFileName());
        httpHeaders.add("Cache-control", "no-cache, no-store, must-revalidate");
        httpHeaders.add("Pragma", "no-cache");
        httpHeaders.add("Expires", "0");
        ByteArrayResource resource = new ByteArrayResource(file.getFileData());

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(resource);

    }
}


