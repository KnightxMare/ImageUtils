package com.darkeyedragon.imageutils.client.gui;

import com.darkeyedragon.imageutils.client.ScreenshotHandler;
import com.darkeyedragon.imageutils.client.utils.RegionSelector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GuiPartialScreenshot extends GuiScreen{

    private final int res = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
    private boolean clicked = false;
    private boolean dragging = false;
    private Point mouseClicked;
    private Point mouseReleased;
    private RegionSelector regionSelector = new RegionSelector();

    @Override
    public void drawScreen (int mouseX, int mouseY, float partialTicks){
        if (dragging){
            regionSelector.drawRegion(Math.min(mouseClicked.x, mouseX), Math.min(mouseClicked.y, mouseY), Math.max(mouseClicked.x, mouseX), Math.max(mouseClicked.y, mouseY));

            int x = Math.abs(mouseClicked.x - mouseX);
            int y = Math.abs(mouseClicked.y - mouseY);
            drawHoveringText(x + "x" + y, mouseClicked.x, mouseClicked.y);
        }else{
            drawHoveringText("Select an area", mouseX, mouseY);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame (){
        return super.doesGuiPauseGame();
    }

    @Override
    public void initGui (){

        super.initGui();
    }

    @Override
    protected void actionPerformed (GuiButton button){

    }

    @Override
    protected void mouseClicked (int mouseX, int mouseY, int mouseButton){
        if (mouseButton == 0){
            clicked = true;
            dragging = true;
            mouseClicked = new Point(mouseX, mouseY);
        }
    }

    @Override
    protected void mouseReleased (int mouseX, int mouseY, int state){
        mouseReleased = new Point(mouseX, mouseY);
        dragging = false;
        mouseClicked.y *= res;
        mouseClicked.x *= res;
        mouseReleased.y *= res;
        mouseReleased.x *= res;

        BufferedImage image = ScreenshotHandler.partial(new Point(Math.min(mouseClicked.x, mouseReleased.x), Math.min(mouseClicked.y, mouseReleased.y)), new Point(Math.max(mouseClicked.x, mouseReleased.x), Math.max(mouseClicked.y, mouseReleased.y)));
        ScreenshotHandler.upload(image);
        mc.displayGuiScreen(null);
    }
}