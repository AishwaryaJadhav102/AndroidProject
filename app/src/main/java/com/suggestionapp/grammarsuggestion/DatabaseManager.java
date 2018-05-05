package com.suggestionapp.grammarsuggestion;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class DatabaseManager extends SQLiteOpenHelper
{
    private static final int VERSION=1;
    private static final SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

    public DatabaseManager(Context context)
    {
        super(context, "db", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table counters (cid integer primary key AUTOINCREMENT, date varchar(20), suggestions int, accepted int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("drop table counters;");

        onCreate(db);
    }




    public void addEntry(int suggestions, int accepted)
    {
        SQLiteDatabase db=getWritableDatabase();
        String datestr=sdf.format(new Date());
        Cursor c=db.rawQuery("select * from counters where date='"+datestr+"'",null);

        if(c.moveToNext())
        {
            int s=c.getInt(2);
            int a=c.getInt(3);
            db.execSQL("update counters set suggestions="+(s+suggestions)+", accepted="+(a+accepted)+" where date='"+datestr+"'");
        }
        else
        {
            db.execSQL("insert into counters values (null,'"+datestr+"',"+suggestions+","+accepted+")");
        }

        c.close();

        db.close();
    }

    public List<Object[]> getCounters(int days)
    {
        SQLiteDatabase db=getReadableDatabase();
        Cursor c=db.rawQuery("select * from counters order by date desc",null);

        List<Object []> list=new ArrayList<>();

        while(c.moveToNext())
        {
            list.add(new Object[]{c.getString(1),c.getInt(2),c.getInt(3)});
        }

        c.close();
        db.close();

        System.out.println(list.size()+" entries found");

        for(Object []o:list)
        {
            System.out.println(Arrays.toString(o));
        }

        return list;
    }
}