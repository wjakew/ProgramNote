/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */

package programnote;

import java.io.File;
import java.util.ArrayList;

/**
 *Class FileWalker - for preparing list of files in the note directory.
 * NOTE: searches all of the path src, all dirs in the directory too
 * @author jakub
 */
public class FileSearcher { 
   
    String version = "v 1.0.4";
    int debug = 0;          // setting the debug option
    ArrayList<String> log;  // collection of log data
    
    String actual_path;     // coping the src for the path to search
    File root;                  // copy of the File object of the src
    File[] list_of_files;                       // list of all the files in the directory
    ArrayList<String> list_of_notes;            // collection of found files
    
    FileSearcher(String path,int debug){
        
        actual_path = path;
        this.debug = debug;
        log = new ArrayList<>();
        
        
        list_of_notes = new ArrayList<>();
        
        if ( new File(actual_path).exists() ){          // checks if directory exists
            search(actual_path);
        }
        else {
            list_of_notes = null;           // if directory didnt exist setting null
        }
        
    }
    /**
     * FileSearcher.dir_to_delete()
     * @param dir_to_delete
     * @return boolean
     * Function deletes given directory.
     */
    boolean delete_directory(String dir_to_delete){
        show_debug("Trying to delete file: "+dir_to_delete+"...");
        if(!dir_to_delete.equals(actual_path)){
            if (list_of_notes.contains(dir_to_delete)){
                show_debug("Deleting file: "+dir_to_delete);
                File todelete = new File(dir_to_delete);
                todelete.delete();
                search(actual_path);
                return true;
            }
            else{
                show_debug("There is no file such as: " + dir_to_delete);
                return false;
                }
            }
        else{
            show_debug("Error, trying to delete home directory");
            return false;
            }
    }

    /**
     * FileSearcher.search(String path)
     * @param path 
     * Finding all note files in the directory. (Stores in list_of_files collection)
     */
    public void search( String path ) { 

        root = new File( path ); 
        list_of_files = root.listFiles(); 

        for ( File f : list_of_files ) { 
            if ( f.isDirectory() ) { 
                search( f.getAbsolutePath() ); 
                show_debug( "Directory:" + f.getAbsoluteFile() ); 
            } 
            else { 
                show_debug( "File path:" + f.getAbsoluteFile() ); 
                
                if ( f.getAbsoluteFile().toString().contains("programnote_")){
                    list_of_notes.add(f.getAbsoluteFile().toString());
                }
            } 
        }
        show_list_of_files();
    }
    /**
     * FileSearcher.show_debug()
     * @param text 
     * Function for showing the debug data if debug field is 1.
     */
    void show_debug(String text){
        if( debug == 1){
            System.out.println("FILESEARCHER DEBUG INFO - : "+text);
        }
        log.add("FILESEARCHER DEBUG INFO - : "+text);
    }
    /**
     * FileSearcher.show_list_of_files()
     * Function shows debug data of the content of list_of_notes collection.
     */
    void show_list_of_files(){
        if( debug == 1){
            System.out.println("FILESEARCHER "+version);
            System.out.println("List of paths to notes: ");
            for (String src : list_of_notes){
                System.out.println(src);
            }
            System.out.println("END.");
        }
    }
} 
