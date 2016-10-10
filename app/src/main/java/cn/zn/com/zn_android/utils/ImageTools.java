package cn.zn.com.zn_android.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 *  Tools for handler picture
 * @author ZHF
 *
 */
public final class ImageTools {

	/**
	 * Resize the bitmap
	 * 图片压缩
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}

}
