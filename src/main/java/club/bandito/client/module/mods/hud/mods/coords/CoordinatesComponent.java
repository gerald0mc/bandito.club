package club.bandito.client.module.mods.hud.mods.coords;

import club.bandito.client.module.mods.hud.HUDComponent;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.math.BlockPos;

public class CoordinatesComponent extends HUDComponent {
    public CoordinatesComponent(int x, int y) {
        super(x, y);
    }

    @Override
    public void doRender() {
        BlockPos playerPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
        String displayCoords = ChatFormatting.GRAY + "XYZ " + ChatFormatting.RESET
                + playerPos.getX() + " "
                + (mc.player.dimension == 0 ? ChatFormatting.RED + "[" + ChatFormatting.GRAY + (playerPos.getX() / 8) + ChatFormatting.RED + "] " + ChatFormatting.RESET : "")
                + playerPos.getY() + " "
                + playerPos.getZ() + " "
                + (mc.player.dimension == 0 ? ChatFormatting.RED + "[" + ChatFormatting.GRAY + (playerPos.getZ() / 8) + ChatFormatting.RED + "] " + ChatFormatting.RESET : "");
        this.width = mc.fontRenderer.getStringWidth(displayCoords);
        mc.fontRenderer.drawStringWithShadow(displayCoords, x, y, color.getRGB());
    }

    @Override
    public int getHeight() {
        return height;
    }
}
