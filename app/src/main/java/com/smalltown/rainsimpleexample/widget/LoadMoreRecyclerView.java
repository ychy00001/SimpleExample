package com.smalltown.rainsimpleexample.widget;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.smalltown.rainsimpleexample.R;

/**
 * 自定义下拉加载更多视图
 * Created by yangchunyu
 * on 2016/1/14 11:28
 */
@SuppressWarnings("unused")
public class LoadMoreRecyclerView extends RecyclerView {

    /**
     * 定义RecyclerView中的条目类型
     */
    public final static int TYPE_NORMAL = 0; //普通RecyclerView
    public final static int TYPE_HEADER = 1;//带头的RecyclerView 用于下拉刷新
    public final static int TYPE_FOOTER = 2;//带脚布局的  用于加载更多
    public final static int TYPE_LIST = 3;//标识条目为List
    public final static int TYPE_GRID = 4;//标识条目为网格
    public final static int TYPE_STAGGER = 5;//标识条目为瀑布流

    private boolean mIsFooterEnable = false;//是否允许加载更多

    private boolean mIsLoadingMore;//用于标识是否正在加载更多，防止重复加载

    private int mLoadMorePosition;//标记加载更多的position

    private LoadMoreListener mListener;//加载更多的监听

    private AutoLoadAdapter mAutoLoadAdapter;//自适配Adapter

    private boolean mIsShowLoadNoMore = false;//是否显示没有更多字样


