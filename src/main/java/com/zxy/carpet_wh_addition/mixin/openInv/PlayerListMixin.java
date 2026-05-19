package com.zxy.carpet_wh_addition.mixin.openInv;

import com.zxy.carpet_wh_addition.OpenInventoryPacket;
import net.minecraft.network.Connection;
import net.minecraft.server.players.PlayerList;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static com.zxy.carpet_wh_addition.CarpetWuHuAddition.loadPrinter;

//#if MC > 12001
import net.minecraft.server.network.CommonListenerCookie;
//#endif

@Mixin(PlayerList.class)
public class PlayerListMixin {
    @Inject(at = @At("RETURN"), method = "placeNewPlayer")
    private void onPlayerConnect(Connection connection, ServerPlayer player,
                                 //#if MC > 12001
                                 CommonListenerCookie clientData,
                                 //#endif
                                 CallbackInfo ci) {
            OpenInventoryPacket.helloRemote(player);
    }
}
