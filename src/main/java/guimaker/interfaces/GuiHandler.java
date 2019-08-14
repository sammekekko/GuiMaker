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

    public Inventory menuTory;

    public void mainMenu(Player p) {
        Inventory MainMenu = Bukkit.createInventory(null, 54, Utils.chat(Main.main.getConfig().getString("MainMenu.title")));
        menuTory = MainMenu;
        p.getInventory().clear();


        // Lores

        // Add Gui
        ArrayList<String> createLore = new ArrayList<>();
        createLore.add(ChatColor.AQUA + "" + ChatColor.BOLD + "Left Click" + ChatColor.GRAY + " to create");
        createLore.add(ChatColor.GRAY + "an interface");

        // Enabled Removing Lore
        ArrayList<String> enabledLore = new ArrayList<>();
        enabledLore.add(ChatColor.GRAY + "Removing mode is " + ChatColor.GREEN + "" + ChatColor.BOLD + "enabled");

        // Disabled Removing Lore
        ArrayList<String> disabledLore = new ArrayList<>();
        disabledLore.add(ChatColor.GRAY + "Removing mode is " + ChatColor.RED + "" + ChatColor.BOLD +  "disabled");

        // Add Gui
        ItemStack create = new ItemStack(Material.ANVIL);
        ItemMeta createMeta = create.getItemMeta();
        createMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Create");
        createMeta.setLore(createLore);
        create.setItemMeta(createMeta);

        // Enabled Removing
        ItemStack enabled = new ItemStack(Material.GREEN_WOOL);
        ItemMeta enabledMeta = enabled.getItemMeta();
        enabledMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Toggle Remove Mode");
        enabledMeta.setLore(enabledLore);
        enabled.setItemMeta(enabledMeta);

        // Disabled Removing
        ItemStack disabled = new ItemStack(Material.RED_WOOL);
        ItemMeta disabledMeta = disabled.getItemMeta();
        disabledMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Toggle Remove Mode");
        disabledMeta.setLore(disabledLore);
        disabled.setItemMeta(disabledMeta);


        int count = 0;


        if (Main.main.guiStorage.getConfig().getConfigurationSection("Guis." + p.getUniqueId()) != null) {
            for (String key : Main.main.guiStorage.getConfig().getConfigurationSection("Guis." + p.getUniqueId()).getKeys(false)) {
                ItemStack template = new ItemStack(Material.CHEST_MINECART);

                ItemMeta templateMeta = template.getItemMeta();
                templateMeta.setDisplayName(ChatColor.WHITE + Utils.chat(Main.main.guiStorage.getConfig().getString("Guis." + p.getUniqueId() + "." + key + ".title")));
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

    public void addItemMenu(Player p) {
        Inventory addItemMenu = Bukkit.createInventory(null, 36, Utils.chat(ChatColor.WHITE + Main.main.getGuiStorage().getString("Guis." + p.getUniqueId() + "." + Main.main.currentSlot.get(p.getUniqueId().toString()) + ".title")));
        Inventory plrInv = p.getInventory();
        plrInv.clear();

        String itemPath = "Guis." + p.getUniqueId() + "." + Main.main.currentSlot.get(p.getUniqueId().toString()) + ".items" + Main.main.currentItemSlot.get(p.getUniqueId().toString());
        String displayName = Utils.chat(Main.main.getGuiStorage().getString(itemPath + ".name"));
        String material = Main.main.getGuiStorage().getString(itemPath + ".material");
        String script = Main.main.getGuiStorage().getString(itemPath + ".script");

        /*
         0 1 2 3 4 5 6 7 8
         9 10 11 12 13 14 15 16 17
         18 19 20 21 22 23 24 25 26
         27 28 29 30 31 32 33 34 35
         - - - - - - - - -
         - - - - I - - - -
         - R - L - M - S -
         - - - - - - - - -
        I = ItemPreview
        R = Rename
        L = Lore
        M = Material
        S = Scripts
         */

        // Lores

        // ItemPreview Lore
        ArrayList<String> itemPLore = new ArrayList<>();
        String path = itemPath + ".lores";
        if (Main.main.getGuiStorage().contains(path)) {
            for (String s : Main.main.getGuiStorage().getStringList(path)) { // TODO when adding LORES make sure u set it as a string list
                itemPLore.add(Utils.chat(ChatColor.WHITE + s));
            }
        }

        // Scripts Lore
        ArrayList<String> scriptLore = new ArrayList<>();
        scriptLore.add(ChatColor.AQUA + "" + ChatColor.BOLD +  "Left Click" + ChatColor.GRAY + " to " + ChatColor.AQUA + "" + ChatColor.BOLD + "add" + ChatColor.GRAY +  " a");
        scriptLore.add(ChatColor.GRAY + "script for your item");
        scriptLore.add("");
        scriptLore.add(ChatColor.GRAY + "Current Script:");
        if (script == null) {
            script = Main.main.getConfig().getString("default.script");
        }
        scriptLore.add(script);

        // Material Lore
        ArrayList<String> materialLore = new ArrayList<>();
        materialLore.add(ChatColor.AQUA + "" + ChatColor.BOLD +  "Left Click" + ChatColor.GRAY + " to " + ChatColor.AQUA + "" + ChatColor.BOLD + "change" + ChatColor.GRAY +  " the");
        materialLore.add(ChatColor.GRAY + "material of your item");
        materialLore.add("");
        materialLore.add(ChatColor.GRAY + "Current Material:");
        if (material == null) {
            material = Main.main.getConfig().getString("default.material").replace("Material.", "");
        }
        materialLore.add(ChatColor.AQUA + material);

        // Rename Lore
        ArrayList<String> renameLore = new ArrayList<>();
        renameLore.add(ChatColor.AQUA + "" + ChatColor.BOLD +  "Left Click" + ChatColor.GRAY + " to " + ChatColor.AQUA + "" + ChatColor.BOLD + "rename");
        renameLore.add(ChatColor.GRAY + "your item");
        renameLore.add("");
        renameLore.add(ChatColor.GRAY + "Current Name:");
        if (displayName == null) {
            displayName = Utils.chat(Main.main.getConfig().getString("default.title"));
        }
        renameLore.add(ChatColor.WHITE + displayName);

        // Lore Lore
        ArrayList<String> loreLore = new ArrayList<>();
        loreLore.add(ChatColor.AQUA + "" + ChatColor.BOLD +  "Left Click" + ChatColor.GRAY + " to " + ChatColor.AQUA + "" + ChatColor.BOLD + "add" + ChatColor.GRAY +  " a");
        loreLore.add(ChatColor.GRAY + "lore for your item");
        loreLore.add(ChatColor.RED + "" + ChatColor.BOLD + "Right Click" + ChatColor.GRAY + " to " + ChatColor.RED + "" + ChatColor.BOLD + "remove" + ChatColor.GRAY + " all");
        loreLore.add(ChatColor.GRAY + "lores from your item");
        loreLore.add("");
        loreLore.add(ChatColor.GRAY + "Current Lores:");
        for (String s : itemPLore) {
            loreLore.add(Utils.chat(s));
        }

        // Items

        // Material Button
        ItemStack materialButton = new ItemStack(Material.WHITE_WOOL);
        ItemMeta materialMeta = materialButton.getItemMeta();
        materialMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Change Material");
        materialMeta.setLore(materialLore);
        materialButton.setItemMeta(materialMeta);

        // Script Button
        ItemStack scriptButton = new ItemStack(Material.COMMAND_BLOCK);
        ItemMeta scriptMeta = scriptButton.getItemMeta();
        scriptMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Add Script");
        scriptMeta.setLore(scriptLore);
        scriptButton.setItemMeta(scriptMeta);

        // Lore Button
        ItemStack lore = new ItemStack(Material.BOOKSHELF);
        ItemMeta loreMeta = lore.getItemMeta();
        loreMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Add Lores");
        loreMeta.setLore(loreLore);
        lore.setItemMeta(loreMeta);

        // Rename Button
        ItemStack rename = new ItemStack(Material.NAME_TAG);
        ItemMeta renameMeta = rename.getItemMeta();
        renameMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Rename");
        renameMeta.setLore(renameLore);
        rename.setItemMeta(renameMeta);


        // ItemPreview
        Material mat = Material.getMaterial(Main.main.getGuiStorage().getString(itemPath + ".material"));
        if (mat == null) {
            mat = (Material.getMaterial(Main.main.getConfig().getString("default.material")));
        }
        ItemStack itemP = new ItemStack(mat);
        ItemMeta itemPMeta = itemP.getItemMeta();
        if (displayName == null) {
            displayName = Main.main.getConfig().getString("default.title");
        }
        itemPMeta.setDisplayName(displayName);
        itemPMeta.setLore(itemPLore);
        itemP.setItemMeta(itemPMeta);
        // TODO continue

        addItemMenu.setItem(13, itemP);
        addItemMenu.setItem(19, rename);
        addItemMenu.setItem(15, lore);
        addItemMenu.setItem(17, materialButton);
        addItemMenu.setItem(19, scriptButton);

        p.openInventory(addItemMenu);
    }

    public void commandMenu(Player p) {
        Inventory cmdMenu = Bukkit.createInventory(null, 9, Utils.chat(ChatColor.WHITE + Main.main.getGuiStorage().getString("Guis." + p.getUniqueId() + "." + Main.main.currentSlot.get(p.getUniqueId().toString()) + ".title") + ChatColor.GRAY + " - " + ChatColor.RED + "" + ChatColor.BOLD + "Command Menu"));
        Inventory plrInv = p.getInventory();
        plrInv.clear();

        // Lores

        // Rename Command

        String displayNameRename = Main.main.getGuiStorage().getString("Guis." + p.getUniqueId() + "." + Main.main.currentSlot.get(p.getUniqueId().toString()) + ".command.name");
        ArrayList<String> renameLore = new ArrayList<>();
        renameLore.add(ChatColor.AQUA + "" + ChatColor.BOLD + "Left Click" + ChatColor.GRAY + " to " + ChatColor.AQUA + "" + ChatColor.BOLD + "set " + ChatColor.GRAY +  "a");
        renameLore.add(ChatColor.GRAY + "command for this GUI");
        renameLore.add("");
        renameLore.add(ChatColor.RED + "" + ChatColor.BOLD + "WARNING: ");
        renameLore.add(ChatColor.GRAY + "Setting the command will");
        renameLore.add(ChatColor.RED + "" + ChatColor.BOLD + "overwrite " + ChatColor.GRAY +  "any command with its name");
        renameLore.add("");
        renameLore.add(ChatColor.GRAY + "Current Command:");
        if (displayNameRename != null) {
            renameLore.add(ChatColor.AQUA + "" + ChatColor.BOLD + displayNameRename);
        } else {
            renameLore.add(Utils.chat(Main.main.getConfig().getString("default.command_name")));
        }


        // Permissions Command
        String displayNamePerm = Main.main.getGuiStorage().getString("Guis." + p.getUniqueId() + "." + Main.main.currentSlot.get(p.getUniqueId().toString()) + ".command.permission");

        ArrayList<String> permLore = new ArrayList<>();
        permLore.add(ChatColor.GREEN + "" + ChatColor.BOLD + "Left Click" + ChatColor.GRAY + " to " + ChatColor.GREEN + "" + ChatColor.BOLD + "add " + ChatColor.GRAY + "permissions");
        permLore.add(ChatColor.GRAY + "for your command");
        permLore.add("");
        permLore.add(ChatColor.GRAY + "Current Permission:");
        if (displayNamePerm != null) {
            permLore.add(ChatColor.GREEN + "" + ChatColor.BOLD + displayNamePerm);
        } else {
            permLore.add(Utils.chat(Main.main.getConfig().getString("default.permission_name")));
        }

        // Back
        ArrayList<String> backLore = new ArrayList<>();
        backLore.add(ChatColor.AQUA + "" + ChatColor.BOLD +  "Left Click" + ChatColor.GRAY + " to");
        backLore.add(ChatColor.GRAY + "go " + ChatColor.AQUA + "" + ChatColor.BOLD + "back");

        // Permissions Button
        ItemStack perm = new ItemStack(Material.REPEATER);
        ItemMeta permMeta = perm.getItemMeta();
        permMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Add Permissions");
        permMeta.setLore(permLore);
        perm.setItemMeta(permMeta);

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

        cmdMenu.setItem(5, perm);
        cmdMenu.setItem(3, rename);
        plrInv.setItem(27, back);

        Main.main.isTransferring.remove(p.getName());
    }

    public void createMenu(Player p) {
        Inventory createMenu = Bukkit.createInventory(null, Main.main.getGuiStorage().getInt("Guis." + p.getUniqueId() + "." + Main.main.currentSlot.get(p.getUniqueId().toString()) + ".size"), Utils.chat(Main.main.getConfig().getString("MainMenu.title")) + ChatColor.GRAY  + " - " + ChatColor.WHITE + Utils.chat(Main.main.getGuiStorage().getString("Guis." + p.getUniqueId() + "." + Main.main.currentSlot.get(p.getUniqueId().toString()) + ".title")));
        Inventory plrInv = p.getInventory();
        plrInv.clear();

        // Lores

        // Rename Button
        String displayName = Main.main.getGuiStorage().getString("Guis." + p.getUniqueId() + "." + Main.main.currentSlot.get(p.getUniqueId().toString()) + ".title");
        ArrayList<String> renameLore = new ArrayList<>();
        renameLore.add(ChatColor.AQUA + "" + ChatColor.BOLD +  "Left Click" + ChatColor.GRAY + " to " + ChatColor.AQUA + "" + ChatColor.BOLD + "rename" + ChatColor.GRAY +  " the");
        renameLore.add(ChatColor.GRAY + "name of your GUI");
        renameLore.add("");
        renameLore.add(ChatColor.GRAY + "Current Name:");
        if (displayName != null) {
            renameLore.add(Utils.chat(ChatColor.WHITE + displayName));
        } else {
            renameLore.add(Utils.chat(Main.main.getConfig().getString("default.title")));
        }

        // Back Button
        ArrayList<String> backLore = new ArrayList<>();
        backLore.add(ChatColor.AQUA + "" + ChatColor.BOLD + "Left Click" + ChatColor.GRAY + " to");
        backLore.add(ChatColor.GRAY + "go " + ChatColor.AQUA + "" + ChatColor.BOLD + "back");

        // Add Row Button
        ArrayList<String> rowLore = new ArrayList<>();
        rowLore.add(ChatColor.GREEN + "" + ChatColor.BOLD + "Left Click" + ChatColor.GRAY + " to " + ChatColor.GREEN + "" + ChatColor.BOLD + "add");
        rowLore.add(ChatColor.GRAY + "a row to your GUI");

        // Remove Row Button
        ArrayList<String> removeRowLore = new ArrayList<>();
        removeRowLore.add(ChatColor.RED + "" + ChatColor.BOLD + "Left Click" + ChatColor.GRAY + " to " + ChatColor.RED + "" + ChatColor.BOLD + "remove");
        removeRowLore.add(ChatColor.GRAY + "a row from your GUI");

        // Bind To Command Button
        ArrayList<String> bindLore = new ArrayList<>();
        bindLore.add(ChatColor.AQUA + "" + ChatColor.BOLD + "Left Click" + ChatColor.GRAY + " to "  + ChatColor.AQUA + "" + ChatColor.BOLD + "bind " + ChatColor.GRAY + "this");
        bindLore.add(ChatColor.GRAY + "GUI to a command");

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
        rowMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Add Row");
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
        DisplayNameMeta.setLore(renameLore);
        DisplayName.setItemMeta(DisplayNameMeta);

        // TODO add Items loop for this GUI

        p.openInventory(createMenu);

        plrInv.setItem(11, removeRow);
        plrInv.setItem(13, DisplayName);
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
