package cn.speedpay.s.xedj.utils;


import android.graphics.Bitmap;
import android.text.TextUtils;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import cn.speedpay.s.xedj.bean.CodeBean;

/**
 * 
* 项目名称：CommunityServicePlatform 
* 类名称：CodeUtil 
* 类描述： 条形码/二维码工具类
* 创建人：cyx 
* 创建时间：2015年3月24日 19:12:30 
* 修改人：cyx 
* 修改时间：2015年3月24日 19:12:35
* 修改备注： 
* @version
 */
public class CodeUtil extends CodeBean {

	private static List<CodeBean> codeList = new ArrayList<CodeBean>();//码数换算

	public CodeUtil() {
	}

	public CodeUtil(String CodeId, String CodeDesc) {
		this.CodeId = CodeId;
		this.CodeDesc = CodeDesc;
	}

	/**
	 * 用于将给定的内容生成成一维码 注：目前生成内容为中文的话将直接报错，要修改底层jar包的内容
	 *
	 * @param content 将要生成一维码的内容
	 * @return 返回生成好的一维码bitmap
	 * @throws WriterException WriterException异常
	 */
	public static Bitmap CreateBarCode(String content) throws WriterException {
		// 生成一维条码,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		BitMatrix matrix = new MultiFormatWriter().encode(content,
				BarcodeFormat.CODE_128, 50, 20);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pixels[y * width + x] = 0xff000000;
				if (matrix.get(x, y)) {
				}
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap,具体参考api
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
	public static Bitmap toBitmap(String content) throws WriterException
	{
		// 定义位图的宽和高
		BitMatrix byteMatrix = new MultiFormatWriter().encode(content,BarcodeFormat.CODE_128, 50, 20);
		int width = byteMatrix.getWidth();
		int height = byteMatrix.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (byteMatrix.get(x, y)) {
					pixels[y * width + x] = 0xff000000;
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap,具体参考api
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/**
	 * 将指定的内容生成成二维码
	 *
	 * @param content 将要生成二维码的内容
	 * @return 返回生成好的二维码事件
	 * @throws WriterException WriterException异常
	 */
	public static Bitmap CreateQRCode(String content) throws WriterException {
		// 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		BitMatrix matrix = new MultiFormatWriter().encode(content,
				BarcodeFormat.QR_CODE, 300, 300);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		// 二维矩阵转为一维像素数组,也就是一直横着排了
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = 0xff000000;
				}
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap,具体参考api
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	public static Bitmap createQrBitmap(String text, int qrWidth, int qrHeight) {
		try {
			// 需要引入core包
			if (text == null || "".equals(text) || text.length() < 1) {
				return null;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			BitMatrix bitMatrix = new QRCodeWriter().encode(text,
					BarcodeFormat.QR_CODE, qrWidth, qrHeight, hints);

			int width = bitMatrix.getWidth();
			int height = bitMatrix.getHeight();
			int[] pixels = new int[width * height];

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * width + x] = 0xff000000;
					} else {
						pixels[y * width + x] = 0xffffffff;
					}
				}
			}

			Bitmap bitmap = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			return bitmap;

		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;

}
	/*
	 * 初始化list数据
	 */
	public static void initCodeList(){
		//初始化code数据
		codeList.add(new CodeBean("0", "00"));
		codeList.add(new CodeBean("1", "01"));
		codeList.add(new CodeBean("2", "02"));
		codeList.add(new CodeBean("3", "03"));
		codeList.add(new CodeBean("4", "04"));
		codeList.add(new CodeBean("5", "05"));
		codeList.add(new CodeBean("6", "06"));
		codeList.add(new CodeBean("7", "07"));
		codeList.add(new CodeBean("8", "08"));
		codeList.add(new CodeBean("9", "09"));
		codeList.add(new CodeBean("A", "10"));
		codeList.add(new CodeBean("B", "11"));
		codeList.add(new CodeBean("C", "12"));
		codeList.add(new CodeBean("D", "13"));
		codeList.add(new CodeBean("E", "14"));
		codeList.add(new CodeBean("F", "15"));
		codeList.add(new CodeBean("G", "16"));
		codeList.add(new CodeBean("H", "17"));
		codeList.add(new CodeBean("I", "18"));
		codeList.add(new CodeBean("J", "19"));
		codeList.add(new CodeBean("K", "20"));
		codeList.add(new CodeBean("L", "21"));
		codeList.add(new CodeBean("M", "22"));
		codeList.add(new CodeBean("N", "23"));
		codeList.add(new CodeBean("O", "24"));
		codeList.add(new CodeBean("P", "25"));
		codeList.add(new CodeBean("Q", "26"));
		codeList.add(new CodeBean("R", "27"));
		codeList.add(new CodeBean("S", "28"));
		codeList.add(new CodeBean("T", "29"));
		codeList.add(new CodeBean("U", "30"));
		codeList.add(new CodeBean("V", "31"));
		codeList.add(new CodeBean("W", "32"));
		codeList.add(new CodeBean("X", "33"));
		codeList.add(new CodeBean("Y", "34"));
		codeList.add(new CodeBean("Z", "35"));
	}
	
	/**
	 * 通过传入CodeDesc 找到对应的codeId 的字典对象
	 * 
	 * @param CodeDesc
	 * @return
	 */
	public static CodeBean getCodeBeanByCodeDesc(String CodeDesc) {
		for (CodeBean codes : codeList) {
			if (codes.CodeDesc.equals(CodeDesc)) {
				return codes;
			}
		}
		return null;
	}
	
	/**
	 * 通过传入CodeId 找到对应的codeDesc 的字典对象
	 * 
	 * @param CodeId
	 * @return
	 */
	public static CodeBean getCodeBeanByCodeId(String CodeId) {
		for (CodeBean codes : codeList) {
			if (codes.CodeId.equals(CodeId)) {
				return codes;
			}
		}
		return null;
	}
    
    
    /**
     * 订单ID转化为十六位CodeID
     * @param orderId
     * @return
     */
    public static String orderIdTurnCodeID(String orderId){
    	if(TextUtils.isEmpty(orderId)){ 
    		return "";
    	}
    	initCodeList();
    	StringBuffer code = new StringBuffer();
    	String year = orderId.substring(0,2),month = orderId.substring(2, 4),day = orderId.substring(4, 6),
    			hour = orderId.substring(6, 8),last = orderId.substring(8, orderId.length());
    	code = code.append(getCodeBeanByCodeDesc(year).getCodeId()).append(getCodeBeanByCodeDesc(month).getCodeId())
    			.append(getCodeBeanByCodeDesc(day).getCodeId()).append(getCodeBeanByCodeDesc(hour).getCodeId()).append(last);
    	return code.toString();
    }
    
    /**
     * 十六位CodeID转化为订单ID
     * @param codeId
     * @return
     */
    public static String codeIdTurnOrderID(String codeId){
    	try {
    		if(TextUtils.isEmpty(codeId)){
    			return "";
    		}
    		initCodeList();
    		StringBuffer orderId = new StringBuffer();
    		String year = codeId.substring(0,1),month = codeId.substring(1, 2),day = codeId.substring(2, 3),
    				hour = codeId.substring(3, 4),last = codeId.substring(4, codeId.length());
    		orderId = orderId.append(getCodeBeanByCodeId(year).getCodeDesc()).append(getCodeBeanByCodeId(month).getCodeDesc())
    				.append(getCodeBeanByCodeId(day).getCodeDesc()).append(getCodeBeanByCodeId(hour).getCodeDesc()).append(last);
    		return orderId.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
}
