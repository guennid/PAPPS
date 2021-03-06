/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frameworks;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import de.abas.ceks.jedp.CantBeginSessionException;
import de.abas.ceks.jedp.EDPFactory;
import de.abas.ceks.jedp.EDPSession;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

/**
 *
 * @author gdenz
 */
public class Infosysteme {
    public boolean install(JTable jTinfosystem, JTextArea jTLog, EDPSession session)
    {
        Thread sync = new Thread();
        boolean status;        
        String arb="";
        String name="";
        File file;
        StringBuilder fromServer=new StringBuilder();
        ByteArrayOutputStream error= new ByteArrayOutputStream();
        int sshexitstatus;
        //Infosysteme installieren
        for(int i=0;i<jTinfosystem.getRowCount();i++)
        {
            try {
                arb=jTinfosystem.getValueAt(i, 0).toString();
                // Arbeitsbereich erweitern
                jTLog.append("Arbeitsbereich "+ arb+" erweitern\n");
                status = Arbeitsbereiche.arbeitsbereicherweitern(arb,session);
                name=jTinfosystem.getValueAt(i, 1).toString();
                file= new File (jTinfosystem.getValueAt(i, 2).toString());
                System.out.println(name);
                // Verbindung aufnehmen
                SshClient sshclient=new SshClient();
                //Infosystem kopieren
                jTLog.append("Infosystem "+file.getName()+" kopieren");
                jTLog.update(jTLog.getGraphics());
                jTLog.setCaretPosition(jTLog.getText().length());
                sshclient.connect(GlobalVars.LinuxUser, GlobalVars.LinuxPass,GlobalVars.Host, 22);
                sshexitstatus=sshclient.sendfile(file.toString(), file.getName());
                jTLog.append("                                         OK\n");
                jTLog.update(jTLog.getGraphics());
                //Infosystem auspacken
                jTLog.append("Infosystem "+file.getName()+" auspacken\n");
                jTLog.append("=======================================\n");
                jTLog.update(jTLog.getGraphics());
                sshexitstatus=sshclient.sendcommand("tar xzvf "+file.getName(), error, fromServer);
                if (sshexitstatus==0)
                {
                    jTLog.append(fromServer.toString());
                    jTLog.append("=======================================\n");
                    jTLog.update(jTLog.getGraphics());
                    //Infosystem installieren
                    jTLog.append("Infosystem "+file.getName()+" installieren\n");
                    jTLog.update(jTLog.getGraphics());
                    jTLog.setCaretPosition(jTLog.getText().length());
                    sshexitstatus=sshclient.sendcommand("eval `sh denv.sh`;sh loadinfosys.sh -p "+GlobalVars.Mandantpass+" -a IMPORT -s "+name+" -w "+arb, error, fromServer);
                    // sshexitstatus=sshclient.sendcommand("ls", error, fromServer);
                    if (sshexitstatus==0)
                    {
                        jTLog.append(fromServer.toString());
                        jTLog.append(error.toString());
                        jTLog.append("OK");
                        jTLog.setCaretPosition(jTLog.getText().length());
                    }
                    else
                    {
                        jTLog.append(error.toString());
                      jTLog.setCaretPosition(jTLog.getText().length());
                    }
                }
                else
                {
                    jTLog.append(error.toString());
                    jTLog.setCaretPosition(jTLog.getText().length());
                }

                // Session beenden
                sshclient.sessiondisconnect();
            } catch (InterruptedException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            } catch (SftpException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            } catch (JSchException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return true;
    }
    
    

}
