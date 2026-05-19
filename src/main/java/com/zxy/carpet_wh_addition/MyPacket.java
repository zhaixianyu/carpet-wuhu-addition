package com.zxy.carpet_wh_addition;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.network.FriendlyByteBuf;

public class MyPacket {
    /**
     * Creates a packet byte buf that delegates its operations to the {@code
     * parent} buf.
     * parent the parent, or delegate, buf
     */
    private final BlockState blockState;
    private final boolean isOpen;
    public MyPacket(BlockState blockState, boolean isOpen) {
        this.blockState = blockState;
        this.isOpen = isOpen;
    }

    public BlockState getBlockState(){
        return blockState;
    }
    public boolean getIsOpen(){
        return isOpen;
    }
    // 用于序列化数据以发送给客户端的方法
    public static void encode(MyPacket msg, FriendlyByteBuf buffer) {
        buffer.writeVarInt(Block.getId(msg.blockState));
        buffer.writeBoolean(msg.isOpen);
    }

    // 用于接收客户端数据的方法
    public static MyPacket decode(FriendlyByteBuf buffer) {
        return new MyPacket(Block.stateById(buffer.readVarInt()), buffer.readBoolean());
    }
}
