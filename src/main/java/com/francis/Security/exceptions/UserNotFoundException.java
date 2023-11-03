package com.francis.Security.exceptions;

import com.francis.Security.models.errors.ErrorModel;

import java.util.List;

public class UserNotFoundException extends RuntimeException {
    private List<ErrorModel> errorModelList;
    public UserNotFoundException(List<ErrorModel> errorModelList) {
        this.errorModelList = errorModelList;
    }
}
