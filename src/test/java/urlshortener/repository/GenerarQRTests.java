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

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length() + 1;
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    @Before
    public void setup() {
        qr =  new QRRepositoryImpl();
    }

    @Test
    public void generarQRTest() throws WriterException, IOException {
        byte[] codigo = qr.getQR("hola", 150, 150);
        String str1 = "[B@1e67b872";
        byte[] b = str1.getBytes("UTF-8");
        assertEquals(codigo, b);
    }

    @Test(expected = QRNotGeneratedException.class)
    public void generarQRTestNull() throws WriterException, IOException {
        byte[] codigo = qr.getQR(null, 150, 150);
    }
}
