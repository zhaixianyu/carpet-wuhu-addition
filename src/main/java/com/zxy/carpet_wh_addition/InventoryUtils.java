package com.zxy.carpet_wh_addition;

import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.BlockDataObject;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.server.command.CloneCommand;

//#if MC > 12006
//$$ import net.minecraft.registry.RegistryKey;
//$$ import net.minecraft.component.type.ItemEnchantmentsComponent;
//$$ import net.minecraft.registry.entry.RegistryEntry;
//$$ import java.util.Set;
//#endif

public class InventoryUtils {
    public static int getEnchantmentLevel(ItemStack itemStack,
                                          //#if MC > 12006
                                          //$$ RegistryKey<Enchantment> enchantment
                                          //#else
                                          Enchantment enchantment
                                          //#endif
    ){
        BlockDataObject c;
        BlockItem c1;
        CloneCommand c2;
        ItemStack c3;
        ScreenHandlerSlotUpdateS2CPacket c4;
        PacketByteBuf cc;

        //#if MC > 12006
        //$$ ItemEnchantmentsComponent enchantments = itemStack.getEnchantments();
        //$$ if (enchantments.equals(ItemEnchantmentsComponent.DEFAULT)) return -1;
        //$$ Set<RegistryEntry<Enchantment>> enchantmentsEnchantments = enchantments.getEnchantments();
        //$$ for (RegistryEntry<Enchantment> entry : enchantmentsEnchantments) {
        //$$     if (entry.matchesKey(enchantment)) {
        //$$         return enchantments.getLevel(entry);
        //$$     }
        //$$ }
        //$$ return -1;
        //#else
        return EnchantmentHelper.getLevel(enchantment,itemStack);
        //#endif
    }
    private void te(){
        MinecraftClient a;
    }
}
