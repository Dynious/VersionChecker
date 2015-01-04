package com.dynious.versionchecker.client.gui;

import com.dynious.versionchecker.handler.UpdateHandler;
import com.dynious.versionchecker.lib.Resources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

public class GuiButtonUpdates extends GuiButton
{
    public GuiButtonUpdates(int id, int x, int y)
    {
        super(id, x, y, 20, 20, String.valueOf(UpdateHandler.getListSize()));
        visible = UpdateHandler.getListSize() != 0;
    }

    @Override
    public void drawButton(Minecraft mc, int x, int y)
    {
        visible = UpdateHandler.getListSize() != 0;

        this.displayString = String.valueOf(UpdateHandler.getListSize());
        if (this.visible)
        {
            Minecraft.getMinecraft().getTextureManager().bindTexture(Resources.GUI_BUTTON_UPDATE);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean flag = x >= this.xPosition && y >= this.yPosition && x < this.xPosition + this.width && y < this.yPosition + this.height;
            int k = 0;
            if (flag)
            {
                k += this.height;
            }
            Gui.drawModalRectWithCustomSizedTexture(this.xPosition, this.yPosition, 0, k, this.width, this.height, 20, 40);
            this.drawCenteredString(Minecraft.getMinecraft().fontRendererObj, this.displayString, this.xPosition + this.width / 2 + 8, this.yPosition + this.height / 2 + 3, 0xFFFFFF);
        }
    }
}
