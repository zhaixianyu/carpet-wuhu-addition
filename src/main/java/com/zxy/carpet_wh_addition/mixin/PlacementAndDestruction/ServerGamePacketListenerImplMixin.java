package com.zxy.carpet_wh_addition.mixin.PlacementAndDestruction;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.zxy.carpet_wh_addition.OpenInventoryPacket;
import com.zxy.carpet_wh_addition.config.CarpetWuHuSettings;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.zxy.carpet_wh_addition.config.CarpetWuHuSettings.handLength;
import static com.zxy.carpet_wh_addition.config.ServerConfig.configData;

@Mixin(value = ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {
    //#if MC > 11802
    @Shadow public ServerPlayer player;
    //#else
    //$$
    //#endif


    //#if MC > 12004

    //#else
        //#if MC > 11802
        //$$ @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;distanceToSqr(Lnet/minecraft/world/phys/Vec3;)D"), method = "handleUseItemOn")
        //$$ public double squaredDistanceTo1(Vec3 instance, Vec3 vec, Operation<Double> original) {
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
    //$$  @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;distanceToSqr(DDD)D"), method = "handleUseItemOn")
    //$$  public double squaredDistanceTo2(ServerPlayer instance, double x, double y, double z, Operation<Double> original) {
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
    @WrapOperation(at= @At(value = "INVOKE",target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;stillValid(Lnet/minecraft/world/entity/player/Player;)Z"),method = "handleContainerClick")
    private boolean test(AbstractContainerMenu instance, Player playerEntity, Operation<Boolean> original){
        for (ServerPlayer player1 : OpenInventoryPacket.playerlist) {
            if (player.equals(player1)) return true;
        }
        return this.player.containerMenu.stillValid(this.player);
    }
    //#else
    //$$
    //#endif

}
