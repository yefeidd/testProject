package cn.zn.com.zn_android.uiclass.listener;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Jolly on 2016/6/28 0028.
 */
public class DragRVItemClickListener implements View.OnTouchListener {
    private static final String TAG = "DragRVItemClickListener";
    private GestureDetectorCompat mGestureDetector;
    private RecyclerView recyclerView;

    public DragRVItemClickListener(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(),new ItemTouchHelperGestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child!=null) {
                Log.d(TAG, "onDown: ");
                onLongClick();

//                onItemClick(vh);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.d(TAG, "onLongPress: ");
//            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
//            if (child!=null) {
//                RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(child);
////                onLongClick(vh);
//            }
        }
    }

    public void onLongClick(){}
    public void onItemClick(){}
}
