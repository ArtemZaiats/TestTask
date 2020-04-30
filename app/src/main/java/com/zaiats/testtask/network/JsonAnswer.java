package com.zaiats.testtask.network;

import android.content.Context;
import android.widget.Toast;

import com.zaiats.testtask.ResponseView;
import com.zaiats.testtask.api.ApiFactory;
import com.zaiats.testtask.api.ApiService;
import com.zaiats.testtask.pojo.Answer;
import com.zaiats.testtask.pojo.Response;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class JsonAnswer {

    private ResponseView view;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ApiFactory apiFactory = ApiFactory.getInstance();
    private ApiService apiService = apiFactory.getApiService();

    private ArrayList<Integer> list = new ArrayList<>();

    public JsonAnswer(ResponseView view) {
        this.view = view;
    }

    public void getResponse() {
        compositeDisposable.add(apiService.getRecords()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Response>>() {
                    @Override
                    public void accept(List<Response> responses) throws Exception {
                        for (Response response : responses) {
                            list.add(response.getId());
                        }
                        view.getResponseList(list);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText((Context) view, "Unable to get data\n" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    public void getAnswers(int pageId) {
        compositeDisposable.add(apiService.getAnswers(pageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Answer>() {
                    @Override
                    public void accept(Answer answer) throws Exception {
                        if (answer.getType().equals("text")) {
                            view.getTextView(answer.getContents());
                        } else if (answer.getType().equals("webview")) {
                            view.getWebView(answer.getUrl());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText((Context) view, "Error getting type\n" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    public void disposeDisposable(){
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }

}
