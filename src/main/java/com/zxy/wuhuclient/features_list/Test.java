package com.zxy.wuhuclient.features_list;

import com.zxy.wuhuclient.config.Configs;
import net.minecraft.client.MinecraftClient;

public class Test {
    public static void tick(){
        if(!Configs.TEST.getBooleanValue())return;
        MinecraftClient.getInstance().player.setVelocity(0,0,0);
//        mezz.jei.core.search.CombinedSearchables
//        ZombieVillagerEntity.
//        System.out.println(Items.STONE.asItem().getTranslationKey());
//        System.out.println(Items.STONE.asItem().getName().getString().toLowerCase());
    }
}
