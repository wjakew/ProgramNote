/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package programnote;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
 *  %user_id  - id of the current user
 *  %gui      - information about prefered interface
 *  %checksum - information about user checksum
 *  %ip       - ip for the later database option
 *  %database_login
 *  %database_password
 *  %mode
 */
public class Configuration {
    final String[] options = {"%date%","%debug%","%name%","%user_id%","%gui%","%checksum%","%ip%","%database_password%","%database_login%","%mode%"};
    final String version = "v1.0.2";               // version of the module
    final String CONFIGURATION_SRC = "./programNote_configuration.txt";     // path to configuration src
    boolean new_configuration = false;      // field if new configuration was made
    boolean fail = false;
    int debug ;                              // field for debug
    
    String field_date = "";
    int field_debug = -1;
    String field_name = "";
    int field_gui = -1;
    int user_id =-1;
    int mode = 0;
    String field_checksum = "";
    String field_ip = "";
    String field_database_login;
    String field_database_password;
    
    
    User_Indentity user = null;
    
    Date date_of_configuration;
    Date actual_date;
    ArrayList<String> log;
    File actual_configuration; 
    ArrayList<String> lines_of_configuration;
    
    
    Configuration() throws IOException{
        if (new File(CONFIGURATION_SRC).exists()){
            this.debug = get_fast_debug_info(); 
        }
        else{
            this.debug = 1;
        }    
        log = new ArrayList<>();
        lines_of_configuration = new ArrayList<>();
        // checking if configuration file is set
        if ( check_configuration_file() ){  // we find configuration file
            load_configuration();
            if ( user != null){
                field_checksum = user.prepare_checksum();
                save_file();
            }
        }
        else{   // we need to make new configuration file
            make_new_configuration_file();
        }
    }
    int get_fast_debug_info() throws FileNotFoundException, IOException{
        BufferedReader reader = new BufferedReader(new FileReader(CONFIGURATION_SRC));
        String line;
        while ( (line = reader.readLine()) != null){
            if (line.contains("%debug%")){
                return Integer.parseInt(line.split("%")[2]);
            }
        }
        return 1;
    }
    //------------------------FUNCTIONS FOR GETTING DATA FROM CONFIGURATION
    int ret_debug_info(){
        if (check_parity()){
            show_debug("Parity checked.");
            return field_debug;
        }
        else{
            return -1;
        }
    }
    int ret_gui_info(){
        if (check_parity()){
            show_debug("Parity checked.");
            return field_gui;
        }
        else{
            return -1;
        }
    }
    int ret_mode_info(){
        if (check_parity()){
            show_debug("Parity checked.");
            return mode;
        }
        else{
            return -1;
        }
    }
            
    //--------------------END OF FUNCTIONS FOR GETTING DATA FROM CONFIGURATION
    /**
     * Configuration.check_parity()
     * @return boolean
     * Function checks if every option is set in the object
     */
    boolean check_parity(){
        show_debug("Checking parity...");
        int check = 0;
        for (String option : options){
            for (String line : lines_of_configuration){
                if (line.contains(option)){
                    check++;
                }
            }
        }
        show_debug("Parity status check: "+Integer.toString(check));
        return check == options.length;
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
        lines_of_configuration.add("%user_id%"+user_id);
        lines_of_configuration.add("%gui%"+Integer.toString(field_gui));
        lines_of_configuration.add("%checksum%"+field_checksum);
        lines_of_configuration.add("%ip%"+field_ip);
        lines_of_configuration.add("%database_login%"+field_database_login);
        lines_of_configuration.add("%database_password%"+field_database_password);
        lines_of_configuration.add("%mode%"+Integer.toString(mode));
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
        else if (mode == 6){
            field_gui = string_int(text);
        }
        // updating user_id
        else if (mode == 4){
            user_id = string_int(text);
        }
        // updating checksum
        else if (mode == 5){
            field_checksum = text;
        }
        // updating ip
        else if (mode == 7){
            field_ip = text;
        }
        else if (mode == 8){
            field_database_password = text;
        }
        else if (mode == 9){
            field_database_login = text;
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
        config_writer.write("%user_id%-1\n");
        show_debug("Config file write - %name%user");
        config_writer.write("%gui%1\n");
        show_debug("Config file write - %gui%1");
        config_writer.write("%checksum%" + Integer.toString(get_checksum())+"\n");
        show_debug("Config file write - %checksum%" + Integer.toString(get_checksum())+"\n");
        config_writer.write("%ip%none\n");
        show_debug("Config file write - %ip%none");
        config_writer.write("%database_login%none\n");
        show_debug("Config file write - %database_login%none");
        config_writer.write("%database_password%none\n");
        show_debug("Config file write - %database_password%none");
        config_writer.write("%mode%0\n");
        show_debug("Config file write - %mode%0");
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
     * Function sets fields for configuration
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
            else if (key.equals("user_id")){
                show_debug("        ? user_id");
                user_id = string_int(value);
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
            else if (key.equals("database_password")){
                show_debug("        ? database_password");
                field_database_password = value;
            }
            else if (key.equals("database_login")){
                show_debug("        ? database_login");
                field_database_login = value;
            }
            else if (key.equals("mode")){
                show_debug("        ? mode");
                mode = string_int(value);
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
        }
        log.add("CONFIGURATION FILE ---> "+text);
    }
    /**
     * Configuration.show_configuration()
     * Function prints all of the configuration
     */
    void show_configuration(){
        System.out.println("Configuration "+version);
        System.out.println("Loaded: "+actual_date.toString());
        System.out.println("1 -Date of configuration: "+field_date);
        System.out.println("2 -Debug is: "+Integer.toString(field_debug));
        System.out.println("3 -Name of the user: "+field_name);
        System.out.println("4 -User id: "+Integer.toString(user_id));
        System.out.println("5 -Checksum: "+field_checksum);
        System.out.println("6 -GUI is: "+ Integer.toString(field_gui));
        System.out.println("7 -The ip of the database: "+ field_ip);
        System.out.println("8 -Login of the user:"+field_database_login);
        System.out.println("9 - Password of the user: ***************");
        System.out.println("10 - Mode of the program: "+Integer.toString(mode));
        show_debug("lines_of_configuration:");
        show_debug(lines_of_configuration.toString());
    }
}
