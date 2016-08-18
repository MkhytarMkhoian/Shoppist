package com.justplay1.shoppist.ui.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewCompat;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.adapters.BaseListItemGroupAdapter;
import com.justplay1.shoppist.models.BaseModel;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.ActivityUtils;
import com.justplay1.shoppist.ui.views.recyclerview.DeleteSwipeResultListener;
import com.justplay1.shoppist.ui.views.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.ui.views.recyclerview.holders.BaseDraggableSwipeableItemViewHolder;
import com.justplay1.shoppist.ui.views.recyclerview.holders.BaseHeaderHolder;

/**
 * Created by Mkhitar on 06.02.2015.
 */
public abstract class BaseListItemActivity<T extends BaseModel, K extends BaseModel, GVH extends BaseHeaderHolder, CVH extends BaseDraggableSwipeableItemViewHolder>
        extends BaseActivity<T>
        implements ShoppistRecyclerView.OnHeaderClickListener, BaseListItemGroupAdapter.SwipeEventListener<T> {

    protected RecyclerViewExpandableItemManager mRecyclerViewExpandableItemManager;
    protected RecyclerViewDragDropManager mRecyclerViewDragDropManager;
    protected RecyclerViewSwipeManager mRecyclerViewSwipeManager;
    protected RecyclerViewTouchActionGuardManager mRecyclerViewTouchActionGuardManager;

    protected BaseListItemGroupAdapter<T, GVH, CVH> mAdapter;

    protected K mData;
    protected abstract int getDataSize();

    @Override
    protected void initFrame(Bundle savedInstanceState) {
        super.initFrame(savedInstanceState);
    }

    protected void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mData != null) {
            mToolbar.setTitle(mData.getName());
        }
        ViewCompat.setElevation(mToolbar, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));
        mToolbar.setBackgroundColor(ShoppistPreferences.getColorPrimary());
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.setNavigationIcon(R.drawable.ic_back_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishWithResult();
            }
        });
        mToolbar.inflateMenu(R.menu.shopping_list_toolbar);
        mToolbar.getMenu().findItem(R.id.action_settings).setVisible(false);
    }

    @Override
    protected void initRecyclerView(Bundle savedInstanceState) {
        super.initRecyclerView(savedInstanceState);

        initAdapter();

        final Parcelable eimSavedState = (savedInstanceState != null) ? savedInstanceState.getParcelable(SAVED_STATE_EXPANDABLE_ITEM_MANAGER) : null;
        mRecyclerViewExpandableItemManager = new RecyclerViewExpandableItemManager(eimSavedState);

        // touch guard manager  (this class is required to suppress scrolling while swipe-dismiss animation is running)
        mRecyclerViewTouchActionGuardManager = new RecyclerViewTouchActionGuardManager();
        mRecyclerViewTouchActionGuardManager.setInterceptVerticalScrollingWhileAnimationRunning(true);
        mRecyclerViewTouchActionGuardManager.setEnabled(true);

        // drag & drop manager
        mRecyclerViewDragDropManager = new RecyclerViewDragDropManager();
        mRecyclerViewDragDropManager.setDraggingItemShadowDrawable(
                (NinePatchDrawable) getResources().getDrawable(R.drawable.material_shadow_z3));

        // swipe manager
        mRecyclerViewSwipeManager = new RecyclerViewSwipeManager();

        mWrappedAdapter = mRecyclerViewExpandableItemManager.createWrappedAdapter(mAdapter);       // wrap for expanding
        mWrappedAdapter = mRecyclerViewDragDropManager.createWrappedAdapter(mWrappedAdapter);           // wrap for dragging
        mWrappedAdapter = mRecyclerViewSwipeManager.createWrappedAdapter(mWrappedAdapter);      // wrap for swiping

        final GeneralItemAnimator animator = new SwipeDismissItemAnimator();
        animator.setSupportsChangeAnimations(false);

        mRecyclerView.setAdapter(mWrappedAdapter);  // requires *wrapped* adapter
        mRecyclerView.setItemAnimator(animator);

        // additional decorations
        //noinspection StatementWithEmptyBody
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mRecyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) getResources().getDrawable(R.drawable.material_shadow_z1)));
        }

        // priority: TouchActionGuard > Swipe > DragAndDrop > ExpandableItem
        mRecyclerViewTouchActionGuardManager.attachRecyclerView(mRecyclerView);
        mRecyclerViewSwipeManager.attachRecyclerView(mRecyclerView);
        mRecyclerViewDragDropManager.attachRecyclerView(mRecyclerView);
        mRecyclerViewExpandableItemManager.attachRecyclerView(mRecyclerView);

        mAdapter.setClickListener(this);
        mAdapter.setHeaderClickListener(this);
        mAdapter.setSwipeEventListener(this);
        mAdapter.setRecyclerViewExpandableItemManager(mRecyclerViewExpandableItemManager);

        loadData();
    }

    @Override
    public void onPause() {
        mRecyclerViewDragDropManager.cancelDrag();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save current state to support screen rotation, etc...
        if (mRecyclerViewExpandableItemManager != null) {
            outState.putParcelable(
                    SAVED_STATE_EXPANDABLE_ITEM_MANAGER,
                    mRecyclerViewExpandableItemManager.getSavedState());
        }
    }

    @Override
    public void onDestroy() {
        if (mRecyclerViewDragDropManager != null) {
            mRecyclerViewDragDropManager.release();
            mRecyclerViewDragDropManager = null;
        }

        if (mRecyclerViewSwipeManager != null) {
            mRecyclerViewSwipeManager.release();
            mRecyclerViewSwipeManager = null;
        }

        if (mRecyclerViewTouchActionGuardManager != null) {
            mRecyclerViewTouchActionGuardManager.release();
            mRecyclerViewTouchActionGuardManager = null;
        }

        if (mRecyclerViewExpandableItemManager != null) {
            mRecyclerViewExpandableItemManager.release();
            mRecyclerViewExpandableItemManager = null;
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

    @Override
    public void onCreateActionMode(ActionMode actionMode, Menu menu) {
        MenuItem move = menu.findItem(R.id.action_move);
        MenuItem copy = menu.findItem(R.id.action_copy);
        if (getDataSize() > 1) {
            move.setEnabled(true);
            copy.setEnabled(true);
        } else {
            move.setEnabled(false);
            copy.setEnabled(false);
        }
    }

    @Override
    public void onBackPressed() {
        if (mSelectItemsManager.isActionModeShowing()) {
            super.onBackPressed();
        } else {
            finishWithResult();
        }
    }

    private void finishWithResult() {
        Intent data = new Intent();
        data.putExtra(ActivityUtils.NEW_DATA, true);
        ActivityUtils.finishActivityWithResult(this, RESULT_OK, data);
    }

    protected void swipeDeleteConfirm(String message, final T item, final DeleteSwipeResultListener<T> resultListener) {
        if (ShoppistPreferences.isNeedShowConfirmDeleteDialog()) {
            showConfirmDeleteDialog(message, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, int which) {
                    switch (which) {
                        case Dialog.BUTTON_POSITIVE:
                            unCheckItem(item);
                            resultListener.onDelete(item);
                            dialog.dismiss();
                        case Dialog.BUTTON_NEGATIVE:
                            resultListener.onCancel(item);
                            dialog.dismiss();
                            break;
                    }
                }
            });
        } else {
            unCheckItem(item);
            resultListener.onDelete(item);
        }
    }

    private void unCheckItem(T item) {
        if (item.isChecked()){
            mSelectItemsManager.deleteItemFromChecked(item.getId());
            mSelectItemsManager.updateActionMode(false);
        }
    }
}
