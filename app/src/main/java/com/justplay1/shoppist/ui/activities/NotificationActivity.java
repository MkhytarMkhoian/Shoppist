package com.justplay1.shoppist.ui.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.adapters.NotificationAdapter;
import com.justplay1.shoppist.communication.ExecutorListener;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.models.Notification;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.ui.views.CustomProgressDialog;
import com.justplay1.shoppist.ui.views.EmptyView;
import com.justplay1.shoppist.ui.views.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.ui.views.recyclerview.holders.BaseItemHolder;
import com.justplay1.shoppist.utils.ActivityUtils;
import com.justplay1.shoppist.utils.ShoppistUtils;

/**
 * Created by Mkhytar on 11.11.2015.
 */
public class NotificationActivity extends AppCompatActivity implements
        Toolbar.OnMenuItemClickListener, ShoppistRecyclerView.OnItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor>, NotificationAdapter.SwipeEventListener {

    private Toolbar mToolbar;
    private EmptyView mEmptyView;
    private ChangeObserver mChangeObserver;
    private CustomProgressDialog mProgressDialog;
    private ShoppistRecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mWrappedAdapter;
    private NotificationAdapter mAdapter;

    private RecyclerViewSwipeManager mRecyclerViewSwipeManager;
    private RecyclerViewTouchActionGuardManager mRecyclerViewTouchActionGuardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ActivityUtils.setStatusBarColor(this);
        mChangeObserver = new ChangeObserver();

        initializeToolbar();
        initializeFrame();
        initRecyclerView();
    }

    private void initializeToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.notifications);
        mToolbar.setBackgroundColor(ShoppistPreferences.getColorPrimary());
        mToolbar.setNavigationIcon(R.drawable.ic_back_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.finishActivity(NotificationActivity.this);
            }
        });
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.inflateMenu(R.menu.notifications_toolbar);
        ViewCompat.setElevation(mToolbar, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));
    }

    protected void initializeFrame() {
        mProgressDialog = new CustomProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getString(R.string.please_wait));

        mEmptyView = (EmptyView) findViewById(android.R.id.empty);
        mEmptyView.setProgressBarColor(ShoppistPreferences.getColorPrimary());
    }

    protected void initRecyclerView() {
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (ShoppistRecyclerView) findViewById(R.id.recycler_view);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setEmptyView(mEmptyView);

        // touch guard manager  (this class is required to suppress scrolling while swipe-dismiss animation is running)
        mRecyclerViewTouchActionGuardManager = new RecyclerViewTouchActionGuardManager();
        mRecyclerViewTouchActionGuardManager.setInterceptVerticalScrollingWhileAnimationRunning(true);
        mRecyclerViewTouchActionGuardManager.setEnabled(true);

        // swipe manager
        mRecyclerViewSwipeManager = new RecyclerViewSwipeManager();

        mAdapter = new NotificationAdapter(this, null, mChangeObserver);
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

        if (getLoaderManager().getLoader(0) == null) {
            getLoaderManager().initLoader(0, ActivityUtils.getBundleWithFlag(1), this);
        } else {
            getLoaderManager().restartLoader(0, ActivityUtils.getBundleWithFlag(0), this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        int flag = ActivityUtils.getFlagFromBundle(args);
        if (flag == 1) {
            mEmptyView.showProgressBar();
        }
        return new NotificationsCursorLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.changeCursor(data);
        mEmptyView.hideProgressBar();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onItemClick(BaseItemHolder holder, int position, long id) {
        Notification notification = mAdapter.getItem(position);
        notification.setExpand(!notification.isExpand());
        mAdapter.notifyItemChanged(position);
    }

    @Override
    public boolean onItemLongClick(BaseItemHolder holder, int position, long id) {
        return false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear_all:
                App.get().getNotificationsManager().clearNotifications(new ExecutorListener<Boolean>() {
                    @Override
                    public void start() {

                    }

                    @Override
                    public void complete(Boolean result) {

                    }

                    @Override
                    public void error(Exception e) {

                    }
                });
                break;
            case R.id.action_mark_as_viewed:
                ShoppistPreferences.setLastUserSeenNotificationsTime(System.currentTimeMillis());
                mAdapter.notifyDataSetChanged();
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ShoppistUtils.setKeepScreenOn(getWindow(), ShoppistPreferences.isLockScreen());
    }

    @Override
    public void onItemRemoved(Notification removeItem, int Position) {
        App.get().getNotificationsManager().delete(removeItem, new ExecutorListener<Boolean>() {
            @Override
            public void start() {

            }

            @Override
            public void complete(Boolean result) {

            }

            @Override
            public void error(Exception e) {

            }
        });
    }

    public class ChangeObserver extends ContentObserver {
        public ChangeObserver() {
            super(new Handler());
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(boolean selfChange) {
            getLoaderManager().restartLoader(0, new Bundle(), NotificationActivity.this);
        }
    }

    @Override
    public void onDestroy() {
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
        mAdapter = null;
        mLayoutManager = null;
        super.onDestroy();
    }

    static class NotificationsCursorLoader extends CursorLoader {

        public NotificationsCursorLoader(Context context) {
            super(context);
        }

        @Override
        public Cursor loadInBackground() {
            return App.get().getNotificationsManager().getNotificationsCursor();
        }

    }
}
