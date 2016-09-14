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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Mkhytar Mkhoian.
 */
public class DateTerm implements Parcelable {

    private int mDayOfMonth = -1;
    private int mMinute = -1;
    private int mYear = -1;
    private int mMonth = -1;
    private int mHourOfDay = -1;

    public DateTerm() {
    }

    public DateTerm(String dateToParse) {
        if (dateToParse == null) return;
        String[] strings = dateToParse.split(" ");
        String[] date = strings[0].split("/");
        String[] time = strings[1].split(":");

        mDayOfMonth = Integer.valueOf(date[0]);
        mMonth = Integer.valueOf(date[1]);
        mYear = Integer.valueOf(date[2]);

        mHourOfDay = Integer.valueOf(time[0]);
        mMinute = Integer.valueOf(time[1]);
    }

    public DateTerm(Parcel parcel) {
        this();
        mDayOfMonth = parcel.readInt();
        mMinute = parcel.readInt();
        mYear = parcel.readInt();
        mMonth = parcel.readInt();
        mHourOfDay = parcel.readInt();
    }

    public Date getDate() {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, mYear);
        date.set(Calendar.MONTH, mMonth);
        date.set(Calendar.DAY_OF_MONTH, mDayOfMonth);
        date.set(Calendar.HOUR_OF_DAY, mHourOfDay);
        date.set(Calendar.MINUTE, mMinute);
        return date.getTime();
    }

    public boolean isDateEmpty(){
        return mDayOfMonth == -1 && mMonth == -1 && mYear == -1;
    }

    public boolean isTimeEmpty(){
        return mHourOfDay == -1 && mMinute == -1;
    }

    public String getDateToString() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());
        return format.format(getDate());
    }

    public String getTimeToString() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return format.format(getDate());
    }

    public int getDayOfMonth() {
        return mDayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.mDayOfMonth = dayOfMonth;
    }

    public int getMinute() {
        return mMinute;
    }

    public void setMinute(int minute) {
        this.mMinute = minute;
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        this.mYear = year;
    }

    public int getMonth() {
        return mMonth;
    }

    public void setMonth(int month) {
        this.mMonth = month;
    }

    public int getHourOfDay() {
        return mHourOfDay;
    }

    public void setHourOfDay(int hourOfMonth) {
        this.mHourOfDay = hourOfMonth;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(mDayOfMonth)
                .append("/")
                .append(mMonth)
                .append("/")
                .append(mYear)
                .append(" ")
                .append(mHourOfDay)
                .append(":")
                .append(mMinute)
                .toString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mDayOfMonth);
        dest.writeInt(mMinute);
        dest.writeInt(mYear);
        dest.writeInt(mMonth);
        dest.writeInt(mHourOfDay);
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || !(o instanceof DateTerm)) return false;

        DateTerm item = (DateTerm) o;

        return item.getDayOfMonth() == this.getDayOfMonth()
                && (item.getHourOfDay() == this.getHourOfDay())
                && (item.getMinute() == this.getMinute())
                && (item.getMonth() == this.getMonth())
                && (item.getYear() == this.getYear());
    }

    @Override
    public int hashCode() {
        int result = 31 * mDayOfMonth;
        result += 31 * mHourOfDay;
        result += 31 * mMinute;
        result += 31 * mMonth;
        result += 31 * mYear;
        return result;
    }

    public static final Parcelable.Creator<DateTerm> CREATOR = new Creator<DateTerm>() {

        @Override
        public DateTerm[] newArray(int size) {

            return new DateTerm[size];
        }

        @Override
        public DateTerm createFromParcel(Parcel source) {

            return new DateTerm(source);
        }
    };
}
