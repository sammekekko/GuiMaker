package guimaker.commands;

import guimaker.interfaces.GuiHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import guimaker.guimaker.Main;
import guimaker.utils.Utils;

public class GuiCommand implements CommandExecutor {

    private Main main;

    public GuiCommand(Main main) {
        this.main = main;
    }

    GuiHandler gh = new GuiHandler();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("GuiManager.guimaker.create")) {
                if (args.length > 0) {
                    // shop new
                    if (args[0].toLowerCase().equals("menu")) {
                        // create main menu for gui maker
                        if (!main.isRenaming.contains(p.getName())) {
                            main.isRemoving.remove(p.getName());
                            gh.saveInventory(p);
                            main.createMainMenu(p);
                        }
                    } else {
                        p.sendMessage(Utils.chat(main.getConfig().getString("arguments_invalid").replace("<label>", label)));
                    }
                }
            } else {
                p.sendMessage(Utils.chat(main.getConfig().getString("no_perm_message")));
            }
        } else {
            sender.sendMessage(Utils.chat(main.getConfig().getString("console_error_message")));
        }
        return false;
    }


}
