/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package papps;

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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.JTextArea;

/**
 *
 * @author gdenz
 */
public class Masken {

    public void maskentranscode(JTable table, String altezd, String neuezd) {
        int neuezdint = 0;

        for (int i = 0; i < table.getRowCount(); i++) {
            // Tabelle durchlaufen
            // Und die Maske erst mal in ZD umrechnen
            // Schleife über das Maskenarray der DB
            for (int dbnummer = 1; dbnummer < 41; dbnummer++) {
                //Schleife üebr die Gruppen
                for (int grnummer = 0; grnummer < 21; grnummer++) {
                    // Wenn nun die Maskennummer zur aktuellen Tabellenzeile passt
                    if (GlobalVars.MaskArray[dbnummer][grnummer].equals(table.getValueAt(i, 0))) {
                   // Dann wissen wir die Datei und die GR
                        //die muss aber noch übersetzt werden in Vartab anstatt DB
                        Vartab vartab = new Vartab();
                        dbnummer = vartab.ZD2Vartab(dbnummer);
                        //Falls diese mit der übergeben ZD die geändert wurde übereinstimmt
                        if (altezd.equals(Integer.toString(dbnummer))) {
                            // dann müssen wir diese transferieren
                            int neuezdi = Integer.parseInt(neuezd);
                            neuezdi = vartab.Vartab2ZD(neuezdi);
                            table.setValueAt(GlobalVars.MaskArray[neuezdi][grnummer], i, 1);
                        }
                    }
                }
                /*  
                 dbgr = table.getValueAt(i, 3).toString();
                 db = dbgr.substring(0, dbgr.indexOf(":"));
                 gr = dbgr.substring(dbgr.indexOf(":") + 1, dbgr.length());
                 if (db.equals(altezd)) {
                 table.setValueAt(neuezd + ":" + gr, i, 3);*/
            }
        }
    }

