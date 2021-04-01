package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Files;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {

        this.fileMapper = fileMapper;
    }

    public boolean isFilenameAvailable(String fileName, Integer userId) {
        return ((this.fileMapper.getFile(fileName, userId) == null) ? false : true);
    }

    public Integer createFile(MultipartFile uploadFile, Integer userid) throws IOException {
        Files file = new Files();
        file.setFileName(uploadFile.getOriginalFilename());
        file.setContentType(uploadFile.getContentType());
        file.setFileSize(uploadFile.getSize());
        file.setUserId(userid);
        file.setFileData(uploadFile.getBytes());

        return fileMapper.insert(file);
    }

    public Integer uploadFile(Files file) {

        return fileMapper.insert(file);
    }

    public List<Files> getFilesByUserId(Integer id) {

        return this.fileMapper.getFilesByUserId(id);
    }

    public Files getFileByFileId(Integer id) {

        return this.fileMapper.getFileByFileId(id);
    }

    public int deleteFile(Files file, Integer userid) {
        file.setUserId(userid);
        return fileMapper.delete(file);
    }

}
