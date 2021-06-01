package com.xdev.expy.textwatcher;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public class PasswordConfirmationTextWatcher implements TextWatcher {

    private final TextInputLayout inputLayout;
    private final EditText edtPassword;

    public PasswordConfirmationTextWatcher(TextInputLayout inputLayout, EditText edtPassword) {
        this.inputLayout = inputLayout;
        this.edtPassword = edtPassword;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable) {
        String passwordConfirm = editable.toString();
        String password = edtPassword.getText().toString();
        if (!passwordConfirm.equals(password)){
            inputLayout.setError("Kata sandi tidak sama");
        } else inputLayout.setErrorEnabled(false);
    }
}
