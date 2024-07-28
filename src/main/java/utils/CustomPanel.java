/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
/**
 *
 * @author Admin
 */

public class CustomPanel extends JPanel {
    private int radius = 30;
    private float transparency = 0.5f;
    private boolean blur = true;
    private BufferedImage backgroundImage;

    public CustomPanel() {
        setOpaque(false);
    }

    public void setBackgroundImage(BufferedImage backgroundImage) {
        this.backgroundImage = backgroundImage;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (backgroundImage != null && blur) {
            BufferedImage blurredImage = blurImage(backgroundImage);
            g2.drawImage(blurredImage, 0, 0, getWidth(), getHeight(), null);
        }

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));

        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getForeground());
        g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
        g2.dispose();
    }

    private BufferedImage blurImage(BufferedImage image) {
        int radius = 10;
        int size = radius * 2 + 1;
        float[] data = new float[size * size];

        float sigma = radius / 3.0f;
        float twoSigmaSquare = 2.0f * sigma * sigma;
        float sigmaRoot = (float) Math.sqrt(twoSigmaSquare * Math.PI);
        float total = 0.0f;

        for (int y = -radius; y <= radius; y++) {
            for (int x = -radius; x <= radius; x++) {
                float distance = x * x + y * y;
                int index = (y + radius) * size + (x + radius);
                data[index] = (float) Math.exp(-distance / twoSigmaSquare) / sigmaRoot;
                total += data[index];
            }
        }

        for (int i = 0; i < data.length; i++) {
            data[i] /= total;
        }

        Kernel kernel = new Kernel(size, size, data);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        return op.filter(image, null);
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        repaint();
    }

    public float getTransparency() {
        return transparency;
    }

    public void setTransparency(float transparency) {
        this.transparency = transparency;
        repaint();
    }

    public boolean isBlur() {
        return blur;
    }

    public void setBlur(boolean blur) {
        this.blur = blur;
        repaint();
    }
}


