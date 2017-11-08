package com.example.hemantkatariya.imageuploading.libraries;

import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.afollestad.bridge.Bridge;
import com.afollestad.bridge.BridgeException;
import com.afollestad.bridge.MultipartForm;
import com.afollestad.bridge.Pipe;
import com.afollestad.bridge.ProgressCallback;
import com.afollestad.bridge.Request;
import com.afollestad.bridge.Response;
import com.afollestad.bridge.ResponseConvertCallback;
import com.android.volley.RequestQueue;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.example.hemantkatariya.imageuploading.network.API_Client_Request;
import com.example.hemantkatariya.imageuploading.network.API_Methods;
import com.example.hemantkatariya.imageuploading.network.AppUtils;
import com.example.hemantkatariya.imageuploading.network.ResponseCallback;
import com.example.hemantkatariya.imageuploading.utils.ProgressRequestBody;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Hemant Katariya on 11/8/2017.
 */

public class Uplaod_Files{

    private static Uplaod_Files uplaod_files = new Uplaod_Files();

    public Uplaod_Files() {
    }

    public static Uplaod_Files getInstance() {
        if (uplaod_files == null) {
            uplaod_files = new Uplaod_Files();
        }
        return uplaod_files;
    }

    public void uploadViaAmitSheker(String URL_Upload, String path, String fileName, final NumberProgressBar numberProgressBar, final ResponseCallback responseCallback) {
        File f = new File(path + fileName);
        AndroidNetworking.upload(URL_Upload)
                .addHeaders("Authorization", AppUtils.Token)
                .addMultipartFile("image", f)
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress

                        int percentage = (int) (bytesUploaded * 100.0 / totalBytes + 0.5);
                        numberProgressBar.setProgress(percentage);
                    }
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("---response---" + response);
                        responseCallback.success(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        System.out.println("anError:" + anError.getErrorBody());
                    }
                });
    }

    public void uploadViaBridge(String URL_Upload, String path, String fileName, final NumberProgressBar numberProgressBar, final ResponseCallback responseCallback) {
        try {
            MultipartForm form = new MultipartForm()
                    .add("FileUpload", fileName, Pipe.forFile(new File(path + fileName)));
            Bridge.post(URL_Upload)
                    .header("Authorization", AppUtils.Token)
                    .body(form)
                    .uploadProgress(new ProgressCallback() {
                        @Override
                        public void progress(Request request, int current, int total, int percent) {
                            numberProgressBar.setProgress(percent);
                        }
                    })
                    .asString(new ResponseConvertCallback<String>() {
                        @Override
                        public void onResponse(@Nullable Response response, @Nullable String object, @Nullable BridgeException e) {

                            System.out.println("---response---" + response.toString());
                            responseCallback.success(response.asString());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uploadViaVolleyPlus(String URL_Upload, String path, String fileName, final NumberProgressBar numberProgressBar, Context applicationContext, final ResponseCallback responseCallback) {

        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(com.android.volley.Request.Method.POST, URL_Upload,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("---response---" + response.toString());
                        responseCallback.success(response);
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", AppUtils.Token);
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        smr.addStringParam("type", "photos");
        smr.addFile("image", path + fileName);
        RequestQueue requestQueue = Volley.newRequestQueue(applicationContext);
        requestQueue.add(smr);
    }

    public void uploadViaRetrofit(String URL_Upload, String path, String fileName, final NumberProgressBar numberProgressBar, Context applicationContext, final ResponseCallback responseCallback) {


        File file = new File(path + fileName);
        Uri selectedUri = Uri.fromFile(file);
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(selectedUri.toString());
        String mimeType
                = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
       /* RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), fileName);*/
        ProgressRequestBody requestFile = new ProgressRequestBody(file, mimeType, new ProgressRequestBody.UploadCallbacks() {
            @Override
            public void onProgressUpdate(int percentage) {
                numberProgressBar.setProgress(percentage+1);
            }

            @Override
            public void onError() {

            }

            @Override
            public void onFinish() {
                numberProgressBar.setProgress(100);
            }
        });
        MultipartBody.Part body = MultipartBody.Part.createFormData("filename", fileName, requestFile);

        API_Methods api_methods = API_Client_Request.apiRequestService();
        api_methods.upload(AppUtils.Token, body).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    responseCallback.success(responseBody.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}