/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frameworks;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author günter
 */
public class SshClient {
    Channel channel;
    InputStream fromServer=null;
    Session session=null;
    ChannelSftp sftpchannel;
    ChannelExec execchannel;
    
    
    public void connect(String username, String password, String host, int port)
			throws JSchException, IOException {
		JSch shell = new JSch();
		session = shell.getSession(username, host, port);
		session.setPassword(password);
                session.setConfig("StrictHostKeyChecking", "no");
                session.setConfig(
    "PreferredAuthentications", 
    "publickey,keyboard-interactive,password");
		session.connect();
                
		
	}
  

    public int sendfile(String source,String target) throws SftpException, JSchException
    { 
      OutputStream outstream=null; 
        
            channel = session.openChannel("sftp");
            channel.connect();
           
            //sftpchannel = (ChannelSftp) channel;
            ((ChannelSftp)channel).put(source,target,null,sftpchannel.OVERWRITE);   
            //sftpchannel.put(source,target,sftpchannel.OVERWRITE);
           channel.disconnect();
            return channel.getExitStatus();
        
    }
    
     public int getfile(String source,String target) throws SftpException, JSchException
    { 
      channel = session.openChannel("sftp"); 
      channel.connect();
     
      //sftpchannel = (ChannelSftp) channel;
      ((ChannelSftp)channel).get(source, target, null,sftpchannel.OVERWRITE );
      channel.disconnect();
      return channel.getExitStatus();   
    }
   
    public int sendcommand(String command, ByteArrayOutputStream error,StringBuilder sbfromServer) throws JSchException, IOException, InterruptedException
    {   System.out.println(command);
         error.reset();
         sbfromServer.setLength(0);
         channel=session.openChannel("exec");
         ((ChannelExec)channel).setErrStream(error);
         ((ChannelExec)channel).setCommand(command);
         fromServer=channel.getInputStream();
         channel.connect();
         byte[] tmp=new byte[1024];
         while(true)
         {
            while(fromServer.available()>0)
            {
                int i=fromServer.read(tmp, 0, 1024);
                if(i<0) 
                {
                    break;
                }
                sbfromServer.append(new String(tmp, 0, i));
            }    
          
           //System.out.print(new String(tmp, 0, i));
                if (channel.isClosed())
                {
                    channel.disconnect();
                   //exitstatus=channel.getExitStatus();
                  return channel.getExitStatus();
                  // break;
                }
               //Thread.sleep(10);
            
            
            
         }   
        
    }
    
    
    
    public boolean isConnected() {
		// TODO Auto-generated method stub
		return (channel != null && channel.isConnected());
	}
    
    
    
    public void channeldisconnect() 
    {
	if (isConnected()) 
        {
            channel.disconnect();
        }
    }
    
    public void sessiondisconnect() 
    {
        session.disconnect();
    }



}
