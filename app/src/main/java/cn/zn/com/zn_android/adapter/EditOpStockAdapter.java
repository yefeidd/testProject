package cn.zn.com.zn_android.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.manage.RnApplication;
import cn.zn.com.zn_android.model.OptionalStockModel;
import cn.zn.com.zn_android.uiclass.callback.RVItemTouchCallback;
import cn.zn.com.zn_android.uiclass.listener.DragRVItemClickListener;
import cn.zn.com.zn_android.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jolly on 2016/6/28 0028.
 */
public class EditOpStockAdapter extends RecyclerView.Adapter<EditOpStockAdapter.ViewHolder> implements RVItemTouchCallback.ItemTouchInter {
    private static final String TAG = "EditOpStockAdapter";
    private Context mContext;
    private int res = R.layout.item_edit_optional_stock;
    private List<OptionalStockModel> list = new ArrayList<>();
    private RecyclerView mRV;
    ItemTouchHelper itemTouchHelper;

    public EditOpStockAdapter(RecyclerView recyclerView, int res, List<OptionalStockModel> list) {
        this.res = res;
        this.list = list;
        this.mRV = recyclerView;
        itemTouchHelper = new ItemTouchHelper(new RVItemTouchCallback(this));
        itemTouchHelper.attachToRecyclerView(mRV);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(res, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        list.get(position).showOpStock(holder);

        holder.mIvWarining.setOnClickListener(v -> {
            ToastUtil.showShort(mContext, "warning");
        });

        holder.mIvDelete.setOnClickListener(v -> {
            if (position >= list.size()) return;
            AppObservable.bindActivity((Activity) mContext, ApiManager.getInstance().getService()
                    .delSelfStock(RnApplication.getInstance().getUserInfo().getSessionID(), 
                            list.get(position).getId()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(returnValue -> {
                        if (null != returnValue) {
                            if (returnValue.getData().getMessage().equals("删除成功")) {
                                list.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, list.size() - position);
                            }
                        }
                    }, throwable -> {
                        Log.e(TAG, "delSelfStock: ", throwable);
                    });

        });

        itemTouchHelper.attachToRecyclerView(mRV);
        holder.mIvDrag.setOnTouchListener(new DragRVItemClickListener(mRV) {
            @Override
            public void onLongClick() {
                Log.d("EditOpStockAdapter", "position: " + position + "\ngetAdapterPosition: " + holder.getAdapterPosition());
                if (position != list.size()) {
                    itemTouchHelper.startDrag(holder);
//                    VibratorUtil.Vibrate(getActivity(), 70);   //震动70ms
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onMove(int fromPosition, int toPosition) {
        if (fromPosition==list.size() || toPosition==list.size()){
            return;
        }
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(list, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(list, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onSwiped(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(android.R.id.title)
        public TextView mTitle;
        @Bind(android.R.id.text1)
        public TextView mText1;
        @Bind(R.id.iv_warining)
        public ImageView mIvWarining;
        @Bind(R.id.iv_drag)
        public ImageView mIvDrag;
        @Bind(R.id.iv_delete)
        public ImageView mIvDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
