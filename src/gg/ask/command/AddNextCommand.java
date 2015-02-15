/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gg.ask.command;

import com.thevoxelbox.command.vCommand;
import gg.ask.Ask;
import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Melissa
 */
public class AddNextCommand extends vCommand{

    @Override
    public boolean run(CommandSender sender, String[] args) {
        ArrayList<String> unans = Ask.getUnanswered();
        if (unans.isEmpty()){
            sender.sendMessage(ChatColor.AQUA+"There are no more unanswered questions.");            
            return true;
        } else {
            sender.sendMessage(ChatColor.AQUA+"Next Unanswered question:");
            sender.sendMessage(ChatColor.AQUA+Ask.getUnanswered().get(0));
            return true;
        }
    }

    @Override
    public String getLabel() {
        return "next";
    }

    @Override
    public String getPermission() {
        return "ask.add";
    }

    @Override
    public String getHelp() {
        return "Displays the next unanswered question";
    }

    @Override
    public String getUseage() {
        return "/ask add next";
    }

    @Override
    public void start() {
        
    }
    
}
