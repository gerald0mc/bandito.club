package club.bandito.client.util;

import net.minecraft.client.Minecraft;

import java.io.File;

public class FileUtil {
    public static boolean hasBaritoneJar() {
        final File modFolder = new File(Minecraft.getMinecraft().gameDir, "mods");
        if (!modFolder.exists())
            return false;
        final File[] filesArray = modFolder.listFiles();
        if (filesArray == null || filesArray.length == 0)
            return false;
        for (File file : filesArray) {
            if (file.isDirectory() && file.getName().contains("1.12.2")) {
                final File walkFolder = new File(modFolder, "1.12.2");
                final File[] walkArray = walkFolder.listFiles();
                if (walkArray != null && walkArray.length != 0) {
                    for (File f : walkArray) {
                        if (!f.isFile())
                            continue;
                        if (f.getName().contains("baritone") || f.getName().contains("Baritone")) {
                            return true;
                        }
                    }
                }
            }
            if (file.getName().contains("baritone") || file.getName().contains("Baritone")) {
                return true;
            }
        }
        return false;
    }
}
