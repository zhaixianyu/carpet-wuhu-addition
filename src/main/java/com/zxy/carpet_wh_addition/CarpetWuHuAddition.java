package com.zxy.carpet_wh_addition;

import carpet.CarpetExtension;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;

public class CarpetWuHuAddition implements CarpetExtension , ModInitializer {

//    计划：控制权限 不给不信任的人使用

    @Override
    public void onInitialize() {

    }

    @Override
    public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandBuildContext) {
//        ServerPlayerEntity
        CarpetExtension.super.registerCommands(dispatcher, commandBuildContext);
    }
}
