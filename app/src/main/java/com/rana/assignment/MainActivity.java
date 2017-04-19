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
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final int READ_REQUEST = 11;
    private static final int PICKFILE_REQUEST_CODE = 14;
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

    public static <K, V extends Comparable<? super V>> HashMap<K, V> sortHashMapByValue(HashMap<K, V> map) {
        List<HashMap.Entry<K, V>> list =
                new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<HashMap.Entry<K, V>>() {
            @Override
            public int compare(HashMap.Entry<K, V> o1, HashMap.Entry<K, V> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        HashMap<K, V> result = new LinkedHashMap<>();
        for (HashMap.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
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
                String pathString = getPath(this, data.getData());
                if (pathString != null) {
                    File file = new File(pathString);
                    HashMap<String, Integer> integerHashMap = openFileAndReadContent(file);
                    integerHashMap = sortHashMapByValue(integerHashMap);
                    
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
                    stringBuilder.append(c);
                } else {
                    String tempString = stringBuilder.toString();
                    Integer integer = hashMap.get(tempString);
                    if (integer != null) {
                        hashMap.put(tempString, integer + 1);
                    } else {
                        hashMap.put(tempString, 1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hashMap;
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
