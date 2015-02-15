/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gg;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Melissa
 */
public class PropertiesFile extends Properties{
    //Really monogitory, but nessicary...
    
    //Just pass it into the super... Class is intended to extend functionality
    public PropertiesFile(){
        super();
    }
    
    public PropertiesFile(Properties defaults) {
        super(defaults);
    }
    
    public PropertiesFile(File file) throws IOException{
        super();
        load(new FileReader(file));
    }
    //Will add more if needed...
    
    //BOOLEAN
    public boolean getBoolean(String key){
        return Boolean.parseBoolean(getProperty(key));
    }
    
    public boolean getBoolean(String key, boolean deafult){
        if (getProperty(key)==null){
            return deafult;
        }
        return Boolean.parseBoolean(getProperty(key));
    }
    
    public void addBoolean(String key, boolean thing){
        put(key, String.valueOf(thing));
    }
    
    //INTEGER
    public int getInt(String key){
        return Integer.parseInt(getProperty(key));
    }
    
    public int getInt(String key, int deafult){
        if (getProperty(key)==null){
            return deafult;
        }
        return Integer.parseInt(getProperty(key));
    }
    
    public void addInt(String key, int thing){
        put(key, String.valueOf(thing));
    }
    
    //STRING
    public String getString(String key, String deafult){
        if (getProperty(key)==null){
            return deafult;
        }
        return getProperty(key);
    }
    
    public String getString(String key){
        return getProperty(key);
    }
    
    public void addString(String key, String thing){
        put(key, thing);
    }
}
