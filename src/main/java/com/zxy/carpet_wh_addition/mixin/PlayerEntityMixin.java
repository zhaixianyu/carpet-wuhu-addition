package com.zxy.carpet_wh_addition.mixin;

import com.zxy.carpet_wh_addition.InventoryUtils;
import com.zxy.carpet_wh_addition.config.CarpetWuHuSettings;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.zxy.carpet_wh_addition.config.CarpetWuHuSettings.handLength;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(at = @At(value = "RETURN"),method = "getProjectileType", cancellable = true)
    public void getProjectileType(ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        int enchantmentLevel = InventoryUtils.getEnchantmentLevel(stack, Enchantments.INFINITY);
        if (CarpetWuHuSettings.infiniteNotRequireArrows && enchantmentLevel > 0) {
            if (cir.getReturnValue().isEmpty()) {
                cir.setReturnValue(new ItemStack(Items.ARROW));
            }
        }
    }

    //#if MC > 12004
    //$$ @Inject(at = @At("RETURN"),method = "getBlockInteractionRange", cancellable = true)
    //$$ public void getBlockInteractionRange(CallbackInfoReturnable<Double> cir){
    //$$     if(!((PlayerEntity)(Object)this).getWorld().isClient && handLength != -1) cir.setReturnValue(handLength);
    //$$ }
    //#else
    //#endif
}
