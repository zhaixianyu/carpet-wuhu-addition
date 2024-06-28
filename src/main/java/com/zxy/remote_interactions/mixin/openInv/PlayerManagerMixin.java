package com.zxy.remote_interactions.mixin.openInv;

import com.zxy.remote_interactions.OpenInventoryPacket;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Inject(at = @At("TAIL"), method = "onPlayerConnect")
    private void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player,
                                 //#if MC > 12001
                                 ConnectedClientData clientData,
                                 //#endif
                                 CallbackInfo ci) {
        OpenInventoryPacket.helloRemote(player);
    }
}
