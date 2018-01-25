package qrcode;


import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;

import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import jp.sourceforge.qrcode.exception.DecodingFailedException;

@Controller
public class ZxingDecodeHandler {

	@RequestMapping("/zxingdecode")
	public String decode(@RequestParam(value = "qrUrl", required = true) String qrUrl) {

		String forwardUrl;
		
		System.out.println("QRCode URL:" + qrUrl);
		
		forwardUrl = decodeQRCode(qrUrl);

		return "redirect:" + forwardUrl;

	}

	private String decodeQRCode(String qrUrl) {

		URL imageURL;
		BufferedImage bufImg = null;

		Result result = null;

		MultiFormatReader formatReader = new MultiFormatReader();

		try {
			imageURL = new URL(qrUrl);

			bufImg = ImageIO.read(imageURL);
			
			// 对图像进行去噪处理，提高识别率
			bufImg = ImageUtil.cleanImage(bufImg);
			
			System.out.println("image width: " + bufImg.getWidth()+ " image width: " + bufImg.getHeight() );
			
			LuminanceSource source = new ZxingBufferedImageLuminanceSource(bufImg);
			Binarizer binarizer = new HybridBinarizer(source);
			BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);

			System.out.println("begin parse: " + imageURL.toString());
			
			Map hints = new HashMap();
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			// 对降低失败率没有影响
//			hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
//			hints.put(DecodeHintType.POSSIBLE_FORMATS,
//			        EnumSet.allOf(BarcodeFormat.class));
//			hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);

			result = formatReader.decode(binaryBitmap, hints);

			System.out.println("解析结果 = " + result.toString());
			System.out.println("二维码格式类型 = " + result.getBarcodeFormat());
			System.out.println("二维码文本内容 = " + result.getText());
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (DecodingFailedException dfe) {
			System.out.println("Error: " + dfe.getMessage());
			dfe.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result.toString();
	}

	
	
}
