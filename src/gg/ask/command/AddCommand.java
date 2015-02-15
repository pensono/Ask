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
 * @author Melissa
 */
public class AddCommand extends vCommand{

    @Override
    public boolean run(CommandSender sender, String[] args) {
        if (args.length==0){
            return false;
        }
        String answer="";
        for (String s:args){
            answer+=s+" ";
        }
        Ask.answer(answer);
        sender.sendMessage("Answer added.");
        new AddNextCommand().run(sender,null);//Show em' the next question
               
        return true;
    }

    @Override
    public String getLabel() {
        return "add";
    }

    @Override
    public String getPermission() {
        return "ask.add";
    }

    @Override
    public String getHelp() {
        return "Adds an answer to the answer list.";
    }

    @Override
    public String getUseage() {
        return "/ask add [Answer/in/this/Format]";
    }

    @Override
    public void start() {
        addSubCommand(new AddNextCommand());
    }    
}
