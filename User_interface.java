/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package programnote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *User_interface
 * @author jakub
 * Console interface for ProgramNote
 */
public class User_interface {
    String interface_version = "v.0.0.1B";
    
    Note_Collector engine;
    BufferedReader reader;      // buffered reader for reading user input
    boolean run = true;         // boolean for main loop
    String interface_input;         // variable for storing user input
    
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
    void run() throws IOException{
        interface_print("User Interface version "+interface_version);
        System.out.println();
        interface_print("type 'help' for additional information");
        while(run){
            interface_input = interface_get();
            interface_logic(interface_input);
        }   
    }
    /**
     * User_interface.interface_logic(String input)
     * @param input 
     * Function 
     */
    void interface_logic(String input) throws IOException{
        
        String[] words = input.split(" ");
        List<String> word_list = Arrays.asList(words);
        
        for(String word : words){
            
            // exiting interface
            if( word.equals("exit")){
                interface_print("User interface exit.");
                run = false;
            }
            //getting help to user
            else if ( word.equals("help")){
                if ( words.length < 2){
                    UI_function_get_some_help("");
                }
                else{
                    UI_function_get_some_help(words[1]);
                }
            }
            //showing list of notes
            else if ( word.equals("list") ){
                
                UI_function_list_notes(word_list);
            }
            //showing notes
            else if ( word.equals("show")){
                UI_function_show_notes(word_list);
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
            interface_print("        -upd           ( updating newest note");
            interface_print("        -upd -number   ( updating note numbered from the list )");
            interface_print("------------");
            interface_print("list:");
            interface_print("   list -a");
            interface_print("        -a / list      ( listing all notes from the home directory )");
            interface_print("        -p path        ( listing all notes from the path user select)");
            interface_print("------------");
            interface_print("show:");
            interface_print("   show -number        ( showing note of the number )");
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
            interface_print("        -upd           ( updating newest note");
            interface_print("        -upd -number   ( updating note numbered from the list )");
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
     * User_interface.UI_function_show_notes(List<String> add)
     * @param add
     * @throws IOException 
     * Function shows details of given note
     */
    void UI_function_show_notes(List<String> add) throws IOException{
        if (add.get(0).equals("show") && add.size() == 1){
            engine.show_collection(1);
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
                interface_print("Wrong option.");
            }
        }
    }

}
