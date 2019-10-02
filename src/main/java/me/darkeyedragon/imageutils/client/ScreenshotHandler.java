package me.darkeyedragon.imageutils.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ScreenShotHelper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class ScreenshotHandler {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
    private static final LinkedHashMap<String, BufferedImage> downloadList = new LinkedHashMap<String, BufferedImage>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, BufferedImage> eldest) {
            return size() > 4;
        }
    };


    /*Take a full screenshot of the current game window*/
    public static BufferedImage full() {
        return ScreenShotHelper.createScreenshot(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, Minecraft.getMinecraft().getFramebuffer());
    }

    /*Take a partial screenshot of the current game window*/
    public static BufferedImage partial(Point first, Point second) {
        BufferedImage screenshot = ScreenShotHelper.createScreenshot(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, Minecraft.getMinecraft().getFramebuffer());
        System.out.println("Values: X:" + (first.x + 3) + " Y:" + (first.y + 3) + " W:" + (second.x - first.x - 3) + " H:" + (second.y - first.y - 3));
        int height = Math.abs(second.y - first.y - 3);
        int width = Math.abs(second.x - first.x - 3);
        return screenshot.getSubimage(first.x + 3, first.y + 3, width, height);
        //upload(alteredScreenshot);
    }

    public static File getTimestampedPNGFileForDirectory(File gameDirectory) {
        String s = DATE_FORMAT.format(new Date());
        int i = 1;

        while (true) {
            File file1 = new File(gameDirectory, s + (i == 1 ? "" : "_" + i) + ".png");

            if (!file1.exists()) {
                return file1;
            }
            ++i;
        }
    }

    public static LinkedHashMap<String, BufferedImage> getDownloadList() {
        return downloadList;
    }
}
