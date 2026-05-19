package com.zxy.carpet_wh_addition;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.mojang.brigadier.CommandDispatcher;
import com.zxy.carpet_wh_addition.config.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.commands.CommandSourceStack;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


//#if MC > 11802
import net.minecraft.commands.CommandBuildContext;
//#else
//$$
//#endif
public class CarpetWuHuAddition implements CarpetExtension , ModInitializer {

    //TODO 创建一个物品栏 可以通过点击交互配置个人carpet属性 比如开关自动经验修补 不再是全都一样 包括自己配置垃圾桶
    public static final Logger LOGGER = LogManager.getLogger("CarpetWuHuAddition");
    public static final String MOD_NAME_LOWER_CASE = "carpetwuhuaddition";
    public static boolean loadPrinter = isLoadMod("litematica-printer");
    public static boolean isLoadMod(String modId){
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public void onInitialize() {
        if (!loadPrinter){
            OpenInventoryPacket.init();
            OpenInventoryPacket.registerReceivePacket();
        }
        ServerConfig.init();
        CarpetServer.manageExtension(new CarpetWuHuAddition());
    }

    @Override
    public void onGameStarted() {
        CarpetRuleRegistrar.register(CarpetServer.settingsManager,CarpetWuHuSettings.class);
    }

    @Override
    public void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher
        //#if MC > 11802
        , CommandBuildContext commandBuildContext
        //#endif
    ) {
        Command.init(dispatcher);
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        return Translate.getTranslate();
    }
}