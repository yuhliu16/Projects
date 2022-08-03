package com.example.mall.common;

public class ServiceException extends RuntimeException{

    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public static void fail(String message){
        throw new ServiceException(message);
    }
}
