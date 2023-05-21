package com.zxy.remoteinventory01;

import net.fabricmc.api.ModInitializer;

public class Remote_inventory_01 implements ModInitializer {
    @Override
    public void onInitialize() {
        OpenInventoryPacket.registerReceivePacket();
    }
}
