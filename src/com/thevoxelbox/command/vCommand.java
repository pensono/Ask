/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thevoxelbox.command;

import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author geekygenius
 */
public abstract class vCommand {
    private ArrayList<vCommand> subCommands;
    
    public final boolean execute(CommandSender sender, String[] args){
        return execute(sender, args, "");
    }
    
    public final boolean execute(CommandSender sender, String[] args, String path){
        path+=getLabel()+" ";
        
        if (args.length!=0){        
            if (args[0].equals("?")||args[0].equalsIgnoreCase("help")){
                if (args.length==1){
                    help(sender,path);
                }else{
                    help(sender,path,Integer.parseInt(args[1]));
                }
                return true;
            }
                
            for (vCommand command:getSubCommands()){
                if (command.getLabel().equalsIgnoreCase(args[0])){
                    if (sender.hasPermission(command.getPermission())){
                        String[] newArgs=new String[args.length-1];
                        System.arraycopy(args, 1, newArgs, 0, newArgs.length);
                        if (command.execute(sender,newArgs,path)){
                            return true;
                        }
                        return false;//Don't want it to exec the command if perms for the sub command are not there.
                    }else{
                        sender.sendMessage(ChatColor.RED+"You don't have permission to do this...");
                    }
                }
            }
        }
        
        if (run(sender, args)){//No sub commands come up... the commands fails for some reason... time to use help
            return true;
        } else {
            help(sender,path);
        }
        
        return false;
    }
    
    public final void help(CommandSender sender, String path){
        help(sender, path, 1);
    }
    
    public final void help(CommandSender sender, String path, int page){
        sender.sendMessage(CommandProperties.getHelpTitle());
        if (sender.hasPermission(getPermission())){//Only let people see what they can use
            sender.sendMessage(CommandProperties.getDescriptionColor()+"Useage: "+getUseage());
            sender.sendMessage(CommandProperties.getCommandColor()+path+CommandProperties.getDescriptionColor()+": "+getHelp());

            int count=1;
            for (vCommand command:getSubCommands()){
                if ((count<page*CommandProperties.getHelpPerPage())&&
                        (count>page*CommandProperties.getHelpPerPage()-CommandProperties.getHelpPerPage())){
                    if (sender.hasPermission(command.getPermission())){
                        sender.sendMessage(CommandProperties.getCommandColor()+"-"+(path+command.getLabel()).trim()+
                                ChatColor.GRAY+": "+CommandProperties.getDescriptionColor()+command.getHelp());
                    }
                }
                count++;
            }
            sender.sendMessage(CommandProperties.getDescriptionColor()+" Page "+page+" of "+((int)Math.ceil(getSubCommands().size()/CommandProperties.getHelpPerPage())+1));
        }
    }
    
    /**
     * Beef of command. This is where the code for the command is stored. Return true if the command was completed. 
     * False if something was missing, or if an error was thrown. Help will be shown if help runs.
     * @param sender
     * @param args
     * @return Secuess value
     */
    
    public abstract boolean run(CommandSender sender, String[] args);
    
    public abstract String getLabel();
    
    public abstract String getPermission();
    
    public abstract String getHelp(); //Abstract to encourage good documentaition :D
    
    public abstract String getUseage();
    
    public final void init(){
        subCommands=new ArrayList<vCommand>(0);
        start();
        for (vCommand command:getSubCommands()){
            command.init();
        }
    }
    
    /**
     * Add sub commands here and set up whatever else. Blank by deafult.
     */
    public abstract void start();
    
    @Override
    public String toString(){
        return CommandProperties.getCommandColor()+getLabel()+CommandProperties.getDescriptionColor()+getHelp();
    }
    
    public final void addSubCommand(vCommand sub){
        subCommands.add(sub);
    }
    
    public final void removeSubCommand(vCommand sub){
        subCommands.remove(sub);
    }
    
    public final ArrayList<vCommand> getSubCommands(){
        return subCommands;
    }
}
