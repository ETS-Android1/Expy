package com.xdev.expy.ui.main;

import com.xdev.expy.data.source.local.entity.ProductEntity;

public interface MainCallback {

    void openProductEditor(ProductEntity product);

    void adjustViews(boolean isAddUpdateFragmentVisible);

    void backToHome(boolean isCancelEditing);
}
