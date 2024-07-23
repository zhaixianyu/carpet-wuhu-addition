package com.zxy.remote_interactions;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import static net.minecraft.block.ShulkerBoxBlock.FACING;

public class OpenInventoryPacket{
    private static final ChunkTicketType<ChunkPos> OPEN_TICKET =
            ChunkTicketType.create("ender_pearl", Comparator.comparingLong(ChunkPos::toLong), 2);
    private static final Identifier HELLO_REMOTE_INTERACTIONS = new Identifier("hello", "hello_remote_interactions");
    public static HashMap<ServerPlayerEntity,TickList> tickMap = new HashMap<>();
    private static final Identifier OPEN_INVENTORY = new Identifier("remoteinventory", "open_inventory");
    private static final Identifier OPEN_RETURN = new Identifier("openreturn", "open_return");
    public static ArrayList<ServerPlayerEntity> playerlist = new ArrayList<>();
    public static void registerReceivePacket(){
        ServerPlayNetworking.registerGlobalReceiver(OPEN_INVENTORY, (server, player, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {

            BlockPos pos = packetByteBuf.readBlockPos();
            RegistryKey<World> key = RegistryKey.of(RegistryKeys.WORLD, packetByteBuf.readIdentifier());
            server.execute(() -> openInv(server,player,pos,key));
        });
    }

    public static void openInv(MinecraftServer server, ServerPlayerEntity player, BlockPos pos, RegistryKey<World> key){
        ServerWorld world = server.getWorld(key);
        if (world == null) return;
        BlockState blockState = world.getBlockState(pos);
        if(blockState==null){
            world.getChunkManager().addTicket(OPEN_TICKET,new ChunkPos(pos),2,new ChunkPos(pos));
        }
        playerlist.add(player);
        if (blockState == null) return;
        tickMap.put(player,new TickList(blockState.getBlock(),world,pos,blockState));
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ShulkerBoxBlockEntity entity &&
                !world.isSpaceEmpty(ShulkerEntity.calculateBoundingBox(0.0f,blockState.get(FACING),  0.5f).offset(pos).contract(1.0E-6)) &&
                entity.getAnimationStage() == ShulkerBoxBlockEntity.AnimationStage.CLOSED) {
            System.out.println("openFail" + pos);
            openReturn(player,blockState,false);
            return;
        }
        NamedScreenHandlerFactory handler = null;
        try {
            handler = ((BlockWithEntity)blockState.getBlock()).createScreenHandlerFactory(blockState, world, pos);
        } catch (Exception ignored) {}
        ActionResult r = blockState.onUse(world, player, new BlockHitResult(Vec3d.ofCenter(pos), Direction.UP, pos, false));

        if ((r != null && !r.equals(ActionResult.CONSUME)) || handler == null) {
            System.out.println("openFail" + pos);
            openReturn(player,blockState,false);
            return;
        }
        openReturn(player,blockState,true);
//        System.out.println("player " + player.getName());
    }
    public static void openReturn(ServerPlayerEntity player, BlockState state, boolean open){
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        MyPacket.encode(new MyPacket(state,open),buf);
        ServerPlayNetworking.send(player,OPEN_RETURN,buf);
    }
    public static void helloRemote(ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, HELLO_REMOTE_INTERACTIONS, new PacketByteBuf(Unpooled.buffer()));
    }
}