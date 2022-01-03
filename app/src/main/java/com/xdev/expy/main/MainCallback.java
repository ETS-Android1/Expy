package com.xdev.expy.main;

import com.xdev.expy.core.data.source.local.entity.ProductEntity;

public interface MainCallback {

    void openProductEditor(ProductEntity product);

    void adjustViews(boolean isAddUpdateFragmentVisible);

    void backToHome(boolean isCancelEditing);
}
