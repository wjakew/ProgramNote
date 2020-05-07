/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programnote;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/**
 *
 * @author jakub
 */
public class ProgramNote {
    
    static String version = "v1.0.0";
    int test = 3;
    static int set_interface;

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
        Configuration actual_configuration = new Configuration();
        set_interface = actual_configuration.ret_gui_info();
        System.out.println("ProgramNote "+version);
        Note_Collector nc = new Note_Collector("./");
        if ( set_interface == 1){
            new User_interface(nc).run();
        }
        else if ( set_interface == 2){
            new GUI_main_window(nc);
        }
        else if ( set_interface == 3){
            
        }
        // closing files and logs
        nc.close();
    }
    
}
