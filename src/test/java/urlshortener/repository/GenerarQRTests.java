package urlshortener.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import urlshortener.exceptions.QRNotGeneratedException;
import urlshortener.repository.impl.QRRepositoryImpl;
import urlshortener.service.GenerarQRService;

import java.io.IOException;
import java.util.List;

import com.google.zxing.WriterException;

import static org.junit.Assert.*;

public class GenerarQRTests {

    private QRRepository qr;

    @Before
    public void setup() {
        qr =  new QRRepositoryImpl();
    }

    @Test(expected = QRNotGeneratedException.class)
    public void generarQRTestNull() throws WriterException, IOException {
        byte[] codigo = qr.getQR(null, 150, 150);
    }
}
