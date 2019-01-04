package com.example.catherine.mycontact;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.catherine.mycontact.db.MyContactsHelper;
import com.example.catherine.mycontact.photoHandle.BitmapOption;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class NewContact extends AppCompatActivity {
    private List<CharSequence> groupList = null;
    private ArrayAdapter<CharSequence> groupAdapter = null;
    private Spinner groupSpinner= null;
    private MyContactsHelper dbHelper;
    EditText stringTemp;
    String tempstr;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);
        //数据库
        dbHelper = new MyContactsHelper(this, "Mycontacts.db", null, 1);
        dbHelper.getReadableDatabase();
        //找到Spinner控件
        groupSpinner = super.findViewById(R.id.spinner);
        groupSpinner.setPrompt("请选择分组:");
        groupList = new ArrayList<CharSequence>();
        groupList.add("家人");
        groupList.add("朋友");
        groupList.add("同事");
        groupList.add("同学");
        groupAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, groupList);
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupSpinner.setAdapter(groupAdapter);

        iv = (ImageView)findViewById(R.id.iv_pic);
        iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (ContextCompat.checkSelfPermission(NewContact.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(NewContact.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    //打开系统相册
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//调用android的图库
                    startActivityForResult(i, 2);
                }
            }
        });

        //数据库添加数据
        Button addData = (Button) findViewById(R.id.button_confirm);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase contact = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                //插入数据
                //姓名
                stringTemp = (EditText) findViewById(R.id.editName);
                tempstr = stringTemp.getText().toString();
                values.put("fname", tempstr);
                //手机
                stringTemp = (EditText) findViewById(R.id.editPhone);
                tempstr = stringTemp.getText().toString();
                values.put("fphoneNum", tempstr);
                //电子邮件
                stringTemp = (EditText) findViewById(R.id.editEmail);
                tempstr = stringTemp.getText().toString();
                values.put("femail", tempstr);
                //公司
                stringTemp = (EditText) findViewById(R.id.editCompany);
                tempstr = stringTemp.getText().toString();
                values.put("fcompany", tempstr);
                //群组
                tempstr = groupSpinner.getSelectedItem().toString();
                values.put("fgroup", tempstr);
                try {
                    contact.insert("mycontacts", null, values);
                    finish();
                    Toast.makeText(NewContact.this, "插入成功", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(NewContact.this, "插入失败", Toast.LENGTH_SHORT).show();
                }
                values.clear();
            }
        });


        Button cancel = (Button) findViewById(R.id.button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == NewContact.RESULT_OK) {
            if (requestCode == 1) {  // 拍照
                Bundle extras = data.getExtras();
                Bitmap photoBit = (Bitmap) extras.get("data");
                Bitmap option = BitmapOption.bitmapOption(photoBit, 5);
                iv.setImageBitmap(option);
                BitmapOption.saveBitmap2file(option, "0001.jpg");
                final File file = new File("/sdcard/contact_pic" + "0001.jpg");

                //开始联网上传的操作

            } else if (requestCode == 2) { // 相册
                try {
                    Uri uri = data.getData();
                    String[] pojo = {MediaStore.Images.Media.DATA};
                    Cursor cursor = managedQuery(uri, pojo, null, null, null);
                    if (cursor != null) {
                        ContentResolver cr = this.getContentResolver();
                        int colunm_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        String path = cursor.getString(colunm_index);
                        final File file = new File(path);
                        Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                        Bitmap option = BitmapOption.bitmapOption(bitmap, 5);
                        iv.setImageBitmap(option);//设置为头像的背景

                        //开始联网上传的操作

                    }
                } catch (Exception e) {

                }
            }
        }
    }
}
