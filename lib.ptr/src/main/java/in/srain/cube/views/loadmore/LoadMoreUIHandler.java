package in.srain.cube.views.loadmore;

public interface LoadMoreUIHandler {

    public void onLoading(LoadMoreContainer container);

    public void onLoadFinish(LoadMoreContainer container, boolean empty, boolean hasMore);

    public void onLoadFinish(LoadMoreContainer container, boolean empty, String emptyMsg, boolean hasMore, String noMoreMsg);

    public void onWaitToLoadMore(LoadMoreContainer container);

    public void onLoadError(LoadMoreContainer container, int errorCode, String errorMessage);
}