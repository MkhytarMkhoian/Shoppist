package com.justplay1.shoppist.models.mappers;

import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.models.ListModel;
import com.justplay1.shoppist.models.ListViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 29.04.2016.
 */
@PerActivity
public class ListModelDataMapper {

    @Inject
    public ListModelDataMapper() {
    }

    @SuppressWarnings("ResourceType")
    public ListViewModel transformToViewModel(ListModel list) {
        ListViewModel listModel = null;
        if (list != null) {
            listModel = new ListViewModel();
            listModel.setId(list.getId());
            listModel.setName(list.getName());
            listModel.setServerId(list.getServerId());
            listModel.setDelete(list.isDelete());
            listModel.setTimestamp(list.getTimestamp());
            listModel.setDirty(list.isDirty());
            listModel.setTimeCreated(list.getTimeCreated());
            listModel.setPriority(list.getPriority());
            listModel.setBoughtCount(list.getBoughtCount());
            listModel.setColor(list.getColor());
            listModel.setSize(list.getSize());
            listModel.setPosition(list.getPosition());
        }
        return listModel;
    }

    public List<ListViewModel> transformToViewModel(Collection<ListModel> lists) {
        List<ListViewModel> listModels = new ArrayList<>();
        ListViewModel listModel;
        for (ListModel list : lists) {
            listModel = transformToViewModel(list);
            if (listModel != null) {
                listModels.add(listModel);
            }
        }
        return listModels;
    }

    public ListModel transform(ListViewModel listModel) {
        ListModel list = null;
        if (listModel != null) {
            list = new ListModel();
            list.setId(listModel.getId());
            list.setName(listModel.getName());
            list.setServerId(listModel.getServerId());
            list.setDelete(listModel.isDelete());
            list.setTimestamp(listModel.getTimestamp());
            list.setDirty(listModel.isDirty());
            list.setTimeCreated(listModel.getTimeCreated());
            list.setPriority(listModel.getPriority());
            list.setBoughtCount(listModel.getBoughtCount());
            list.setColor(listModel.getColor());
            list.setSize(listModel.getSize());
            list.setPosition(listModel.getPosition());
        }
        return list;
    }

    public java.util.List transform(Collection<ListViewModel> listModels) {
        java.util.List lists = new ArrayList<>();
        ListModel list;
        for (ListViewModel listModel : listModels) {
            list = transform(listModel);
            if (list != null) {
                lists.add(list);
            }
        }
        return lists;
    }
}
