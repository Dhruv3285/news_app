package com.example.news_app;

import android.os.Parcelable;
import android.os.Parcel;

public class Article implements Parcelable {
    private String title;
    private String description;
    private String url;


    public Article(String title, String description, String url) {
        this.title = title;
        this.description = description;
        this.url = url;
    }

    // Parcelable implementation
    protected Article(Parcel in) {
        title = in.readString();
        description = in.readString();
        url = in.readString();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }
}

