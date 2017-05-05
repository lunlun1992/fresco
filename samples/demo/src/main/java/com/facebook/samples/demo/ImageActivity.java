package com.facebook.samples.demo;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;


public class ImageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);
        ArrayList<String> datas = new ArrayList<>();
        for(int i = 1; i <= 50; i++)
        {
            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/kpgtest/" + Integer.toString(i) + ".kpg");
            Uri uri = Uri.fromFile(file);
            datas.add(uri.toString());
        }


        ListView listViewleft = (ListView) this.findViewById(R.id.list_left);
        MyAdapter adapter = new MyAdapter();
        listViewleft.setAdapter(adapter);
        adapter.setDatas(datas);


        ListView listViewright = (ListView) this.findViewById(R.id.list_right);
        adapter = new MyAdapter();
        listViewright.setAdapter(adapter);
        adapter.setDatas(datas);
//        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/test.kpg");
//        Uri uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/testImages/1.jpg");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/test.kpg");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/testImages/2.jpg");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/test.kpg");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/testImages/3.jpg");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/test.kpg");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/testImages/4.webp");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/test.kpg");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/testImages/5.webp");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/test.kpg");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/testImages/6.webp");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/test.kpg");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/testImages/7.webp");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/test.kpg");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/testImages/8.webp");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/test.kpg");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/testImages/9.webp");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/test.kpg");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/testImages/10.webp");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/test.kpg");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/testImages/11.webp");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/test.kpg");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/testImages/12.webp");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/test.kpg");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/testImages/13.webp");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/test.kpg");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/testImages/14.webp");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/test.kpg");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/testImages/15.webp");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/test.kpg");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/testImages/16.webp");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/test.kpg");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/testImages/17.webp");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());
//        file = new File(Environment.getExternalStorageDirectory().getPath() + "/test.kpg");
//        uri = Uri.fromFile(file);
//        datas.add(uri.toString());


    }


}