/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programnote;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

/**
 *
 * @author jakub
 */
public class GUI_main_window extends javax.swing.JFrame {
    Configuration actual_configuration = new Configuration();
    String version = "v1.0.0";
    Note_Collector engine;
    
    // program states
    boolean editable = false;
    boolean new_note = false;
    
    // models for gui
    DefaultListModel model = new DefaultListModel();
    
    // window data
    Note actual_note = null;
    
    int debug = actual_configuration.get_fast_debug_info();;
    ArrayList<String> log;
    /**
     * Creates new form GUI_main_window
     */
    public GUI_main_window(Note_Collector engine) throws IOException {
        super ( "ProgramNote" );
        this.engine = engine;
        log = new ArrayList<String>();
        initComponents();
        jLabel3.setText(version);
        //setting welcome set of the window
        reload_window();
        
        
        setLocationRelativeTo(null);        // center the window
        setVisible(true);
    }
    /**
     * update_list_of_notes()
     * Updating jlist_list_of_notes by storing all titles of notes
     */
    void update_list_of_notes(){
        show_debug("Updating list of notes");
        model.clear();
        for(Note note : engine.actual_notes){
            model.addElement(note.field_title);
        }
        show_debug("List of notes updated: "+engine.actual_notes.toString());
        jlist_list_of_notes.setModel(model);
    }
    /**
     * reload_window()
     * Reloads window to the startup state
     */
    void reload_window(){
        jLabel9.setText("");
        jLabel6.setText("");
        update_list_of_notes();
        button_addnewnote.setEnabled(true);
        button_savenote.setVisible(false);
        button_editnote.setVisible(false);
        textarea_note.setEditable(false);
        textfield_title.setEditable(false);
        textfield_name.setEditable(false);
        textfield_hashtags.setEditable(false);
        textfield_title.setText("");
        textfield_name.setText("");
        jLabel9.setText("");
        textfield_hashtags.setText("");
        textarea_note.setText("");
        actual_note = null;
        editable = false;
        new_note = false;
    }
    /**
     * GUI_main_window.load_note(Note to_load)
     * @param to_load 
     * Function loads note
     */
    void load_note(Note to_load){
        show_debug("Trying to load the note");
        jLabel9.setText(to_load.field_date);
        button_editnote.setVisible(true);
        textfield_title.setText(to_load.field_title);
        textfield_name.setText(to_load.field_name);
        String content = "";
        for (String line : to_load.list_of_content){
            content = content + line + "\n";
        }
        textarea_note.setText(content);
        textfield_hashtags.setText(to_load.ret_hashtags_to_GUI());
    }
    /**
     * GUI_main_window.show_debug(String text)
     * @param text 
     * Function for showing debug info
     */
    void show_debug(String text){
        if ( debug == 1){
            System.out.println("GUI DEBUG----> "+text);
        }
        log.add("GUI DEBUG----> "+text);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jlist_list_of_notes = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        textarea_note = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        button_editnote = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        textfield_title = new javax.swing.JTextField();
        textfield_name = new javax.swing.JTextField();
        button_savenote = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        button_addnewnote = new javax.swing.JButton();
        textfield_hashtags = new javax.swing.JTextField();
        button_reset = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ProgramNote");

        jlist_list_of_notes.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jlist_list_of_notes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlist_list_of_notesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jlist_list_of_notes);

        textarea_note.setColumns(20);
        textarea_note.setRows(5);
        jScrollPane2.setViewportView(textarea_note);

        jLabel1.setText("List of notes");

        jLabel2.setText("Note content:");

