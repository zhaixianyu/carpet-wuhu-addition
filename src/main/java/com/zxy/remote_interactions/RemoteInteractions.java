package com.zxy.remote_interactions;

import com.zxy.remote_interactions.config.ServerConfig;
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
