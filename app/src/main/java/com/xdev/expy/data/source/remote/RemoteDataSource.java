package com.xdev.expy.data.source.remote;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.xdev.expy.data.source.remote.entity.ProductEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xdev.expy.utils.DateUtils.getCurrentDate;
import static com.xdev.expy.utils.ImageUtils.convertUriToByteArray;
import static com.xdev.expy.utils.ImageUtils.getCompressedByteArray;

public class RemoteDataSource {

    private final String TAG = getClass().getSimpleName();

    private volatile static RemoteDataSource INSTANCE = null;
    private final FirebaseStorage storage;
    private final CollectionReference productsRef;

    private RemoteDataSource(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        productsRef = database.collection("products");
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

    public LiveData<ApiResponse<List<ProductEntity>>> queryProducts(boolean isExpiredProduct) {
        MutableLiveData<ApiResponse<List<ProductEntity>>> result = new MutableLiveData<>();
        List<ProductEntity> productList = new ArrayList<>();

        Query query = productsRef;
        if (isExpiredProduct) {
            query = query.whereLessThanOrEqualTo("expiryDate", getCurrentDate());
        } else {
            query = query.whereGreaterThan("expiryDate", getCurrentDate());
        }

        query.orderBy("expiryDate", Query.Direction.ASCENDING)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                if (task.getResult() != null){
                    for (DocumentSnapshot document : task.getResult()){
                        ProductEntity product = document.toObject(ProductEntity.class);
                        if (product != null) {
                            productList.add(product);
                            Log.d(TAG, "queryProducts: " + productList.toString());
                        }
                    }
                    if (!productList.isEmpty()) result.postValue(ApiResponse.success(productList));
                    else result.postValue(ApiResponse.empty("Empty list", productList));
                }
            }  else {
                Log.w(TAG, "Error querying document", task.getException());
                result.postValue(ApiResponse.error("Error querying document", productList));
            }
        });

        return result;
    }

    public LiveData<ApiResponse<ProductEntity>> queryProduct(String id) {
        MutableLiveData<ApiResponse<ProductEntity>> result = new MutableLiveData<>();
        productsRef.document(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                if (task.getResult() != null){
                    ProductEntity product = task.getResult().toObject(ProductEntity.class);
                    if (product != null) {
                        Log.d(TAG, "queryProduct: " + product.toString());
                        result.postValue(ApiResponse.success(product));
                    } else {
                        result.postValue(ApiResponse.empty("Null", product));
                    }
                }
            }  else {
                Log.w(TAG, "Error querying document", task.getException());
                result.postValue(ApiResponse.error("Error querying document", new ProductEntity()));
            }
        });
        return result;
    }

    public LiveData<ApiResponse<Boolean>> insertProduct(ProductEntity product){
        MutableLiveData<ApiResponse<Boolean>> result = new MutableLiveData<>();
        product.setId(productsRef.document().getId());
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

    public LiveData<ApiResponse<Boolean>> updateProduct(ProductEntity product){
        MutableLiveData<ApiResponse<Boolean>> result = new MutableLiveData<>();
        productsRef.document(product.getId())
                .update(objectToHashMap(product))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "updateProduct: " + product.toString());
                        result.postValue(ApiResponse.success(true));
                    }
                    else {
                        Log.w(TAG, "Error updating document", task.getException());
                        result.postValue(ApiResponse.error("Error updating document", false));
                    }
                });
        return result;
    }

    public LiveData<ApiResponse<Boolean>> deleteProduct(ProductEntity product){
        MutableLiveData<ApiResponse<Boolean>> result = new MutableLiveData<>();
        productsRef.document(product.getId())
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "deleteProduct: " + product.toString());
                        result.postValue(ApiResponse.success(true));
                    }
                    else {
                        Log.w(TAG, "Error deleting document", task.getException());
                        result.postValue(ApiResponse.error("Error deleting document", false));
                    }
                });
        return result;
    }

    public LiveData<ApiResponse<String>> uploadImage(Context context, Uri uriPath, String storagePath, String fileName){
        MutableLiveData<ApiResponse<String>> result = new MutableLiveData<>();

        byte[] image = convertUriToByteArray(context, uriPath);
        image = getCompressedByteArray(image, true);

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

    private Map<String, Object> objectToHashMap(ProductEntity product){
        Map<String, Object> document = new HashMap<>();
        document.put("id", product.getId());
        document.put("userId", product.getUserId());
        document.put("name", product.getName());
        document.put("expiryDate", product.getExpiryDate());
        document.put("opened", product.isOpened());
        document.put("openedDate", product.getOpenedDate());
        document.put("pao", product.getPao());
        document.put("reminders", product.getReminders());
        return document;
    }
}
