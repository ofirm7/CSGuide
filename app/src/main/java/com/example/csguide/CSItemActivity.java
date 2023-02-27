package com.example.csguide;

import static android.os.Environment.DIRECTORY_DCIM;
import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static android.os.Environment.DIRECTORY_PICTURES;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.csguide.databinding.ActivityCsitemBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class CSItemActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPref sharedPref;
    TextView nameOfCSItemTv, csItemDetailsTv;
    AlertDialog.Builder builder;
    Button editDetails, removeDetails, submitDetails;
    EditText csItemDetailsEt;

    ImageButton downloadImageIB;
    ImageView afterDownloadIV;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    StorageReference storageReference2;

    Button changeOrAddImage, deleteImage;;
    TextView noImageTv;

    ActivityCsitemBinding binding;
    Handler handler = new Handler();

    String url = null;
    public  Uri imageUri;
    static ImageView csItemImageExample;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        super.onCreate(savedInstanceState);
        binding = ActivityCsitemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActivityCompat.requestPermissions(CSItemActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(CSItemActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        nameOfCSItemTv = binding.nameOfCSItemTv;
        nameOfCSItemTv.setText(DataModel.csItems.get(getIntent().getIntExtra("CSII", 0)).getName());

        csItemDetailsTv = binding.csItemDetailsTv;
        csItemDetailsEt = binding.csItemDetailsEt;
        csItemDetailsEt.setVisibility(View.GONE);

        csItemDetailsTv.setText(DataModel.csItems.get(getIntent().getIntExtra("CSII", 0)).getDescription());
        csItemDetailsEt.setText(csItemDetailsTv.getText());

        submitDetails = binding.submitDetails;
        submitDetails.setVisibility(View.GONE);

        editDetails = binding.editDetails;
        removeDetails = binding.removeDetails;

        if (!sharedPref.IsAdmin()) {
            removeDetails.setVisibility(View.GONE);
            editDetails.setVisibility(View.GONE);
        } else {
            removeDetails.setOnClickListener(this);
            editDetails.setOnClickListener(this);
        }

        submitDetails.setOnClickListener(this);

        noImageTv = binding.noImageTv;
        noImageTv.setVisibility(View.GONE);

        deleteImage = binding.deleteImage;

        //Image

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        csItemImageExample = binding.csItemImageExample;
        changeOrAddImage = binding.addImage;

        url = DataModel.csItems.get(getIntent().getIntExtra("CSII", 0)).getUrl();

        changeOrAddImage.setVisibility(View.GONE);
        if (url == null) {
            csItemImageExample.setVisibility(View.GONE);
            if (sharedPref.IsAdmin())
            {
                changeOrAddImage.setVisibility(View.VISIBLE);
            }
            noImageTv.setVisibility(View.VISIBLE);
            deleteImage.setVisibility(View.GONE);
        } else {
            if (sharedPref.IsAdmin()) {
                changeOrAddImage.setVisibility(View.VISIBLE);
                changeOrAddImage.setText(" Change Image");
            } else {
                deleteImage.setVisibility(View.GONE);
            }

            new FetchImage(url).start();

            /*
            imageUri = Uri.parse(url);
            csItemImageExample.setImageURI(imageUri);
            */
        }

        //download image (142 - 150)
        downloadImageIB = binding.downloadImage;
        downloadImageIB.setOnClickListener(this);
        if (url == null)
        {
            downloadImageIB.setVisibility(View.GONE);
        }

        afterDownloadIV = binding.afterDownloadImage;
        afterDownloadIV.setVisibility(View.GONE);

        //

        submitDetails.setOnClickListener(this);
        changeOrAddImage.setOnClickListener(this);
        deleteImage.setOnClickListener(this);

        builder = new AlertDialog.Builder(this);
    }

    @Override
    public void onClick(View view) {
        if (view == editDetails) {
            csItemDetailsEt.setVisibility(View.VISIBLE);
            csItemDetailsTv.setVisibility(View.GONE);

            submitDetails.setVisibility(View.VISIBLE);
            editDetails.setVisibility(View.GONE);
        } else if (view == submitDetails) {
            DataModel.csItems.get(getIntent().getIntExtra("CSII", 0)).setDescription(csItemDetailsEt.getText().toString());
            DataModel.csItemsSave();

            restartapp();
        } else if (view == removeDetails) {
            DataModel.csItems.get(getIntent().getIntExtra("CSII", 0)).setDescription("");
            DataModel.csItemsSave();

            restartapp();
        } else if (view == changeOrAddImage) {
            // Code for showing progressDialog while uploading
            progressDialog = new ProgressDialog(CSItemActivity.this);
            chooseVideo();
        } else if(view == deleteImage)
        {
            builder.setMessage("Do you want to delete this image?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();

                            DataModel.csItems.get(getIntent().getIntExtra("CSII", 0)).setUrl(null);
                            DataModel.csItemsSave();

                            Toast.makeText(getApplicationContext(), "You deleted the image",
                                    Toast.LENGTH_SHORT).show();
                            restartapp();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            dialog.cancel();
                            Toast.makeText(getApplicationContext(), "You canceled the delete",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setTitle("Delete Image");
            alert.show();
        }
        else if (view == downloadImageIB)
        {
            downloadImageFunc();
        }
    }

    public void downloadImageFunc()
    {

        storageReference = firebaseStorage.getInstance().getReference("Files");
        String imageName = DataModel.csItems.get(getIntent().getIntExtra("CSII", 0)).getUrl().split("%2F")[1].split("alt")[0];
        imageName = imageName.substring(0, imageName.length()-1);
        storageReference2 = storageReference.child(imageName);

        String finalImageName = imageName;
        storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                String name = finalImageName.substring(0,finalImageName.length()-4);
                downloadImageInternalFunc(CSItemActivity.this , name, ".jpg", DIRECTORY_DOWNLOADS, url);
                downloadImageIB.setVisibility(View.GONE);
                afterDownloadIV.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Error",e.getMessage());
            }
        });
    }

    public void downloadImageInternalFunc(Context context, String fileName,
                                          String fileExtension, String destinationDirectory,
                                          String url)
    {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri =Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        downloadManager.enqueue(request);
    }

    private void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    // startActivityForResult is used to receive the result, which is the selected image.
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            uploadImage();
        }
    }

    private void uploadImage() {
        if (imageUri != null) {
            // save the selected video in Firebase storage
            final StorageReference reference = FirebaseStorage.getInstance().getReference("Files/" + System.currentTimeMillis() + "." + getfiletype(imageUri));
            reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful()) ;
                    // get the link of video
                    String downloadUri = uriTask.getResult().toString();
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Image");
                    HashMap<String, String> map = new HashMap<>();
                    map.put("imagelink", downloadUri);
                    reference1.child("" + System.currentTimeMillis()).setValue(map);
                    // Image uploaded successfully
                    // Dismiss dialog
                    progressDialog.dismiss();
                    Toast.makeText(CSItemActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                    DataModel.csItems.get(getIntent().getIntExtra("CSII", 0)).setUrl(downloadUri);
                    DataModel.csItemsSave();

                    restartapp();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Error, Image not uploaded
                    progressDialog.dismiss();
                    Toast.makeText(CSItemActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                // Progress Listener for loading
                // percentage on the dialog box
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    // show the progress bar
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                }
            });
        }
    }

    private String getfiletype(Uri imageuri) {
        ContentResolver r = getContentResolver();
        // get the file type ,in this case its mp4
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(r.getType(imageuri));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);

        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        MenuItem item;

        if (sharedPref.GetUsername().equals("YouRGuest")) {
            item = menu.getItem(0);
            item.setEnabled(false);
            item.setVisible(false);

            item = menu.getItem(3);
            item.setEnabled(false);
            item.setVisible(false);


        } else {

            item = menu.getItem(1);
            item.setEnabled(false);
            item.setVisible(false);

            item = menu.getItem(2);
            item.setEnabled(false);
            item.setVisible(false);

            item = menu.getItem(0);
            item.setTitle(sharedPref.GetUsername());
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_login) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, 0);
            //Toast.makeText(this,"you selected login",Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_register) {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivityForResult(intent, 0);
            return true;
        } else if (id == R.id.action_exit) {
            builder.setMessage("Do you want to logout?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            sharedPref.SetUsername("YouRGuest", false);
                            Toast.makeText(getApplicationContext(), "You logged out",
                                    Toast.LENGTH_SHORT).show();
                            restartapp();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            dialog.cancel();
                            Toast.makeText(getApplicationContext(), "You canceled the logout",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setTitle("Logout");
            alert.show();
        } else if (id == R.id.action_GoHome) {
            finish();
            return true;
        }
        return true;
    }

    void restartapp() {
        Intent i = getIntent();
        startActivity(i);
        finish();
    }

    class FetchImage extends Thread
    {
        String URL;
        Bitmap bitmap;

        public FetchImage(String fetchUrl)
        {
            this.URL = fetchUrl;
        }

        @Override
        public void run()
        {
            InputStream inputStream = null;
            try {
                inputStream = new java.net.URL(this.URL).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    binding.csItemImageExample.setImageBitmap(bitmap);
                }
            });
        }
    }

}



