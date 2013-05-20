package com.vito.xmutems.utils;

import it.sauronsoftware.base64.Base64;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
/**
 * 图片工具
 * @author Administrator
 *
 */
public class ImageUtils {
	public static final int[] COLOR_BLUE = new int[] { 0xffFFFFFF, 0xff54C7E1 };
	public static final int[] COLOR_GRAY = new int[] { 0xFFA2A2A2, 0xFF5F5F5F };

	public static Bitmap getBitmap(byte[] iconBase64) {
		byte[] bitmapArray;
		bitmapArray = Base64.decode(iconBase64);
		return BitmapFactory
				.decodeByteArray(bitmapArray, 0, bitmapArray.length);
	}

	/**
	 * 给图片染色,目前只支持黑白图片的染色
	 * @param src 需要上色的图片
	 * @param colors 线性颜色列表
	 * @return 处理后的图片
	 */
	public static Bitmap filterBitmap(Bitmap src, int[] colors) {

		// img.setDrawingCacheEnabled(false);

		Bitmap target = Bitmap.createBitmap(src.getWidth(), src.getHeight(),
				Bitmap.Config.ARGB_8888);
		// 定义画笔
		Paint p = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
		// 定义画板
		Canvas tempCanvas = new Canvas();
		// 给临时画板画图
		tempCanvas.setBitmap(target);
		// 设置过滤器

		p.setShader(new LinearGradient(0, 0, src.getWidth(), src.getHeight(),
				colors, null, TileMode.CLAMP));

		// 向临时画板画上渐变色
		tempCanvas.drawRect(0, 0, src.getWidth(), src.getHeight(), p);
		// 循环点阵图
		for (int x = 0; x < src.getWidth(); x++) {
			for (int y = 0; y < src.getHeight(); y++) {
				if ((src.getPixel(x, y) & 0xFF000000) == 0) {
					target.setPixel(x, y, 0);
				}
			}
		}
		return target;
	}
	
	
	
	/**
	 *  通过url获得bitmap
	 * @param url
	 * @param opts 
	 * @return
	 */
	public static Bitmap getBitmap(String url,Options opts){
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is,null,opts);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			
		}
		return bitmap;
	}
	
	/**
	 * 通过url获得bitmap
	 * @param url
	 * @return
	 */
	public static Bitmap getBitmap(String url) {
		return getBitmap(url,null);
	}

	
	/**
	 * 为图片添加水印
	 *
	 * @param src the bitmap object you want proecss
	 * @param watermark the water mark above the src
	 * @return return a bitmap object ,if paramter's length is 0,return null
	 */
	public static Bitmap createBitmap( Bitmap src, Bitmap watermark ){
		String tag = "createBitmap";
		L.d( tag, "create a new bitmap with water mark" );
		if( src == null ){
			return null;
		}
		int w = src.getWidth();
		int h = src.getHeight();
		int ww = watermark.getWidth();
		
		int wh = watermark.getHeight();
		//create the new blank bitmap
		Bitmap newb = Bitmap.createBitmap( w, h, Config.ARGB_8888 );//创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas( newb );
		//draw src into
		cv.drawBitmap( src, 0, 0, null );//在 0，0坐标开始画入src
		//draw watermark into
		cv.drawBitmap( watermark, w - ww + 5, h - wh + 5, null );//在src的右下角画入水印
		//save all clip
		cv.save( Canvas.ALL_SAVE_FLAG );//保存
		//store
		cv.restore();//存储
		return newb;
	}
	
	/**
	 * 为图片添加圆角效果
	 * @param bitmap
	 * @param pixels 圆角弧度
	 * @return
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {  
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);  
        Canvas canvas = new Canvas(output);  
        final int color = 0xff424242;  
        final Paint paint = new Paint();  
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());  
        final RectF rectF = new RectF(rect);  
        final float roundPx = pixels;  
        //防止锯齿  
        paint.setAntiAlias(true);
        //相当于清屏
        canvas.drawARGB(0, 0, 0, 0);  
        paint.setColor(color);
        //先画了一个带圆角的矩形
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);  
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        //再把原来的bitmap画到现在的bitmap！
        canvas.drawBitmap(bitmap, rect, rect, paint);  
        return output;  
    }
	
	/**
	 * 缩放图片，压缩后图片的宽和高以及kB大小均会变化
	 * @param bitmap 需要缩放的位图
	 * @param newWidth 新的宽度
	 * @param newHeight 新的高度
	 * @return
	 */
	public static Bitmap compressBitmap(Bitmap bitmap, int newWidth, int newHeight) {
		//————>以下为将图片高宽和的大小kB压缩  
	    // 得到图片原始的高宽  
	    int rawHeight = bitmap.getHeight();  
	    int rawWidth = bitmap.getWidth();  
	    // 计算缩放因子  
	    float heightScale = ((float) newHeight) / rawHeight;  
	    float widthScale = ((float) newWidth) / rawWidth;  
	    // 新建立矩阵  
	    Matrix matrix = new Matrix();  
	    matrix.postScale(heightScale, widthScale);  
	    //将图片大小压缩  
	    //压缩后图片的宽和高以及kB大小均会变化  
	    Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, rawWidth,rawHeight, matrix, true);  
		return newBitmap;
	}
	
	/**
	 * 根据比例获取图片缩略图
	 * @param filePath
	 * @param minSideLength 希望生成的缩略图的宽高中的较小的值 
	 * @param maxNumOfPixels 希望生成的缩量图的总像素
	 * @return
	 */
	public static Bitmap getBitmapThumbnail(String url,int minSideLength, int maxNumOfPixels){  
          BitmapFactory.Options options=new BitmapFactory.Options();  
          //true那么将不返回实际的bitmap对象,不给其分配内存空间但是可以得到一些解码边界信息即图片大小等信息  
          options.inJustDecodeBounds=true;  
          getBitmap(url, options);  

          int sampleSize=computeSampleSize(options, minSideLength, maxNumOfPixels);  
          //读取图片
          options.inJustDecodeBounds = false;  
          options.inSampleSize = sampleSize;  
          Bitmap thumbnailBitmap = getBitmap(url, options);  
          return thumbnailBitmap;  
	}

	/**
	 * <a href='http://my.csdn.net/zljk000/code/detail/18212 '>参考资料</a>
	 * @param options 原本Bitmap的options
	 * @param minSideLength 希望生成的缩略图的宽高中的较小的值  
	 * @param maxNumOfPixels 希望生成的缩量图的总像素 
	 * @return
	 */
	public static int computeSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {  
		int initialSize = computeInitialSampleSize(options, minSideLength,maxNumOfPixels);  
		int roundedSize;  
		if (initialSize <= 8) {  
		    roundedSize = 1;  
		    while (roundedSize < initialSize) {  
		        roundedSize <<= 1;  
		    }  
		} else {  
		    roundedSize = (initialSize + 7) / 8 * 8;  
		}  

		return roundedSize;  
	}  
	    
	private static int computeInitialSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {  
		//原始图片的宽  
	    double w = options.outWidth;  
	    //原始图片的高  
	    double h = options.outHeight;  
	    int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math  
	            .sqrt(w * h / maxNumOfPixels));  
	    int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(  
	            Math.floor(w / minSideLength), Math.floor(h / minSideLength));  

	    if (upperBound < lowerBound) {  
	        // return the larger one when there is no overlapping zone.  
	        return lowerBound;  
	    }
	    if ((maxNumOfPixels == -1) && (minSideLength == -1)) {  
	        return 1;  
	    } else if (minSideLength == -1) {  
	        return lowerBound;  
	    } else {  
	        return upperBound;  
	    }  
	}  
	
	
}
