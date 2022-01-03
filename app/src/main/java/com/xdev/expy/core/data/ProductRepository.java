package com.xdev.expy.core.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.google.firebase.firestore.CollectionReference;
import com.xdev.expy.core.util.AppExecutors;
import com.xdev.expy.core.data.source.local.LocalDataSource;
import com.xdev.expy.core.data.source.local.entity.ProductWithReminders;
import com.xdev.expy.core.data.source.local.entity.ReminderEntity;
import com.xdev.expy.core.data.source.remote.ApiResponse;
import com.xdev.expy.core.data.source.remote.RemoteDataSource;
import com.xdev.expy.core.data.source.local.entity.ProductEntity;
import com.xdev.expy.core.data.source.remote.response.ProductResponse;
import com.xdev.expy.core.data.source.remote.response.ReminderResponse;
import com.xdev.expy.core.vo.Resource;

import java.util.ArrayList;
import java.util.List;

import static com.xdev.expy.core.util.AppUtils.isNetworkAvailable;

public class ProductRepository implements ProductDataSource {

    private volatile static ProductRepository INSTANCE = null;
    private final RemoteDataSource remoteDataSource;
    private final LocalDataSource localDataSource;
    private final AppExecutors appExecutors;

    private ProductRepository(@NonNull RemoteDataSource remoteDataSource, @NonNull LocalDataSource localDataSource, AppExecutors appExecutors) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
        this.appExecutors = appExecutors;
    }

    public static ProductRepository getInstance(RemoteDataSource remoteData, LocalDataSource localDataSource, AppExecutors appExecutors) {
        if (INSTANCE == null) {
            synchronized (ProductRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ProductRepository(remoteData, localDataSource, appExecutors);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public LiveData<Resource<PagedList<ProductWithReminders>>> getProducts(boolean isExpired, boolean fetchNow) {
        return new NetworkBoundResource<PagedList<ProductWithReminders>, List<ProductResponse>>(appExecutors) {
            @Override
            protected LiveData<PagedList<ProductWithReminders>> loadFromDB() {
                PagedList.Config config = new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setInitialLoadSizeHint(10)
                        .setPageSize(10)
                        .build();
                return new LivePagedListBuilder<>(localDataSource.getProducts(isExpired), config).build();
            }

            @Override
            protected Boolean shouldFetch(PagedList<ProductWithReminders> data) {
                return isNetworkAvailable() && fetchNow;
            }

            @Override
            protected LiveData<ApiResponse<List<ProductResponse>>> createCall() {
                return remoteDataSource.getProducts();
            }

            @Override
            protected void saveCallResult(List<ProductResponse> data) {
                ArrayList<ProductEntity> productList = new ArrayList<>();
                ArrayList<ReminderEntity> reminderList = new ArrayList<>();
                for (ProductResponse productResponse : data) {
                    ProductEntity product = new ProductEntity(productResponse.getId(),
                            productResponse.getName(),
                            productResponse.getExpiryDate(),
                            productResponse.isOpened,
                            productResponse.getOpenedDate(),
                            productResponse.getPao(),
                            new ArrayList<>(),
                            productResponse.isFinished);
                    productList.add(product);

                    for (ReminderResponse reminderResponse : productResponse.getReminders()) {
                        ReminderEntity reminder = new ReminderEntity(reminderResponse.getId(),
                                productResponse.getId(),
                                reminderResponse.getTimestamp());
                        reminderList.add(reminder);
                    }
                }
                localDataSource.insertProductsAndReminders(productList, reminderList);
            }
        }.asLiveData();
    }

    @Override
    public void insertProduct(ProductEntity product) {
        remoteDataSource.insertProduct(product);
    }

    @Override
    public void updateProduct(ProductEntity product) {
        remoteDataSource.updateProduct(product);
    }

    @Override
    public void deleteProduct(ProductEntity product) {
        remoteDataSource.deleteProduct(product);
    }

    @Override
    public CollectionReference getProductsReference() {
        return remoteDataSource.getProductsReference();
    }

    @Override
    public void setProductsReference(String userId) {
        remoteDataSource.setProductsReference(userId);
    }

    @Override
    public void clearDatabase() {
        appExecutors.diskIO().execute(localDataSource::clearDatabase);
    }
}
