/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package programnote;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *User_interface
 * @author jakub
 * Console interface for ProgramNote
 */
public class User_interface {
    Configuration actual_config = new Configuration();
    String interface_version = "v.1.0.1";
    
    Note_Collector engine;
    BufferedReader reader;      // buffered reader for reading user input
    boolean run = true;         // boolean for main loop
    String interface_input;         // variable for storing user input
    
    Database_Connection database = null;
    
    ArrayList<String> input_history;        // colection for storing user input
    
    /**
     * User_interface constructor
     * @param version
     * @throws IOException 
     */
    User_interface(Note_Collector engine) throws IOException{
        
        reader = new BufferedReader(new InputStreamReader(System.in));
        input_history = new ArrayList<>();
        this.engine = engine;
        
    }
    /**
     * User_interface.run()
     * @throws IOException 
     * Function holds main logic of the module.
     */
    void run() throws IOException, ParseException, SQLException{
        interface_print("User Interface version "+interface_version);
        System.out.println();
        interface_print("Time of the reload: "+engine.date_of_reload.toString());
        interface_print("type 'help' for additional information");
        while(run){
            interface_input = interface_get();
            interface_logic(interface_input);
        }   
    }
    /**
     * User_interface.quit()
     */
    void quit() throws SQLException{
        database.close();
        database = null;
        
    }
    /**
     * User_interface.interface_logic(String input)
     * @param input 
     * Function 
     */
    void interface_logic(String input) throws IOException, ParseException, FileNotFoundException, SQLException{
        
        String[] words = input.split(" ");
        List<String> word_list = Arrays.asList(words);
        interface_db_print("echo: "+word_list.toString());
        interface_db_print("echo length: "+Integer.toString(word_list.size()));
        for(String word : words){
            
            // exiting interface
            if( word.equals("exit")){
                interface_print("User interface exit.");
                quit();
                run = false;
                break;
            }
            // getting help to user
            else if ( word.equals("help")){
                if ( words.length < 2){
                    UI_function_get_some_help("");
                    break;
                }
                else{
                    UI_function_get_some_help(words[1]);
                    break;
                }
            }
            // showing list of notes
            else if ( word.equals("list") ){
                
                UI_function_list_notes(word_list);
                break;
            }
            // showing notes
            else if ( word.equals("show")){
                UI_function_show_notes(word_list);
                break;
            }
            // making notes
            else if (word.equals("note")){
                UI_function_note(word_list);
                break;
            }
            // reloading note base
            else if (word.equals("reload")){
                UI_function_reload();
                break;
            }
            else if (word.equals("config")){
                UI_function_config_edit(word_list);
                break;
            }
            else if (word.equals("gui")){
                interface_print("Launching GUI...");
                new GUI_main_window(engine);
                interface_print("GUI launched");
                break;
            }
            else if (word.equals("database")){
                UI_function_database(word_list);
                break;
            }
            // wrong action input
            else{
                interface_print("Wrong command");
            }
        }   
    }
    /**
     * User_interface.interface_get();
     * @return String
     * @throws IOException 
     * Interface for getting user input.
     */
    String interface_get() throws IOException{
        System.out.print(">");
        String input = reader.readLine();
        this.engine.log.add("user_interface user input: "+input);
        return input;
        
    }
    /**
     * User_interface.interface_print()
     * @param text 
     * Interface for printing data to console.
     */
    void interface_print(String text){
        System.out.println(text);
    }
    /**
     * User_interface.interface_db_print(String text)
     * @param text 
     * Interface for printing debug to the console
     */
    void interface_db_print(String text){
        if (engine.debug == 1){
            System.out.println("INTERFACE DEBUG-->"+text);
            this.engine.log.add("INTERFACE DEBUG-->"+text);
        }
    }
    //--------------functions of the user interface
    /**
     * User_interface.UI_function_get_some_help(String add)
     * @param add 
     * Prints help for the user.
     */
    
