package com.zaiats.testtask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.zaiats.testtask.api.ApiFactory;
import com.zaiats.testtask.api.ApiService;
import com.zaiats.testtask.pojo.Answer;
import com.zaiats.testtask.pojo.Response;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private final LinkedList<Integer> responseList = new LinkedList<>();

    private Disposable disposable;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getResponse();
    }

    public void getResponse() {
        ApiFactory apiFactory = ApiFactory.getInstance();
        ApiService apiService = apiFactory.getApiService();
        compositeDisposable = new CompositeDisposable();
        disposable = apiService.getRecords()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Response>>() {
                    @Override
                    public void accept(List<Response> responses) throws Exception {
                        for (Response response : responses) {
                            responseList.add(response.getId());
                        }
                        getAnswers(responseList.getFirst());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MainActivity.this, "Unable to get data\n" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void getAnswers(int pageId) {
        ApiFactory apiFactory = ApiFactory.getInstance();
        ApiService apiService = apiFactory.getApiService();
        disposable = apiService.getAnswers(pageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Answer>() {
                    @Override
                    public void accept(Answer answer) throws Exception {
                        if (answer.getType().equals("text")) {
                            getTextView(answer.getContents());
                        } else if (answer.getType().equals("webview")) {
                            getWebView(answer.getUrl());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MainActivity.this, "Error getting type\n" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        super.onDestroy();
    }

    private void getTextView(String text) {
        WebView webView = findViewById(R.id.webView);
        webView.setVisibility(View.GONE);
        TextView textViewContent = findViewById(R.id.textViewContent);
        textViewContent.setText(text);
        textViewContent.setVisibility(View.VISIBLE);
    }

    private void getWebView(String url) {
        TextView textViewContent = findViewById(R.id.textViewContent);
        textViewContent.setVisibility(View.GONE);
        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setVisibility(View.VISIBLE);
    }

    public void onClickNext(View view) {
        /*
        *not done
        */
    }

}

