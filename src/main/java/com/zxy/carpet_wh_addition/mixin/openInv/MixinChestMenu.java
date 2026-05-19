package com.zxy.carpet_wh_addition.mixin.openInv;

import com.zxy.carpet_wh_addition.OpenInventoryPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ChestMenu.class)
public class MixinChestMenu {
    @Inject(at = @At("HEAD"), method = "removed",cancellable = true,locals = LocalCapture.CAPTURE_FAILHARD)
    public void onClosed(Player player, CallbackInfo ci) {
        if(!(player instanceof ServerPlayer)) return;
        for (ServerPlayer player1 : OpenInventoryPacket.playerlist) {
            if (player.equals(player1)) {
                ci.cancel();
            }
        }
    }
}
