package com.justplay1.shoppist.ui.activities;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.HapticFeedbackConstants;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.adapters.SearchAdapter;
import com.justplay1.shoppist.communication.ExecutorListener;
import com.justplay1.shoppist.cursors.SearchLoader;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.models.Currency;
import com.justplay1.shoppist.models.Priority;
import com.justplay1.shoppist.models.Product;
import com.justplay1.shoppist.models.ShoppingList;
import com.justplay1.shoppist.models.ShoppingListItem;
import com.justplay1.shoppist.models.Status;
import com.justplay1.shoppist.ui.fragments.dialog.BaseDialogFragment;
import com.justplay1.shoppist.ui.fragments.dialog.GoodsEditorDialogFragment;
import com.justplay1.shoppist.ui.fragments.dialog.UnitsEditorDialogFragment;
import com.justplay1.shoppist.ui.views.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.ui.views.recyclerview.holders.BaseItemHolder;
import com.justplay1.shoppist.ui.views.search.FloatingSearchView;
import com.justplay1.shoppist.utils.ActivityUtils;
import com.justplay1.shoppist.utils.DialogUtils;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Mkhytar on 11.02.2016.
 */
public class SearchActivity extends AppCompatActivity implements FloatingSearchView.OnIconClickListener,
        FloatingSearchView.OnSearchListener, ActionMenuView.OnMenuItemClickListener,
        FloatingSearchView.OnSearchFocusChangedListener, FloatingSearchView.OnContainerTouchClickListener,
        LoaderManager.LoaderCallbacks<Map<String, Product>>, ShoppistRecyclerView.OnItemClickListener {

    private static final int REQ_CODE_SPEECH_INPUT = 111;
    public static final int CONTEXT_QUICK_ADD_GOODS_TO_LIST = 222;
    public static final int CONTEXT_QUICK_SEARCH_IN_GOODS_LIST = 333;
    public static final String CONTEXT_TYPE = "context_type";

    private FloatingSearchView mSearchView;
    private SearchAdapter mAdapter;
    private int mContextType;
    private ShoppingList mParentList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seach);
        ActivityUtils.setStatusBarColor(this, FloatingSearchView.DEFAULT_BACKGROUND_COLOR);

        mContextType = CONTEXT_QUICK_SEARCH_IN_GOODS_LIST;
        Intent intent = getIntent();
        if (intent != null) {
            mContextType = intent.getIntExtra(CONTEXT_TYPE, CONTEXT_QUICK_SEARCH_IN_GOODS_LIST);
            switch (mContextType) {
                case CONTEXT_QUICK_ADD_GOODS_TO_LIST:
                    mParentList = intent.getParcelableExtra(ShoppingList.class.getName());
                    break;
                case CONTEXT_QUICK_SEARCH_IN_GOODS_LIST:

                    break;
            }
        }
        init(savedInstanceState);
