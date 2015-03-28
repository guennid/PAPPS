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

/**
 *
 * @author günter
 */
public class Aufzählungen {
    
    public void Install(String LinuxUser, String LinuxPassword,String Host,File file) throws JSchException, IOException, SftpException
    {int sshexitstatus=0;
        //Aus Aufzählungsdatei einzelne Dateien machen
        //Einzelne Dateien auf Server übertragen
        SshClient sshclient=new SshClient();
        sshclient.connect(LinuxUser, LinuxPassword,Host, 22);
        sshexitstatus=sshclient.sendfile(file.toString(), file.getName());        
        //Dateien importieren
        
    }
}
