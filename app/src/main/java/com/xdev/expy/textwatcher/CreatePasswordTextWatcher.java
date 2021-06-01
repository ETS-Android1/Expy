package com.xdev.expy.textwatcher;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class CreatePasswordTextWatcher implements TextWatcher {

    private final TextInputLayout tilPassword;
    private final TextInputLayout tilPasswordConfirmation;
    private final EditText edtPasswordConfirmation;

    public CreatePasswordTextWatcher(TextInputLayout tilPassword, TextInputLayout tilPasswordConfirmation, EditText edtPasswordConfirmation) {
        this.tilPassword = tilPassword;
        this.tilPasswordConfirmation = tilPasswordConfirmation;
        this.edtPasswordConfirmation = edtPasswordConfirmation;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable) {
        String password = editable.toString();
        if (password.isEmpty() || !Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$").matcher(password).matches()){
            tilPassword.setError("Masukkan minimal 6 karakter dengan kombinasi 1 huruf besar, huruf kecil, angka, dan simbol");
        } else tilPassword.setErrorEnabled(false);

        String passwordConfirm = edtPasswordConfirmation.getText().toString();
        if (!passwordConfirm.equals(password)){
            tilPasswordConfirmation.setError("Kata sandi tidak sama");
        } else tilPasswordConfirmation.setErrorEnabled(false);
    }
}
