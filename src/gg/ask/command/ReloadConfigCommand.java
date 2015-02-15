/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gg.ask.command;

import com.thevoxelbox.command.vCommand;
import gg.ask.Ask;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author geekygenius
 */
public class ReloadConfigCommand extends vCommand{

    @Override
    public boolean run(CommandSender sender, String[] args) {
        sender.sendMessage(ChatColor.AQUA+"Loading Settings...");
        Ask.loadSettings();
        sender.sendMessage(ChatColor.AQUA+"Settings loaded.");
        return true;
    }

    @Override
    public String getLabel() {
        return "reload";
    }

    @Override
    public String getPermission() {
        return "ask.reload";
    }

    @Override
    public String getHelp() {
        return "Reload config";
    }

    @Override
    public String getUseage() {
        return "/ask reload";
    }

    @Override
    public void start() {
        
    }
    
}
