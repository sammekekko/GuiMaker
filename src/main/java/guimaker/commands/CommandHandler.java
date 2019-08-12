package guimaker.commands;

import guimaker.guimaker.Main;
import guimaker.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;

import java.lang.invoke.ConstantCallSite;

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
            if (isGuiCommand(command) != null) {
                if (!p.hasPermission(main.getGuiStorage().getString(isGuiCommand(command)) + ".permission")) {
                    p.sendMessage(Utils.chat(main.getConfig().getString("no_perm_message")));
                    p.sendMessage(main.getGuiStorage().getString(isGuiCommand(command) + ".permission"));
                    return;
                }
            }
            openInventory(p, command);
        }
    }

    public String isGuiCommand(String label) {
        if (main.guiStorage.getConfig().getConfigurationSection("Guis") != null) {
            for (String uniqueID : main.guiStorage.getConfig().getConfigurationSection("Guis").getKeys(false)) {
                for (String slot : main.guiStorage.getConfig().getConfigurationSection("Guis." + uniqueID).getKeys(false)) {
                    String command = main.getGuiStorage().getString("Guis." + uniqueID + "." + slot + ".command.name");
                    if (("/" + command).equals(label.toLowerCase())) {
                        return "Guis." + uniqueID + "." + slot + ".command";
                    }
                }
            }
        }
        return null;
    }

    public void openInventory(Player p, String cmd) {
        if (cmd.contains("/")) {
            cmd = cmd.replace("/" , "");
        }
        Inventory iv = Bukkit.createInventory(null, main.getGuiStorage().getInt( Main.main.getPlayerCommand.get(cmd) + ".size"), Utils.chat(main.getGuiStorage().getString(Main.main.getPlayerCommand.get(cmd) + ".title")));

        // TODO make items category under player yml
        p.openInventory(iv);
    }
}