    public LoadMoreRecyclerView(Context context) {
        super(context);
        init();
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * 初始化-添加一个滚动监听
     * <p/>
     * 回调加载更多的方法
     * 1.有监听并且支持加载更多: null != mListener && mIsFooterEnable;
     * 2.目前没有在加载  正在上拉 dy>0,当前最后一条可见数据是总数据的最后一条 加载更多
     * 3. 当前不显示加载更多
     */
    private void init() {
        super.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                System.out.println("dy:"+dy);
                if (null != mListener && mIsFooterEnable && !mIsLoadingMore && dy > 0 && !mIsShowLoadNoMore) {
                    int lastVisiblePosition = getLastVisiblePosition();
                    if (lastVisiblePosition + 1 == mAutoLoadAdapter.getItemCount()) {
                        setLoadingMore(true);
                        mLoadMorePosition = lastVisiblePosition;
                        mListener.onLoadMore();
                    }
                }
            }
        });
    }


    /**
     * 设置正在加载更多
     *
     * @param loadingMore 是否正在加载
     */
    public void setLoadingMore(boolean loadingMore) {
        this.mIsLoadingMore = loadingMore;
    }

    /**
     * 设置加载更多的监听
     *
     * @param listener 加载更多监听
     */
    public void setLoadMoreListener(LoadMoreListener listener) {
        mListener = listener;
    }

    /**
     * 设置是否显示加载更多
     */
    private void setShowLoadNoMore(boolean showLoadNoMore) {
        this.mIsShowLoadNoMore = showLoadNoMore;
    }

    /**
     * 设置头布局
     *
     * @param view 头布局
     */
    public void setHeaderView(View view) {
        if(view != null){
            mAutoLoadAdapter.setHeaderView(view);
            setHeaderEnable(true);
        }
    }

    /**
     * 获取脚布局的高度
     * @return 脚布局高度
     */
    public int getFootViewHeight(Context context) {
        if(mIsFooterEnable){
            View view = View.inflate(context, R.layout.list_foot_loading, null);
            view.measure(0,0);
            return view.getMeasuredHeight();
        }
        return 0;
    }


    /**
     * 加载更多的监听
     */
    public interface LoadMoreListener {
        //加载更多
        void onLoadMore();
    }

    /**
     * 自适应LoadAdapter
     */
    public class AutoLoadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private RecyclerView.Adapter mInternalAdapter;
        private int mHeadResId;
        private boolean mIsHeaderEnable;
        private View mHeaderView;
        private LayoutManager mLayoutManager;


        public AutoLoadAdapter(Adapter internalAdapter) {
            this.mInternalAdapter = internalAdapter;
            this.mLayoutManager = getLayoutManager();
            setGridSpanCount(mLayoutManager);
        }

        @Override
        public int getItemViewType(int position) {
            System.out.println("getItemViewType position:" + position);

            int headerPosition = 0;
            int footerPosition = getItemCount() - 1;

            int itemType = TYPE_NORMAL;
            if (headerPosition == position && mIsHeaderEnable && (mHeadResId > 0 | mHeaderView != null)) {
                System.out.println("返回头布局的type");
                itemType = TYPE_HEADER;
            }
            if (footerPosition == position && mIsFooterEnable) {
                System.out.println("返回脚布局的type");
                itemType = TYPE_FOOTER;
            }
            return itemType;


            //以后需要注意 如果要转换线性与网格布局 以下有可能执行不到 建议单独重写一个RecyclerView
//            if (layoutManager instanceof LinearLayoutManager) {
//                if (layoutManager instanceof GridLayoutManager) {
//                    return TYPE_GRID;
//                }
//                return TYPE_LIST;
//            }else if (layoutManager instanceof StaggeredGridLayoutManager) {
//                return TYPE_STAGGER;
//            }else {
//                return TYPE_NORMAL;
//            }

        }

        /**
         * 设置在GridLayout中对于条目横跨处理
         * 1.如果存在头布局 并且位置为0  横跨一行
         * 2.如果存在尾布局 并且位置为最后一位  横跨一行
         * @param layoutManager 布局管理器
         */
        private void setGridSpanCount(final LayoutManager layoutManager) {//当时卡片布局 GridLayoutManager 继承LinearLayoutManager
            if (layoutManager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = (GridLayoutManager) layoutManager;
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        int spanCount = gridManager.getSpanCount();
                        if(mIsHeaderEnable && position == 0){
                            System.out.println(position + "设置头布局横跨的长度" + spanCount);
                            return spanCount;
                        }
                        if(mIsFooterEnable && position == getItemCount()-1){
                            return spanCount;
                        }
                        return 1;
                    }
                });
            }
        }

        public void setHeaderView(View headerView) {
            this.mHeaderView = headerView;
        }

        /**
         * 脚布局Holder
         */
        public class FooterViewHolder extends RecyclerView.ViewHolder {
            public FooterViewHolder(View itemView) {
                super(itemView);
            }
        }

        /**
         * 头布局Holder
         */
        public class HeaderViewHolder extends RecyclerView.ViewHolder {
            public HeaderViewHolder(View itemView) {
                super(itemView);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_HEADER && mHeadResId > 0) {
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                        mHeadResId, parent, false));
            }else if(viewType == TYPE_HEADER && mHeaderView != null){
                //                return new HeaderViewHolder(mHeaderView);
                return new FooterViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.list_foot_loading, parent, false));
            }
            if (viewType == TYPE_FOOTER) {
                return new FooterViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.list_foot_loading, parent, false));
            } else { //TYPE_NORMAL
                return mInternalAdapter.onCreateViewHolder(parent, viewType);
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            int type = getItemViewType(position);
            if (type != TYPE_FOOTER && type != TYPE_HEADER) {
                //如果有头布局 传递给需要代理的Adapter中时候 需要位置-1
                if(mIsHeaderEnable){
                    mInternalAdapter.onBindViewHolder(holder, position-1);
                }else{
                    mInternalAdapter.onBindViewHolder(holder, position);
                }
            }
            if(type == TYPE_FOOTER && mIsShowLoadNoMore){
                //展示没有更多字样
                setLoadMoreView(holder,true);
            }else if(type == TYPE_FOOTER){
                //不展示没有更多字样
                setLoadMoreView(holder,false);
            }
        }

        /**
         * 设置底部加载的样式
         */
        private void setLoadMoreView(ViewHolder holder, boolean isShowLoadNoMore) {
            LinearLayout loadMoreView = (LinearLayout) holder.itemView.findViewById(R.id.ll_loading_more);
            LinearLayout loadNoMoreView = (LinearLayout) holder.itemView.findViewById(R.id.ll_loading_no_more);
            if(isShowLoadNoMore){
                loadMoreView.setVisibility(View.GONE);
                loadNoMoreView.setVisibility(View.VISIBLE);
            }else{
                loadMoreView.setVisibility(View.VISIBLE);
                loadNoMoreView.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            int count = mInternalAdapter.getItemCount();
            if (mIsFooterEnable) count++;
            if (mIsHeaderEnable) count++;
            return count;
        }

        public void setHeaderEnable(boolean enable) {
            mIsHeaderEnable = enable;
        }

        public void addHeaderView(int resId) {
            mHeadResId = resId;
        }
    }


    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter != null) {
            mAutoLoadAdapter = new AutoLoadAdapter(adapter);
        }
        super.swapAdapter(mAutoLoadAdapter, true);
    }

    /**
     * 切换页面布局
     * @param layoutManager 布局管理者
     */
    public void switchLayoutManager(LayoutManager layoutManager) {
        int firstViewiblePosition = getFirstVisiblePosition();
        setLayoutManager(layoutManager);
        getLayoutManager().scrollToPosition(firstViewiblePosition);
    }

    /**
     * 获取第一个可见条目
     * @return 第一个可见条目
     */
    public int getFirstVisiblePosition() {
        int position;
        if (getLayoutManager() instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        } else if (getLayoutManager() instanceof GridLayoutManager) {
            position = ((GridLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) getLayoutManager();
            int[] lastPositions = layoutManager.findFirstVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMinPositions(lastPositions);
        } else {
            position = 0;
        }
        return position;
    }

    /**
     * 获取最后一条可见数据
     *
     * @return 数据位置
     */
    public int getLastVisiblePosition() {
        int position;
        if (getLayoutManager() instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        } else if (getLayoutManager() instanceof GridLayoutManager) {
            position = ((GridLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) getLayoutManager();
            int[] lastPositions = layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMaxPosition(lastPositions);
        } else {
            position = getLayoutManager().getItemCount() - 1;
        }
        return position;
    }

    /**
     * 获得当前展示最小的position
     *
     * @param positions 位置集合
     * @return 返回最小的位置
     */
    private int getMinPositions(int[] positions) {
        int minPosition = Integer.MAX_VALUE;
        for (int position : positions) {
            minPosition = Math.min(minPosition, position);
        }
        return minPosition;
    }


    /**
     * 获取最大条目数
     *
     * @param positions 获取最大位置数组
     *
     * @return 最大的的某个位置
     */
    private int getMaxPosition(int[] positions) {
        int maxPosition = Integer.MIN_VALUE;
        for (int position : positions) {
            maxPosition = Math.max(maxPosition, position);
        }
        return maxPosition;
    }

    /**
     * 添加头部View
     * @param resId 资源ID
     */
    public void addHeaderView(int resId) {
        mAutoLoadAdapter.addHeaderView(resId);
    }

    /**
     * 设置头部View是否展示
     * @param enable true 显示  false 不显示
     */
    public void setHeaderEnable(boolean enable){
        mAutoLoadAdapter.setHeaderEnable(enable);
    }

    /**
     * 设置是否支持自动加载更多
     * @param autoLoadMore true 加载更多 false 不加载
     */
    public void setAutoLoadMoreEnable(boolean autoLoadMore) {
        mIsFooterEnable = autoLoadMore;
    }

    /**
     * 通知更多的数据已经加载
     *
     * 每次加载完成后添加了data数据 用该方法刷新列表
     * 不用notifyDataSetChange来刷新
     * @param hasMore 是否还有更多了  没有的话不显示上拉加载框
     */
    public void notifyMoreFinish(boolean hasMore) {
        setAutoLoadMoreEnable(hasMore);
        if(!hasMore)
            setShowLoadNoMore(false);//设置不显示加载跟多
        getAdapter().notifyItemRemoved(mLoadMorePosition);
        mIsLoadingMore = false;
    }


    /**
     * 通知更多的数据已经加载 并在最后布局中提示没有更多
     *
     * @param hasMore 是否还有更多了  没有的话脚布局显示没有更多了
     */
    public void notifyMoreFinishWithNoMoreData(boolean hasMore) {
        setAutoLoadMoreEnable(true);
        if(!hasMore)
            setShowLoadNoMore(true);
        getAdapter().notifyItemRemoved(mLoadMorePosition);
        mIsLoadingMore = false;
    }

}






///**
// * 自定义下拉加载更多视图
// * Created by yangchunyu
// * on 2016/1/14 11:28
// */
//public class LoadMoreRecyclerView extends RecyclerView {
//
//    /**
//     * 定义RecyclerView中的条目类型
//     */
//    public final static int TYPE_NORMAL = 0; //普通RecyclerView
//    public final static int TYPE_HEADER = 1;//带头的RecyclerView 用于下拉刷新
//    public final static int TYPE_FOOTER = 2;//带脚布局的  用于加载更多
//    public final static int TYPE_LIST = 3;//标识条目为List
//    public final static int TYPE_STAGGER = 4;//标识条目为网格模式
//
//    private boolean mIsFooterEnable = false;//是否允许加载更多
//
//    private boolean mIsLoadingMore;//用于标识是否正在加载更多，防止重复加载
//
//    private int mLoadMorePosition;//标记加载更多的position
//
//    private LoadMoreListener mListener;//加载更多的监听
//
//    private AutoLoadAdapter mAutoLoadAdapter;//自适配Adapter
//
//    private boolean mIsShowLoadNoMore = false;//是否显示没有更多字样
//
//    public LoadMoreRecyclerView(Context context) {
//        super(context);
//        init();
//    }
//
//    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//        init();
//    }
//
//    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        init();
//    }
//
//    /**
//     * 初始化-添加一个滚动监听
//     * <p/>
//     * 回调加载更多的方法
//     * 1.有监听并且支持加载更多: null != mListener && mIsFooterEnable;
//     * 2.目前没有在加载  正在上拉 dy>0,当前最后一条可见数据是总数据的最后一条 加载更多
//     * 3. 当前不显示加载更多
//     */
//    private void init() {
//        super.addOnScrollListener(new OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (null != mListener && mIsFooterEnable && !mIsLoadingMore && dy > 0 && !mIsShowLoadNoMore) {
//                    int lastVisiblePosition = getLastVisiblePosition();
//                    if (lastVisiblePosition + 1 == mAutoLoadAdapter.getItemCount()) {
//                        setLoadingMore(true);
//                        mLoadMorePosition = lastVisiblePosition;
//                        mListener.onLoadMore();
//                    }
//                }
//            }
//        });
//    }
//
//
//    /**
//     * 设置正在加载更多
//     *
//     * @param loadingMore 是否正在加载
//     */
//    public void setLoadingMore(boolean loadingMore) {
//        this.mIsLoadingMore = loadingMore;
//    }
//
//    /**
//     * 设置加载更多的监听
//     *
//     * @param listener 加载更多监听
//     */
//    public void setLoadMoreListener(LoadMoreListener listener) {
//        mListener = listener;
//    }
//
//    public void setShowLoadNoMore(boolean showLoadNoMore) {
//        this.mIsShowLoadNoMore = showLoadNoMore;
//    }
//
//
//    /**
//     * 加载更多的监听
//     */
//    public interface LoadMoreListener {
//        //加载更多
//        void onLoadMore();
//    }
//
//    /**
//     * 自适应LoadAdapter
//     */
//    public class AutoLoadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//        private RecyclerView.Adapter mInternalAdapter;
//        private boolean mIsHeaderEnable;
//        private int mHeadResId;
//
//
//        public AutoLoadAdapter(Adapter internalAdapter) {
//            this.mInternalAdapter = internalAdapter;
//            mIsHeaderEnable = false;
//        }
//
//        @Override
//        public int getItemViewType(int position) {
//            int headerPosition = 0;
//            int footerPosition = getItemCount() - 1;
//
//            if (headerPosition == position && mIsHeaderEnable && mHeadResId > 0) {
//                return TYPE_HEADER;
//            }
//            if (footerPosition == position && mIsFooterEnable) {
//                return TYPE_FOOTER;
//            }
//            if (getLayoutManager() instanceof LinearLayoutManager) {
//                return TYPE_LIST;
//            }
//            //TODO 适配GridLayout 是否添加 || getLayoutManager() instanceof  GridLayoutManager
//            else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
//                return TYPE_STAGGER;
//            } else {
//                return TYPE_NORMAL;
//            }
//        }
//
//        /**
//         * 脚布局Holder
//         */
//        public class FooterViewHolder extends RecyclerView.ViewHolder {
//            public FooterViewHolder(View itemView) {
//                super(itemView);
//            }
//        }
//
//        /**
//         * 头布局Holder
//         */
//        public class HeaderViewHolder extends RecyclerView.ViewHolder {
//            public HeaderViewHolder(View itemView) {
//                super(itemView);
//            }
//        }
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            System.out.println("view_Type:"+viewType);
//            if (viewType == TYPE_HEADER) {
//                return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(
//                        mHeadResId, parent, false));
//            }
//            if (viewType == TYPE_FOOTER) {
//                return new FooterViewHolder(LayoutInflater.from(parent.getContext()).inflate(
//                        R.layout.list_foot_loading, parent, false));
//            } else { //TYPE_NORMAL
//                return mInternalAdapter.onCreateViewHolder(parent, viewType);
//            }
//        }
//
//        @SuppressWarnings("unchecked")
//        @Override
//        public void onBindViewHolder(ViewHolder holder, int position) {
//            int type = getItemViewType(position);
//            if (type != TYPE_FOOTER && type != TYPE_HEADER) {
//                mInternalAdapter.onBindViewHolder(holder, position);
//            }
//            if(type == TYPE_FOOTER && mIsShowLoadNoMore){
//                //展示没有更多字样
//               setLoadMoreView(holder,true);
//            }else if(type == TYPE_FOOTER){
//                //不展示没有更多字样
//                setLoadMoreView(holder,false);
//            }
//        }
//
//        /**
//         * 设置底部加载的样式
//         */
//        private void setLoadMoreView(ViewHolder holder,boolean isShowLoadNoMore) {
//            LinearLayout loadMoreView = (LinearLayout) holder.itemView.findViewById(R.id.ll_loading_more);
//            LinearLayout loadNoMoreView = (LinearLayout) holder.itemView.findViewById(R.id.ll_loading_no_more);
//            if(isShowLoadNoMore){
//                loadMoreView.setVisibility(View.GONE);
//                loadNoMoreView.setVisibility(View.VISIBLE);
//            }else{
//                loadMoreView.setVisibility(View.VISIBLE);
//                loadNoMoreView.setVisibility(View.GONE);
//            }
//        }
//
//        @Override
//        public int getItemCount() {
//            int count = mInternalAdapter.getItemCount();
//            if (mIsFooterEnable) count++;
//            if (mIsHeaderEnable) count++;
//            return count;
//        }
//
//        public void setHeaderEnable(boolean enable) {
//            mIsHeaderEnable = enable;
//        }
//
//        public void addHeaderView(int resId) {
//            mHeadResId = resId;
//        }
//    }
//
//
//    @Override
//    public void setAdapter(Adapter adapter) {
//        if (adapter != null) {
//            System.out.println("设置Adapter");
//            mAutoLoadAdapter = new AutoLoadAdapter(adapter);
//        }
//        super.swapAdapter(mAutoLoadAdapter, true);
//    }
//
//    /**
//     * 切换页面布局
//     * @param layoutManager 布局管理者
//     */
//    public void switchLayoutManager(LayoutManager layoutManager) {
//        int firstViewiblePosition = getFirstVisiblePosition();
//        setLayoutManager(layoutManager);
//        getLayoutManager().scrollToPosition(firstViewiblePosition);
//    }
//
//    /**
//     * 获取第一个可见条目
//     * @return 第一个可见条目
//     */
//    public int getFirstVisiblePosition() {
//        int position;
//        if (getLayoutManager() instanceof LinearLayoutManager) {
//            position = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
//        } else if (getLayoutManager() instanceof GridLayoutManager) {
//            position = ((GridLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
//        } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
//            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) getLayoutManager();
//            int[] lastPositions = layoutManager.findFirstVisibleItemPositions(new int[layoutManager.getSpanCount()]);
//            position = getMinPositions(lastPositions);
//        } else {
//            position = 0;
//        }
//        return position;
//    }
//
//    /**
//     * 获取最后一条可见数据
//     *
//     * @return 数据位置
//     */
//    public int getLastVisiblePosition() {
//        int position;
//        if (getLayoutManager() instanceof LinearLayoutManager) {
//            position = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
//        } else if (getLayoutManager() instanceof GridLayoutManager) {
//            position = ((GridLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
//        } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
//            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) getLayoutManager();
//            int[] lastPositions = layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]);
//            position = getMaxPosition(lastPositions);
//        } else {
//            position = getLayoutManager().getItemCount() - 1;
//        }
//        return position;
//    }
//
//    /**
//     * 获得当前展示最小的position
//     *
//     * @param positions 位置集合
//     * @return 返回最小的位置
//     */
//    private int getMinPositions(int[] positions) {
//        int minPosition = Integer.MAX_VALUE;
//        for (int position : positions) {
//            minPosition = Math.min(minPosition, position);
//        }
//        return minPosition;
//    }
//
//
//    /**
//     * 获取最大条目数
//     *
//     * @param positions 获取最大位置数组
//     *
//     * @return 最大的的某个位置
//     */
//    private int getMaxPosition(int[] positions) {
//        int maxPosition = Integer.MIN_VALUE;
//        for (int position : positions) {
//            maxPosition = Math.max(maxPosition, position);
//        }
//        return maxPosition;
//    }
//
//    /**
//     * 添加头部View
//     * @param resId 资源ID
//     */
//    public void addHeaderView(int resId) {
//        mAutoLoadAdapter.addHeaderView(resId);
//    }
//
//    /**
//     * 设置头部View是否展示
//     * @param enable true 显示  false 不显示
//     */
//    public void setHeaderEnable(boolean enable){
//        mAutoLoadAdapter.setHeaderEnable(enable);
//    }
//
//    /**
//     * 设置是否支持自动加载更多
//     * @param autoLoadMore true 加载更多 false 不加载
//     */
//    public void setAutoLoadMoreEnable(boolean autoLoadMore) {
//        mIsFooterEnable = autoLoadMore;
//    }
//
//    /**
//     * 通知更多的数据已经加载
//     *
//     * 每次加载完成后添加了data数据 用该方法刷新列表
//     * 不用notifyDataSetChange来刷新
//     * @param hasMore 是否还有更多了  没有的话就没有上拉加载更多
//     */
//    public void notifyMoreFinish(boolean hasMore) {
//        setAutoLoadMoreEnable(hasMore);
//        if(!hasMore)
//            setShowLoadNoMore(false);//设置不显示加载跟多
//        getAdapter().notifyItemRemoved(mLoadMorePosition);
//        mIsLoadingMore = false;
//    }
//
//
//    /**
//     * 通知更多的数据已经加载 并在最后布局中提示没有更多
//     *
//     * @param hasMore 是否还有更多了  没有的话脚布局显示没有更多了
//     */
//    public void notifyMoreFinishWithNoMoreData(boolean hasMore) {
//        setAutoLoadMoreEnable(true);
//        if(!hasMore)
//            setShowLoadNoMore(true);
//        getAdapter().notifyItemRemoved(mLoadMorePosition);
//        mIsLoadingMore = false;
//    }
//}
