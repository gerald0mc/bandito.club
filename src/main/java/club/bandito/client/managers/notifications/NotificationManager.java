package club.bandito.client.managers.notifications;

import club.bandito.client.Bandito;
import club.bandito.client.module.Module;
import club.bandito.client.module.mods.hud.mods.notifications.Notifications;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.LinkedList;

public class NotificationManager {
    //New instance of a LinkedList containing the Notification class
    private final LinkedList<Notification> notifications;
    //Instance of the Notifications module
    private final Notifications module;

    public NotificationManager() {
        MinecraftForge.EVENT_BUS.register(this);
        //Create a new instance of the LinkedList
        notifications = new LinkedList<>();
        //Case the Notifications module to Notifications
        this.module = Bandito.getModuleManager().getModule(Notifications.class);
    }

    //Getters for all the notifications
    public LinkedList<Notification> getNotifications() {
        return notifications;
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        //Checks with the nullCheck method, and returns if true
        if (Module.nullCheck()) return;
        //Check if the notifications LinkedList is empty, and returns if true
        if (notifications.isEmpty()) return;
        //Loops through all the notifications in the notifications LinkedList
        for (Notification notification : notifications) {
            //Checks if the notifications startTime is more then the timeActive
            if (System.currentTimeMillis() - notification.getStartTime() >= module.timeActive.getValue() * 1000) {
                //Removes the notification from the LinkedList
                notifications.remove(notification);
                //Breaks out of the loop as well as returning
                return;
            }
        }
    }

    //Method to add a new notification to the LinkedList
    public void addNotification(String message) {
        notifications.add(new Notification(message));
    }
}
