package com.dynious.versionchecker.client.gui;

import com.dynious.versionchecker.api.Update;

public class UpdateListProperties
{
    private GuiUpdates gui;
    private boolean showNEM = true;
    private boolean showCurse = true;

    public UpdateListProperties(GuiUpdates gui)
    {
        this.gui = gui;
    }

    public boolean shouldBeInList(Update update)
    {
        if (!showNEM && update.updateType == Update.UpdateType.NOT_ENOUGH_MODS)
            return false;
        else if (!showCurse && update.updateType == Update.UpdateType.CURSE)
            return false;
        return true;
    }

    public void updateList()
    {
        gui.updateList();
    }

    public boolean showNEM()
    {
        return showNEM;
    }

    public boolean showCurse()
    {
        return showCurse;
    }

    public void setShowNEM(boolean showNEM)
    {
        this.showNEM = showNEM;
        updateList();
    }

    public void setShowCurse(boolean showCurse)
    {
        this.showCurse = showCurse;
        updateList();
    }
}
