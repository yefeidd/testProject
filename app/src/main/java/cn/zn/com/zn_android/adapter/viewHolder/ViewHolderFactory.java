package cn.zn.com.zn_android.adapter.viewHolder;

import android.view.View;

/**
 * Created by zjs on 2016/9/12 0012.
 * email: m15267280642@163.com
 * explain: 各个ViewHolder的生产车间
 */
public class ViewHolderFactory {
    public static BaseViewHolder createViewHoler(String viewHolderName, View view) {
        switch (viewHolderName) {
            case "HotStockGodViewHolder":
                return new HotStockGodViewHolder(view);
            case "FYListHolder":
                return new FYListHolder(view);
            case "HotStockListHolder":
                return new HotStockListHolder(view);
            case "HotArticleContestHolder":
                return new HotArticleContestHolder(view);
            case "TeacherExplainHolder":
                return new TeacherExplainHolder(view);
            case "BuyInViewHolder":
                return new BuyInViewHolder(view);
            case "ContestGameHolder":
                return new ContestGameHolder(view);
            case "ContestRankingListHolder":
                return new ContestRankingListHolder(view);
            case "ContestNoticeListHolder":
                return new ContestNoticeListHolder(view);
            case "ActionDetailViewHolder":
                return new ActionDetailViewHolder(view);
            case "TaOperateDetailViewHolder":
                return new TaOperateDetailViewHolder(view);
            case "TaRecordViewHolder":
                return new TaRecordViewHolder(view);
            case "DynamicExpertViewHolder":
                return new DynamicExpertViewHolder(view);
            case "GeniusRankingViewHolder":
                return new GeniusRankingViewHolder(view);
            case "VoucherViewHolder":
                return new VoucherViewHolder(view);
            case "EntrustViewHolder":
                return new EntrustViewHolder(view);
            case "OnlineTeacherGVHolder":
                return new OnlineTeacherGVHolder(view);
            case "ExcellentAnswerHolder":
                return new ExcellentAnswerHolder(view);
            case "OnlineTeacherRankingHolder":
                return new OnlineTeacherRankingHolder(view);
            case "PreAnswerHolder":
                return new PreAnswerHolder(view);
            case "FinishAnswerHolder":
                return new FinishAnswerHolder(view);
            case "MyQuestionHolder":
                return new MyQuestionHolder(view);
            case "OrderAllHolder":
                return new OrderAllHolder(view);
            case "BillListHolder":
                return new BillListHolder(view);
            case "BillHolder":
                return new BillHolder(view);
            default:
                return null;

        }

    }

}
