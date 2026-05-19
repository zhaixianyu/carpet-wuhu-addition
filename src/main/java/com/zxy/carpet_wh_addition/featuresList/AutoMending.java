package com.zxy.carpet_wh_addition.featuresList;

//#if MC > 12006
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
//#else
//#endif
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;

import java.util.Map;
import java.util.Optional;

import static com.zxy.carpet_wh_addition.config.CarpetWuHuSettings.autoMending;

public class AutoMending {
    public static void mending(ServerPlayer player){
        if (!autoMending) return;
        int exp = Mth.floor(player.experienceProgress * (float)player.getXpNeededForNextLevel());
        int amount = exp == 0 ? player.experienceLevel > 0 ? 1 : 0 : exp;
        if(amount == 0) return;
        //#if MC > 12006
        Optional<EnchantedItemInUse> optional = EnchantmentHelper.getRandomItemWith(EnchantmentEffectComponents.REPAIR_WITH_XP, player, ItemStack::isDamaged);
        //#else
        //$$ Map.Entry<EquipmentSlot, ItemStack> entry = EnchantmentHelper.getRandomItemWith(Enchantments.MENDING, player, ItemStack::isDamaged);
        //$$ if (entry == null) return;
        //$$ Optional<ItemStack> optional = Optional.of(entry.getValue());
        //#endif

        if (optional.isPresent()) {
            //#if MC > 12006
            ItemStack itemStack = optional.get().itemStack();
            int i = EnchantmentHelper.modifyDurabilityToRepairFromXp(player.level(), itemStack, amount);
            //#else
            //$$ int i = amount * 2;
            //$$ ItemStack itemStack = optional.get();
            //#endif

            int j = Math.min(i, itemStack.getDamageValue());
            itemStack.setDamageValue(itemStack.getDamageValue() - j);
            if (j > 0) {
                player.giveExperiencePoints(-j);
            }
        }
    }
}
