package com.xdev.expy.data;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;

import com.xdev.expy.data.source.remote.ApiResponse;
import com.xdev.expy.data.source.remote.entity.ProductEntity;

import java.util.List;

public interface MainDataSource {

    LiveData<ApiResponse<List<ProductEntity>>> queryMonitoredProducts();

    LiveData<ApiResponse<List<ProductEntity>>> queryExpiredProducts();

    LiveData<ApiResponse<ProductEntity>> queryProduct(String id);

    LiveData<ApiResponse<Boolean>> insertProduct(ProductEntity product);

    LiveData<ApiResponse<Boolean>> updateProduct(ProductEntity product);

    LiveData<ApiResponse<Boolean>> deleteProduct(ProductEntity product);

    LiveData<ApiResponse<String>> uploadImage(Context context, Uri uriPath, String storagePath, String fileName);

    void addProductsSnapshotListener();
}
