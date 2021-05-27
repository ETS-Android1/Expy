package com.xdev.expy.data;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;

import com.xdev.expy.data.source.remote.ApiResponse;
import com.xdev.expy.data.source.remote.RemoteDataSource;
import com.xdev.expy.data.source.remote.entity.ProductEntity;

import java.util.List;

public class MainRepository implements MainDataSource {

    private volatile static MainRepository INSTANCE = null;
    private final RemoteDataSource remoteDataSource;

    private MainRepository(RemoteDataSource remoteDataSource){
        this.remoteDataSource = remoteDataSource;
    }

    public static MainRepository getInstance(RemoteDataSource remoteDataSource) {
        if (INSTANCE == null){
            synchronized (MainRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MainRepository(remoteDataSource);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public LiveData<ApiResponse<List<ProductEntity>>> queryMonitoredProducts() {
        return remoteDataSource.queryProducts(false);
    }

    @Override
    public LiveData<ApiResponse<List<ProductEntity>>> queryExpiredProducts() {
        return remoteDataSource.queryProducts(true);
    }

    @Override
    public LiveData<ApiResponse<ProductEntity>> queryProduct(String id) {
        return remoteDataSource.queryProduct(id);
    }

    @Override
    public LiveData<ApiResponse<Boolean>> insertProduct(ProductEntity product) {
        return remoteDataSource.insertProduct(product);
    }

    @Override
    public LiveData<ApiResponse<Boolean>> updateProduct(ProductEntity product) {
        return remoteDataSource.updateProduct(product);
    }

    @Override
    public LiveData<ApiResponse<Boolean>> deleteProduct(ProductEntity product) {
        return remoteDataSource.deleteProduct(product);
    }

    @Override
    public LiveData<ApiResponse<String>> uploadImage(Context context, Uri uriPath, String storagePath, String fileName) {
        return remoteDataSource.uploadImage(context, uriPath, storagePath, fileName);
    }

    @Override
    public void addProductsSnapshotListener() {
        remoteDataSource.addProductsSnapshotListener();
    }
}
