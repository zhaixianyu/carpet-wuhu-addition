package com.zxy.remoteinventory01;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import static net.minecraft.block.ShulkerBoxBlock.FACING;

public class OpenInventoryPacket{
    private static final ChunkTicketType<ChunkPos> OPEN_TICKET =
            ChunkTicketType.create("ender_pearl", Comparator.comparingLong(ChunkPos::toLong), 2);
    private static final Identifier OPEN_INVENTORY = new Identifier("remoteinventory", "open_inventory");
    private static final Identifier OPEN_RETURN = new Identifier("openreturn", "open_return");
    public static ArrayList<ServerPlayerEntity> playerlist = new ArrayList<>();
    public static HashMap<ServerPlayerEntity,TickList> tickmap = new HashMap<>();
    public static void registerReceivePacket(){

        ServerPlayNetworking.registerGlobalReceiver(OPEN_INVENTORY, (server, player, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {
            BlockPos pos = packetByteBuf.readBlockPos();
            RegistryKey<World> key = RegistryKey.of(Registry.WORLD_KEY, packetByteBuf.readIdentifier());
            server.execute(() -> openInv(server,player, pos,key));
        });
    }

    public static void openInv(MinecraftServer server, ServerPlayerEntity player, BlockPos pos, RegistryKey<World> key){
        ServerWorld world = server.getWorld(key);
        BlockState blockState = world.getBlockState(pos);
        if(blockState==null){
            world.getChunkManager().addTicket(OPEN_TICKET,new ChunkPos(pos),2,new ChunkPos(pos));
        }
        playerlist.add(player);
        tickmap.put(player,new TickList(blockState.getBlock(),world,pos,blockState));

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ShulkerBoxBlockEntity entity &&
                !world.isSpaceEmpty(ShulkerEntity.calculateBoundingBox(blockState.get(FACING), 0.0f, 0.5f).offset(pos).contract(1.0E-6)) &&
                entity.getAnimationStage() == ShulkerBoxBlockEntity.AnimationStage.CLOSED) {
            System.out.println("openFail" + pos);
                openFail(player);
                return;
        }
        NamedScreenHandlerFactory handler = null;
        try {
            handler = ((BlockWithEntity)blockState.getBlock()).createScreenHandlerFactory(blockState, world, pos);
        } catch (Exception e) {
        }
        ActionResult r = blockState.onUse(world, player, Hand.MAIN_HAND, new BlockHitResult(Vec3d.ofCenter(pos), Direction.UP, pos, false));
        if ((r != null && !r.equals(ActionResult.CONSUME)) || handler == null) {
            System.out.println("openFail" + pos);
            openFail(player);
        }
//        System.out.println("player " + player.getName());
    }
    public static void openFail(ServerPlayerEntity player){

        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBoolean(false);
        ServerPlayNetworking.send(player,OPEN_RETURN,new PacketByteBuf(buf));
    }
    public static void sendOpenInventory(BlockPos pos, RegistryKey<World> key){
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBlockPos(pos);
        buf.writeIdentifier(key.getValue());
        ClientPlayNetworking.send(OPEN_INVENTORY, new PacketByteBuf(buf));
    }
}