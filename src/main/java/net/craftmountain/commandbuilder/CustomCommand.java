package net.craftmountain.commandbuilder;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CustomCommand extends Command {

    private CommandExecutor exe = null;
    private String command;

    CustomCommand(String command) {
        super(command);
        this.command = command;
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if(exe != null){
            exe.onCommand(sender, this, label, args);
        } else {
            String uuid = "N/A";
            if (sender instanceof Player)
                uuid = ((Player) sender).getUniqueId().toString();

            //if command has actions
            CommandSender consoleSender = CommandBuilder.getInstance().getServer().getConsoleSender();
            if (CommandBuilder.getInstance().getConfig().contains("commands." + command + ".actions") && CommandBuilder.getInstance().getConfig().isList("commands." + command + ".actions")) {
                for (String cmd : CommandBuilder.getInstance().getConfig().getStringList("commands." + command + ".actions")) {
                    cmd = cmd.replaceAll("%NAME%", sender.getName()).replaceAll("%UUID%", uuid);
                    CommandBuilder.getInstance().getServer().dispatchCommand(consoleSender, cmd);
                }
            }

            //if command has output
            if (CommandBuilder.getInstance().getConfig().contains("commands." + command + ".output") && CommandBuilder.getInstance().getConfig().isList("commands." + command + ".output")) {
                for (String message : CommandBuilder.getInstance().getConfig().getStringList("commands." + command + ".output")) {
                    message = message.replaceAll("%NAME%", sender.getName()).replaceAll("%UUID%", uuid);
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                }
            }
        }

        return false;
    }

    public void setExecutor(CommandExecutor exe){
        this.exe = exe;
    }

    public String getDescription() {
        String desc = CommandBuilder.getInstance().getConfig().getString("commands." + command + ".description");
        return (desc == null ? "A custom command made using CommandBuilder" : desc);
    }

}
