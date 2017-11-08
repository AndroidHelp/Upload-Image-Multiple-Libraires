package com.example.hemantkatariya.imageuploading.network;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by Hemant Katariya on 11/8/2017.
 */

public interface API_Methods {

    @Multipart
    @POST("upload")
    Observable<ResponseBody> upload(@Header("Authorization") String token, @Part MultipartBody.Part file);

}
