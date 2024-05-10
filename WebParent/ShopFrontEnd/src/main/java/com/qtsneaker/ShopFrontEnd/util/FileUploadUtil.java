package com.qtsneaker.ShopFrontEnd.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadUtil.class);
    public  static void saveFile(String uploadDir,
                                 String fileName,
                                 MultipartFile multipartFile)
            throws IOException {
        // Tạo Path
        Path uploadpath = Paths.get(uploadDir);

        // kiểm tra xemn  uploadpath đã tồn tại chưa
        if(!Files.exists(uploadpath)){
            // nến chưa thì tạo thư mục
            Files.createDirectories(uploadpath);
        }
        try (InputStream inputStream = multipartFile.getInputStream()){

            // resolve() để kết hợp đường dẫn của thư mục lưu trữ với tên tệp tải lên
            // để tạo ra một đường dẫn hoàn chỉnh đến vị trí lưu trữ của tệp
            Path filePath = uploadpath.resolve(fileName);

            // StandardCopyOption.REPLACE_EXISTING được sử dụng để ghi đè lên tệp nếu tồn tại
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException ex){

            throw new IOException("Could not save file:"+ fileName, ex);
        }
    }

    public static void cleanDir(String dir)  {
        Path dirPatrh = Paths.get(dir);
        try {
            Files.list(dirPatrh).forEach(file ->{
                if(!Files.isDirectory(file)){
                    try{
                        Files.delete(file);
                    }
                    catch (IOException ex){
                        System.out.println("could not delete file: "+file);
                    }
                }
            });
        }
        catch (IOException ex){
            System.out.println("Could not list directory"+dirPatrh);
        }
    }

    public static void removeDir(String dir) {
        cleanDir(dir);

        try {
            Files.delete(Paths.get(dir));
        } catch (IOException e) {
            LOGGER.error("Could not remove directory: " + dir);
        }

    }
}
