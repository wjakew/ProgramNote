/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package programnote;

/**
 *Handler Object
 * @author jakub
 * For storing save data.
 */
public class Handler {
    
    private String h1;
    private String h2;
    
    Handler(){
        h1 = "";
        h2 = "";
    }
    
    void put(String text,int mode){
        if (mode == 1){
            h1 = text;
        }
        else if ( mode == 2 ){
            h2 = text;
        } 
    }
    String get_h1(){
        return h1;
    }
    String get_h2(){
        return h2;
    }
    
    void show(){
        System.out.println("h1:"+h1+" h2:"+h2);
    }
}