    public boolean maskenInstall(JTable table, JTextArea jTLog, EDPSession session, String LinuxUser, String LinuxPass, String Host) {
        try {
            ByteArrayOutputStream error = new ByteArrayOutputStream();
            StringBuilder fromServer = new StringBuilder();
            StringBuilder screenls = new StringBuilder();
            String maskennummer;
            String fromServerString = "";
            String[] fromServerArray;
            String resourcesServerString = "";
            String[] resourcesServerArray;
            ArrayList datei = new ArrayList();
            String prio = "";
            String xmlziel = "";
            String resourcesziel = "";
            int masksearch = 0;
            int sshexitstatus = 0;
            File file = null;
            int zielzeile = 0;
            //SSh Verbindung zum Mandanten öffnen
            SshClient sshclient = new SshClient();
            sshclient.connect(LinuxUser, LinuxPass, Host, 22);
            
            //ls im screen Verzeichnis
            
            sshexitstatus = sshclient.sendcommand("ls screens -l", error, screenls);
            jTLog.append("Vorhandene Masken aus Mandanten eingelesen\n");
            //Dateinamen erzeugen
            for (int i = 0; i < table.getRowCount(); i++) {
                xmlziel = table.getValueAt(i, 2).toString().replace(table.getValueAt(i, 0).toString(), table.getValueAt(i, 1).toString());
                resourcesziel = table.getValueAt(i, 3).toString().replace(table.getValueAt(i, 0).toString(), table.getValueAt(i, 1).toString());
                maskennummer = table.getValueAt(i, 1).toString();
                // schauen ob das Maskenverzeichnis vorhanden ist im ls
                masksearch = screenls.indexOf("screen_" + maskennummer + "\n");
                // hab ich eine ZD und ein Masken TGZ?      
                if (table.getValueAt(i, 3).equals("TGZ")) {
                    prio = xmlziel.substring(xmlziel.indexOf(".") + 1, xmlziel.lastIndexOf("."));
                    prio = prio.substring(prio.indexOf(".") + 1, prio.length());
                    // Dann kann ich die Maske einfach auf den Serverv kopieren
                    file = new File(GlobalVars.dir + "\\Masken\\" + table.getValueAt(i, 2).toString());
                    sshexitstatus = sshclient.sendfile(file.toString(), xmlziel);
                    // und importieren
                    sshexitstatus = sshclient.sendcommand("eval `sh denv.sh`;screen_import.sh -n " + maskennummer + " -p " + prio + " " + xmlziel, error, fromServer);
                    // und generieren
                    sshexitstatus = sshclient.sendcommand("eval `sh denv.sh`;edpinfosys.sh -p " + GlobalVars.Mandantpass + " -n MASKE -s selmasknr,bstart,1:tmaskgen -w'" + maskennummer + ";1;1'", error, fromServer);
                   jTLog.append(error.toString());
                } else {

                    if (masksearch <= 0) {
                        sshexitstatus = sshclient.sendcommand("eval `sh denv.sh`;edpinfosys.sh -p " + GlobalVars.Mandantpass + " -n MASKE -s selmasknr,bstart,1:tindivid,2:tindivid,3:tindivid -w'" + maskennummer + ";1;1;1;1'", error, fromServer);

                        System.out.println(error);
                    }
                    //Maske ist vorhanden
                    prio = xmlziel.substring(xmlziel.indexOf(".") + 1, xmlziel.lastIndexOf("."));
                    prio = prio.substring(prio.indexOf(".") + 1, prio.indexOf("-"));

                    // Nun noch schauen welche Prioritäten da sind
                    sshexitstatus = sshclient.sendcommand("ls screens/screen_" + maskennummer + " -l", error, fromServer);

                    fromServerString = fromServer.toString();
                    fromServerArray = fromServerString.split("\n");

                    // Wir müssen durchs Array durchlaufen um die Prios raus zu finden
                    for (int iServer = 0; iServer < fromServerArray.length; iServer++) {
                        //die Prio aus dem fromServerArray raus fischen
                        String prioServermaske = fromServerArray[iServer].substring(fromServerArray[iServer].length() - 1, fromServerArray[iServer].length());
                        if (prioServermaske.equals(prio)) {
                           jTLog.append("Maske "+maskennummer+" "+prio+" wird erweitert\n");
                            // Maske abholen
                            String servermaske = "./screens/screen_" + maskennummer + "/" + prio + "/screen_" + maskennummer + "_" + prio + "_M.xml";
                            sshexitstatus = sshclient.getfile(servermaske, "tmp\\" + maskennummer + prio + ".xml");
                            //Maske einlesen
                            BufferedReader inservermaske = new BufferedReader(new FileReader("tmp\\" + maskennummer + prio + ".xml"));

                            BufferedReader inmaske = new BufferedReader(new FileReader(GlobalVars.dir + "\\Masken\\" + table.getValueAt(i, 2).toString()));
                            PrintWriter outservermaske = new PrintWriter(new BufferedWriter(new FileWriter("tmp\\" + maskennummer + prio + "X.xml")));
                            int lineNr = 0;

                            String line = inservermaske.readLine();

                            while (line != null) {

                                datei.add(line);

                                if (line.contains("</pane.notebook>")) {
                                    zielzeile = lineNr;
                                }
                                lineNr++;

                                line = inservermaske.readLine();
                            }
                            inservermaske.close();

                            // Raus schreiben der Datei
                            for (int izeilen = 0; izeilen < lineNr - 1; izeilen++) {
                                outservermaske.print(datei.get(izeilen) + "\n");
                                if (izeilen == zielzeile) {
                                    line = inmaske.readLine();
                                    while (line != null) {
                                        // Zuerst die Datei einlesen und jede Zeile raus schreiben
                                        outservermaske.print(line + "\n");
                                        line = inmaske.readLine();

                                    }
                                }
                            }
                            outservermaske.close();

                            //Datei zurück zum server schicken
                            sshexitstatus = sshclient.sendfile("tmp\\" + maskennummer + prio + "X.xml", servermaske);
                            
                         // Jetzt noch die Resources Datei an die Resources auf dem server anhängen

                            sshexitstatus = sshclient.sendfile(GlobalVars.dir + "\\Masken\\" + table.getValueAt(i, 3).toString(), "./screens/screen_" + maskennummer + "/" + prio + "/ResourcesFW");
                            sshexitstatus = sshclient.sendcommand("cat  ./screens/screen_" + maskennummer + "/" + prio + "/ResourcesFW  >> ./screens/screen_" + maskennummer + "/" + prio + "/Resources.language", error, fromServer);
                        //  

                            sshexitstatus = sshclient.sendcommand("ls screens/screen_" + maskennummer + "/" + prio + "/Resources_* -l", error, fromServer);
                            resourcesServerString = fromServer.toString();
                            resourcesServerArray = resourcesServerString.split("\n");
                            for (int iresources = 0; iresources < resourcesServerArray.length; iresources++) {
                                if (resourcesServerArray[iresources].toString().contains("Resources")) {
                                    sshexitstatus = sshclient.sendcommand("cat  ./screens/screen_" + maskennummer + "/" + prio + "/ResourcesFW  >>" + resourcesServerArray[iresources].toString().substring(resourcesServerArray[iresources].lastIndexOf(" "), resourcesServerArray[iresources].length()), error, fromServer);
                                }

                            }
                            // Maske nun noch generieren
                            sshexitstatus = sshclient.sendcommand("eval `sh denv.sh`;edpinfosys.sh -p " + GlobalVars.Mandantpass + " -n MASKE -s selmasknr,bstart,1:tmaskgen,2:tmaskgen,3:tmaskgen,4:tmaskgen -w'" + maskennummer + ";1;1;1;1;1'", error, fromServer);
                            //System.out.println(error);
                            jTLog.append(error.toString());
                        }
                        //System.out.println();

                        // Danach einfügen des neuen Registers
                        // und zurücksenden der Datei auf den Server
                    }

                    // Maske nicht da, neu individualisieren
                    //Maske abholen
                    /* sshexitstatus = sshclient.sendcommand("mkdir screens/screen_"+maskennummer , error, fromServer);
                    
                   
                     // XML kopieren
                     file=new File(GlobalVars.dir+"\\Masken\\"+table.getValueAt(i,2).toString());
                     sshexitstatus = sshclient.sendfile(file.toString(), "./screens/screen_"+maskennummer+"/"+xmlziel);
                     //Resources kopieren
                     file=new File(GlobalVars.dir+"//Masken//"+table.getValueAt(i,3).toString());
                     sshexitstatus = sshclient.sendfile(file.toString(), "./screens/screen_"+maskennummer+"/"+resourcesziel);
                     */
        // In fromServer steht nun der ls, den wir nun anschauen müssen ob der screen schon da ist
        // Maske kopieren
                    // Resources kopieren
                    // Prüfung auf bestehende individuelle Maske
                    // Maske erzeugen 
                    // Oder Maske anhängen
                }
            }

        } catch (JSchException ex) {
            Logger.getLogger(Masken.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Masken.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Masken.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SftpException ex) {
            Logger.getLogger(Masken.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

}
