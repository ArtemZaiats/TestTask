package com.zaiats.testtask.api;

import com.zaiats.testtask.pojo.Response;
import com.zaiats.testtask.pojo.Answer;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET ("trending")
    Observable<List<Response>> getRecords();

    @GET("object/{pageId}")
    Observable<Answer> getAnswers(@Path("pageId") int id);
}
