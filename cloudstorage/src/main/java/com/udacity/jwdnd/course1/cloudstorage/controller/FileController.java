package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Files;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
                redirectAttributes.addAttribute("message",
                        "You successfully uploaded " + file.getOriginalFilename() + "!");
            } else{
                redirectAttributes.addAttribute("error", true);
                redirectAttributes.addAttribute("message",
                        "There was an error uploading your file. " + file.getOriginalFilename() + "!");
            }
        } catch (IOException ioException){
            redirectAttributes.addAttribute("error", true);
            redirectAttributes.addAttribute("message",
                    "There was an error uploading your file. " + file.getOriginalFilename() + "!");
        }

        return "redirect:/home";
    }

    @GetMapping("home/files/view/{fileId}")
    public ResponseEntity viewFile(@PathVariable("fileId") Integer fileId) {

        Files file = fileService.getFileByFileId(fileId);

        String fileName = file.getFileName();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\""+fileName+"\"")
                .body(file.getFileData());

    }





    @GetMapping("home/files/delete/{fileId}")
    public String deleteFile(@PathVariable("fileId") Integer fileId,
                                   RedirectAttributes redirectAttributes,
                                   Authentication authentication) {

        Integer currentUserId = userService.getUser(authentication.getName()).getUserId();
        String fileName = null;

        try {

            Files file = fileService.getFileByFileId(fileId);
            fileName = file.getFileName();
            fileService.deleteFile(fileId, currentUserId);
            redirectAttributes.addAttribute("success", true);
            redirectAttributes.addAttribute("message", "You successfully deleted " +
                     fileName + " !");

        } catch (Exception e) {

            redirectAttributes.addAttribute("error", true);
            redirectAttributes.addAttribute("message",
                            "There was an error deleting your file " +
                                    fileName + "!");
        }

        return "redirect:/home";

    }


}


