package club.bandito.client.module.mods.misc;

import club.bandito.client.module.Module;
import club.bandito.client.module.setting.FloatSetting;
import club.bandito.client.util.Timer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Reminders extends Module {
    public Reminders() {
        super("Reminders", "Shows reminders on the screen.", Category.MISC);
    }

    public static final FloatSetting renderTime = new FloatSetting("RenderTime", 30, 1, 60);

    public final ConcurrentHashMap<Reminder, Long> reminders = new ConcurrentHashMap<>();

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if (nullCheck()) return;
        if (reminders.isEmpty()) return;
        int yOffset = 2;
        for (Map.Entry<Reminder, Long> entry : reminders.entrySet()) {
            Reminder reminder = entry.getKey();
            if (reminder.shouldDelete()) {
                reminders.remove(entry.getKey(), entry.getValue());
                return;
            }
            ScaledResolution sr = new ScaledResolution(mc);
            if (reminder.shouldRender()) {
                mc.fontRenderer.drawStringWithShadow(reminder.getMessage(), sr.getScaledWidth() / 2f - mc.fontRenderer.getStringWidth(reminder.getMessage()) / 2f, yOffset, -1);
                yOffset += mc.fontRenderer.FONT_HEIGHT + 2;
            }
        }
    }

    public static class Reminder {
        private final String message;
        private final float minutesUntilAct;
        private final Timer timer;
        private boolean rendering = false;

        public Reminder(String message, float minutes) {
            this.message = message;
            this.minutesUntilAct = minutes;
            this.timer = new Timer();
        }

        public String getMessage() {
            return message;
        }

        public float getMinutesUntilAct() {
            return minutesUntilAct;
        }

        public Timer getTimer() {
            return timer;
        }

        public boolean shouldRender() {
            if (timer.passed((long) (minutesUntilAct * 60000L)) && !rendering) {
                this.rendering = true;
                this.timer.reset();
            } else return rendering;
            return false;
        }

        public boolean shouldDelete() {
            return rendering && timer.passed((long) (Reminders.renderTime.getValue() * 1000));
        }
    }
}
