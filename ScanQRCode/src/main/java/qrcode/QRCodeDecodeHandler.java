package qrcode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.exception.DecodingFailedException;

//@RestController
@Controller

public class QRCodeDecodeHandler {

	@RequestMapping("/decode")
    public String decode(@RequestParam(value="qrUrl", required=true) String qrUrl) {
        
		String forwardUrl;
		
		forwardUrl = decodeQRCode(qrUrl);
		System.out.println("-----URL----:"+forwardUrl+"-----URL----:");
		
		//return forwardUrl;
		
		return "redirect:"+forwardUrl;
		
	//	 return new ModelAndView(forwardUrl);
    }
	
	
	private String decodeQRCode(String qrUrl)  { 
		 
		
		  //System.out.println("Info1:"+qrUrl);
		  // qrUrl = qrUrl.replace("\\", "/");  
	
		  // System.out.println("Info2:"+qrUrl);
		   
		   
		  // File imageFile = new File(qrUrl); 
	 
		  URL imageURL;
		   BufferedImage bufImg = null; 
	       String decodedData = null; 
	    
	       
		try {
			imageURL = new URL(qrUrl);
		
	     
	           bufImg = ImageIO.read(imageURL); 
	 
	           QRCodeDecoder decoder = new QRCodeDecoder(); 
	           decodedData = new String(decoder.decode(new QRCodeImageHandler(bufImg))); 
	 
//	            try { 
//	            System.out.println(new String(decodedData.getBytes("gb2312"),"UTF8")); 
//	            } catch (Exception e) { 
//	            // TODO: handle exception 
//	            } 
	           
	 	       } catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
	       } catch (DecodingFailedException dfe) { 
	           System.out.println("Error: " + dfe.getMessage()); 
	           dfe.printStackTrace(); 
	       } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	       return decodedData; 
	    } 
}
