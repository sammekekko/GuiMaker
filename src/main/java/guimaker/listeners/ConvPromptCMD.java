package guimaker.listeners;

import guimaker.commands.RegisterCommand;
import guimaker.guimaker.Main;
import guimaker.interfaces.GuiHandler;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class ConvPromptCMD extends StringPrompt {

    private GuiHandler gh = new GuiHandler();

    private RegisterCommand rc = new RegisterCommand();

    @Override
    public Prompt acceptInput(ConversationContext con, String answer) {
        Player p = (Player) con.getForWhom();
        p.getInventory().clear();

        final String oldName = Main.main.getGuiStorage().getString("Guis." + p.getUniqueId() + "." + Main.main.currentSlot.get(p.getUniqueId().toString()) + ".command.name");
        if (Main.main.getPlayerCommand.get(oldName) != null) {
            rc.unRegCMD(Main.main.getPlayerCommand.get(oldName));
            Main.main.getPlayerCommand.remove(oldName);
        }

        if (Main.main.getPlayerCommand.get(answer.toLowerCase()) != null) {
            rc.unRegCMD(Main.main.getGuiStorage().getString(Main.main.getPlayerCommand.get(answer.toLowerCase())));
            Main.main.guiStorage.getConfig().set(Main.main.getPlayerCommand.get(answer.toLowerCase()) + ".command.name", null);
            Main.main.getPlayerCommand.remove(answer.toLowerCase());
        }

        gh.addValueGuiStorage("Guis." + p.getUniqueId() + "." + Main.main.currentSlot.get(p.getUniqueId().toString()) + ".command.name", answer.toLowerCase());
        rc.registerCommand(answer.toLowerCase());

        Main.main.isRenaming.remove(p.getName());
        Main.main.getPlayerCommand.put(answer.toLowerCase(), "Guis." + p.getUniqueId() + "." + Main.main.currentSlot.get(p.getUniqueId().toString()));

        gh.commandMenu(p);
        return null;
    }

    @Override
    public String getPromptText(ConversationContext con) {
        Player p = (Player) con.getForWhom();
        p.getInventory().clear();
        gh.loadInventory(p);
        return ChatColor.DARK_GRAY + "Enter your command without " + ChatColor.AQUA + "/";
    }

}