package guimaker.listeners;

import guimaker.commands.CommandHandler;
import guimaker.guimaker.Main;
import guimaker.interfaces.GuiHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class ConvPromptCMD extends StringPrompt {

    private GuiHandler gh = new GuiHandler();

    @Override
    public Prompt acceptInput(ConversationContext con, String answer) {
        Player p = (Player) con.getForWhom();
        p.getInventory().clear();
        if (Main.main.getPlayerCommand.get(answer.toLowerCase()) != null) {
            Main.main.guiStorage.getConfig().set(Main.main.getPlayerCommand.get(answer.toLowerCase()), null);
        }
        gh.addValueGuiStorage("Guis." + p.getUniqueId() + "." + Main.main.currentSlot.get(p.getUniqueId().toString()) + ".command.name", answer);
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