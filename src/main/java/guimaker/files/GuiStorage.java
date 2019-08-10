package guimaker.files;

import org.bukkit.configuration.file.FileConfiguration;

import guimaker.guimaker.Main;

public class GuiStorage extends fileCreation {

    public GuiStorage(Main main) {
        super(main, "guiStorage.yml");
    }

    public FileConfiguration getConfig() {
        return config;
    }

}
