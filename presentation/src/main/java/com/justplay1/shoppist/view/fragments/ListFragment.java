package com.justplay1.shoppist.view.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.bus.ThemeUpdatedEvent;
import com.justplay1.shoppist.bus.UiEventBus;
import com.justplay1.shoppist.di.components.DaggerListsComponent;
import com.justplay1.shoppist.di.components.ListsComponent;
import com.justplay1.shoppist.di.modules.ActivityModule;
import com.justplay1.shoppist.di.modules.ListsModule;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.models.ListViewModel;
import com.justplay1.shoppist.presenter.ListPresenter;
import com.justplay1.shoppist.utils.AnimationResultListener;
import com.justplay1.shoppist.utils.Const;
import com.justplay1.shoppist.view.ListView;
import com.justplay1.shoppist.view.activities.MainActivity;
import com.justplay1.shoppist.view.adapters.ListAdapter;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseHeaderHolder;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseItemHolder;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by Mkhytar on 26.03.2016.
 */
public class ListFragment extends BaseEDSListFragment implements ShoppistRecyclerView.OnItemClickListener,
        ShoppistRecyclerView.OnHeaderClickListener, ListView, View.OnClickListener {

    @Inject
    ListPresenter mPresenter;

    private Subscription mBusSubscription;

    private ListsComponent mComponent;
    private ListsFragmentInteractionListener mListener;
    private ListAdapter mAdapter;
    private FloatingActionButton mActionButton;

    public static ListFragment newInstance() {
        Bundle args = new Bundle();

        ListFragment fragment = new ListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (ListsFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentInteractionListener");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        mPresenter.init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBusSubscription.unsubscribe();
        mPresenter.detachView();
    }

    @Override
    public void onResume() {
        super.onResume();
        UiEventBus.instanceOf().filteredObservable(ThemeUpdatedEvent.class);
        mBusSubscription = UiEventBus.instanceOf().observable().subscribe(o -> {
            mActionButton.setBackgroundTintList(ColorStateList.valueOf(mPreferences.getColorPrimary()));
            ((MainActivity) getActivity()).refreshToolbarColor();
            ((MainActivity) getActivity()).setStatusBarColor();
            mAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPreferences.isManualSortEnableForLists()) {
            mPresenter.savePosition(mAdapter.getItems());
        }
    }

    @Override
    protected void init(View view) {
        super.init(view);
        mActionButton = (FloatingActionButton) view.findViewById(R.id.add_button);
        mActionButton.setBackgroundTintList(ColorStateList.valueOf(mPreferences.getColorPrimary()));
        mActionButton.setOnClickListener(this);
    }

    @Override
    protected void injectDependencies() {
        super.injectDependencies();
        mComponent = DaggerListsComponent.builder()
                .appComponent(App.get().getAppComponent())
                .activityModule(new ActivityModule(getActivity()))
                .listsModule(new ListsModule())
                .build();
        mComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list_with_button;
    }

    @Override
    protected void initAdapter() {
        mAdapter = new ListAdapter(getContext(), mActionModeOpenCloseListener, mRecyclerView, mPreferences);
        mAdapter.setClickListener(this);
        mAdapter.setHeaderClickListener(this);
    }

    @Override
    protected void initRecyclerView(View view, Bundle savedInstanceState) {
        super.initRecyclerView(view, savedInstanceState);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    public void deleteCheckedLists() {
//        showDeleteDialog(getString(R.string.delete_the_list));
    }

    @Override
    public void onClick(View v) {
        mPresenter.onAddButtonClick();
    }

    @Override
    public void onItemClick(BaseItemHolder holder, int position, long id) {
        mListener.openListDetailScreen(mAdapter.getChildItem(holder.groupPosition, holder.childPosition));
    }

    @Override
    public void onHeaderClick(BaseHeaderHolder holder, int position, long id) {

    }

    @Override
    public boolean onHeaderLongClick(BaseHeaderHolder holder, int position, long id) {
        return false;
    }

    @Override
    public boolean onItemLongClick(BaseItemHolder holder, int position, long id) {
        if (mPreferences.getLongItemClickAction() == 0) {
            mAdapter.onCheck(holder);
        } else {
            mPresenter.onListItemLongClick(mAdapter.getChildItem(holder.groupPosition, holder.childPosition));
        }
        return true;
    }

    public void onSortByNameClick() {
        mPresenter.sortByName(mAdapter.getItems());
    }

    public void onSortByPriorityClick() {
        mPresenter.sortByPriority(mAdapter.getItems());
    }

    public void onSortByTimeCreatedClick() {
        mPresenter.sortByTimeCreated(mAdapter.getItems());
    }

    public void onSortByManualClick() {
        mPresenter.onSortByManualClick();
    }

    public void onExpandAllClick() {
        mRecyclerViewExpandableItemManager.expandAll();
    }

    public void onCollapseAllClick() {
        mRecyclerViewExpandableItemManager.collapseAll();
    }

    public void emailShare() {
//        ThreadExecutor.doBackgroundTaskAsync(new Callable<String>() {
//            @Override
//            public String call() throws Exception {
//                StringBuilder textToSend = new StringBuilder();
//                for (ListViewModel shoppingList : mAdapter.getCheckedItems()) {
//                    textToSend.append(shoppingList.getName()).append("\n").append("\n");
//                    List<ListItemViewModel> items = App.get().getTablesHolder().getShoppingListItemTable().getShoppingListItems(shoppingList.getId());
//                    textToSend.append(ShoppistUtils.buildShareString(items));
//                }
//                return textToSend.toString();
//            }
//        }, new ExecutorListener<String>() {
//            @Override
//            public void start() {
//                mProgressDialog.show();
//            }
//
//            @Override
//            public void complete(String result) {
//                mProgressDialog.dismiss();
//                share(result, getString(R.string.shopping_list));
//            }
//
//            @Override
//            public void error(Exception e) {
//                mProgressDialog.dismiss();
//            }
//        });
    }

    protected void deleteItem() {
        mAdapter.deleteCheckedView(new AnimationResultListener<ListViewModel>() {
            @Override
            public void onAnimationEnd(Collection<ListViewModel> deleteItems) {

            }
        });
    }

    @Override
    public Context context() {
        return getContext();
    }

    @Override
    public void openEditListScreen(ListViewModel list) {
        mListener.openEditScreen(list);
    }

    @Override
    public void showData(List<Pair<HeaderViewModel, List<ListViewModel>>> data) {
        mAdapter.setData(data);
        mAdapter.notifyDataSetChanged();
        onExpandAllClick();
    }

    @Override
    public void setManualSortModeEnable(boolean enable) {
        mAdapter.setManualSortModeEnable(enable);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showEmailShareDialog(String listName) {
        emailShare();
    }

    @Override
    public void showRateDialog() {
        showRateDialog(getContext());
    }

    @Override
    public void showLoading() {
        mEmptyView.showProgressBar();
    }

    @Override
    public void hideLoading() {
        mEmptyView.hideProgressBar();
    }

    @Override
    public void showError(String message) {

    }

    public boolean isManualSortModeEnable() {
        return mPresenter.isManualSortEnable();
    }

    public void disableManualSort() {
        mAdapter.setManualSortModeEnable(false);
        mAdapter.notifyDataSetChanged();
    }

    public void onEditItemClick() {
        mPresenter.onEditItemClick(mAdapter.getCheckedItems().get(0));
    }

    public void onCheckAllItemsClick() {
        mAdapter.checkAllItems();
    }

    public void onUnCheckAllItemsClick() {
        mAdapter.unCheckAllItems(true);
    }

    public void onDeleteCheckedItemsClick() {

    }

    public void onEmailShareClick() {

    }

    public boolean isEditButtonEnable() {
        boolean editFlag = true;
        if (mAdapter.getCheckedCount() != 1) {
            editFlag = false;
        }
        return editFlag;
    }

    public boolean isCheckAllButtonEnable() {
        return !mAdapter.isAllItemsChecked();
    }

    private void showRateDialog(final Context context) {
        DialogInterface.OnClickListener listener = (dialog, which) -> {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    try {
                        context.startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        context.startActivity(intent);
                    }
                    mPreferences.setNeedShowRateDialog(31);
                    dialog.dismiss();
                case Dialog.BUTTON_NEGATIVE:
                    mPreferences.setNeedShowRateDialog(31);
                    dialog.dismiss();
                    break;
                case Dialog.BUTTON_NEUTRAL:
                    mPreferences.setNeedShowRateDialog(0);
                    dialog.dismiss();
                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.rate_shoppist);
        builder.setMessage(R.string.rate_info);
        builder.setNeutralButton(R.string.no_later, listener);
        builder.setPositiveButton(R.string.rate, listener);
        builder.setNegativeButton(R.string.no, listener);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(mPreferences.getColorPrimary());
        dialog.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(mPreferences.getColorPrimary());
        dialog.getButton(Dialog.BUTTON_NEUTRAL).setTextColor(mPreferences.getColorPrimary());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (data.getStringExtra(Const.OLD_ID) != null) {
                String oldId = data.getStringExtra(Const.OLD_ID);
                String newId = data.getStringExtra(Const.NEW_ID);

                boolean checked = mAdapter.isItemChecked(oldId);
                mAdapter.deleteItemFromChecked(oldId);
                mAdapter.addToChecked(newId, checked);

                data.removeExtra(Const.NEW_ID);
                data.removeExtra(Const.OLD_ID);
            }
        }
    }

    public interface ListsFragmentInteractionListener {

        void openEditScreen(ListViewModel list);

        void openListDetailScreen(ListViewModel list);
    }
}
