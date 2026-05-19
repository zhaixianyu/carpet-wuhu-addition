package com.zxy.carpet_wh_addition;

import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.server.commands.data.BlockDataAccessor;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.commands.CloneCommands;

//#if MC > 12006
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.core.Holder;
import java.util.Set;
//#endif

public class InventoryUtils {
    public static int getEnchantmentLevel(ItemStack itemStack,
                                          //#if MC > 12006
                                          ResourceKey<Enchantment> enchantment
                                          //#else
                                          //$$ Enchantment enchantment
                                          //#endif
    ){
        BlockDataAccessor c;
        BlockItem c1;
        CloneCommands c2;
        ItemStack c3;
        ClientboundContainerSetSlotPacket c4;
        FriendlyByteBuf cc;

        //#if MC > 12006
        ItemEnchantments enchantments = itemStack.getEnchantments();
        if (enchantments.equals(ItemEnchantments.EMPTY)) return -1;
        Set<Holder<Enchantment>> enchantmentsEnchantments = enchantments.keySet();
        for (Holder<Enchantment> entry : enchantmentsEnchantments) {
            if (entry.is(enchantment)) {
                return enchantments.getLevel(entry);
            }
        }
        return -1;
        //#else
        //$$ return EnchantmentHelper.getItemEnchantmentLevel(enchantment,itemStack);
        //#endif
    }
    private void te(){
        Minecraft a;
    }
}
