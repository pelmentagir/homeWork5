package services;

import models.PhotoFile;
import repositories.interfaces.FileRepository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class FileService {
    private FileRepository fileRepository;

    public FileService(FileRepository filesRepository) {
        this.fileRepository = filesRepository;
    }

    public void uploadFile(Long recipeId, InputStream file, String originalFileName, String contentType, Long size) {

        String storageFileName = UUID.randomUUID().toString();
        System.out.println(storageFileName);
        try {
            Path destinationPath = Paths.get("/Users/tagirfajrusin/photos/" + storageFileName);
            Files.copy(file, destinationPath);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error saving file", e);
        }

        PhotoFile photoFileEntity = PhotoFile.builder()
                .recipeId(recipeId)
                .originalFileName(originalFileName)
                .storageFileName(storageFileName)
                .type(contentType)
                .size(size)
                .build();

        fileRepository.save(photoFileEntity);
    }

    public List<PhotoFile> getFilesByRecipeId(Long recipeId) {
        return fileRepository.findByRecipeId(recipeId);
    }
}