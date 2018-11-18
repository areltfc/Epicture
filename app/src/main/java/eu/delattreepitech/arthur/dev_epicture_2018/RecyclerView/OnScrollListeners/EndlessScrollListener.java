package eu.delattreepitech.arthur.dev_epicture_2018.RecyclerView.OnScrollListeners;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Objects;

public class EndlessScrollListener extends RecyclerView.OnScrollListener {
    private boolean _isLoading;
    private boolean _hasMorePages;
    private RefreshList _refreshList;
    private boolean _isRefreshing;
    private int _pastVisibleItems;

    public EndlessScrollListener(RefreshList refreshList) {
        _isLoading = false;
        _hasMorePages = true;
        _refreshList = refreshList;
    }

    @Override public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int visibleItemCount = Objects.requireNonNull(manager).getChildCount();
        int totalItemCount = manager.getItemCount();
        int firstVisibleItems = manager.findFirstVisibleItemPosition();
        if (firstVisibleItems > 0) {
            _pastVisibleItems = firstVisibleItems;
        }
        if (visibleItemCount + _pastVisibleItems >= totalItemCount && !_isLoading) {
            _isLoading = true;
            if (_hasMorePages && !_isRefreshing) {
                _isRefreshing = true;
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        _refreshList.onRefresh(EndlessScrollListener.this);
                    }
                }, 200);
            }
        } else {
            _isLoading = false;
        }
    }

    public void reset() {
        _isLoading = false;
        _hasMorePages = true;
    }

    public void noMorePages() {
        this._hasMorePages = false;
    }

    public void notifyMorePages() {
        _isRefreshing = false;
    }

    public interface RefreshList {
        void onRefresh(EndlessScrollListener listener);
    }
}
