package com.suggestionapp.grammarsuggestion;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SuggestionActivity extends Activity
{

    ResultReceiver callback;
    String text;
    private ListView listview;
    ArrayAdapter<Object[]> adapter;
    ArrayAdapter<Object[]> adapter1;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);

        callback = getIntent().getParcelableExtra("ResultReceiver");
        text = getIntent().getStringExtra("text");

        adapter = new ArrayAdapter<Object[]>(this, R.layout.suggestion_list_item)
        {

            List<View> viewlist=new ArrayList<>();

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

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
            {
                if(position<viewlist.size() && viewlist.get(position)!=null)
                {
                    return viewlist.get(position);
                }

                View view=View.inflate(SuggestionActivity.this,R.layout.suggestion_list_item,null);

                TextView tv1= (TextView) view.findViewById(R.id.text_view1);
                TextView tv2= (TextView) view.findViewById(R.id.text_view2);
//                TextView tv3= (TextView) view.findViewById(R.id.text_view3);


                Object []data=getItem(position);
                ArrayAdapter<String> adapter1=new ArrayAdapter<>(SuggestionActivity.this, android.R.layout.simple_spinner_dropdown_item);

                Spinner spinner=(Spinner)view.findViewById(R.id.planets_spinner);

                spinner.setAdapter(adapter1);

                tv1.setText((String) data[0]);
                tv2.setText((String) data[1]);
//                tv3.setText((String) data[2]);


                String[] datum = (String[]) data[3];
                for(String r:datum)
                {
                    adapter1.add(r);
                }

                while(viewlist.size()<=position) viewlist.add(null);


                viewlist.set(position,view);
                return view;
            }
        };


        listview=(ListView)findViewById(R.id.listView1);

        listview.setAdapter(adapter);
        //listview.setItemsCanFocus(false);
        // we want multiple clicks
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        new GrammerTask(this).execute(text);
    }

    private static class GrammerTask extends AsyncTask<String, Void, Boolean>
    {
        private final SuggestionActivity context;
        List<Object[]> results;

        public GrammerTask(SuggestionActivity context)
        {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(String... strings)
        {
            String text = strings[0];
            try
            {
                results = LangUtil.getResults(text);
                return true;
            } catch (Exception e)
            {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean)
        {
            //populate list box
//            context.listview.setAdapter(context.adapter);
            context.adapter.clear();
            for(Object []r:results)
            {
                context.adapter.add(r);
            }
        }
    }

    public void onOK(View v)
    {

        //collect changes


        Bundle bundle = new Bundle();
        bundle.putString("text", "hello world");
        callback.send(1, bundle);
        finish();
    }
}
