/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package programnote;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
/**
 *Object for maintaining and sharing notes and configurations
 * @author jakub
 */
public class Share_Check {
    
    User_Indentity logged;
    
    
    Date actual_date;
    
    Database_Connection database;
    
    // main constructor
    Share_Check(Database_Connection database){
        this.database = database;
        this.logged = database.actual_user;
    }
    
    /**
     * Share_Check.share(User_Indentity to_share,Note to_send)
     * @param to_share
     * @param to_send
     * @return boolean
     * @throws SQLException
     * Sets record to share
     */
    boolean share(int user_id_to_share,int note_id_to_send) throws SQLException{
        actual_date = new Date();
        String query = "insert into SHARE_HISTORY\n" +
                       "(user_id_from,user_id_to,note_id,share_history_date,share_history_done,share_type)\n" +
                       "VALUES\n" +
                       "(?,?,?,?,0,1)";
        PreparedStatement ps = database.con.prepareStatement(query);
        ps.setInt(1, logged.get_id());
        ps.setInt(2,user_id_to_share);
        ps.setInt(3,note_id_to_send);
        ps.setString(4,actual_date.toString());
        
        try{
            ps.execute();
            database.log.add("SHARE CHECK --- QUERY: "+ps.toString());
            database.log.add("SHARE CHECK --- Shared note");
            return true;
        }catch(SQLException e){
            database.log.add("SHARE CHECK ---E!!!! "+e.toString());
            System.out.println(e.toString());
            System.out.println(ps.toString());
            return false;
        }
        
    }
    
    /**
     * Share_Check.check_shares()
     * @throws SQLException 
     * @return integer
     * Function for checking shared note
     * Ret codes:
     * same as maintain_shares with one new:
     * -2 - Fail on the database
     */
    int check_shares() throws SQLException, IOException{
        String query = "SELECT * from SHARE_HISTORY where user_id_to = ? and share_history_done = 0;";
        PreparedStatement ps = database.con.prepareStatement(query);
        ps.setInt(1, logged.get_id());
        try{
            ResultSet rs = ps.executeQuery();
            database.log.add("SHARE CHECK --- Looking for shared notes...");
            // here we have our shares in resultset
             return maintain_shares(rs);    // coping sets to different user
            
        }catch(SQLException e){
            database.log.add("SHARE CHECK ---E!!! "+e.toString());
            return -2;
        }
    }
    /**
     * Share_Check.done_share(int share_history_id)
     * @param share_history_id
     * @throws SQLException 
     * Sets share_history_done to 1
     */
    void done_share(int share_history_id) throws SQLException{
        String query = "UPDATE SHARE_HISTORY set share_history_done = 1 where share_history_id = ?";
        PreparedStatement ps = database.con.prepareStatement(query);
        ps.setInt(1,share_history_id);
        try{
            ps.execute();
        }catch(SQLException e ){
            database.log.add(("SHARE CHECK ---E!!! "+e.toString()));
        }
        
    }
    
    /**
     * Share_Check.miantain_shares(ResultSet shares_set)
     * @param shares_set 
     * Function fo mantaining lefted shares
     * return codes:
     * 1 - at least one note shared
     * 0 - failed trying to share
     * -1 - nothing to share
     */
    int maintain_shares(ResultSet shares_set) throws SQLException, IOException{
        
        while ( shares_set.next() ){ // looping on not setted notes to share
            // setting main data to make share
            int note_id = shares_set.getInt("note_id");
            int user_id_to = shares_set.getInt("user_id_to");
            
            // loading note
            String note_query = "SELECT * FROM NOTE WHERE note_id = ?";
            PreparedStatement ps = database.con.prepareStatement(note_query);
            ps.setInt(1,note_id);
            ResultSet rs_note = ps.executeQuery();
            
            // loading hashtags
            String hashtags_query = "SELECT * FROM HASHTAGS WHERE note_id = ?";
            PreparedStatement ps_hashtags = database.con.prepareStatement(hashtags_query);
            ps_hashtags.setInt(1,note_id);
            ResultSet rs_hashtags = ps_hashtags.executeQuery();
            
            // loading content
            String content_query = "SELECT * FROM HASHTAGS WHERE note_id = ?";
            PreparedStatement ps_content = database.con.prepareStatement(content_query);
            ps_content.setInt(1,note_id);
            ResultSet rs_content = ps_content.executeQuery();
            
            
            Note to_share = new Note(rs_note,rs_content,rs_hashtags);
            try{
                database.put_note(to_share,user_id_to);
                done_share(shares_set.getInt("share_history_id"));
                return 1;
            }catch(Exception e){
                database.log.add("SHARE CHECK ---!!!E "+ e.toString());
                return 0;
            }
        }
        return -1;
    }
}
