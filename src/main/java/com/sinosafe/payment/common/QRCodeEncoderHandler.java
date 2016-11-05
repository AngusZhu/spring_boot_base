package com.sinosafe.payment.common;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.swetake.util.Qrcode;


/**
 * 生成二维码并以流的形式输出 参见http://blog.csdn.net/huakaihualuo1223/article/details/7910232
 */
public class QRCodeEncoderHandler {

	/** 字符集 */
	private String charset = "UTF-8";
	private String imageType = "png";// 图片类型
	private int qrcode_version  = 11;// 二维码尺寸
	private int logo_width  = 40;// LOGO宽度
	private int logo_height  = 40;// LOGO高度

	private static String SECURITY_KEY = "^s5.8%3#4@k!s-0=9+\\|~u/$";// 加密密钥,用于加密二维码对应的参数

	public QRCodeEncoderHandler() {
		super();
	}

	/**
	 * @param charset
	 *            字符集，比如UTF-8
	 * @param imageType
	 *            图片类型,比如png
	 * @param qrcode_size
	 *            二维码尺寸
	 */
	public QRCodeEncoderHandler(String charset, String imageType,
			int qrcode_size) {
		super();
		this.charset = charset;
		this.imageType = imageType;
		this.qrcode_version = qrcode_size;
	}

	public QRCodeEncoderHandler(String charset, String imageType,
			int qrcode_size, int logo_width, int logo_height) {
		super();
		this.charset = charset;
		this.imageType = imageType;
		this.qrcode_version = qrcode_size;
		this.logo_width = logo_width;
		this.logo_height = logo_height;
	}

	/**
	 * 二维码参数加密方法
	 * 
	 * @param src
	 *            要加密的字符串
	 * @return 加密后的字符串
	 * @since 1.0.0
	 */
	public static String encryptContent(String src) {
		return DES.des_encrypt(SECURITY_KEY, src);// 加密
		// return DESedeUtils.encrypt(data);// 加密
	}

	/**
	 * 二维码参数解密方法
	 * 
	 * @param src
	 *            加密后的字符串
	 * @return 解密后的字符串
	 * @since 1.0.0
	 */
	public static String decryptContent(String src) {
		return DES.des_descrypt(SECURITY_KEY, src);// 解密
		// return DESedeUtils.decrypt(data);// 解密
	}

	/**
	 * 
	 * Java制作二维码代码，中间带logo图片，可设置logo大小
	 * 参见：http://blog.csdn.net/maomao092092/article/details/29572459
	 * 
	 * @param content
	 *            要生成二维码的文本内容
	 * @param logo
	 *            中间logo的路径
	 * @param os
	 *            图片输出流
	 * @return 返回0表示成功；-1表示content的长度太长导致无法生成；-100 表示其他异常
	 */
	public int createQRCode(String content, String logo, OutputStream os) {
		try {
			int imgSize = 67 + 12 * (this.qrcode_version - 1);// 图片尺寸
			// System.out.println("图片尺寸:"+imgSize);

			Qrcode qrcodeHandler = new Qrcode();
			// 设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小
			qrcodeHandler.setQrcodeErrorCorrect('M');
			qrcodeHandler.setQrcodeEncodeMode('B');

			// 设置设置二维码尺寸，取值范围1-40，值越大尺寸越大，可存储的信息越大
			qrcodeHandler.setQrcodeVersion(this.qrcode_version);

			// 获得内容的字节数组，设置编码格式
			byte[] contentBytes = content.getBytes(this.charset);

			// 构造一个BufferedImage对象 设置宽、高
			BufferedImage bufImg = new BufferedImage(imgSize, imgSize,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D gs = bufImg.createGraphics();

			gs.setBackground(Color.WHITE);
			gs.clearRect(0, 0, imgSize, imgSize);
			// 设定图像颜色 > BLACK
			gs.setColor(Color.BLACK);

			// 设置偏移量 不设置可能导致解析出错
			int pixoff = 2;
			// 输出内容 > 二维码
			if (contentBytes.length > 0 && contentBytes.length < 800) {
				boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
				for (int i = 0; i < codeOut.length; i++) {
					for (int j = 0; j < codeOut.length; j++) {
						if (codeOut[j][i]) {
							gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
						}
					}
				}
			} else {
				System.err.println("QRCode content bytes length = "
						+ contentBytes.length + " not in [ 800 ]. ");
				return -1;
			}
			gs.dispose();
			
			if (logo != null && !"".equals(logo)) {
				this.insertImage(bufImg, logo, true);//插入LOGO
			}
			bufImg.flush();

			ImageIO.write(bufImg, this.imageType, os);
		} catch (Exception e) {
			e.printStackTrace();
			return -100;
		}
		return 0;
	}
	
	/** 
     * 插入LOGO 
     *  
     * @param source 
     *            二维码图片 
     * @param imgPath 
     *            LOGO图片地址 
     * @param needCompress 
     *            是否压缩 
     * @throws Exception 
     */  
    private void insertImage(BufferedImage source, String imgPath,  
            boolean needCompress) throws Exception {  
    	int imgSize = 67 + 12 * (this.qrcode_version - 1);// 二维码图片尺寸
    	
        File file = new File(imgPath);  
        if (!file.exists()) {  
            System.err.println(""+imgPath+"   该文件不存在！");  
            return;  
        }  
        Image src = ImageIO.read(new File(imgPath));  
        int width = src.getWidth(null);  
        int height = src.getHeight(null);  
        if (needCompress) { // 压缩LOGO  
            if (width > this.logo_width) {  
                width = this.logo_width;  
            }  
            if (height > this.logo_height) {  
                height = this.logo_height;  
            }  
            Image image = src.getScaledInstance(width, height,  
                    Image.SCALE_SMOOTH);  
            BufferedImage tag = new BufferedImage(width, height,  
                    BufferedImage.TYPE_INT_RGB);  
            Graphics g = tag.getGraphics();  
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图  
            g.dispose();  
            src = image;  
        }  
        // 插入LOGO  
        Graphics2D graph = source.createGraphics();  
        int x = (imgSize - width) / 2;  
        int y = (imgSize - height) / 2;  
        graph.drawImage(src, x, y, width, height, null);  
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);  
        graph.setStroke(new BasicStroke(3f));  
        graph.draw(shape);  
        graph.dispose();  
    }  

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        String xx = encryptContent("xx");
        System.out.println(xx);
//		String content = "weixin://wxpay/bizpayurl?pr=yiT0kj0";
//		String imgPath = "D:/qrcode_test.png";
//		// 生成二维码QRCode图片
//		File imgFile = new File(imgPath);
//		FileOutputStream fos = null;
//		try {
//			fos = new FileOutputStream(imgFile);
//
//			QRCodeEncoderHandler q = new QRCodeEncoderHandler("UTF-8", "png", 11);
//			// 添加轮胎天使的logo
//			q.createQRCode(content, "",
//					fos);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (fos != null)
//					fos.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	}


}
