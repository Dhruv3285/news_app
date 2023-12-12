package com.example.news_app;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;

public class NewsTile extends ArrayAdapter<Article> {

    private TextToSpeech textToSpeech;

    public NewsTile(Context context, List<Article> articles) {
        super(context, 0, articles);

        // Initialize TextToSpeech
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
            }
        });
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Article article = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_tile, parent, false);
        }

        // Lookup view for data population
        TextView titleTextView = convertView.findViewById(R.id.titleTextView);

        // lookup view for the 2 buttons
        Button button1 = convertView.findViewById(R.id.button1);
        Button button2 = convertView.findViewById(R.id.button2);

        // Populate the data into the template view using the data object
        titleTextView.setText(article.getTitle());

        // Set click listeners for buttons
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // due to the API not sending "description" in the response I will be using the article title to be read out in the audio.
                textToSpeech.speak(article.getTitle(), TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("url", article.getUrl());
                getContext().startActivity(intent);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }


}


