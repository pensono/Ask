package com.thevoxelbox.command;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import org.bukkit.ChatColor;

/**
 *
 * @author geekygenius
 */
public class CommandProperties { //Porperties must be configured like this because its a library
    private static ChatColor commandColor = ChatColor.DARK_RED;
    private static ChatColor descColor = ChatColor.RED;
    private static String helpTitle="==== HELP ====";
    private static int helpPerPage=8;
    
    public static ChatColor getCommandColor(){
        return commandColor;
    }
    
    public static void setCommandColor(ChatColor color){
        commandColor = color;
    }
    
    public static ChatColor getDescriptionColor(){
        return descColor;
    }
    
    public static void setDescriptionColor(ChatColor color){
        descColor = color;
    }
    
    public static String getHelpTitle(){
        return helpTitle;
    }
    
    /**
     * Title of the help. Remember to format it with ChatColor's
     * @param title 
     */
    
    public static void setHelpTitle(String title){
        helpTitle=title;
    }
    
     public static int getHelpPerPage(){
        return helpPerPage;
    }
    
    public static void setHelpPerPage(int number){
        helpPerPage = number;
    }
}
