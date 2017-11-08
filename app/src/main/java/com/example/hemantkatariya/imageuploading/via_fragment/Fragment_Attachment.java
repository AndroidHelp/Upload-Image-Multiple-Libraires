package com.example.hemantkatariya.imageuploading.via_fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.example.hemantkatariya.imageuploading.R;
import com.example.hemantkatariya.imageuploading.network.ResponseCallback;
import com.example.hemantkatariya.imageuploading.libraries.Uplaod_Files;
import com.example.hemantkatariya.imageuploading.network.AppUtils;
import com.example.hemantkatariya.imageuploading.utils.Imageutils;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Attachment extends Fragment implements Imageutils.ImageAttachmentListener {


    ImageView iv_attachment;
    Button btn_upload;
    NumberProgressBar numberProgressBar;
    TextView txt_response;

    //image path
    String path = "";
    private String file_name;
    Imageutils imageutils;

    public Fragment_Attachment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attachment, container, false);

        imageutils = new Imageutils(getActivity(), this, true);

        txt_response = (TextView) view.findViewById(R.id.textView_Response);
        btn_upload = (Button) view.findViewById(R.id.button_Upload);
        numberProgressBar = (NumberProgressBar) view.findViewById(R.id.progressBar);

        numberProgressBar.setMax(100);
        iv_attachment = (ImageView) view.findViewById(R.id.imageView);

        iv_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageutils.imagepicker(1);
            }
        });


        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("image Path: " + path + file_name);

//                uploadImage(AppUtils.URL_Upload, path + file_name);

                PopupMenu popup = new PopupMenu(getActivity(), btn_upload);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if (menuItem.getItemId() == 2131165264) {
                            numberProgressBar.setProgress(0);
                            Uplaod_Files.getInstance().uploadViaAmitSheker(AppUtils.URL_Upload, path, file_name, numberProgressBar, new ResponseCallback() {
                                @Override
                                public void success(String result) {
                                    txt_response.setText(result);
                                }
                            });
                        } else if (menuItem.getItemId() == 2131165318) {
                            numberProgressBar.setProgress(0);
                            Uplaod_Files.getInstance().uploadViaBridge(AppUtils.URL_Upload, path, file_name, numberProgressBar, new ResponseCallback() {
                                @Override
                                public void success(String result) {
                                    txt_response.setText(result);
                                }
                            });
                        } else if (menuItem.getItemId() == 2131165311) {
                            numberProgressBar.setProgress(0);
                            Uplaod_Files.getInstance().uploadViaVolleyPlus(AppUtils.URL_Upload, path, file_name, numberProgressBar, getActivity() ,new ResponseCallback() {
                                @Override
                                public void success(String result) {
                                    txt_response.setText(result);
                                }
                            });
                        } else if (menuItem.getItemId() == 2131165238) {
                            numberProgressBar.setProgress(0);
                            Uplaod_Files.getInstance().uploadViaRetrofit(AppUtils.URL_UploadRetrofit, path, file_name, numberProgressBar, getActivity() ,new ResponseCallback() {
                                @Override
                                public void success(String result) {
                                    txt_response.setText(result);
                                }
                            });
                        } else {

                        }
                        return false;
                    }
                });

                popup.show();//showing popup menu

            }
        });
        return view;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("Fragment", "onRequestPermissionsResult: " + requestCode);
        imageutils.request_permission_result(requestCode, permissions, grantResults);
    }

    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        Bitmap bitmap = file;
        this.file_name = filename;
        iv_attachment.setImageBitmap(file);

        path = Environment.getExternalStorageDirectory() + File.separator + "ImageAttach" + File.separator;
        imageutils.createImage(file, filename, path, false);

        numberProgressBar.setProgress(0);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Fragment", "onActivityResult: ");
        imageutils.onActivityResult(requestCode, resultCode, data);

    }
}
