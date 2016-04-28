package com.wiky.integralwalluidemo.widght;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 通用的RecyclerView
 * <p/>
 * 1. 支持添加滚动到底部监听回调
 * myCommonRecyclerView.setOnReachBottomListener(new OnReachBottomListener(){
 * public void onReachBottom(){ // 即将到达底部，加载更多数据};
 * })
 * <p/>
 * 2. 设置EnptyView，当列表为空时，显示EnptyView
 * 布局：
 * <com.wiky.integralwalluidemo.widght.MyCommonRecyclerView
 * android:id="@+id/recyclerView"
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content"/>
 * <TextView
 * android:id="@+id/emptyView"
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content"
 * />
 * 使用：
 * myCommonRecyclerView.setEmptyView(findViewById(R.id.emptyView));
 * <p/>
 * Created by wiky on 2016/4/28.
 */
public class MyCommonRecyclerView extends RecyclerView {
    private OnReachBottomListener onReachBottomListener;
    private boolean isInTheBottom = false;
    /**
     * reachBottomRow = 1;(default)
     * mean : when the lastVisibleRow is lastRow , call the onReachBottom();
     * reachBottomRow = 2;
     * mean : when the lastVisibleRow is Penultimate Row , call the onReachBottom();
     * And so on
     */
    private int reachBottomRow = 1;

    private View emptyView;

    public MyCommonRecyclerView(Context context) {
        super(context);
    }

    public MyCommonRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCommonRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            Adapter<?> adapter = getAdapter();
            if (adapter != null && emptyView != null) {
                if (adapter.getItemCount() == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                    MyCommonRecyclerView.this.setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(View.GONE);
                    MyCommonRecyclerView.this.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);

        if (onReachBottomListener != null) {
            LayoutManager layoutManager = getLayoutManager();
            if (layoutManager == null) { //it maybe unnecessary
                throw new RuntimeException("LayoutManager is null,Please check it!");
            }
            Adapter adapter = getAdapter();
            if (adapter == null) { //it maybe unnecessary
                throw new RuntimeException("Adapter is null,Please check it!");
            }
            boolean isReachBottom = false;
            //is GridLayoutManager
            if (layoutManager instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                int rowCount = adapter.getItemCount() / gridLayoutManager.getSpanCount();
                int lastVisibleRowPosition = gridLayoutManager.findLastVisibleItemPosition() / gridLayoutManager.getSpanCount();
                isReachBottom = (lastVisibleRowPosition >= rowCount - reachBottomRow);
            }
            //is LinearLayoutManager
            else if (layoutManager instanceof LinearLayoutManager) {
                int lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                int rowCount = adapter.getItemCount();
                if (reachBottomRow > rowCount)
                    reachBottomRow = 1;
                isReachBottom = (lastVisibleItemPosition >= rowCount - reachBottomRow);
            }
            //is StaggeredGridLayoutManager
            else if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                int spanCount = staggeredGridLayoutManager.getSpanCount();
                int[] into = new int[spanCount];
                int[] eachSpanListVisibleItemPosition = staggeredGridLayoutManager.findLastVisibleItemPositions(into);
                for (int i = 0; i < spanCount; i++) {
                    if (eachSpanListVisibleItemPosition[i] > adapter.getItemCount() - reachBottomRow * spanCount) {
                        isReachBottom = true;
                        break;
                    }
                }
            }

            if (!isReachBottom) {
                isInTheBottom = false;
            } else if (!isInTheBottom) {
                onReachBottomListener.onReachBottom();
                isInTheBottom = true;
                Log.d("RBCallbkRecyclerView", "onReachBottom");
            }
        }
    }

    public void setReachBottomRow(int reachBottomRow) {
        if (reachBottomRow < 1)
            reachBottomRow = 1;
        this.reachBottomRow = reachBottomRow;
    }

    public interface OnReachBottomListener {
        void onReachBottom();
    }

    public void setOnReachBottomListener(OnReachBottomListener onReachBottomListener) {
        this.onReachBottomListener = onReachBottomListener;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);

        if (adapter != null) {
            adapter.registerAdapterDataObserver(emptyObserver);
        }
        emptyObserver.onChanged();
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }
}
