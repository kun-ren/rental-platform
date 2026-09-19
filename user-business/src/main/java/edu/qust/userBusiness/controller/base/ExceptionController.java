package edu.qust.userBusiness.controller.base;

import edu.qust.common.base.Response;
import edu.qust.common.exception.ContentIllegalException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ContentIllegalException.class)
    @ResponseBody
    public Response handleError(RuntimeException e){
        return Response.fail(e.getMessage());
    }
}
