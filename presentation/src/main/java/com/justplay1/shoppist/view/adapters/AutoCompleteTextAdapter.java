package com.justplay1.shoppist.view.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.ProductViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mkhitar on 30.01.2015.
 */
public class AutoCompleteTextAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private Map<String, ProductViewModel> mProducts;
    private List<SpannableStringBuilder> mProductsToDisplay;

    private String mCurrentText = "";
    private String mNotEqualText = "";

    public AutoCompleteTextAdapter(Context context) {
        mContext = context;
        mProducts = new HashMap<>();
        mProductsToDisplay = new ArrayList<>();
    }

    public ProductViewModel getProduct(String name) {
        return mProducts.get(name.toLowerCase());
    }

    public String getCurrentText() {
        return mCurrentText;
    }

    public void setNotEqualText(String notEqual) {
        this.mNotEqualText = notEqual;
    }

    @Override
    public int getCount() {
        return mProductsToDisplay.size();
    }

    @Override
    public String getItem(int position) {
        return mProductsToDisplay.get(position).toString();
    }

    @Override
    public long getItemId(int position) {
        return mProductsToDisplay.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.view_avto_complete_text, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.title);
            holder.addBtn = (ImageView) convertView.findViewById(R.id.add_btn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mProductsToDisplay.get(position).toString().equals(mCurrentText)) {
            holder.addBtn.setVisibility(View.VISIBLE);
        } else {
            holder.addBtn.setVisibility(View.GONE);
        }
        holder.name.setText(mProductsToDisplay.get(position), TextView.BufferType.SPANNABLE);
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    results.values = mProducts;
                    results.count = mProducts.size();
                } else {
                    String prefixString = constraint.toString().toLowerCase();
                    final List<SpannableStringBuilder> newValues = new ArrayList<>();

                    if (prefixString.equals(mNotEqualText.toLowerCase())) {
                        return results;
                    }

                    if (!mProducts.containsKey(prefixString)) {
                        mCurrentText = constraint.toString();
                        newValues.add(getSpannableStringBuilder(constraint.toString(), 0, constraint.length()));
                    }

                    for (final ProductViewModel value : mProducts.values()) {
                        final String valueText = value.getName().toLowerCase();

                        if (valueText.startsWith(prefixString)) {
                            newValues.add(getSpannableStringBuilder(value.getName(), 0, prefixString.length()));
                        } else {
                            final String[] words = valueText.split(" ");

                            // Start at index 0, in case valueText starts with space(s)
                            for (String word : words) {
                                if (word.startsWith(prefixString)) {
                                    int start = valueText.length() - word.length();
                                    newValues.add(getSpannableStringBuilder(value.getName(), start, start + prefixString.length()));
                                    break;
                                }
                            }
                        }
                    }
                    results.values = newValues;
                    results.count = newValues.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (constraint == null) return;
                if (results.values == null) return;

                mProductsToDisplay = (List<SpannableStringBuilder>) results.values;
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }

    private SpannableStringBuilder getSpannableStringBuilder(String value, int start, int end) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString redSpannable = new SpannableString(value);
        redSpannable.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        redSpannable.setSpan(new ForegroundColorSpan(Color.BLACK),
                start, end, 0);
        builder.append(redSpannable);
        return builder;
    }

    public void setData(Map<String, ProductViewModel> data){
        mProducts = data;
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        public TextView name;
        public ImageView addBtn;
    }
}
