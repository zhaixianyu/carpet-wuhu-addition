package com.zxy.carpet_wh_addition.mixin;


import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.zxy.carpet_wh_addition.config.CarpetWuHuSettings.upDataWildDragonFight;

@Mixin(EnderDragonEntity.class)
public class EnderDragoEntityMixin extends MobEntity{
    @Shadow
    private EnderDragonFight fight;

    protected EnderDragoEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }
    //更新版本会导致龙战龙的uuid和旧版本的龙uuid不一样 从而导致无法获取龙战数据
    @Inject(at = @At(value = "TAIL"), method = "updatePostDeath")
    private void test(CallbackInfo ci) {
        EnderDragonFight fight1 = fight;
        World world = this.getWorld();
        if(fight1 == null && upDataWildDragonFight && !world.isClient){
            fight = ((ServerWorld) world).getEnderDragonFight();
        }
    }
}
