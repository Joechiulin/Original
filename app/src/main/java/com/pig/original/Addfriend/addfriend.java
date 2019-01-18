package com.pig.original.Addfriend;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pig.original.Common.Common;
import com.pig.original.Common.CommonTask;
import com.pig.original.R;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.app.Activity.RESULT_OK;
import static android.support.constraint.Constraints.TAG;

public class addfriend extends Fragment {
    private FragmentActivity comactivity;
    private FragmentManager fragmentManager;
    private static final int REQ_PICK_IMAGE = 1;
    private static final int REQ_TAKE_IMAGE = 2;
    Bitmap pictureimage;
    private ImageView Pickpicture;
    private TextView textView;
    private byte[] image;


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
                String name = textView.getText().toString().trim();
                if (name.length() <= 0) {
                    Common.showToast(getActivity(), R.string.msg_NameIsInvalid);
                    return;
                }

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
                if (image == null) {
                    Common.showToast(getActivity(), R.string.msg_NoImage);
                    return;
                }

                if (Common.networkConnected(comactivity)) {
                    String url = Common.URL + "/SpotServlet";
                    Friend spot = new Friend(0, name, logintime, lastloginRegion, latitude, longitude);
                    String imageBase64 = Base64.encodeToString(image, Base64.DEFAULT);
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "spotInsert");
                    jsonObject.addProperty("spot", new Gson().toJson(spot));
                    jsonObject.addProperty("imageBase64", imageBase64);
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

//        ivSpot = rootView.findViewById(R.id.ivSpot);
        textView = view.findViewById(R.id.tvaddcommunity);
        Pickpicture = view.findViewById(R.id.ivAddcommunity);
//         btpost = view.findViewById(R.id.communityfriendAdd);
//        btCancel = rootView.findViewById(R.id.btCancel);
//        etName = rootView.findViewById(R.id.etName);
//        etPhone = rootView.findViewById(R.id.etPhone);
//        etAddress = rootView.findViewById(R.id.etAddress);
    }
//    private BitmapFactory.Options getBitmapOption(int inSampleSize){
//        System.gc();
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPurgeable = true;
//        options.inSampleSize = inSampleSize;
//        return options;
//    }
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

                case REQ_TAKE_IMAGE:
//                    Bitmap srcPicture = BitmapFactory.decodeFile(file.getPath());
//                    Bitmap downsizedPicture = Common.downSize(srcPicture, newSize);
//                    imageView.setImageBitmap(downsizedPicture);
//                    pictureimage = downsizedPicture;
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
                    }
                    break;
            }
        }

    }


}




