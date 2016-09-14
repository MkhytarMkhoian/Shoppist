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

package com.justplay1.shoppist.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.BaseViewModel;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.models.ItemType;
import com.justplay1.shoppist.models.ListItemViewModel;
import com.justplay1.shoppist.models.Priority;
import com.justplay1.shoppist.models.SortType;
import com.justplay1.shoppist.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.ShoppistUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mkhytar Mkhoian.
 */
public abstract class BaseFactory<T extends BaseViewModel> implements RemoteViewsService.RemoteViewsFactory {

    protected ShoppistPreferences mPreferences;

    protected Context mContext;
    protected int mWidgetId;
    protected String mParentListName;
    protected String mParentListId;
    protected List<T> mData;
    protected int mSortType;

    protected BitmapDrawable mChecked;
    protected BitmapDrawable mDefault;

    protected
    @ColorInt
    int mNoPriority;
    protected
    @ColorInt
    int mHighPriority;
    protected
    @ColorInt
    int mLowPriority;
    protected
    @ColorInt
    int mMediumPriority;

    public BaseFactory(Context context, Intent intent) {
        mContext = context;
        mWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        mParentListName = intent.getStringExtra(SelectListDialogActivity.WIDGET_LIST_NAME + mWidgetId);
        mParentListId = intent.getStringExtra(SelectListDialogActivity.WIDGET_LIST_ID + mWidgetId);
        mSortType = intent.getIntExtra(SortWidgetActivity.WIDGET_SORT + mWidgetId, 4);

        Resources res = mContext.getResources();
        mDefault = (BitmapDrawable) res.getDrawable(R.drawable.abc_btn_check_to_on_mtrl_000);
        mChecked = (BitmapDrawable) res.getDrawable(R.drawable.abc_btn_check_to_on_mtrl_015);

        mData = new ArrayList<>();

        mNoPriority = context.getResources().getColor(R.color.action_mode_toolbar_color);
        mHighPriority = context.getResources().getColor(R.color.red_color);
        mLowPriority = context.getResources().getColor(R.color.green_500);
        mMediumPriority = context.getResources().getColor(R.color.orange_500);
    }

    private void setPriorityTextColor(@Priority int priority, RemoteViews view) {
        switch (priority) {
            case Priority.HIGH:
                view.setTextColor(R.id.header_name, mHighPriority);
                break;
            case Priority.LOW:
                view.setTextColor(R.id.header_name, mLowPriority);
                break;
            case Priority.MEDIUM:
                view.setTextColor(R.id.header_name, mMediumPriority);
                break;
            case Priority.NO_PRIORITY:
                view.setTextColor(R.id.header_name, mNoPriority);
                break;
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {
        switch (mData.get(position).getItemType()) {
            case ItemType.CART_HEADER:
                return getRemoteViewsForCartHeader(position, (HeaderViewModel) mData.get(position));
            case ItemType.HEADER_ITEM:
                return getRemoteViewsForHeader(position, (HeaderViewModel) mData.get(position));
            default:
                return getRemoteViewsForItem(position);
        }
    }

    @NonNull
    protected RemoteViews getRemoteViewsForHeader(int position, HeaderViewModel header) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_header);

        if (mSortType == SortType.SORT_BY_PRIORITY) {
            setPriorityTextColor(header.getPriority(), remoteViews);
        } else {
            remoteViews.setTextColor(R.id.header_name, mPreferences.getColorPrimary());
        }
        remoteViews.setTextViewText(R.id.header_name, header.getName());
        remoteViews.setInt(R.id.container, "setBackgroundColor", Color.WHITE);
        return remoteViews;
    }

