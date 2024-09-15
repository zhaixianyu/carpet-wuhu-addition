package com.zxy.wuhuclient.mixin.masa.litematica_easy_place_fix;

import com.zxy.wuhuclient.features_list.EasyPlaceFix;
import fi.dy.masa.litematica.config.Configs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.zxy.wuhuclient.config.Configs.EASY_PLACED_FIX;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin_EasyPlaceFix {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "interactBlock", at = @At("HEAD"), cancellable = true)
    //#if MC > 11802
    private void onInteractBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
    //#else
    //$$ private void onInteractBlock(ClientPlayerEntity player, ClientWorld world , Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
    //#endif
        if (EASY_PLACED_FIX.getBooleanValue() && Configs.Generic.EASY_PLACE_MODE.getBooleanValue() && !EasyPlaceFix.isPlacingWithEasyPlace) {
            if (EasyPlaceFix.handleEasyPlaceRestriction(client)) {
                cir.setReturnValue(ActionResult.FAIL);
            }
        }
    }
}