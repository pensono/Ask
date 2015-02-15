package gg.ask;
//gg=geekygenius
import com.thevoxelbox.command.CommandProperties;
import com.thevoxelbox.command.vCommand;
import gg.PropertiesFile;
import gg.ask.command.MainCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Pattern;
import org.bukkit.Server;

public class Ask extends JavaPlugin {

    private static ArrayList<Answer> answers;
    private static ArrayList<String> keywords;
    private static ArrayList<String> unanswered;
    private static PropertiesFile props;
    private static Properties reminders;
    private vCommand mainCmd;
    private static File dataFolder;
    private static Server server;

    //regex
    @Override
    public void onEnable() {
        //vCommand setup
        mainCmd = new MainCommand();
        mainCmd.init();
        CommandProperties.setHelpTitle(ChatColor.GOLD + "#%===[ Ask Help ]===%#");

        dataFolder = getDataFolder();
        server = getServer();

        loadSettings();

        //Standard enable code...
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " enabled.");
    }

    @Override
    public void onDisable() {
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return mainCmd.execute(sender, args);
    }

    public static void loadSettings() {
        answers = new ArrayList<Answer>();
        keywords = new ArrayList<String>();
        unanswered = new ArrayList<String>();
        //load the answers into the list

        new File(getDataDrive().getPath()).mkdirs();

        loadConfig();
        loadAnswers();
        loadUnanswered();
        loadReminders();
    }

    private static void loadConfig() {
        try {
            File propertiesFile = new File(getDataDrive().getPath() + "/ask.properties");

            props = new PropertiesFile();
            if (!propertiesFile.exists()) {
                System.out.println("[Ask] Answers properties file not persent.. created new file. Search for ask/ask.properties");
                FileOutputStream fos = new FileOutputStream(propertiesFile);
                Properties deafult = deafultProperties();
                deafult.store(fos, "Ask Properties File");
            }

            props.load(new FileReader(propertiesFile));//Load properties file anyways... skip parsing if there is no data file
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("Config loading failed. Reverting to deafults...");
        }
    }

    private static void loadAnswers() {
        BufferedReader br = null;
        File data = new File(Ask.getDataDrive().getPath() + "/answers.txt");
        try {
            if (!data.exists()) {
                File drive = new File(Ask.getDataDrive().getPath());
                drive.mkdirs();

                data.createNewFile();
                System.out.println("[Ask] Answers file not present... created new file. Search for ask/answers.txt");

                BufferedWriter bw = new BufferedWriter(new FileWriter(data));
                fillAnswerFile(bw);
                loadAnswers();//Load em now.
                return;
            } else {
                br = new BufferedReader(new FileReader(data));

                String line = null;
                Answer answer = null;


                String[] in = {"", "", "", ""};

                while (br.ready()) {
                    line = br.readLine();
                    line = line.trim();
                    if (line.startsWith("#")) {//Comment
                        continue;
                    }

                    if (line.equals("")) {//Skipped line
                        continue;
                    }
                    String[] split = line.split("/");//Split on the /

                    int offset = 0;

                    if (split[0].equals("\"")) {//Uses ditto marks. Find out where in the string it should start.
                        offset = 5 - split.length;
                    }
                    for (int i = 0; i < split.length; i++) {
                    }

                    for (int i = 0; i + offset < 4; i++) {
                        if (offset != 0) {//Gotta do this to ignore the ditto mark.
                            in[offset + i] = split[i + 1];
                        } else {
                            in[offset + i] = split[i];
                        }
                    }

                    keywords.add(split[0]);

                    answer = new Answer(in[0], in[1], in[2].split(","), in[3]);

                    answers.add(answer);
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("Answer file loading failed...");
        } finally {
            try {//try in a finally... REALLY?!?
                br.close();
            } catch (Exception ex) {
                System.out.println("[Ask] Failed closing the file... (Expected if file was just created)");
            }
        }
    }

    public static void dumpQuestion(String question) {
        unanswered.add(question);
        BufferedWriter bw = null;
        try {
            File data = new File(getDataDrive().getPath() + "/unansweredQuestions.txt");
            bw = new BufferedWriter(new FileWriter(data, true));//Append text to the file.
            Ask.writeLine(question, bw);
        } catch (IOException ioe) {
            ioe.printStackTrace(System.err);
            System.out.println("[Ask] failed to write an unanswered question to the file. Not a HUGE thing but...");
        } finally {
            try {//try in a finally... REALLY?!?
                bw.close();
            } catch (Exception ex) {
                System.out.println("[Ask] Failed closing the file... (Expected if file was just created)");
            }
        }
    }

    public static void updateUnanswered() {
        BufferedWriter bw = null;
        try {
            File data = new File(getDataDrive().getPath() + "/unansweredQuestions.txt");
            data.createNewFile();//Does nothing if its already there.
            bw = new BufferedWriter(new FileWriter(data, false));//Overwrite
            for (String s : unanswered) {
                Ask.writeLine(s, bw);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace(System.err);
            System.out.println("[Ask] failed to update unanswered question file. Not a HUGE thing but...");
        } finally {
            try {//try in a finally... REALLY?!?
                bw.close();
            } catch (Exception ex) {
                System.out.println("[Ask] Failed closing the file... (Expected if file was just created)");
            }
        }
    }

    public static void loadUnanswered() {
        BufferedReader br = null;
        try {
            File data = new File(getDataDrive().getPath() + "/unansweredQuestions.txt");
            if (!data.exists()) {
                data.createNewFile();
            }
            br = new BufferedReader(new FileReader(data));
            while (br.ready()) {
                unanswered.add(br.readLine());
            }
        } catch (IOException ioe) {
            ioe.printStackTrace(System.err);
            System.out.println("[Ask] failed to write an unanswered question to the file. Not a HUGE thing but...");
        } finally {
            try {//try in a finally... REALLY?!?
                br.close();
            } catch (Exception ex) {
                System.out.println("[Ask] Failed closing the file... (Expected if file was just created)");
            }
        }
    }

    public static boolean compare(String a, String b) {
        if (props.getBoolean("useRegex", true)) {
            return Pattern.compile(a).matcher(b).matches();
        }
        return a.equalsIgnoreCase(b);
    }

    private static void writeLine(String line, BufferedWriter bw) throws IOException {
        bw.write(line);
        bw.newLine();
    }

    private static void fillAnswerFile(BufferedWriter bw) throws IOException {
        //Fill the file with some info
        Ask.writeLine("#==Ask Answer file==#", bw);
        Ask.writeLine("", bw);
        Ask.writeLine("#Format:", bw);
        Ask.writeLine("#Keyword/FirstWord/related,words/Answer to the question.", bw);
        Ask.writeLine("#Keyword: Subject of the question.", bw);
        Ask.writeLine("#FirstWord:The word the question begins with: Who, What, When, Where, Why, How, Are, Is, Do, ect. Asks what about the keyword.", bw);
        Ask.writeLine("#Related words: Other words about the keyword. Use to discriminate between two questions. ex:", bw);
        Ask.writeLine("#    What does an admin do?, What is an admin?, related words: do and is.", bw);
        Ask.writeLine("#Answer: Answer to the formatted question.", bw);
        Ask.writeLine("#Comment: # is used to comment out a line. It will not be read.", bw);
        Ask.writeLine("#Insert a space in the related words secion if you do not have any.", bw);
        Ask.writeLine("#You can use ditto marks (\") to copy the arguments stated above. Something like this:", bw);
        Ask.writeLine("Admin/How/ /Be a good person and you will get Admin.", bw);
        Ask.writeLine("\"/What/is/An admin is a person who can ban you. Be nice.", bw);
        Ask.writeLine("\"/do/Admins are in charge of administrating the server.", bw);
        Ask.writeLine("#Questions How do I become an Admin? What is an Admin? and What does an Admin so? are stated", bw);
        Ask.writeLine("#You can only use ditto marks at the beginning of a line.", bw);
        Ask.writeLine("#This gets really handy if you want to start calling Admins Leaders or something.", bw);
        Ask.writeLine("#Remember to add the seperators", bw);
        Ask.writeLine("#Visit the BukkitDev at: http://dev.bukkit.org/ask/ for more info.", bw);
        Ask.writeLine("#Example question:", bw);
        Ask.writeLine("#How do I add questions?", bw);
        Ask.writeLine("questions/How/add/Add a line into the answers.dat file in the format listed at the top.", bw);
        Ask.writeLine("questions/How/use/Type /ask into the chat followed by your question.", bw);
        bw.close();
        return;
    }

    private static Properties deafultProperties() {
        Properties p = new Properties();
        p.setProperty("deafultAnswer", "No answers found. Try asking it like its a real question.");
        p.setProperty("useRegex", "true");
        p.setProperty("askReminder", "Remember, you can use /ask [question] to ask a question. Stop bugging me.");
        return p;
    }

    private static void loadReminders() {
        reminders = new Properties();
        try {
            File data = new File(getDataDrive().getPath() + "/reminders.properties");
            if (!data.exists()) {
                data.createNewFile();
                reminders.store(new FileWriter(data), "Custom reminders");
            }
            reminders.load(new FileReader(data));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("Failed loading/creating custom reminders.");
        }
    }

    public static void saveReminders() {
        try {
            reminders.store(new FileWriter(new File(getDataDrive().getPath() + "/reminders.properties")), "Custom reminders");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Reminder saving failed.");
        }
    }

    public static ArrayList<String> getKeywords() {
        return keywords;
    }

    public static ArrayList<Answer> getAnswers() {
        return answers;
    }

    public static ArrayList<String> getUnanswered() {
        return unanswered;
    }
    
    public static void removeUnansweredSimmalarTo(String answer){
        Answer newAns = Answer.parseAnswer(answer);
        ArrayList<String> newUnans = new ArrayList<String>(getUnanswered());//Concurrent Mod Exc workaroud        
        
        for (String s : getUnanswered()) {
            String[] words = s.split(" ");
            if (words.length<4)
            for (String word : words) {//If the string contains the keyword and first word
                if (word.equalsIgnoreCase(newAns.getBegin())) {//Begin first because it should be at the front
                    for (String w : words) {//If the string contains the keyword and first word
                        if (w.equalsIgnoreCase(newAns.getKeyword())) {
                            newUnans.remove(s);
                        }
                    }
                }
            }
        }
        unanswered=newUnans;
        Ask.updateUnanswered();
    }
    
    public static void removeUnanswered(String answer){
        unanswered.remove(answer);
        Ask.updateUnanswered();
    }

    public static Properties getReminders() {
        return reminders;
    }

    public static PropertiesFile getProps() {
        return props;
    }

    public static File getDataDrive() {
        return dataFolder;
    }

    public static Server server() {
        return server;
    }

    public static void answer(String answer) {
        Ask.addAnswer(answer);
        removeUnansweredSimmalarTo(answer);//Well that ended up very clean :D
        //remove other matching questions
        //Update the file to keep things in sync
        //display next unans question (invoke AddNextCommand.run())
        //Done in AddCommand where a player reference is obtained
    }

    public static void addAnswer(String answer) {
        //Add answer to the file
        BufferedWriter bw = null;
        try {
            File data = new File(getDataDrive().getPath() + "/answers.txt");
            data.createNewFile();//Does nothing if its already there.
            bw = new BufferedWriter(new FileWriter(data, true));//Add onto the end
            Ask.writeLine(answer, bw);
        } catch (IOException ioe) {
            ioe.printStackTrace(System.err);
            System.out.println("[Ask] failed to write an unanswered question to the file. Not a HUGE thing but...");
        } finally {
            try {//try in a finally... REALLY?!?
                bw.close();
            } catch (Exception ex) {
                System.out.println("[Ask] Failed closing the file... (Expected if file was just created)");
            }
        }
        //Add answer to the list.
        answers.add(Answer.parseAnswer(answer));
    }
}
