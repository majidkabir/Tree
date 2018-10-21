package com.majid.tree.services;

import com.majid.tree.util.Error;

/**
 * This class for wraps the response object in another object for getting the specific errors in client side
 *
 * Created by Majid Ghaffuri on 10/20/2018.
 */
public class ServiceResponse<T> {
    private Error error = Error.SUCCESS;
    private T responseObject;

    public ServiceResponse(){}

    public ServiceResponse(T responseObject){
        this.responseObject = responseObject;
    }

    public T getResponseObject() {
        return responseObject;
    }

    public int getErrorCode() {
        return error.getCode();
    }

    public String getErrorMessage() {
        return error.getDescription();
    }

    public ServiceResponse setError(Error error){
        this.error = error;
        return this;
    }

    public static ServiceResponse Error(Error error) {
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.error = error;
        return serviceResponse;
    }
}
