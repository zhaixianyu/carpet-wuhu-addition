package com.zxy.carpet_wh_addition.mixin;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.dimension.end.EnderDragonFight;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.zxy.carpet_wh_addition.config.CarpetWuHuSettings.dragonsDropMoreExperience;

@Mixin(EnderDragon.class)
public class EnderDragoEntityMixin{
    @Shadow
    private EnderDragonFight dragonFight;
    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/dimension/end/EnderDragonFight;hasPreviouslyKilledDragon()Z"), method = "tickDeath")
    private boolean hasPreviouslyKilled(EnderDragonFight instance, Operation<Boolean> original){
        if(dragonsDropMoreExperience) return false;
        return original.call(instance);
    }

    //更新版本会导致龙战龙的uuid和旧版本的龙uuid不一样 从而导致无法获取龙战数据
    @Inject(at = @At(value = "HEAD"), method = "tickDeath")
    private void test(CallbackInfo ci) {
        EnderDragonFight fight1 = dragonFight;
        Level world = ((EnderDragon)(Object)this).level();
        if(fight1 == null && dragonsDropMoreExperience && !world.isClientSide()){
            dragonFight = ((ServerLevel) world).getDragonFight();
        }
    }
}
