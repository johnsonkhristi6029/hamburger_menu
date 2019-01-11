package com.example.velmurugan.navigationdrawerexample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

/**
 * Created by velmmuru on 1/6/2018.
 */

public class ProfileFragment extends Fragment {

    EditText editfname, editlastname, edituni, editstu_id;
    ImageView imgprofile;
    Button btnupdate;
    static  String firstname , lastname , univesity , studentid ;
    public static SharedPreferences.Editor editor ;
   public static SharedPreferences pref;
    private final int requestCode = 20;
    private  Bitmap bitmap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        // Control decalration
        return view ;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editfname = (EditText)view.findViewById(R.id.editfirstname);
        editlastname = (EditText)view.findViewById(R.id.editlastname);
        editstu_id = (EditText)view.findViewById(R.id.editstudentid);
        edituni = (EditText)view.findViewById(R.id.edituni);
        imgprofile =(ImageView)view.findViewById(R.id.imgprofile);
        btnupdate=(Button)view.findViewById(R.id.btnupdate);

         pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode




        if(pref.contains("Firstname"))
        {
            editfname.setText(pref.getString("Firstname",""));
        }
        if(pref.contains("Lastname"))
        {
            editlastname.setText(pref.getString("Lastname",""));
        }
        if(pref.contains("University"))
        {
            edituni.setText(pref.getString("University",""));
        }
        if(pref.contains("StudentId"))
        {
            editstu_id.setText(pref.getString("StudentId",""));
        }

        if(pref.contains("imagePreferance"))
        {
            retrivesharedPreferences();
        }
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                editor = pref.edit();
                firstname = editfname.getText().toString();
                lastname = editlastname.getText().toString();

                univesity = edituni.getText().toString();

                studentid = editstu_id.getText().toString();
                editor.putString("Firstname",firstname);
                editor.putString("Lastname", lastname);
                editor.putString("University", univesity);
                editor.putString("StudentId", studentid);

                imgprofile.buildDrawingCache();
                Bitmap bmap = imgprofile.getDrawingCache();

                editor.putString("imagePreferance", encodeTobase64(bmap));

                editor.commit();


            }
        });

        imgprofile.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoCaptureIntent, requestCode);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(this.requestCode == requestCode && resultCode == RESULT_OK){
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            imgprofile.setImageBitmap(bitmap);
        }
    }
    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }
    private void retrivesharedPreferences()
    {
        String photo = pref.getString("imagePreferance", "photo");
        assert photo != null;
        if(!photo.equals("photo"))
        {
            byte[] b = Base64.decode(photo, Base64.DEFAULT);
            InputStream is = new ByteArrayInputStream(b);
            bitmap = BitmapFactory.decodeStream(is);
            imgprofile.setImageBitmap(bitmap);
        }

    }
}

