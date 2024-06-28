package com.zxy.remote_interactions.mixin.PlacementAndDestruction;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.zxy.remote_interactions.config.ServerConfig.configData;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
//    @Inject(at = @At(value = "INVOKE",target = "Lnet/minecraft/item/ItemStack;isItemEnabled(Lnet/minecraft/resource/featuretoggle/FeatureSet;)Z"),method = "onPlayerInteractBlock")
//    public void test1(PlayerInteractBlockC2SPacket packet, CallbackInfo ci){
//        System.out.println("111");
//    }
//    @Inject(at = @At(value = "INVOKE",target = "Lnet/minecraft/util/math/Vec3d;squaredDistanceTo(Lnet/minecraft/util/math/Vec3d;)D"),method = "onPlayerInteractBlock")
//    public void test(PlayerInteractBlockC2SPacket packet, CallbackInfo ci){
//        System.out.println("???");
//    }
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;squaredDistanceTo(Lnet/minecraft/util/math/Vec3d;)D"), method = "onPlayerInteractBlock")
    public double squaredDistanceTo1(Vec3d instance, Vec3d vec) {
        double d = vec.x - instance.x;
        double e = vec.y - instance.y;
        double f = vec.z - instance.z;
        double v = d * d + e * e + f * f;
        if(configData.putRange == -1 || v < configData.putRange) return -1;
        return v;
    }
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;squaredDistanceTo(DDD)D"), method = "onPlayerInteractBlock")
    public double squaredDistanceTo2(ServerPlayerEntity instance, double x, double y, double z) {
        double d = x - instance.getX();
        double e = y - instance.getY();
        double f = z - instance.getZ();
        double v = d * d + e * e + f * f;
        if(configData.putRange == -1 || v < configData.putRange) return -1;
        return v;
    }
}
