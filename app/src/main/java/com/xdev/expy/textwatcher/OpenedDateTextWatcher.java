package com.xdev.expy.textwatcher;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.xdev.expy.R;

public class OpenedDateTextWatcher implements TextWatcher {

    private final Context context;
    private final TextInputLayout inputLayout;
    private final SwitchCompat switchOpened;

    public OpenedDateTextWatcher(Context context, TextInputLayout inputLayout, SwitchCompat switchOpened) {
        this.context = context;
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
                inputLayout.setError(context.getResources().getString(R.string.hint_date_picker));
            } else inputLayout.setErrorEnabled(false);
        } else inputLayout.setErrorEnabled(false);
    }
}
