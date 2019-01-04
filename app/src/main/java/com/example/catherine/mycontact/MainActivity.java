package com.example.catherine.mycontact;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.catherine.mycontact.db.MyContactsHelper;
import com.example.catherine.mycontact.db.dbservice;

import java.util.ArrayList;
import java.util.HashMap;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {
    private SimpleAdapter mAdapter;
    private ListView listContact;
    private dbservice serv;
    private PopupWindow pop = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listContact = (ListView) findViewById(R.id.contactList);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,NewContact.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.app_bar_search){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("请输入要查找的联系人");    //设置对话框标题
            final EditText edit = new EditText(this);
            builder.setView(edit);
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    serv=new dbservice(MainActivity.this);
                    ArrayList<HashMap<String, Object>> data =  serv.selectByName(edit.getText().toString());
                    adapt(data);
                }
            });
            builder.setCancelable(true);    //设置按钮是否可以按返回键取消,false则不可以取消
            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(true); //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
            dialog.show();
        }
        else if(id == R.id.action_showgroup){
            pop = new PopupWindow(MainActivity.this);
            View view = getLayoutInflater().inflate(R.layout.pop_layoutgroup, null);//加载布局

            //设置PopupWindow 一些参数
            pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            pop.setBackgroundDrawable(new BitmapDrawable());
            pop.setFocusable(true);
            pop.setOutsideTouchable(true);
            pop.setContentView(view);
            pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);

            final Button bt1 = (Button) view.findViewById(R.id.relatives);
            final Button bt2 = (Button) view.findViewById(R.id.friends);
            final Button bt3 = (Button) view.findViewById(R.id.colleages);
            final Button bt4 = (Button) view.findViewById(R.id.classmates);
            final Button bt5 = (Button) view.findViewById(R.id.allgroup);

            bt1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    serv=new dbservice(MainActivity.this);
                    ArrayList<HashMap<String, Object>> data =  serv.selectBygroup(bt1.getText().toString());
                    adapt(data);
                    pop.dismiss();
                }
            });
            bt2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ArrayList<HashMap<String, Object>> data =  serv.selectBygroup(bt2.getText().toString());
                    adapt(data);
                    pop.dismiss();
                }
            });
            bt3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ArrayList<HashMap<String, Object>> data =  serv.selectBygroup(bt3.getText().toString());
                    adapt(data);
                    pop.dismiss();
                }
            });
            bt4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ArrayList<HashMap<String, Object>> data =  serv.selectBygroup(bt4.getText().toString());
                    adapt(data);
                    pop.dismiss();
                }
            });
            bt5.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ArrayList<HashMap<String, Object>> data =  serv.selectall();
                    adapt(data);
                    pop.dismiss();
                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart(){
        super.onStart();
        if (getContext()!=null){

            serv=new dbservice(MainActivity.this);
            ArrayList<HashMap<String, Object>> data =  serv.selectall();
            adapt(data);
            //条目点击事件
            listContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView ttext=view.findViewById(R.id.showPhone);
                    Intent intent = new Intent(MainActivity.this,updateContact.class);
                    intent.putExtra("data_trans",ttext.getText().toString());
                    startActivity(intent);
                }
            });
        }
    }

    protected void adapt(ArrayList<HashMap<String, Object>> data){
        ArrayList<HashMap<String, Object>> showdata=new ArrayList<HashMap<String, Object>>();
        for(HashMap<String, Object> titem :data){
            HashMap<String, Object> temp = new HashMap<String, Object>();
            temp.put("phone", titem.get("fphoneNum"));
            temp.put("name", titem.get("fname"));
            temp.put("picture", titem.get("fpic"));
            showdata.add(temp);
        }
        //创建SimpleAdapter适配器将数据绑定到item显示控件上
        mAdapter = new SimpleAdapter(MainActivity.this, showdata, R.layout.item,
                new String[]{ "phone","name","picture"}, new int[]{ R.id.showPhone,R.id.showName,R.id.iv_pic});
        //实现列表的显示
        listContact.setAdapter(mAdapter);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

}
