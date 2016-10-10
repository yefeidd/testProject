package cn.zn.com.zn_android.uiclass;

import android.text.TextPaint;
import android.text.style.UnderlineSpan;

/**
 * Created by Jolly on 2016/3/15 0015.
 */
public class NoUnderlineSpan extends UnderlineSpan {
    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(ds.linkColor);
        ds.setUnderlineText(false);
    }
}
