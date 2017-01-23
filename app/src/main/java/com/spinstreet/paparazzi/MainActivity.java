package com.spinstreet.paparazzi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 2;
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        GridView gv = (GridView) findViewById(R.id.image_grid);
        String[] images = {
                "http://i.imgur.com/FSDDkMR.jpg",
                "http://i.imgur.com/qQh26DE.jpg",
                "http://i.imgur.com/MZ3srpv.jpg",
                "http://i.imgur.com/fRcsH1Z.jpg",
                "http://i.imgur.com/iaDR8xq.jpg",
                "http://i.imgur.com/rQizzfi.jpg",
                "http://i.imgur.com/pfdso0Y.jpg"
        };
        gv.setAdapter(new ImageAdapter(this, images));
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        //respond to menu item selection

        switch (item.getItemId()) {
            case R.id.set_profile:
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.spinstreet.paparazzi.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView mImageView = (ImageView) findViewById(R.id.profile_image);
            mImageView.setImageBitmap(imageBitmap);
        }
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {


            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap a = BitmapFactory.decodeFile(mCurrentPhotoPath, options);
            Bitmap b = Bitmap.createScaledBitmap(a, a.getWidth() / 3, a.getHeight() / 3, false);

            FileOutputStream out;
            final File resized;
            try {
                resized = createImagResizedeFile();
                out = new FileOutputStream(resized);
                b.compress(Bitmap.CompressFormat.JPEG, 100, out);
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Ion.with(this)
                        .load(Session.url("photos/"))
                        .setLogging("LOGS", Log.ERROR)
                        .setHeader("Authorization", "JWT " + Session.jwt)
                        .setMultipartFile("image", resized)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                if (e != null) {
                                    e.printStackTrace();
                                    return;
                                }
                                if (result != null) {
                                    Log.e("Blah", result.toString());
                                    JsonArray json = result.get("data").getAsJsonObject().get("data").getAsJsonArray();
                                    StringBuilder sb = new StringBuilder();
                                    try {
                                        for (int i = 0; i < json.size(); i++) {
                                            JsonObject obj = json.get(i).getAsJsonObject().get("faceAttributes").getAsJsonObject();
                                            if (i > 0) sb.append("\n");
                                            sb.append("Face ").append(i).append(":\n");
                                            sb.append("Gender :").append(obj.get("gender").getAsString()).append("\n");
                                            sb.append("Age :").append(obj.get("age").getAsDouble()).append("\n");
                                            sb.append("Smile :").append(obj.get("Smile").getAsDouble()).append("\n");
                                            sb.append("Glasses :").append(obj.get("glasses").getAsString()).append("\n");
                                            sb.append("Gender ").append(obj.get("gender").getAsString()).append("\n");
                                            sb.append("Moustache ").append(obj.get("facialHair").getAsJsonObject().get("moustache").getAsString()).append("\n");
                                            sb.append("Beard ").append(obj.get("facialHair").getAsJsonObject().get("beard").getAsString()).append("\n");
                                            sb.append("Sideburns ").append(obj.get("facialHair").getAsJsonObject().get("sideburns").getAsString()).append("\n");
                                        }
                                        Helpers.makeDialog(getApplicationContext(), android.R.drawable.ic_menu_help, "Faces Detected", sb.toString(), null);
                                    }catch (Exception ignored){}
                                }
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                "JPEG_profile",  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private File createImagResizedeFile() throws IOException {
        // Create an image file name
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        return File.createTempFile(
                "JPEG_profile_resized",  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


}