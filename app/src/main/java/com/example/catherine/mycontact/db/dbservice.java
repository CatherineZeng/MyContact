package com.example.catherine.mycontact.db;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.HashMap;

//查询方法是自己写的，其他都是用现成的方法
public class dbservice extends Service {
    private MyContactsHelper dbHelper;

    public dbservice(Context context) {
        dbHelper=new MyContactsHelper(context,"Mycontacts.db",null,1);
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * 查询contact所有内容
     * @return ArrayList list 查询结果信息
     */
    public HashMap<String, Object> selectByPhone(String Phone){
        HashMap<String, Object> map = new HashMap<String, Object>();
        SQLiteDatabase sdb = dbHelper.getReadableDatabase();

        String sql="select * from mycontacts where fphoneNum = '" + Phone + "'";
        Cursor cursor = sdb.rawQuery(sql, null);

        while(cursor.moveToNext()){
            map.put("fname",cursor.getString(0));
            map.put("fphoneNum",cursor.getString(1));
            map.put("femail",cursor.getString(2));
            map.put("fcompany",cursor.getString(3));
            map.put("fgroup",cursor.getString(4));
            map.put("fpic",cursor.getBlob(cursor.getColumnIndexOrThrow("fpic")));
            break;
        }
        return map;
    }

    public ArrayList selectBygroup(String group){
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        SQLiteDatabase sdb = dbHelper.getReadableDatabase();

        String sql="select * from mycontacts where fgroup = '" + group + "'";
        Cursor cursor = sdb.rawQuery(sql, null);

        while(cursor.moveToNext()){
            HashMap<String, Object> map= new HashMap<String, Object>();
            map.put("fname",cursor.getString(0));
            map.put("fphoneNum",cursor.getString(1));
            map.put("femail",cursor.getString(2));
            map.put("fcompany",cursor.getString(3));
            map.put("fgroup",cursor.getString(4));
            map.put("fpic",cursor.getBlob(cursor.getColumnIndexOrThrow("fpic")));
            list.add(map);
        }
        return list;
    }

    public ArrayList selectByName(String name){
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        SQLiteDatabase sdb = dbHelper.getReadableDatabase();

        String sql="select * from mycontacts where fname = '" + name + "'";
        Cursor cursor = sdb.rawQuery(sql, null);

        while(cursor.moveToNext()){
            HashMap<String, Object> map= new HashMap<String, Object>();
            map.put("fname",cursor.getString(0));
            map.put("fphoneNum",cursor.getString(1));
            map.put("femail",cursor.getString(2));
            map.put("fcompany",cursor.getString(3));
            map.put("fgroup",cursor.getString(4));
            map.put("fpic",cursor.getBlob(cursor.getColumnIndexOrThrow("fpic")));
            list.add(map);
        }
        return list;
    }

    public ArrayList selectall(){
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        SQLiteDatabase sdb = dbHelper.getReadableDatabase();

        String sql="select * from mycontacts";
        Cursor cursor = sdb.rawQuery(sql, null);

        while(cursor.moveToNext()){
            HashMap<String, Object> map= new HashMap<String, Object>();
            map.put("fname",cursor.getString(0));
            map.put("fphoneNum",cursor.getString(1));
            map.put("femail",cursor.getString(2));
            map.put("fcompany",cursor.getString(3));
            map.put("fgroup",cursor.getString(4));
            map.put("fpic",cursor.getBlob(cursor.getColumnIndexOrThrow("fpic")));
            list.add(map);
        }
        return list;
    }
//
//    public ArrayList deleteContact(String phone){
//        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
//        SQLiteDatabase sdb = dbHelper.getReadableDatabase();
//
//        String sql="delete * from mycontacts where fphoneNum = "+phone;
//        Cursor cursor = sdb.rawQuery(sql, null);
//
//        while(cursor.moveToNext()){
//            HashMap<String, Object> map= new HashMap<String, Object>();
//            map.put("fname",cursor.getString(0));
//            map.put("fphoneNum",cursor.getString(1));
//            map.put("femail",cursor.getString(2));
//            map.put("fcompany",cursor.getString(3));
//            map.put("fgroup",cursor.getString(4));
//            map.put("fpic",cursor.getBlob(5));
//            list.add(map);
//        }
//        return list;
//    }
//
//    public ArrayList updateContact(HashMap<String, Object> tmap, String prePhone){
//        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
//        SQLiteDatabase sdb = dbHelper.getReadableDatabase();
//
//        String sql="update mycontacts set fname = '"+ tmap.get("fname").toString()
//                +" ',fphoneNum = '"+tmap.get("fphoneNum").toString()
//                +"',femail = '"+ tmap.get("femail").toString()
//                +"', fcompany = '"+ tmap.get("fcompany").toString()
//                +"',fgroup = '"+tmap.get("fgroup").toString()
//                +"', fpic = '"+tmap.get("fpic").toString()
//                +"' where fphoneNum = '"+prePhone+"'";
//        Cursor cursor = sdb.rawQuery(sql, null);
//
//        while(cursor.moveToNext()){
//            HashMap<String, Object> map= new HashMap<String, Object>();
//            map.put("fname",cursor.getString(0));
//            map.put("fphoneNum",cursor.getString(1));
//            map.put("femail",cursor.getString(2));
//            map.put("fcompany",cursor.getString(3));
//            map.put("fgroup",cursor.getString(4));
//            map.put("fpic",cursor.getBlob(5));
//            list.add(map);
//        }
//        return list;
//    }

}
