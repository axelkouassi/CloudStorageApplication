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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;

@Controller
public class FileController {

    private final FileService fileService;
    private final UserService userService;


    @Autowired
    public FileController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @PostMapping("/home/files")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile file,
                             RedirectAttributes redirectAttributes, Authentication authentication){

        Integer currentUserId = userService.getUser(authentication.getName()).getUserId();

        try {

            //Display error message if no file uploaded
            if (file.isEmpty()) {
                redirectAttributes.addAttribute("error", true);
                redirectAttributes.addAttribute("message", "Please, choose a file to upload!");
                return "redirect:/home";
            }

            //Checking if filename already exists
            if(fileService.isFilenameAvailable(file.getOriginalFilename(),currentUserId)) {
                redirectAttributes.addAttribute("error", true);
                redirectAttributes.addAttribute("message", "File name already exists!");
                return "redirect:/home";
            }

            Integer fileId = fileService.store(file, currentUserId);

            if(fileId > 0){
                redirectAttributes.addAttribute("success", true);
                redirectAttributes.addFlashAttribute("message",
                        "You successfully uploaded " + file.getOriginalFilename() + "!");
            } else{
                redirectAttributes.addAttribute("error", true);
                redirectAttributes.addFlashAttribute("message",
                        "There was an error uploading your file. " + file.getOriginalFilename() + "!");
            }
        } catch (IOException ioException){
            redirectAttributes.addAttribute("error", true);
            redirectAttributes.addFlashAttribute("message",
                    "There was an error uploading your file. " + file.getOriginalFilename() + "!");
        }

        return "redirect:/home";
    }

    @GetMapping("home/files/view/{fileId}")
    public ResponseEntity<Resource> download(@PathVariable("fileId") Integer fileId,
                                             RedirectAttributes redirectAttributes,
                                             Authentication authentication) {

        Integer currentUserId = userService.getUser(authentication.getName()).getUserId();

        Files file = fileService.getFileByFileId(fileId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(httpHeaders.CONTENT_DISPOSITION, "inline; " +
                "filename = " + file.getFileName());
        httpHeaders.add("Cache-control", "no-cache, no-store, must-revalidate");
        httpHeaders.add("Pragma", "no-cache");
        httpHeaders.add("Expires", "0");
        ByteArrayResource resource = new ByteArrayResource(file.getFileData());

        redirectAttributes.addAttribute("success", true);

        redirectAttributes.addFlashAttribute("message",
                "You successfully downloaded " + file.getFileName() + "!");

        return ResponseEntity.ok().
                headers(httpHeaders).
                body(resource);


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


}


