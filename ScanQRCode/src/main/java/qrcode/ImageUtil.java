package qrcode;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;

public class ImageUtil {

	public static BufferedImage toBufferedImage(Image image) {  
        if (image instanceof BufferedImage) {  
            return (BufferedImage)image;  
         }  
        
        // This code ensures that all the pixels in the image are loaded  
         image = new ImageIcon(image).getImage();  
        
        // Determine if the image has transparent pixels; for this method's  
        // implementation, see e661 Determining If an Image Has Transparent Pixels  
        //boolean hasAlpha = hasAlpha(image);  
        
        // Create a buffered image with a format that's compatible with the screen  
         BufferedImage bimage = null;  
         GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();  
        try {  
            // Determine the type of transparency of the new buffered image  
            int transparency = Transparency.OPAQUE;  
           /* if (hasAlpha) { 
             transparency = Transparency.BITMASK; 
             }*/  
        
            // Create the buffered image  
             GraphicsDevice gs = ge.getDefaultScreenDevice();  
             GraphicsConfiguration gc = gs.getDefaultConfiguration();  
             bimage = gc.createCompatibleImage(  
             image.getWidth(null), image.getHeight(null), transparency);  
         } catch (HeadlessException e) {  
            // The system does not have a screen  
         }  
        
        if (bimage == null) {  
            // Create a buffered image using the default color model  
            int type = BufferedImage.TYPE_INT_RGB;  
            //int type = BufferedImage.TYPE_3BYTE_BGR;//by wang  
            /*if (hasAlpha) { 
             type = BufferedImage.TYPE_INT_ARGB; 
             }*/  
             bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);  
         }  
        
        // Copy image to buffered image  
         Graphics g = bimage.createGraphics();  
        
        // Paint the image onto the buffered image  
         g.drawImage(image, 0, 0, null);  
         g.dispose();  
        
        return bimage;  
    }  
	
	
	public static BufferedImage cleanImage(BufferedImage sbufferedImage)throws IOException{  
       
        int h = sbufferedImage.getHeight();  
        int w = sbufferedImage.getWidth();  
  
        // 灰度化  
        int[][] gray = new int[w][h];  
        for (int x = 0; x < w; x++){  
            for (int y = 0; y < h; y++){  
                int argb = sbufferedImage.getRGB(x, y);  
                // 图像加亮（调整亮度识别率非常高）  
                int r = (int) (((argb >> 16) & 0xFF) * 1.1 + 30);  
                int g = (int) (((argb >> 8) & 0xFF) * 1.1 + 30);  
                int b = (int) (((argb >> 0) & 0xFF) * 1.1 + 30);  
                if (r >= 255){  
                    r = 255;  
                }  
                if (g >= 255){  
                    g = 255;  
                }  
                if (b >= 255){  
                    b = 255;  
                }  
                gray[x][y] = (int) Math.pow((Math.pow(r, 2.2) * 0.2973 + Math.pow(g, 2.2)* 0.6274 + Math.pow(b, 2.2) * 0.0753), 1 / 2.2);  
            }  
        }  
  
        // 二值化  
        int threshold = ostu(gray, w, h);  
        BufferedImage binaryBufferedImage = new BufferedImage(w, h,BufferedImage.TYPE_BYTE_BINARY);  
        for (int x = 0; x < w; x++){  
            for (int y = 0; y < h; y++){  
                if (gray[x][y] > threshold){  
                    gray[x][y] |= 0x00FFFF;  
                } else{  
                    gray[x][y] &= 0xFF0000;  
                }  
                binaryBufferedImage.setRGB(x, y, gray[x][y]);  
            }  
        }  
  
        // 矩阵打印  
        for (int y = 0; y < h; y++){  
            for (int x = 0; x < w; x++){  
                if (isBlack(binaryBufferedImage.getRGB(x, y))){  
                    System.out.print("*");  
                } else{  
                    System.out.print(" ");  
                }  
            }  
            System.out.println();  
        }  
        
       return binaryBufferedImage;
    }  
	
	
	
	public static int ostu(int[][] gray, int w, int h){  
        int[] histData = new int[w * h];  
        // Calculate histogram  
        for (int x = 0; x < w; x++){  
            for (int y = 0; y < h; y++){  
                int red = 0xFF & gray[x][y];  
                histData[red]++;  
            }  
        }  
  
        // Total number of pixels  
        int total = w * h;  
  
        float sum = 0;  
        for (int t = 0; t < 256; t++)  
            sum += t * histData[t];  
  
        float sumB = 0;  
        int wB = 0;  
        int wF = 0;  
  
        float varMax = 0;  
        int threshold = 0;  
  
        for (int t = 0; t < 256; t++){  
            wB += histData[t]; // Weight Background  
            if (wB == 0)  
                continue;  
  
            wF = total - wB; // Weight Foreground  
            if (wF == 0)  
                break;  
  
            sumB += (float) (t * histData[t]);  
  
            float mB = sumB / wB; // Mean Background  
            float mF = (sum - sumB) / wF; // Mean Foreground  
  
            // Calculate Between Class Variance  
            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);  
  
            // Check if new maximum found  
            if (varBetween > varMax){  
                varMax = varBetween;  
                threshold = t;  
            }  
        }  
  
        return threshold;  
    }  
	
	
	public static boolean isBlack(int colorInt){  
        Color color = new Color(colorInt);  
        if (color.getRed() + color.getGreen() + color.getBlue() <= 300){  
            return true;  
        }  
        return false;  
    }  
}
