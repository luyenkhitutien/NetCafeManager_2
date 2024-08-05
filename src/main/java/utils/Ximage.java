/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
        URL url = Ximage.class.getResource("/images/");
        
        File dst = new File(url.getPath(), src.getName());
        if (!dst.getParentFile().exists()) {
            dst.getParentFile().mkdirs(); // tạo thư mục nếu k tồn tại
        }
        try {
            Path from = Paths.get(src.getAbsolutePath());
            Path to = Paths.get(dst.getAbsolutePath());
            
            Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
            return dst.getName();
        } catch (Exception e) {
            System.out.println("Không thể lưu hình!");
            return null;
        }
    }

    public static ImageIcon read(String fileName) {
        URL url = Ximage.class.getResource("/images/" + fileName);
        ImageIcon originalIcon = new ImageIcon(url);
        Image img = originalIcon.getImage();
        Image resizeImage = img.getScaledInstance(350, 148, Image.SCALE_SMOOTH);
        return new ImageIcon(resizeImage);
    }
}
