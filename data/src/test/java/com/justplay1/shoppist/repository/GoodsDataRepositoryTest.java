package com.justplay1.shoppist.repository;

import com.justplay1.shoppist.entity.ProductDAO;
import com.justplay1.shoppist.entity.mappers.GoodsDAODataMapper;
import com.justplay1.shoppist.repository.datasource.local.LocalGoodsDataStore;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;

import static com.justplay1.shoppist.entity.DAOUtil.FAKE_ID;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeCategoryDAO;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeCategoryModel;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeProductDAO;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeProductModel;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeUnitDAO;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeUnitModel;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Mkhytar Mkhoian.
 */
public class GoodsDataRepositoryTest {

    private GoodsDataRepository dataRepository;

    @Mock private GoodsDAODataMapper mockMapper;
    @Mock private LocalGoodsDataStore mockLocalGoodsDataStore;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dataRepository = new GoodsDataRepository(mockMapper, mockLocalGoodsDataStore);
    }

    @Test
    public void getItem_ReturnsResults() {
        ProductDAO dao = createFakeProductDAO(createFakeUnitDAO(), createFakeCategoryDAO());
        when(mockLocalGoodsDataStore.getItem(FAKE_ID)).thenReturn(Observable.just(dao));

        dataRepository.getItem(FAKE_ID);

        verify(mockLocalGoodsDataStore).getItem(FAKE_ID);
    }

    @Test
    public void getItems_ReturnsResults() {
        List<ProductDAO> daos = new ArrayList<>();
        daos.add(createFakeProductDAO(createFakeUnitDAO(), createFakeCategoryDAO()));
        when(mockLocalGoodsDataStore.getItems()).thenReturn(Observable.just(daos));

        dataRepository.getItems();

        verify(mockLocalGoodsDataStore).getItems();
    }

    @Test
    public void save_InvokeCorrectApiCall() {
        List<ProductDAO> daos = new ArrayList<>();
        daos.add(createFakeProductDAO(createFakeUnitDAO(), createFakeCategoryDAO()));
        mockLocalGoodsDataStore.save(daos);

        dataRepository.save(Collections.singletonList(createFakeProductModel(createFakeUnitModel(), createFakeCategoryModel())));

        verify(mockLocalGoodsDataStore).save(daos);
    }

    @Test
    public void update_InvokeCorrectApiCall() {
        List<ProductDAO> daos = new ArrayList<>();
        daos.add(createFakeProductDAO(createFakeUnitDAO(), createFakeCategoryDAO()));
        mockLocalGoodsDataStore.update(daos);

        dataRepository.update(Collections.singletonList(createFakeProductModel(createFakeUnitModel(), createFakeCategoryModel())));

        verify(mockLocalGoodsDataStore).update(daos);
    }

    @Test
    public void delete_InvokeCorrectApiCall() {
        List<ProductDAO> daos = new ArrayList<>();
        daos.add(createFakeProductDAO(createFakeUnitDAO(), createFakeCategoryDAO()));
        mockLocalGoodsDataStore.delete(daos);

        dataRepository.delete(Collections.singletonList(createFakeProductModel(createFakeUnitModel(), createFakeCategoryModel())));

        verify(mockLocalGoodsDataStore).delete(daos);
    }

    @Test
    public void clear_InvokeCorrectApiCall() {
        when(mockLocalGoodsDataStore.clear()).thenReturn(1);

        dataRepository.clear();

        verify(mockLocalGoodsDataStore).clear();
    }
}