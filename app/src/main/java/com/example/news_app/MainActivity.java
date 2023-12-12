package com.example.news_app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText searchEditText;
    NewsApi news = new NewsApi(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEditText = findViewById(R.id.searchEditText);
    }

    public void onSearchButtonClick(View view) {
        String enteredWord = searchEditText.getText().toString().trim();

        if (!enteredWord.isEmpty()) {
            // Cancel the previous task if it is running
            if (news != null && news.getStatus() == AsyncTask.Status.RUNNING) {
                news.cancel(true);
            }

            // start the task of executing the newsAPI
           news = new NewsApi(MainActivity.this);
            news.execute(enteredWord);
        } else {
            // Display an error message if the search box is empty
            Toast.makeText(this, "Please enter a Topic", Toast.LENGTH_SHORT).show();
        }
    }

    private class NewsApi extends AsyncTask<String, Void, List<Article>> {
        private Context context;

        // Constructor that takes a Context parameter
        public NewsApi(Context context) {
            this.context = context;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected List<Article> doInBackground(String... params) {
            String enteredWord = params[0];
            String apiKey = "710119f4520a4c25b4ab12e46322e7db";
            //todays date
             LocalDate date = LocalDate.now();

            try {
                // Construct the URL for the News API query
                URL url = new URL("https://newsapi.org/v2/everything?qInTitle="+ enteredWord + "&from"+date+"&to"+date+"&language=en"+ "&apiKey=" +apiKey);
                Log.d("<---URL---->", ": " + url);
                // Create an HTTP connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("User-Agent", "PostmanRuntime/7.35.0");

                try {

                   int  responseCode =  urlConnection.getResponseCode();

                   if(responseCode==HttpURLConnection.HTTP_OK) {
                       // Read the response
                       BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                       StringBuilder stringBuilder = new StringBuilder();
                       String line;

                       while ((line = bufferedReader.readLine()) != null) {
                           stringBuilder.append(line).append("\n");
                       }

                       // Parse the JSON response
                       return parseJsonResponse(stringBuilder.toString());
                   }else{
                       // Handle other response codes, log, or throw an exception
                       Log.e("NewsApiTask", "HTTP response code: " + responseCode);
                       return null;
                   }
                } finally {
                    // Disconnect the HTTP connection
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                Log.e("NewsApiTask", "Error making HTTP request", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Article> articles) {
            if (articles != null && !articles.isEmpty()) {
                // Start NewsActivity with the list of articles
                Intent intent = new Intent(context, NewsList.class);
                intent.putParcelableArrayListExtra("articles", new ArrayList<Article>(articles));
                context.startActivity(intent);
            } else {
                // Display an error message if the response is empty or invalid
                Toast.makeText(MainActivity.this, "Error retrieving news articles or no new articles found", Toast.LENGTH_SHORT).show();
            }
        }

        private List<Article> parseJsonResponse(String jsonResponse) {
            List<Article> articles = new ArrayList<>();

            try {
                Log.d("NewsApiTask", "JSON Response: " + jsonResponse);

                JSONObject jsonObject = new JSONObject(jsonResponse);

                // Check if the response contains the "articles" array
                if (jsonObject.has("articles")) {
                    JSONArray articlesArray = jsonObject.getJSONArray("articles");

                    // Iterate through the articles and extract titles
                    for (int i = 0; i < articlesArray.length(); i++) {
                        JSONObject articleJson = articlesArray.getJSONObject(i);
                        String title = articleJson.optString("title");
                        String description = articleJson.optString("description");
                        String url = articleJson.optString("url");

                        Article article = new Article(title, description, url);
                        articles.add(article);
                    }
                }
            } catch (JSONException e) {
                Log.e("NewsApiTask", "Error parsing JSON response", e);
            }

            return articles;
        }
    }
}
