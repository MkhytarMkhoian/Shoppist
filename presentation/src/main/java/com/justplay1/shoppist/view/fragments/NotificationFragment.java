package com.justplay1.shoppist.view.fragments;

import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.DaggerNotificationComponent;
import com.justplay1.shoppist.di.components.NotificationComponent;
import com.justplay1.shoppist.di.modules.ActivityModule;
import com.justplay1.shoppist.di.modules.NotificationModule;
import com.justplay1.shoppist.models.NotificationViewModel;
import com.justplay1.shoppist.presenter.NotificationPresenter;
import com.justplay1.shoppist.view.NotificationsView;
import com.justplay1.shoppist.view.adapters.NotificationAdapter;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseItemHolder;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 26.03.2016.
 */
public class NotificationFragment extends BaseListFragment
        implements NotificationsView, ShoppistRecyclerView.OnItemClickListener,
        NotificationAdapter.SwipeEventListener {

    @Inject
    NotificationPresenter mPresenter;

    private NotificationComponent mComponent;
    private NotificationAdapter mAdapter;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewSwipeManager mRecyclerViewSwipeManager;
    private RecyclerViewTouchActionGuardManager mRecyclerViewTouchActionGuardManager;

    public static NotificationFragment newInstance() {

        Bundle args = new Bundle();

        NotificationFragment fragment = new NotificationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        mPresenter.init();
    }

    @Override
    protected void injectDependencies() {
        super.injectDependencies();
        mComponent = DaggerNotificationComponent.builder()
                .appComponent(App.get().getAppComponent())
                .activityModule(new ActivityModule(getActivity()))
                .notificationModule(new NotificationModule())
                .build();
        mComponent.inject(this);
    }

    @Override
    protected void initRecyclerView(View view, Bundle savedInstanceState) {
        super.initRecyclerView(view, savedInstanceState);

        // touch guard manager  (this class is required to suppress scrolling while swipe-dismiss animation is running)
        mRecyclerViewTouchActionGuardManager = new RecyclerViewTouchActionGuardManager();
        mRecyclerViewTouchActionGuardManager.setInterceptVerticalScrollingWhileAnimationRunning(true);
        mRecyclerViewTouchActionGuardManager.setEnabled(true);

        // swipe manager
        mRecyclerViewSwipeManager = new RecyclerViewSwipeManager();


        mWrappedAdapter = mRecyclerViewSwipeManager.createWrappedAdapter(mAdapter);

        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();
        animator.setSupportsChangeAnimations(false);

        mRecyclerView.setAdapter(mWrappedAdapter);  // requires *wrapped* adapter
        mRecyclerView.setItemAnimator(animator);

        // additional decorations
        //noinspection StatementWithEmptyBody
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mRecyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) getResources().getDrawable(R.drawable.material_shadow_z1)));
        }

        mRecyclerViewTouchActionGuardManager.attachRecyclerView(mRecyclerView);
        mRecyclerViewSwipeManager.attachRecyclerView(mRecyclerView);

        mAdapter.setClickListener(this);
        mAdapter.setSwipeEventListener(this);
    }

    @Override
    protected void initAdapter() {
        mAdapter = new NotificationAdapter(this, mPreferences, getContext());
    }

    @Override
    public void onItemClick(BaseItemHolder holder, int position, long id) {
        NotificationViewModel notification = mAdapter.getItem(position);
        notification.setExpand(!notification.isExpand());
        mAdapter.notifyItemChanged(position);
    }

    @Override
    public boolean onItemLongClick(BaseItemHolder holder, int position, long id) {
        return false;
    }

    @Override
    public void onItemRemoved(NotificationViewModel removeItem, int position) {
        mPresenter.onItemRemoved(removeItem, position);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();

        if (mRecyclerViewSwipeManager != null) {
            mRecyclerViewSwipeManager.release();
            mRecyclerViewSwipeManager = null;
        }

        if (mRecyclerViewTouchActionGuardManager != null) {
            mRecyclerViewTouchActionGuardManager.release();
            mRecyclerViewTouchActionGuardManager = null;
        }

        if (mRecyclerView != null) {
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setAdapter(null);
            mRecyclerView = null;
        }

        if (mWrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(mWrappedAdapter);
            mWrappedAdapter = null;
        }
    }

    @Override
    public void showData(List<NotificationViewModel> data) {
        mAdapter.setData(data);
    }

    @Override
    public void showLoading() {
        mProgressDialog.show();
    }

    @Override
    public void hideLoading() {
        mProgressDialog.dismiss();
    }

    @Override
    public void showError(String message) {

    }

    public void onClearAllClick() {
        mPresenter.onClearAllClick();
    }


    public void onMarkAsViewedClick() {
        mPresenter.onMarkAsViewedClick();
    }

    @Override
    public void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }
}
