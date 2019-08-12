package guimaker.listeners;

import guimaker.guimaker.Main;
import guimaker.interfaces.GuiHandler;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class ConvPromptPerm extends StringPrompt {

    private GuiHandler gh = new GuiHandler();

    @Override
    public Prompt acceptInput(ConversationContext con, String answer) {
        Player p = (Player) con.getForWhom();
        p.getInventory().clear();
        gh.addValueGuiStorage("Guis." + p.getUniqueId() + "." + Main.main.currentSlot.get(p.getUniqueId().toString()) + ".command.permission", answer);
        Main.main.isRenaming.remove(p.getName());
        gh.commandMenu(p);
        return null;
    }

    @Override
    public String getPromptText(ConversationContext con) {
        Player p = (Player) con.getForWhom();
        p.getInventory().clear();
        gh.loadInventory(p);
        return ChatColor.DARK_GRAY + "Enter your permission:";
    }

}