package com.suggestionapp.grammarsuggestion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class LangUtil
{
    static public List<Object[]> parseResult(String str) throws JSONException
    {
        JSONObject jo = new JSONObject(str);

        List<Object[]> list = new ArrayList<>();


        JSONArray matches = jo.getJSONArray("matches");


        for (int i = 0; i < matches.length(); i++)
        {

            JSONObject o = matches.getJSONObject(i);

            String message = o.getString("message");

            JSONObject context = o.getJSONObject("context");

            String text = context.getString("text");
            int offset = context.getInt("offset");
            int length = context.getInt("length");

            JSONArray replacements = o.getJSONArray("replacements");
            String[] stringArray = new String[replacements.length()];
            String[] replArray = new String[replacements.length()];
            for (int j = 0; j < replacements.length(); j++)
            {
                JSONObject o2 = replacements.getJSONObject(j);
                String value = o2.getString("value");

                String replacedText = new StringBuilder(text).replace(offset, offset + length, value).toString();
                stringArray[j]=replacedText;
                replArray[j]=value;
                //  list.add(new Object[]{text,message,});
            }


            /*for (int k = 0; k < replacements.length(); k++)
            {
                try
                {
                    String jsonString = replacements.getString(k);
                    stringArray[k] = jsonString;
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }*/
            list.add(new Object[]{text, message, replArray, stringArray,offset,length});
//            list.add(new Object[]{me});
        }


        return list;
    }

    static public List<Object[]> getResults(String text) throws Exception
    {
//        String url = "https://languagetool.org/api/v2/check?text=" + URLEncoder.encode(text, "UTF-8") + "&language=en&enabledOnly=false";
        String url = "http://192.168.225.150:8081/v2/check?text=" + URLEncoder.encode(text, "UTF-8") + "&language=en&enabledOnly=false";
        byte[] b = get(url);
        String str = new String(b, "UTF-8");
        return parseResult(str);
    }

    static public byte[] get(String url) throws Exception
    {

        //url = "https://languagetool.org/api/v2/check?text=She%20were%20reading%20a%20novel.&language=en&enabledOnly=false";
        URL u = new URL(url);
        HttpURLConnection hcon = (HttpURLConnection) u.openConnection();
        InputStream is = hcon.getInputStream();
        // String readStream = readStream(hcon.getInputStream());

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[20000];
        while ((nRead = is.read(data, 0, data.length)) != -1)
        {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
        // System.out.println(is);

    }
}