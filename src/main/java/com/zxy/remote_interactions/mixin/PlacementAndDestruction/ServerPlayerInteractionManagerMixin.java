package com.zxy.remote_interactions.mixin.PlacementAndDestruction;

import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.zxy.remote_interactions.config.ServerConfig.configData;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
    @Redirect(at = @At(value = "INVOKE",target = "Lnet/minecraft/util/math/Vec3d;squaredDistanceTo(Lnet/minecraft/util/math/Vec3d;)D",ordinal = 0),method = "processBlockBreakingAction")
    public double processBlockBreakingAction(Vec3d instance, Vec3d vec){
        double d = vec.x - instance.x;
        double e = vec.y - instance.y;
        double f = vec.z - instance.z;
        double v = d * d + e * e + f * f;
        if(configData.destructionRange == -1 || v < configData.destructionRange) return -1;
        return v;
    }
}