    void UI_function_get_some_help(String add){
        
        // print all help
        if (add.equals("")){
            interface_print("User interface help:");
            interface_print("------------");
            interface_print("gui:");
            interface_print("      ( starting graphical interface )");
            interface_print("------------");
            interface_print("note:");
            interface_print("   note -add");
            interface_print("       -add            ( adding new note with note creator )");
            interface_print("       -add name title ( adding note with name and title )");
            interface_print("       -add -b         ( adding blank note )");
            interface_print("   ---------------");
            interface_print("   note -del");
            interface_print("   note -del           ( deleting newest note ) ");
            interface_print("   note -del -number   ( deleting note numbered from the list )");
            interface_print("   ---------------");
            interface_print("   note -upd");
            interface_print("        -upd -option -number");
            interface_print("        options:");
            interface_print("           -name       ( updates name of the note )");
            interface_print("           -title      ( updates title of the note )");
            interface_print("           -content    ( updates content of the note )");
            interface_print("------------");
            interface_print("list:");
            interface_print("   list -a");
            interface_print("        -a / list      ( listing all notes from the home directory )");
            interface_print("        -p path        ( listing all notes from the path user select)");
            interface_print("------------");
            interface_print("show:");
            interface_print("   show -number        ( showing note of the number )");
            interface_print("   show -config        ( showing actual config )");
            interface_print("------------");
            interface_print("reload:");
            interface_print("   reload              ( reloading whole base of notes");
            interface_print("------------");
            interface_print("config:");
            interface_print("config     -number value  ( edits configurtion file )");
            interface_print("------------");
            interface_print("database:");
            interface_print("   database            ( shows actual login and status of the connection");
            interface_print("       -connect        ( connect to the database )");
            interface_print("       -login login password ( logs to the database )");
            interface_print("       -load           ( loads notes from database to the computer )");
            interface_print("       -load -c        ( loads config to the program ) ");
            interface_print("       -offload        ( loads local notes to the database )");
            interface_print("       -offload -c     ( loads actual config into database ) ");
            interface_print("       -quit           ( quitting the conntection ) ");
            interface_print("------------");
        }
        else if (add.equals("-note")){
            interface_print("Help for note:");
            interface_print("   note -add");
            interface_print("       -add            ( adding new note with note creator )");
            interface_print("       -add name title ( adding note with name and title )");
            interface_print("       -add -b         ( adding blank note )");
            interface_print("   ---------------");
            interface_print("   note -del");
            interface_print("   note -del           ( deleting newest note ) ");
            interface_print("   note -del -number   ( deleting note numbered from the list )");
            interface_print("   ---------------");
            interface_print("   note -upd");
            interface_print("        -upd -option -number");
            interface_print("        options:");
            interface_print("           -name       ( updates name of the note )");
            interface_print("           -title      ( updates title of the note )");
            interface_print("           -content    ( updates content of the note )");
        }
        else if (add.equals("-list")){
            interface_print("Help for list:");
            interface_print("   list -a");
            interface_print("        -a             ( listing all notes from the home directory )");
            interface_print("        -p path        ( listing all notes from the path user select)");
            interface_print("        -date          ( listing all notes with dates of made )");
            interface_print("        -hashtags      ( listing all notes with hashtags )");
            interface_print("        -checksum      ( listing all notes with checksums");
        }
        else if (add.equals("-show")){
            interface_print("Help for show:");
            interface_print("   show -number        ( showing note of the number )");
            interface_print("   show -config        ( showing actual config )");
        }
        else if (add.equals("-database")){
            interface_print("Help for database:");
            interface_print("   database            ( shows actual login and status of the connection");
            interface_print("       -connect        ( connect to the database )");
            interface_print("       -login login password ( logs to the database )");
            interface_print("       -load           ( loads notes from database to the computer )");
            interface_print("       -load -c        ( loads config to the program ) ");
            interface_print("       -offload        ( loads local notes to the database )");
            interface_print("       -offload -c     ( loads actual config into database ) ");
            interface_print("       -quit           ( quitting the conntection ) ");
        }
        else if (add.equals("-reload")){
            interface_print("Help for reload:");
            interface_print("   reload              ( reloading whole base of notes");
        }
    }
    /**
     * User_interface.UI_function_config_edit(List<String> add)
     * @param add
     * @throws IOException 
     * Function for editing program config
     */
    void UI_function_config_edit(List<String> add) throws IOException{
        int number = ret_int(add);
        
        if ( number != -1 ){
            String value = add.get(2);
            
            if(value.startsWith("-")){
                value = value.substring(0);
            }
            interface_print("Are you sure to change value number: "+Integer.toString(number)+" to: "+value+"? (y/n)");
            String ans = interface_get();
            if (ans.equals("y")){
                actual_config.update_field(value, number);
                interface_print("Updated");
            }
            else if (ans.equals("n")){
                interface_print("Canceled");
            }
            else{
                interface_print("Wrong input");
            }
        }
        else{
            interface_print("Wrong option");
        }
    }
    /**
     * User_interface.UI_function_list_notes(String add)
     * @param add 
     * Function shows notes
     */
    void UI_function_list_notes(List<String> add){
        if ( add.get(0).equals("list") && add.size() == 1){

            engine.show_collection(1);
            
        }
        else{
            int index = 0;
            if ( add.contains("-p")){ 
                for (String s: add){
                    if (s.contains("/")){
                        break;
                    }
                    index++;
                }
                interface_print("Found path: "+ add.get(index));
            }
            else if ( add.contains("-date")){
                engine.show_collection(2);
            }
            else if ( add.contains("-hashtag")){
                engine.show_collection(4);
            }
            else if ( add.contains("-checksum")){
                engine.show_collection(3);
            }
            else{
                interface_print("Wrong option.");
            }
        }
    }
    /**
     * User_interface.ret_int(List<String> data)
     * @param data
     * @return int
     * Function finds in list integer and returns it.
     * If List didn't have an integer returns -1
     */
    int ret_int(List<String> data){
        int ret=-1;
        for(String d : data){
            if ( d.contains("-")){
                d = d.substring(1);
            }
            try{
                ret = Integer.parseInt(d);
                break;
            }catch(NumberFormatException e){
                ret = -1;
            }
        }
        return ret;
    }
    /**
     * User_interface.check_int(String a)
     * @param a
     * @return boolean
     * Function for checking if string is a number.
     */
    boolean check_int(String a){
        try{
            Integer.parseInt(a);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }
    /**
     * User_interface.UI_function_show_notes(List<String> add)
     * @param add
     * @throws IOException 
     * Function shows details of given note
     */
    void UI_function_show_notes(List<String> add) throws IOException{
        if (add.get(0).equals("show") && add.size() == 1){
            interface_print("No additional arguments. See help"); 
        }
        else{
            int index = ret_int(add);
            if ( index != -1){
                if (engine.in_range(index)){
                    engine.get_note(index).show_note();
                }
                else{
                    interface_print("Wrong option.");
                    }
                }
            else{
                if (add.get(1).contains("config")){
                    actual_config.show_configuration();
                }
                else{
                    interface_print("Wrong option.");
                }
                
            }
        }
    }
    /**
     * User_interface.UI_function_reload()
     * @throws IOException
     * @throws FileNotFoundException
     * @throws ParseException 
     * Function reloading whole note base
     */
    void UI_function_reload() throws IOException, FileNotFoundException, ParseException, SQLException{
        engine.reload();
        interface_print("Note base reloaded : "+engine.date_of_reload.toString());
    }
    /**
     * User_interface.UI_function_note(List<String> add)
     * @param add 
     * Function for editing notes
     */
    void UI_function_note(List<String> add) throws IOException, ParseException, FileNotFoundException, SQLException{
        /**
         * Functionality to develop:
         * note:
         *      -add              ( adding new note with note creator )");        DONE
         *      -add -name -title ( adding new note with given name and title )   DONE
         *      -add -b           ( adding blank note )                           DONE
         *
         *      note -del");
         *      note -del           ( deleting newest note ) ");                DONE
         *      note -del -number   ( deleting note numbered from the list )"); DONE
         *      ---------------");
         *  note -upd
         *      -upd -option -number"
         *      -name       ( updates name of the note )
         *      -title      ( updates title of the note )
         *      -content    ( updates content of the note )
        }
         */
        // note
        if ( add.get(0).equals("note") && add.size()==1){
            interface_print("No additional argument. See help.");
        }
        // note -add
        else if ( !add.contains("-b") && add.contains("-add") && add.size() == 2){
            Note note_to_add = new Note();
            engine.add_note_to_collection(note_to_add);
        }
        // note -add name title
        else if ( add.contains("-add") && add.size() > 3 ){
            Note note_to_add = new Note(add.get(2),add.get(3));
            engine.add_note_to_collection(note_to_add);
        }
        //note -add -b
        else if ( add.contains("-add") && add.contains("-b") && add.size() == 3){
            Note note_to_add = new Note("blank");
            engine.add_note_to_collection(note_to_add);
            interface_print("Blank note was made");
        }
        // note -del
        else if ( add.contains("-del") && add.size()==2){
            interface_print("Are you sure to delete note? (y/n)");
            String q = interface_get();
            if (q.equals("y")){
                engine.delete_note(0);
                interface_print("Newest note deleted.");
            }
            else if (q.equals("n")){
                interface_print("Canceled.");
            }
            else{
                interface_print("Wrong input");
            }
        }
        // note -del -number
        else if ( add.size()>2 && ret_int(add)!=-1 && add.contains("-del") ){
            interface_print("Are you sure to delete note number ("+add.get(2)+") ? (y/n)");
            String q = interface_get();
            if (q.equals("y")){
                engine.delete_note(ret_int(add));
                interface_print("Note numbered ("+Integer.toString(ret_int(add))+") deleted.");
            }
            else if (q.equals("n")){
                interface_print("Canceled.");
            }
            else{
                interface_print("Wrong input");
            }
        }
        // note -upd
        else if (  add.contains("-upd") && add.size() == 2){
            interface_print("No arguments.");
            interface_print("        -upd -option -number");
            interface_print("        options:");
            interface_print("           -name       ( updates name of the note )");
            interface_print("           -title      ( updates title of the note )");
            interface_print("           -content    ( updates content of the note )");
        }
        // note -upd -name -number
        else if ( add.contains("-upd") && add.contains("-name") && add.size() == 4){
            interface_print("New name: ");
            String n_name = interface_get();
            if ( !n_name.isEmpty() ){
                engine.actual_notes.get(ret_int(add)).update_name(n_name);
                interface_print("Name updated");
            }
            else{
                interface_print("Wrong name");
            }
        }
        // note -upd -title -number
        else if ( add.contains("-upd") && add.contains("-title") && add.size() == 4){
            interface_print("New name: ");
            String n_name = interface_get();
            if ( !n_name.isEmpty() ){
                engine.actual_notes.get(ret_int(add)).update_title(n_name);
                interface_print("Title updated");
            }
            else{
                interface_print("Wrong title");
            }
        }
        // note -upd -title -number
        else if ( add.contains("-upd") && add.contains("-content") && add.size() == 4){
            interface_print("New name: ");
            String n_name = interface_get();
            if ( !n_name.isEmpty() ){
                engine.actual_notes.get(ret_int(add)).update_content(n_name);
                interface_print("Content updated");
            }
            else{
                interface_print("Wrong content");
            }
        }
    }
    
    void UI_function_database(List<String> add) throws SQLException, IOException, FileNotFoundException, ParseException{
        /**
         * Functionality to develop
           database            ( shows actual login and status of the connection )
            -connect           ( connect to the database )
            -login login password ( logs to the database )
            -load           ( loads notes from database to the computer )
            -load -c        ( loads config to the program ) 
            -offload        ( loads local notes to the database )
            -offload -c     ( loads actual config into database ) 
            -offload -n number ( loads note to the database )
            -quit           ( quitting the conntection ) 
         */
        
        // database
        if ( add.contains("database") && add.size() == 1){
            if ( database == null){
                interface_print("Database not connected");
                interface_print("Type login to connect");
            }
            else{
                interface_print("Database connected");
                interface_print(database.con.toString());
            }
        }
        else if ( add.contains("-connect") && add.size() == 2){
            database = new Database_Connection(actual_config);
        }
        // database -login login password
        else if ( add.contains("-login") && add.size() == 4){
            if ( database == null ){
                interface_print("Database is not connected");
            }
            else{
                String login = add.get(2);
                String password = add.get(3);
                interface_print("Trying to log as : "+login);
                database.log(login, password);
            }
        }
        // -load           
        else if ( add.contains("-load") && add.size() == 2){
            if ( database == null ){
                interface_print("Database is not connected");
            }
            else{
                engine.mode = 1;
                engine.load_notes();
            }
        }
        // -load -c
        else if ( add.contains("-load") && add.contains("-c")){
            if ( database != null ){
                interface_print("Loading configuration to the database...");
                database.put_configuration(actual_config);
            }
        }
        // -offload
        else if ( add.contains("-offload") && add.size() == 2){
            if ( database != null ){
                interface_print("Loading notes to database..");
                database.offload_notes(engine.actual_notes);
            }
        }
        // wrong input
        else {
            interface_print("Wrong option");
        }
    }
}
