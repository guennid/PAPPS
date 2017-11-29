/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frameworks;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import de.abas.ceks.jedp.EDPSession;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.JTextArea;

/**
 *
 * @author gdenz
 */
public class FOPS {

    public void foptxtTranscode(JTable jTFOPTXT, int zdalt, int zdneu) {
       
        String alteMaske = "";
        String neueMaske = "";

        //SChleife über das Maskenarray
        System.out.println("FOPTXT Transcode");
        System.out.println("FOPTXT Transfer von Zusatzdatei "+zdalt+" nach "+zdneu);
        
        for (int gruppen = 0; gruppen < 20; gruppen++) {
            if (GlobalVars.MaskArray[zdalt][gruppen] != null) {
                alteMaske = GlobalVars.MaskArray[zdalt][gruppen].trim();
                neueMaske = GlobalVars.MaskArray[zdneu][gruppen].trim();
                System.out.println("Transferiere FOP.TXT von der Maske "+ alteMaske +" nach "+neueMaske);
                
                // Und nun einfach mit diesen Infos durch die jTable laufen 
                for (int i = 0; i < jTFOPTXT.getRowCount(); i++) {
                    if (jTFOPTXT.getValueAt(i, 0).toString().equals(alteMaske)) {
                        // Und die Trefferersetzen
                        jTFOPTXT.setValueAt(neueMaske, i, 0);
                        System.out.println(neueMaske+ "gesetzt in Zeile "+i);
                    }
                }
            }
        } 
        System.out.println("FOPTXT Transcode Ende");
    }

    public boolean foptxtinstall(String fwnummer, String LinuxUser, String LinuxPass, String Host, File dir, JTable table) {
        String zeile = "";

        try {
            ByteArrayOutputStream error = new ByteArrayOutputStream();
            StringBuilder fromServer = new StringBuilder();
            
            /*BufferedWriter bw =new BufferedWriter( new FileWriter ("tmp\\fop.txt"));
            bw.write("Anfang FW "+fwnummer);
            bw.newLine();
            for (int i=0;i<table.getRowCount();i++)
            {    
            bw.write(table.getValueAt(i, 0)+ " "+table.getValueAt(i, 1)+" "+table.getValueAt(i, 2)+" "+table.getValueAt(i, 3)+" "+table.getValueAt(i, 4)+" "+table.getValueAt(i, 5)+" "+table.getValueAt(i, 6)+" "+table.getValueAt(i, 7));
            bw.newLine();
            }
            bw.write("ENDE FW "+fwnummer); 
            bw.close();
           */
            FileWriter f = new FileWriter ("tmp\\fop.txt");
            f.write("#            Anfang FW "+fwnummer+"\n");
            
            for (int i=0;i<table.getRowCount();i++)
            {    
            f.write(table.getValueAt(i, 0)+ " "+table.getValueAt(i, 1)+" "+table.getValueAt(i, 2)+" "+table.getValueAt(i, 3)+" "+table.getValueAt(i, 4)+" "+table.getValueAt(i, 5)+" "+table.getValueAt(i, 6)+" "+table.getValueAt(i, 7)+"\n");
           
            }
            f.write("#             ENDE FW "+fwnummer+"\n"); 
            f.close();
            //fop.txt zum Kundenserver senden
            SshClient sshclient = new SshClient();
            sshclient.connect(LinuxUser, LinuxPass, Host, 22);
            int sshexitstatus = sshclient.sendfile("tmp\\fop.txt", "fopfw.txt");

            String s = new String(error.toByteArray());
            System.out.println(s + " " + fromServer);
            // fop.txt auf Server sichern
            sshexitstatus = sshclient.sendcommand("cp fop.txt fop.txt.SIK.FW" + fwnummer, error, fromServer);
            s = new String(error.toByteArray());
            System.out.println(s + " " + fromServer);
             sshexitstatus = sshclient.sendcommand("eval `sh denv.sh`;s3_conv 51 < fopfw.txt >fopfw2.txt", error, fromServer);
            s = new String(error.toByteArray());
            System.out.println(s + " " + fromServer);
            // fop.txt ran catten
            sshexitstatus = sshclient.sendcommand("cat  fopfw2.txt  >>fop.txt", error, fromServer);
             s = new String(error.toByteArray());
            System.out.println(s + " " + fromServer);
           
            //fop.txt.fw löschen
          //  sshexitstatus = sshclient.sendcommand("rm fopfw.txt", error, fromServer);
         //   s = new String(error.toByteArray());
            System.out.println(s + " " + fromServer);
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

    public boolean install(String LinuxUser, String LinuxPass, String Host, File file, String dir,EDPSession session,  JTextArea jTLog, boolean isfile) {
        try {
            boolean status;
            // Arbeitsbereich erweitern
            if (!isfile && !dir.equals(""))
            {
            jTLog.append("Arbeitsbereich "+ dir+" erweitern\n");
             status = Arbeitsbereiche.arbeitsbereicherweitern(dir,session);
            }
            //FOps und SPX
            StringBuilder fromServer = new StringBuilder();
            ByteArrayOutputStream error = new ByteArrayOutputStream();
            int sshexitstatus;

            SshClient sshclient = new SshClient();
            sshclient.connect(LinuxUser, LinuxPass, Host, 22);
            if (isfile)
            {
                // File irgendwo
                if (!dir.contains("$HOMEDIR"))
                {
                if (dir.equals(""))
                     {// Kein verzeichnis zu erstellen
                    
                    sshexitstatus = sshclient.sendfile(file.toString(), file.getName());
                      }
                else
                    {// nur wenn auch ein Verzeichnis vorne dran liegt
                    sshexitstatus = sshclient.sendcommand("mkdir -p " + dir, error, fromServer);
                    sshexitstatus = sshclient.sendfile(file.toString(), dir + "/" + file.getName());
                      }
                
                }
                else
                {
                sshexitstatus= sshclient.sendcommand("eval `sh denv.sh`;echo $HOMEDIR", error, fromServer);
                String homedir=fromServer.toString().replaceAll("\n", "");
                dir=dir.replace("$HOMEDIR", homedir);
                sshexitstatus = sshclient.sendfile(file.toString(),dir + "/" + file.getName());
                }
            }
            else
            {
                //fop oder spx
            if (file.getName().endsWith(".spx")) {
                sshexitstatus = sshclient.sendcommand("mkdir spx/" + dir, error, fromServer);
                sshexitstatus = sshclient.sendfile(file.toString(), "spx/" + dir + "/" + file.getName());
            } else {
                if (!dir.equals(""))
                {                
                    sshexitstatus = sshclient.sendcommand("mkdir " + dir, error, fromServer);
                    sshexitstatus = sshclient.sendfile(file.toString(), dir + "/" + file.getName());
                }
                else
                {
                    sshexitstatus = sshclient.sendfile(file.toString(), file.getName());
                }
                
                
            }    
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
