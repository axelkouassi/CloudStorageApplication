package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Files;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {

        this.fileMapper = fileMapper;
    }

    public Integer store(MultipartFile multipartFile, Integer userId) throws IOException {
        Files file = new Files(null, multipartFile.getOriginalFilename(), multipartFile.getContentType(),
                multipartFile.getSize(), userId, multipartFile.getBytes());

        return fileMapper.insert(file);
    }

    public List<Files> getFilesForUser(Integer userId) {

        return fileMapper.getFilesByUserId(userId);
    }

    public Integer uploadFile(Files file) {

        return fileMapper.insert(file);
    }


    public Files getFileByFileId(Integer id) {

        return this.fileMapper.getFileByFileId(id);
    }

    public Files getFileByName(Integer fileId) {

        return this.fileMapper.getFileByFileId(fileId);
    }

    public int deleteFile(Integer fileId, Integer userid) {
        //file.setUserId(userid);
        return fileMapper.delete(fileId);
    }

    public boolean isFilenameAvailable(String fileName, Integer userId) {
        return ((this.fileMapper.getFile(fileName, userId) == null) ? false : true);
    }

}
