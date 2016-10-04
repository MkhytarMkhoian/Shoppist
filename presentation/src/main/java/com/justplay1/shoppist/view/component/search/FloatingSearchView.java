/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.justplay1.shoppist.view.component.search;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.utils.ViewUtils;
import com.justplay1.shoppist.view.component.search.internal.RoundRectDrawableWithShadow;
import com.justplay1.shoppist.view.component.search.internal.SuggestionItemDecorator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.justplay1.shoppist.view.component.search.internal.RoundRectDrawableWithShadow.BOTTOM;
import static com.justplay1.shoppist.view.component.search.internal.RoundRectDrawableWithShadow.LEFT;
import static com.justplay1.shoppist.view.component.search.internal.RoundRectDrawableWithShadow.RIGHT;
import static com.justplay1.shoppist.view.component.search.internal.RoundRectDrawableWithShadow.TOP;

public class FloatingSearchView extends RelativeLayout {

    public static final int DEFAULT_BACKGROUND_COLOR = 0x90000000;
    private static final int DEFAULT_CONTENT_COLOR = 0xfff0f0f0;

    private static final int DEFAULT_RADIUS = 2;
    private static final int DEFAULT_ELEVATION = 2;
    private static final int DEFAULT_MAX_ELEVATION = 2;

    private static final long DEFAULT_DURATION_ENTER = 300;
    private static final long DEFAULT_DURATION_EXIT = 400;

    private static final Interpolator DECELERATE = new DecelerateInterpolator(3f);
    private static final Interpolator ACCELERATE = new AccelerateInterpolator(2f);

    private RecyclerView.AdapterDataObserver adapterObserver = new android.support.v7.widget.RecyclerView.AdapterDataObserver() {

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            onChanged();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            onChanged();
        }

