package com.zxy.carpet_wh_addition.mixin;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.zxy.carpet_wh_addition.config.CarpetWuHuSettings.anvilEnchantRemoveRestriction;


@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin {
    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/Property;get()I",ordinal = 1), method = "updateResult")
    private int get(Property instance, Operation<Integer> original) {
        int i = instance.get();
        if (anvilEnchantRemoveRestriction && i >= 40) {
            instance.set(39);
            return 39;
        }else return original.call(instance);
    }
    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/Property;set(I)V"),method = "updateResult")
    private void setRepairCost(Property instance, int repairCost, Operation<Void> original){
        if (anvilEnchantRemoveRestriction && repairCost >= 40) {
            instance.set(38);
        }else original.call(instance, repairCost);
    }
    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;getMaxLevel()I"),method = "updateResult")
    private int getMaxLevel(Enchantment instance, Operation<Integer> original){
        return anvilEnchantRemoveRestriction ? Integer.MAX_VALUE : original.call(instance);
    }
}
