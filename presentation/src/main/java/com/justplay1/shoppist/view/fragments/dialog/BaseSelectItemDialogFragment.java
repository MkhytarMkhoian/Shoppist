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

package com.justplay1.shoppist.view.fragments.dialog;

import android.view.View;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.BaseViewModel;
import com.justplay1.shoppist.view.component.spinner.SpinnerView;

/**
 * Created by Mkhytar Mkhoian.
 */
public abstract class BaseSelectItemDialogFragment<T extends BaseViewModel>
        extends BaseDialogFragment {

    protected SpinnerView<T> mSelectView;
    protected OnCompleteListener<T> mCompleteListener;

    @Override
    public void init(View view) {
        super.init(view);
        mSelectView = (SpinnerView<T>) view.findViewById(R.id.custom_spinner);
        mSelectView.setOnAddBtnClickListener(this);
        mSelectView.setOnEditBtnClickListener(this);
    }

    public void setCompleteListener(OnCompleteListener<T> listener) {
        mCompleteListener = listener;
    }

    public interface OnCompleteListener<T> {

        void onComplete(T item, boolean isUpdate);
    }
}
