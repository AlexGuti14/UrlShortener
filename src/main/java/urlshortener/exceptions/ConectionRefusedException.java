package urlshortener.exceptions;


public class ConectionRefusedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ConectionRefusedException(String exception) {
        super(exception);
    }
 }