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

package com.justplay1.shoppist.presenter.base;

import android.support.annotation.StringRes;
import android.support.v4.util.Pair;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.BaseViewModel;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.models.ItemType;
import com.justplay1.shoppist.models.ListItemViewModel;
import com.justplay1.shoppist.models.ListViewModel;
import com.justplay1.shoppist.models.ProductViewModel;
import com.justplay1.shoppist.models.SortType;
import com.justplay1.shoppist.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.view.ContextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mkhytar Mkhoian.
 */
public abstract class BaseSortablePresenter<V extends ContextView, T extends BaseViewModel> extends BaseRxPresenter<V> {

    protected int mDefaultSort;
    protected final ShoppistPreferences mPreferences;

    public BaseSortablePresenter(ShoppistPreferences preferences) {
        this.mPreferences = preferences;
    }

    public abstract boolean isManualSortEnable();

    protected String getString(@StringRes int resId) {
        return getView().context().getString(resId);
    }

    protected List<Pair<HeaderViewModel, List<T>>> sortByManually(List<Pair<HeaderViewModel, List<T>>> data) {
        for (Pair<HeaderViewModel, List<T>> pair : data) {
            Collections.sort(pair.second, (lhs, rhs) ->
                    lhs.getPosition() < rhs.getPosition() ? -1 : (lhs.getPosition() == rhs.getPosition() ? 0 : 1));
        }
        return data;
    }

    protected List<Pair<HeaderViewModel, List<T>>> sort(final List<T> data, final @SortType int sortType) {
        List<Pair<HeaderViewModel, List<T>>> result = new ArrayList<>();
        if (data == null || data.isEmpty()) return result;

        final Class<?> clazz = data.get(0).getClass();
        if (clazz.isAssignableFrom(ListViewModel.class)
                || clazz.isAssignableFrom(ProductViewModel.class)) {
            sort(result, sortType, data, true);
            if (isManualSortEnable()) {
                sortByManually(result);
            }
            return result;
        }

        final List<T> done = new ArrayList<>();
        final List<T> noDone = new ArrayList<>();
        for (T item : data) {
            if (item.getStatus()) {
                done.add(item);
            } else {
                noDone.add(item);
            }
        }
        sort(result, sortType, noDone, true);
        if (isManualSortEnable()) {
            sortByManually(result);
        }
        sort(result, sortType, done, false);

        if (noDone.size() > 0) {
            HeaderViewModel header = new HeaderViewModel();
            if (clazz.isAssignableFrom(ListItemViewModel.class) && mPreferences.isShowGoodsHeader()) {
                header.setName(getString(R.string.goods).toUpperCase(Locale.getDefault()));
                header.setShowExpandIndicator(false);
                double totalPrice = 0;
                for (T item : noDone) {
                    ListItemViewModel listItem = ((ListItemViewModel) item);
                    totalPrice = totalPrice + (listItem.getPrice() * listItem.getQuantity());
                }
                header.setTotalPrice(ShoppistUtils.roundDouble(totalPrice, 2));
                header.setItemType(ItemType.CART_HEADER);
                result.add(0, Pair.create(header, (List<T>) new ArrayList<T>()));
            }
        }

        if (done.size() > 0) {
            HeaderViewModel header = null;
            if (clazz.isAssignableFrom(ListItemViewModel.class)) {
                header = new HeaderViewModel();
                header.setName(getString(R.string.shopping_cart).toUpperCase(Locale.getDefault()));
                double totalPrice = 0;
                for (T item : done) {
                    ListItemViewModel listItem = ((ListItemViewModel) item);
                    totalPrice = totalPrice + (listItem.getPrice() * listItem.getQuantity());
                }
                header.setTotalPrice(ShoppistUtils.roundDouble(totalPrice, 2));
            }
            if (header != null) {
                header.setItemType(ItemType.CART_HEADER);
                result.add(Pair.create(header, done));
            }
        }
        return result;
    }

    private void sort(List<Pair<HeaderViewModel, List<T>>> result, int sortType, List<T> data, boolean withHeaders) {
        switch (sortType) {
            case SortType.SORT_BY_NAME:
                sortByName(result, data, withHeaders);
                break;
            case SortType.SORT_BY_PRIORITY:
                sortByPriority(result, data, withHeaders);
                break;
            case SortType.SORT_BY_CATEGORIES:
                sortByCategory(result, data, withHeaders);
                break;
            case SortType.SORT_BY_TIME_CREATED:
                sortByTimeCreated(result, data, withHeaders);
                break;
        }
    }

    private void sortItemsByName(List<Pair<HeaderViewModel, List<T>>> result) {
        for (Pair<HeaderViewModel, List<T>> pair : result) {
            sortByName(pair.second);
        }
    }

