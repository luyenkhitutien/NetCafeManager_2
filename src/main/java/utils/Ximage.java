/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author doanc
 */
public class Ximage {
    public static Image getAppIdon(){
        URL url = Ximage.class.getResource("/image/fptimage.png");
        return new ImageIcon(url).getImage();
    }

    public static String save(File src) {
        // Sử dụng đường dẫn tương đối từ thư mục gốc của dự án
        String resourcesPath = "src/main/resources/images";
        File dst = new File(resourcesPath, src.getName());
        
        // Tạo thư mục nếu không tồn tại
        if (!dst.getParentFile().exists()) {
            dst.getParentFile().mkdirs();
        }

        try {
            Path from = Paths.get(src.getAbsolutePath());
            Path to = Paths.get(dst.getAbsolutePath());
            Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
            return dst.getName();
        } catch (IOException e) {
            System.out.println("Không thể lưu hình!");
            e.printStackTrace();
            return null;
        }
    }

    public static ImageIcon read(String fileName) {
        try {
            URL url = Ximage.class.getClassLoader().getResource("images/" + fileName);
            if (url != null) {
                Image img = ImageIO.read(url);
                Image resizedImage = img.getScaledInstance(350, 148, Image.SCALE_SMOOTH);
                return new ImageIcon(resizedImage);
            } else {
                throw new IllegalArgumentException("Resource not found: images/" + fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
