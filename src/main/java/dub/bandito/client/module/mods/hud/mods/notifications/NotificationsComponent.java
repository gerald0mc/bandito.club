package club.bandito.client.module.mods.hud.mods.notifications;

import club.bandito.client.Bandito;
import club.bandito.client.managers.notifications.Notification;
import club.bandito.client.module.mods.hud.HUDComponent;
import club.bandito.client.util.MessageUtil;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.LinkedList;

public class NotificationsComponent extends HUDComponent {
    public NotificationsComponent(int x, int y) {
        super(x, y);
    }

    @Override
    public void doRender() {
        int yOffset = 0;
        if (Bandito.getNotificationManager().getNotifications().isEmpty()) return;
        LinkedList<String> strings = new LinkedList<>();
        for (Notification notification : Bandito.getNotificationManager().getNotifications()) {
            int width = mc.fontRenderer.getStringWidth(notification.getMessage());
            strings.add(notification.getMessage());
            Gui.drawRect(x - 1, y + yOffset - 1, x + width + 1, y + yOffset + mc.fontRenderer.FONT_HEIGHT + 1, new Color(30, 30, 30, 200).getRGB());
            mc.fontRenderer.drawStringWithShadow(notification.getMessage(), x, y + yOffset + 1, -1);
            yOffset += mc.fontRenderer.FONT_HEIGHT + 3;
        }
        this.width = MessageUtil.getLongestWordInt(strings);
        this.height = yOffset == 0 ? mc.fontRenderer.FONT_HEIGHT : yOffset;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
