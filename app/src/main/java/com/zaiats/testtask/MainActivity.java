package com.zaiats.testtask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.zaiats.testtask.network.JsonAnswer;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements ResponseView {

    private  ArrayList<Integer> responseList = new ArrayList<>();
    private int countOfResponse = 0;

    JsonAnswer answer = new JsonAnswer(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            countOfResponse = savedInstanceState.getInt("counter");
            responseList = savedInstanceState.getIntegerArrayList("list");
            answer.getAnswers(responseList.get(countOfResponse));
        } else {
            answer.getResponse();
            answer.getAnswers(1);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("counter", countOfResponse);
        ArrayList<Integer> integers = new ArrayList<>();
        integers.addAll(responseList);
        outState.putIntegerArrayList("list", integers);
    }

    public void onClickNext(View view) {
        countOfResponse++;
        if (countOfResponse < responseList.size()) {
            answer.getAnswers(responseList.get(countOfResponse));
        } else {
            answer.getAnswers(responseList.get(0));
            countOfResponse = 0;
        }
    }

    @Override
    protected void onDestroy() {
    answer.disposeDisposable();
        super.onDestroy();
    }


    @Override
    public void getTextView(String text) {
        WebView webView = findViewById(R.id.webView);
        webView.setVisibility(View.GONE);
        TextView textViewContent = findViewById(R.id.textViewContent);
        textViewContent.setText(text);
        textViewContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void getWebView(String url) {
        TextView textViewContent = findViewById(R.id.textViewContent);
        textViewContent.setVisibility(View.GONE);
        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setVisibility(View.VISIBLE);
    }

    @Override
    public void getResponseList(ArrayList<Integer> list) {
        responseList.addAll(list);
    }

}

