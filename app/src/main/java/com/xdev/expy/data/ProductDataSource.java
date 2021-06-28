package com.xdev.expy.data;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.google.firebase.firestore.CollectionReference;
import com.xdev.expy.data.source.local.entity.ProductWithReminders;
import com.xdev.expy.data.source.remote.ApiResponse;
import com.xdev.expy.data.source.local.entity.ProductEntity;
import com.xdev.expy.vo.Resource;

public interface ProductDataSource {

    LiveData<Resource<PagedList<ProductWithReminders>>> getProducts(boolean isExpired, boolean reFetch);

    LiveData<ApiResponse<Boolean>> insertProduct(ProductEntity product);

    LiveData<ApiResponse<Boolean>> updateProduct(ProductEntity product);

    LiveData<ApiResponse<Boolean>> deleteProduct(ProductEntity product);

    CollectionReference getProductsReference();

    void setProductsReference(String userId);

    void clearDatabase();
}
