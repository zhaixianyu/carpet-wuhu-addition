package com.zxy.carpet_wh_addition.mixin;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.zxy.carpet_wh_addition.config.CarpetWuHuSettings.anvilEnchantRemoveRestriction;


@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {
    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/DataSlot;get()I",ordinal = 1), method = "createResult")
    private int get(DataSlot instance, Operation<Integer> original) {
        int i = instance.get();
        if (anvilEnchantRemoveRestriction && i >= 40) {
            instance.set(39);
            return 39;
        }else return original.call(instance);
    }
    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/DataSlot;set(I)V"),method = "createResult")
    private void setRepairCost(DataSlot instance, int repairCost, Operation<Void> original){
        if (anvilEnchantRemoveRestriction && repairCost >= 40) {
            instance.set(38);
        }else original.call(instance, repairCost);
    }
    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/Enchantment;getMaxLevel()I"),method = "createResult")
    private int getMaxLevel(Enchantment instance, Operation<Integer> original){
        return anvilEnchantRemoveRestriction ? Integer.MAX_VALUE : original.call(instance);
    }
}
