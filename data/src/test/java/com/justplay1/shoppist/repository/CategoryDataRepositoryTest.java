package com.justplay1.shoppist.repository;

import com.justplay1.shoppist.entity.CategoryDAO;
import com.justplay1.shoppist.entity.mappers.CategoryDAODataMapper;
import com.justplay1.shoppist.repository.datasource.local.LocalCategoryDataStore;

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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Mkhytar Mkhoian.
 */
public class CategoryDataRepositoryTest {

    private CategoryDataRepository dataRepository;

    @Mock private CategoryDAODataMapper mockMapper;
    @Mock private LocalCategoryDataStore mockLocalCategoryDataStore;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dataRepository = new CategoryDataRepository(mockMapper, mockLocalCategoryDataStore);
    }

    @Test
    public void getItem_ReturnsResults() {
        CategoryDAO dao = createFakeCategoryDAO();
        when(mockLocalCategoryDataStore.getItem(FAKE_ID)).thenReturn(Observable.just(dao));

        dataRepository.getItem(FAKE_ID);

        verify(mockLocalCategoryDataStore).getItem(FAKE_ID);
    }

    @Test
    public void getItems_ReturnsResults() {
        List<CategoryDAO> daos = new ArrayList<>();
        daos.add(createFakeCategoryDAO());
        when(mockLocalCategoryDataStore.getItems()).thenReturn(Observable.just(daos));

        dataRepository.getItems();

        verify(mockLocalCategoryDataStore).getItems();
    }

    @Test
    public void save_InvokeCorrectApiCall() {
        List<CategoryDAO> daos = new ArrayList<>();
        daos.add(createFakeCategoryDAO());
        mockLocalCategoryDataStore.save(daos);

        dataRepository.save(Collections.singletonList(createFakeCategoryModel()));

        verify(mockLocalCategoryDataStore).save(daos);
    }

    @Test
    public void update_InvokeCorrectApiCall() {
        List<CategoryDAO> daos = new ArrayList<>();
        daos.add(createFakeCategoryDAO());
        mockLocalCategoryDataStore.update(daos);

        dataRepository.update(Collections.singletonList(createFakeCategoryModel()));

        verify(mockLocalCategoryDataStore).update(daos);
    }

    @Test
    public void delete_InvokeCorrectApiCall() {
        List<CategoryDAO> daos = new ArrayList<>();
        daos.add(createFakeCategoryDAO());
        mockLocalCategoryDataStore.delete(daos);

        dataRepository.delete(Collections.singletonList(createFakeCategoryModel()));

        verify(mockLocalCategoryDataStore).delete(daos);
    }

    @Test
    public void clear_InvokeCorrectApiCall() {
        when(mockLocalCategoryDataStore.clear()).thenReturn(1);

        dataRepository.clear();

        verify(mockLocalCategoryDataStore).clear();
    }
}