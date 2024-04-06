package com.tuanvuong.qtsnearker.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Tên thư mục chứa ảnh
        String dirName ="user-photos";

        // Tạo path đại diện cho đường dẫn thư mục
        Path userPhotosdir = Paths.get(dirName);

        // Lấy đường dẫn của thư mục user-photos lưu vào biến userPhotosPath
        String userPhotosPath = userPhotosdir.toFile().getAbsolutePath();

        // Đăng ký một resource handler cho các yêu cầu với URL pattern bắt đầu bằng /user-photos/.
        // Khi có yêu cầu với URL tương ứng, Spring sẽ xử lý nó bằng cách tìm kiếm tệp trong thư mục user-photos.
        registry.addResourceHandler("/"+dirName+"/**")
                // xác định vị trí tệp
                .addResourceLocations("file:/"+userPhotosPath +"/");
    }
}
