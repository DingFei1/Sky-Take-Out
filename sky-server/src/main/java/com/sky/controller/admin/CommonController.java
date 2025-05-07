package com.sky.controller.admin;

import com.sky.result.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Common request controller (Deal with file uploading)
 */
@RestController
@RequestMapping("/admin/common")
public class CommonController {
    /**
     * Upload the file (image)
     *
     * @param file The uploaded file
     * @return Operation results with absolute path of the file stored
     * @throws IOException if file transfer fails
     * @throws IllegalArgumentException if the file is null or does not have the extension
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException, IllegalArgumentException {
        String filePathStored = "D:/Software Development/Sky Take out/sky-take-out-back-end/repository";

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        }
        int lastIndex = originalFilename.lastIndexOf(".");
        if (lastIndex == -1) {
            throw new IllegalArgumentException("The filename should have an extension");
        }

        String extension = originalFilename.substring(lastIndex);
        String newFilename = UUID.randomUUID().toString() + extension;
        File storageFile = new File(filePathStored, newFilename);
        file.transferTo(storageFile);
        return Result.success(storageFile.getAbsolutePath());
    }
}