package guimaker.commands;

import guimaker.guimaker.Main;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class CommandRegister extends BukkitCommand { // TODO fix

    CommandHandler cm = new CommandHandler(Main.main);
    private String command;


    public CommandRegister(String cmd) {
        super(cmd);
        this.description = "Access command for GUI";
        this.usageMessage = "/" + cmd;
        command = cmd;
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission(cm.getCommandFromString(command))) {

            }
        }
        return false;
    }
    
}
