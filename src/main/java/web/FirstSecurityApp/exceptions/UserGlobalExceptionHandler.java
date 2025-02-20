package web.FirstSecurityApp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserGlobalExceptionHandler extends RuntimeException {
    public UserGlobalExceptionHandler() {

    }

    @ExceptionHandler
    private ResponseEntity<UserIncorrectData> handleException(NoSuchElementException e) {
        UserIncorrectData data = new UserIncorrectData();
        data.setInfo(e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }
}
