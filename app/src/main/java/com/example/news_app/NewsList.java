package com.example.news_app;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class NewsList extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout for this activity
        setContentView(R.layout.news_articles);

        // Retrieve the list of articles from the Intent that started this activity.
        List<Article> articles = getIntent().getParcelableArrayListExtra("articles");

        // Create an news tile to display the list of articles in a ListView
        NewsTile newsTile = new NewsTile(this, articles);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(newsTile);

        // back arrow that can be used to go back to the parent page.
        ImageView backArrowImageView = findViewById(R.id.backArrowImageView);
        backArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  go back to the previous activity
                finish();
            }
        });
    }

}

