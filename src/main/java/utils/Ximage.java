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
    public static boolean save (File src){
            File dst = new File("src\\main\\resources\\image",src.getName());
            if(!dst.getParentFile().exists()){
                dst.getParentFile().mkdirs(); // tạo thư mục nếu k tồn tại
            }try {
            Path from = Paths.get(src.getAbsolutePath());
            Path to = Paths.get(dst.getAbsolutePath());
            Files.copy(from,to,StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
           System.out.println("Không thể lưu hình!"); 
           return false;
        }
    }
    public static ImageIcon read(String fileName){
        File path = new File("src\\main\\resources\\image",fileName);
        ImageIcon originalIcon = new ImageIcon(path.getAbsolutePath());
        Image img = originalIcon.getImage();
        Image resizeImage = img.getScaledInstance(350, 148, Image.SCALE_SMOOTH);
        return new ImageIcon(resizeImage);
    }
}
