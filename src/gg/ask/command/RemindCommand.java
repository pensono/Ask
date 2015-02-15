/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gg.ask.command;

import com.thevoxelbox.command.vCommand;
import gg.ask.Ask;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author geekygenius
 */
public class RemindCommand extends vCommand{

    @Override
    public boolean run(CommandSender sender, String[] args) {
        String message=Ask.getProps().getProperty("askReminder", "Remember, you can use /ask [question] to ask a question. Stop bugging me.");;
        message=Ask.getReminders().getProperty(sender.getName(), message);
        if (sender instanceof Player){
            ((Player)sender).chat(message);
        }else{//Not an actual player...
            Ask.server().broadcastMessage(message);
        }
        return true;
    }

    @Override
    public String getLabel() {
        return "r";
    }

    @Override
    public String getPermission() {
        return "ask.remind";
    }

    @Override
    public String getHelp() {
        return "Reminds pesky noobs how to use Ask.";
    }

    @Override
    public String getUseage() {
        return "/ask r";
    }

    @Override
    public void start() {
        addSubCommand(new RemindSetCommand());
    }    
}