    @NonNull
    protected RemoteViews getRemoteViewsForCartHeader(int position, HeaderViewModel header) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_header);

        remoteViews.setViewVisibility(R.id.line, View.INVISIBLE);
        remoteViews.setInt(R.id.container, "setBackgroundColor", mPreferences.getColorPrimary());
        remoteViews.setTextColor(R.id.header_name, Color.WHITE);
        remoteViews.setTextViewText(R.id.header_name, header.getName().toUpperCase(Locale.getDefault()));

        return remoteViews;
    }

    protected abstract RemoteViews getRemoteViewsForItem(int position);

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {
        mData = null;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    protected void setItemChecked(RemoteViews remoteViews, boolean isChecked) {
        if (isChecked) {
            remoteViews.setImageViewBitmap(R.id.check_btn, mChecked.getBitmap());
            remoteViews.setInt(R.id.check_btn, "setColorFilter", mPreferences.getColorPrimary());
        } else {
            remoteViews.setImageViewBitmap(R.id.check_btn, mDefault.getBitmap());
            remoteViews.setInt(R.id.check_btn, "setColorFilter", mPreferences.getColorPrimary());
        }
    }

    protected void setNote(RemoteViews remoteViews, String note, boolean isEmpty) {
        if (isEmpty) {
            remoteViews.setViewVisibility(R.id.note_text, View.GONE);
        } else {
            remoteViews.setViewVisibility(R.id.note_text, View.VISIBLE);
            remoteViews.setTextViewText(R.id.note_text, note);
        }
    }


    protected void sortByTime(List<T> data, boolean withHeaders) {
        Collections.sort(data, (lhs, rhs) -> {
            if (lhs.getTimeCreated() < rhs.getTimeCreated()) {
                return 1;
            } else if (lhs.getTimeCreated() > rhs.getTimeCreated()) {
                return -1;
            } else {
                return 0;
            }
        });

        if (!withHeaders) return;

        final Calendar currentTime = Calendar.getInstance();
        final Calendar itemTime = Calendar.getInstance();

        final int day = currentTime.get(Calendar.DAY_OF_YEAR);

        long flag = -1;
        for (int i = 0; i < data.size(); i++) {
            itemTime.setTimeInMillis(data.get(i).getTimeCreated());
            final int itemDay = itemTime.get(Calendar.DAY_OF_YEAR);
            final int diffDay = day - itemDay;

            if (flag != diffDay) {
                flag = diffDay;
                HeaderViewModel header = new HeaderViewModel();
                if (flag < 1) {
                    header.setName(mContext.getString(R.string.today));
                } else if (flag == 1) {
                    header.setName(mContext.getString(R.string.yesterday));
                } else {
                    header.setName(flag + mContext.getString(R.string.days_ago));
                }
                header.setItemType(ItemType.HEADER_ITEM);

                data.add(i, (T) header);
            }
        }
    }

    protected void sortByName(List<T> data, boolean withHeaders) {
        Collections.sort(data, (lhs, rhs) -> lhs.getName().compareToIgnoreCase(rhs.getName()));
        if (!withHeaders) return;

        String headerName = "";
        for (int i = 0; i < data.size(); i++) {
            String firstCharacter = ShoppistUtils.getFirstCharacter(data.get(i).getName()).toUpperCase();
            if (!headerName.equals(firstCharacter)) {
                headerName = firstCharacter;
                HeaderViewModel header = new HeaderViewModel();
                header.setName(headerName);
                header.setItemType(ItemType.HEADER_ITEM);
                data.add(i, (T) header);
            }
        }
    }

    protected void sortByPriority(List<T> data, boolean withHeaders) {
        Collections.sort(data, (lhs, rhs) -> {
            if (lhs.getPriority() < rhs.getPriority()) {
                return 1;
            } else if (lhs.getPriority() > rhs.getPriority()) {
                return -1;
            } else {
                return 0;
            }
        });

        if (!withHeaders) return;

        final String noPriority = mContext.getResources().getString(R.string.no_priority);
        final String lowPriority = mContext.getResources().getString(R.string.low);
        final String mediumPriority = mContext.getResources().getString(R.string.medium_priority);
        final String highPriority = mContext.getResources().getString(R.string.high);
        @Priority int priority = -1;

        for (int i = 0; i < data.size(); i++) {
            final @Priority int p = data.get(i).getPriority();
            if (priority != p) {
                priority = p;
                HeaderViewModel header = new HeaderViewModel();
                switch (priority) {
                    case 0:
                        header.setName(noPriority);
                        break;
                    case 1:
                        header.setName(lowPriority);
                        break;
                    case 2:
                        header.setName(mediumPriority);
                        break;
                    case 3:
                        header.setName(highPriority);
                        break;
                }
                header.setPriority(p);
                header.setItemType(ItemType.HEADER_ITEM);
                data.add(i, (T) header);
            }
        }
    }

    protected void sortByCategory(List<T> data, boolean withHeaders) {
        if (mPreferences.isManualSortEnableForCategories()) {
            Collections.sort(data, (lhs, rhs) -> lhs.getCategory().getPosition() < rhs.getCategory().getPosition() ? -1
                    : (lhs.getCategory().getPosition() == rhs.getCategory().getPosition() ? 0 : 1));
        } else {
            Collections.sort(data, (lhs, rhs) -> lhs.getCategory().getName().compareToIgnoreCase(rhs.getCategory().getName()));
        }

        if (!withHeaders) return;

        String headerName = "";
        for (int i = 0; i < data.size(); i++) {
            String name = data.get(i).getCategory().getName();
            if (!headerName.equals(name)) {
                headerName = name;
                HeaderViewModel header = new HeaderViewModel();
                header.setName(headerName);
                header.setItemType(ItemType.HEADER_ITEM);

                data.add(i, (T) header);
            }
        }
    }

    private void sort(List<T> data, boolean withHeaders) {
        switch (mSortType) {
            case SortType.SORT_BY_NAME:
                sortByName(data, withHeaders);
                break;
            case SortType.SORT_BY_PRIORITY:
                sortByPriority(data, withHeaders);
                break;
            case SortType.SORT_BY_CATEGORIES:
                sortByCategory(data, withHeaders);
                break;
            case SortType.SORT_BY_TIME_CREATED:
                sortByTime(data, withHeaders);
                break;
        }
    }

    protected void sort() {
        if (mData.size() == 0) return;

        final Class<?> clazz = mData.get(0).getClass();
        final List<T> done = new ArrayList<>();
        final List<T> noDone = new ArrayList<>();
        for (T item : mData) {
            if (item.getStatus()) {
                done.add(item);
            } else {
                noDone.add(item);
            }
        }
        sort(done, false);
        sort(noDone, true);

        if (done.size() > 0) {
            HeaderViewModel header = null;
            if (clazz.isAssignableFrom(ListItemViewModel.class)) {
                header = new HeaderViewModel();
                header.setName(mContext.getString(R.string.shopping_cart).toUpperCase(Locale.getDefault()));
                double totalPrice = 0;
                for (T item : done) {
                    ListItemViewModel listItem = ((ListItemViewModel) item);
                    totalPrice = totalPrice + (listItem.getPrice() * listItem.getQuantity());
                }
                header.setTotalPrice(ShoppistUtils.roundDouble(totalPrice, 2));
            }
            if (header != null) {
                header.setItemType(ItemType.CART_HEADER);
                done.add(0, (T) header);
            }
        }

        mData.clear();
        mData.addAll(noDone);
        mData.addAll(done);
    }
}
