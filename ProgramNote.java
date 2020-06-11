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
import java.util.Date;
import javax.mail.MessagingException;

/**
 *
 * @author jakub
 */
public class ProgramNote {
    
    static String version = "v1.2.0beta";
    int test = 3;
    static int set_interface;

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, ParseException, SQLException, MessagingException {
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
            System.out.println("---------------------------------------------------");
            Database_Connection db = new Database_Connection(actual_configuration);
            Note n = new Note("programnote_agdaga-Wed May 13 21:41:55 CEST 2020",1,1);
            db.log("wjakew", "test");
            Handler h = db.get_mail_cred();
            MailSender m = new MailSender(h,n,"kubawawak@gmail.com");
            m.run();
            System.out.println("---------------------------------------------------");
        }
        // closing files and logs
        nc.close();
    }
    
}
