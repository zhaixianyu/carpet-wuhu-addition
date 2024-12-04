package com.zxy.carpet_wh_addition.mixin.PlacementAndDestruction;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.zxy.carpet_wh_addition.OpenInventoryPacket;
import com.zxy.carpet_wh_addition.config.CarpetWuHuSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.zxy.carpet_wh_addition.config.CarpetWuHuSettings.handLength;
import static com.zxy.carpet_wh_addition.config.ServerConfig.configData;

@Mixin(value = ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    //#if MC > 11802
    @Shadow public ServerPlayerEntity player;
    //#else
    //$$
    //#endif


    //#if MC > 12004

    //#else
        //#if MC > 11802
        //$$ @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;squaredDistanceTo(Lnet/minecraft/util/math/Vec3d;)D"), method = "onPlayerInteractBlock")
        //$$ public double squaredDistanceTo1(Vec3d instance, Vec3d vec, Operation<Double> original) {
        //$$     if (handLength < 0 ) return original.call(instance, vec);
        //$$     double d = vec.x - instance.x;
        //$$     double e = vec.y - instance.y;
        //$$     double f = vec.z - instance.z;
        //$$     double v = d * d + e * e + f * f;
        //$$     if(v <= handLength * handLength) return -1;
        //$$     return v;
        //$$ }
        //#endif
    //$$
    //$$  @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;squaredDistanceTo(DDD)D"), method = "onPlayerInteractBlock")
    //$$  public double squaredDistanceTo2(ServerPlayerEntity instance, double x, double y, double z, Operation<Double> original) {
    //$$      if (handLength < 0 ) return original.call(instance, x, y, z);
    //$$      double d = x - instance.getX();
    //$$      double e = y - instance.getY();
    //$$      double f = z - instance.getZ();
    //$$      double v = d * d + e * e + f * f;
    //$$      if(v <= handLength * handLength) return -1;
    //$$      return v;
    //$$  }
    //#else
    //$$
    //#endif


    //#if MC > 11802
    @WrapOperation(at= @At(value = "INVOKE",target = "Lnet/minecraft/screen/ScreenHandler;canUse(Lnet/minecraft/entity/player/PlayerEntity;)Z"),method = "onClickSlot")
    private boolean test(ScreenHandler instance, PlayerEntity playerEntity, Operation<Boolean> original){
        for (ServerPlayerEntity player1 : OpenInventoryPacket.playerlist) {
            if (player.equals(player1)) return true;
        }
        return this.player.currentScreenHandler.canUse(this.player);
    }
    //#else
    //$$
    //#endif

}
