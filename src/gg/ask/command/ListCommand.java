/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gg.ask.command;

import com.thevoxelbox.command.vCommand;
import gg.ask.Answer;
import gg.ask.Ask;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author geekygenius
 */
public class ListCommand extends vCommand{

    @Override
    public boolean run(CommandSender sender, String[] args) {
        sender.sendMessage(ChatColor.AQUA + "[Ask] Possible Question/Answers:");
            for (Answer a : Ask.getAnswers()) {
                sender.sendMessage(ChatColor.AQUA + "[Ask] " + a.toString());
            }
            return true;
    }

    @Override
    public String getLabel() {
        return "list";
    }

    @Override
    public String getPermission() {
        return "ask.list";
    }

    @Override
    public String getHelp() {
        return "List all the avalible questions.";
    }

    @Override
    public String getUseage() {
        return "/ask list";
    }

    @Override
    public void start() {
        //No sub commands
    }
    
}
