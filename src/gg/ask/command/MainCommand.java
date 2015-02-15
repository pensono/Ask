/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gg.ask.command;

import com.thevoxelbox.command.vCommand;
import gg.ask.Answer;
import gg.ask.Ask;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author geekygenius
 */
public class MainCommand extends vCommand{

    @Override
    public synchronized boolean run(CommandSender asker, String[] args) {
        if (args.length==0){
            return false;
        }
            
        if (args[0].equals("?")||args[0].equals("help")){
            return false;
        }
        asker.sendMessage(ChatColor.AQUA + "[Ask] Getting Answer...");
        //Get some variables from the plugin
        ArrayList<String> keywords=Ask.getKeywords();
        ArrayList<Answer> answers=Ask.getAnswers();
        
        //Filter out pesky question marks
        if (args[args.length - 1].endsWith("?")) {
            String finalWord = args[args.length - 1];
            args[args.length - 1] = finalWord.substring(0, finalWord.length() - 1).toString();//Just remove the question mark at the end.
        }

        ArrayList<Answer> hits = new ArrayList<Answer>(10);
        for (String word : args) {
            if (keywords.contains(word)) {
                //found a keyword, now look for matching questions. 
                for (Answer a : answers) {
                    if (Ask.compare(a.getKeyword(), word)) {
                        hits.add(a);
                    }
                }
            }
        }
        //Narrow by first word.
        for (Answer a : hits) {
            if (!Ask.compare(a.getBegin(), args[0])) {
                hits.remove(a);
            }
        }

        //Cut it off early if its narrow enough.
        if (hits.size() == 1) {            
            asker.sendMessage(ChatColor.AQUA + "[Ask] Answer: " + hits.get(0).getAnswer());
            return true;//End it there
        }

        HashMap<Answer, Integer> matches = new HashMap<Answer, Integer>();
        for (String word : args) {//For every word
            for (Answer a : hits) {//Look at every remaining answer
                matches.put(a, 0);
                for (String related : a.getRelated()) {//Compare their related words list
                    if (Ask.compare(word, related)) {
                        int i = matches.get(a);//Not sure if this is the best way, but...
                        matches.remove(a);
                        matches.put(a, i + 1);
                    }
                }
            }
        }

        //Find the one with the most hits
        int highest = -1;//-1 so If no matches come up, one will still show
        Answer best=null;
        for (Answer a : matches.keySet()) {
            if (matches.get(a) > highest) {
                highest = matches.get(a);
                best = a;
            }
        }
        if (best==null){
            asker.sendMessage(ChatColor.AQUA +Ask.getProps().getString("deafultAnswer", "No answers found. Try asking it like its a real question."));
            String dump="";//Eww
            for (String s:args){
                dump+=s+" ";
            }
            
            Ask.server().broadcast(ChatColor.AQUA+asker.getName()+" asked "+dump+ 
                    "\" without getting an answer. Please let them know.","ask.notify");//Let the people know
            Ask.dumpQuestion(dump);
            return true;
        }
        asker.sendMessage(ChatColor.AQUA + "[Ask] Answer: " + best.getAnswer());
        return true;
    }

    @Override
    public String getLabel() {
        return "ask";
    }

    @Override
    public String getPermission() {
        return "ask.ask";
    }

    @Override
    public String getHelp() {
        return "Asks a question";
    }

    @Override
    public String getUseage() {
        return "/ask [question]";
    }

    @Override
    public void start() {
        addSubCommand(new ListCommand());
        addSubCommand(new RemindCommand());
        addSubCommand(new ReloadConfigCommand());
        addSubCommand(new AddCommand());
        addSubCommand(new RemoveCommand());
    }    
}
