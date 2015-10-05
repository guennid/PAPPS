/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package papps;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gdenz
 */
public class FOPS {
 public boolean install(String LinuxUser,String LinuxPass, String Host,File file, String dir)
 {      try {
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
