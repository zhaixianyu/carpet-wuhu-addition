package com.zxy.carpet_wh_addition.featuresList;

//#if MC > 12006
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.EnchantmentEffectContext;
//#else
//#endif
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;

import java.util.Map;
import java.util.Optional;

import static com.zxy.carpet_wh_addition.config.CarpetWuHuSettings.autoMending;

public class AutoMending {
    public static void mending(ServerPlayerEntity player){
        if (!autoMending) return;
        int exp = MathHelper.floor(player.experienceProgress * (float)player.getNextLevelExperience());
        int amount = exp == 0 ? player.experienceLevel > 0 ? 1 : 0 : exp;
        if(amount == 0) return;
        //#if MC > 12006
        Optional<EnchantmentEffectContext> optional = EnchantmentHelper.chooseEquipmentWith(EnchantmentEffectComponentTypes.REPAIR_WITH_XP, player, ItemStack::isDamaged);
        //#else
        //$$ Map.Entry<EquipmentSlot, ItemStack> entry = EnchantmentHelper.chooseEquipmentWith(Enchantments.MENDING, player, ItemStack::isDamaged);
        //$$ if (entry == null) return;
        //$$ Optional<ItemStack> optional = Optional.of(entry.getValue());
        //#endif

        if (optional.isPresent()) {
            //#if MC > 12006
            ItemStack itemStack = optional.get().stack();
            int i = EnchantmentHelper.getRepairWithXp(player.getServerWorld(), itemStack, amount);
            //#else
            //$$ int i = amount * 2;
            //$$ ItemStack itemStack = optional.get();
            //#endif

            int j = Math.min(i, itemStack.getDamage());
            itemStack.setDamage(itemStack.getDamage() - j);
            if (j > 0) {
                player.addExperience(-j);
            }
        }
    }
}
