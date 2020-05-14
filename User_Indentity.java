/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */

package programnote;
/**
 *User_Indentity object.
 * @author jakub
 * Stores indentity of logged user.
 */
public class User_Indentity {
    
    private String name;
    private String surname;
    private int id;
    private String login;
    
    /**
     * User_Indentity constructor with 4 parameters
     * @param name
     * @param surname
     * @param id
     * @param login 
     */
    User_Indentity(String name,String surname,int id,String login){
        this.name = name;
        this.surname = surname;
        this.id = id;
        this.login = login;
    }
    /**
     * User_Indentity constructor without parameters
     */
    User_Indentity(){
        name = null;
        surname = null;
        id = -1;
        login = null;
    }
    int get_id(){
        return id;
    }
    String prepare_checksum(){
        return Integer.toString(id)+"-"+login+"-"+name;
    }
    /**
     * User_Indentity.check()
     * @return boolean
     * Checks the object.
     */
    boolean check(){
        if( name == null || surname == null || login == null || id == -1){
            return false;
        }
        return true;
    }
    /**
     * User_Intentity.show_user()
     * Show info stores in the object
     */
    void show_user(){
        if ( check() ){
        System.out.println("Logged as: "+login);
        System.out.println("Name: "+ name+" Surname: "+surname);
        System.out.println("id: "+Integer.toString(id));
        }
        else{
            System.out.println("Object User Indentity corrupted");
        }
    }
}