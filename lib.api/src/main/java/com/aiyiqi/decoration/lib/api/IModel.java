package com.aiyiqi.decoration.lib.api;

/**
 * Created by lixufeng on 16/2/17.
 */
public interface IModel {
    /**
     *
     * @param cls 下一页数据类型
     * @param listener 回调
     */
    public void fetchNextPageData(Class cls, Listeners.FetchListener listener);
}
