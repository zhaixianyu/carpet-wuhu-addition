package com.zxy.carpet_wh_addition;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
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

import static com.zxy.carpet_wh_addition.CarpetWuHuAddition.loadPrinter;
import static com.zxy.carpet_wh_addition.config.CarpetWuHuSettings.remoteOpenInventory;
import static net.minecraft.block.ShulkerBoxBlock.FACING;
//#if MC > 12004
import net.minecraft.block.Block;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

//#endif

//#if MC > 11802
import net.minecraft.registry.RegistryKeys;
//#else
//$$ import net.minecraft.util.registry.Registry;
//#endif
public class OpenInventoryPacket {
    private static final ChunkTicketType<ChunkPos> OPEN_TICKET =
            ChunkTicketType.create("openInv", Comparator.comparingLong(ChunkPos::toLong), 2);
    public static HashMap<ServerPlayerEntity, TickList> tickMap = new HashMap<>();

    //#if MC > 12006
    private static final Identifier OPEN_INVENTORY = Identifier.of("remoteinventory", "open_inventory");
    private static final Identifier OPEN_RETURN = Identifier.of("openreturn", "open_return");
    private static final Identifier HELLO_REMOTE_INTERACTIONS = Identifier.of("hello", "hello_remote_interactions");
    //#else
    //$$ private static final Identifier OPEN_INVENTORY = new Identifier("remoteinventory", "open_inventory");
    //$$ private static final Identifier OPEN_RETURN = new Identifier("openreturn", "open_return");
    //$$ private static final Identifier HELLO_REMOTE_INTERACTIONS = new Identifier("hello", "hello_remote_interactions");
    //#endif
    public static ArrayList<ServerPlayerEntity> playerlist = new ArrayList<>();

    //#if MC > 12004
    public static class OpenPackage implements CustomPayload{
       public static final Id<OpenPackage> OPEN_INVENTORY_ID = new Id<>(OPEN_INVENTORY);
       public static final PacketCodec<RegistryByteBuf,OpenPackage> CODEC = new PacketCodec<>() {

           @Override
           public void encode(RegistryByteBuf buf, OpenPackage value) {
               buf.writeRegistryKey(value.world);
               buf.writeBlockPos(value.pos);
           }
           @Override
           public OpenPackage decode(RegistryByteBuf buf) {
               OpenPackage openPackage = new OpenPackage();
               openPackage.world = buf.readRegistryKey(RegistryKeys.WORLD);
               openPackage.pos = buf.readBlockPos();
               return openPackage;
           }
       };
       RegistryKey<World> world = null;
       BlockPos pos = null;
       public OpenPackage() {
       }
        @Override
        public Id<? extends CustomPayload> getId() {
            return OPEN_INVENTORY_ID;
        }
    }

    public static class HelloPackage implements CustomPayload{
        public static final Id<HelloPackage> HELLO_REMOTE_INTERACTIONS_ID = new Id<>(HELLO_REMOTE_INTERACTIONS);
        public static final PacketCodec<RegistryByteBuf,HelloPackage> CODEC = new PacketCodec<>() {
            @Override
            public void encode(RegistryByteBuf buf, HelloPackage value) {
            }
            @Override
            public HelloPackage decode(RegistryByteBuf buf) {
                return new HelloPackage();
            }
        };
        @Override
        public Id<? extends CustomPayload> getId() {
            return HELLO_REMOTE_INTERACTIONS_ID;
        }
    }
    public static class ReturnPackage implements CustomPayload{
        BlockState state = null;
        boolean isOpen = false;
        public static final Id<ReturnPackage> OPEN_RETURN_ID = new Id<>(OPEN_RETURN);
        public static final PacketCodec<RegistryByteBuf,ReturnPackage> CODEC = new PacketCodec<>() {
            @Override
            public void encode(RegistryByteBuf buf, ReturnPackage value) {
                buf.writeInt(Block.getRawIdFromState(value.state));
                buf.writeBoolean(value.isOpen);
            }
            @Override
            public ReturnPackage decode(RegistryByteBuf buf) {
                ReturnPackage returnPackage = new ReturnPackage();
                returnPackage.state = Block.getStateFromRawId(buf.readInt());
                returnPackage.isOpen = buf.readBoolean();
                return returnPackage;
            }
        };

        @Override
        public Id<? extends CustomPayload> getId() {
            return OPEN_RETURN_ID;
        }
    }
    //#endif

    public static void init(){
        //#if MC > 12004
        PayloadTypeRegistry.playC2S().register(OpenPackage.OPEN_INVENTORY_ID, OpenPackage.CODEC);
        PayloadTypeRegistry.playC2S().register(ReturnPackage.OPEN_RETURN_ID, ReturnPackage.CODEC);
        PayloadTypeRegistry.playC2S().register(HelloPackage.HELLO_REMOTE_INTERACTIONS_ID, HelloPackage.CODEC);
        PayloadTypeRegistry.playS2C().register(OpenPackage.OPEN_INVENTORY_ID, OpenPackage.CODEC);
        PayloadTypeRegistry.playS2C().register(ReturnPackage.OPEN_RETURN_ID, ReturnPackage.CODEC);
        PayloadTypeRegistry.playS2C().register(HelloPackage.HELLO_REMOTE_INTERACTIONS_ID, HelloPackage.CODEC);
        //#endif
    }

