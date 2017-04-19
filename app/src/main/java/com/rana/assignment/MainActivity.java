package com.rana.assignment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.rana.assignment.models.RowItem;
import com.rana.assignment.utility.Utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final int READ_REQUEST = 11;
    private static final int PICKFILE_REQUEST_CODE = 14;
    private static final String TAG = "MainActivity";
    @BindView(R.id.b_askforfile_mainactivity) Button b_askforfile_mainactivity;
    @BindView(R.id.rv_list_mainactivity) RecyclerView rv_list_mainactivity;

    public static String getPath(final Context context, final Uri uri) {
        if (DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.b_askforfile_mainactivity)
    void onClickAskForFileButton() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("text/*");
            startActivityForResult(intent, PICKFILE_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICKFILE_REQUEST_CODE:
//                String pathString = getPath(this, data.getData());
                String pathString = data.getData().toString();
                if (pathString != null) {
                    File file = null;
                    try {
                        file = new File(new URI(pathString));
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    HashMap<String, Integer> integerHashMap = openFileAndReadContent(file);
                    integerHashMap = Utility.sortHashMapByValue(integerHashMap);
                    ArrayList<RowItem> arrayList = Utility.transformHashMapIntoArraylistForPerformanceAndSimplity(integerHashMap);
                    AdapterWordsAndCount adapterWordsAndCount = new AdapterWordsAndCount(this, arrayList);
                    rv_list_mainactivity.setAdapter(adapterWordsAndCount);
                    rv_list_mainactivity.setLayoutManager(new LinearLayoutManager(this));
                } else {
                    Toast.makeText(this, "Error : Invalid File Path", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private HashMap<String, Integer> openFileAndReadContent(File file) {
        BufferedReader br = null;
        HashMap<String, Integer> hashMap = new HashMap<>();
        try {
            FileInputStream inputStream = new FileInputStream(file);

            int c;
            StringBuilder stringBuilder = new StringBuilder();
            while ((c = inputStream.read()) != -1) {
                if (c == ' ' || c == '\n') {
                    String tempString = stringBuilder.toString();
                    pushToHashMap(tempString, hashMap);
                    stringBuilder.delete(0, stringBuilder.length());
                } else {
                    Log.e(TAG, String.valueOf((char) c) + "");
                    stringBuilder.append((char) c);
                }
            }
            pushToHashMap(stringBuilder.toString(), hashMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hashMap;
    }

    private void pushToHashMap(String tempString, HashMap<String, Integer> hashMap) {
        Integer integer = hashMap.get(tempString);
        if (integer != null) {
            hashMap.put(tempString, integer + 1);
        } else {
            hashMap.put(tempString, 1);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_REQUEST:
                onClickAskForFileButton();
                break;
        }
    }
}
