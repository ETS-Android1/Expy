package com.xdev.expy.textwatcher;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;
import com.xdev.expy.R;

public class PasswordTextWatcher implements TextWatcher {

    private final Context context;
    private final TextInputLayout inputLayout;

    public PasswordTextWatcher(Context context, TextInputLayout inputLayout) {
        this.context = context;
        this.inputLayout = inputLayout;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(@NonNull Editable editable) {
        String value = editable.toString();
        if (value.isEmpty()) {
            inputLayout.setError(context.getResources().getString(R.string.hint_password));
        } else inputLayout.setErrorEnabled(false);
    }
}
