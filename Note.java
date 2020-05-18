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
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *Class Note. - object that stores notes.
 * @author jakub wawak
 * Files gonna be stored in 'notes' folder
 * Idea of the file:
 * ---------------------------------------------------------------------------
 * #sf#                                               <---- start of file
 * #date# ----date----                                <---- date of make
 * #checksum# ----data----                            <---- checksum, unique user id
 * #name# ----data----                                <---- name of the file
 * #title# ----data----                               <---- title of the file
 * #hashtags# ----data----                            <---- hashtags, main keys
 * #content#                                          <---- content of the note
 * #/content#                                         <---- end of content
 * #/sf#                                              <---- end of file
 */
public class Note {
    Configuration actual_configuration = new Configuration();
    String version = "v 1.1.8";
    int debug = actual_configuration.ret_debug_info();
    ArrayList<String> log;
    // data section
    
    // startup data
    String note_src = "";                // copy of file path of the file
    File note_file = null;               // object that stores file
    boolean new_file = false;            // set false if note didnt find file and has to make new one
    String note_name = "";               // name of the note from context
    
    
    Date actual_date = new Date();
    
    // data gathered during run
    List<String> records = null;               // list stores lines of file
    boolean source_fail = false;               // flag if file has wrong structure
    boolean updated = false;                   // flag if data of the note was updated 
    boolean stopped = false;                   // flag if creator was stopped during make
    boolean from_gui = false;
    int number_of_lines = 0;
    
    // database flags
    int note_id_from_database = 0;
    boolean note_from_database = false;
    
    // data stored from the file ( for speed )
    String field_date = "";
    String field_checksum = "";
    String field_name = "";
    String field_title = "";
    ArrayList<String> list_of_hashtags = new ArrayList<>();
    ArrayList<String> list_of_content = new ArrayList<>();
    String field_note_content = "";
    Date field_date_date = null;
    