//        if (savedInstanceState == null) {
//            showWithAnimation();
//        }
    }

    private void showWithAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSearchView.setVisibility(View.INVISIBLE);
            mSearchView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    mSearchView.removeOnLayoutChangeListener(this);
                    if (getIntent() != null) {
                        circularRevealActivity(getIntent().getIntExtra("cx", 0), getIntent().getIntExtra("cy", 0));
                    }
                }
            });
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void circularRevealActivity(int cx, int cy) {
        float finalRadius = Math.max(mSearchView.getWidth(), mSearchView.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(mSearchView, cx, cy, 0, finalRadius);
        circularReveal.setDuration(800);

        // make the view visible and start the animation
        mSearchView.setVisibility(View.VISIBLE);
        circularReveal.start();
    }

    private void init(@Nullable Bundle savedInstanceState) {
        mAdapter = new SearchAdapter(this, mContextType);
        mAdapter.setClickListener(this);

        mSearchView = (FloatingSearchView) findViewById(R.id.search_view);
        mSearchView.setAdapter(mAdapter);
        updateNavigationIcon();
        mSearchView.showIcon(true);
        mSearchView.setOnIconClickListener(this);
        mSearchView.setOnSearchListener(this);
        mSearchView.setOnMenuItemClickListener(this);
        mSearchView.setOnSearchFocusChangedListener(this);
        mSearchView.setOnContainerTouchClickListener(this);
        mSearchView.setText(null);
        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {
                showVoiceButton(query.length() == 0 && mSearchView.isActivated());
                showClearButton(query.length() > 0 && mSearchView.isActivated());
                search(query.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mSearchView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                mSearchView.removeOnLayoutChangeListener(this);
                mSearchView.setActivated(true);
                if (getSupportLoaderManager().getLoader(SearchLoader.ID) == null) {
                    getSupportLoaderManager().initLoader(SearchLoader.ID, ActivityUtils.getBundleWithFlag(1), SearchActivity.this).forceLoad();
                } else {
                    getSupportLoaderManager().restartLoader(SearchLoader.ID, ActivityUtils.getBundleWithFlag(0), SearchActivity.this).forceLoad();
                }
            }
        });
        mSearchView.setHint(getString(R.string.search));
    }

    private void updateNavigationIcon() {
        Context context = mSearchView.getContext();
        Drawable drawable = getResources().getDrawable(R.drawable.ic_arrow_back);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, ViewUtils.getThemeAttrColor(context, R.attr.colorControlNormal));
        mSearchView.setIcon(drawable);
    }

    private void search(String query) {
        //   showProgressBar(mSearchView.isActivated());
        mAdapter.getFilter().filter(query);
    }

    private void showProgressBar(boolean show) {
        mSearchView.getMenu().findItem(R.id.menu_progress_bar).setVisible(show);
    }

    private void showClearButton(boolean show) {
        mSearchView.getMenu().findItem(R.id.menu_clear).setVisible(show);
    }

    private void showVoiceButton(boolean show) {
        mSearchView.getMenu().findItem(R.id.menu_voice_search).setVisible(show);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mSearchView.setActivated(true);
                    mSearchView.setText(result.get(0));
                }
                break;
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onNavigationClick() {
        finish();
    }

    @Override
    public void onSearchAction(CharSequence charSequence) {
        mSearchView.setActivated(false);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                mSearchView.setText(null);
                mSearchView.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                break;
            case R.id.menu_voice_search:
                ActivityUtils.startTextToSpeech(SearchActivity.this, null, REQ_CODE_SPEECH_INPUT);
                break;
        }
        return true;
    }

    @Override
    public void onFocusChanged(boolean focused) {
        boolean textEmpty = mSearchView.getText().length() == 0;
        showClearButton(focused && !textEmpty);
        showVoiceButton(focused && textEmpty);
        if (!focused) showProgressBar(false);
        mSearchView.showLogo(!focused && textEmpty);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        finish();
        return true;
    }

    @Override
    public Loader<Map<String, Product>> onCreateLoader(int id, Bundle args) {
        int flag = ActivityUtils.getFlagFromBundle(args);
        if (flag == 1) {
//            showProgressBar(true);
        }
        return new SearchLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Map<String, Product>> loader, Map<String, Product> data) {
//        showProgressBar(false);
        mAdapter.setData(data);
    }

    @Override
    public void onLoaderReset(Loader<Map<String, Product>> loader) {

    }

    @Override
    public void onItemClick(BaseItemHolder holder, int position, long id) {
        switch (mContextType) {
            case CONTEXT_QUICK_ADD_GOODS_TO_LIST:
                addItem(mAdapter.getItem(position));
                break;
            case CONTEXT_QUICK_SEARCH_IN_GOODS_LIST:
                Product product = mAdapter.getItem(position);
                if (product.getId().equals("000")) {
                    showEditGoodsDialog(GoodsEditorDialogFragment.newInstance(product.getName()));
                } else {
                    showEditGoodsDialog(GoodsEditorDialogFragment.newInstance(mAdapter.getItem(position)));
                }
                break;
        }
    }

    @Override
    public boolean onItemLongClick(BaseItemHolder holder, int position, long id) {
        return false;
    }

    private void addItem(Product product) {
        ShoppingListItem shoppingListItem = new ShoppingListItem();
        shoppingListItem.setParentListId(mParentList.getId());
        shoppingListItem.setNote("");
        shoppingListItem.setDirty(true);
        shoppingListItem.setName(product.getName());
        shoppingListItem.setTimeCreated(System.currentTimeMillis());
        shoppingListItem.setStatus(Status.NOT_DONE);
        shoppingListItem.setId(UUID.nameUUIDFromBytes((product.getName() + UUID.randomUUID()).getBytes()).toString());
        shoppingListItem.setUnit(product.getUnit());
        Currency currency = new Currency();
        currency.setId(Currency.NO_CURRENCY_ID);
        shoppingListItem.setCurrency(currency);
        shoppingListItem.setPrice(0);
        shoppingListItem.setQuantity(0);
        shoppingListItem.setCategory(product.getCategory());
        shoppingListItem.setPriority(Priority.NO_PRIORITY);
        addItem(shoppingListItem);
    }

    private void addItem(final ShoppingListItem item) {
        App.get().getShoppingListItemsManager().add(item, new ExecutorListener<ShoppingListItem>() {
            @Override
            public void start() {

            }

            @Override
            public void complete(ShoppingListItem result) {
                mSearchView.fadeInSignal(result.getCategory().getColor());
            }

            @Override
            public void error(Exception e) {
                DialogUtils.showErrorDialog(SearchActivity.this,
                        ShoppistUtils.getParseMessageFromException(SearchActivity.this, e.getMessage()));
            }
        });
    }

    private void showEditGoodsDialog(GoodsEditorDialogFragment dialog) {
        FragmentManager fm = getSupportFragmentManager();
        dialog.setCompleteListener(new BaseDialogFragment.OnCompleteListener<Product>() {
            @Override
            public void onComplete(Product item, boolean isUpdate) {
                getSupportLoaderManager().restartLoader(SearchLoader.ID, ActivityUtils.getBundleWithFlag(0), SearchActivity.this).forceLoad();
            }
        });
        dialog.show(fm, UnitsEditorDialogFragment.class.getName());
    }
}
