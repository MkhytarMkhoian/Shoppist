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

package com.justplay1.shoppist.view.component.spinner;

import android.content.Context;
import android.util.AttributeSet;

import com.justplay1.shoppist.models.CategoryViewModel;

/**
 * Created by Mkhytar Mkhoian.
 */
public class CategorySpinnerView extends SpinnerView<CategoryViewModel> {
    public CategorySpinnerView(Context context) {
        super(context);
    }

    public CategorySpinnerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CategorySpinnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
