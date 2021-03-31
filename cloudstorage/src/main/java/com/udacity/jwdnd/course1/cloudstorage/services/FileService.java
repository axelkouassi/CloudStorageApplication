package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.services.storage.StorageException;
import com.udacity.jwdnd.course1.cloudstorage.services.storage.StorageFileNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.services.storage.StorageProperties;
import com.udacity.jwdnd.course1.cloudstorage.services.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class FileService implements StorageService {

    private final Path rootLocation;
    //private FileMapper fileMapper;


    @Autowired
    public FileService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());;
    }

//    public FileService(FileMapper fileMapper) {
//        this.fileMapper = fileMapper;
//    }

//    public boolean isFilenameAvailable(String fileName, Integer userId) {
//        return (this.fileMapper.getFile(fileName, userId) == null);
//    }

    // Initialize storage
    @Override
    public void init() {
        try {
            java.nio.file.Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    //Store file
    public void store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            Path destinationFile = this.rootLocation.resolve(
                    Paths.get(file.getOriginalFilename()))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                java.nio.file.Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    //View all files
    @Override
    public Stream<Path> loadAll() {
        try {
            return java.nio.file.Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    //view a file
    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    //Upload a file
    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    //Delete all files
    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    /*public Integer createFile(MultipartFile uploadFile, Integer userid) throws IOException {
        Files file = new Files();
        file.setFileName(uploadFile.getOriginalFilename());
        file.setContentType(uploadFile.getContentType());
        file.setFileSize(String.valueOf(uploadFile.getSize()));
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
    }*/

}
