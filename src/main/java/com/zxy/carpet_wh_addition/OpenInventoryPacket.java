package com.zxy.carpet_wh_addition;

import com.zxy.carpet_wh_addition.mixin.openInv.ShulkerBoxBlockAccessor;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.Container;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import static com.zxy.carpet_wh_addition.CarpetWuHuAddition.loadPrinter;
import static com.zxy.carpet_wh_addition.config.CarpetWuHuSettings.remoteOpenInventory;
import static net.minecraft.world.level.block.ShulkerBoxBlock.FACING;
//#if MC > 12004
import net.minecraft.world.level.block.Block;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

//#endif

//#if MC > 11802
import net.minecraft.core.registries.Registries;
//#else
//$$
//#endif
public class OpenInventoryPacket {
    //#if MC > 12104
    private static final TicketType OPEN_TICKET = TicketType.UNKNOWN;
    //#else
    //$$ private static final TicketType<ChunkPos> OPEN_TICKET = TicketType.create("openInv", Comparator.comparingLong(ChunkPos::toLong), 2);
    //#endif
    public static HashMap<ServerPlayer, TickList> tickMap = new HashMap<>();

    //#if MC > 12006
    private static final Identifier OPEN_INVENTORY = Identifier.fromNamespaceAndPath("remoteinventory", "open_inventory");
    private static final Identifier OPEN_RETURN = Identifier.fromNamespaceAndPath("openreturn", "open_return");
    private static final Identifier HELLO_REMOTE_INTERACTIONS = Identifier.fromNamespaceAndPath("hello", "hello_remote_interactions");
    //#else
    //$$ private static final ResourceLocation OPEN_INVENTORY = new ResourceLocation("remoteinventory", "open_inventory");
    //$$ private static final ResourceLocation OPEN_RETURN = new ResourceLocation("openreturn", "open_return");
    //$$ private static final ResourceLocation HELLO_REMOTE_INTERACTIONS = new ResourceLocation("hello", "hello_remote_interactions");
    //#endif
    public static ArrayList<ServerPlayer> playerlist = new ArrayList<>();

    //#if MC > 12004
    public static class OpenPackage implements CustomPacketPayload {
       public static final Type<OpenPackage> OPEN_INVENTORY_ID = new Type<>(OPEN_INVENTORY);
       public static final StreamCodec<RegistryFriendlyByteBuf,OpenPackage> CODEC = new StreamCodec<>() {

           @Override
           public void encode(RegistryFriendlyByteBuf buf, OpenPackage value) {
               buf.writeResourceKey(value.world);
               buf.writeBlockPos(value.pos);
           }
           @Override
           public OpenPackage decode(RegistryFriendlyByteBuf buf) {
               OpenPackage openPackage = new OpenPackage();
               openPackage.world = buf.readResourceKey(Registries.DIMENSION);
               openPackage.pos = buf.readBlockPos();
               return openPackage;
           }
       };
       ResourceKey<Level> world = null;
       BlockPos pos = null;
       public OpenPackage() {
       }
        @Override
        public Type<? extends CustomPacketPayload> type() {
            return OPEN_INVENTORY_ID;
        }
    }

