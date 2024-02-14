package com.zxy.remote_interactions.config;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;

import java.nio.file.Path;

public class ServerConfig {
    public static Configs configData;
    public static void init() {
        ServerLifecycleEvents.SERVER_STARTING.register((MinecraftServer server) -> loadConfig());
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> saveConfig());
    }
    public static void loadConfig() {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve("RemoteInteractionConfig.json");
        configData = ConfigHelper.loadConfig(configPath, new Configs());
    }
    public static void saveConfig() {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve("RemoteInteractionConfig.json");
        ConfigHelper.saveConfig(configPath, configData);
    }
}
