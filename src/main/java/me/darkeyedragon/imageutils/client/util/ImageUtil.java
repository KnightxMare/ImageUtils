package me.darkeyedragon.imageutils.client.util;

import me.darkeyedragon.imageutils.client.ImageUtilsMain;
import me.darkeyedragon.imageutils.client.ScreenshotHandler;
import me.darkeyedragon.imageutils.client.image.ImageType;
import me.darkeyedragon.imageutils.client.message.ClientMessage;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class ImageUtil {


    /**
     * @param img  the image to resize
     * @param newW the new width
     * @param newH the new height
     * @return the resized image
     */
    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        try {
            return Thumbnails.of(img).size(newW, newH).asBufferedImage();
        } catch (IOException e) {
            ClientMessage.basic("Unable to resize image for preview!");
            return null;
        }
    }


    /**
     * Checks if an url contains a valid image by checking the first 8 bytes of the content body. They represent an {@link ImageType}
     *
     * @param imageUrl the url of the image
     * @return returns true if the first bytes represent a image type
     */
    public static boolean isValidImage(String imageUrl) {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(imageUrl);
        httpGet.addHeader(HttpHeaders.USER_AGENT, ImageUtilsMain.NAME + "/" + ImageUtilsMain.VERSION);
        httpGet.addHeader(HttpHeaders.RANGE, "bytes=0-7");
        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity.getContentLength() > 8) return false;
            int[] bytes = new int[8];
            for (int i = 0; i < 8; i++) {
                bytes[i] = (short) httpEntity.getContent().read();
            }
            for (int i = 0; i < ImageType.values().length; i++) {
                if (ImageType.values()[i].compare(bytes)) return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param url the url to download the image from
     * @return the downloaded image
     * @throws IOException thrown when no connection could be made to the url
     */
    public static BufferedImage downloadFromUrl(String url) throws IOException {

        if (!StringFilter.isValidUrl(url) || !isValidImage(url)) {
            return null;
        }
        URL imgUrl = new URL(url);
        if (isValidImage(url)) {
            URLConnection conn;
            conn = imgUrl.openConnection();
            conn.setRequestProperty(HttpHeaders.USER_AGENT, ImageUtilsMain.NAME + "/" + ImageUtilsMain.VERSION);
            InputStream image = conn.getInputStream();
            BufferedImage img = ImageIO.read(image);
            Map<String, BufferedImage> downloads = ScreenshotHandler.getDownloadList();
            if (!downloads.containsKey(imgUrl.toString())) {
                downloads.put(imgUrl.toString(), img);
                return img;

            } else {
                return downloads.get(imgUrl.toString());
            }
        }
        return null;
    }

    public void getImageSize(String url) {

    }

    public static BufferedImage getLocal(File location) throws IOException {
        return ImageIO.read(location);
    }
}
