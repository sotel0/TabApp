package com.sweettreat.ar.tabsscroller;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private DBHandler db;
    public static final int REQUEST_CODE = 1;

    private String PRINTTAG = "TESTMainActivity"; //for debug printing


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get or create database
        db = new DBHandler(this);

        //initial population of list view
        populateListView();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , REQUEST_CODE);//one can be replaced with any action code
            }
        });

    }

    private void populateListView(){
    //set onClicklistener once imageButton is created, to delete the image

        //retrieve images from database
        List<Bitmap> myList = db.getImgEntries();

       //adapter object
        CustomBaseAdapter myCursorAdapter;

        //custom adapter to move data into views
        myCursorAdapter = new CustomBaseAdapter(
                this,
                R.layout.list_tabs,
                myList);

        //get ListView by ID
        ListView listImg = (ListView) findViewById(R.id.listViewMain);

        //set adapter to list
        listImg.setAdapter(myCursorAdapter);
    }

@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case REQUEST_CODE:

                if(resultCode == RESULT_OK){

                    //get Image as Uri
                    Uri selectedImage = imageReturnedIntent.getData();

                    Bitmap bitmap1 ;
                    try {
                        //convert Uri to Bitmap
                        bitmap1 = android.provider.MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);


                        //add image to database
                        db.addEntry(bitmap1);


                        //////db.deleteTable();

                        //update the ListView of images
                        populateListView();

                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }



    //save and remove files by name
    private Bitmap loadImageFromStorage(String path)
    {
        Bitmap b = null;
        try {
            File f=new File(path, "profile.jpg");
            b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return b;
    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

}
