package programnote;
import java.io.IOException;
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
    Configuration ac;
    String version = "v0.0.1";
    
    int debug = 1;
    ArrayList<String> log;
    
    
    Connection con = null;
    ResultSet rs = null;
    
    
    Database_Connection(Configuration c) throws SQLException, IOException{
        log = new ArrayList<>();
        ac = c;
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
        logout();
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
    /**
     * Database_Connection.log(String username,String password)
     * @param username
     * @param password
     * @return User_Indentity
     * @throws SQLException 
     * Function returns actual user
     */
    User_Indentity log (String username,String password) throws SQLException{
        ResultSet log = get_query("SELECT * FROM USER_INFO where user_login = '"+username+"' AND user_password = '"+password+"';");
        if ( log.next() ){
            show_debug("Logged user.");
            ac.user = new User_Indentity(log.getString("user_name"),log.getString("user_surname"),log.getInt("user_id"),log.getString("user_login"));
            ac.user.show_user();
            return new User_Indentity(log.getString("user_name"),log.getString("user_surname"),log.getInt("user_id"),log.getString("user_login"));
        }
        else{
            return null;
        }
    }
    /**
     * Database_Connection.logout()
     * Everything you have to do before logout
     */
    void logout(){
        show_debug("Logged out.");
        ac.user = null;
    }
    //--------------FUNCTIONS FOR GETTING DATA FROM THE DATABASE
//---gathering notes
    ArrayList<String> get_hashtags(int note_id) throws SQLException{
        ArrayList<String> to_ret = new ArrayList<>();
        String query = "SELECT * FROM HASHTAG WHERE note_id = ?";
        PreparedStatement prepSt = con.prepareStatement(query);
        prepSt.setInt(1, note_id);
        ResultSet rs = prepSt.executeQuery();        
        while ( rs.next() ){
            to_ret.add(rs.getString("hashtag_name"));
        }
        return to_ret;
    }
    String get_content(int content_id) throws SQLException{
        String query = "SELECT * FROM CONTENT WHERE content_id = ?";
        PreparedStatement prepSt = con.prepareStatement(query);
        prepSt.setInt(1, content_id);
        ResultSet rs = prepSt.executeQuery(); 
        if ( rs.next() ){
            return rs.getString("content_note");
        }
        return null;
    }
    
    ArrayList<Note> get_notes() throws SQLException, IOException{
        ArrayList<Note> to_ret = new ArrayList<>();
        String query = "SELECT * FROM NOTE WHERE user_id = ?";
        PreparedStatement prepSt = con.prepareStatement(query);
        prepSt.setInt(1, ac.user.get_id());
        
        ResultSet rs = prepSt.executeQuery();
        boolean database = true;
        while (rs.next()){
            //boolean database,String date,String checksum,
            //String name,String title,ArrayList<String> hashtags,String content
            Note to_add = new Note(database,rs.getString("note_date"),rs.getString("note_checksum"),
            rs.getString("note_title"),
                    get_hashtags(rs.getInt("note_id")),get_content(rs.getInt("note_id")));
            to_add.note_id_from_database = rs.getInt("note_id");
            to_ret.add(to_add);
        }
        return to_ret;
    }
    /**
     * Database_Connection.get_last_content()
     * @return int
     * @throws SQLException 
     * Getting the id of the last note content id the base
     */
    int get_last_content() throws SQLException{
        String query = "select *from CONTENT ORDER BY content_id DESC LIMIT 1;";
        ResultSet log = get_query(query);
        if ( log.next()){
            return log.getInt("content_id");
        }
        return -1;
    }
    /**
     * Database_Connection.get_last_note()
     * @return int
     * @throws SQLException 
     * Getting the id of the last note id the base
     */
    int get_last_note() throws SQLException{
        String query = "select *from NOTE ORDER BY note_id DESC LIMIT 1;";
        ResultSet log = get_query(query);
        if ( log.next()){
            return log.getInt("note_id");
        }
        return -1;
        }
    

    //--------------FUNCTIONS FOR PUTTING DATA INTO DATABASE
// --- adding note    
    void put_content(Note note_to_add) throws SQLException{
        String content_query = "INSERT INTO CONTENT\n"+
                               "(content_note)\n"+
                               "VALUES\n"+
                               "(?)";
        PreparedStatement prepSt = con.prepareStatement(content_query);
        prepSt.setString(1, note_to_add.field_note_content);
        prepSt.execute();
        show_debug(prepSt.toString());
        show_debug("Added note content to database");
    }
    void put_hashtags(Note note_to_add) throws SQLException{
        String hashtags_query = "INSERT INTO HASHTAG\n" +
                                "(user_id,note_id,hashtag_name)\n" +
                                "VALUES\n" +
                                "(?,?,?);";
        for ( String hashtag : note_to_add.list_of_hashtags){
            PreparedStatement prepSt = con.prepareStatement(hashtags_query);
            prepSt.setInt(1, ac.user.get_id());
            prepSt.setInt(2, get_last_note());
            prepSt.setString(3,hashtag);
            prepSt.execute();
            show_debug(prepSt.toString());
            show_debug("Added hashtag: "+hashtag+" to the database");
        }
        show_debug("Added hashtags to database");
    }
    /**
     * Database_Connection.put_note(Note note_to_add)
     * @param note_to_add
     * @throws SQLException 
     * Function add note to the database
     */
    void put_note(Note note_to_add) throws SQLException{
        String note_query = "INSERT INTO NOTE\n" +
                            "(user_id,content_id,note_date,note_checksum,note_title)\n" +
                            "VALUES\n" +
                            "(?,?,?,?,?)";
        put_content(note_to_add);
        PreparedStatement prepSt = con.prepareStatement(note_query);
        prepSt.setInt(1,ac.user.get_id());
        prepSt.setInt(2,get_last_content());
        prepSt.setString(3,note_to_add.field_date);
        prepSt.setString(4, ac.user.prepare_checksum());
        prepSt.setString(5, note_to_add.field_title);
        prepSt.execute();
        put_hashtags(note_to_add);
        show_debug(prepSt.toString());
        show_debug("Added note to database");
    }
    //--------------FUNCTIONS FOR DELETING DATA INTO DATABASE
// --- deleting notes
    
    
    
    
    
    //--------------------------------------------------------------------------
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
