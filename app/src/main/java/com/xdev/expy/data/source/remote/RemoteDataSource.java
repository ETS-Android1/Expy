package com.xdev.expy.data.source.remote;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.xdev.expy.data.source.local.entity.ProductEntity;
import com.xdev.expy.data.source.remote.response.ProductResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xdev.expy.utils.ImageUtils.uriToByteArray;
import static com.xdev.expy.utils.ImageUtils.compressByteArray;

public class RemoteDataSource {

    private final String TAG = getClass().getSimpleName();

    private volatile static RemoteDataSource INSTANCE = null;
    private final FirebaseFirestore database;
    private final FirebaseStorage storage;
    private CollectionReference productsRef;

    private RemoteDataSource(){
        storage = FirebaseStorage.getInstance();
        database = FirebaseFirestore.getInstance();
        productsRef = database.collection("users");
    }

    public static RemoteDataSource getInstance() {
        if (INSTANCE == null){
            synchronized (RemoteDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RemoteDataSource();
                }
            }
        }
        return INSTANCE;
    }

    public LiveData<ApiResponse<List<ProductResponse>>> getProducts() {
        MutableLiveData<ApiResponse<List<ProductResponse>>> result = new MutableLiveData<>();

        productsRef.get().addOnCompleteListener(task -> {
            List<ProductResponse> productList = new ArrayList<>();
            if (task.isSuccessful()){
                if (task.getResult() != null){
                    for (DocumentSnapshot document : task.getResult()){
                        ProductResponse product = document.toObject(ProductResponse.class);
                        if (product != null) {
                            productList.add(product);
                            Log.d(TAG, "getProducts: " + product.toString());
                        }
                    }
                    result.postValue(ApiResponse.success(productList));
                }
            }  else {
                Log.w(TAG, "Error querying document", task.getException());
                result.postValue(ApiResponse.error("Error querying document", productList));
            }
        });

        return result;
    }

    public LiveData<ApiResponse<Boolean>> insertProduct(@NonNull ProductEntity product){
        MutableLiveData<ApiResponse<Boolean>> result = new MutableLiveData<>();
        result.postValue(ApiResponse.loading(null));

        productsRef.document(product.getId())
                .set(product)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "insertProduct: " + product.toString());
                        result.postValue(ApiResponse.success(true));
                    } else {
                        Log.w(TAG, "Error adding document", task.getException());
                        result.postValue(ApiResponse.error("Error adding document", false));
                    }
                });

        return result;
    }

    public LiveData<ApiResponse<Boolean>> updateProduct(@NonNull ProductEntity product){
        MutableLiveData<ApiResponse<Boolean>> result = new MutableLiveData<>();
        result.postValue(ApiResponse.loading(null));

        productsRef.document(product.getId())
                .update(objectToHashMap(product))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "updateProduct: " + product.toString());
                        result.postValue(ApiResponse.success(true));
                    } else {
                        Log.w(TAG, "Error updating document", task.getException());
                        result.postValue(ApiResponse.error("Error updating document", false));
                    }
                });

        return result;
    }

    public LiveData<ApiResponse<Boolean>> deleteProduct(@NonNull ProductEntity product){
        MutableLiveData<ApiResponse<Boolean>> result = new MutableLiveData<>();
        result.postValue(ApiResponse.loading(null));

        productsRef.document(product.getId())
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "deleteProduct: " + product.toString());
                        result.postValue(ApiResponse.success(true));
                    } else {
                        Log.w(TAG, "Error deleting document", task.getException());
                        result.postValue(ApiResponse.error("Error deleting document", false));
                    }
                });

        return result;
    }

    public LiveData<ApiResponse<String>> uploadImage(Context context, Uri uriPath, String storagePath, String fileName){
        MutableLiveData<ApiResponse<String>> result = new MutableLiveData<>();
        result.postValue(ApiResponse.loading(null));

        byte[] image = uriToByteArray(context, uriPath);
        image = compressByteArray(image, true);

        StorageReference ref = storage.getReference().child(storagePath + fileName);
        UploadTask uploadTask = ref.putBytes(image);
        uploadTask.addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uriResult -> {
            result.postValue(ApiResponse.success(uriResult.toString()));
            Log.d(TAG, "Image was uploaded");
        })).addOnFailureListener(e -> {
            Log.w(TAG, "Error uploading image", e);
            result.postValue(ApiResponse.error("Error uploading image", ""));
        });

        return result;
    }

    public CollectionReference getProductsReference(){
        return productsRef;
    }

    public void setProductsReference(String userId){
        productsRef = database.collection("users").document(userId).collection("products");
    }

    @NonNull
    private Map<String, Object> objectToHashMap(@NonNull ProductEntity product){
        Map<String, Object> document = new HashMap<>();
        document.put("id", product.getId());
        document.put("name", product.getName());
        document.put("expiryDate", product.getExpiryDate());
        document.put("isOpened", product.getIsOpened());
        document.put("openedDate", product.getOpenedDate());
        document.put("pao", product.getPao());
        document.put("reminders", product.getReminders());
        document.put("isFinished", product.getIsFinished());
        return document;
    }
}
