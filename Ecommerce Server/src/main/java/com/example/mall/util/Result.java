package com.example.mall.util;

//import io.swagger.annotations.ApiModelProperty;

public class Result<T> {

//    @ApiModelProperty("返回码")
    private int resultCode;

//    @ApiModelProperty("返回信息")
    private String message;

//    @ApiModelProperty("返回数据")
    private T data;

    public Result(int resultCode, String message, T data) {
        this.resultCode = resultCode;
        this.message = message;
        this.data = data;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "resultCode=" + resultCode +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
