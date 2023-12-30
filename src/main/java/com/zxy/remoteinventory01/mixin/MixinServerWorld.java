package com.zxy.remoteinventory01.mixin;

import com.zxy.remoteinventory01.OpenInventoryPacket;
import com.zxy.remoteinventory01.TickList;
import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.zxy.remoteinventory01.OpenInventoryPacket.playerlist;
import static com.zxy.remoteinventory01.OpenInventoryPacket.tickMap;

@Mixin(ServerWorld.class)
public class MixinServerWorld {
    @Inject(at = @At("HEAD"),method = "tick")
    public void tick(CallbackInfo ci){
        for (ServerPlayerEntity s : playerlist) {
            TickList list = tickMap.get(s);
            if (!list.world.isChunkLoaded(ChunkPos.toLong(list.pos))) {
                list.world.shouldTick(list.pos);
            }
            BlockState state =  list.state;
            BlockState state2 = list.world.getBlockState(list.pos);
            if(!state.equals(state2)){
                OpenInventoryPacket.openReturn(s,state2,false);
            }
        }
//        for (ServerPlayerEntity s : playerlist) {
//            TickList list = OpenInventoryPacket.tickmap.get(s);
//            list.block.scheduledTick(list.state,list.world,list.pos,list.world.random);
//            BlockState state =  list.state;
//            BlockState state2 = list.world.getBlockState(list.pos);
//            if(!state.equals(state2)){
//                OpenInventoryPacket.openFail(s);
//            }
////            if(list.world.getBlockState(list.pos).equals(list.state)) s.closeHandledScreen();
//        }
    }
}
