package com.sky.controller.admin;

import com.sky.result.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
public class CommonController {
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        int lastIndex = originalFilename.lastIndexOf(".");
        String extension = originalFilename.substring(lastIndex);
        String newFilename = UUID.randomUUID().toString() + extension;
        File storageFile = new File("D:/Software Development/Sky Take out/sky-take-out-back-end/repository", newFilename);
        file.transferTo(storageFile);
        return Result.success(storageFile.getAbsolutePath());
    }
}
