package com.zxy.carpet_wh_addition.mixin.PlacementAndDestruction;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.zxy.carpet_wh_addition.config.CarpetWuHuSettings.handLength;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
    //#if MC > 12004
    //$$
    //#elseif MC > 11802
    @WrapOperation(at = @At(value = "INVOKE",target = "Lnet/minecraft/util/math/Vec3d;squaredDistanceTo(Lnet/minecraft/util/math/Vec3d;)D",ordinal = 0),method = "processBlockBreakingAction")
    public double processBlockBreakingAction(Vec3d instance, Vec3d vec, Operation<Double> original){
        if (handLength < 0 ) return original.call(instance, vec);
        double d = vec.x - instance.x;
        double e = vec.y - instance.y;
        double f = vec.z - instance.z;
        double v = d * d + e * e + f * f;
        if(v > handLength * handLength) return -1;
        return v;
    }
    //#else
    //$$
    //$$ @ModifyVariable(method = "processBlockBreakingAction", at = @At("STORE"), ordinal = 3)
    //$$ private double modifyDistance(double g) {
    //$$     if (handLength < 0 ) return g;
    //$$     return g > handLength * handLength ? 100 : -1;
    //$$     // 修改计算的距离值
//        return handLength * handLength;
    //$$ }
    //#endif

}
