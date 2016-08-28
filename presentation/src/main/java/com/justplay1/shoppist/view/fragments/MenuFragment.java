package com.justplay1.shoppist.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.DaggerMenuComponent;
import com.justplay1.shoppist.di.components.MenuComponent;
import com.justplay1.shoppist.di.modules.ActivityModule;
import com.justplay1.shoppist.presenter.MenuPresenter;
import com.justplay1.shoppist.view.MenuView;
import com.justplay1.shoppist.view.activities.SettingsActivity;
import com.parse.ParseUser;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 09.07.2016.
 */
public class MenuFragment extends BaseFragment implements NavigationView.OnNavigationItemSelectedListener, MenuView {

    @Inject
    MenuPresenter mPresenter;

    private MenuComponent mComponent;
    private MenuFragmentInteraction mListener;
    private NavigationView mNavigationView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (MenuFragmentInteraction) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentInteractionListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        mPresenter.attachView(this);
        mPresenter.init();
    }

    @Override
    protected void injectDependencies() {
        super.injectDependencies();
        mComponent = DaggerMenuComponent.builder()
                .appComponent(App.get().getAppComponent())
                .activityModule(new ActivityModule(getActivity()))
                .build();
        mComponent.inject(this);
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
    protected void init(View view) {
        mNavigationView = (NavigationView) view.findViewById(R.id.drawer_menu);
        mNavigationView.setNavigationItemSelectedListener(this);

        View header = LayoutInflater.from(getContext()).inflate(R.layout.drawer_header, null);
        mNavigationView.addHeaderView(header);
        mNavigationView.getHeaderView(0).setOnClickListener(v -> mPresenter.onHeaderClick());
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.goods_button:
                mListener.onGoodsClick();
                break;
            case R.id.category_button:
                mListener.onCategoryClick();
                break;
            case R.id.currencies_button:
                mListener.onCurrencyClick();
                break;
            case R.id.units_button:
                mListener.onUnitsClick();
                break;
            case R.id.settings_button:
                mListener.onSettingClick(0);
                break;
            case R.id.feedback_button:
                mListener.onFeedbackClick();
                break;
            case R.id.notifications:
                mListener.onNotificationClick();
                break;
        }
        return true;
    }

    @Override
    public void setDefaultAccountTitle() {
//        TextView usernameView = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.username);
//        usernameView.setText(getString(R.string.tap_to_sing_up_or_login));
    }

    @Override
    public void openLogin() {
        mListener.onLoginClick();
    }

    @Override
    public void openAccountSetting() {
        mListener.onSettingClick(SettingsActivity.ACCOUNT_SETTING);
    }

    @Override
    public Context context() {
        return getContext();
    }

    @Override
    public void setAccount(String username) {
//        TextView usernameView = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.username);
//        usernameView.setText(username);
    }

    @Override
    public void setNotificationBadge(int count) {
        TextView view = (TextView) mNavigationView.getMenu().findItem(R.id.notifications).getActionView();
        view.setText(count > 0 ? String.valueOf(count) : null);
    }

    public interface MenuFragmentInteraction {
        void onLoginClick();

        void onCategoryClick();

        void onCurrencyClick();

        void onNotificationClick();

        void onSettingClick(int settingId);

        void onFeedbackClick();

        void onGoodsClick();

        void onUnitsClick();
    }
}
