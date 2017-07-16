package com.dynious.versionchecker.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.dynious.versionchecker.api.Update;
import com.dynious.versionchecker.handler.DownloadThread;
import com.dynious.versionchecker.handler.IMCHandler;
import com.dynious.versionchecker.helper.DesktopHelper;
import com.dynious.versionchecker.helper.WebHelper;
import com.dynious.versionchecker.lib.Reference;
import com.dynious.versionchecker.lib.Resources;
import com.dynious.versionchecker.lib.Strings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class GuiUpdates extends GuiScreen
{
    private GuiUpdateList updateList;
    private UpdateListProperties updateListProperties = new UpdateListProperties(this);
    private GuiButton updateButton;
    private GuiButton closeButton;
    private GuiButtonDownloaded buttonDownloaded;
    private GuiButtonNEM NEMButton;
    private GuiButtonCurse curseButton;

    private Update openUpdate = null;
    private GuiChangeLogList changeLogList;

    private int windowStartX, windowStartY, windowEndX, windowEndY;

    private static final int listShift = 50;
    private byte tempDisableButtonPress = 0;

    @Override
    public void initGui()
    {
        IMCHandler.processMessages(FMLInterModComms.fetchRuntimeMessages(Reference.MOD_ID));
        super.initGui();

        windowStartX = width / 2 - 110 + listShift;
        windowStartY = height / 2 - 90;
        windowEndX = width / 2 + 110 + listShift;
        windowEndY = height / 2 + 70;

        buttonList.add(new GuiButton(0, width / 2 - 75 + listShift, height - 30, 150, 20, I18n.translateToLocal("gui.done")));

        buttonList.add(updateButton = new GuiButton(1, width / 2 - 100 + listShift, height / 2 + 40, 96, 20, I18n.translateToLocal(Strings.UPDATE)));

        buttonList.add(closeButton = new GuiButton(2, width / 2 + 4 + listShift, height / 2 + 40, 96, 20, I18n.translateToLocal("gui.done")));

        buttonList.add(new GuiButton(3, 10, height - 30, 150, 20, I18n.translateToLocal(Strings.MOD_FOLDER)));

        buttonList.add(buttonDownloaded = new GuiButtonDownloaded(4, width / 2 - 100 + listShift, height / 2 + 15));

        buttonList.add(NEMButton = new GuiButtonNEM(getUpdateListProperties(), 5, width / 2 + 90 + listShift, height - 30));
        buttonList.add(curseButton = new GuiButtonCurse(getUpdateListProperties(), 6, width / 2 + 125 + listShift, height - 30));

        updateList = new GuiUpdateList(this, 300, height - 60, 20, height - 40, width / 2 - 150 + listShift);
        changeLogList = new GuiChangeLogList(this, 200, 75, height / 2 - 60, height / 2 + 15, width / 2 - 100 + listShift);

        if (openUpdate != null)
        {
            openInfoScreen(openUpdate);
        }
        else
        {
            closeInfoScreen();
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

        this.fontRendererObj.drawSplitString(I18n.translateToLocal(Strings.INFO).replace(";", "\n"), 10, height / 2 - 60, width / 2 - 150 + listShift - 20, 0xCCCCCC);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(Resources.GUI_LOGO);

        int i = width / 2 - 150 + listShift - 10;
        Gui.drawModalRectWithCustomSizedTexture(5, 5, 0, 0, i, (int) (i * 0.4), i, (int) (i * 0.4));

        if (openUpdate != null)
        {
            drawCenteredString(fontRendererObj, openUpdate.displayName, width / 2 + listShift, height / 2 - 80, 0xFFFFFF);
            if (openUpdate.changeLog == null)
            {
                drawCenteredString(fontRendererObj, I18n.translateToLocal(Strings.NO_CHANGE_LOG), width / 2 + listShift, height / 2 - 60, 0xCCCCCC);
            }
        }
        if (DownloadThread.isUpdating())
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getMinecraft().renderEngine.bindTexture(Resources.GUI_ICONS);
            Gui.drawModalRectWithCustomSizedTexture(width - 20, 4, 0, 0, 16, 16, 64, 32);
        }

        super.drawScreen(mouseX, mouseY, par3);

        drawToolTip(mouseX, mouseY);
        if (tempDisableButtonPress > 0)
            tempDisableButtonPress--;
    }

    public void updateList()
    {
        if (updateList != null)
            updateList.makeList();
    }

    public void drawWindow()
    {
        GL11.glColor4f(0.6F, 0.6F, 0.6F, 1.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(Resources.GUI_WINDOW);
        Gui.drawModalRectWithCustomSizedTexture(windowStartX, windowStartY, 0, 0, 220, 160, 220, 160);
    }

    public void drawToolTip(int mouseX, int mouseY)
    {
        if (updateButton.mousePressed(mc, mouseX, mouseY))
        {
            List<String> list = new ArrayList<String>();
            String left = openUpdate.updateURL;
            while(left != null)
            {
                String s = fontRendererObj.trimStringToWidth(left, 200);
                list.add(s);
                if (s.length() == left.length())
                {
                    break;
                }
                else
                {
                    left = left.substring(s.length());
                }
            }
            this.drawHoveringText(list, mouseX, mouseY, fontRendererObj);
        }
        else if (buttonDownloaded.mousePressed(mc, mouseX, mouseY))
        {
            this.drawHoveringText(Arrays.asList(I18n.translateToLocal(Strings.DL_MARKED_INFO).split(";")), mouseX, mouseY, fontRendererObj);
        }
        else if (NEMButton.mousePressed(mc, mouseX, mouseY))
        {
            this.drawHoveringText(Arrays.asList(I18n.translateToLocal(Strings.TOGGLE_NEM_UPDATE).split(";")), mouseX, mouseY, fontRendererObj);
        }
        else if (curseButton.mousePressed(mc, mouseX, mouseY))
        {
            this.drawHoveringText(Arrays.asList(I18n.translateToLocal(Strings.TOGGLE_CURSE_UPDATE).split(";")), mouseX, mouseY, fontRendererObj);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (tempDisableButtonPress > 0)
            return;

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
            case 5:
                NEMButton.onButtonClicked();
                break;
            case 6:
                curseButton.onButtonClicked();
                break;
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int par3) throws IOException
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
        changeLogList.disableInput = false;
        updateList.disableInput = true;
        buttonDownloaded.setUpdate(update);
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        if (update.isDirectLink)
        {
            updateButton.displayString = I18n.translateToLocal(Strings.UPDATE);
            updateButton.enabled = update.updateURL != null && !update.isDownloaded();
        }
        else
        {
            updateButton.displayString = I18n.translateToLocal(Strings.OPEN_WEBPAGE);
            updateButton.enabled = update.updateURL != null;
        }
        if (openUpdate.changeLog != null)
        {
            changeLogList.setText(openUpdate.changeLog);
        }
        tempDisableButtonPress = 5;
    }

    public void closeInfoScreen()
    {
        openUpdate = null;
        updateButton.visible = false;
        closeButton.visible = false;
        changeLogList.disableInput = true;
        updateList.disableInput = false;
        buttonDownloaded.setUpdate(null);
        changeLogList.setText("");
    }

    public UpdateListProperties getUpdateListProperties()
    {
        return updateListProperties;
    }
}
