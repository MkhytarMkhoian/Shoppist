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

package com.justplay1.shoppist.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mkhytar Mkhoian.
 */
public class CurrencyViewModel extends BaseViewModel {

    public static final String NO_CURRENCY_ID = "no_currency";

    private boolean isChecked;

    public CurrencyViewModel() {
        name = "";
    }

    public CurrencyViewModel(Parcel parcel) {
        this();
        id = parcel.readString();
        name = parcel.readString();
        isChecked = parcel.readByte() != 0;
    }

    @Override
    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof CurrencyViewModel)) return false;
        CurrencyViewModel item = (CurrencyViewModel) o;

        return item.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return 31 * id.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }

    public static final Parcelable.Creator<CurrencyViewModel> CREATOR = new Creator<CurrencyViewModel>() {

        @Override
        public CurrencyViewModel[] newArray(int size) {
            return new CurrencyViewModel[size];
        }

        @Override
        public CurrencyViewModel createFromParcel(Parcel source) {
            return new CurrencyViewModel(source);
        }
    };
}
