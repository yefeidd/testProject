package cn.zn.com.zn_android.uiclass.customerview.customChart;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

/**
 * author：ajiang
 * mail：1025065158@qq.com
 * blog：http://blog.csdn.net/qqyanjiang
 */
public class MyRightMarkerView extends MarkerView {
    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    private TextView markerTv;
    private SpannableString num;
    public int[] colors = {Color.parseColor("#999999")};
    ;

    public MyRightMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        markerTv = (TextView) findViewById(R.id.marker_tv);
        markerTv.setTextSize(10);

    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    public void setData(SpannableString num) {
        this.num = num;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        markerTv.setText(num);
    }

    @Override
    public int getXOffset(float xpos) {

        return 0;
    }

    @Override
    public int getYOffset(float ypos) {
        return 0;
    }
}
