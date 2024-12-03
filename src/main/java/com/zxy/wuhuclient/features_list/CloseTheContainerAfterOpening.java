//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zxy.wuhuclient.features_list;

import com.zxy.wuhuclient.Utils.ZxyUtils;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class CloseTheContainerAfterOpening {
    static LinkedList<BlockPos> pos = new LinkedList();
    public static int step = 0;

    public CloseTheContainerAfterOpening() {
    }

    private static void addPos() {
        LinkedList<BlockPos> blockPos = ZxyUtils.siftBlock("-inventory");
        pos.addAll(blockPos);
    }

    public static void start() {
        if (step == 0) {
            SyncInventory.getReadyColor();
            addPos();
            SyncInventory.highlightPosList.addAll(pos);
            step = 1;
        } else {
            step = 0;
            LinkedList var10000 = pos;
            Set var10001 = SyncInventory.highlightPosList;
            Objects.requireNonNull(var10001);
            var10000.forEach(var10001::remove);
            pos.clear();
        }

    }

    public static void step() {
        if (pos != null) {
            switch (step) {
                case 1:
                    for(BlockPos po : pos) {
                        if (SyncInventory.openInv(po, false)) {
                            SyncInventory.highlightPosList.remove(po);
                            pos.remove(po);
                            step = 2;
                            break;
                        }
                    }

                    ZxyUtils.actionBar("剩余 " + pos.size() + " 个需要打开的容器，再次按下快捷键取消");
                    break;
                case 2:
                    ClientPlayerEntity player = Synthesis.client.player;
                    if (player != null) {
                        player.closeHandledScreen();
                    }

                    step = 1;
                    if (pos.isEmpty()) {
                        step = 0;
                    }
            }

        }
    }
}
