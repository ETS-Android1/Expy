package com.xdev.expy.core.helper.textwatcher;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.xdev.expy.R;

public class PaoTextWatcher implements TextWatcher {

    private final Context context;
    private final TextInputLayout inputLayout;
    private final SwitchCompat switchOpened;

    public PaoTextWatcher(Context context, TextInputLayout inputLayout, SwitchCompat switchOpened) {
        this.context = context;
        this.inputLayout = inputLayout;
        this.switchOpened = switchOpened;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (switchOpened.isChecked()) {
            String value = editable.toString();
            if (value.isEmpty()) {
                inputLayout.setError(context.getResources().getString(R.string.no_empty_field));
            } else if (value.length() > String.valueOf(Integer.MAX_VALUE).length() - 1) {
                inputLayout.setError(context.getResources().getString(R.string.max_limit_number_of_digits));
            } else inputLayout.setErrorEnabled(false);
        } else inputLayout.setErrorEnabled(false);
    }
}
