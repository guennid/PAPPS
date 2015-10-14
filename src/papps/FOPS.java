/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package papps;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;

/**
 *
 * @author gdenz
 */
public class FOPS {
    public boolean foptxtinstall(String fwnummer,String LinuxUser,String LinuxPass, String Host,File dir, JTable table)
    {String zeile="";
     
        try {
            ByteArrayOutputStream error= new ByteArrayOutputStream();
            StringBuilder fromServer=new StringBuilder();
            //fop.txt einlesen
            FileReader fr = null;
            fr = new FileReader(dir.toString()+"\\FOP\\"+GlobalVars.foptxtfile);
            BufferedReader br = new BufferedReader(fr);
            while( (zeile = br.readLine()) != null )
            {    
            if (!zeile.startsWith("#"))
            {
            // und ZD übersetzen
            String [] zeilelist=zeile.split("\\s+",2);  
            
            //SChleife über die ZD
            for (int i=0;i<GlobalVars.MaskArray.length;i++)
            {
                for (int y=0;y<=20;y++)
                {// Und Schleife über die Gruppen
                    System.out.println(GlobalVars.MaskArray[i][y]);
                    if (zeilelist[0].equals(GlobalVars.MaskArray[i][y]))  
                    {
                    // wir müssen die Übersetzung der ZD anstossen
                    Vartab vartab=new Vartab();
                    // ZD rein und ZD raus
                    int zdvartab=vartab.Vartabtranscode(i,table,false);
                  System.out.println("Treffer");
                    }
                }
                
              
            }   
            
            // und als Datei im tmp schreiben
            }
            }
            //fop.txt zum Kundenserver senden
            SshClient sshclient=new SshClient();
            sshclient.connect(LinuxUser, LinuxPass,Host, 22);
            int sshexitstatus=sshclient.sendfile(dir.toString()+"\\FOP\\"+GlobalVars.foptxtfile, "fopfw.txt");
            
            String s = new String(error.toByteArray());
           System.out.println(s+" "+fromServer); 
           // fop.txt auf Server sichern
            sshexitstatus=sshclient.sendcommand("cp fop.txt fop.txt.SIK.FW"+fwnummer, error, fromServer);
            s = new String(error.toByteArray());
           System.out.println(s+" "+fromServer); 
            // fop.txt ran catten
            sshexitstatus=sshclient.sendcommand("cat  fopfw.txt  >>fop.txt", error, fromServer);
            s = new String(error.toByteArray());
            System.out.println(s+" "+fromServer);             
            //fop.txt.fw löschen
            sshexitstatus=sshclient.sendcommand("rm fopfw.txt", error, fromServer);
            s = new String(error.toByteArray());
           System.out.println(s+" "+fromServer); 
            return true;
        } catch (JSchException ex) {
            Logger.getLogger(FOPS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FOPS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SftpException ex) {
            Logger.getLogger(FOPS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(FOPS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
 public boolean install(String LinuxUser,String LinuxPass, String Host,File file, String dir)
 {      try {
     
     
     //FOps und SPX
      StringBuilder fromServer=new StringBuilder();
        ByteArrayOutputStream error= new ByteArrayOutputStream();
        int sshexitstatus;
    
     SshClient sshclient=new SshClient();
     sshclient.connect(LinuxUser, LinuxPass,Host, 22);
     
     
     
    if (file.getName().endsWith(".spx"))
        {
            sshexitstatus=sshclient.sendcommand("mkdir spx/"+dir, error, fromServer);
        sshexitstatus=sshclient.sendfile(file.toString(), "spx/"+dir+"/"+file.getName());
        }   
        else
        {
            sshexitstatus=sshclient.sendcommand("mkdir "+dir, error, fromServer);
        sshexitstatus=sshclient.sendfile(file.toString(), dir+"/"+file.getName());
        }
     
     return true;
     } catch (JSchException ex) {
         Logger.getLogger(FOPS.class.getName()).log(Level.SEVERE, null, ex);
     } catch (IOException ex) {
         Logger.getLogger(FOPS.class.getName()).log(Level.SEVERE, null, ex);
     } catch (SftpException ex) {
         Logger.getLogger(FOPS.class.getName()).log(Level.SEVERE, null, ex);
     } catch (InterruptedException ex) {
         Logger.getLogger(FOPS.class.getName()).log(Level.SEVERE, null, ex);
     }
 return true;
 }
}
