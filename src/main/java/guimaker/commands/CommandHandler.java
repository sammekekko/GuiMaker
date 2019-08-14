package guimaker.commands;

import guimaker.guimaker.Main;
import guimaker.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CommandHandler implements CommandExecutor {

    public void openInventory(Player p, String raw) {
        Inventory iv = Bukkit.createInventory(null, Main.main.getGuiStorage().getInt( raw + ".size"), Utils.chat(Main.main.getGuiStorage().getString(raw + ".title")));

        // TODO make items category under player yml
        p.openInventory(iv);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (Main.main.getPlayerCommand.get(cmd.getName()) == null) {
                p.sendMessage(ChatColor.RED + "Please reload the server for the command to be fully deleted.");
                return false;
            }
            String permission = Main.main.getGuiStorage().getString(Main.main.getPlayerCommand.get(cmd.getName()) + ".command.permission");
            if (permission != null) {
                if (!p.hasPermission(Main.main.getPlayerCommand.get(cmd.getName()) + ".command.permission")) {
                    p.sendMessage(Utils.chat(Main.main.getConfig().getString("no_perm_message")));
                    return false;
                }
            }
            openInventory(p, Main.main.getPlayerCommand.get(cmd.getName()));
        }
        return false;
    }
}