    Note(String date,String checksum,String name,String title,ArrayList<String> hashtags,String content) throws IOException, ParseException{
        try{
            log = new ArrayList<>();
            new_file = true;
            records = new ArrayList<>();
            field_date = date;
            field_checksum = checksum;
            field_name = name;
            field_title = title;
            list_of_hashtags = hashtags;
            field_note_content = content;
            content_line_folding(field_note_content);
            note_src = "programnote_"+field_name+"-"+actual_date.toString();
            prepare_file();
            update_records();
            write_to_file();
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
    Note(boolean database,String date,String checksum,String title,ArrayList<String> hashtags,String content) throws IOException{
        log = new ArrayList<>();
        note_from_database = database;
        field_date = date;
        field_checksum = checksum;
        field_name = title+"_database";
        field_title = title;
        list_of_hashtags = hashtags;
        field_note_content = content;
        content_line_folding(field_note_content);
        note_src = "databaseCopy_programnote_"+field_name+"-"+actual_date.toString();
    }
    
    /**
     * Constructor of the object
     * @param src
     * @param mode
     * @throws FileNotFoundException
     * @throws IOException 
     */
    Note(String src,int mode,int debug) throws FileNotFoundException, IOException, ParseException{
        
        this.note_src = src;                       // coping file path 
        //this.debug = debug;
        log = new ArrayList<>();
        
        if ( mode == 1 ){                          // opening existing file
            if ( this.check_if_exists(note_src) ){ // file exists 
                    new_file = false;
                    open_file();
            }
            else{
                make_file();
            }
        }
        if ( mode == 2 ){                          // we need to make file
            make_file();
        }
    }
    /**
     * Constructor without arguments.
     * @throws IOException
     * @throws ParseException 
     */
    Note() throws IOException, ParseException{
        this.debug = 1;
        records = new ArrayList<>();
        log = new ArrayList<>();
        new_file = true;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome in the Note creator!");
        update_date();
        System.out.println("Set the name of the note:");
        update_name(reader.readLine());
        // simple exit
        if(!field_name.equals("exit")){
            System.out.println("Set the title of the note:");
            update_title(reader.readLine());
            System.out.println("Set the checksum:");
            update_checksum(reader.readLine());
            System.out.println("Create content:");
            update_content(reader.readLine());
            System.out.println("-----");
            System.out.println("\n\n Set hashtags: (separate with commas,left blank to add nothing)");
            String input = reader.readLine();
            if ( !input.isEmpty()){
                for (String hashtag : input.split(",")){
                    add_hashtag(hashtag);
                }
            }
            note_src = "programnote_"+field_name+"-"+actual_date.toString();
            prepare_file();
            update_records();
            write_to_file();
        }
        else{
            stopped = true;
            System.out.println("Creator exited");
        }
    }
    /**
     * Constructor with two arguments 
     * @param name
     * @param title
     * @throws ParseException
     * @throws IOException 
     */
    Note(String name,String title) throws ParseException, IOException{
        this.debug = 1;
        records = new ArrayList<>();
        log = new ArrayList<>();
        new_file = true;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome in the Note creator!");
        update_date();
        update_name(name);
        update_title(title);
        System.out.println("Set the checksum:");
        update_checksum(reader.readLine());
        System.out.println("Create content:");
        update_content(reader.readLine());
        System.out.println("-----");
        System.out.println("\n\n Set hashtags: (separate with commas,left blank to add nothing)");
        String input = reader.readLine();
        if ( !input.isEmpty()){
            for (String hashtag : input.split(",")){
                add_hashtag(hashtag);
            }
        }
        note_src = "programnote_"+field_name+"-"+actual_date.toString();
        prepare_file();
        update_records();
        write_to_file();
    }
    /**
     * Constructor with one argument
     * @param checksum
     * @throws ParseException
     * @throws IOException
     * @throws IOException 
     * Making blank note.
     */
    Note(String checksum) throws ParseException, IOException {
        this.debug = 1;
        records = new ArrayList<>();
        log = new ArrayList<>();
        new_file = true;
        update_date();
        update_name("blank");
        update_title("blank - "+actual_date.toString());
        update_checksum(checksum);
        update_content("blank");
        add_hashtag("blank");
        note_src = "programnote_"+field_name+"-"+actual_date.toString();
        prepare_file();
        update_records();
        write_to_file();
    }
    //-------------------functions for updating stuff in the note
    void update() throws IOException, ParseException{
        if ( updated == true){
            write_to_file();
        }
    }
    void update_date() throws ParseException{
        actual_date = new Date();
        field_date = actual_date.toString();
        prepare_Date_object();
        updated = true;
    }
    void update_checksum(String text){
        field_checksum = text;
        updated = true;
    }
    void update_name(String text){
        field_name = text;
        updated = true;
    }
    void update_title(String text){
        field_title = text;
        updated = true;
    }
    // functions for editing hashtags
    void clear_hashtag(){
        list_of_hashtags.clear();
        updated = true;
    }
    void add_hashtag(String hashtag){
        if (!list_of_hashtags.contains(hashtag)){
            list_of_hashtags.add(hashtag);
            updated = true;
        }
    }
    void delete_hashtag(String hashtag){
        if (list_of_hashtags.contains(hashtag)){
            list_of_hashtags.remove(hashtag);
            updated = true;
        }
    }
    void update_content(String text){
        if ( from_gui ){
            field_note_content = text;
        }
        field_note_content = text;
        updated = true;
        
        // folding lines and putting to collection
        content_line_folding(text);
    }
    //---------------end---of---functions for updating stuff in the note
    /**
     * Note.content_line_folding(String text)
     * @param text 
     * Function fold lines. Breaks to 30 character lines content of the content string.
     */
    void content_line_folding(String text){
        list_of_content.clear();
        String line = "";
        int line_break = 0;
     
        for ( String word : field_note_content.split(" ")){
            if (line_break < 30){
                line = line +" "+ word;
                line_break = line_break + word.length();
                if ( field_note_content.split(" ")[field_note_content.split(" ").length-1].equals(word)){
                    list_of_content.add(line);
                    break;
                }
            }
            else{
                list_of_content.add(line);
                line = "";
                line_break = 0;
            }
        }
    }
    /**
     * Note.make_storage()
     * Function that copies data to fields in the object.
     */
    void make_storage() throws ParseException{
        field_date = get_from_line("#date#");
        show_debug("loaded field date : "+field_date);
        field_checksum = get_from_line("#checksum#");
        show_debug("loaded field checksum : "+field_checksum);
        field_name = get_from_line("#name#");
        show_debug("loaded field name : "+field_name);
        field_title = get_from_line("#title");
        show_debug("loaded field title : "+field_title);
        for(String hash : get_from_line("#hashtags#").split(",")){
            list_of_hashtags.add(hash);
        }
        show_debug("loaded hashtags : "+list_of_hashtags.toString());
        field_note_content = get_content();
        show_debug("loaded content : "+field_note_content);
        show_debug("loaded content (list) : "+list_of_content.toString());
        prepare_Date_object();
    }
    /**
     * Note.check_date_field()
     * @return boolean
     * @throws ParseException
     * Function returns true if field date is a correct date.
     */
    boolean check_date_field() throws ParseException{
        try{
            show_debug("Checking date object...");
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
            String date = field_date.split(" ")[2]+"-"+field_date.split(" ")[1]+"-"
                    +field_date.split(" ")[5]+" "+field_date.split(" ")[3];
            show_debug("Prepared date object: "+date);
            formatter.parse(date);
            show_debug(date + " - is correct");
            return true;
        }catch (Exception e){
            show_debug("Field date isn't correct");
            return false;
        }
    }
    /**
     * Note.prepare_Date_object()
     * @throws ParseException 
     * Function for making date object.
     */
    void prepare_Date_object() throws ParseException{
        boolean check_date_field;
        if ( !check_date_field() ){
            show_debug("Preparing data object failed");
        }
        else{
            show_debug("Preparring date object...");
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
            String date = field_date.split(" ")[2]+"-"+field_date.split(" ")[1]+"-"
                    +field_date.split(" ")[5]+" "+field_date.split(" ")[3];
            show_debug("Prepared date object: "+date);
            field_date_date = formatter.parse(date);
        }
    }
    /**
     * Note.make_file()
     * @throws IOException
     * Function makes file for note.
     */
    void make_file() throws IOException, ParseException{
        show_debug("Making new file ("+note_src+")...");
        note_file = new File(note_src);
        records = new ArrayList<>();
        new_file = true;
        prepare_file();
        write_to_file();
    }
    /**
     * Note.open_file()
     * Function opens and prepares file.
     */
    void open_file() throws IOException, ParseException{
        show_debug("Opening file ("+note_src+")...");
        records = read_file(note_src);  // reading lines of file
        if (check_parity()){          // checking if file is prepared for use in program
            show_debug("Check complete. OK.");
            // preparing file to open
            make_storage();
        }
        else{                           // file is wrong
            source_fail = true;
            show_debug("Check complete. FAIL.");
            this.number_of_lines = records.size();
            show_debug_info();
        }
    }
    /**
     * Note.update_records()
     * Function for updating records array.
     */
    void update_records(){
        /**
        * Idea of the file:
        * ---------------------------------------------------------------------------
        * #sf#                                               <---- start of file
        * #date# ----date----                                <---- date of make
        * #checksum# ----data----                            <---- checksum, unique user id
        * #name# ----data----                                <---- name of the file
        * #title# ----data----                               <---- title of the file
        * #hashtags# ----data----                            <---- hashtags, main keys
        * #content#                                          <---- content of the note
        * #/content#                                        <---- end of content
        * #/sf#                                              <---- end of file
        */
        records.clear();
        records.add("#sf#"+"\n");
        records.add("#date#"+ field_date+"\n");
        records.add("#checksum#"+ field_checksum+"\n");
        records.add("#name#"+field_name+"\n");
        records.add("#title#"+field_title+"\n");
        String hashtags = "";
        for (String hashtag : list_of_hashtags){
            hashtags = hashtags + hashtag + ",";
        }
        records.add("#hashtags#"+hashtags+"\n");
        records.add("#content#"+"\n");
        if ( !list_of_content.isEmpty() ){
            for(String line : list_of_content){
                records.add(line+"\n");
            }
        }
        else{
            records.add(field_note_content);
        }
        records.add("#/content#"+"\n");
        records.add("#/sf#"+"\n");
        show_debug("reloaded records (list) : "+records.toString()+"\n");
    }
    /**
     * Note.close_note()
     * @throws IOException 
     * Function closes note. Updating records and writing to file.
     */
    void close_note() throws IOException, ParseException{
        if ( updated == true ){
            show_debug("Closing note. New updates");
            update_records();
            write_to_file();
        }
        else{
            show_debug("Closing note. No new updates.");
        }
    }
    /**
     * Note.write_to_file()
     * @throws IOException 
     * Function writing records array to file.
     * NOTE: records array need to be updated
     */
    void write_to_file() throws IOException, ParseException{
        update_records();
        if (note_file != null){
            show_debug("Deleting old file...");
            note_file.delete();             // deleting old file
        }
        note_file = new File(note_src); // making new one
        show_debug("Making new file...");
        FileWriter myWriter_toFile = new FileWriter(note_src);
        show_debug("Starting writing to file...");
        for( String record : records ){
            myWriter_toFile.write(record);
            show_debug("Writing : "+record);
        }
        myWriter_toFile.close();
        updated = false;
        open_file();
    }
    /**
     * Note.get_content()
     * @return String
     * Function returns content of note.
     */
    String get_content(){
        String content = "";
        int index = -1;
        for ( String line : records ){ //find index of the start
            if (line.contains("#content#")){
                index = records.indexOf(line);  // getting index of the start of content
                break;
            }
        }
        index++;    //getting over '#content#'
        for (int i = index; i < records.size()-1;i++){
            if( records.get(i).contains("#/content#")){
                break;
            }
            list_of_content.add(records.get(i));
            content = content + records.get(i) +"\n";
        }
        return content;
    }
    /**
     * Note.show_debug_info()
     * Function that shows info from file
     */
    void show_debug_info(){
        if( debug == 1 ){
            System.out.println("ProgramNote - Note Module version: "+version);
            log.add("ProgramNote - Note Module version: "+version);
            System.out.println(actual_date.toString());
            System.out.println("Loaded file: " + note_src);
            log.add("Loaded file: " + note_src);
            System.out.println("Number of lines loaded: "+Integer.toString(number_of_lines));
            log.add("Number of lines loaded: "+Integer.toString(number_of_lines));
            if ( source_fail == true){
                System.out.println("File isn't Note file.");
                log.add("File isn't Note file.");
            }
            System.out.println("Showing raw content of the file:");
            show_content_of_file();
            System.out.println("Debug info end.");
        }
        
    }
    /**
     * Note.show_note()
     * Shows content of the note.
     */
    void show_note(){
        System.out.println("\nDate of made: " + field_date);
        System.out.println("     Name of the file ( path ) :" +note_src+"\n");
        System.out.println(" Checksum: " + field_checksum);
        System.out.println(" Name of the note: "+ field_name);
        System.out.println(" Title of the note: "+field_title);
        System.out.println(" Hashtags : " + list_of_hashtags.toString()+"\n");
        System.out.println("---------------NOTE---CONTENT");
        System.out.println(field_note_content);
        System.out.println("---------------NOTE---CONTENT---END");
    }
    /**
     * Note.show_debug(String note)
     * @param note 
     * Prints in console developer data if debug is 1
     */
    void show_debug(String note){
        if ( debug == 1){
            System.out.println("!!!!!!!DEBUG NOTE - - - "+note);
        }
        log.add("!!!!!!!DEBUG PRINT - - - "+note);
    }
    /**
     * Note.ret_hashtags_to_GUI()
     * @return String
     * Returns string contains all hashtags
     */
    String ret_hashtags_to_GUI(){
        String to_ret = "";
        for(String hashtag : list_of_hashtags){
            to_ret = to_ret + hashtag +",";
        }
        return to_ret;
    }
    /**
     * Note.prepare_file()
     * Prepares List of key words for adding content.
     */
    void prepare_file (){
        records.add("#sf#");
        records.add("#date#"+actual_date.toString());
        records.add("#checksum#");
        records.add("#name#");
        records.add("#title#");
        records.add("#hashtags#");
        records.add("#content#");
        records.add("#/content#");
        records.add("#/sf# ");  
    }
    /**
     * Note.show_desc()
     * @return String
     * Prepares custom label for user interface.
     */
    String show_desc(){
        //return field_name + "-"+custom_date();
        return note_src;
    }
    /**
     * Note.show_content_of_file()
     * Function shows content of the file.
     */
    void show_content_of_file(){
        for (String line : records){
            System.out.println(line);
        }
    }
    /**
     * Note.custom_date()
     * @return String
     * Functions customizing date output.
     */
    String custom_date(){
        String[] cst = actual_date.toString().split(" ");
        // NUMDAY MONTH (DAY) HOUR:MINUTES
        String ret = cst[2]+ " " + cst[1] + " ("+cst[0]+") "+cst[3].substring(0,4);
        return ret;
    }
    /**
     * Note.get_from_line(String header)
     * @param header
     * @return String / null
     * Returns value of the key
     */
    String get_from_line(String header){
        String to_ret;
        for ( String line: records ){
            if (line.contains(header)){
                line = line.strip();
                if ( get_index_of_hash(line) == -1){
                    return line;
                }
                return line.substring(get_index_of_hash(line));
            }
        }
        return null;
    }
    /**
     * Note.get_index_of_hash(String line)
     * @param line
     * @return Integer
     * Function returns index of second hash
     */
    int get_index_of_hash(String line){
        int index = -1;
        boolean hash = false;
        
        for(int i = 0 ; i < line.length()-1; i++){
            if ( line.charAt(i) == '#'){
                if ( hash == false){
                    hash = true;
                }
                else{
                    return ++i;
                }
            } 
        }
        return index;
    }
    /**
     * Note.check_parity()
     * @return boolean
     * Checks if file contains right data
     */
    boolean check_parity(){
        number_of_lines = records.size();
        show_debug("number_of_lines = "+Integer.toString(number_of_lines));
        for (String line : records){
            show_debug("line.split  = " + line.split(" ")[0]);
            if ( line.split(" ")[0].equals("#sf#")){
                if ( number_of_lines >= 9){
                    if ( records.get(records.size()-1).contains("#/sf#")){
                        show_debug("eof line  = " + records.get(records.size()-1));
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * Note.blank_cleaner(String to_clear
     * @param to_clear
     * Function clears empty spaces from the end of the string
     */
    String blank_cleaner(String to_clear){
        int last_index = -1;
        for ( int i = to_clear.length()-1; i >=0; i--){
            if ( to_clear.charAt(i) != ' '){
                last_index = i;
                break;
            }
        }
        return to_clear.substring(0,last_index);
    }
    /**
     * Note.read_file(String src)
     * @param src
     * @return List
     * @throws FileNotFoundException
     * @throws IOException 
     * Method returns all lines of the file
     */
    List read_file ( String src ) throws FileNotFoundException, IOException{
        
        List<String> lines = new ArrayList<>();     //stores lines for a moment
        
        try (BufferedReader reader = new BufferedReader(new FileReader(note_src))) {
            String line;
            while ((line = reader.readLine()) != null)
            {
                line = line.strip();
                lines.add(line);
            }
        } catch (Exception e){
            //here exception reading to console for now
            System.out.println("Note - file error.");
        }
        
        if ( !lines.isEmpty() ){        //checking if file is empty
            return lines;
        }
        else
            return null;
    }
    /**
     * Note.check_if_exists(String src)
     * @param src
     * @return boolean
     * Function for checking if file exists.
     */
    boolean check_if_exists(String src){
        
        try{
           note_file = new File(src);
         
           return note_file.exists();
           
        }catch( Exception e ){
            //file do not exists
            System.out.println("Note - file doesn't exist.");
            return false;
        }
    }
    
    /**
     * Note.compare(Note to_compare)
     * @param to_compare
     * @return boolean
     * Function for comparing notes
     */
    boolean compare(Note to_compare){
        if(to_compare.field_note_content.equals(field_note_content) && to_compare.field_date.equals(field_date)
                && to_compare.field_title.equals(field_title)){
            return true;
        }
        return false;
    }
}