    private void sortByTimeCreated(List<T> data) {
        Collections.sort(data, (lhs, rhs) -> {
            if (lhs.getTimeCreated() < rhs.getTimeCreated()) {
                return 1;
            } else if (lhs.getTimeCreated() > rhs.getTimeCreated()) {
                return -1;
            } else {
                return 0;
            }
        });
    }

    private void sortByTimeCreated(List<Pair<HeaderViewModel, List<T>>> result, List<T> data, boolean withHeaders) {
        sortByTimeCreated(data);

        if (!withHeaders) return;

        final Calendar currentTime = Calendar.getInstance();
        final Calendar itemTime = Calendar.getInstance();

        final int day = currentTime.get(Calendar.DAY_OF_YEAR);

        long flag = -1;
        List<T> items = null;
        for (int i = 0; i < data.size(); i++) {
            itemTime.setTimeInMillis(data.get(i).getTimeCreated());
            final int itemDay = itemTime.get(Calendar.DAY_OF_YEAR);
            final int diffDay = day - itemDay;

            if (flag != diffDay) {
                flag = diffDay;
                HeaderViewModel header = new HeaderViewModel();
                if (flag < 1) {
                    header.setName(getString(R.string.today));
                } else if (flag == 1) {
                    header.setName(getString(R.string.yesterday));
                } else {
                    header.setName(flag + getString(R.string.days_ago));
                }
                header.setItemType(ItemType.HEADER_ITEM);

                items = new ArrayList<>();
                result.add(Pair.create(header, items));
            }
            if (items != null) {
                items.add(data.get(i));
            }
        }
        sortItemsByName(result);
    }

    private void sortByName(List<T> data) {
        Collections.sort(data, (lhs, rhs) -> lhs.getName().compareToIgnoreCase(rhs.getName()));
    }

    private void sortByName(List<Pair<HeaderViewModel, List<T>>> result, List<T> data, boolean withHeaders) {
        sortByName(data);
        if (!withHeaders) return;

        String headerName = "";
        List<T> items = null;
        for (int i = 0; i < data.size(); i++) {
            String firstCharacter = ShoppistUtils.getFirstCharacter(data.get(i).getName()).toUpperCase();
            if (!headerName.equals(firstCharacter)) {
                headerName = firstCharacter;
                HeaderViewModel header = new HeaderViewModel();
                header.setName(headerName);
                header.setItemType(ItemType.HEADER_ITEM);

                items = new ArrayList<>();
                result.add(Pair.create(header, items));
            }
            if (items != null) {
                items.add(data.get(i));
            }
        }
    }

    private void sortByPriority(List<T> data) {
        Collections.sort(data, (lhs, rhs) -> {
            if (lhs.getPriority() < rhs.getPriority()) {
                return 1;
            } else if (lhs.getPriority() > rhs.getPriority()) {
                return -1;
            } else {
                return 0;
            }
        });
    }

    private void sortByPriority(List<Pair<HeaderViewModel, List<T>>> result, List<T> data, boolean withHeaders) {
        sortByPriority(data);

        if (!withHeaders) return;

        final String noPriority = getString(R.string.no_priority);
        final String lowPriority = getString(R.string.low);
        final String mediumPriority = getString(R.string.medium_priority);
        final String highPriority = getString(R.string.high);
        List<T> items = null;
        int priority = -1;

        for (int i = 0; i < data.size(); i++) {
            final int p = data.get(i).getPriority();
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

                items = new ArrayList<>();
                result.add(Pair.create(header, items));
            }
            if (items != null) {
                items.add(data.get(i));
            }
        }
        sortItemsByName(result);
    }

    private void sortByCategory(List<T> data) {
        Collections.sort(data, (lhs, rhs) -> lhs.getCategory().getName().compareToIgnoreCase(rhs.getCategory().getName()));
    }

    private void sortByCategory(List<Pair<HeaderViewModel, List<T>>> result, List<T> data, boolean withHeaders) {
        if (mPreferences.isManualSortEnableForCategories()) {
            Collections.sort(data, (lhs, rhs) -> lhs.getCategory().getPosition() < rhs.getCategory().getPosition() ? -1
                    : (lhs.getCategory().getPosition() == rhs.getCategory().getPosition() ? 0 : 1));
        } else {
            sortByCategory(data);
        }

        if (!withHeaders) return;

        String headerName = "";
        List<T> items = null;
        for (int i = 0; i < data.size(); i++) {
            String name = data.get(i).getCategory().getName();
            if (!headerName.equals(name)) {
                headerName = name;
                HeaderViewModel header = new HeaderViewModel();
                header.setName(headerName);
                header.setItemType(ItemType.HEADER_ITEM);

                items = new ArrayList<>();
                result.add(Pair.create(header, items));
            }
            if (items != null) {
                items.add(data.get(i));
            }
        }
        sortItemsByName(result);
    }
}
