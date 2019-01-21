package com.pig.original.Community;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pig.original.Addfriend.Friend;
import com.pig.original.Common.Common;
import com.pig.original.Common.CommonTask;
import com.pig.original.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import static android.app.Activity.RESULT_OK;
import static android.support.constraint.Constraints.TAG;

public class CommunityAdd extends Fragment {
    private FragmentActivity comactivity;
    private FragmentManager fragmentManager;
    private static final int REQ_PICK_IMAGE = 1;
    private static final int REQ_CROP_PICTURE = 2;
    Bitmap pictureimage;
    private ImageView Pickpicture;
    private TextView textView;
    private byte[] picture;
    private Uri contentUri, croppedImageUri;



    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        comactivity = getActivity();
        fragmentManager = getFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.addcommunity, container, false);
        findViews(view);
//        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
//        Common.askPermissions(this.getActivity(), permissions, Common.REQ_EXTERNAL_STORAGE);
//        askPermissions();
        Pickpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_PICK_IMAGE);
            }
        });
        FloatingActionButton btpost = view.findViewById(R.id.addcommunity);

        btpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textView.getText().toString().trim();
                if (text.length() <= 0) {
                    Common.showToast(getActivity(), R.string.msg_NameIsInvalid);
                    return;
                }
                String name = "joe";
                String lastloginRegion = "122";
                String logintime = "111";
//                List<Address> addressList;
                double latitude = 0.0;
                double longitude = 0.0;
//                try {
//                    addressList = new Geocoder(getActivity()).getFromLocationName(address, 1);
//                    latitude = addressList.get(0).getLatitude();
//                    longitude = addressList.get(0).getLongitude();
//                } catch (IOException e) {
//                    Log.e(TAG, e.toString());
//                }
                if (picture == null) {
                    Common.showToast(getActivity(), R.string.msg_NoImage);
                    return;
                }

                if (Common.networkConnected(comactivity)) {
                    String url = Common.URL + "/SpotServlet";
                    CommunityPost spot = new CommunityPost(0,name, logintime, lastloginRegion, latitude, longitude,text);
                    String picture64 = Base64.encodeToString(picture, Base64.DEFAULT);
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "spotInsert");
                    jsonObject.addProperty("spot", new Gson().toJson(spot));
                    jsonObject.addProperty("picture64", picture64);
                    int count = 0;
                    try {
                        String result = new CommonTask(url, jsonObject.toString()).execute().get();
                        count = Integer.valueOf(result);
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    if (count == 0) {
                        Common.showToast(getActivity(), R.string.msg_InsertFail);
                    } else {
                        Common.showToast(getActivity(), R.string.msg_InsertSuccess);
                    }
                } else {
                    Common.showToast(getActivity(), R.string.msg_NoNetwork);
                }
                /* 回前一個Fragment */
                fragmentManager.popBackStack();
            }
        });
        return view;
    }

    private void askPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        Set<String> permissionsRequest = new HashSet<>();
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(this.getContext(), permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionsRequest.add(permission);
            }
        }
    }

    private void findViews(View view) {

        textView = view.findViewById(R.id.tvaddcommunity);
        Pickpicture = view.findViewById(R.id.ivAddcommunity);
    }
    public void onStart() {
        super.onStart();
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Common.askPermissions(this.getActivity(), permissions, Common.REQ_EXTERNAL_STORAGE);
        askPermissions();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            int newSize = 512;
            switch (requestCode) {

                case REQ_CROP_PICTURE:
                    Log.d(TAG, "REQ_CROP_PICTURE: " + croppedImageUri.toString());
                    try {
                        Bitmap pictures = BitmapFactory.decodeStream(
                                comactivity.getContentResolver().openInputStream(croppedImageUri));
                        Pickpicture.setImageBitmap(pictures);
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        pictures.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        picture = out.toByteArray();
                    } catch (FileNotFoundException e) {
                        Log.e(TAG, e.toString());
                    }
                    break;
                case REQ_PICK_IMAGE:

                    Uri uri = intent.getData();

                    if (uri != null) {
                        String[] columns = {MediaStore.Images.Media.DATA};
                        Cursor cursor = comactivity.getApplicationContext().getContentResolver().query(uri, columns,
                                null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            String imagePath = cursor.getString(0);
                            cursor.close();
                            Bitmap srcImage = BitmapFactory.decodeFile(imagePath);
//                            Bitmap srcImage = BitmapFactory.decodeFile(imagePath);

//                            Bitmap downsizedImage = Common.downSize(srcImage, newSize);
                            Pickpicture.setImageBitmap(srcImage);
//                            pictureimage = downsizedImage;

                        }
                        crop(uri);
                    }
                    break;
            }
        }

    }
    private void crop(Uri sourceImageUri) {
        File file = comactivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        file = new File(file, "picture_cropped.jpg");
        croppedImageUri = Uri.fromFile(file);
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // the recipient of this Intent can read soruceImageUri's data
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // set image source Uri and type
            cropIntent.setDataAndType(sourceImageUri, "image/*");
            // send crop message
            cropIntent.putExtra("crop", "true");
            // aspect ratio of the cropped area, 0 means user define
            cropIntent.putExtra("aspectX", 0); // this sets the max width
            cropIntent.putExtra("aspectY", 0); // this sets the max height
            // output with and height, 0 keeps original size
            cropIntent.putExtra("outputX", 0);
            cropIntent.putExtra("outputY", 0);
            // whether keep original aspect ratio
            cropIntent.putExtra("scale", true);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, croppedImageUri);
            // whether return data by the intent
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, REQ_CROP_PICTURE);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Common.showToast(getActivity(), "This device doesn't support the crop action!");
        }
    }

}







