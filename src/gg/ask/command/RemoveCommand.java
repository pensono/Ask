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
 * @author Melissa
 */
public class RemoveCommand extends vCommand{

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (!Ask.getUnanswered().isEmpty()){
            sender.sendMessage(ChatColor.AQUA+Ask.getUnanswered().get(0)+" was removed.");
            Ask.removeUnanswered(Ask.getUnanswered().get(0));
        }else{
            sender.sendMessage(ChatColor.AQUA+"There was nothing to remove...");
        }
        return true;
    }

    @Override
    public String getLabel() {
        return "remove";
    }

    @Override
    public String getPermission() {
        return "ask.remove";
    }

    @Override
    public String getHelp() {
        return "Remove the next, ans simmalar unanswered question from the queue";
    }

    @Override
    public String getUseage() {
        return "/ask remove";
    }

    @Override
    public void start() {
        
    }
    
}
