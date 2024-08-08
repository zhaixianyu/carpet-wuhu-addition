package com.zxy.carpet_wh_addition.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorStandEntity.class)
public class ArmorStandEntityMixin {
    @Inject(at = @At("HEAD"), method = "damage", cancellable = true)
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getSource() instanceof ShulkerBulletEntity) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