    public static class HelloPackage implements CustomPacketPayload {
        public static final Type<HelloPackage> HELLO_REMOTE_INTERACTIONS_ID = new Type<>(HELLO_REMOTE_INTERACTIONS);
        public static final StreamCodec<RegistryFriendlyByteBuf,HelloPackage> CODEC = new StreamCodec<>() {
            @Override
            public void encode(RegistryFriendlyByteBuf buf, HelloPackage value) {
            }
            @Override
            public HelloPackage decode(RegistryFriendlyByteBuf buf) {
                return new HelloPackage();
            }
        };
        @Override
        public Type<? extends CustomPacketPayload> type() {
            return HELLO_REMOTE_INTERACTIONS_ID;
        }
    }
    public static class ReturnPackage implements CustomPacketPayload {
        BlockState state = null;
        boolean isOpen = false;
        public static final Type<ReturnPackage> OPEN_RETURN_ID = new Type<>(OPEN_RETURN);
        public static final StreamCodec<RegistryFriendlyByteBuf,ReturnPackage> CODEC = new StreamCodec<>() {
            @Override
            public void encode(RegistryFriendlyByteBuf buf, ReturnPackage value) {
                buf.writeInt(Block.getId(value.state));
                buf.writeBoolean(value.isOpen);
            }
            @Override
            public ReturnPackage decode(RegistryFriendlyByteBuf buf) {
                ReturnPackage returnPackage = new ReturnPackage();
                returnPackage.state = Block.stateById(buf.readInt());
                returnPackage.isOpen = buf.readBoolean();
                return returnPackage;
            }
        };

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return OPEN_RETURN_ID;
        }
    }
    //#endif

    public static void init(){
        //#if MC > 12004
        PayloadTypeRegistry.clientboundPlay().register(OpenPackage.OPEN_INVENTORY_ID, OpenPackage.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(ReturnPackage.OPEN_RETURN_ID, ReturnPackage.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(HelloPackage.HELLO_REMOTE_INTERACTIONS_ID, HelloPackage.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(OpenPackage.OPEN_INVENTORY_ID, OpenPackage.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(ReturnPackage.OPEN_RETURN_ID, ReturnPackage.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(HelloPackage.HELLO_REMOTE_INTERACTIONS_ID, HelloPackage.CODEC);
        //#endif
    }

    public static void registerReceivePacket() {
        //#if MC > 12004
        ServerPlayNetworking.registerGlobalReceiver(OpenPackage.OPEN_INVENTORY_ID, (payload,context) -> {
            if (payload instanceof OpenPackage packetByteBuf) {
                MinecraftServer server =
                        //#if MC > 12106
                        context.server();
                        //#else
                        //$$ context.player().getServer();
                        //#endif
                server.execute(() -> {
                    openInv(server, context.player(), packetByteBuf.pos, packetByteBuf.world);
                });
            }
        });
        //#else
        //$$ ServerPlayNetworking.registerGlobalReceiver(OPEN_INVENTORY, (server, player, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {
        //$$     BlockPos pos = packetByteBuf.readBlockPos();
            //#if MC < 11904
            //$$ ResourceKey<Level> key = ResourceKey.create(Registry.DIMENSION_REGISTRY, packetByteBuf.readResourceLocation());
            //#else
            //$$ ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, packetByteBuf.readResourceLocation());
            //#endif
        //$$     server.execute(() -> openInv(server, player, pos, key));
        //$$ });
        //#endif
    }

    public static void helloRemote(ServerPlayer player) {
        if (!remoteOpenInventory || loadPrinter) return;
        //#if MC > 12004
        ServerPlayNetworking.send(player,new HelloPackage());
        //#else
        //$$ ServerPlayNetworking.send(player, HELLO_REMOTE_INTERACTIONS, new FriendlyByteBuf(Unpooled.buffer()));
        //#endif
    }

    public static void openInv(MinecraftServer server, ServerPlayer player, BlockPos pos, ResourceKey<Level> key) {
        if(!remoteOpenInventory || loadPrinter) return;
        ServerLevel world = server.getLevel(key);
        if (world == null) return;
        BlockState blockState = world.getBlockState(pos);
        if (blockState == null) {
            //#if MC > 12104
                //#if MC >= 260100
                world.getChunkSource().addTicketWithRadius(OPEN_TICKET, ChunkPos.containing(pos), 2);
                //#else
                //$$ world.getChunkSource().addTicketWithRadius(OPEN_TICKET, new ChunkPos(pos), 2);
                //#endif
            //#else
            //$$ world.getChunkSource().addRegionTicket(OPEN_TICKET, new ChunkPos(pos), 2, new ChunkPos(pos));
            //#endif
        }
        playerlist.add(player);
        if (blockState == null) return;
        tickMap.put(player, new TickList(blockState.getBlock(), world, pos, blockState));
        if (!canOpenInv(world,pos)) {
            System.out.println("openFail  " + pos);
            openReturn(player, blockState, false);
            return;
        }

        //#if MC > 12004
        InteractionResult r = blockState.useWithoutItem(world, player, new BlockHitResult(Vec3.atCenterOf(pos), Direction.UP, pos, false));
        //#else
        //$$ InteractionResult r = blockState.use(world, player, InteractionHand.MAIN_HAND, new BlockHitResult(Vec3.atCenterOf(pos), Direction.UP, pos, false));
        //#endif

        if (r != null && (!r.equals(InteractionResult.CONSUME)
            //#if MC > 12101
            && !r.equals(InteractionResult.SUCCESS)
            //#endif
        )) {
            System.out.println("openFail  " + pos);
            openReturn(player, blockState, false);
            return;
        }
        openReturn(player, blockState, true);
    }
    public static void openReturn(ServerPlayer player, BlockState state, boolean open) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
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
        return blockEntity instanceof Container;
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

    public static boolean canOpenInv(Level world, BlockPos pos){
        if (world != null) {
            BlockState blockState = world.getBlockState(pos);
            BlockEntity blockEntity = world.getBlockEntity(pos);
            boolean isInventory = isContainer(blockEntity);
            try {
                if ((isInventory && blockState.getMenuProvider(world,pos) == null) ||
                        (blockEntity instanceof ShulkerBoxBlockEntity entity &&
                                !ShulkerBoxBlockAccessor.canOpen(blockState,world,pos,entity))) {
                    return false;
                }else if(!isInventory){
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
            return true;
        }else {
            return false;
        }
    }
}
