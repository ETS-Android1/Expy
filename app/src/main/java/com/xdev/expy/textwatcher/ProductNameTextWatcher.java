package com.xdev.expy.textwatcher;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputLayout;
import com.xdev.expy.R;

public class ProductNameTextWatcher implements TextWatcher {

    private final Context context;
    private final TextInputLayout inputLayout;

    public ProductNameTextWatcher(Context context, TextInputLayout inputLayout) {
        this.context = context;
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
            inputLayout.setError(context.getResources().getString(R.string.no_empty_field));
        } else inputLayout.setErrorEnabled(false);
    }
}
