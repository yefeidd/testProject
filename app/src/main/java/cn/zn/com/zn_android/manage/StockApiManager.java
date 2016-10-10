package cn.zn.com.zn_android.manage;

import cn.zn.com.zn_android.model.bean.OptionalStockBean;
import cn.zn.com.zn_android.model.bean.StockBean;
import cn.zn.com.zn_android.model.entity.RetValue;

import java.util.List;

import retrofit.RestAdapter;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Query;
import rx.Observable;

/**
 * 股票 服务接口
 * Created by Jolly on 2016/8/3 0003.
 */
public class StockApiManager {
    private static final String endPonit = StockConstansUrl.BASE_API_URL;
    private RestAdapter restAdapter;
    private StockApiService stockApiService;

    private static StockApiManager mStockApiManager;

    private StockApiManager() {
    }

    public static StockApiManager getInstance() {
        if (null == mStockApiManager) {
            synchronized (StockApiManager.class) {
                if (null == mStockApiManager) {
                    mStockApiManager = new StockApiManager();
                }
            }
        }

        return mStockApiManager;
    }

    public StockApiService getService() {
        if (null == restAdapter) {
            restAdapter = new RestAdapter.Builder().setEndpoint(endPonit).build();
        }
        if (null == stockApiService) {
            synchronized (StockApiService.class) {
                if (null == stockApiService) {
                    stockApiService = restAdapter.create(StockApiService.class);
                }
            }
        }
        return stockApiService;
    }

    public interface StockApiService {

        /**
         * 获取沪深股票涨跌幅排行
         *
         * @param token 固定的
         * @param field 可选参数，用,分隔,默认为空，返回全部字段，不选即为默认值。返回字段见下方
         * @param exchangeCD 交易所代码；不输入返回沪深，输入XSHG只返回沪市，输入XSHE只返回深市
         * @param pagesize
         * @param pagenum
         * @param desc 是否是跌幅排行；不输入返回涨幅排行；输入1返回跌幅排行
         * @return
         */
        @GET(StockConstansUrl.EQU_RT_RANK)
        Observable<RetValue<List<OptionalStockBean>>> getEquRTRank(@Header("Authorization") String token,
                                                                   @Query("field") String field,
                                                                   @Query("exchangeCD") String exchangeCD,
                                                                   @Query("pagesize") String pagesize,
                                                                   @Query("pagenum") String pagenum,
                                                                   @Query("desc") String desc);
    }
}
