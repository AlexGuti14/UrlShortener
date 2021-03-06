package urlshortener.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import urlshortener.exceptions.*; 

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler  {


   
   /** 
    * @param QRNotGeneratedException
    * @return ResponseEntity<Object>
    */
   @ExceptionHandler(value = QRNotGeneratedException.class)
   public ResponseEntity<Object> exception(QRNotGeneratedException ex) {
      return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
   }

   
   /** 
    * @param ConectionRefusedException
    * @return ResponseEntity<Object>
    */
   @ExceptionHandler(value = ConectionRefusedException.class)
   public ResponseEntity<Object> exception(ConectionRefusedException ex) {
      return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
   }

}