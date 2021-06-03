package com.xdev.expy.textwatcher;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.xdev.expy.R;

import java.util.regex.Pattern;

public class CreatePasswordTextWatcher implements TextWatcher {

    private final Context context;
    private final TextInputLayout tilPassword;
    private final TextInputLayout tilPasswordConfirmation;
    private final EditText edtPasswordConfirmation;

    public CreatePasswordTextWatcher(Context context, TextInputLayout tilPassword, TextInputLayout tilPasswordConfirmation, EditText edtPasswordConfirmation) {
        this.context = context;
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
            tilPassword.setError(context.getResources().getString(R.string.hint_create_password));
        } else tilPassword.setErrorEnabled(false);

        String passwordConfirmation = edtPasswordConfirmation.getText().toString();
        if (!passwordConfirmation.equals(password)){
            tilPasswordConfirmation.setError(context.getResources().getString(R.string.invalid_password_confirmation));
        } else tilPasswordConfirmation.setErrorEnabled(false);
    }
}
