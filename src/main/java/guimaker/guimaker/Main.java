package guimaker.guimaker;

import guimaker.commands.GuiCommand;
import guimaker.commands.RegisterCommand;
import guimaker.files.GuiStorage;
import guimaker.interfaces.GuiHandler;
import guimaker.listeners.GuiListener;
import guimaker.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends JavaPlugin {

    GuiHandler gh = new GuiHandler();

    RegisterCommand rc = new RegisterCommand();

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
        gh.mainMenu(p);
    }

//    public void registerPlayerCommand(String cmd) {
//        try {
//            final Field bcm = Bukkit.getServer().getClass().getDeclaredField("commandMap");
//            bcm.setAccessible(true);
//            CommandMap cm = (CommandMap) bcm.get(Bukkit.getServer());
//            cm.register(cmd, new RegisterCommand(cmd));
//        } catch(IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
//    }

    private void loadAllCommands() {
        if(guiStorage.getConfig().contains("Guis")) {
            for (String p : guiStorage.getConfig().getConfigurationSection("Guis").getKeys(false)) {
                for (String gui : guiStorage.getConfig().getConfigurationSection("Guis." + p).getKeys(false)) {
                    if (guiStorage.getConfig().contains("Guis." + p + "." + gui + ".command.name")) {
                        String location = "Guis." + p + "." + gui;
                        String cmd = guiStorage.getConfig().getString("Guis." + p + "." + gui + ".command.name");
                        getPlayerCommand.put(cmd, location);
                        rc.registerCommand(cmd);
                    }
                }
            }
        }
    }

    public static Main main;

    @Override
    public void onEnable() {
        main = this;
        saveDefaultConfig();
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        this.guiStorage = new GuiStorage(this);
        getCommand("gui").setExecutor(new GuiCommand(this));
        Bukkit.getPluginManager().registerEvents(new GuiListener(this), this);
        try {
            loadAllCommands();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        guiStorage.save();
    }

}
