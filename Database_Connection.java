package programnote;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.ResultSet;

/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */

/**
 *Object for maintaining and editing database
 * @author jakub
 */
public class Database_Connection {
    String version = "v0.0.1";
    
    int debug = 1;
    ArrayList<String> log;
    
    
    Connection con = null;
    ResultSet rs = null;
    
    
    Database_Connection() throws SQLException{
        log = new ArrayList<>();
        
        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost/programnote_database?" +
                                   "user=root&password=password");
            show_debug("Connected: "+con.toString());
            
        }catch(SQLException ex){
            show_debug("SQLException: " + ex.getMessage());
            show_debug("SQLState: " + ex.getSQLState());
            show_debug("VendorError: " + ex.getErrorCode());
            
        }
    }
    void close() throws SQLException{
        con.close();
    }
    /**
     * Database_Connection.get_query()
     * @param query
     * @return ResultSet
     * @throws SQLException
     * Function returns ResultSet from database
     */
    ResultSet get_query(String query) throws SQLException{
        try{
            show_debug("Trying to get query: "+query);
            PreparedStatement ps = con.prepareStatement(query);
            return ps.executeQuery();
        }catch (SQLException ex){
            show_debug("Getting ResultSet return fail");
            show_debug("SQLException: " + ex.getMessage());
            show_debug("SQLState: " + ex.getSQLState());
            show_debug("VendorError: " + ex.getErrorCode());
            return null;
        }
    }
    //--------------LOGGING AS USER
    int log (String username,String password) throws SQLException{
        ResultSet log = get_query("SELECT * FROM USER_INFO where user_login = '"+username+"' AND user_password = '"+password+"'");
        return log.getInt("user_id");
    }
    
    //--------------FUNCTIONS FOR GETTING DATA FROM THE DATABASE
    
    /**
     * Function for showing debug
     * @param text 
     */
    void show_debug(String text){
        if (debug == 1){
            System.out.println("DATABASE CONNECTION DEBUG---->" + text);
        }
        log.add("DATABASE CONNECTION DEBUG---->" + text);
    }
    
}
