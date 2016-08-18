package com.justplay1.shoppist.models.mappers;

import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.models.ListItemModel;
import com.justplay1.shoppist.models.ListItemViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 29.04.2016.
 */
@PerActivity
public class ListItemsModelDataMapper {

    private final CategoryModelDataMapper mCategoryDataMapper;
    private final CurrencyModelDataMapper mCurrencyDataMapper;
    private final UnitsDataModelMapper mUnitsDataMapper;

    @Inject
    public ListItemsModelDataMapper(CategoryModelDataMapper categoryDataMapper, CurrencyModelDataMapper currencyDataMapper, UnitsDataModelMapper unitsDataMapper) {
        this.mCategoryDataMapper = categoryDataMapper;
        this.mCurrencyDataMapper = currencyDataMapper;
        this.mUnitsDataMapper = unitsDataMapper;
    }

    @SuppressWarnings("ResourceType")
    public ListItemViewModel transformToViewModel(ListItemModel item) {
        ListItemViewModel itemModel = null;
        if (item != null) {
            itemModel = new ListItemViewModel();
            itemModel.setId(item.getId());
            itemModel.setName(item.getName());
            itemModel.setServerId(item.getServerId());
            itemModel.setDirty(item.isDirty());
            itemModel.setDelete(item.isDelete());
            itemModel.setTimestamp(item.getTimestamp());
            itemModel.setNote(item.getNote());
            itemModel.setParentListId(item.getParentListId());
            itemModel.setPrice(item.getPrice());
            itemModel.setPriority(item.getPriority());
            itemModel.setStatus(item.getStatus());
            itemModel.setCategory(mCategoryDataMapper.transformToViewModel(item.getCategory()));
            itemModel.setCurrency(mCurrencyDataMapper.transformToViewModel(item.getCurrency()));
            itemModel.setUnit(mUnitsDataMapper.transformToViewModel(item.getUnit()));
            itemModel.setQuantity(item.getQuantity());
            itemModel.setTimeCreated(item.getTimeCreated());
            itemModel.setPosition(item.getPosition());
        }
        return itemModel;
    }

    public List<ListItemViewModel> transformToViewModel(Collection<ListItemModel> listItems) {
        List<ListItemViewModel> itemModels = new ArrayList<>();
        ListItemViewModel itemModel;
        for (ListItemModel item : listItems) {
            itemModel = transformToViewModel(item);
            if (itemModel != null) {
                itemModels.add(itemModel);
            }
        }
        return itemModels;
    }

    public ListItemModel transform(ListItemViewModel itemModel) {
        ListItemModel item = null;
        if (itemModel != null) {
            item = new ListItemModel();
            item.setId(itemModel.getId());
            item.setName(itemModel.getName());
            item.setServerId(itemModel.getServerId());
            item.setDirty(itemModel.isDirty());
            item.setDelete(itemModel.isDelete());
            item.setTimestamp(itemModel.getTimestamp());
            item.setNote(itemModel.getNote());
            item.setParentListId(itemModel.getParentListId());
            item.setPrice(itemModel.getPrice());
            item.setPriority(itemModel.getPriority());
            item.setStatus(itemModel.getStatus());
            item.setCategory(mCategoryDataMapper.transform(itemModel.getCategory()));
            item.setCurrency(mCurrencyDataMapper.transform(itemModel.getCurrency()));
            item.setUnit(mUnitsDataMapper.transform(itemModel.getUnit()));
            item.setQuantity(itemModel.getQuantity());
            item.setTimeCreated(itemModel.getTimeCreated());
            item.setPosition(itemModel.getPosition());
        }
        return item;
    }

    public List<ListItemModel> transform(Collection<ListItemViewModel> itemModels) {
        List<ListItemModel> items = new ArrayList<>();
        ListItemModel item;
        for (ListItemViewModel itemModel : itemModels) {
            item = transform(itemModel);
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }
}
