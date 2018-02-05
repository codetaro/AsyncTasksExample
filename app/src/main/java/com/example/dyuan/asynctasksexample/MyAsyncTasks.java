package com.example.dyuan.asynctasksexample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyAsyncTasks extends AsyncTask<String, String, String> {

    private Context mContext;

    TextView titleTextView, categoryTextView;
    Button displayData;
    ImageView imageView;

    String apiUrl = "http://mobileappdatabase.in/demo/smartnews/app_dashboard/jsonUrl/single-article.php?article-id=71";
    String title, image, category;
    ProgressDialog progressDialog;

    public MyAsyncTasks(Context context) {
        mContext = context;

        titleTextView = (TextView) ((Activity) context).findViewById(R.id.titleTextView);
        categoryTextView = (TextView) ((Activity) context).findViewById(R.id.categoryTextView);
        imageView = (ImageView) ((Activity) context).findViewById(R.id.imageView);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {

        String current = "";
        try {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(apiUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);

                int data = isw.read();
                while (data != -1) {
                    current += (char) data;
                    data = isw.read();
                    System.out.print(current);
                }
                return current;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception: " + e.getMessage();
        }
        return current;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d("data", s.toString());

        progressDialog.dismiss();
        try {
            JSONArray jsonArray = new JSONArray(s);
            JSONObject oneObject = jsonArray.getJSONObject(0);

            title = oneObject.getString("title");
            category = oneObject.getString("category");
            image = oneObject.getString("image");

            titleTextView.setText("Title: " + title);
            categoryTextView.setText("Catetory: " + category);

            Picasso.with(mContext)
                    .load(image)
                    .into(imageView);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
