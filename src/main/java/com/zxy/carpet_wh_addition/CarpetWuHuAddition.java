package com.zxy.carpet_wh_addition;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.mojang.brigadier.CommandDispatcher;
import com.zxy.carpet_wh_addition.config.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


//#if MC > 11802
//$$ import net.minecraft.command.CommandRegistryAccess;
//#else

//#endif
public class CarpetWuHuAddition implements CarpetExtension , ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger("CarpetWuHuAddition");
    public static final String MOD_NAME_LOWER_CASE = "carpetwuhuaddition";
    public static MinecraftServer minecraftServer;


    @Override
    public void onInitialize() {
        OpenInventoryPacket.init();
        OpenInventoryPacket.registerReceivePacket();
        ServerConfig.init();

        CarpetServer.manageExtension(new CarpetWuHuAddition());
    }

    @Override
    public void onGameStarted() {
        CarpetRuleRegistrar.register(CarpetServer.settingsManager,CarpetWuHuSettings.class);
    }

    @Override
    public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher
        //#if MC > 11802
        //$$ , CommandRegistryAccess commandBuildContext) {
        //#else
        ){
        //#endif
        Command.init(dispatcher);
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        return Translate.getTranslate();
    }
}