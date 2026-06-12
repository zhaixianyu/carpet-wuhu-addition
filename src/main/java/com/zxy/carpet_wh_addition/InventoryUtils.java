package com.zxy.carpet_wh_addition;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;

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
}
