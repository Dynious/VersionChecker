package com.dynious.versionchecker.client.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.dynious.versionchecker.api.Update;
import com.dynious.versionchecker.handler.DownloadThread;
import com.dynious.versionchecker.handler.UpdateHandler;
import com.dynious.versionchecker.lib.Reference;
import com.dynious.versionchecker.lib.Resources;
import com.dynious.versionchecker.lib.Strings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.text.translation.I18n;

public class GuiUpdateList extends GuiScroll
{
    private GuiUpdates parent;
    private List<Update> updateList = new ArrayList<Update>();
    private int selectedIndex = -1;

    public GuiUpdateList(GuiUpdates parent, int width, int height, int top, int bottom, int left)
    {
        super(Minecraft.getMinecraft(), width, height, top, bottom, left, 45);
        this.parent = parent;
        setPadding(4, 4);
        makeList();
    }

    public void makeList()
    {
        updateList.clear();

        for (int index = 0; index < UpdateHandler.getListSize(); index++)
        {
            Update update = UpdateHandler.getElement(index);
            if (update != null && parent.getUpdateListProperties().shouldBeInList(update))
            {
                updateList.add(update);
            }
        }
    }

    @Override
    protected int getSize()
    {
        return updateList.size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick)
    {
        selectedIndex = index;
        if (doubleClick && updateList.get(index) != null)
        {
            parent.openInfoScreen(updateList.get(index));
        }
    }

    @Override
    protected boolean isSelected(int index)
    {
        return index == selectedIndex;
    }

    @Override
    protected void drawBackground()
    {
        parent.drawDefaultBackground();
    }

    @Override
    protected void drawSlot(int slotIndex, int minX, int maxX, int minY, int maxY, Tessellator tesselator)
    {
        Update update = updateList.get(slotIndex);
        if (update != null && !update.oldVersion.matches(update.newVersion))
        {
            this.parent.getFontRenderer().drawString(this.parent.getFontRenderer().trimStringToWidth(update.displayName, listWidth - 10), minX + 5, minY + 4, 0xFFFFFF);
            if (this.parent.getFontRenderer().getStringWidth(update.newVersion) >= (listWidth - 125)){
            	this.parent.getFontRenderer().drawString(this.parent.getFontRenderer().trimStringToWidth(update.oldVersion + " -> ", listWidth - 10), minX + 5, minY + 15, 0xCCCCCC);
            	this.parent.getFontRenderer().drawString(this.parent.getFontRenderer().trimStringToWidth("  " + update.newVersion, listWidth - 10), minX + 5, minY + 25, 0xCCCCCC);
            }
            else
            {
            	this.parent.getFontRenderer().drawString(this.parent.getFontRenderer().trimStringToWidth(update.oldVersion + " -> " + update.newVersion, listWidth - 10), minX + 5, minY + 15, 0xCCCCCC);
            }
            
            String info;

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getMinecraft().renderEngine.bindTexture(Resources.GUI_ICONS);

            if (DownloadThread.isUpdating(update))
            {
                Gui.drawModalRectWithCustomSizedTexture(maxX - 30, minY + 8, 0, 0, 16, 16, 64, 32);

                info = I18n.translateToLocal(Strings.UPDATING);
            }
            else if (update.isDownloaded())
            {
                Gui.drawModalRectWithCustomSizedTexture(maxX - 30, minY + 8, 16, 0, 16, 16, 64, 32);

                if (!update.MOD_ID.equalsIgnoreCase(Reference.MOD_ID))
                {
                    info = I18n.translateToLocal(Strings.IS_DOWNLOADED);
                }
                else
                {
                    info = I18n.translateToLocal(Strings.UNABLE_TO_REMOVE_SELF);
                }
            }
            else if (update.isErrored())
            {
                Gui.drawModalRectWithCustomSizedTexture(maxX - 30, minY + 8, 32, 0, 16, 16, 64, 32);

                info = I18n.translateToLocal(Strings.ERRORED);
            }
            else if (update.isDirectLink)
            {
                Gui.drawModalRectWithCustomSizedTexture(maxX - 30, minY + 8, 16, 16, 16, 16, 64, 32);

                info = I18n.translateToLocal(Strings.DL_AVAILABLE);
            }
            else if (update.updateURL != null)
            {
                Gui.drawModalRectWithCustomSizedTexture(maxX - 30, minY + 8, 0, 16, 16, 16, 64, 32);

                info = I18n.translateToLocal(Strings.LINK_TO_DL);
            }
            else
            {
                info = I18n.translateToLocal(Strings.CANNOT_UPDATE);
            }

            if (update.updateType == Update.UpdateType.NOT_ENOUGH_MODS)
            {
                Gui.drawModalRectWithCustomSizedTexture(maxX - 30, minY + 8, 32, 16, 16, 16, 64, 32);
            }
            else if (update.updateType == Update.UpdateType.CURSE)
            {
                Gui.drawModalRectWithCustomSizedTexture(maxX - 30, minY + 8, 48, 0, 16, 16, 64, 32);
            }

            this.parent.getFontRenderer().drawString(this.parent.getFontRenderer().trimStringToWidth(info, listWidth - 10), minX + 5, minY + 35, 0xCCCCCC);
        }
    }

    @Override
    public void overlayBackground()
    {
        this.client.renderEngine.bindTexture(Gui.OPTIONS_BACKGROUND);
        GL11.glColor4f(0.25F, 0.25F, 0.25F, 1.0F);
        Gui.drawModalRectWithCustomSizedTexture(left - 10, top - slotHeight, 0, 0, listWidth + 20, slotHeight, 32, 32);
        Gui.drawModalRectWithCustomSizedTexture(left - 10, top + listHeight, 0, listHeight + slotHeight, listWidth + 20, slotHeight, 32, 32);
    }
}
