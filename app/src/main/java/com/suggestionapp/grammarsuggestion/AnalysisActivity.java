package com.suggestionapp.grammarsuggestion;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class AnalysisActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        ListView listView= (ListView) findViewById(R.id.list);

        DatabaseManager db = new DatabaseManager(this);
        List<Object[]> list = db.getCounters(15);
        db.close();

        int max=0;
        for(Object []o:list)
        {
            int s=(int)o[1];

            if(max<s) max=s;
        }

        final int MAX=max;

        ArrayAdapter<Object[]> a=new ArrayAdapter<Object[]>(this,R.layout.analysis_item)
        {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
            {
                View v=View.inflate(AnalysisActivity.this,R.layout.analysis_item,null);

                TextView tv= (TextView) v.findViewById(R.id.textView);
                ProgressBar pb= (ProgressBar) v.findViewById(R.id.progressBar);
                ProgressBar pb2= (ProgressBar) v.findViewById(R.id.progressBar2);
                TextView tv1= (TextView) v.findViewById(R.id.suggestions);
                TextView tv2= (TextView) v.findViewById(R.id.accepted);

                Object []data=getItem(position);

                tv.setText((String)data[0]);

                pb.setMax(MAX);
                pb.setProgress((int)data[1]);

                pb2.setMax(MAX);
                pb2.setProgress((int)data[2]);

                tv1.setText("Suggested : "+String.valueOf(data[1]));
                tv2.setText("Accepted : "+String.valueOf(data[2]));

                return v;
            }

            @Override
            public long getItemId(int position)
            {
                return position;
            }

            @Override
            public boolean hasStableIds()
            {
                return true;
            }
        };

        for(Object []o:list)
        {
            a.add(o);
        }

        listView.setAdapter(a);

    }

    public void onOK(View v)
    {
        finish();
    }
}
