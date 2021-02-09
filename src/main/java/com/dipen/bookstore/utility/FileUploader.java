package com.dipen.bookstore.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.dipen.bookstore.utility.exception.EmptyFileException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Component
public class FileUploader {

    public String upload(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
           throw new EmptyFileException("File Cannot be empty");
        }

        try {
            Path currentPath = Paths.get(".");
            Path absolutePath = currentPath.toAbsolutePath();
            String fileName = new Date().toInstant().toString() + "." + file.getContentType().split("/")[1];
            String filePath = absolutePath + "/src/main/resources/static/bookImages/" + fileName;
            byte[] bytes = file.getBytes();
            Path path = Paths.get(filePath);
            Files.write(path, bytes);

            return fileName;

        } catch (IOException e) {
            throw e;
        }
    }
}
