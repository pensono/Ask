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
public class RemindSetCommand extends vCommand {

    @Override
    public boolean run(CommandSender sender, String[] args) {
        String reminder = "";
        for (String s : args) {
            reminder += s + " ";
        }
        Ask.getReminders().setProperty(sender.getName(), reminder);
        Ask.saveReminders();
        sender.sendMessage(ChatColor.AQUA+"Reminder set.");
        return true;
    }

    @Override
    public String getLabel() {
        return "set";
    }

    @Override
    public String getPermission() {
        return "ask.remind.set";
    }

    @Override
    public String getHelp() {
        return "Sets your reminder";
    }

    @Override
    public String getUseage() {
        return "/ask r set [reminder]";
    }

    @Override
    public void start() {
    }
}