        button_editnote.setText("Edit note");
        button_editnote.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                button_editnoteMouseClicked(evt);
            }
        });

        jLabel4.setText("Title:");

        jLabel5.setText("Name:");

        button_savenote.setText("Save note");
        button_savenote.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                button_savenoteMouseClicked(evt);
            }
        });

        jLabel8.setText("Date:");

        jLabel9.setText("jLabel9");

        button_addnewnote.setText("Add new note");
        button_addnewnote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_addnewnoteActionPerformed(evt);
            }
        });

        textfield_hashtags.setText("jTextField1");

        button_reset.setText("Reset");
        button_reset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                button_resetMouseClicked(evt);
            }
        });

        jLabel3.setText("jLabel3");

        jLabel6.setText("jLabel6");
        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                            .addComponent(button_addnewnote, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(button_savenote, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(button_editnote, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
                            .addComponent(textfield_hashtags)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel5)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(43, 43, 43)
                                        .addComponent(jLabel4)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(textfield_title, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                                    .addComponent(textfield_name))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel6)
                                .addGap(97, 97, 97)
                                .addComponent(button_reset)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(textfield_title, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(textfield_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textfield_hashtags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_addnewnote)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(button_savenote)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(button_editnote)
                            .addComponent(button_reset)
                            .addComponent(jLabel6))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    /**
     * jlist_list_of_notesMouseClicked()
     * @param evt 
     * Function handle event of clicked list of notes
     */
    private void jlist_list_of_notesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlist_list_of_notesMouseClicked
        int index = jlist_list_of_notes.getSelectedIndex();
        button_editnote.setEnabled(true);
        show_debug("index of list: "+Integer.toString(index));
        actual_note = engine.get_note(index);
        load_note(actual_note);
    }//GEN-LAST:event_jlist_list_of_notesMouseClicked
    /**
     * button_resetMouseClicked(java.awt.event.MouseEvent evt)
     * @param evt 
     * 
     */
    private void button_resetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_resetMouseClicked
        show_debug("Reloading the window");
        reload_window();
    }//GEN-LAST:event_button_resetMouseClicked
    /**
     * GUI_main_window.check_whats_new()
     * @return ArrayList<String>
     * Function returns collection of edited fields.
     */
    ArrayList<String> check_whats_new(){
        ArrayList<String> field_name = new ArrayList<String>();
        
        if ( !textarea_note.getText().equals(actual_note.field_note_content)){
            field_name.add("textarea_note");
        }
        if ( !textfield_title.getText().equals(actual_note.field_title)){
            field_name.add("textfield_title");
        }
        if (!textfield_name.getText().equals(actual_note.field_name)){
            field_name.add("textfield_name");
        }
        if (!textfield_hashtags.getText().equals(actual_note.ret_hashtags_to_GUI())){
            field_name.add("textfield_hashtags");
        }
        show_debug("Updated fields by user: "+field_name.toString());
        return field_name;
    }
    /**
     * GUI_main_window().button_editnoteMouseClicked(java.awt.event.MouseEvent evt)
     * @param evt 
     * Function for setting program to edit state
     */
    private void button_editnoteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_editnoteMouseClicked
        button_savenote.setVisible(true);
        button_editnote.setVisible(true);
        textarea_note.setEditable(true);
        textfield_title.setEditable(true);
        textfield_name.setEditable(true);
        textfield_hashtags.setEditable(true);
        editable = true;
    }//GEN-LAST:event_button_editnoteMouseClicked
    void show_fields(){
        if (debug == 1){
            System.out.println("Showing content of the fields:");
            System.out.println("textarea_note:");
            System.out.println(textarea_note.getText());
            System.out.println("textfield_title:");
            System.out.println(textfield_title.getText());
            System.out.println("textfield_hashtags:");
            System.out.println(textfield_hashtags.getText());
            System.out.println("textfield_name:");
            System.out.println(textfield_name.getText());
            System.out.println("End of content of the fields.");
        }
        
    }
    private void button_savenoteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_savenoteMouseClicked
        if ( editable ){
            actual_note.from_gui = true;
            show_debug("Before update:");
            actual_note.show_note();
            show_fields();
            ArrayList<String> to_do = check_whats_new();
            try {
                actual_note.update_date();
            } catch (ParseException ex) {
                show_debug(ex.toString());
            }
            if (to_do.contains("textarea_note")){
                actual_note.update_content(textarea_note.getText());  
            }
            else if (to_do.contains("textfield_title")){
                actual_note.update_title(textfield_title.getText());
            }
            else if (to_do.contains("textfield_name")){
                actual_note.update_name(textfield_name.getText());
            }
            else if (to_do.contains("textfield_hashtags")){
                String[] new_hashtags = textfield_hashtags.getText().split(",");
                actual_note.clear_hashtag();
                for (String hashtag : new_hashtags){
                    actual_note.add_hashtag(hashtag);
                }
            }
            reload_window();
        }
        else if ( new_note ){
            ArrayList<String> hsh = new ArrayList<>();
            hsh.addAll(Arrays.asList(textfield_hashtags.getText().split(",")));

            if ( checkif_all_filled() ){
                try {
                    engine.add_note_to_collection(new Note(new Date().toString(),actual_configuration.field_checksum,textfield_name.getText()
                            ,textfield_title.getText(),hsh,textarea_note.getText()));
                } catch (IOException ex) {
                    show_debug(ex.toString());
                } catch (ParseException ex) {
                    show_debug(ex.toString());
                } catch (SQLException ex) {
                    Logger.getLogger(GUI_main_window.class.getName()).log(Level.SEVERE, null, ex);
                }
                reload_window();
            }
            else{
                jLabel6.setText("Not all fields filled");
            }
        }
    }//GEN-LAST:event_button_savenoteMouseClicked
    /**
     * GUI_main_window.checkif_all_filled()
     * @return boolean
     * Simple function for checking if all fields are filled
     */
    boolean checkif_all_filled(){
        if ( textfield_hashtags.getText().isEmpty()){
            return false;
        }
        else if (textfield_name.getText().isEmpty()){
            return false;
        }
        else if (textfield_title.getText().isEmpty()){
            return false;
        }
        else if (textarea_note.getText().isEmpty()){
            return false;
        }
        else{
            return true;
        }
        
    }
    private void button_addnewnoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_addnewnoteActionPerformed
        reload_window();
        button_addnewnote.setEnabled(false);
        button_savenote.setVisible(true);
        button_savenote.setEnabled(true);
        button_editnote.setEnabled(false);
        textfield_hashtags.setEditable(true);
        textfield_name.setEditable(true);
        textfield_title.setEditable(true);
        textarea_note.setEditable(true);
        new_note = true;
    }//GEN-LAST:event_button_addnewnoteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_addnewnote;
    private javax.swing.JButton button_editnote;
    private javax.swing.JButton button_reset;
    private javax.swing.JButton button_savenote;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<String> jlist_list_of_notes;
    private javax.swing.JTextArea textarea_note;
    private javax.swing.JTextField textfield_hashtags;
    private javax.swing.JTextField textfield_name;
    private javax.swing.JTextField textfield_title;
    // End of variables declaration//GEN-END:variables
}
