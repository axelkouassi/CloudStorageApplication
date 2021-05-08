package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Files;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

import static com.udacity.jwdnd.course1.cloudstorage.constants.Constants.*;

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
                redirectAttributes.addAttribute("message", NO_FILE_SELECTED);
                return "redirect:/home";
            }

            //Checking if filename already exists
            if(fileService.isFilenameAvailable(file.getOriginalFilename(),currentUserId)) {
                redirectAttributes.addAttribute("error", true);
                redirectAttributes.addAttribute("message", EXISTING_FILE);
                return "redirect:/home";
            }

            Integer fileId = fileService.store(file, currentUserId);

            if(fileId > 0){
                redirectAttributes.addAttribute("success", true);
                redirectAttributes.addAttribute("message",
                        SUCCESSFUL_FILE_UPLOAD + file.getOriginalFilename() + "!");
            } else{
                redirectAttributes.addAttribute("error", true);
                redirectAttributes.addAttribute("message",
                        FILE_UPLOAD_ERROR + file.getOriginalFilename() + "!");
            }
        } catch (IOException ioException){
            redirectAttributes.addAttribute("error", true);
            redirectAttributes.addAttribute("message",
                    FILE_UPLOAD_ERROR + file.getOriginalFilename() + "!");
        }

        return "redirect:/home";
    }

    @GetMapping("home/files/view/{fileId}")
    public ResponseEntity viewFile(@PathVariable("fileId") Integer fileId) {

        Files file = fileService.getFileByFileId(fileId);

        String fileName = file.getFileName();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))/*inline*/
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+fileName+"\"")
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


