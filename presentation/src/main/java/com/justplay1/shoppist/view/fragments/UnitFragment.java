package com.justplay1.shoppist.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.DaggerUnitsComponent;
import com.justplay1.shoppist.di.components.UnitsComponent;
import com.justplay1.shoppist.di.modules.ActivityModule;
import com.justplay1.shoppist.di.modules.UnitsModule;
import com.justplay1.shoppist.models.UnitViewModel;
import com.justplay1.shoppist.presenter.UnitsPresenter;
import com.justplay1.shoppist.utils.Const;
import com.justplay1.shoppist.view.UnitsView;
import com.justplay1.shoppist.view.adapters.UnitsAdapter;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseItemHolder;
import com.justplay1.shoppist.view.fragments.dialog.AddUnitsDialogFragment;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 26.03.2016.
 */
public class UnitFragment extends BaseListFragment
        implements UnitsView, ShoppistRecyclerView.OnItemClickListener, View.OnClickListener {

    @Inject
    UnitsPresenter mPresenter;

    private UnitsComponent mComponent;
    private FloatingActionButton mActionButton;
    private UnitsAdapter mAdapter;

    public static UnitFragment newInstance() {

        Bundle args = new Bundle();

        UnitFragment fragment = new UnitFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list_with_button;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.onCreate(null, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        mPresenter.init();
    }

    @Override
    protected void initAdapter() {
        mAdapter = new UnitsAdapter(getContext(), mActionModeOpenCloseListener, mRecyclerView);
        mAdapter.setClickListener(this);
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
        mComponent = DaggerUnitsComponent.builder()
                .appComponent(App.get().getAppComponent())
                .activityModule(new ActivityModule(getActivity()))
                .unitsModule(new UnitsModule())
                .build();
        mComponent.inject(this);
    }

    @Override
    protected void initRecyclerView(View view, Bundle savedInstanceState) {
        super.initRecyclerView(view, savedInstanceState);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        mPresenter.onAddButtonClick();
    }

    @Override
    public void onItemClick(BaseItemHolder holder, int position, long id) {
        mPresenter.onListItemClick(mAdapter.getItem(position));
    }

    @Override
    public boolean onItemLongClick(BaseItemHolder holder, int position, long id) {
        mPresenter.onListItemLongClick(mAdapter.getItem(position));
        return true;
    }

    @Override
    public void openUnitAddDialog(UnitViewModel unit) {
        FragmentManager fm = getFragmentManager();
        AddUnitsDialogFragment dialog = AddUnitsDialogFragment.newInstance(unit);
        dialog.setCompleteListener(isUpdate -> mPresenter.loadData());
        dialog.show(fm, AddUnitsDialogFragment.class.getName());
    }

    public void onCheckAllItemsClick() {
        mAdapter.checkAllItems();
    }

    public void onUnCheckAllItemsClick() {
        mAdapter.unCheckAllItems(true);
    }

    public void onDeleteCheckedItemsClick() {
        //  showDeleteDialog(getString(R.string.delete_the_category));
    }

    public boolean isDeleteButtonEnable() {
        boolean deleteFlag = true;
        for (UnitViewModel unit : mAdapter.getCheckedItems()) {
            if (unit.getId().equals(UnitViewModel.NO_UNIT_ID)) {
                deleteFlag = false;
            }
        }
        return deleteFlag;
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
    public void showData(List<UnitViewModel> data) {
        mAdapter.setData(data);
        mAdapter.notifyDataSetChanged();
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
}
