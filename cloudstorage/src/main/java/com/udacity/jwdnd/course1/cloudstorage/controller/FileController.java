package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.services.storage.StorageFileNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.services.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.stream.Collectors;

@Controller
public class FileController {

    private final StorageService storageService;

    @Autowired
    public FileController(StorageService storageService) {
        this.storageService = storageService;
    }

    /*******************************************************************************
     [GET /]: Looks up the current list of uploaded files from the StorageService and
     loads it into a Thymeleaf template. It calculates a link to the actual resource
     by using [MvcUriComponentsBuilder].
     ********************************************************************************/
    @GetMapping("/home/files")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("fileUpload", storageService.loadAll()
                .map(path -> MvcUriComponentsBuilder.
                        fromMethodName(FileController.class, "serveFile",
                                path.getFileName().toString())
                        .build().toUri().toString())
                .collect(Collectors.toList()));

        return "home";
    }

    /*******************************************************************************
     [GET /files/{filename}]: Loads the resource (if it exists) and sends it to the browser
     to download by using a [Content-Disposition] response header.
     ********************************************************************************/
    @GetMapping("home/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    /******************************************************************************************
     [POST /]: Handles a multi-part message file and gives it to the StorageService for saving.
     *****************************************************************************************/
    @PostMapping("/")
    public String handleFileUpload(@RequestParam("fileUpload") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    /*private final UserService userService;
    private final FileService fileService;

    public FileController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @PostMapping("/home/file")
    public ModelAndView uploadFile(@RequestParam("fileUpload") MultipartFile multipartFile,
                                   Authentication authentication, Model model) {
        if (multipartFile.isEmpty()) {
            model.addAttribute("success", false);
            model.addAttribute("error", true);
            model.addAttribute("message", "Please, choose a file to upload!");
            return new ModelAndView("result");
        }

        User user = this.userService.getUser(authentication.getName());
        Integer userId = user.getUserId();

        if (!fileService.isFilenameAvailable(multipartFile.getOriginalFilename(), userId)) {

            model.addAttribute("success", false);
            model.addAttribute("error", true);
            model.addAttribute("message", "File name already exists!");
            return new ModelAndView("result");
        }

        try {
            fileService.createFile(multipartFile, userId);
            model.addAttribute("success", true);
            model.addAttribute("message", "New file added successfully!");
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Error adding file!" + e.getMessage());
        }
        return new ModelAndView("result");
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

    }*/
}


