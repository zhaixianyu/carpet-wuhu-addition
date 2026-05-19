package com.zxy.carpet_wh_addition.mixin;

import com.zxy.carpet_wh_addition.InventoryUtils;
import com.zxy.carpet_wh_addition.config.CarpetWuHuSettings;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.zxy.carpet_wh_addition.config.CarpetWuHuSettings.handLength;

@Mixin(Player.class)
public class PlayerMixin {

    @Inject(at = @At(value = "RETURN"),method = "getProjectile", cancellable = true)
    public void getProjectileType(ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        int enchantmentLevel = InventoryUtils.getEnchantmentLevel(stack, Enchantments.INFINITY);
        if (CarpetWuHuSettings.infiniteNotRequireArrows && enchantmentLevel > 0) {
            if (cir.getReturnValue().isEmpty()) {
                cir.setReturnValue(new ItemStack(Items.ARROW));
            }
        }
    }

    //#if MC > 12004
    @Inject(at = @At("RETURN"),method = "blockInteractionRange", cancellable = true)
    public void getBlockInteractionRange(CallbackInfoReturnable<Double> cir){
        if(!((Player)(Object)this).level().isClientSide() && handLength != -1) cir.setReturnValue(handLength);
    }
    //#else
    //#endif
}
