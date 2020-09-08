package com.taotao.common.httpclient;

public class HttpResult {
    private int code;
    private String Content;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public HttpResult(int code, String content) {
        this.code = code;
        Content = content;
    }
}
