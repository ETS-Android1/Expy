package com.xdev.expy.textwatcher;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputLayout;

public class ProductNameTextWatcher implements TextWatcher {

    private final TextInputLayout inputLayout;

    public ProductNameTextWatcher(TextInputLayout inputLayout) {
        this.inputLayout = inputLayout;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable) {
        String value = editable.toString();
        if (value.isEmpty()){
            inputLayout.setError("Kolom ini tidak boleh kosong");
        } else inputLayout.setErrorEnabled(false);
    }
}
