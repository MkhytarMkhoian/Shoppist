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

package com.justplay1.shoppist.utils;

import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.justplay1.shoppist.shared.widget.recyclerview.holders.BaseHeaderHolder;

/**
 * Created by Mkhytar Mkhoian.
 */
public class ExpandUtils {

    public static void toggleIndicator(BaseHeaderHolder holder){
        final int expandState = holder.getExpandStateFlags();
        if (((expandState & RecyclerViewExpandableItemManager.STATE_FLAG_IS_UPDATED) != 0)) {
            if ((expandState & RecyclerViewExpandableItemManager.STATE_FLAG_IS_EXPANDED) != 0) {
                holder.indicator.setExpand(true);
            } else {
                holder.indicator.setExpand(false);
            }
        }
    }
}
