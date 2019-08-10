package guimaker.commands;

import guimaker.guimaker.Main;
import guimaker.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
        if (isGuiCommand(p, command)) {
            e.setCancelled(true);
            if (main.getGuiStorage().getString(main.getPlayerCommand.get(command) + ".command.permission") != null) {
                if (!(p.hasPermission(main.getGuiStorage().getString("Guis." + p.getUniqueId() + "." + main.getPlayerCommand.get(command) + ".command.permission")))) {
                    p.sendMessage(main.getConfig().getString("no_perm_message"));
                    return;
                }
            }
            openInventory(p, command);
        }
    }

    private boolean isGuiCommand(Player p, String label) {
        if (main.guiStorage.getConfig().getConfigurationSection("Guis") != null) {
            for (String gui : main.guiStorage.getConfig().getConfigurationSection("Guis." + p.getUniqueId()).getKeys(false)) {
                String command = main.getGuiStorage().getString("Guis." + p.getUniqueId() + "." + gui + ".command.name");
                if (("/" + command).equals(label.toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void openInventory(Player p, String label) {
        label = label.replace("/", "");
        System.out.println(label);
        System.out.print(main.getPlayerCommand.get(label));
        Inventory iv = Bukkit.createInventory(null, main.getGuiStorage().getInt(main.getPlayerCommand.get(label) + ".size"), main.getGuiStorage().getString(main.getPlayerCommand.get(label) + ".title"));

        // TODO make items category under player yml
        p.openInventory(iv);
    }
}