        @Override
        public void onChanged() {
            updateSuggestionsVisibility();
        }
    };

    public interface OnSearchListener {
        void onSearchAction(CharSequence text);
    }

    public interface OnIconClickListener {
        void onNavigationClick();
    }

    public interface OnSearchFocusChangedListener {
        void onFocusChanged(boolean focused);
    }

    public interface OnContainerTouchClickListener {
        boolean onTouch(View v, MotionEvent event);
    }

    final private LogoEditText searchInput;
    final private ImageView navButtonView;
    final private RecyclerView recyclerView;
    final private ViewGroup searchContainer;
    final private View divider;
    final private ActionMenuView actionMenu;

    final private Activity activity;

    final private RoundRectDrawableWithShadow searchBackground;
    final private SuggestionItemDecorator cardDecorator;

    final private List<Integer> alwaysShowingMenu = new ArrayList<>();

    private OnContainerTouchClickListener onContainerTouchClickListener;
    private OnSearchFocusChangedListener focusListener;
    private OnIconClickListener navigationClickListener;
    private Drawable backgroundDrawable;
    private boolean suggestionsShown;

    public FloatingSearchView(Context context) {
        this(context, null);
    }

    public FloatingSearchView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.floatingSearchViewStyle);
    }

    public FloatingSearchView(Context context, AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        activity = getActivity();
        setFocusable(true);
        setFocusableInTouchMode(true);

        inflate(getContext(), R.layout.layout_fsv_floating_search, this);

        searchInput = (LogoEditText) findViewById(R.id.fsv_search_text);
        navButtonView = (ImageView) findViewById(R.id.fsv_search_action_navigation);
        recyclerView = (RecyclerView) findViewById(R.id.fsv_suggestions_list);
        divider = findViewById(R.id.fsv_suggestions_divider);
        searchContainer = (ViewGroup) findViewById(R.id.fsv_search_container);
        actionMenu = (ActionMenuView) findViewById(R.id.fsv_search_action_menu);

        searchBackground = new RoundRectDrawableWithShadow(
                DEFAULT_CONTENT_COLOR, ViewUtils.dpToPx(DEFAULT_RADIUS),
                ViewUtils.dpToPx(DEFAULT_ELEVATION),
                ViewUtils.dpToPx(DEFAULT_MAX_ELEVATION));
        searchBackground.setAddPaddingForCorners(true);

        cardDecorator = new SuggestionItemDecorator(searchBackground.mutate());

        applyXmlAttributes(attrs, defStyleAttr, 0);
        setupViews();
    }

    private void applyXmlAttributes(AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.FloatingSearchView, defStyleAttr, defStyleRes);

        // Search bar width
        View suggestionsContainer = findViewById(R.id.fsv_suggestions_container);
        int searchBarWidth = a.getDimensionPixelSize(R.styleable.FloatingSearchView_fsv_searchBarWidth,
                searchContainer.getLayoutParams().width);
        searchContainer.getLayoutParams().width = searchBarWidth;
        suggestionsContainer.getLayoutParams().width = searchBarWidth;

        // Divider
        ViewUtils.setBackground(divider, a.getDrawable(R.styleable.FloatingSearchView_android_divider));
        int dividerHeight = a.getDimensionPixelSize(R.styleable.FloatingSearchView_android_dividerHeight, -1);

        MarginLayoutParams dividerLP = (MarginLayoutParams) divider.getLayoutParams();

        if (divider.getBackground() != null && dividerHeight != -1)
            dividerLP.height = dividerHeight;

        float maxShadowSize = searchBackground.getMaxShadowSize();
        float cornerRadius = searchBackground.getCornerRadius();
        int horizontalPadding = (int) (RoundRectDrawableWithShadow.calculateHorizontalPadding(
                maxShadowSize, cornerRadius, false) + .5f);

        dividerLP.setMargins(horizontalPadding, dividerLP.topMargin, horizontalPadding, dividerLP.bottomMargin);
        divider.setLayoutParams(dividerLP);

        // Content inset
        MarginLayoutParams searchParams = (MarginLayoutParams) searchInput.getLayoutParams();

        int contentInsetStart = a.getDimensionPixelSize(R.styleable.FloatingSearchView_contentInsetStart,
                MarginLayoutParamsCompat.getMarginStart(searchParams));
        int contentInsetEnd = a.getDimensionPixelSize(R.styleable.FloatingSearchView_contentInsetEnd,
                MarginLayoutParamsCompat.getMarginEnd(searchParams));

        MarginLayoutParamsCompat.setMarginStart(searchParams, contentInsetStart);
        MarginLayoutParamsCompat.setMarginEnd(searchParams, contentInsetEnd);

        // anything else
        setLogo(a.getDrawable(R.styleable.FloatingSearchView_logo));
        setContentBackgroundColor(a.getColor(R.styleable.FloatingSearchView_fsv_contentBackgroundColor, DEFAULT_CONTENT_COLOR));
        setRadius(a.getDimensionPixelSize(R.styleable.FloatingSearchView_fsv_cornerRadius, ViewUtils.dpToPx(DEFAULT_RADIUS)));
        inflateMenu(a.getResourceId(R.styleable.FloatingSearchView_fsv_menu, 0));
        setPopupTheme(a.getResourceId(R.styleable.FloatingSearchView_popupTheme, 0));
        setHint(a.getString(R.styleable.FloatingSearchView_android_hint));
        setIcon(a.getDrawable(R.styleable.FloatingSearchView_fsv_icon));

        a.recycle();
    }

    private void setupViews() {
        searchContainer.setLayoutTransition(getDefaultLayoutTransition());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            searchContainer.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        ViewUtils.setBackground(searchContainer, searchBackground);
        searchContainer.setMinimumHeight((int) searchBackground.getMinHeight());
        searchContainer.setMinimumWidth((int) searchBackground.getMinWidth());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(cardDecorator);
        recyclerView.setHasFixedSize(true);
        recyclerView.setVisibility(View.INVISIBLE);

        backgroundDrawable = getBackground();

        if (backgroundDrawable != null) {
            backgroundDrawable = backgroundDrawable.mutate();
        } else {
            backgroundDrawable = new ColorDrawable(DEFAULT_BACKGROUND_COLOR);
        }

        ViewUtils.setBackground(this, backgroundDrawable);
        backgroundDrawable.setAlpha(0);

        navButtonView.setOnClickListener(v -> {
            if (navigationClickListener != null)
                navigationClickListener.onNavigationClick();
        });

        setOnTouchListener((v, event) -> {
            if (!isActivated()) return false;
            setActivated(false);
            return onContainerTouchClickListener == null || onContainerTouchClickListener.onTouch(v, event);
        });

        searchInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus != isActivated()) setActivated(hasFocus);
        });

        searchInput.setOnKeyListener((view, keyCode, keyEvent) -> {
            if (keyCode != KeyEvent.KEYCODE_ENTER) return false;
            setActivated(false);
            return true;
        });
    }

    public void setOnContainerTouchClickListener(OnContainerTouchClickListener listener) {
        this.onContainerTouchClickListener = listener;
    }

    public void setRadius(float radius) {
        searchBackground.setCornerRadius(radius);
        cardDecorator.setCornerRadius(radius);
    }

    public void setContentBackgroundColor(@ColorInt int color) {
        searchBackground.setColor(color);
        cardDecorator.setBackgroundColor(color);
        actionMenu.setBackgroundColor(color);
    }

    public Menu getMenu() {
        return actionMenu.getMenu();
    }

    public void setPopupTheme(@StyleRes int resId) {
        actionMenu.setPopupTheme(resId);
    }

    public void inflateMenu(@MenuRes int menuRes) {
        if (menuRes == 0) return;
        getActivity().getMenuInflater().inflate(menuRes, actionMenu.getMenu());

        XmlResourceParser parser = null;
        try {
            //noinspection ResourceType
            parser = getResources().getLayout(menuRes);
            AttributeSet attrs = Xml.asAttributeSet(parser);
            parseMenu(parser, attrs);
        } catch (XmlPullParserException | IOException e) {
            // should not happens
            throw new InflateException("Error parsing menu XML", e);
        } finally {
            if (parser != null) parser.close();
        }
    }

    public void setOnSearchListener(final OnSearchListener listener) {
        searchInput.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode != KeyEvent.KEYCODE_ENTER) return false;
            listener.onSearchAction(searchInput.getText());
            return true;
        });
    }

    public void setOnMenuItemClickListener(ActionMenuView.OnMenuItemClickListener listener) {
        actionMenu.setOnMenuItemClickListener(listener);
    }

    public CharSequence getText() {
        return searchInput.getText();
    }

    public void setText(CharSequence text) {
        searchInput.setText(text);
    }

    public void setHint(CharSequence hint) {
        searchInput.setHint(hint);
    }

    @Override
    public void setActivated(boolean activated) {
        if (activated == isActivated()) return;
        super.setActivated(activated);

        if (activated) {
            searchInput.requestFocus();
            ViewUtils.showSoftKeyboardDelayed(searchInput, 100);
        } else {
            requestFocus();
            ViewUtils.closeSoftKeyboard(activity);
        }

        if (focusListener != null)
            focusListener.onFocusChanged(activated);

        showMenu(!activated);
        fadeIn(activated);
        updateSuggestionsVisibility();
    }

    public void setOnIconClickListener(OnIconClickListener navigationClickListener) {
        this.navigationClickListener = navigationClickListener;
    }

    public void setOnSearchFocusChangedListener(OnSearchFocusChangedListener focusListener) {
        this.focusListener = focusListener;
    }

    public void addTextChangedListener(TextWatcher textWatcher) {
        searchInput.addTextChangedListener(textWatcher);
    }

    public void removeTextChangedListener(TextWatcher textWatcher) {
        searchInput.removeTextChangedListener(textWatcher);
    }

    public void setAdapter(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter) {
        RecyclerView.Adapter<? extends RecyclerView.ViewHolder> old = getAdapter();
        if (old != null) old.unregisterAdapterDataObserver(adapterObserver);
        adapter.registerAdapterDataObserver(adapterObserver);
        recyclerView.setAdapter(adapter);
    }

    public void setItemAnimator(RecyclerView.ItemAnimator itemAnimator) {
        recyclerView.setItemAnimator(itemAnimator);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decoration) {
        recyclerView.addItemDecoration(decoration);
    }

    public void setLogo(Drawable drawable) {
        searchInput.setLogo(drawable);
    }

    public void setLogo(@DrawableRes int resId) {
        searchInput.setLogo(resId);
    }

    public void setIcon(@DrawableRes int resId) {
        showIcon(resId != 0);
        navButtonView.setImageResource(resId);
    }

    public void setIcon(Drawable drawable) {
        showIcon(drawable != null);
        navButtonView.setImageDrawable(drawable);
    }

    public void showLogo(boolean show) {
        searchInput.showLogo(show);
    }

    public void showIcon(boolean show) {
        navButtonView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public Drawable getIcon() {
        if (navButtonView == null) return null;
        return navButtonView.getDrawable();
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public RecyclerView.Adapter<? extends RecyclerView.ViewHolder> getAdapter() {
        return recyclerView.getAdapter();
    }

    protected LayoutTransition getDefaultLayoutTransition() {
        return new LayoutTransition();
    }

    private void fadeIn(boolean enter) {
        ValueAnimator backgroundAnim;

        if (Build.VERSION.SDK_INT >= 19) {
            backgroundAnim = ObjectAnimator.ofInt(backgroundDrawable, "alpha", enter ? 255 : 0);
        } else {
            backgroundAnim = ValueAnimator.ofInt(enter ? 0 : 255, enter ? 255 : 0);
            backgroundAnim.addUpdateListener(animation -> {
                int value = (Integer) animation.getAnimatedValue();
                backgroundDrawable.setAlpha(value);
            });
        }

        backgroundAnim.setDuration(enter ? DEFAULT_DURATION_ENTER : DEFAULT_DURATION_EXIT);
        backgroundAnim.setInterpolator(enter ? DECELERATE : ACCELERATE);
        backgroundAnim.start();

        Drawable icon = unwrap(getIcon());

        if (icon != null) {
            ObjectAnimator iconAnim = ObjectAnimator.ofFloat(icon, "progress", enter ? 1 : 0);
            iconAnim.setDuration(backgroundAnim.getDuration());
            iconAnim.setInterpolator(backgroundAnim.getInterpolator());
            iconAnim.start();
        }
    }

    public void fadeInSignal(final int colorTo, final ValueAnimator.AnimatorUpdateListener updateListener) {
        final int colorFrom = DEFAULT_BACKGROUND_COLOR;
        final ValueAnimator colorAnimation = getValueAnimator(colorTo, colorFrom);
        colorAnimation.setDuration(DEFAULT_DURATION_ENTER); // milliseconds
        colorAnimation.addUpdateListener(updateListener);
        colorAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                final ValueAnimator colorAnimation = getValueAnimator(colorFrom, colorTo);
                colorAnimation.setDuration(DEFAULT_DURATION_EXIT); // milliseconds
                colorAnimation.addUpdateListener(updateListener);
                colorAnimation.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        colorAnimation.start();
    }

    private ValueAnimator getValueAnimator(int colorTo, int colorFrom) {
        final ValueAnimator colorAnimation;
        if (Build.VERSION.SDK_INT >= 19)
            colorAnimation = ObjectAnimator.ofObject(this, "setBackgroundColor", new ArgbEvaluator(), colorFrom, colorTo);
        else {
            colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        }
        return colorAnimation;
    }

    private int getSuggestionsCount() {
        RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter = getAdapter();
        if (adapter == null) return 0;
        return adapter.getItemCount();
    }

    private void updateSuggestionsVisibility() {
        showSuggestions(isActivated() && getSuggestionsCount() > 0);
    }

    private boolean suggestionsShown() {
        return suggestionsShown;
    }

    private void showSuggestions(final boolean show) {
        if (show == suggestionsShown()) return;

        suggestionsShown = show;

        int childCount = recyclerView.getChildCount();
        int translation = 0;

        final Runnable endAction = () -> {
            if (show) {
                updateDivider();
            } else {
                showDivider(false);
                recyclerView.setVisibility(View.INVISIBLE);
                recyclerView.setTranslationY(-recyclerView.getHeight());
            }
        };

        if (show) {
            updateDivider();
            recyclerView.setVisibility(VISIBLE);
            if (recyclerView.getTranslationY() == 0)
                recyclerView.setTranslationY(-recyclerView.getHeight());
        } else if (childCount > 0) {
            translation = -recyclerView.getChildAt(childCount - 1).getBottom();
        } else {
            showDivider(false);
        }

        ViewPropertyAnimatorCompat listAnim = ViewCompat.animate(recyclerView)
                .translationY(translation)
                .setDuration(show ? DEFAULT_DURATION_ENTER : DEFAULT_DURATION_EXIT)
                .setInterpolator(show ? DECELERATE : ACCELERATE)
                .withLayer()
                .withEndAction(endAction);

        if (show || childCount > 0) {
            listAnim.start();
        } else {
            endAction.run();
        }
    }

    private void showDivider(boolean visible) {
        divider.setVisibility(visible ? View.VISIBLE : View.GONE);
        int shadows = TOP | LEFT | RIGHT;
        if (!visible) shadows |= BOTTOM;
        searchBackground.setShadow(shadows);
    }

    private void updateDivider() {
        showDivider(isActivated() && getSuggestionsCount() > 0);
    }

    @NonNull
    private Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        throw new IllegalStateException();
    }

    private void showMenu(final boolean visible) {
        Menu menu = getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (alwaysShowingMenu.contains(item.getItemId())) continue;
            item.setVisible(visible);
        }
    }

    private void parseMenu(XmlPullParser parser, AttributeSet attrs)
            throws XmlPullParserException, IOException {

        int eventType = parser.getEventType();
        String tagName;
        boolean lookingForEndOfUnknownTag = false;
        String unknownTagName = null;

        // This loop will skip to the menu start tag
        do {
            if (eventType == XmlPullParser.START_TAG) {
                tagName = parser.getName();
                if (tagName.equals("menu")) {
                    // Go to next tag
                    eventType = parser.next();
                    break;
                }

                throw new RuntimeException("Expecting menu, got " + tagName);
            }
            eventType = parser.next();
        } while (eventType != XmlPullParser.END_DOCUMENT);

        boolean reachedEndOfMenu = false;

        while (!reachedEndOfMenu) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if (lookingForEndOfUnknownTag) {
                        break;
                    }

                    tagName = parser.getName();
                    if (tagName.equals("item")) {
                        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MenuItem);
                        int itemShowAsAction = a.getInt(R.styleable.MenuItem_showAsAction, -1);

                        if ((itemShowAsAction & MenuItem.SHOW_AS_ACTION_ALWAYS) != 0) {
                            int itemId = a.getResourceId(R.styleable.MenuItem_android_id, NO_ID);
                            if (itemId != NO_ID) alwaysShowingMenu.add(itemId);
                        }
                        a.recycle();
                    } else {
                        lookingForEndOfUnknownTag = true;
                        unknownTagName = tagName;
                    }
                    break;

                case XmlPullParser.END_TAG:
                    tagName = parser.getName();
                    if (lookingForEndOfUnknownTag && tagName.equals(unknownTagName)) {
                        lookingForEndOfUnknownTag = false;
                        unknownTagName = null;
                    } else if (tagName.equals("menu")) {
                        reachedEndOfMenu = true;
                    }
                    break;

                case XmlPullParser.END_DOCUMENT:
                    throw new RuntimeException("Unexpected end of document");
            }

            eventType = parser.next();
        }
    }

    static private Drawable unwrap(Drawable icon) {
        if (icon instanceof android.support.v7.graphics.drawable.DrawableWrapper)
            return ((android.support.v7.graphics.drawable.DrawableWrapper) icon).getWrappedDrawable();
        if (icon instanceof android.support.v4.graphics.drawable.DrawableWrapper)
            return ((android.support.v4.graphics.drawable.DrawableWrapper) icon).getWrappedDrawable();
        if (Build.VERSION.SDK_INT >= 23 && icon instanceof android.graphics.drawable.DrawableWrapper)
            return ((android.graphics.drawable.DrawableWrapper) icon).getDrawable();
        return DrawableCompat.unwrap(icon);
    }

    public static class RecyclerView extends android.support.v7.widget.RecyclerView {

        public RecyclerView(Context context) {
            super(context);
        }

        public RecyclerView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public RecyclerView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        public boolean onTouchEvent(MotionEvent e) {
            View child = findChildViewUnder(e.getX(), e.getY());
            return child != null && super.onTouchEvent(e);
        }
    }

    public static class LogoEditText extends AppCompatEditText {

        private Drawable logo;
        private boolean logoShown;
        private boolean dirty;

        public LogoEditText(Context context) {
            super(context);
        }

        public LogoEditText(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public LogoEditText(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public void showLogo(boolean shown) {
            logoShown = shown;
        }

        public void setLogo(@DrawableRes int res) {
            if (res == 0) {
                setLogo(null);
            } else {
                setLogo(ResourcesCompat.getDrawable(getResources(), res, getContext().getTheme()));
            }
        }

        public void setLogo(Drawable logo) {
            this.logo = logo;
            dirty = true;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if (logoShown && logo != null) {
                if (dirty) {
                    updateLogoBounds();
                    dirty = false;
                }
                logo.draw(canvas);
            } else {
                super.onDraw(canvas);
            }
        }

        // fit center
        private void updateLogoBounds() {
            int logoHeight = Math.min(getHeight(), logo.getIntrinsicHeight());
            int top = (getHeight() - logoHeight) / 2;
            int logoWidth = (logo.getIntrinsicWidth() * logoHeight) / logo.getIntrinsicHeight();
            logo.setBounds(0, top, logoWidth, top + logoHeight);
        }
    }
}
