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
public class UnitViewModel extends BaseViewModel {

    public static final String NO_UNIT_ID = "no_unit";

    private String shortName;
    private boolean isChecked;

    public UnitViewModel() {
        name = "";
        shortName = "";
    }

    public UnitViewModel(Parcel parcel) {
        id = parcel.readString();
        name = parcel.readString();
        shortName = parcel.readString();
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof UnitViewModel)) return false;
        UnitViewModel item = (UnitViewModel) o;

        return item.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(shortName);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }

    public static final Parcelable.Creator<UnitViewModel> CREATOR = new Creator<UnitViewModel>() {

        @Override
        public UnitViewModel[] newArray(int size) {
            return new UnitViewModel[size];
        }

        @Override
        public UnitViewModel createFromParcel(Parcel source) {
            return new UnitViewModel(source);
        }
    };
}
