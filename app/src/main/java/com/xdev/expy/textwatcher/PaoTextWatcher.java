package com.xdev.expy.textwatcher;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.textfield.TextInputLayout;

public class PaoTextWatcher implements TextWatcher {

    private final TextInputLayout inputLayout;
    private final SwitchCompat switchOpened;

    public PaoTextWatcher(TextInputLayout inputLayout, SwitchCompat switchOpened) {
        this.inputLayout = inputLayout;
        this.switchOpened = switchOpened;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable) {
        if (switchOpened.isChecked()) {
            String value = editable.toString();
            if (value.isEmpty()){
                inputLayout.setError("Kolom ini tidak boleh kosong");
            } else if (value.length() > String.valueOf(Integer.MAX_VALUE).length()-1) {
                inputLayout.setError("Jumlah digit melebihi batas maksimum");
            } else inputLayout.setErrorEnabled(false);
        } else inputLayout.setErrorEnabled(false);
    }
}
