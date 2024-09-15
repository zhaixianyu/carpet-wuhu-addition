package com.zxy.wuhuclient.mixin.masa.litematica_easy_place_fix;

import com.zxy.wuhuclient.features_list.EasyPlaceFix;
import fi.dy.masa.litematica.util.WorldUtils;
import fi.dy.masa.malilib.util.LayerRange;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static com.zxy.wuhuclient.config.Configs.EASY_PLACED_FIX;

@Mixin(WorldUtils.class)
public class WorldUtilsMixin {
    //#if MC > 11802
    @Inject(method = "doEasyPlaceAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;interactBlock(Lnet/minecraft/client/network/ClientPlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;", remap = true), require = 2)
    //#else
    //$$ @Inject(method = "doEasyPlaceAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;interactBlock(Lnet/minecraft/client/network/ClientPlayerEntity;Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;", remap = true), require = 2)
    //#endif
    private static void preInteractBlock(CallbackInfoReturnable<ActionResult> cir) {
        EasyPlaceFix.isPlacingWithEasyPlace = true;
    }
    //#if MC > 11802
    @Inject(method = "doEasyPlaceAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;interactBlock(Lnet/minecraft/client/network/ClientPlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;", shift = At.Shift.AFTER, remap = true), require = 2)
    //#else
    //$$ @Inject(method = "doEasyPlaceAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;interactBlock(Lnet/minecraft/client/network/ClientPlayerEntity;Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;", shift = At.Shift.AFTER, remap = true), require = 2)
    //#endif
    private static void postInteractBlock(CallbackInfoReturnable<ActionResult> cir) {
        EasyPlaceFix.isPlacingWithEasyPlace = false;
    }

    @Inject(method = "placementRestrictionInEffect", at = @At(value = "INVOKE", target = "Lfi/dy/masa/litematica/materials/MaterialCache;getInstance()Lfi/dy/masa/litematica/materials/MaterialCache;"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD,remap = false)
    private static void stopEasyPlaceWhenBlockAlreadyCorrect(MinecraftClient mc, CallbackInfoReturnable<Boolean> cir, HitResult trace, ItemStack stack, BlockHitResult blockHitResult, ItemPlacementContext ctx, BlockPos pos, BlockState stateClient, World worldSchematic, LayerRange range, boolean schematicHasAir, BlockState stateSchematic) {
        if (EASY_PLACED_FIX.getBooleanValue() && stateClient == stateSchematic) {
            cir.setReturnValue(Boolean.TRUE);
        }
    }
}
