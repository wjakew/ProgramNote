/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */

package programnote;
import java.io.IOException;
import static java.lang.ProcessBuilder.Redirect.to;
import java.util.*;  
import javax.mail.*;  
import javax.mail.internet.*;  
import javax.activation.*; 
/**
 *MailSender object
 * @author jakub
 * Object for sending e-mails with notes
 */
public class MailSender {
    
    Configuration c = new Configuration();
        
    int debug = c.get_fast_debug_info();
    
    final String version = "v1.0.0";
    final String HOST = "smtp.gmail.com";
    final String USER="main.tes.instruments@gmail.com";
    private String password="";
    
    Properties props;
    
    ArrayList<String> log;
    
    MimeMessage message;
    Handler mail_creds;
    Note to_send;
    String email_address;
    
    
    MailSender(Handler creds,Note to_send,String email) throws MessagingException, IOException{

        System.out.println("MailSender by ProgramNote "+version);
        mail_creds = creds;
        this.to_send = to_send;
        email_address = email;
        log = new ArrayList<>();
        System.out.println("Got email adress: "+email_address);
    }
    
    void run(){
        prepare();
        Session actual_session = ret_session();
        
        try{
            
            compose(actual_session);
            
            System.out.println("Title of the message: "+"programnote_email_"+to_send.field_name);
            System.out.println("Content of the message:");
            System.out.println(prepare_content());
            
            Transport.send(message); 
            
            System.out.println("Note sent to : "+email_address);
            
        }catch(MessagingException e){
            show_debug(e.toString());
        } 
    }

    void prepare(){
        password = mail_creds.get_h2();
        props = new Properties();  
        props.setProperty("mail.transport.protocol", "smtp");     
        props.setProperty("mail.host", "smtp.gmail.com");  
        props.put("mail.smtp.auth", "true");  
        props.put("mail.smtp.port", "465");  
        props.put("mail.debug", "true");  
        props.put("mail.smtp.socketFactory.port", "465");  
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");  
        props.put("mail.smtp.socketFactory.fallback", "false"); 
    }
    
    Session ret_session(){
        return Session.getDefaultInstance(props,  
            new javax.mail.Authenticator() {  
              protected PasswordAuthentication getPasswordAuthentication() {  
            return new PasswordAuthentication(USER,password);  
              }}); 
    }
    String prepare_content(){
        String to_ret="";
        
        for (String line : to_send.records){
            to_ret = to_ret + line +"\n";
        }
        return to_ret;
    }
    
    void compose(Session actual) throws AddressException, MessagingException{
        message = new MimeMessage(actual); 
        message.setFrom(new InternetAddress(USER));  
        message.addRecipient(Message.RecipientType.TO,new InternetAddress(email_address));  
        message.setSubject("programnote_email_"+to_send.field_name);  
        message.setText(prepare_content());
    }
    
    void show_debug(String text){
        if ( debug == 1){
            System.out.println("MAILSENDER DEBUG -----> "+text);
        }
        log.add("MAILSENDER DEBUG -----> "+text);
    }
    
}
