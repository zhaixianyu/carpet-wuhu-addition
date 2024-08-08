package com.zxy.carpet_wh_addition;

import com.zxy.carpet_wh_addition.config.ServerConfig;
import net.fabricmc.api.ModInitializer;

public class RemoteInteractions implements ModInitializer {
    //
    @Override
    public void onInitialize() {
        OpenInventoryPacket.registerReceivePacket();
        Command.init();
        ServerConfig.init();
    }
}
