package guimaker.interfaces;

import guimaker.guimaker.Main;
import guimaker.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class GuiHandler {

    public void mainMenu(Player p) {
        Inventory MainMenu = Bukkit.createInventory(null, 54, Utils.chat(Main.main.getConfig().getString("MainMenu.title")));
        p.getInventory().clear();


        // Lores

        // Add Gui
        ArrayList<String> createLore = new ArrayList<>();
        createLore.add(ChatColor.AQUA + "Left Click" + ChatColor.DARK_GRAY + " to create");
        createLore.add(ChatColor.DARK_GRAY + "an interface");

        // Enabled Removing Lore
        ArrayList<String> enabledLore = new ArrayList<>();
        enabledLore.add(ChatColor.AQUA + "Left Click" + ChatColor.DARK_GRAY + " to disable");
        enabledLore.add(ChatColor.DARK_GRAY + "removing mode");

        // Disabled Removing Lore
        ArrayList<String> disabledLore = new ArrayList<>();
        disabledLore.add(ChatColor.AQUA + "Left Click" + ChatColor.DARK_GRAY + " to enable");
        disabledLore.add(ChatColor.DARK_GRAY + "removing mode");

        // Add Gui
        ItemStack create = new ItemStack(Material.ANVIL);
        ItemMeta createMeta = create.getItemMeta();
        createMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Create");
        createMeta.setLore(createLore);
        create.setItemMeta(createMeta);

        // Enabled Removing
        ItemStack enabled = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta enabledMeta = enabled.getItemMeta();
        enabledMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Disable Remove Mode");
        enabledMeta.setLore(enabledLore);
        enabled.setItemMeta(enabledMeta);

        // Disabled Removing
        ItemStack disabled = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta disabledMeta = disabled.getItemMeta();
        disabledMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Enable Remove Mode");
        disabledMeta.setLore(enabledLore);
        disabled.setItemMeta(disabledMeta);


        int count = 0;


        if (Main.main.guiStorage.getConfig().getConfigurationSection("Guis." + p.getUniqueId()) != null) {
            for (String key : Main.main.guiStorage.getConfig().getConfigurationSection("Guis." + p.getUniqueId()).getKeys(false)) {
                ItemStack template = new ItemStack(Material.CHEST_MINECART);

                ItemMeta templateMeta = template.getItemMeta();
                templateMeta.setDisplayName(Utils.chat(Main.main.guiStorage.getConfig().getString("Guis." + p.getUniqueId() + "." + key + ".title")));
                template.setItemMeta(templateMeta);
                MainMenu.setItem(count, template);

                Main.main.guiStorage.save();

                count++;
            }
        }
        if (Main.main.isRemoving.contains(p.getName())) {
            MainMenu.setItem(48, enabled);
        } else {
            MainMenu.setItem(48, disabled);
        }
        MainMenu.setItem(50, create);

        p.openInventory(MainMenu);

        Main.main.isTransferring.remove(p.getName());
    }

    public void commandMenu(Player p) {
        Inventory cmdMenu = Bukkit.createInventory(null, 9, Utils.chat(Main.main.getGuiStorage().getString("Guis." + p.getUniqueId() + "." + Main.main.currentSlot.get(p.getUniqueId().toString()) + ".title") + ChatColor.DARK_GRAY + " - " + ChatColor.RED + "" + ChatColor.BOLD + "Command Menu"));
        Inventory plrInv = p.getInventory();
        plrInv.clear();
        // Lores

        // Rename Command
        ArrayList<String> renameLore = new ArrayList<>();
        renameLore.add(ChatColor.AQUA + "Left Click" + ChatColor.DARK_GRAY + " to " + ChatColor.AQUA + "set " + ChatColor.DARK_GRAY +  "your");
        renameLore.add(ChatColor.DARK_GRAY + "command for this GUI");

        // Back
        ArrayList<String> backLore = new ArrayList<>();
        backLore.add(ChatColor.AQUA + "Left Click" + ChatColor.DARK_GRAY + " to");
        backLore.add(ChatColor.DARK_GRAY + "go back");

        ItemStack rename = new ItemStack(Material.NAME_TAG);
        ItemMeta renameMeta = rename.getItemMeta();
        renameMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Set Command");
        renameMeta.setLore(renameLore);
        rename.setItemMeta(renameMeta);

        ItemStack back = new ItemStack(Material.FEATHER);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Back");
        backMeta.setLore(backLore);
        back.setItemMeta(backMeta);

        p.openInventory(cmdMenu);

        cmdMenu.setItem(8, rename);
        plrInv.setItem(27, back);

        Main.main.isTransferring.remove(p.getName());
    }

    public void createMenu(Player p) {
        Inventory createMenu = Bukkit.createInventory(null, Main.main.getGuiStorage().getInt("Guis." + p.getUniqueId() + "." + Main.main.currentSlot.get(p.getUniqueId().toString()) + ".size"), Utils.chat(Main.main.getConfig().getString("MainMenu.title")) + ChatColor.DARK_GRAY  + " - " + Utils.chat(Main.main.getGuiStorage().getString("Guis." + p.getUniqueId() + "." + Main.main.currentSlot.get(p.getUniqueId().toString()) + ".title")));
        Inventory plrInv = p.getInventory();
        plrInv.clear();

        // Lores

        // Rename Button
        ArrayList<String> createLore = new ArrayList<>();
        createLore.add(ChatColor.AQUA + "Left Click" + ChatColor.DARK_GRAY + " to " + ChatColor.AQUA + "rename" + ChatColor.DARK_GRAY +  " the");
        createLore.add(ChatColor.DARK_GRAY + "name of your GUI");


        // Back Button
        ArrayList<String> backLore = new ArrayList<>();
        backLore.add(ChatColor.AQUA + "Left Click" + ChatColor.DARK_GRAY + " to");
        backLore.add(ChatColor.DARK_GRAY + "go back");

        // Add Row Button
        ArrayList<String> rowLore = new ArrayList<>();
        rowLore.add(ChatColor.AQUA + "Left Click" + ChatColor.DARK_GRAY + " to add");
        rowLore.add(ChatColor.DARK_GRAY + "a row to your GUI");

        // Remove Row Button
        ArrayList<String> removeRowLore = new ArrayList<>();
        removeRowLore.add(ChatColor.AQUA + "Left Click" + ChatColor.DARK_GRAY + " to remove");
        removeRowLore.add(ChatColor.DARK_GRAY + "a row from your GUI");

        // Bind To Command Button
        ArrayList<String> bindLore = new ArrayList<>();
        bindLore.add(ChatColor.AQUA + "Left Click" + ChatColor.DARK_GRAY + " to bind this");
        bindLore.add(ChatColor.DARK_GRAY + "GUI to a command");


        // Item Setting

        // Bind Button
        ItemStack bind = new ItemStack(Material.REDSTONE_TORCH);
        ItemMeta bindMeta = bind.getItemMeta();
        bindMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Bind To Command");
        bindMeta.setLore(bindLore);
        bind.setItemMeta(bindMeta);

        // Back Button
        ItemStack back = new ItemStack(Material.FEATHER);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Back");
        backMeta.setLore(backLore);
        back.setItemMeta(backMeta);

        // Add Row Button
        ItemStack row = new ItemStack(Material.BLAZE_ROD);
        ItemMeta rowMeta = row.getItemMeta();
        rowMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Add Row");
        rowMeta.setLore(rowLore);
        row.setItemMeta(rowMeta);

        // Remove Row Button
        ItemStack removeRow = new ItemStack(Material.STICK);
        ItemMeta removeRowMeta = removeRow.getItemMeta();
        removeRowMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Remove Row");
        removeRowMeta.setLore(removeRowLore);
        removeRow.setItemMeta(removeRowMeta);

        // Rename Button
        ItemStack DisplayName = new ItemStack(Material.NAME_TAG);
        ItemMeta DisplayNameMeta = DisplayName.getItemMeta();
        DisplayNameMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Rename");
        DisplayNameMeta.setLore(createLore);
        DisplayName.setItemMeta(DisplayNameMeta);

        p.openInventory(createMenu);

        plrInv.setItem(13, removeRow);
        plrInv.setItem(11, DisplayName);
        plrInv.setItem(15, row);
        plrInv.setItem(27, back);
        plrInv.setItem(35, bind);

        Main.main.isTransferring.remove(p.getName());
    }

    public void sortMainGui(Player p, Inventory inv) {
        ArrayList<ItemStack> totalGuis = new ArrayList<>();

        for(ItemStack is : inv) {
            if (is != null && is.getType() != Material.AIR) {
                if (is.getType() == Material.CHEST_MINECART) {
                    totalGuis.add(is);
                }
            }
        }

        int count = 0;

        for (String key : Main.main.getGuiStorage().getConfigurationSection("Guis." + p.getUniqueId()).getKeys(false)) {
            final String title = Main.main.getGuiStorage().getString("Guis." + p.getUniqueId() + "." + key + ".title");
            final int size = Main.main.getGuiStorage().getInt("Guis." + p.getUniqueId() + "." + key + ".size");

            Main.main.getGuiStorage().set("Guis." + p.getUniqueId() + "." + key, null);
            Main.main.getGuiStorage().set("Guis." + p.getUniqueId() + "." + count + ".title", title);
            Main.main.getGuiStorage().set("Guis." + p.getUniqueId() + "." + count + ".size", size);
            Main.main.guiStorage.save();
            count++;
        }

        for (int i = 0; i < totalGuis.size(); i++) {
            inv.setItem(i, totalGuis.get(i));
        }
        Main.main.createMainMenu(p);
    }

    public void addValueGuiStorage(String value, String key) {
        Main.main.getGuiStorage().set(value, key);
        Main.main.guiStorage.save();
    }

    public int getAllSlots(Player p) {
        int total = -1;
        if (Main.main.guiStorage.getConfig().getConfigurationSection("Guis." + p.getUniqueId()) != null) {
            for (String s : Main.main.guiStorage.getConfig().getConfigurationSection("Guis." + p.getUniqueId()).getKeys(false)) {
                total++;
            }
        }
        return total;
    }

    public void loadInventory(Player p) {
        Inventory piv = p.getInventory();
        piv.clear();
        ItemStack[] Contents = (ItemStack[]) Main.main.getGuiStorage().get("Saved." + p.getUniqueId());
        if (Contents != null) {
            p.getInventory().setContents(Contents);
        }
        p.updateInventory();
    }

    public void saveInventory(Player p) {
        Inventory piv = p.getInventory();
        ItemStack[] Contents = piv.getContents();
        Main.main.getGuiStorage().set("Saved." + p.getUniqueId(), Contents);
        Main.main.guiStorage.save();
        piv.clear();
    }

}
