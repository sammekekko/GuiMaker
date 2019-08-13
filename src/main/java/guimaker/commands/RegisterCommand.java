package guimaker.commands;

import guimaker.guimaker.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class RegisterCommand {

    public void registerCommand(String name) {
        PluginCommand cmd = getCommand(name, Main.main);
        getCommandMap().register(Main.main.getDescription().getName(), cmd);
        cmd.setExecutor(new CommandHandler());
    }

//    public String isGuiCommand(String label) {
//        if (Main.main.guiStorage.getConfig().getConfigurationSection("Guis") != null) {
//            for (String uniqueID : Main.main.guiStorage.getConfig().getConfigurationSection("Guis").getKeys(false)) {
//                for (String slot : Main.main.guiStorage.getConfig().getConfigurationSection("Guis." + uniqueID).getKeys(false)) {
//                    String command = Main.main.getGuiStorage().getString("Guis." + uniqueID + "." + slot + ".command.name");
//                    if (("/" + command).equals(label.toLowerCase())) {
//                        return "Guis." + uniqueID + "." + slot + ".command";
//                    }
//                }
//            }
//        }
//        return null;
//    }
//
//    public String getSlotFromString(String s) {
//        if (Main.main.guiStorage.getConfig().getConfigurationSection("Guis") != null) {
//            for (String uniqueID : Main.main.guiStorage.getConfig().getConfigurationSection("Guis").getKeys(false)) {
//                for (String slot : Main.main.guiStorage.getConfig().getConfigurationSection("Guis." + uniqueID).getKeys(false)) {
//                    String command = Main.main.getGuiStorage().getString("Guis." + uniqueID + "." + slot + ".command.name");
//                    if (command != null) {
//                        if (command.equals(s.toLowerCase())) {
//                            return "Guis." + uniqueID + "." + slot;
//                        }
//                    }
//                }
//            }
//        }
//        return null;
//    }

    private static PluginCommand getCommand(String name, Plugin plugin) {
        PluginCommand cmd = null;
        try {
            Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            c.setAccessible(true);

            cmd = c.newInstance(name, plugin);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return cmd;
    }

    private static CommandMap getCommandMap() {
        CommandMap cmap = null;
        try {
            if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
                Field f = SimplePluginManager.class.getDeclaredField("commandMap");
                f.setAccessible(true);
                cmap = (CommandMap) f.get(Bukkit.getPluginManager());
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return cmap;
    }

//    public RegisterCommand(String name) {
//        super(name);
//        this.description = "Access command for GUI";
//        this.setPermission(null);
//    }

//    @Override
//    public boolean execute(CommandSender sender, String alias, String[] args) {
//        if (sender instanceof Player) {
//            Player p = (Player) sender;
//            String perm = Main.main.getGuiStorage().getString(Main.main.getPlayerCommand.get(this.getName()) + ".command.permission");
//            if (perm != null) {
//                if (p.hasPermission(Main.main.getGuiStorage().getString(Main.main.getPlayerCommand.get(this.getName())) + ".command.permission")) {
//                    Main.main.openInventory(p, Main.main.getPlayerCommand.get(this.getName()));
//                } else {
//                    p.sendMessage(Utils.chat(Main.main.getConfig().getString("no_perm_message")));
//                }
//            } else {
//                Main.main.openInventory(p, Main.main.getPlayerCommand.get(this.getName()));
//            }
//        }
//        return false;
//    }
}
