package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
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
public class CredentialController {

    private final UserService userService;
    private final CredentialService credentialService;

    @Autowired
    public CredentialController(UserService userService, CredentialService credentialService) {
        this.userService = userService;
        this.credentialService = credentialService;
    }

    // Create or edit credentials
    @PostMapping("home/credentials")
    public String createOrEditCredential(@ModelAttribute Credentials credential,
                                RedirectAttributes redirectAttributes, Authentication authentication) {
        Integer currentUserId = userService.getUser(authentication.getName()).getUserId();
        credential.setUserId(currentUserId);

        try {
            if (credential.getCredentialId() == null) {
                credentialService.addCredential(credential);
                redirectAttributes.addAttribute("success", true);
                redirectAttributes.addAttribute("message", NEW_CREDENTIALS +
                        credential.getUrl() + " .");
            } else {
                try{
                    credentialService.editCredential(credential);
                    redirectAttributes.addAttribute("success", true);
                    redirectAttributes.addAttribute("message", CREDENTIALS_EDIT +
                            credential.getUrl() + " have been " + EDITED);

                    return "redirect:/home";

                }catch (Exception e) {
                    redirectAttributes.addAttribute("error", true);
                    redirectAttributes.addAttribute("message", CREDENTIALS_EDIT_ERROR +
                            credential.getUrl() + "!");

                    return "redirect:/home";
                }

            }
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", true);
            redirectAttributes.addAttribute("message", NEW_CREDENTIALS_ERROR +
                    credential.getUrl() + "!");
        }

        return "redirect:/home";
    }

    //Delete Credentials
    @GetMapping("home/credentials/delete/{credentialId}")
    public String deleteNote(@PathVariable("credentialId") Integer credentialId,
                             RedirectAttributes redirectAttributes,
                             Authentication authentication) {

        Integer currentUserId = userService.getUser(authentication.getName()).getUserId();
        String credentialUrl = null;

        try {

            Credentials credentials = credentialService.getCredentialById(credentialId);
            credentialUrl= credentials.getUrl();
            credentialService.deleteCredential(credentialId, currentUserId);
            redirectAttributes.addAttribute("success", true);
            redirectAttributes.addAttribute("message", CREDENTIALS_DELETE_SUCCESS
                    + credentialUrl + " !");

        } catch (Exception e) {

            redirectAttributes.addAttribute("error", true);
            redirectAttributes.addAttribute("message", CREDENTIALS_DELETE_ERROR +
                            credentialUrl + "!");
        }

        return "redirect:/home";

    }
}
