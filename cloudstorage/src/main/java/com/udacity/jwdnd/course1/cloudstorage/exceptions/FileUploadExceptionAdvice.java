package com.udacity.jwdnd.course1.cloudstorage.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.udacity.jwdnd.course1.cloudstorage.constants.Constants.FILE_SIZE_LIMIT_EXCEEDED;

@ControllerAdvice
public class FileUploadExceptionAdvice {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(MaxUploadSizeExceededException exc, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("error", true);
        redirectAttributes.addAttribute("message", FILE_SIZE_LIMIT_EXCEEDED);
        return "redirect:/home";
    }
}
