package com.dynious.versionchecker.client.gui;

import com.dynious.versionchecker.api.Update;
import com.dynious.versionchecker.handler.RemoveHandler;
import com.dynious.versionchecker.helper.ModHelper;
import com.dynious.versionchecker.lib.Resources;
import com.dynious.versionchecker.lib.Strings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.text.translation.I18n;
import org.lwjgl.opengl.GL11;

import java.io.File;

public class GuiButtonDownloaded extends GuiButton
{
    private Update update;
    private boolean ticked;

    public GuiButtonDownloaded(int id, int x, int y)
    {
        super(id, x, y, 20, 20, "");
    }

    public void setUpdate(Update update)
    {
        this.visible = update != null && !update.isDirectLink;
        ticked = update != null && RemoveHandler.filesToDelete.contains( ModHelper.getModContainer(update.MOD_ID).getSource());
        this.update = update;
    }

    public void onButtonClicked()
    {
        ticked = !ticked;
        File file = ModHelper.getModContainer(update.MOD_ID).getSource();
        if (ticked)
        {
            RemoveHandler.filesToDelete.add(file);
        }
        else
        {
            RemoveHandler.filesToDelete.remove(file);
        }
        update.setDownloaded(ticked);
    }

    @Override
    public void drawButton(Minecraft minecraft, int x, int y)
    {
        if (this.visible)
        {
            Minecraft.getMinecraft().getTextureManager().bindTexture(Resources.GUI_BUTTON_TICK);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean flag = x >= this.xPosition && y >= this.yPosition && x < this.xPosition + this.width && y < this.yPosition + this.height;
            int k = 0;
            int j = 0;
            if (flag)
            {
                k += this.height;
            }
            if (ticked)
            {
                j += this.width;
            }
            Gui.drawModalRectWithCustomSizedTexture(this.xPosition, this.yPosition, j, k, this.width, this.height, 40, 40);
            this.drawString(Minecraft.getMinecraft().fontRendererObj, I18n.translateToLocal(Strings.MARK_DL), this.xPosition + this.width + 5, this.yPosition + this.height / 2 - 4, 0xFFFFFF);
        }
    }
}
