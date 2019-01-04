package com.example.catherine.mycontact;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.catherine.mycontact.db.MyContactsHelper;
import com.example.catherine.mycontact.db.dbservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class updateContact extends AppCompatActivity {

    private dbservice serv;
    private List<CharSequence> groupList = null;
    private ArrayAdapter<CharSequence> groupAdapter = null;
    private Spinner groupSpinner= null;
    private static String[] selectitem = {"家人","朋友","同事","同学"};
    private ImageView iv;
    private String phoneNum;
    private MyContactsHelper updbHeelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);
        Intent intent = getIntent();
        iv = findViewById(R.id.iv_pic2);
        phoneNum = intent.getStringExtra("data_trans");
        serv=new dbservice(this);
        updbHeelper= new MyContactsHelper(this,"Mycontacts.db",null,1);

        //填充控件内容
        HashMap<String, Object> data =  serv.selectByPhone(phoneNum);
        TextView tempText = (TextView)this.findViewById(R.id.editName);
        tempText.setText(data.get("fname").toString());
        tempText = (TextView)this.findViewById(R.id.editPhone);
        tempText.setText(data.get("fphoneNum").toString());
        tempText = (TextView)this.findViewById(R.id.editEmail);
        tempText.setText(data.get("femail").toString());
        tempText = (TextView)this.findViewById(R.id.editCompany);
        tempText.setText(data.get("fcompany").toString());
        //找到Spinner控件
        groupSpinner = super.findViewById(R.id.spinner);
        groupSpinner.setPrompt("请选择分组:");
        groupList = new ArrayList<CharSequence>();
        for(int i=0;i<4;i++){
            groupList.add(selectitem[i]);
        }
        groupAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, groupList);
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupSpinner.setAdapter(groupAdapter);
        int k= groupAdapter.getCount();
        for(int i=0;i<k;i++) {
            if (data.get("fgroup").toString().equals(groupAdapter.getItem(i).toString())) {
                groupSpinner.setSelection(i);// 默认选中项
                break;
            }
        }
//        //暂时未实现图片功能
//        byte[]showImg = (byte[]) data.get("fpic");
//        Bitmap bmp = BitmapFactory.decodeByteArray(showImg, 0, showImg.length);
//        iv.setImageBitmap(bmp);

//        iv = (ImageView)findViewById(R.id.iv_pic);
//        iv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (ContextCompat.checkSelfPermission(updateContact.this,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(updateContact.this,
//                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//                } else {
//                    //打开系统相册
//                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//调用android的图库
//                    startActivityForResult(i, 1);
//                }
//            }
//        });

        Button modify= findViewById(R.id.button_modify);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase contact = updbHeelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                TextView tempText = findViewById(R.id.editName);
                values.put("fname",tempText.getText().toString());
                tempText = findViewById(R.id.editPhone);
                values.put("fphoneNum",tempText.getText().toString());
                tempText = findViewById(R.id.editEmail);
                values.put("femail",tempText.getText().toString());
                tempText = findViewById(R.id.editCompany);
                values.put("fcompany",tempText.getText().toString());
                values.put("fgroup",groupSpinner.getSelectedItem().toString());
                values.put("fpic",new byte[]{0});//未完成
                try {
                    contact.update("mycontacts",values,"fphoneNum='"+phoneNum+"'",null);
                    finish();
                    Toast.makeText(updateContact.this, "更新成功", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(updateContact.this, "更新失败", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Button delete= findViewById(R.id.button_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SQLiteDatabase contact = updbHeelper.getWritableDatabase();
                    contact.delete("mycontacts", "fphoneNum='" + phoneNum + "'", null);
                    finish();
                    Toast.makeText(updateContact.this, "删除成功", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(updateContact.this, "删除失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        FloatingActionButton fabcall = (FloatingActionButton) findViewById(R.id.floatingcall);
        fabcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent call = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));
                call.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(call);
            }
        });

        FloatingActionButton fabsend = (FloatingActionButton) findViewById(R.id.floatingsendmsg);
        fabsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(Uri.parse("smsto:" + phoneNum));
                startActivity(sendIntent);
            }
        });
    }
}
