package com.example.hemantkatariya.imageuploading.via_activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
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

public class Activity_ImageAttachment extends AppCompatActivity implements Imageutils.ImageAttachmentListener {


    ImageView iv_attachment;
    Button btn_upload;
    NumberProgressBar numberProgressBar;
    TextView txt_response;

    //image path
    String path = "";
    //For Image Attachment

    private Bitmap bitmap;
    private String file_name;

    Imageutils imageutils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Upload via Activity");
        setContentView(R.layout.activity_image_attachment);

        imageutils = new Imageutils(this);

        txt_response = (TextView) findViewById(R.id.textView_Response);
        btn_upload = (Button) findViewById(R.id.button_Upload);
        iv_attachment = (ImageView) findViewById(R.id.imageView);
        numberProgressBar = (NumberProgressBar) findViewById(R.id.progressBar);

        numberProgressBar.setMax(100);

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

                PopupMenu popup = new PopupMenu(Activity_ImageAttachment.this, btn_upload);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        System.out.println("menu id: " +menuItem.getItemId());
                        if (menuItem.getItemId() == 2131165266) {
                            numberProgressBar.setProgress(0);
                            Uplaod_Files.getInstance().uploadViaAmitSheker(AppUtils.URL_Upload, path, file_name, numberProgressBar, new ResponseCallback() {
                                @Override
                                public void success(String result) {
                                    txt_response.setText(result);
                                }
                            });
                        } else if (menuItem.getItemId() == 2131165320) {
                            numberProgressBar.setProgress(0);
                            Uplaod_Files.getInstance().uploadViaBridge(AppUtils.URL_Upload, path, file_name, numberProgressBar, new ResponseCallback() {
                                @Override
                                public void success(String result) {
                                    txt_response.setText(result);
                                }
                            });
                        } else if (menuItem.getItemId() == 2131165313) {
                            numberProgressBar.setProgress(0);
                            Uplaod_Files.getInstance().uploadViaVolleyPlus(AppUtils.URL_Upload, path, file_name, numberProgressBar, getApplicationContext() ,new ResponseCallback() {
                                @Override
                                public void success(String result) {
                                    txt_response.setText(result);
                                }
                            });
                        } else if (menuItem.getItemId() == 2131165238) {
                            numberProgressBar.setProgress(0);
                            Uplaod_Files.getInstance().uploadViaRetrofit(AppUtils.URL_UploadRetrofit, path, file_name, numberProgressBar, getApplicationContext() ,new ResponseCallback() {
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
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageutils.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        imageutils.request_permission_result(requestCode, permissions, grantResults);
    }

    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        this.bitmap = file;
        this.file_name = filename;
        iv_attachment.setImageBitmap(file);

        path = Environment.getExternalStorageDirectory() + File.separator + "ImageAttach" + File.separator;
        imageutils.createImage(file, filename, path, false);

        numberProgressBar.setProgress(0);
        txt_response.setText(null);
    }

}
