package com.justplay1.shoppist.repository;

import com.justplay1.shoppist.entity.UnitDAO;
import com.justplay1.shoppist.entity.mappers.UnitsDAODataMapper;
import com.justplay1.shoppist.repository.datasource.local.LocalUnitsDataStore;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;

import static com.justplay1.shoppist.entity.TestUtil.FAKE_ID;
import static com.justplay1.shoppist.entity.TestUtil.createFakeUnitDAO;
import static com.justplay1.shoppist.entity.TestUtil.createFakeUnitModel;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Mkhytar Mkhoian.
 */
public class UnitsDataRepositoryTest {

    private UnitsDataRepository dataRepository;

    @Mock private UnitsDAODataMapper mockMapper;
    @Mock private LocalUnitsDataStore mockLocalUnitsDataStore;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dataRepository = new UnitsDataRepository(mockMapper, mockLocalUnitsDataStore);
    }

    @Test
    public void getItem_ReturnsResults() {
        UnitDAO dao = createFakeUnitDAO();
        when(mockLocalUnitsDataStore.getItem(FAKE_ID)).thenReturn(Observable.just(dao));

        dataRepository.getItem(FAKE_ID);

        verify(mockLocalUnitsDataStore).getItem(FAKE_ID);
    }

    @Test
    public void getItems_ReturnsResults() {
        List<UnitDAO> daos = new ArrayList<>();
        daos.add(createFakeUnitDAO());
        when(mockLocalUnitsDataStore.getItems()).thenReturn(Observable.just(daos));

        dataRepository.getItems();

        verify(mockLocalUnitsDataStore).getItems();
    }

    @Test
    public void save_InvokeCorrectApiCall() {
        List<UnitDAO> daos = new ArrayList<>();
        daos.add(createFakeUnitDAO());
        mockLocalUnitsDataStore.save(daos);

        dataRepository.save(Collections.singletonList(createFakeUnitModel()));

        verify(mockLocalUnitsDataStore).save(daos);
    }

    @Test
    public void update_InvokeCorrectApiCall() {
        List<UnitDAO> daos = new ArrayList<>();
        daos.add(createFakeUnitDAO());
        mockLocalUnitsDataStore.update(daos);

        dataRepository.update(Collections.singletonList(createFakeUnitModel()));

        verify(mockLocalUnitsDataStore).update(daos);
    }

    @Test
    public void delete_InvokeCorrectApiCall() {
        List<UnitDAO> daos = new ArrayList<>();
        daos.add(createFakeUnitDAO());
        mockLocalUnitsDataStore.delete(daos);

        dataRepository.delete(Collections.singletonList(createFakeUnitModel()));

        verify(mockLocalUnitsDataStore).delete(daos);
    }

    @Test
    public void clear_InvokeCorrectApiCall() {
        when(mockLocalUnitsDataStore.clear()).thenReturn(1);

        dataRepository.clear();

        verify(mockLocalUnitsDataStore).clear();
    }
}