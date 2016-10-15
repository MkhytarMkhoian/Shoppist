package com.justplay1.shoppist.repository;

import com.justplay1.shoppist.entity.ListDAO;
import com.justplay1.shoppist.entity.mappers.ListDAODataMapper;
import com.justplay1.shoppist.repository.datasource.local.LocalListDataStore;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;

import static com.justplay1.shoppist.entity.TestUtil.FAKE_ID;
import static com.justplay1.shoppist.entity.TestUtil.createFakeListDAO;
import static com.justplay1.shoppist.entity.TestUtil.createFakeListModel;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Mkhytar Mkhoian.
 */
public class ListDataRepositoryTest {

    private ListDataRepository dataRepository;

    @Mock private ListDAODataMapper mockMapper;
    @Mock private LocalListDataStore mockLocalListDataStore;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dataRepository = new ListDataRepository(mockMapper, mockLocalListDataStore);
    }

    @Test
    public void getItems_ReturnsResults() {
        List<ListDAO> daos = new ArrayList<>();
        daos.add(createFakeListDAO());
        when(mockLocalListDataStore.getItems()).thenReturn(Observable.just(daos));

        dataRepository.getItems();

        verify(mockLocalListDataStore).getItems();
    }

    @Test
    public void save_InvokeCorrectApiCall() {
        List<ListDAO> daos = new ArrayList<>();
        daos.add(createFakeListDAO());
        mockLocalListDataStore.save(daos);

        dataRepository.save(Collections.singletonList(createFakeListModel()));

        verify(mockLocalListDataStore).save(daos);
    }

    @Test
    public void update_InvokeCorrectApiCall() {
        List<ListDAO> daos = new ArrayList<>();
        daos.add(createFakeListDAO());
        mockLocalListDataStore.update(daos);

        dataRepository.update(Collections.singletonList(createFakeListModel()));

        verify(mockLocalListDataStore).update(daos);
    }

    @Test
    public void delete_InvokeCorrectApiCall() {
        List<ListDAO> daos = new ArrayList<>();
        daos.add(createFakeListDAO());
        mockLocalListDataStore.delete(daos);

        dataRepository.delete(Collections.singletonList(createFakeListModel()));

        verify(mockLocalListDataStore).delete(daos);
    }

    @Test
    public void deleteListItems_InvokeCorrectApiCall() {
        dataRepository.deleteListItems(FAKE_ID);

        verify(mockLocalListDataStore).deleteListItems(FAKE_ID);
    }

    @Test
    public void clear_InvokeCorrectApiCall() {
        when(mockLocalListDataStore.clear()).thenReturn(1);

        dataRepository.clear();

        verify(mockLocalListDataStore).clear();
    }
}