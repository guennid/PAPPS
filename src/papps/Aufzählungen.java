/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package papps;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author günter
 */
public class Aufzählungen {
    
    public void Install(String LinuxUser, String LinuxPassword,String Host,File file) throws JSchException, IOException, SftpException
    {int sshexitstatus=0;
    File subfile=null;
    int i =0;
        //Nach 1. Subdatei der Aufzählung schauen
        for (i=0;i<5;i++)
        {
            Integer si=new Integer(i);        
            subfile=new File(file.getName()+si.toString());
            if (subfile.exists() && !subfile.isDirectory())
            {
                // Subfile auf Server kopieren
                SshClient sshclient=new SshClient();
                sshclient.connect(LinuxUser, LinuxPassword,Host, 22);
                sshexitstatus=sshclient.sendfile(subfile.toString(), subfile.getName());  
                // Subfile importieren
                sshexitstatus=sshclient.sendcommand("eval `sh denv.sh`;sh edpimport.sh -p "+new String(jPMandant.getPassword())+" -a IMPORT -s "+name+" -w "+arb, error, fromServer);
            }
        
        }
    }
}
