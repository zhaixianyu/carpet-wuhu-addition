package com.zxy.carpet_wh_addition.mixin.openInv;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.zxy.carpet_wh_addition.OpenInventoryPacket.playerlist;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends PlayerEntity{

    public MixinServerPlayerEntity(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }
    @Inject(at = @At("HEAD"),method = "onHandledScreenClosed")
    public void closeHandledScreen(CallbackInfo ci) {
        playerlist.removeIf(player -> player.getUuid().equals(getUuid()));
    }
}
