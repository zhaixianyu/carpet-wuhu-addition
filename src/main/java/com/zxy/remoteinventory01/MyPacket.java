package com.zxy.remoteinventory01;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketByteBuf;

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
    public static void encode(MyPacket msg, PacketByteBuf buffer) {
        buffer.writeVarInt(Block.getRawIdFromState(msg.blockState));
        buffer.writeBoolean(msg.isOpen);
    }

    // 用于接收客户端数据的方法
    public static MyPacket decode(PacketByteBuf buffer) {
        return new MyPacket(Block.getStateFromRawId(buffer.readVarInt()), buffer.readBoolean());
    }
}
