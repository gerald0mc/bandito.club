package club.bandito.client.util;

import net.minecraft.util.math.BlockPos;

import java.util.LinkedList;
import java.util.List;

public class WorldUtil {
    public static List<BlockPos> getSphere(BlockPos middlePos, int range) {
        List<BlockPos> blockPosList = new LinkedList<>();
        int x2 = middlePos.getX();
        int y2 = middlePos.getY();
        int z2 = middlePos.getZ();
        for (int x = x2 - range; x <= x2 + range; x++) {
            for (int z = z2 - range; z <= z2 + range; z++) {
                for (int y = y2 - range; y < y2 + range; y++) {
                    double dist = (x2 - x) * (x2 - x) + (z2 - z) * (z2 - z) + (y2 - y) * (y2 - y);
                    if (dist < range * range) {
                        blockPosList.add(new BlockPos(x, y, z));
                    }
                }
            }
        }
        return blockPosList;
    }
}
