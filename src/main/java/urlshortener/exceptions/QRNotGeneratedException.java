package urlshortener.exceptions;


public class QRNotGeneratedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public QRNotGeneratedException(String exception) {
        super(exception);
    }
 }