package com.xdev.expy.core.helper.textwatcher;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;
import com.xdev.expy.R;

public class PasswordConfirmationTextWatcher implements TextWatcher {

    private final Context context;
    private final TextInputLayout inputLayout;
    private final EditText edtPassword;

    public PasswordConfirmationTextWatcher(Context context, TextInputLayout inputLayout, EditText edtPassword) {
        this.context = context;
        this.inputLayout = inputLayout;
        this.edtPassword = edtPassword;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(@NonNull Editable editable) {
        String passwordConfirmation = editable.toString();
        String password = edtPassword.getText().toString();
        if (!passwordConfirmation.equals(password)) {
            inputLayout.setError(context.getResources().getString(R.string.invalid_password_confirmation));
        } else inputLayout.setErrorEnabled(false);
    }
}
