/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package programnote;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Math.random;
import static java.lang.StrictMath.random;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 *Configuration class
 * @author jakub
 * Object stores configuration for a file
 * Configuration file stores:
 *  %date     - date of made f the configuration
 *  %debug    - information about debug session
 *  %name     - name of the user
 *  %gui      - information about prefered interface
 *  %checksum - information about user checksum
 *  %ip       - ip for the later database option
 */
public class Configuration {
    final String version = "v0.0.3B";               // version of the module
    final String CONFIGURATION_SRC = "./programnote_configuration.txt";     // path to configuration src
    boolean new_configuration = false;      // field if new configuration was made
    boolean fail = false;
    int debug;                              // field for debug
    
    String field_date = "";
    int field_debug = -1;
    String field_name = "";
    int field_gui = -1;
    String field_checksum = "";
    String field_ip = "";
    
    
    Date date_of_configuration;
    Date actual_date;
    ArrayList<String> log;
    File actual_configuration; 
    ArrayList<String> lines_of_configuration;
    
    
    Configuration(int debug) throws IOException{
        this.debug = debug;
        log = new ArrayList<>();
        lines_of_configuration = new ArrayList<>();
        // checking if configuration file is set
        if ( check_configuration_file() ){  // we find configuration file
            load_configuration();
        }
        else{   // we need to make new configuration file
            make_new_configuration_file();
        }
    }
    /**
     * Configuration.update_lines()
     * Function updates lines of the collection
     */
    void update_lines(){
        show_debug("Updating lines_of_configuration...");
        lines_of_configuration.clear();
        /**
        *  %date     - date of made f the configuration
        *  %debug    - information about debug session
        *  %name     - name of the user
        *  %gui      - information about prefered interface
        *  %checksum - information about user checksum
        *  %ip       - ip for the later database option
        */
        lines_of_configuration.add("?ProgramNote configuration file");
        lines_of_configuration.add("?DO N0T EDIT!");
        lines_of_configuration.add("%date%"+field_date);
        lines_of_configuration.add("%debug%"+Integer.toString(field_debug));
        lines_of_configuration.add("%name%"+field_name);
        lines_of_configuration.add("%gui%"+Integer.toString(field_gui));
        lines_of_configuration.add("%checksum%"+field_checksum);
        lines_of_configuration.add("%ip%"+field_ip);
        show_debug("Updated");
    }
    /**
     * Configuration.reload()
     * @throws IOException 
     * Function reload configuration file
     */
    void save_file() throws IOException{
        show_debug("Updating lines...");
        update_lines();
        show_debug("Updated lines: "+lines_of_configuration.toString());
        actual_configuration.delete();
        actual_configuration = new File(CONFIGURATION_SRC);
        
        FileWriter config_writer = new FileWriter(CONFIGURATION_SRC);
        for ( String line : lines_of_configuration){
            config_writer.write(line+"\n");
        }
        config_writer.close();
        load_configuration();
        show_debug("Lines updated.");
    }
    /**
     * Configuration.update_field(String text,int mode)
     * @param text
     * @param mode 
     * Updates field of the configuration:
     *  mode:
     * 1 - date
     * 2 - debug
     * 3 - name
     * 4 - gui
     * 5 - checksum
     * 6 - ip
     */
    void update_field(String text,int mode) throws IOException{
        show_debug("Updating field: mode:"+Integer.toString(mode)+" text:"+text);
        /**
        *  %date     - date of made f the configuration
        *  %debug    - information about debug session
        *  %name     - name of the user
        *  %gui      - information about prefered interface
        *  %checksum - information about user checksum
        *  %ip       - ip for the later database option
        */


        // updating date
        if ( mode == 1){
            field_date = text;
        }
        // updating debug
        else if (mode == 2){
            field_debug = string_int(text);
        }
        // updating name
        else if (mode == 3){
            field_name = text;
        }
        // updating gui
        else if (mode == 4){
            field_gui = string_int(text);
        }
        // updating checksum
        else if (mode == 5){
            field_checksum = text;
        }
        // updating ip
        else if (mode == 6){
            field_ip = text;
        }
        save_file();
        show_debug("Field updated.");
    }
    /**
     * Configuration.get_checksum()
     * @return Integer
     * Function makes checksum for user
     */
    int get_checksum(){
       Random r = new Random(); 
       int to_ret = r.nextInt();
       return to_ret; 
    }
    /**
     * Configuration.make_new_configuration_file()
     * @throws IOException 
     * Function making new configuration file
     */
    void make_new_configuration_file() throws IOException{
        date_of_configuration = new Date();
        show_debug("Preparing new configuration file");
        FileWriter config_writer = new FileWriter(CONFIGURATION_SRC);
        config_writer.write("?ProgramNote configuration file\n?Do not edit\n");
        show_debug("Config file write - %date%"+date_of_configuration.toString()+"\n");
        config_writer.write("%date%"+date_of_configuration.toString()+"\n");
        show_debug("Config file write - %debug%1");
        config_writer.write("%debug%0\n");
        config_writer.write("%name%user\n");
        show_debug("Config file write - %name%user");
        config_writer.write("%gui%1\n");
        show_debug("Config file write - %gui%1");
        config_writer.write("%checksum%" + Integer.toString(get_checksum())+"\n");
        show_debug("Config file write - %checksum%" + Integer.toString(get_checksum())+"\n");
        config_writer.write("%ip%none\n");
        show_debug("Config file write - %ip%none");
        this.new_configuration = true;
        config_writer.close();
        actual_date = new Date();
        load_configuration();
    }
    /**
     * Configuration.string_int(String text)
     * @param text
     * @return Integer
     * Function returns -1 if text wasn't string. 
     */
    int string_int(String text){
        int ret = -1;
        try{
           ret = Integer.parseInt(text);
           return ret;
        }catch(NumberFormatException e){
            return ret;
        }
    }
    /**
     * Configuration.understand_lines(ArrayList<String> lines)
     * @param lines 
     * Function sets fileds for configuration
     */
    void understand_lines(ArrayList<String> lines){
        show_debug("Trying to understand configuration file..");
        for (String line : lines){
            if ( line.startsWith("?")){
                continue;
            }
            show_debug( "line is ? : "+line);
            String key = line.split("%")[1];
            String value = line.split("%")[2];
            /**
             *  %date     - date of made f the configuration
             *  %debug    - information about debug session
             *  %name     - name of the user
             *  %gui      - information about prefered interface
             *  %checksum - information about user checksum
             *  %ip       - ip for the later database option
             */
            if ( key.equals("date")){
                show_debug("        ? date");
                field_date = value;
            }
            else if (key.equals("debug")){
                show_debug("        ? debug");
                field_debug = string_int(value);
            }
            else if (key.equals("name")){
                show_debug("        ? name");
                field_name = value;
            }
            else if (key.equals("gui")){
                show_debug("        ? gui");
                field_gui = string_int(value);
            }
            else if (key.equals("checksum")){
                show_debug("        ? checksum");
                field_checksum = value;
            }
            else if (key.equals("ip")){
                show_debug("        ? ip");
                field_ip = value;
            }
        }
    }
    
