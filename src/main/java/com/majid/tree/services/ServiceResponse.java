package com.majid.tree.services;

import com.majid.tree.util.ErrorHelper;

import java.util.Objects;
import java.util.logging.ErrorManager;

/**
 * This class for wrapping the response object in another object for getting the specific errors in client side
 *
 * Created by Majid Ghaffuri on 10/20/2018.
 */
public class ServiceResponse<T> {
    private int errorCode;
    private String errorMessage;
    private T responseObject;

    public ServiceResponse(){}

    public ServiceResponse(T responseObject){
        this.responseObject = responseObject;
    }

    public T getResponseObject() {
        return responseObject;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public ServiceResponse setErrorCode(int errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = ErrorHelper.getMessage(errorCode);

        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
