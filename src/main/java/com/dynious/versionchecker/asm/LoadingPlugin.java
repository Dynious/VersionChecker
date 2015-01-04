package com.dynious.versionchecker.asm;

import com.dynious.versionchecker.deleter.Deleter;
import com.dynious.versionchecker.handler.RemoveHandler;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.TransformerExclusions({"com.dynious.versionchecker.asm"})
public class LoadingPlugin implements IFMLLoadingPlugin
{
    public LoadingPlugin()
    {
        if (RemoveHandler.DELETE_FILE.exists())
        {
            Deleter.deleteModsFromFile(RemoveHandler.DELETE_FILE.getAbsolutePath());
        }
    }

    @Override
    public String[] getASMTransformerClass()
    {
        return new String[0];
    }

    @Override
    public String getModContainerClass()
    {
        return null;
    }

    @Override
    public String getSetupClass()
    {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data)
    {

    }

    @Override
    public String getAccessTransformerClass()
    {
        return null;
    }
}
