package com.xdev.expy.ui.main;

import com.xdev.expy.data.source.remote.entity.ProductEntity;

public interface MainCallback {
    void addUpdateProduct(ProductEntity product);
    void backToHome(boolean isCancelEditing);
}
