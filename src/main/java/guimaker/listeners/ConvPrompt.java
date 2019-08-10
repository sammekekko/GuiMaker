package guimaker.listeners;

import guimaker.interfaces.GuiHandler;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import guimaker.guimaker.Main;

public class ConvPrompt extends StringPrompt {

    private GuiHandler gh = new GuiHandler();

    @Override
    public Prompt acceptInput(ConversationContext con, String answer) {
        Player p = (Player) con.getForWhom();
        p.getInventory().clear();
        gh.addValueGuiStorage("Guis." + p.getUniqueId() + "." + Main.main.currentSlot.get(p.getUniqueId().toString())  + ".title", answer);
        Main.main.createCreateMenu((Player) con.getForWhom());
        Main.main.isRenaming.remove(p.getName());
        return null;
    }

    @Override
    public String getPromptText(ConversationContext con) {
        Player p = (Player) con.getForWhom();
        p.getInventory().clear();
        gh.loadInventory(p);
        return ChatColor.DARK_GRAY + "Enter your new " + ChatColor.AQUA +  "Display Name:";
    }

}
