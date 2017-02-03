package cn.zn.com.zn_android.utils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zjs on 2016/12/28 0028.
 * email: m15267280642@163.com
 * explain: 计时器
 */

public class RxCountDown {

    public static Observable<Integer> countdown(long time) {
        if (time < 0) time = 0L;

        final int countTime = (int) time;
        return Observable.interval(1, TimeUnit.SECONDS, Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long increaseTime) {
                        return countTime - increaseTime.intValue();
                    }
                })
                .take(countTime + 1);

    }
}
