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
    public void drawButton(Minecraft mc, int x, int y, float partialTicks)
    {
        visible = UpdateHandler.getListSize() != 0;

        this.displayString = String.valueOf(UpdateHandler.getListSize());
        if (this.visible)
        {
            Minecraft.getMinecraft().getTextureManager().bindTexture(Resources.GUI_BUTTON_UPDATE);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean flag = x >= this.x && y >= this.y && x < this.x + this.width && y < this.y + this.height;
            int k = 0;
            if (flag)
            {
                k += this.height;
            }
            Gui.drawModalRectWithCustomSizedTexture(this.x, this.y, 0, k, this.width, this.height, 20, 40);
            this.drawCenteredString(Minecraft.getMinecraft().fontRenderer, this.displayString, this.x + this.width / 2 + 8, this.y + this.height / 2 + 3, 0xFFFFFF);
        }
    }
}
