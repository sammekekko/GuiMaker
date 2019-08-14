package guimaker.listeners;

import guimaker.commands.RegisterCommand;
import guimaker.guimaker.Main;
import guimaker.interfaces.GuiHandler;
import guimaker.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

public class GuiListener implements Listener{

    private Main main;

    private ArrayList<String> db = new ArrayList<>();

    public GuiListener(Main main) {
        this.main = main;
    }

    private GuiHandler gh = new GuiHandler();

    private RegisterCommand rc = new RegisterCommand();

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equals(Utils.chat(main.getConfig().getString("MainMenu.title")))) {
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                e.setCancelled(true);
                final ItemStack item = e.getCurrentItem();
                ItemMeta im = item.getItemMeta();
                if(e.getClick() == ClickType.LEFT) {
                    switch (e.getCurrentItem().getType()) {
                        case ANVIL:
                            // create InterfaceMenu
                            int newSlot = gh.getAllSlots(p) + 1;
                            if (newSlot <= 44) {
                                main.getGuiStorage().set("Guis." + p.getUniqueId() + "." + newSlot + ".title", Utils.chat(main.getConfig().getString("default.title")));
                                main.getGuiStorage().set("Guis." + p.getUniqueId() + "." + newSlot + ".size", main.getConfig().getInt("default.size"));
                                main.guiStorage.save();
                                main.currentSlot.put(p.getUniqueId().toString(), newSlot);
                                main.isTransferring.add(p.getName());
                                main.createCreateMenu(p);
                            } else {
                                // Make Barrier block
                                item.setType(Material.BARRIER);
                                if (!db.contains(p.getName())) {
                                    db.add(p.getName());
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
                                        item.setType(Material.ANVIL);
                                        db.remove(p.getName());
                                    }, 60L);
                                }
                            }
                            break;
                        case CHEST_MINECART:
                            if (main.isRemoving.contains(p.getName())) {
                                main.isTransferring.add(p.getName());
                                final ItemStack currentItem = e.getCurrentItem();
                                final int slot = e.getSlot();
                                e.getInventory().removeItem(currentItem);
                                String location = "Guis." + p.getUniqueId() + "." + slot + ".command.name";
                                if (Main.main.getGuiStorage().getString(location) != null) {
                                   rc.unRegCMD(Main.main.getGuiStorage().getString(location));
                                   Main.main.getPlayerCommand.remove(Main.main.getGuiStorage().getString(location));
                                }
                                main.getGuiStorage().set("Guis." + p.getUniqueId() + "." + slot, null);
                                main.guiStorage.save();
                                gh.sortMainGui(p, e.getInventory());
                            } else {
                                int cSlot = e.getSlot();
                                main.currentSlot.put(p.getUniqueId().toString(), cSlot);
                                main.isTransferring.add(p.getName());
                                p.getInventory().clear();
                                main.createCreateMenu(p);
                            }
                            break;
                        case GREEN_WOOL:
                            ArrayList<String> disabledLore = new ArrayList<>();
                            disabledLore.add(ChatColor.GRAY + "Removing mode is " + ChatColor.RED + "" + ChatColor.BOLD + "disabled");

                            main.isRemoving.remove(p.getName());
                            item.setType(Material.RED_WOOL);
                            im.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD +  "Toggle Remove Mode");
                            im.setLore(disabledLore);
                            item.setItemMeta(im);
                            break;
                        case RED_WOOL:
                            ArrayList<String> enabledLore = new ArrayList<>();
                            enabledLore.add(ChatColor.GRAY + "Removing mode is " + ChatColor.GREEN + "" + ChatColor.BOLD + "enabled");

                            main.isRemoving.add(p.getName());
                            item.setType(Material.GREEN_WOOL);
                            im.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Toggle Remove Mode");
                            im.setLore(enabledLore);
                            item.setItemMeta(im);
                            break;
                        default:
                            break;
                    }
                }
            }
        } else if (e.getView().getTitle().equals(Utils.chat(main.getConfig().getString("MainMenu.title")) + ChatColor.GRAY  + " - " + ChatColor.WHITE + Utils.chat(main.getGuiStorage().getString("Guis." + p.getUniqueId() + "." + main.currentSlot.get(p.getUniqueId().toString()) + ".title")))) {
            if (e.getRawSlot() < e.getInventory().getSize()) {
                if (e.getCurrentItem() == null && e.getRawSlot() != -999) {
                    main.getGuiStorage().set("Guis." + p.getUniqueId() + "." + Main.main.currentSlot.get(p.getUniqueId().toString()) + ".items." + e.getRawSlot() + ".stack", Main.main.getConfig().getString("default.material"));
                    main.getGuiStorage().set("Guis." + p.getUniqueId() + "." + Main.main.currentSlot.get(p.getUniqueId().toString()) + ".items." + e.getRawSlot() + ".name", Main.main.getConfig().getString("default.title"));
                    main.currentItemSlot.put(p.getUniqueId().toString(), e.getRawSlot());
                    main.guiStorage.save();
                    main.isTransferring.add(p.getName());
                    gh.addItemMenu(p);
                }
            }
            if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                e.setCancelled(true);
                if (e.getClick() == ClickType.LEFT) {
                    switch (e.getCurrentItem().getType()) {
                        case NAME_TAG:
                            // change name
                            p.getInventory().clear();
                            // Rename och sedan skriv /gui create så får man det som man har i ivet under renamen
                            p.closeInventory();
                            p.sendTitle(ChatColor.AQUA + "Display Name", ChatColor.GRAY + "Type in the Display Name of the GUI", 5, 40, 5);
                            main.isRenaming.add(p.getName());
                            main.isTransferring.add(p.getName());
                            ConversationFactory cf = new ConversationFactory(main);
                            Conversation conv = cf.withFirstPrompt(new ConvPrompt()).withLocalEcho(true).buildConversation(p);
                            conv.begin();
                            break;
                        case FEATHER:
                            main.isTransferring.add(p.getName());
                            p.getInventory().clear();
                            main.createMainMenu(p);
                            break;
                        case BLAZE_ROD:
                                if (main.getGuiStorage().getInt("Guis." + p.getUniqueId() + "." + main.currentSlot.get(p.getUniqueId().toString()) + ".size") < 54)  {
                                    main.getGuiStorage().set("Guis." + p.getUniqueId() + "." + main.currentSlot.get(p.getUniqueId().toString()) + ".size", main.getGuiStorage().getInt("Guis." + p.getUniqueId() + "." + main.currentSlot.get(p.getUniqueId().toString()) + ".size") + 9);
                                    main.guiStorage.save();
                                    main.isTransferring.add(p.getName());
                                    p.getInventory().clear();
                                    main.createCreateMenu(p);
                                } else {
                                    final ItemStack item = e.getCurrentItem();
                                    item.setType(Material.BARRIER);
                                    if (!db.contains(p.getName())) {
                                        db.add(p.getName());
                                        Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
                                            item.setType(Material.BLAZE_ROD);
                                            db.remove(p.getName());
                                        }, 60L);
                                    }
                                }
                            break;
                        case STICK:
                                if (main.getGuiStorage().getInt("Guis." + p.getUniqueId() + "." +  main.currentSlot.get(p.getUniqueId().toString()) + ".size") > 9) {
                                    main.getGuiStorage().set("Guis." + p.getUniqueId() + "." + main.currentSlot.get(p.getUniqueId().toString()) + ".size", main.getGuiStorage().getInt("Guis." + p.getUniqueId() + "." + main.currentSlot.get(p.getUniqueId().toString()) + ".size") - 9);
                                    main.guiStorage.save();
                                    main.isTransferring.add(p.getName());
                                    p.getInventory().clear();
                                    main.createCreateMenu(p);
                                } else {
                                    final ItemStack item = e.getCurrentItem();
                                    item.setType(Material.BARRIER);
                                    if (!db.contains(p.getName())) {
                                        db.add(p.getName());
                                        Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
                                            item.setType(Material.STICK);
                                            db.remove(p.getName());
                                        }, 60L);
                                    }
                                }
                            break;
                        case REDSTONE_TORCH:
                                main.isTransferring.add(p.getName());
                                p.getInventory().clear();
                                gh.commandMenu(p);
                            break;
                        default:
                            break;
                    }
                }
            }
        } else if (e.getView().getTitle().equals(Utils.chat(ChatColor.WHITE + Main.main.getGuiStorage().getString("Guis." + p.getUniqueId() + "." + Main.main.currentSlot.get(p.getUniqueId().toString()) + ".title") + ChatColor.GRAY + " - " + ChatColor.RED + "" + ChatColor.BOLD + "Command Menu"))) {
            if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                e.setCancelled(true);
                if (e.getClick() == ClickType.LEFT) {
                    switch (e.getCurrentItem().getType()) {
                        case NAME_TAG:
                            p.getInventory().clear();
                            p.closeInventory();
                            p.sendTitle(ChatColor.AQUA + "Enter Command", ChatColor.GRAY + "Type in the command without the " + ChatColor.AQUA + "/", 5, 40, 5);
                            main.isRenaming.add(p.getName());
                            main.isTransferring.add(p.getName());
                            ConversationFactory cf = new ConversationFactory(main);
                            Conversation conv = cf.withFirstPrompt(new ConvPromptCMD()).withLocalEcho(true).buildConversation(p);
                            conv.begin();
                            break;
                        case REPEATER:
                            p.getInventory().clear();
                            p.closeInventory();
                            p.sendTitle(ChatColor.AQUA + "Enter Permission", ChatColor.GRAY + "Type in the permission", 5, 40, 5);
                            main.isRenaming.add(p.getName());
                            main.isTransferring.add(p.getName());
                            ConversationFactory cf2 = new ConversationFactory(main);
                            Conversation conv2 = cf2.withFirstPrompt(new ConvPromptPerm()).withLocalEcho(true).buildConversation(p);
                            conv2.begin();
                            break;
                        case FEATHER:
                            main.isTransferring.add(p.getName());
                            p.getInventory().clear();
                            main.createCreateMenu(p);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player p = Bukkit.getPlayerExact(e.getPlayer().getName());
        if (p != null) {
            if (e.getView().getTitle().equals(Utils.chat(main.getConfig().getString("MainMenu.title")) + ChatColor.GRAY  + " - " + ChatColor.WHITE + Utils.chat(main.getGuiStorage().getString("Guis." + p.getUniqueId() + "." + main.currentSlot.get(p.getUniqueId().toString()) + ".title"))) || e.getView().getTitle().equals(Utils.chat(main.getConfig().getString("MainMenu.title"))) || e.getView().getTitle().equals(Utils.chat(ChatColor.WHITE + Main.main.getGuiStorage().getString("Guis." + p.getUniqueId() + "." + Main.main.currentSlot.get(p.getUniqueId().toString()) + ".title") + ChatColor.GRAY + " - " + ChatColor.RED + "" + ChatColor.BOLD + "Command Menu"))) {
                if (!main.isTransferring.contains(p.getName())) {
                    p.getInventory().clear();
                    gh.loadInventory(p);
                }
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        main.isRenaming.remove(p.getName());
        main.isTransferring.remove (p.getName());
        main.isRemoving.remove(p.getName());
    }

}
