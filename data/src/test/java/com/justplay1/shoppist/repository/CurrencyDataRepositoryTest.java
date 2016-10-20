package com.justplay1.shoppist.repository;

import com.justplay1.shoppist.entity.CurrencyDAO;
import com.justplay1.shoppist.entity.mappers.CurrencyDAODataMapper;
import com.justplay1.shoppist.repository.datasource.local.LocalCurrencyDataStore;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;

import static com.justplay1.shoppist.entity.DAOUtil.FAKE_ID;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeCurrencyDAO;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeCurrencyModel;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Mkhytar Mkhoian.
 */
public class CurrencyDataRepositoryTest {

    private CurrencyDataRepository dataRepository;

    @Mock private CurrencyDAODataMapper mockMapper;
    @Mock private LocalCurrencyDataStore mockLocalCurrencyDataStore;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dataRepository = new CurrencyDataRepository(mockMapper, mockLocalCurrencyDataStore);
    }

    @Test
    public void getItem_ReturnsResults() {
        CurrencyDAO dao = createFakeCurrencyDAO();
        when(mockLocalCurrencyDataStore.getItem(FAKE_ID)).thenReturn(Observable.just(dao));

        dataRepository.getItem(FAKE_ID);

        verify(mockLocalCurrencyDataStore).getItem(FAKE_ID);
    }

    @Test
    public void getItems_ReturnsResults() {
        List<CurrencyDAO> daos = new ArrayList<>();
        daos.add(createFakeCurrencyDAO());
        when(mockLocalCurrencyDataStore.getItems()).thenReturn(Observable.just(daos));

        dataRepository.getItems();

        verify(mockLocalCurrencyDataStore).getItems();
    }

    @Test
    public void save_InvokeCorrectApiCall() {
        List<CurrencyDAO> daos = new ArrayList<>();
        daos.add(createFakeCurrencyDAO());
        mockLocalCurrencyDataStore.save(daos);

        dataRepository.save(Collections.singletonList(createFakeCurrencyModel()));

        verify(mockLocalCurrencyDataStore).save(daos);
    }

    @Test
    public void update_InvokeCorrectApiCall() {
        List<CurrencyDAO> daos = new ArrayList<>();
        daos.add(createFakeCurrencyDAO());
        mockLocalCurrencyDataStore.update(daos);

        dataRepository.update(Collections.singletonList(createFakeCurrencyModel()));

        verify(mockLocalCurrencyDataStore).update(daos);
    }

    @Test
    public void delete_InvokeCorrectApiCall() {
        List<CurrencyDAO> daos = new ArrayList<>();
        daos.add(createFakeCurrencyDAO());
        mockLocalCurrencyDataStore.delete(daos);

        dataRepository.delete(Collections.singletonList(createFakeCurrencyModel()));

        verify(mockLocalCurrencyDataStore).delete(daos);
    }

    @Test
    public void clear_InvokeCorrectApiCall() {
        when(mockLocalCurrencyDataStore.clear()).thenReturn(1);

        dataRepository.clear();

        verify(mockLocalCurrencyDataStore).clear();
    }
}