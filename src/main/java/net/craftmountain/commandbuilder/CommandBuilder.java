package net.craftmountain.commandbuilder;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public class CommandBuilder extends JavaPlugin {
    private static CommandBuilder plugin;

    @Override
    public void onEnable() {
        plugin = this;
        PluginDescriptionFile pdfFile = getDescription();
        getLogger().info("Enabling " + pdfFile.getName() + "...");

        saveDefaultConfig();
        if (loadCommands())
            getLogger().info(pdfFile.getName() + " has been enabled running version " + pdfFile.getVersion() + "!");
        else {
            for (int i=0; i<3; i++)
                getLogger().warning("AN ERROR OCCURRED WHILE ATTEMPTING TO LOAD " + pdfFile.getName() + "!");
        }
    }

    @Override
    public void onDisable() {
        PluginDescriptionFile pdfFile = getDescription();
        getLogger().info(pdfFile.getName() + " has been disabled!");
    }

    private boolean loadCommands() {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            if (getConfig().contains("commands") && getConfig().isConfigurationSection("commands")) {
                for (String command : getConfig().getConfigurationSection("commands").getKeys(false)) {
                    if ((getConfig().contains("commands."+command+".output") && getConfig().isList("commands."+command+".output")) ||
                            (getConfig().contains("commands." + command + ".actions") && getConfig().isList("commands" + command + ".actions"))) {
                        commandMap.register(command, new CustomCommand(command));
                        getLogger().info("Command \"" + command + "\" has been registered with the server!");
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static CommandBuilder getInstance() {
        return plugin;
    }
}
