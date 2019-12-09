package urlshortener.repository;

public interface QRRepository {
	byte[] getQR(String text, int width, int height);

}
