package guimaker.utils;

import org.bukkit.ChatColor;

public class Utils {

    public static String chat(String s) {
        if (s == null) {
            return null;
        }
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
