package com.xdev.expy.core.data;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.google.firebase.firestore.CollectionReference;
import com.xdev.expy.core.data.source.local.entity.ProductWithReminders;
import com.xdev.expy.core.data.source.local.entity.ProductEntity;
import com.xdev.expy.core.vo.Resource;

public interface ProductDataSource {

    LiveData<Resource<PagedList<ProductWithReminders>>> getProducts(boolean isExpired, boolean reFetch);

    void insertProduct(ProductEntity product);

    void updateProduct(ProductEntity product);

    void deleteProduct(ProductEntity product);

    CollectionReference getProductsReference();

    void setProductsReference(String userId);

    void clearDatabase();
}
