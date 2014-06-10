package com.dynious.versionchecker.client.gui;

import com.dynious.versionchecker.handler.DownloadThread;
import com.dynious.versionchecker.api.Update;
import com.dynious.versionchecker.helper.DesktopHelper;
import com.dynious.versionchecker.helper.WebHelper;
import com.dynious.versionchecker.lib.Resources;
import com.dynious.versionchecker.lib.Strings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiUpdates extends GuiScreen
{
    private GuiUpdateList updateList;
    private GuiButton updateButton;
    private GuiButton closeButton;
    private GuiButtonDownloaded buttonDownloaded;

    private Update openUpdate = null;
    private GuiChangeLog changeLogList;

    private int windowStartX, windowStartY, windowEndX, windowEndY;

    private static final int listShift = 50;

    @Override
    @SuppressWarnings("unchecked")
    public void initGui()
    {
        super.initGui();

        windowStartX = width / 2 - 110 + listShift;
        windowStartY = height / 2 - 90;
        windowEndX = width / 2 + 110 + listShift;
        windowEndY = height / 2 + 70;

        buttonList.add(new GuiButton(0, width / 2 - 75 + listShift, height - 30, 150, 20, StatCollector.translateToLocal("gui.done")));

        buttonList.add(updateButton = new GuiButton(1, width / 2 - 100 + listShift, height / 2 + 40, 96, 20, StatCollector.translateToLocal(Strings.UPDATE)));
        updateButton.visible = openUpdate != null;

        buttonList.add(closeButton = new GuiButton(2, width / 2 + 4 + listShift, height / 2 + 40, 96, 20, StatCollector.translateToLocal("gui.done")));
        closeButton.visible = openUpdate != null;

        buttonList.add(new GuiButton(3, 10, height - 30, 150, 20, StatCollector.translateToLocal(Strings.MOD_FOLDER)));

        buttonList.add(buttonDownloaded = new GuiButtonDownloaded(4, width / 2 - 100 + listShift, height / 2 + 15));

        updateList = new GuiUpdateList(this, 300, 180, 20, height - 40, width / 2 - 150 + listShift);
        changeLogList = new GuiChangeLog(this, 200, 75, height / 2 - 60, height / 2 + 15, width / 2 - 100 + listShift);

        if (openUpdate != null)
        {
            buttonDownloaded.enable(openUpdate);
            if (openUpdate.changeLog != null)
                changeLogList.setText(openUpdate.changeLog);
        }
        else
        {
            buttonDownloaded.disable();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float par3)
    {
        updateList.drawScreen(mouseX, mouseY, par3);

        if (openUpdate != null)
        {
            changeLogList.drawScreen(mouseX, mouseY, par3);
        }

        this.fontRendererObj.drawSplitString(StatCollector.translateToLocal(Strings.INFO).replace(";", "\n"), 10, height / 2 - 60, width / 2 - 150 + listShift - 20, 0xCCCCCC);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(Resources.GUI_LOGO);

        int i = width / 2 - 150 + listShift - 10;
        Gui.func_146110_a(5, 5, 0, 0, i, (int) (i * 0.4), i, (int) (i * 0.4));

        if (openUpdate != null)
        {
            drawCenteredString(fontRendererObj, openUpdate.displayName, width / 2 + listShift, height / 2 - 80, 0xFFFFFF);
            if (openUpdate.changeLog == null)
            {
                drawCenteredString(fontRendererObj, StatCollector.translateToLocal(Strings.NO_CHANGE_LOG), width / 2 + listShift, height / 2 - 60, 0xCCCCCC);
            }
        }
        if (DownloadThread.isUpdating())
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getMinecraft().renderEngine.bindTexture(Resources.GUI_ICONS);
            Gui.func_146110_a(width - 20, 4, 0, 0, 16, 16, 32, 32);
        }

        super.drawScreen(mouseX, mouseY, par3);

        drawToolTip(mouseX, mouseY);
    }

    public void drawWindow()
    {
        GL11.glColor4f(0.6F, 0.6F, 0.6F, 1.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(Resources.GUI_WINDOW);
        Gui.func_146110_a(windowStartX, windowStartY, 0, 0, 220, 160, 220, 160);
    }

    public void drawToolTip(int mouseX, int mouseY)
    {
        if (updateButton.mousePressed(mc, mouseX, mouseY))
        {
            List<String> list = new ArrayList<String>();
            String left = openUpdate.updateURL;
            while(true)
            {
                String s = fontRendererObj.trimStringToWidth(left, 200);
                list.add(s);
                if (s.length() == left.length())
                {
                    break;
                }
                else
                {
                    left = fontRendererObj.trimStringToWidth(left, 200, true);
                }
            }
            this.drawHoveringText(list, mouseX, mouseY, fontRendererObj);
        }
        else if (buttonDownloaded.mousePressed(mc, mouseX, mouseY))
        {
            this.drawHoveringText(Arrays.asList(StatCollector.translateToLocal(Strings.DL_MARKED_INFO).split(";")), mouseX, mouseY, fontRendererObj);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        super.actionPerformed(button);
        switch (button.id)
        {
            case 0:
                Minecraft.getMinecraft().displayGuiScreen(null);
                break;
            case 1:
                if (!openUpdate.isDirectLink)
                {
                    WebHelper.openWebpage(openUpdate.updateURL);
                }
                else
                {
                    DownloadThread.downloadUpdate(openUpdate);
                    closeInfoScreen();
                }
                break;
            case 2:
                closeInfoScreen();
                break;
            case 3:
                DesktopHelper.openFolderInExplorer(DesktopHelper.MOD_FOLDER);
                break;
            case 4:
                buttonDownloaded.onButtonClicked();
                break;
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int par3)
    {
        if (openUpdate == null)
        {
            super.mouseClicked(x, y, par3);
        }
        else
        {
            if (x > windowStartX && x < windowEndX && y > windowStartY && y < windowEndY)
            {
                super.mouseClicked(x, y, par3);
            }
            else
            {
                closeInfoScreen();
            }
        }
    }

    public FontRenderer getFontRenderer()
    {
        return fontRendererObj;
    }

    public void openInfoScreen(Update update)
    {
        openUpdate = update;
        updateButton.visible = true;
        closeButton.visible = true;
        if (!update.isDirectLink)
        {
            buttonDownloaded.enable(update);
        }
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
        if (update.isDirectLink)
        {
            updateButton.displayString = StatCollector.translateToLocal(Strings.UPDATE);
            updateButton.enabled = !update.isDownloaded();
        }
        else
        {
            updateButton.displayString = StatCollector.translateToLocal(Strings.OPEN_WEBPAGE);
            updateButton.enabled = update.updateURL != null;
        }
        if (openUpdate.changeLog != null)
        {
            changeLogList.setText(openUpdate.changeLog);
        }
    }

    public void closeInfoScreen()
    {
        openUpdate = null;
        updateButton.visible = false;
        closeButton.visible = false;
        buttonDownloaded.disable();
        changeLogList.setText("");
    }
}
