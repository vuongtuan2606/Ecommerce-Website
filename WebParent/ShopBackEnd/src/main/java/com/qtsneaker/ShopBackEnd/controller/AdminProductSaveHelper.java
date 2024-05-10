package com.qtsneaker.ShopBackEnd.controller;

import com.qtsneaker.ShopBackEnd.util.FileUploadUtil;
import com.qtsneaker.common.entity.Product;
import com.qtsneaker.common.entity.ProductImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class AdminProductSaveHelper {
    // Ghi thông tin, cảnh báo và lỗi trong lớp AdminProductController.
     static final Logger LOGGER = LoggerFactory.getLogger(AdminProductSaveHelper.class);
    static void saveUploadedImages(MultipartFile mainImageMultipart,
                                    MultipartFile[] extraImageMultipart, Product savedProduct) throws IOException {
        if (!mainImageMultipart.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
            String uploadDir = "../product-images/" + savedProduct.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
        }

        if (extraImageMultipart.length > 0) {
            String uploadDir = "../product-images/" + savedProduct.getId() + "/extras";

            for (MultipartFile multipartFile : extraImageMultipart) {
                if (multipartFile.isEmpty()) continue;

                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            }
        }
    }

    static void setMainImageName(MultipartFile mainImageMultipart, Product product) {

        // Kiểm tra xem mainImage đã được tải lên hay chưa
        if (!mainImageMultipart.isEmpty()) {

            // Lấy tên tệp gốc của ảnh chính và loại bỏ các ký tự đặc biệt
            String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());

            product.setMainImage(fileName);
        }
    }

    static void setExistingExtraImageNames(String[] imageIDs, String[] imageNames,
                                            Product product) {

        // Kiểm tra xem ExtraImage đã tồn tại hay không
        if (imageIDs == null || imageIDs.length == 0) return;

        // Khởi tạo một Set chứa các đối tượng ProductImage đại diện cho ExtraImage
        Set<ProductImage> images = new HashSet<>();

        for (int count = 0; count < imageIDs.length; count++) {
            // Lấy ID và name ExtraImage
            Integer id = Integer.parseInt(imageIDs[count]);
            String name = imageNames[count];

            // Tạo một đối tượng ProductImage và thêm vào Set
            images.add(new ProductImage(id, name, product));
        }

        product.setImages(images);

    }
    static void setNewExtraImageNames(MultipartFile[] extraImageMultipart, Product product) {
        // Kiểm tra xem có extraImage mới được tải lên hay không
        if (extraImageMultipart.length > 0) {

            // Duyệt qua mảng các đối tượng MultipartFile
            for (MultipartFile multipartFile : extraImageMultipart) {

                // Kiểm tra xem file extraImage có được tải lên không
                if (!multipartFile.isEmpty()) {

                    // Lấy tên tệp gốc của ảnh phụ và loại bỏ các ký tự đặc biệt
                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

                    // Kiểm tra xem sản phẩm đã chứa tên của extraImage chưa
                    if (!product.containsImageName(fileName)) {
                        // Nếu chưa, thêm tên extraImage vào sản phẩm
                        product.addExtraImages(fileName);
                    }
                }
            }
        }
    }

    static void deleteExtraImagesWereRemovedOnForm(Product product) {
        // Đường dẫn đến thư mục chứa
        String extraImageDir = "../product-images/" + product.getId() + "/extras";

        // Tạo đối tượng Path đại diện cho đường dẫn của thư mục
        Path dirPath = Paths.get(extraImageDir);

        try {

            // Liệt kê tất cả các tệp trong thư mục
            Files.list(dirPath).forEach(file -> {

                // Lấy tên của tệp
                String filename = file.toFile().getName();

                // Kiểm tra xem tên của extraImage có tồn tại trong product hay không
                if (!product.containsImageName(filename)) {
                    try {
                        // Nếu không tồn tại, xóa extraImage khỏi thư mục lưu trữ
                        Files.delete(file);
                        LOGGER.info("Deleted extra image: " + filename);

                    } catch (IOException e) {
                        LOGGER.error("Could not delete extra image: " + filename);
                    }
                }

            });
        } catch (IOException ex) {
            LOGGER.error("Could not list directory: " + dirPath);
        }
    }
}
