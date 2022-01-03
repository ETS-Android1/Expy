package com.xdev.expy.core.data;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.xdev.expy.R;
import com.xdev.expy.core.util.Constants;
import com.xdev.expy.core.util.Event;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import static com.xdev.expy.core.util.AppUtils.getAvatarFromResource;
import static com.xdev.expy.core.util.AppUtils.getRandomAvatar;

public class AuthRepository {

    private final String TAG = getClass().getSimpleName();

    private final Application application;
    private final FirebaseAuth firebaseAuth;

    private final MutableLiveData<FirebaseUser> _user = new MutableLiveData<>();

    public LiveData<FirebaseUser> getUser() {
        return _user;
    }

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();

    public LiveData<Boolean> isLoading() {
        return _isLoading;
    }

    private final MutableLiveData<Event<String>> _toastText = new MutableLiveData<>();

    public MutableLiveData<Event<String>> getToastText() {
        return _toastText;
    }

    private volatile static AuthRepository INSTANCE = null;

    private AuthRepository(Application application) {
        this.application = application;
        firebaseAuth = FirebaseAuth.getInstance();
        _user.postValue(firebaseAuth.getCurrentUser());
    }

    public static AuthRepository getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (AuthRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AuthRepository(application);
                }
            }
        }
        return INSTANCE;
    }

    public void authWithGoogle(AuthCredential authCredential) {
        _isLoading.postValue(true);
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "signInWithCredential: success");
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                boolean isNewAccount = true;
                if (task.getResult() != null && task.getResult().getAdditionalUserInfo() != null) {
                    isNewAccount = task.getResult().getAdditionalUserInfo().isNewUser();
                }

                if (isNewAccount) {
                    // Give avatar to new Google user
                    updateProfile(getRandomAvatar());
                } else {
                    // Give avatar to Google-first logged-in old user
                    if (firebaseUser != null &&
                            getAvatarFromResource(firebaseUser.getPhotoUrl()) == Constants.NO_AVATAR) {
                        updateProfile(getRandomAvatar());
                    }
                }

                _user.postValue(firebaseUser);
            } else {
                _toastText.postValue(new Event<>(application.getResources().getString(R.string.failure_auth_with_google)));
                Log.w(TAG, "signInWithCredential: failure", task.getException());
            }
            _isLoading.postValue(false);
        });
    }

    public void registerWithEmail(String name, String email, String password) {
        _isLoading.postValue(true);
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "createUserWithEmail: success");
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                updateName(name);
                updateProfile(getRandomAvatar());
                sendEmailVerification();
                _user.postValue(firebaseUser);
            } else {
                _toastText.postValue(new Event<>(application.getResources().getString(R.string.failure_register_with_email)));
                Log.w(TAG, "createUserWithEmail: failure", task.getException());
            }
            _isLoading.postValue(false);
        });
    }

    public void loginWithEmail(String email, String password) {
        _isLoading.postValue(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "signInWithEmail: success");
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                _user.postValue(firebaseUser);
            } else {
                _toastText.postValue(new Event<>(application.getResources().getString(R.string.failure_login_with_email)));
                Log.w(TAG, "signInWithEmail: failure", task.getException());
            }
            _isLoading.postValue(false);
        });
    }

    private void sendEmailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            _isLoading.postValue(true);
            firebaseUser.sendEmailVerification().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    _toastText.postValue(new Event<>(application.getResources().getString(R.string.success_send_email_verification)));
                    Log.d(TAG, "sendEmailVerification: success");
                } else {
                    _toastText.postValue(new Event<>(application.getResources().getString(R.string.failure_send_email_verification)));
                    Log.w(TAG, "sendEmailVerification: failure", task.getException());
                }
                _isLoading.postValue(false);
            });
        }
    }

    public void sendPasswordReset(String email) {
        _isLoading.postValue(true);
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                _toastText.postValue(new Event<>(application.getResources().getString(R.string.success_send_password_reset)));
                Log.d(TAG, "sendPasswordReset: success");
            } else {
                _toastText.postValue(new Event<>(application.getResources().getString(R.string.failure_send_password_reset)));
                Log.w(TAG, "sendPasswordReset: failure", task.getException());
            }
            _isLoading.postValue(false);
        });
    }

    public void updateName(String newName) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            _isLoading.postValue(true);
            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newName)
                    .build();
            firebaseUser.updateProfile(profileUpdate).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    _user.postValue(firebaseUser);
                    Log.d(TAG, "updateName: success");
                } else Log.w(TAG, "updateName: failure", task.getException());
                _isLoading.postValue(false);
            });
        }
    }

    public void updateProfile(String newProfile) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            _isLoading.postValue(true);
            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(Uri.parse(newProfile))
                    .build();
            firebaseUser.updateProfile(profileUpdate).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    _user.postValue(firebaseUser);
                    Log.d(TAG, "updateProfile: success");
                } else Log.w(TAG, "updateProfile: failure", task.getException());
                _isLoading.postValue(false);
            });
        }
    }

    public void logout() {
        _isLoading.postValue(true);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(application.getResources().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignIn.getClient(application, gso).signOut().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                firebaseAuth.signOut();
                _user.postValue(null);
                Log.d(TAG, "logout: success");
            } else {
                _toastText.postValue(new Event<>(application.getResources().getString(R.string.failure_logout)));
                Log.w(TAG, "logout: failure", task.getException());
            }
            _isLoading.postValue(false);
        });
    }
}