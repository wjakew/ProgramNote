/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package programnote;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 *LogGrabber - object for storing and editing all the logs.
 * @author jakub
 */
public class LogGrabber {
    String version = "v 1.0.0";
    
    final static String NAME_OF_THE_PROGRAM = "programnote";    // static for name of the program to log
    
    ArrayList<String> list_of_logs; // collection for storing all logs
    File log_file;      // file for log
    String log_src;     // src path to file
    FileWriter writer;  // writer object for writting to file
    
    Date actual_date = new Date();      // actual date
    
    /**
     * Constructor of the class. 
     * @throws IOException 
     */
    LogGrabber() throws IOException{     
        init();     // inits the variables
        
    }
    /**
     * LogGrabber.add(String log_string)
     * @param log_string 
     * Function add string to the collection.
     */
    void add(String log_string){
        list_of_logs.add(log_string);
    }
    /**
     * LogGrabber.write()
     * @throws IOException
     * Function writes to file all of the records and clears the collection.
     */
    void write() throws IOException{
        for (String log : list_of_logs){
            writer.write(log);
        }
        list_of_logs.clear();
    }
    /**
     * LogGrabber.init()
     * @throws IOException 
     * Function initializes of the variables, files and writers to file.
     */
    void init() throws IOException{
        list_of_logs = new ArrayList<>();       
        log_src = NAME_OF_THE_PROGRAM + "-" +actual_date.toString()+".txt";
        log_file = new File(log_src);
        writer = new FileWriter(log_src);
        writer.write("new log ( "+NAME_OF_THE_PROGRAM+" ) date: "+actual_date.toString());
    }
    /**
     * LogGrabber.kill()
     * @throws IOException
     * Function closes files and writes the rest of the log.
     */
    void kill() throws IOException{
        write();
        writer.write("end of the log. date: "+actual_date.toString());
        writer.close();
    }
    
}
