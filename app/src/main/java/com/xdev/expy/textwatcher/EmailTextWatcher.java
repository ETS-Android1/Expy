package com.xdev.expy.textwatcher;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;

import com.google.android.material.textfield.TextInputLayout;

public class EmailTextWatcher implements TextWatcher {

    private final TextInputLayout inputLayout;

    public EmailTextWatcher(TextInputLayout inputLayout) {
        this.inputLayout = inputLayout;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable) {
        String value = editable.toString();
        if (value.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(value).matches()){
            inputLayout.setError("Masukkan email yang valid");
        } else inputLayout.setErrorEnabled(false);
    }
}
