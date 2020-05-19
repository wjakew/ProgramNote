package programnote;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.text.ParseException;

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
    String version = "v1.0.0";
    
    int debug = 1;
    ArrayList<String> log;
    
    boolean logged_as_user = false;
    
    User_Indentity actual_user = null;
    
    Connection con = null;
    ResultSet rs = null;
    
    
    Database_Connection(Configuration c) throws SQLException, IOException{
        log = new ArrayList<>();
        ac = c;
        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost/programnote_database?" +
                                   "user=root&password=password");
            show_debug("Connected: "+con.toString());
            
            if ( !ac.field_database_login.equals("none") ){
                log(ac.field_database_login,ac.field_database_password);
            }
            
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
    User_Indentity log (String username,String password) throws SQLException, IOException{
        ResultSet log = get_query("SELECT * FROM USER_INFO where user_login = '"+username+"' AND user_password = '"+password+"';");
        if ( log.next() ){
            show_debug("Logged user.");
            ac.user = new User_Indentity(log.getString("user_name"),log.getString("user_surname"),log.getInt("user_id"),log.getString("user_login"));
            ac.user.show_user();
            actual_user = ac.user;
            ac.update_field(username, 9);
            ac.update_field(password, 8);
            logged_as_user = true;
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
        actual_user = null;
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
    /**
     * Database_Connection.load_notes()
     * @return
     * @throws SQLException
     * @throws IOException 
     * Loads all notes from the database
     */
    ArrayList<Note> load_notes() throws SQLException, IOException{
        ArrayList<Note> to_ret = new ArrayList<>();
        String query = "SELECT * FROM NOTE WHERE user_id = ?";
        PreparedStatement prepSt = con.prepareStatement(query);
        prepSt.setInt(1, actual_user.get_id());
        
        ResultSet rs = prepSt.executeQuery();
        boolean database = true;
        while (rs.next()){
            //boolean database,String date,String checksum,
            //String name,String title,ArrayList<String> hashtags,String content
            Note to_add = new Note(true,rs.getString("note_date"),rs.getString("note_checksum"),
            rs.getString("note_title"),
                    get_hashtags(rs.getInt("note_id")),get_content(rs.getInt("note_id")));
            to_add.note_id_from_database = rs.getInt("note_id");
            to_ret.add(to_add);
        }
        return to_ret;
    }
    /**
     * Database_Connection.download_note(int note_id)
     * @param note_id
     * @throws SQLException
     * @throws IOException
     * @throws ParseException 
     * Downloads note from the database
     */
    boolean download_note(int note_id) throws SQLException, IOException, ParseException{
        String query = "SELECT * FROM NOTE WHERE note_id = ?";
        PreparedStatement prepSt = con.prepareStatement(query);
        prepSt.setInt(1, note_id);
        ResultSet rs = prepSt.executeQuery();
        
        if ( rs.next() ){
            show_debug("Downloading note...");
            Note downloaded = new Note(true,rs.getString("note_date"),rs.getString("note_checksum"),
            rs.getString("note_title"),
                    get_hashtags(rs.getInt("note_id")),get_content(rs.getInt("note_id")));
            downloaded.note_id_from_database = rs.getInt("note_id");
            downloaded.prepare_file();
            downloaded.update_records();
            downloaded.show_note();
            downloaded.write_to_file();
            show_debug("Note downloaded");
            return true;
        }
        show_debug("Download cant be done");
        return false;
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
    int get_index_of_the_note(String checksum,String date,String title) throws SQLException{
        String query = "SELECT NOTE_ID FROM NOTE WHERE note_checksum = ? "
                + "and note_date = ? and user_id = ? and note_title = ?;";
        PreparedStatement prepSt = con.prepareStatement(query);
        prepSt.setString(1,checksum);
        prepSt.setString(2, date);
        prepSt.setInt(3,actual_user.get_id());
        prepSt.setString(4, title);
        ResultSet rs = prepSt.executeQuery();
        
        if ( rs.next() ){
            return rs.getInt("note_id");
        }
        return -1;
    }
    
    Handler get_mail_cred() throws SQLException{
        String handler_query = "SELECT addons_n1,addons_n2 FROM ADDONS where addons_id = 1;";
        PreparedStatement prepSt = con.prepareStatement(handler_query);
        ResultSet rs = prepSt.executeQuery();
        if ( rs.next() ){
            Handler h = new Handler();
            h.put(rs.getString("addons_n1"), 1);
            h.put(rs.getString("addons_n2"), 2);
            return h;
        }
        return null;
    }
    
    void get_config(int user_id) throws SQLException, IOException{
        String configuration_query = "SELECT * FROM CONFIGURATION WHERE user_id = ?;";
        PreparedStatement prepSt = con.prepareStatement(configuration_query);
        prepSt.setInt(1,actual_user.get_id());
        ResultSet rs = prepSt.executeQuery();
        
        if (rs.next()){
            Configuration got = new Configuration();
            got.load_from_database(rs.getString("configuration_date"), rs.getInt("configuration_debug"), rs.getInt("configuration_gui")
                    , actual_user.get_id(), 1, rs.getString("configuration_checksum"),
                    rs.getString("configuration_ip"), actual_user.get_login());
        }
    }
    boolean check_if_configuration(int user_id) throws SQLException{
        String query = "SELECT * FROM CONFIGURATION WHERE user_id = ?;";
        PreparedStatement prepSt = con.prepareStatement(query);
        prepSt.setInt(1,user_id);
        ResultSet rs = prepSt.executeQuery();
        return rs.next();
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
            prepSt.setInt(1, actual_user.get_id());
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
        prepSt.setInt(1,actual_user.get_id());
        prepSt.setInt(2,get_last_content());
        prepSt.setString(3,note_to_add.field_date);
        prepSt.setString(4, actual_user.prepare_checksum());
        prepSt.setString(5, note_to_add.field_title);
        prepSt.execute();
        put_hashtags(note_to_add);
        show_debug(prepSt.toString());
        show_debug("Added note to database");
    }
    /**
     * Database_Connection.put_configuration(Configuration to_add)
     * @param to_add
     * @throws SQLException 
     * Function puts configuration to the database without fields password,login and mode
     */
    void put_configuration(Configuration to_add) throws SQLException{
        if ( check_if_configuration(actual_user.get_id())){
            delete_configuration(actual_user.get_id());
        }
        show_debug("Trying to load configuration to the database");
        String config_query = "INSERT INTO CONFIGURATION\n" +
                              "(user_id,configuration_date,configuration_debug,configuration_name,configuration_gui,configuration_checksum,configuration_ip)\n" +
                              "VALUES\n" +
                              "(?,?,?,?,?,?,?);";
        PreparedStatement prepSt = con.prepareStatement(config_query);
        prepSt.setInt(1, actual_user.get_id());
        prepSt.setString(2,ac.field_date);
        prepSt.setInt(3,ac.debug);
        prepSt.setString(4, ac.field_name);
        prepSt.setInt(5,ac.field_gui);
        prepSt.setString(6, ac.field_checksum);
        prepSt.setString(7,ac.field_ip);
        prepSt.execute();
        show_debug(prepSt.toString());
        show_debug("Added configuration to database");
    }
    //--------------FUNCTIONS FOR UPDATING DATA IN DATABASE
    /**
     * Database_Connection.update_content(String content,int note_id)
     * @param content
     * @param note_id
     * @throws SQLException 
     * Function updates content of the note
     */
    void update_content(String content,int note_id) throws SQLException{
        show_debug("Updating content of the note:"+Integer.toString(note_id));
        String query = "update CONTENT set content_note = ? "
                + "where content_id = ?;";
        PreparedStatement prepSt = con.prepareStatement(query);
        prepSt.setString(1,content);
        prepSt.setInt(2,note_id);
        prepSt.execute();
        show_debug(prepSt.toString());
        show_debug("Updated");
    }
    /**
     * Database_Connection.offload_notes(ArrayList<Note> to_offload)
     * @param to_offload
     * @throws SQLException 
     * Function for offloading notes into database.
     */
    void offload_notes(ArrayList<Note> to_offload) throws SQLException{
        show_debug("Starting offloading notes to the database...");
        int amount = to_offload.size();
        int i = 1;
        for (Note n : to_offload){
            show_debug("Note "+Integer.toString(i)+"/"+Integer.toString(amount));
            put_note(n);
            i++;
        }
        if ( i == amount ){
            show_debug("Notes loaded into database");
        }
        else{
            show_debug("Error in the process");
        }
    }
    //--------------FUNCTIONS FOR DELETING DATA INTO DATABASE
// --- deleting configuration
    /**
     * Database_Connection.delete_configuration(int user_id)
     * @param user_id
     * @throws SQLException 
     * Function for deleting configuration.
     */
    void delete_configuration(int user_id) throws SQLException{
        String query = "DELETE  FROM CONFIGURATION WHERE user_id = ?;";
        PreparedStatement prepSt = con.prepareStatement(query);
        prepSt.setInt(1,user_id);
        prepSt.execute();
        show_debug("Row from table configuration deleted. User id: "+Integer.toString(user_id));
    }
    
    void delete_hashtag(int note_id) throws SQLException{
        String query = "DELETE FROM HASHTAG WHERE note_id = ?";
        PreparedStatement prepSt = con.prepareStatement(query);
        prepSt.setInt(1,note_id);
        prepSt.execute();
        show_debug("Deleted hashtags from note, note id: "+Integer.toString(note_id));
    }
    
    void delete_content(int content_id) throws SQLException{
        String query = "DELETE FROM CONTENT WHERE content_id = ?";
        PreparedStatement prepSt = con.prepareStatement(query);
        prepSt.setInt(1,content_id);
        prepSt.execute();
        show_debug("Deleted content from note, note id: "+Integer.toString(content_id));
    }
    /**
     * 
     * @param note_id
     * @throws SQLException 
     */
    void delete_note(int note_id) throws SQLException{
        delete_hashtag(note_id);
        String query = "DELETE FROM NOTE WHERE note_id = ?";
        PreparedStatement prepSt = con.prepareStatement(query);
        prepSt.setInt(1,note_id);
        prepSt.execute();
        delete_content(note_id);
    }
    
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
