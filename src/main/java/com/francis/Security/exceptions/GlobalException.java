package com.francis.Security.exceptions;

import com.francis.Security.models.errors.ErrorModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalException {

    @ExceptionHandler(UsernameAlreadyExistException.class)
    public ResponseEntity<ErrorModel> UsernameAlreadyExist(final UsernameAlreadyExistException exception){
        ErrorModel errorResponse = new ErrorModel();
                errorResponse.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
                errorResponse.setMessage("Username or email already exist");
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorModel> RoleNotFoundException(final RoleNotFoundException exception){
        ErrorModel errorResponse = new ErrorModel();
        errorResponse.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
        errorResponse.setMessage("Role not found with name USER");
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ErrorModel> CommentNotFoundException(final CommentNotFoundException exception){
        ErrorModel errorResponse = new ErrorModel();
        errorResponse.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
        errorResponse.setMessage("Comment not found with commentId ");
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordMissMatchException.class)
    public ResponseEntity<ErrorModel> PasswordMissMatch(final PasswordMissMatchException exception){
        ErrorModel errorResponse = new ErrorModel();
        errorResponse.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
        errorResponse.setMessage("password mismatch");
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ErrorModel> PostNotFoundException(final PostNotFoundException exception){
        ErrorModel errorResponse = new ErrorModel();
        errorResponse.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
        errorResponse.setMessage("Post NOt Found with this postId ");
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<ErrorModel> UnauthorizedUserException(final UnauthorizedUserException exception){
        ErrorModel errorResponse = new ErrorModel();
        errorResponse.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
        errorResponse.setMessage("You are not authorized to access this post.");
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectCredentialsException.class)
    public ResponseEntity<ErrorModel> HandleIncorrectCredentialsException(final IncorrectCredentialsException exception){
        ErrorModel errorResponse = new ErrorModel();
        errorResponse.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
        errorResponse.setMessage("Incorrect username or password");
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorModel>> handleFieldValidation(MethodArgumentNotValidException manv){

        List<ErrorModel> errorModelList = new ArrayList<>();
        ErrorModel errorModel = null;
        List<FieldError> fieldErrorList = manv.getBindingResult().getFieldErrors();

        for(FieldError fe: fieldErrorList){
            log.debug("Inside field validation: {} - {}", fe.getField(), fe.getDefaultMessage());
            log.info("Inside field validation: {} - {}", fe.getField(), fe.getDefaultMessage());
            errorModel = new ErrorModel();
            errorModel.setCode(fe.getField());
            errorModel.setMessage(fe.getDefaultMessage());
            errorModelList.add(errorModel);
        }

        return new ResponseEntity<>(errorModelList, HttpStatus.BAD_REQUEST);


    }
}