    public static void registerReceivePacket() {
        //#if MC > 12004
        ServerPlayNetworking.registerGlobalReceiver(OpenPackage.OPEN_INVENTORY_ID, (payload,context) -> {
            if (payload instanceof OpenPackage packetByteBuf) {
                context.player().server.execute(() -> {
                    openInv(context.player().server, context.player(), packetByteBuf.pos, packetByteBuf.world);
                });
            }
        });
        //#else
        //$$ ServerPlayNetworking.registerGlobalReceiver(OPEN_INVENTORY, (server, player, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {
        //$$     BlockPos pos = packetByteBuf.readBlockPos();
            //#if MC < 11904
            //$$ RegistryKey<World> key = RegistryKey.of(Registry.WORLD_KEY, packetByteBuf.readIdentifier());
            //#else
            //$$ RegistryKey<World> key = RegistryKey.of(RegistryKeys.WORLD, packetByteBuf.readIdentifier());
            //#endif
        //$$     server.execute(() -> openInv(server, player, pos, key));
        //$$ });
        //#endif
    }

    public static void helloRemote(ServerPlayerEntity player) {
        if (!remoteOpenInventory || loadPrinter) return;
        //#if MC > 12004
        ServerPlayNetworking.send(player,new HelloPackage());
        //#else
        //$$ ServerPlayNetworking.send(player, HELLO_REMOTE_INTERACTIONS, new PacketByteBuf(Unpooled.buffer()));
        //#endif
    }

    public static void openInv(MinecraftServer server, ServerPlayerEntity player, BlockPos pos, RegistryKey<World> key) {
        if(!remoteOpenInventory || loadPrinter) return;
        ServerWorld world = server.getWorld(key);
        if (world == null) return;
        BlockState blockState = world.getBlockState(pos);
        if (blockState == null) {
            world.getChunkManager().addTicket(OPEN_TICKET, new ChunkPos(pos), 2, new ChunkPos(pos));
        }
        playerlist.add(player);
        if (blockState == null) return;
        tickMap.put(player, new TickList(blockState.getBlock(), world, pos, blockState));
        BlockEntity blockEntity = world.getBlockEntity(pos);
        boolean isInv = isContainer(blockEntity);

        if (!isInv || blockState.isAir() || (blockEntity instanceof ShulkerBoxBlockEntity entity &&
                //#if MC > 12004
                !world.isSpaceEmpty(ShulkerEntity.calculateBoundingBox(1.0f,blockState.get(FACING),  0.0f,0.5f).offset(pos).contract(1.0E-6)) &&
                //#else
                //$$ !world.isSpaceEmpty(ShulkerEntity.calculateBoundingBox(blockState.get(FACING), 0.0f, 0.5f).offset(pos).contract(1.0E-6)) &&
                //#endif
                entity.getAnimationStage() == ShulkerBoxBlockEntity.AnimationStage.CLOSED)) {
            System.out.println("openFail" + pos);
            openReturn(player, blockState, false);
            return;
        }

        //#if MC > 12004
        ActionResult r = blockState.onUse(world, player, new BlockHitResult(Vec3d.ofCenter(pos), Direction.UP, pos, false));
        //#else
        //$$ ActionResult r = blockState.onUse(world, player, Hand.MAIN_HAND, new BlockHitResult(Vec3d.ofCenter(pos), Direction.UP, pos, false));
        //#endif

        if ((r != null && !r.equals(ActionResult.CONSUME))) {
            System.out.println("openFail" + pos);
            openReturn(player, blockState, false);
            return;
        }
        openReturn(player, blockState, true);
    }
    public static void openReturn(ServerPlayerEntity player, BlockState state, boolean open) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        MyPacket.encode(new MyPacket(state, open), buf);
        //#if MC > 12004
        ReturnPackage returnPackage = new ReturnPackage();
        returnPackage.state = state;
        returnPackage.isOpen = open;
        ServerPlayNetworking.send(player,returnPackage);
        //#else
        //$$ ServerPlayNetworking.send(player, OPEN_RETURN, buf);
        //#endif
    }

    public static boolean isContainer(BlockEntity blockEntity) {
        return blockEntity instanceof Inventory;
//        if(blockEntity == null) return false;
//
//
//        BlockEntityType<?> type = blockEntity.getType();
//        return  type == BlockEntityType.CHEST || type == BlockEntityType.ENDER_CHEST ||
//                type == BlockEntityType.SHULKER_BOX || type == BlockEntityType.BARREL ||
//                type == BlockEntityType.HOPPER || type == BlockEntityType.DISPENSER ||
//                type == BlockEntityType.DROPPER || type == BlockEntityType.BREWING_STAND ||
//                type == BlockEntityType.BLAST_FURNACE || type == BlockEntityType.SMOKER
//                //#if MC > 12002
//                ||
//                type == BlockEntityType.CRAFTER
//                //#endif
//                ;
    }
}
