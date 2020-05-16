/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package programnote;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 *Note_Collector - object for managing notes
 * @author jakub
 */
public class Note_Collector {
    Configuration actual_configuration = new Configuration();
    String version = "v 1.0.3";
    int debug = actual_configuration.ret_debug_info();
    int mode = actual_configuration.ret_mode_info();    // mode = 0 - local
                                                        // mode = 1 - database
    
    String main_path;                       // copy of the src path
    FileSearcher search_engine;             // searchning note engine
    LogGrabber log;                         // for log storing
    ArrayList<Note> actual_notes;           // collection of the notes
    Database_Connection db;                 // connector for the database

    Date date_of_reload;
    /**
     * Note_Collector constructor.
     * @param path 
     * Initialization all of the components.
     */
    Note_Collector(String path) throws IOException, FileNotFoundException, ParseException, SQLException{
        // init of the components
        main_path = path;
        log = new LogGrabber();
        search_engine = new FileSearcher(main_path,debug);
        db = new Database_Connection(actual_configuration);
        actual_notes = new ArrayList<>();
        load_notes();
    }
    /**
     * Note_Collector.reload()
     * @throws IOException
     * @throws FileNotFoundException
     * @throws ParseException 
     * Function reloads whole object
     */
    void reload() throws IOException, FileNotFoundException, ParseException, SQLException{
        load_notes();
    }
    /**
     * Note_Collector.load_notes()
     * @throws IOException 
     * Function load notes and store them in actual_notes collection
     */
    void load_notes() throws IOException, FileNotFoundException, ParseException, SQLException{
        if (mode == 0){
            actual_notes.clear();
            search_engine = new FileSearcher(main_path,debug);
            for (String src : search_engine.list_of_notes){
                Note to_add = new Note(src,1,debug);
                if (!to_add.new_file){
                    actual_notes.add(to_add);
                }
            }
            date_of_reload = new Date();
        }
        else if (mode == 1){
            log.add("Loading notes from database...");
            db.log(actual_configuration.field_database_login, actual_configuration.field_database_password);
            actual_notes = db.get_notes();
        }
    }
    /**
     * Note_Collector.get_note(int number)
     * @param number
     * @return Note
     * Function returns note in exact index number, if number is out of index
     * range returns null
     */
    Note get_note(int number){
        if (get_size() > number){
            return actual_notes.get(number);
        }
        else{
            return null;
        }
    }
    /**
     * Note_Collector.in_range()
     * @param number
     * @return boolean
     * Function returns true if number is in range.
     */
    boolean in_range(int number){
        if( get_note(number)!=null){
            return true;
        }
        return false;
    }
    /**
     * Note_Collector.get_size()
     * @return int
     * Function returns size of the actual_notes collection.
     */
    int get_size(){
        return actual_notes.size();
    }
    // functions for managing notes
    /**
     * Note_Collector.add_note_to_collection()
     * @param to_add 
     * Function add note to collection
     */
    void add_note_to_collection(Note to_add) throws IOException, FileNotFoundException, ParseException, SQLException{
        if (mode == 1){
            db.put_note(to_add);
            }
        load_notes();
        
    }
    /**
     * Note_Collector.delete_note(String src_to_delete)
     * @param src_to_delete
     * Function delete note by given path
     */
    void delete_note(String src_to_delete) throws IOException, FileNotFoundException, ParseException, SQLException{
        if (mode == 0){
            search_engine.delete_directory(src_to_delete);
        }
        else if ( mode == 1){
            
        }
        load_notes();
    }
    /**
     * Note_Collector.delete_note(int index)
     * @param index
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     * @throws ParseException 
     * Func        if ( mode == 0 ){// local modetion delete note by given index
     */
    boolean delete_note(int index) throws IOException, FileNotFoundException, ParseException, SQLException{
        if ( in_range(index) ){
            Note to_delete = actual_notes.get(index);
            search_engine.delete_directory(to_delete.note_src);
            load_notes();
            return true;
        }
        else{
            return false;
        }
        
    }
    // end of functions for managing notes
    /**
     * Note_Collector.show_collection()
     * Function prints all of the stored notes.
     * modes:
     * 1 - standard without information
     * 2 - with date
     * 3 - with checksum
     * 4 - with hashtags
     */
    void show_collection(int mode){
        System.out.println("NoteCollector "+version+" list of notes:");
        if (actual_notes.isEmpty()){
            System.out.println("No notes to show.");
        }
        else{
            for (Note n : actual_notes){
                if (mode == 1){
                    System.out.println(Integer.toString(actual_notes.indexOf(n))+":"+n.show_desc());
                }
                if (mode == 2){
                    System.out.println(Integer.toString(actual_notes.indexOf(n))+":"+n.show_desc() + " - "+n.field_date);
                }
                if (mode == 3){
                    System.out.println(Integer.toString(actual_notes.indexOf(n))+":"+n.show_desc() + " - "+n.field_checksum);
                }
                if (mode ==4){
                    System.out.println(Integer.toString(actual_notes.indexOf(n))+":"+n.show_desc() + " - "+n.list_of_hashtags.toString());
                }
            }
        }

    }
    /**
     * Note_Collector.close()
     * @throws IOException 
     * Closing, log making and saving notes.
     */
    void close() throws IOException, ParseException{
        for (Note n : actual_notes){
            log.add_array(n.log);
            n.close_note();
        }
        log.add_array(db.log);
        log.add_array(search_engine.log);
        log.kill();
    }
}
