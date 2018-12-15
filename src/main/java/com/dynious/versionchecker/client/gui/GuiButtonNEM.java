package com.dynious.versionchecker.client.gui;

import com.dynious.versionchecker.lib.Resources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

public class GuiButtonNEM extends GuiButton
{
    private boolean ticked;
    private UpdateListProperties updateListProperties;

    public GuiButtonNEM(UpdateListProperties updateListProperties, int id, int x, int y)
    {
        super(id, x, y, 20, 20, "");
        this.updateListProperties = updateListProperties;
        ticked = updateListProperties.showNEM();
    }

    public void onButtonClicked()
    {
        ticked = !ticked;
        updateListProperties.setShowNEM(ticked);
    }

    @Override
    public void drawButton(Minecraft minecraft, int x, int y, float partialTicks)
    {
        if (this.visible)
        {
            Minecraft.getMinecraft().getTextureManager().bindTexture(Resources.GUI_BUTTON_NEM);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean flag = x >= this.x && y >= this.y && x < this.x + this.width && y < this.y + this.height;
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
            Gui.drawModalRectWithCustomSizedTexture(this.x, this.y, j, k, this.width, this.height, 40, 40);
        }
    }
}
