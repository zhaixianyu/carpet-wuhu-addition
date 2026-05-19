package com.zxy.carpet_wh_addition.mixin.infiniteTreasureTrove;

import com.zxy.carpet_wh_addition.config.CarpetWuHuSettings;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.minecraft.world.level.block.entity.vault.VaultServerData;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;
import java.util.UUID;

@Mixin(VaultServerData.class)
public class VaultServerDataMixin {
    @Inject(method = "hasRewardedPlayer", at = @At("HEAD"), cancellable = true)
    void hasRewardedPlayer(Player player, CallbackInfoReturnable<Boolean> cir) {
        if(CarpetWuHuSettings.infiniteTreasureTrove) cir.setReturnValue(false);
    }
    @Inject(method = "getRewardedPlayers",at = @At("HEAD"), cancellable = true)
    private void getRewardedPlayers(CallbackInfoReturnable<Set<UUID>> cir){
        if(CarpetWuHuSettings.infiniteTreasureTrove) cir.setReturnValue(new ObjectLinkedOpenHashSet<>());
    }
}
