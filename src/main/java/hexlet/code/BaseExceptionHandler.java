package hexlet.code;

import com.rollbar.notifier.Rollbar;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@ResponseBody
@ControllerAdvice
public class BaseExceptionHandler {
    private final Rollbar rollbar;

    public BaseExceptionHandler(Rollbar rollbar) {
        this.rollbar = rollbar;
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(UsernameNotFoundException.class)
    public String userNotFoundExceptionHandler(UsernameNotFoundException exception) {
        rollbar.error(exception);
        return exception.getMessage();
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String noSuchElementExceptionHandler(NoSuchElementException exception) {
        return exception.getMessage();
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(ConstraintViolationException.class)
    public String noValidExceptionHandler(ConstraintViolationException exception) {
        rollbar.error(exception);
        return exception.getMessage();
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public String validationExceptionsHandler(DataIntegrityViolationException exception) {
        rollbar.error(exception);
        return exception.getCause().getCause().getMessage();
    }
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ObjectError> validationExceptionsHandler(MethodArgumentNotValidException exception) {
        rollbar.error(exception);
        return exception.getAllErrors();
    }
}