    /**
     * Configuration.load_configuration()
     * Function loads configuration from readed lines
     */
    void load_configuration(){
        lines_of_configuration.clear();
        actual_date = new Date();
        show_debug("Loading configuration...");
        show_debug("Configuration src: "+CONFIGURATION_SRC);
        try (BufferedReader reader = new BufferedReader(new FileReader(CONFIGURATION_SRC))) {
            String line;
            while ( (line = reader.readLine()) != null){
                show_debug("Loading line: "+line);
                lines_of_configuration.add(line);
            }
            // lines loaded properly
            understand_lines(lines_of_configuration);
        }catch(Exception e){
            show_debug("Configuration file corrupted. ( "+e.toString()+" )");
            fail = true;
        }
    }
    /**
     * Configuration.check_configuration_file()
     * @return boolean
     * Function returns true if configuration file exists
     */
    boolean check_configuration_file(){
        try{
            actual_configuration = new File(CONFIGURATION_SRC);
            return actual_configuration.exists();
        }catch(Exception e){
            return false;
        }
    }       
    /**
     * Configuration show_debug(String text)
     * @param text 
     * Function prints debug info in console
     */
    void show_debug(String text){
        if (debug == 1){
            System.out.println("CONFIGURATION FILE ---> "+text);
            log.add("CONFIGURATION FILE ---> "+text);
        }
    }
    /**
     * Configuration.show_configuration()
     * Function prints all of the configuration
     */
    void show_configuration(){
        System.out.println("Configuration "+version);
        System.out.println("Loaded: "+actual_date.toString());
        System.out.println("Date of configuration: "+field_date);
        System.out.println("Debug is: "+Integer.toString(field_debug));
        System.out.println("Name of the user: "+field_name);
        System.out.println("Checksum: "+field_checksum);
        System.out.println("GUI is: "+ Integer.toString(field_gui));
        System.out.println("The ip of the database: "+ field_ip);
        show_debug("lines_of_configuration:");
        show_debug(lines_of_configuration.toString());
    }
}
