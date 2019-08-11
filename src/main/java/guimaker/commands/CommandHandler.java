package guimaker.commands;

import guimaker.guimaker.Main;
import guimaker.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;

public class CommandHandler implements Listener {

    private Main main;

    public CommandHandler(Main main) {
        this.main = main;
    }


    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        String command = e.getMessage().toLowerCase();
        if (isGuiCommand(command) != null) {
            e.setCancelled(true);
            if (main.getGuiStorage().getString(main.getPlayerCommand.get(command) + ".command.permission") != null) {
                if (!p.hasPermission(main.getGuiStorage().getString(isGuiCommand(command)))) {
                    p.sendMessage(main.getConfig().getString("no_perm_message"));
                    return;
                }
            }
            openInventory(p, command);
        }
    }

    public String getCommandFromString(String s) {
        if (main.guiStorage.getConfig().getConfigurationSection("Guis") != null) {
            for (String uniqueID : main.guiStorage.getConfig().getConfigurationSection("Guis").getKeys(false)) {
                for (String slot : main.guiStorage.getConfig().getConfigurationSection("Guis." + uniqueID).getKeys(false)) {
                    String command = main.getGuiStorage().getString("Guis." + uniqueID + "." + slot + ".command.name");
                    if (command.equals(s.toLowerCase())) {
                        return "Guis." + uniqueID + "." + slot + ".command";
                    }
                }
            }
        }
        return null;
    }

    public String isGuiCommand(String label) {
        if (main.guiStorage.getConfig().getConfigurationSection("Guis") != null) {
            for (String uniqueID : main.guiStorage.getConfig().getConfigurationSection("Guis").getKeys(false)) {
                for (String slot : main.guiStorage.getConfig().getConfigurationSection("Guis." + uniqueID).getKeys(false)) {
                    String command = main.getGuiStorage().getString("Guis." + uniqueID + "." + slot + ".command.name");
                    if (("/" + command).equals(label.toLowerCase())) {
                        return "Guis." + uniqueID + "." + slot + ".command.name";
                    }
                }
            }
        }
        return null;
    }

    private void openInventory(Player p, String label) {
        label = label.replace("/", "");
        Inventory iv = Bukkit.createInventory(null, main.getGuiStorage().getInt(main.getPlayerCommand.get(label) + ".size"), Utils.chat(main.getGuiStorage().getString(main.getPlayerCommand.get(label) + ".title")));

        // TODO make items category under player yml
        p.openInventory(iv);
    }
}
