package com.justplay1.shoppist.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.DaggerListItemsComponent;
import com.justplay1.shoppist.di.components.ListItemsComponent;
import com.justplay1.shoppist.di.modules.ActivityModule;
import com.justplay1.shoppist.di.modules.ListItemsModule;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.models.ListItemViewModel;
import com.justplay1.shoppist.models.ListViewModel;
import com.justplay1.shoppist.presenter.ListItemsPresenter;
import com.justplay1.shoppist.utils.Const;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.view.ListItemsView;
import com.justplay1.shoppist.view.adapters.BaseListItemGroupAdapter;
import com.justplay1.shoppist.view.adapters.ListItemAdapter;
import com.justplay1.shoppist.view.component.recyclerview.DeleteSwipeResultListener;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseHeaderHolder;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseItemHolder;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 26.03.2016.
 */
public class ListItemsFragment extends BaseEDSListFragment
        implements ShoppistRecyclerView.OnHeaderClickListener, BaseListItemGroupAdapter.SwipeEventListener<ListItemViewModel>,
        ShoppistRecyclerView.OnItemClickListener, View.OnClickListener, View.OnLongClickListener, ListItemsView, ListItemAdapter.NoteClickListener {

    @Inject
    ListItemsPresenter mPresenter;

    private ListItemsComponent mComponent;
    private FloatingActionButton mActionButton;
    private ListItemAdapter mAdapter;
    private ListItemsFragmentInteractionListener mListener;

    public static ListItemsFragment newInstance(ListViewModel parentList) {
        Bundle args = new Bundle();
        args.putParcelable(ListViewModel.class.getName(), parentList);
        ListItemsFragment fragment = new ListItemsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (ListItemsFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.onCreate(getArguments(), savedInstanceState);
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
        mPresenter.detachView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.savePosition(mAdapter.getItems());
    }

    @Override
    protected void injectDependencies() {
        super.injectDependencies();
        mComponent = DaggerListItemsComponent.builder()
                .appComponent(App.get().getAppComponent())
                .activityModule(new ActivityModule(getActivity()))
                .listItemsModule(new ListItemsModule())
                .build();
        mComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list_with_button;
    }

    @Override
    protected void initAdapter() {
        mAdapter = new ListItemAdapter(getContext(), mActionModeInteractionListener, mRecyclerView, mPreferences);
        mAdapter.setClickListener(this);
        mAdapter.setHeaderClickListener(this);
        mAdapter.setSwipeEventListener(this);
        mAdapter.setNoteClickListener(this);
    }

    @Override
    protected void init(View view) {
        super.init(view);
        mActionButton = (FloatingActionButton) view.findViewById(R.id.add_button);
        mActionButton.setBackgroundTintList(ColorStateList.valueOf(mPreferences.getColorPrimary()));
        mActionButton.setOnLongClickListener(this);
        mActionButton.setOnClickListener(this);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_button:
                mPresenter.onAddButtonClick();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        mPresenter.onAddButtonLongClick();
        return true;
    }

    @Override
    public void showData(List<Pair<HeaderViewModel, List<ListItemViewModel>>> data) {
        mAdapter.setData(data);
        mAdapter.notifyDataSetChanged();
        onExpandAll();
    }

    @Override
    public Context context() {
        return getContext();
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

    @Override
    public void openStandardMode(ListViewModel list, ListItemViewModel item) {
        mListener.openStandardMode(list, item);
    }

    @Override
    public void openQuickMode(String parentListId) {
        mListener.openQuickMode(parentListId);
    }

    @Override
    public void onItemClick(BaseItemHolder holder, int position, long id) {
        mPresenter.onListItemClick(mAdapter.getChildItem(holder.groupPosition, holder.childPosition));
    }

    @Override
    public boolean onItemLongClick(BaseItemHolder holder, int position, long id) {
        if (mPreferences.getLongItemClickAction() == 0) {
            holder.toggle();
        } else {
            mPresenter.onListItemLongClick(mAdapter.getChildItem(holder.groupPosition, holder.childPosition));
        }
        return true;
    }

    private void showListDialog(final boolean copy, List<ListViewModel> lists) {
//        ArrayList<Map<String, Object>> data = new ArrayList<>(lists.size());
//        Map<String, Object> m;
//        for (ListViewModel item : lists) {
//            if (item.getName().equals(mData.getName())) continue;
//            m = new HashMap<>();
//            m.put("name", item.getName());
//            m.put("object", item);
//            data.add(m);
//        }
//
//        String[] from = {"name"};
//        int[] to = {android.R.id.text1};
//
//        final SimpleAdapter adapter = new SimpleAdapter(getContext(), data, R.layout.dialog_list_item, from, to);
//        adapter.setDropDownViewResource(R.layout.dialog_list_item);
//
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.list_dialog, null, false);
//        final ListView listView = (ListView) view.findViewById(R.id.list_view);
//        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
//        listView.setAdapter(adapter);
//
//        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(final DialogInterface dialog, int which) {
//                switch (which) {
//                    case Dialog.BUTTON_POSITIVE:
//                        ListViewModel newList = (ListViewModel) (((Map<String, Object>) adapter.getItem(listView.getCheckedItemPosition())).get("object"));
//                        List<ListItemViewModel> items = new ArrayList<>(mAdapter.getCheckedItems());
//
//                        if (copy) {
//                            mPresenter.copyItems(newList, items);
//                            App.get().getShoppingListItemsManager().copyTo(newList, items,
//                                    new ExecutorListener<List<ListItemViewModel>>() {
//                                        @Override
//                                        public void start() {
//
//                                        }
//
//                                        @Override
//                                        public void complete(List<ListItemViewModel> result) {
//                                            dialog.dismiss();
//                                            mSelectItemsManager.closeActionMode();
//                                        }
//
//                                        @Override
//                                        public void error(Exception e) {
//
//                                        }
//                                    });
//                        } else {
//                            App.get().getShoppingListItemsManager().moveTo(newList, items,
//                                    new ExecutorListener<List<ListItemViewModel>>() {
//                                        @Override
//                                        public void start() {
//
//                                        }
//
//                                        @Override
//                                        public void complete(List<ListItemViewModel> result) {
//                                            dialog.dismiss();
//                                            mSelectItemsManager.closeActionMode(true);
//                                        }
//
//                                        @Override
//                                        public void error(Exception e) {
//
//                                        }
//                                    });
//                        }
//                        break;
//                    case Dialog.BUTTON_NEGATIVE:
//                        dialog.dismiss();
//                        break;
//                }
//            }
//        };
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setView(view);
//        builder.setTitle(R.string.title_activity_shopping_lists);
//        builder.setPositiveButton(R.string.choose, listener);
//        builder.setNegativeButton(R.string.cancel, listener);
//        AlertDialog dialog = builder.create();
//        dialog.show();
//        dialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(mPreferences.getColorPrimary());
//        dialog.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(mPreferences.getColorPrimary());
    }

    @Override
    public void onHeaderClick(BaseHeaderHolder holder, int position, long id) {

    }

    @Override
    public boolean onHeaderLongClick(BaseHeaderHolder holder, int position, long id) {
        return false;
    }

    @Override
    public void openEditScreen(ListViewModel list, ListItemViewModel item) {
        mListener.openStandardMode(list, item);
    }

    public void deleteCheckedLists() {
//        showDeleteDialog(getString(R.string.delete_the_item));
    }

    public void onReturnAllToListClick() {
        mPresenter.onReturnAllToListClick(mAdapter.getCheckedItems());
    }

    public void onStrikeOutAllClick() {
        mPresenter.onStrikeOutAllClick(mAdapter.getCheckedItems());
    }

    public void onExpandAll() {
        mRecyclerViewExpandableItemManager.expandAll();
    }

    public void onCollapseAll() {
        mRecyclerViewExpandableItemManager.collapseAll();
    }

    public void onUnCheckAllItemsClick() {
        mAdapter.unCheckAllItems(true);
    }

    public void onCheckAllItemsClick() {
        mAdapter.checkAllItems();
    }

    public void onSortByNameClick() {
        mPresenter.onSortByNameClick();
    }

    public void onSortByPriorityClick() {
        mPresenter.onSortByPriorityClick();
    }

    public void onSortByCategoryClick() {
        mPresenter.onSortByCategoryClick();
    }

    public void onSortByTimeCreatedClick() {
        mPresenter.onSortByTimeCreatedClick();
    }

    public void onSortByManualClick() {
        mPresenter.onSortByManualClick();
    }

    @Override
    public void setManualSortModeEnable(boolean enable) {
        mAdapter.setManualSortModeEnable(enable);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showEmailShareDialog(String listName) {
        StringBuilder textToSend = new StringBuilder();
        textToSend.append(listName).append("\n").append("\n");
        textToSend.append(ShoppistUtils.buildShareString(mAdapter.getCheckedItems()));
        share(textToSend.toString(), getString(R.string.shopping_list));
    }

    protected void swipeDeleteConfirm(String message, final ListItemViewModel item,
                                      final DeleteSwipeResultListener<ListItemViewModel> resultListener) {
        if (mPreferences.isNeedShowConfirmDeleteDialog()) {
//            showConfirmDeleteDialog(message, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(final DialogInterface dialog, int which) {
//                    switch (which) {
//                        case Dialog.BUTTON_POSITIVE:
//                            unCheckItem(item);
//                            resultListener.onDelete(item);
//                            dialog.dismiss();
//                        case Dialog.BUTTON_NEGATIVE:
//                            resultListener.onCancel(item);
//                            dialog.dismiss();
//                            break;
//                    }
//                }
//            });
        } else {
            unCheckItem(item);
            resultListener.onDelete(item);
        }
    }

    private void unCheckItem(ListItemViewModel item) {
        if (item.isChecked()) {
            mAdapter.deleteItemFromChecked(item.getId());
            mAdapter.updateCount(false);
        }
    }

    @Override
    public void onChildItemRemoved(final ListItemViewModel removeItem, final int groupPosition, final int childPosition) {
        swipeDeleteConfirm(getString(R.string.delete_the_item), removeItem, new DeleteSwipeResultListener<ListItemViewModel>() {
            @Override
            public void onDelete(final ListItemViewModel deleteItem) {
                mPresenter.deleteItems(Collections.singletonList(deleteItem));
            }

            @Override
            public void onCancel(ListItemViewModel deleteItem) {
                deleteItem.setPinned(false);
                mRecyclerViewExpandableItemManager.notifyChildItemChanged(groupPosition, childPosition);
            }
        });
    }

    @Override
    public void onChildItemEdit(ListItemViewModel editItem, final int groupPosition, final int childPosition) {
        mPresenter.onChildItemEdit(editItem);
        mRecyclerView.postDelayed(() -> mRecyclerViewExpandableItemManager.notifyChildItemChanged(groupPosition, childPosition), 300);
    }

    @Override
    public void onChildItemMoved(ListItemViewModel moveItem) {
        mPresenter.onChildItemMoved(moveItem);
    }

    public void onCopyItemsClick() {

    }

    public void onMoveItemsClick() {

    }

    public void onEmailShareClick() {
        mPresenter.onEmailShareClick();
    }

    public void onEditItemClick() {

    }

    public void onDeleteCheckedItemsClick() {
        showDeletingAnimation();
    }

    public boolean isMoveCopyButtonEnable() {
//        if (getDataSize() > 1) {
//            return true;
//        }
        return false;
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

    @Override
    public void showDeletingAnimation() {
        mAdapter.deleteCheckedView(deleteItems -> mPresenter.deleteItems(deleteItems));
    }

    public boolean isManualSortModeEnable() {
        return mAdapter.isManualSortModeEnable();
    }

    public void disableManualSort() {
        mAdapter.setManualSortModeEnable(false);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNoteClick(String note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle(getString(R.string.note));
        builder.setMessage(note);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public interface ListItemsFragmentInteractionListener {

        void setTitle(String title);

        void openStandardMode(ListViewModel list, ListItemViewModel item);

        void openQuickMode(String parentListId);
    }
}
