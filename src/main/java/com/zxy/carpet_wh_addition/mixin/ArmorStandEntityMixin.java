package com.zxy.carpet_wh_addition.mixin;


//#if MC > 12002
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.entity.vehicle.VehicleEntity;
//#else
//$$ import net.minecraft.world.entity.vehicle.AbstractMinecart;
//$$ import net.minecraft.world.entity.vehicle.Boat;
//#endif

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.projectile.ShulkerBullet;

import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.zxy.carpet_wh_addition.config.CarpetWuHuSettings.armorOrBoatStandIgnoredShulkerBullet;

@Mixin(value = {ArmorStand.class,
        //#if MC > 12002
        VehicleEntity.class
        //#else
        //$$ Boat.class,
        //$$ AbstractMinecart.class
        //#endif
})
public class ArmorStandEntityMixin {
    //#if MC < 12104
    //$$ @Inject(at = @At("HEAD"), method = "hurt", cancellable = true)
    //$$ public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
    //$$
    //#else
    @Inject(at = @At("HEAD"), method = "hurtServer", cancellable = true)
    public void damage(ServerLevel serverLevel, DamageSource source, float f, CallbackInfoReturnable<Boolean> cir) {
    //#endif

        if (armorOrBoatStandIgnoredShulkerBullet && source.getDirectEntity() instanceof ShulkerBullet) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
