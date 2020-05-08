package programnote;
import java.sql.Statement;
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
    Statement stmt = null;
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
            if (stmt.execute(query)) {
                show_debug("Getting query with succes");
                return stmt.getResultSet();
            }
            else{
                return null;
            }
        }catch (SQLException ex){
            show_debug("Getting ResultSet return fail");
            show_debug("SQLException: " + ex.getMessage());
            show_debug("SQLState: " + ex.getSQLState());
            show_debug("VendorError: " + ex.getErrorCode());
            return null;
        }
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
