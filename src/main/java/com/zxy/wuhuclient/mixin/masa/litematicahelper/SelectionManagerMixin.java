package com.zxy.wuhuclient.mixin.masa.litematicahelper;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.zxy.wuhuclient.config.Configs;
import fi.dy.masa.litematica.selection.SelectionManager;
import fi.dy.masa.litematica.world.SchematicWorldHandler;
import fi.dy.masa.litematica.world.WorldSchematic;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import static fi.dy.masa.litematica.util.RayTraceUtils.*;

@Mixin(SelectionManager.class)
public class SelectionManagerMixin {
    @WrapOperation(at = @At(value = "INVOKE", target = "Lfi/dy/masa/litematica/util/RayTraceUtils;getTargetedPosition(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;DZ)Lnet/minecraft/util/math/BlockPos;"),method = "resetSelectionToClickedPosition")
    public BlockPos resetSelectionToClickedPosition(World world, Entity player, double maxDistance, boolean sneakToOffset, Operation<BlockPos> original){
        return getPos(world,player,maxDistance,sneakToOffset,original);
    }
    @WrapOperation(at = @At(value = "INVOKE", target = "Lfi/dy/masa/litematica/util/RayTraceUtils;getTargetedPosition(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;DZ)Lnet/minecraft/util/math/BlockPos;"),method = "growSelectionToContainClickedPosition")
    public BlockPos growSelectionToContainClickedPosition(World world, Entity player, double maxDistance, boolean sneakToOffset, Operation<BlockPos> original){
        return getPos(world,player,maxDistance,sneakToOffset,original);
    }
    @WrapOperation(at = @At(value = "INVOKE", target = "Lfi/dy/masa/litematica/util/RayTraceUtils;getTargetedPosition(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;DZ)Lnet/minecraft/util/math/BlockPos;"),method = "setPositionOfCurrentSelectionToRayTrace")
    public BlockPos setPositionOfCurrentSelectionToRayTrace(World world, Entity player, double maxDistance, boolean sneakToOffset, Operation<BlockPos> original){
        return getPos(world,player,maxDistance,sneakToOffset,original);
    }

    @Unique
    private BlockPos getPos(World world, Entity player, double maxDistance, boolean sneakToOffset, Operation<BlockPos> original){
        WorldSchematic schematicWorld = SchematicWorldHandler.getSchematicWorld();
        if (schematicWorld == null || !Configs.LITEMATICA_HELPER.getBooleanValue()) return original.call(world,player,maxDistance,sneakToOffset);

        BlockHitResult blockHitResult = traceToSchematicWorld(player, 200, true, true);
        if(blockHitResult == null || schematicWorld.getBlockState(blockHitResult.getBlockPos()).isAir()){
            return original.call(world, player, maxDistance, sneakToOffset);
        }
        BlockPos pos = blockHitResult.getBlockPos();
        if (sneakToOffset == player.isSneaking())
        {
            pos = pos.offset(blockHitResult.getSide());
        }
        return pos;
    }
}
