package com.xdev.expy.textwatcher;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;
import com.xdev.expy.R;

public class EmailTextWatcher implements TextWatcher {

    private final Context context;
    private final TextInputLayout inputLayout;

    public EmailTextWatcher(Context context, TextInputLayout inputLayout) {
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
        if (value.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            inputLayout.setError(context.getResources().getString(R.string.hint_email));
        } else inputLayout.setErrorEnabled(false);
    }
}
