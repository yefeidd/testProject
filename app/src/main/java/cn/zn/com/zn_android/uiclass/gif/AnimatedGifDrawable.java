package cn.zn.com.zn_android.uiclass.gif;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import cn.zn.com.zn_android.manage.RnApplication;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class AnimatedGifDrawable extends AnimationDrawable {

    private int mCurrentIndex = 0;
    private UpdateListener mListener;
    private RnApplication mRnApp = RnApplication.getInstance();

    public AnimatedGifDrawable(String index, InputStream source, UpdateListener listener) {
        mListener = listener;
        HashMap<String, Object> map = mRnApp.getBmMap();
        if (null == map.get(index)) {
            GifDecoder decoder = new GifDecoder();
            decoder.read(source);
            map.put(index, decoder.getFrames());
        }
        // Iterate through the gif frames, add each as animation frame
        Vector<GifDecoder.GifFrame> frameVector = (Vector<GifDecoder.GifFrame>) map.get(index);
        for (int i = 0; i < frameVector.size(); i++) {
            Bitmap bitmap = frameVector.get(i).image;
            BitmapDrawable drawable = new BitmapDrawable(bitmap);
            // Explicitly set the bounds in order for the frames to display
            drawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
            addFrame(drawable, frameVector.get(i).delay);
            if (i == 0) {
                // Also set the bounds for this container drawable
                setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
            }
        }

    }

    /**
     * Naive method to proceed to next frame. Also notifies listener.
     */
    public void nextFrame() {
        mCurrentIndex = (mCurrentIndex + 1) % getNumberOfFrames();
        if (mListener != null)
            mListener.update();
    }

    /**
     * Return display duration for current frame
     */
    public int getFrameDuration() {
        return getDuration(mCurrentIndex);
    }

    /**
     * Return drawable for current frame
     */
    public Drawable getDrawable() {
        return getFrame(mCurrentIndex);
    }

    /**
     * Interface to notify listener to update/redraw Can't figure out how to
     * invalidate the drawable (or span in which it sits) itself to force redraw
     */
    public interface UpdateListener {
        void update();
    }

}