package com.justplay1.shoppist.repository;

import com.justplay1.shoppist.entity.ListItemDAO;
import com.justplay1.shoppist.entity.mappers.ListItemsDAODataMapper;
import com.justplay1.shoppist.repository.datasource.local.LocalListItemsDataStore;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;

import static com.justplay1.shoppist.entity.TestUtil.FAKE_ID;
import static com.justplay1.shoppist.entity.TestUtil.createFakeCategoryDAO;
import static com.justplay1.shoppist.entity.TestUtil.createFakeCategoryModel;
import static com.justplay1.shoppist.entity.TestUtil.createFakeCurrencyDAO;
import static com.justplay1.shoppist.entity.TestUtil.createFakeCurrencyModel;
import static com.justplay1.shoppist.entity.TestUtil.createFakeListItemDAO;
import static com.justplay1.shoppist.entity.TestUtil.createFakeListItemModel;
import static com.justplay1.shoppist.entity.TestUtil.createFakeUnitDAO;
import static com.justplay1.shoppist.entity.TestUtil.createFakeUnitModel;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Mkhytar Mkhoian.
 */
public class ListItemsDataRepositoryTest {

    private ListItemsDataRepository dataRepository;

    @Mock private ListItemsDAODataMapper mockMapper;
    @Mock private LocalListItemsDataStore mockLocalListItemsDataStore;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dataRepository = new ListItemsDataRepository(mockMapper, mockLocalListItemsDataStore);
    }

    @Test
    public void getItemById_ReturnsResults() {
        ListItemDAO dao = createFakeListItemDAO(createFakeCategoryDAO(), createFakeUnitDAO(), createFakeCurrencyDAO());
        when(mockLocalListItemsDataStore.getItem(FAKE_ID)).thenReturn(Observable.just(dao));

        dataRepository.getItem(FAKE_ID);

        verify(mockLocalListItemsDataStore).getItem(FAKE_ID);
    }

    @Test
    public void getItemsByParentId_ReturnsResults() {
        List<ListItemDAO> daos = new ArrayList<>();
        daos.add(createFakeListItemDAO(createFakeCategoryDAO(), createFakeUnitDAO(), createFakeCurrencyDAO()));
        when(mockLocalListItemsDataStore.getItems(FAKE_ID)).thenReturn(Observable.just(daos));

        dataRepository.getItems(FAKE_ID);

        verify(mockLocalListItemsDataStore).getItems(FAKE_ID);
    }

    @Test
    public void getItems_ReturnsResults() {
        List<ListItemDAO> daos = new ArrayList<>();
        daos.add(createFakeListItemDAO(createFakeCategoryDAO(), createFakeUnitDAO(), createFakeCurrencyDAO()));
        when(mockLocalListItemsDataStore.getItems()).thenReturn(Observable.just(daos));

        dataRepository.getItems();

        verify(mockLocalListItemsDataStore).getItems();
    }

    @Test
    public void save_InvokeCorrectApiCall() {
        List<ListItemDAO> daos = new ArrayList<>();
        daos.add(createFakeListItemDAO(createFakeCategoryDAO(), createFakeUnitDAO(), createFakeCurrencyDAO()));
        mockLocalListItemsDataStore.save(daos);

        dataRepository.save(Collections.singletonList(createFakeListItemModel(createFakeCategoryModel(), createFakeUnitModel(), createFakeCurrencyModel())));

        verify(mockLocalListItemsDataStore).save(daos);
    }

    @Test
    public void update_InvokeCorrectApiCall() {
        List<ListItemDAO> daos = new ArrayList<>();
        daos.add(createFakeListItemDAO(createFakeCategoryDAO(), createFakeUnitDAO(), createFakeCurrencyDAO()));
        mockLocalListItemsDataStore.update(daos);

        dataRepository.update(Collections.singletonList(createFakeListItemModel(createFakeCategoryModel(), createFakeUnitModel(), createFakeCurrencyModel())));

        verify(mockLocalListItemsDataStore).update(daos);
    }

    @Test
    public void delete_InvokeCorrectApiCall() {
        List<ListItemDAO> daos = new ArrayList<>();
        daos.add(createFakeListItemDAO(createFakeCategoryDAO(), createFakeUnitDAO(), createFakeCurrencyDAO()));
        mockLocalListItemsDataStore.delete(daos);

        dataRepository.delete(Collections.singletonList(createFakeListItemModel(createFakeCategoryModel(), createFakeUnitModel(), createFakeCurrencyModel())));

        verify(mockLocalListItemsDataStore).delete(daos);
    }

    @Test
    public void clear_InvokeCorrectApiCall() {
        when(mockLocalListItemsDataStore.clear()).thenReturn(1);

        dataRepository.clear();

        verify(mockLocalListItemsDataStore).clear();
    }
}