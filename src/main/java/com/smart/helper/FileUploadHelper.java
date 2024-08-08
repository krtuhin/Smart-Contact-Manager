package com.smart.helper;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadHelper {

    public static boolean uploadFile(MultipartFile file) {

        boolean f = false;

        try {

            // fetch dynamic folder path
            String path = new ClassPathResource("static").getFile().getAbsolutePath() +
                    File.separator + "img" + File.separator + "contacts" +
                    File.separator + file.getOriginalFilename();

            // save file into server
            Files.copy(file.getInputStream(), Paths.get(path), StandardCopyOption.REPLACE_EXISTING);

            f = true;

        } catch (IOException e) {

            e.printStackTrace();
        }
        return f;
    }

    // method for delete file
    public static boolean deleteFile(String fileName) {

        boolean f = false;

        // if the file is not saved then cannot be deleted
        if (fileName.trim().contains("default.png")) {
            return true;
        }

        // file deleting process
        try {
            // path for delete file
            String path = new ClassPathResource("static").getFile().getAbsolutePath() +
                    File.separator + "img" + File.separator + "contacts" +
                    File.separator + fileName;

            // delete photo from server
            Files.delete(Paths.get(path));

            f = true;

        } catch (IOException e) {

            e.printStackTrace();
        }
        return f;
    }
}
