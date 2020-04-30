package com.zaiats.testtask;

import java.util.ArrayList;

public interface ResponseView {

    void getTextView(String text);
    void getWebView(String url);
    void getResponseList(ArrayList<Integer> list);

}
