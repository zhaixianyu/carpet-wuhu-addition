package com.zxy.carpet_wh_addition.mixin.openInv;

import com.zxy.carpet_wh_addition.OpenInventoryPacket;
import com.zxy.carpet_wh_addition.TickList;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

import static com.zxy.carpet_wh_addition.OpenInventoryPacket.playerlist;
import static com.zxy.carpet_wh_addition.OpenInventoryPacket.tickMap;

@Mixin(ServerLevel.class)
public class MixinServerLevel {
    @Inject(at = @At("HEAD"),method = "tick")
    public void tick(CallbackInfo ci){
        for (ServerPlayer s : playerlist) {
            TickList list = tickMap.get(s);
            if (!list.world.areEntitiesLoaded(
                    //#if MC > 12111
                    ChunkPos.containing(list.pos).pack()
                    //#else
                    //$$ ChunkPos.asLong(list.pos)
                    //#endif
            )) {
                list.world.shouldTickBlocksAt(list.pos
                        //#if MC < 11902
                        //$$ .asLong()
                        //#endif
                );
            }
//            BlockState state =  list.state;
            BlockState state2 = list.world.getBlockState(list.pos);
            if(state2.isAir()){
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
