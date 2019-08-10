package guimaker.guimaker;

import java.util.ArrayList;
import java.util.HashMap;

import guimaker.commands.CommandHandler;
import guimaker.interfaces.GuiHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import guimaker.commands.GuiCommand;
import guimaker.files.GuiStorage;
import guimaker.listeners.GuiListener;

public class Main extends JavaPlugin {

    GuiHandler gh = new GuiHandler();

    public GuiStorage guiStorage;

    public ArrayList<String> isTransferring = new ArrayList<>();

    public ArrayList<String> isRemoving = new ArrayList<>();

    public ArrayList<String> isRenaming = new ArrayList<>();

    public FileConfiguration getGuiStorage() {
        return guiStorage.getConfig();
    }

    public HashMap<String, Integer> currentSlot = new HashMap<>();

    public HashMap<String, String> getPlayerCommand = new HashMap<>();

    public void createCreateMenu(Player p) {
        gh.createMenu(p);
    }

    public void createMainMenu(Player p) {
        GuiHandler gh = new GuiHandler();
        gh.mainMenu(p);
    }

    private void loadAllCommands() {
        if(guiStorage.getConfig().contains("Guis")) {
            for (String p : guiStorage.getConfig().getConfigurationSection("Guis").getKeys(false)) {
                for (String gui : guiStorage.getConfig().getConfigurationSection("Guis." + p).getKeys(false)) {
                    if (main.guiStorage.getConfig().getString("Guis." + p + "." + gui + ".command.name") != null) { // TODO glitchar andra gången man joinar+
                        String location = "Guis." + p + "." + gui;
                        getPlayerCommand.put(guiStorage.getConfig().getString("Guis." + p + "." + gui + ".command.name"), location);
                    }
                }
            }
        }
    }

    public static Main main;
    @Override
    public void onEnable() {
        saveDefaultConfig();
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        this.guiStorage = new GuiStorage(this);
        getCommand("gui").setExecutor(new GuiCommand(this));
        Bukkit.getPluginManager().registerEvents(new GuiListener(this), this);
        Bukkit.getPluginManager().registerEvents(new CommandHandler(this), this);
        loadAllCommands();
        main = this;
    }

    @Override
    public void onDisable() {
        guiStorage.save();
    }

}
