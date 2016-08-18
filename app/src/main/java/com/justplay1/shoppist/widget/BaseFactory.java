package com.justplay1.shoppist.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.adapters.BaseAdapter;
import com.justplay1.shoppist.models.BaseModel;
import com.justplay1.shoppist.models.Header;
import com.justplay1.shoppist.models.ItemType;
import com.justplay1.shoppist.models.Priority;
import com.justplay1.shoppist.models.ShoppingListItem;
import com.justplay1.shoppist.models.Status;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.ShoppistUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mkhytar on 19.10.2015.
 */
public abstract class BaseFactory<T extends BaseModel> implements RemoteViewsService.RemoteViewsFactory {

    protected Context mContext;
    protected int mWidgetId;
    protected String mParentListName;
    protected String mParentListId;
    protected List<T> mData;
    protected int mSortType;

    protected BitmapDrawable mChecked;
    protected BitmapDrawable mDefault;

    public BaseFactory(Context ctx, Intent intent) {
        mContext = ctx;
        mWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        mParentListName = intent.getStringExtra(SelectListDialogActivity.WIDGET_LIST_NAME + mWidgetId);
        mParentListId = intent.getStringExtra(SelectListDialogActivity.WIDGET_LIST_ID + mWidgetId);
        mSortType = intent.getIntExtra(SortWidgetActivity.WIDGET_SORT + mWidgetId, 4);

        Resources res = mContext.getResources();
        mDefault = (BitmapDrawable) res.getDrawable(R.drawable.abc_btn_check_to_on_mtrl_000);
        mChecked = (BitmapDrawable) res.getDrawable(R.drawable.abc_btn_check_to_on_mtrl_015);

        mData = new ArrayList<>();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        switch (mData.get(position).getItemType()) {
            case ItemType.CART_HEADER:
                return getRemoteViewsForCartHeader(position, (Header) mData.get(position));
            case ItemType.HEADER_ITEM:
                return getRemoteViewsForHeader(position, (Header) mData.get(position));
            default:
                return getRemoteViewsForItem(position);
        }
    }

    @NonNull
    protected RemoteViews getRemoteViewsForHeader(int position, Header header) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_header);

        if (mSortType == BaseAdapter.SORT_BY_PRIORITY) {
            if (header.getPriority() == Priority.NO_PRIORITY) {
                remoteViews.setTextColor(R.id.header_name, mContext.getResources().getColor(R.color.action_mode_toolbar_color));
            } else {
                remoteViews.setTextColor(R.id.header_name, mContext.getResources().getColor(header.getPriority().mColorRes));
            }
        } else {
            remoteViews.setTextColor(R.id.header_name, ShoppistPreferences.getColorPrimary());
        }
        remoteViews.setTextViewText(R.id.header_name, header.getName());
        remoteViews.setInt(R.id.container, "setBackgroundColor", Color.WHITE);
        return remoteViews;
    }

    @NonNull
    protected RemoteViews getRemoteViewsForCartHeader(int position, Header header) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_header);

        remoteViews.setViewVisibility(R.id.line, View.INVISIBLE);
        remoteViews.setInt(R.id.container, "setBackgroundColor", ShoppistPreferences.getColorPrimary());
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
            remoteViews.setInt(R.id.check_btn, "setColorFilter", ShoppistPreferences.getColorPrimary());
        } else {
            remoteViews.setImageViewBitmap(R.id.check_btn, mDefault.getBitmap());
            remoteViews.setInt(R.id.check_btn, "setColorFilter", ShoppistPreferences.getColorPrimary());
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
        Collections.sort(data, new Comparator<T>() {
            @Override
            public int compare(T lhs, T rhs) {
                if (lhs.getTimeCreated() < rhs.getTimeCreated()) {
                    return 1;
                } else if (lhs.getTimeCreated() > rhs.getTimeCreated()) {
                    return -1;
                } else {
                    return 0;
                }
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
                Header header = new Header();
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
        Collections.sort(data, new Comparator<T>() {
            @Override
            public int compare(T lhs, T rhs) {
                return lhs.getName().compareToIgnoreCase(rhs.getName());
            }
        });
        if (!withHeaders) return;

        String headerName = "";
        for (int i = 0; i < data.size(); i++) {
            String firstCharacter = ShoppistUtils.getFirstCharacter(data.get(i).getName()).toUpperCase();
            if (!headerName.equals(firstCharacter)) {
                headerName = firstCharacter;
                Header header = new Header();
                header.setName(headerName);
                header.setItemType(ItemType.HEADER_ITEM);
                data.add(i, (T) header);
            }
        }
    }

    protected void sortByPriority(List<T> data, boolean withHeaders) {
        Collections.sort(data, new Comparator<T>() {
            @Override
            public int compare(T lhs, T rhs) {
                if (lhs.getPriority().ordinal() < rhs.getPriority().ordinal()) {
                    return 1;
                } else if (lhs.getPriority().ordinal() > rhs.getPriority().ordinal()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        if (!withHeaders) return;

        final String noPriority = mContext.getResources().getString(R.string.no_priority);
        final String lowPriority = mContext.getResources().getString(R.string.low);
        final String mediumPriority = mContext.getResources().getString(R.string.medium_priority);
        final String highPriority = mContext.getResources().getString(R.string.high);
        int priority = -1;

        for (int i = 0; i < data.size(); i++) {
            final Priority p = data.get(i).getPriority();
            if (priority != p.ordinal()) {
                priority = p.ordinal();
                Header header = new Header();
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
        if (ShoppistPreferences.isManualSortEnableForCategories()) {
            Collections.sort(data, new Comparator<T>() {
                @Override
                public int compare(T lhs, T rhs) {
                    return lhs.getCategory().getPosition() < rhs.getCategory().getPosition() ? -1
                            : (lhs.getCategory().getPosition() == rhs.getCategory().getPosition() ? 0 : 1);
                }
            });
        } else {
            Collections.sort(data, new Comparator<T>() {
                @Override
                public int compare(T lhs, T rhs) {
                    return lhs.getCategory().getName().compareToIgnoreCase(rhs.getCategory().getName());
                }
            });
        }

        if (!withHeaders) return;

        String headerName = "";
        for (int i = 0; i < data.size(); i++) {
            String name = data.get(i).getCategory().getName();
            if (!headerName.equals(name)) {
                headerName = name;
                Header header = new Header();
                header.setName(headerName);
                header.setItemType(ItemType.HEADER_ITEM);

                data.add(i, (T) header);
            }
        }
    }

    private void sort(List<T> data, boolean withHeaders) {
        switch (mSortType) {
            case BaseAdapter.SORT_BY_NAME:
                sortByName(data, withHeaders);
                break;
            case BaseAdapter.SORT_BY_PRIORITY:
                sortByPriority(data, withHeaders);
                break;
            case BaseAdapter.SORT_BY_CATEGORIES:
                sortByCategory(data, withHeaders);
                break;
            case BaseAdapter.SORT_BY_TIME_CREATED:
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
            if (item.getStatus() == Status.DONE) {
                done.add(item);
            } else {
                noDone.add(item);
            }
        }
        sort(done, false);
        sort(noDone, true);

        if (done.size() > 0) {
            Header header = null;
            if (clazz.isAssignableFrom(ShoppingListItem.class)) {
                header = new Header();
                header.setName(mContext.getString(R.string.shopping_cart).toUpperCase(Locale.getDefault()));
                double totalPrice = 0;
                for (T item : done) {
                    ShoppingListItem listItem = ((ShoppingListItem) item);
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
