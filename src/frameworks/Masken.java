/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frameworks;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import de.abas.ceks.jedp.CantBeginEditException;
import de.abas.ceks.jedp.CantChangeFieldValException;
import de.abas.ceks.jedp.CantReadFieldPropertyException;
import de.abas.ceks.jedp.CantSaveException;
import de.abas.ceks.jedp.EDPEditor;
import de.abas.ceks.jedp.EDPSession;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.JTextArea;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.FileUtils;
 


/**
 *
 * @author gdenz
 */
public class Masken {

    public void maskentranscode(JTable table, String altezd, String neuezd) {
        int neuezdint = 0;
        int dbnummerx = 0;

        for (int i = 0; i < table.getRowCount(); i++) {
            // Tabelle durchlaufen
            // Und die Maske erst mal in ZD umrechnen
            // Schleife über das Maskenarray der DB
            for (int dbnummer = 1; dbnummer < 41; dbnummer++) {
                //Schleife üebr die Gruppen
                for (int grnummer = 0; grnummer < 21; grnummer++) {
                    // Wenn nun die Maskennummer zur aktuellen Tabellenzeile passt
                    if (GlobalVars.MaskArray[dbnummer][grnummer] != null) {
                        if (GlobalVars.MaskArray[dbnummer][grnummer].equals(table.getValueAt(i, 0))) {
                            // Dann wissen wir die Datei und die GR
                            //die muss aber noch übersetzt werden in Vartab anstatt DB
                            Vartab vartab = new Vartab();
                            dbnummerx = vartab.ZD2Vartab(dbnummer);
                            //Falls diese mit der übergeben ZD die geändert wurde übereinstimmt
                            if (altezd.equals(Integer.toString(dbnummerx))) {
                                // dann müssen wir diese transferieren
                                int neuezdi = Integer.parseInt(neuezd);
                                neuezdi = vartab.Vartab2ZD(neuezdi);
                                table.setValueAt(GlobalVars.MaskArray[neuezdi][grnummer], i, 1);
                            }
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

    public void maskegenerieren(EDPSession session, String maskennummer, String prio) {
        try {
            EDPEditor edpE1 = null;
            boolean individuelleMaskeDa = false;

            edpE1 = session.createEditor();
            edpE1.beginEdit(EDPEditor.EDIT_GET, "Infosystem", "",
                    EDPEditor.REFTYPE_NUMSW, "MASKE");
            edpE1.setFieldVal(0, "selmasknr", maskennummer);
            edpE1.setFieldVal(0, "bstart", "");
            int maxrows = edpE1.getRowCount();
            int aktrow = 1;
            // schauen ob eine individuelle Maske schon vorhanden ist
            while (aktrow <= maxrows) {
                if (edpE1.getFieldVal(aktrow, "tprio").equals(prio.toUpperCase()) && edpE1.getFieldVal(aktrow, "tstdmaske").equals("nein")) {
                    // Individuelle Maske ist vorhanden
                    edpE1.setFieldVal(aktrow, "tmaskgen", "");

                }
                aktrow++;
            }
            edpE1.endEditSave();
        } catch (CantBeginEditException ex) {

            Logger.getLogger(Masken.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CantChangeFieldValException ex) {
            Logger.getLogger(Masken.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CantReadFieldPropertyException ex) {
            Logger.getLogger(Masken.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CantSaveException ex) {
            Logger.getLogger(Masken.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void individuelleMaskePruefen(EDPSession session, String maskennummer, String prio,JTextArea jTLog) {
        try {
            EDPEditor edpE1 = null;
            boolean individuelleMaskeDa = false;

            edpE1 = session.createEditor();
            edpE1.beginEdit(EDPEditor.EDIT_GET, "Infosystem", "",
                    EDPEditor.REFTYPE_NUMSW, "MASKE");
            edpE1.setFieldVal(0, "selmasknr", maskennummer);
            edpE1.setFieldVal(0, "bstart", "");
            int maxrows = edpE1.getRowCount();
            int aktrow = 1;
            // schauen ob eine individuelle Maske schon vorhanden ist
            while (aktrow <= maxrows) {
                if (edpE1.getFieldVal(aktrow, "tprio").equals(prio.toUpperCase()) && edpE1.getFieldVal(aktrow, "tstdmaske").equals("nein")) {
                    // Individuelle Maske ist vorhanden
                    individuelleMaskeDa = true;

                }
                aktrow++;
            }
            if (individuelleMaskeDa == false) {
                // wenn keine Maske vorhanden, dann individualisieren
                aktrow = 1;
                while (aktrow <= maxrows) {
                    if (edpE1.getFieldVal(aktrow, "tprio").equals(prio.toUpperCase())) {
                        edpE1.setFieldVal(aktrow, "tindivid", "");
                    }
                    aktrow++;
                }

            }
            edpE1.endEditSave();
        } catch (CantBeginEditException ex) {
            Logger.getLogger(Masken.class.getName()).log(Level.SEVERE, null, ex);
            jTLog.append(ex.toString());
            jTLog.paint(jTLog.getGraphics());
        } catch (CantChangeFieldValException ex) {
            Logger.getLogger(Masken.class.getName()).log(Level.SEVERE, null, ex);
                        jTLog.append(ex.toString());
            jTLog.paint(jTLog.getGraphics());

        } catch (CantReadFieldPropertyException ex) {
            Logger.getLogger(Masken.class.getName()).log(Level.SEVERE, null, ex);
                        jTLog.append(ex.toString());
            jTLog.paint(jTLog.getGraphics());

        } catch (CantSaveException ex) {
            Logger.getLogger(Masken.class.getName()).log(Level.SEVERE, null, ex);
                        jTLog.append(ex.toString());
            jTLog.paint(jTLog.getGraphics());

        }

    }

    public boolean maskenInstall(JTable table, JTextArea jTLog, EDPSession session, String LinuxUser, String LinuxPass, String Host) {
        try {
            ByteArrayOutputStream error = new ByteArrayOutputStream();
            StringBuilder fromServer = new StringBuilder();
            StringBuilder screenls = new StringBuilder();
            StringBuilder screenlsstring = new StringBuilder();
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
            String tabellekopf = "";
            boolean failure = false;
            int firstcell=0;
            int lastcell=0;
            boolean  newpanenotebook=true;

            //SSh Verbindung zum Mandanten öffnen
            SshClient sshclient = new SshClient();
            sshclient.connect(LinuxUser, LinuxPass, Host, 22);

            jTLog.append("----------Maskenimport----------\n");

            jTLog.append("Vorhandene Masken aus Mandanten ermittelt\n");
            jTLog.paint(jTLog.getGraphics());
            //Dateinamen erzeugen
            for (int i = 0; i < table.getRowCount(); i++) {
                //Fehlerflag auf true setzen
                failure = true;
                xmlziel = table.getValueAt(i, 2).toString().replace(table.getValueAt(i, 0).toString(), table.getValueAt(i, 1).toString());
                xmlziel = table.getValueAt(i, 2).toString();
                resourcesziel = table.getValueAt(i, 3).toString().replace(table.getValueAt(i, 0).toString(), table.getValueAt(i, 1).toString());
                maskennummer = table.getValueAt(i, 1).toString();

                // hab ich eine ZD und ein Masken TGZ?      
                if (table.getValueAt(i, 3).equals("TGZ")) {
                    xmlziel = table.getValueAt(i, 2).toString();
                    prio = xmlziel.substring(xmlziel.indexOf(".") + 1, xmlziel.lastIndexOf("."));
                    prio = prio.substring(prio.indexOf(".") + 1, prio.length());
                    // Dann kann ich die Maske einfach auf den Serverv kopieren
                    file = new File(GlobalVars.dir + "\\Masken\\" + table.getValueAt(i, 2).toString());
                    sshexitstatus = sshclient.sendfile(file.toString(), xmlziel);
                    jTLog.append("Neue Maske " + maskennummer + prio + " wird hochgeladen\n");
                    jTLog.paint(jTLog.getGraphics());
                    // und importieren
                    jTLog.append("Neue Maske " + maskennummer + prio + " wird im Mandanten importiert\n");
                    jTLog.paint(jTLog.getGraphics());
                    sshexitstatus = sshclient.sendcommand("eval `sh denv.sh`;screen_import.sh -n " + maskennummer + " -p " + prio + " " + xmlziel, error, fromServer);

                    // und generieren
                    jTLog.append("Neue Maske " + maskennummer + prio + " wird generiert\n");
                    jTLog.paint(jTLog.getGraphics());
                    // Maske generieren
                    maskegenerieren(session, maskennummer, prio);

                    jTLog.append(error.toString());
                    jTLog.paint(jTLog.getGraphics());
                    // Kein ZD und kein TGZ File, also muss die Maske integriert werden
                } else {
                    datei.clear();
                    prio = xmlziel.substring(xmlziel.indexOf(".") + 1, xmlziel.lastIndexOf("."));
                    prio = prio.substring(prio.indexOf(".") + 1, prio.indexOf("-"));
                    //Tabelle oder Kopf
                    tabellekopf = xmlziel.substring(xmlziel.lastIndexOf(".") - 1, xmlziel.lastIndexOf("."));
                    if (tabellekopf.equals("Z")) {
                        tabellekopf = "_T.xml";
                    } else {
                        tabellekopf = "_M.xml";
                    }
                    // Maske individualisieren falls notwendig
                    individuelleMaskePruefen(session, maskennummer, prio,jTLog);
                    // Maske sichern mit screen_export
                    jTLog.append("Maske " + maskennummer + " " + prio + " wird gesichert und kann mit screen_import -n " + maskennummer + " -p " + prio + " screen_restauriert werden\n");
                    jTLog.paint(jTLog.getGraphics());
                    sshexitstatus = sshclient.sendcommand("eval `sh denv.sh`;screen_export.sh -n " + maskennummer + " -p " + prio, error, fromServer);

                   
                    //Tmp leeren
                    File tmpdir=new File("tmp");
                    if (tmpdir.exists()&& (tmpdir.isDirectory()))
                            {
                                deleteDir(tmpdir);
                               
                            }
                     new File ("tmp").mkdir();
                        
                    
                 
   
                    // Masken.tgz abholen
                    jTLog.append("Maske " + maskennummer + " " + prio + " wird geladen\n");
                    jTLog.paint(jTLog.getGraphics());
                    String servermaske = "./screen." + maskennummer + "." + prio + ".tgz";
                    
                    sshexitstatus = sshclient.getfile(servermaske, "tmp\\");
                    if (sshexitstatus == -1) 
                    {// Maske wurde erfolgreich geladen
                        
                        //Maske einlesen
                        jTLog.append("Maske " + maskennummer + " " + prio + " wird ausgepackt\n");
                        jTLog.paint(jTLog.getGraphics()); 
                       //tgz lokal auspacken
                        uncompressTarGZ(new File ("tmp\\screen."+maskennummer+"."+prio+".tgz"),new File ("tmp\\"));
                       // tgz löschen
                        new File ("tmp\\screen."+maskennummer+"."+prio+".tgz").delete();
                        String changemaske="screen_" + maskennummer + "_" + prio + tabellekopf;
                         //Maske einlesen
                        jTLog.append("Maske " + maskennummer + " " + prio + " wird erweitert\n");
                        jTLog.paint(jTLog.getGraphics());
                        BufferedReader inservermaske = null;
                        inservermaske = new BufferedReader(new FileReader("tmp\\screens\\screen_"+maskennummer+"\\"+prio+"\\screen_" + maskennummer +"_"+ prio + tabellekopf));
                        BufferedReader inmaske = null;
                        inmaske = new BufferedReader(new FileReader(GlobalVars.dir + "\\Masken\\" + table.getValueAt(i, 2).toString()));
                        PrintWriter outservermaske = new PrintWriter(new BufferedWriter(new FileWriter("tmp\\screens\\screen_"+maskennummer+"\\"+prio+"\\Xscreen_" + maskennummer +"_"+ prio +tabellekopf)));
                        int lineNr = 0;
                        zielzeile=0;
                        firstcell=0;
                        lastcell=0;
                        String line = inservermaske.readLine();
                        while (line != null) {

                            datei.add(line);
                            if (line.contains("<cell")&&(firstcell==0) ) firstcell=lineNr;
                            if (line.contains("</layout.grid")&&(firstcell!=0) ) lastcell=lineNr;
                            if (line.contains("</pane.notebook>")) {
                                zielzeile = lineNr;
                                newpanenotebook=false;
                            }
                            lineNr++;

                            line = inservermaske.readLine();
                        }
                        inservermaske.close();
                        
                        
                          // Raus schreiben der Datei
                        for (int izeilen = 0; izeilen < lineNr - 1; izeilen++) {
                            outservermaske.print(datei.get(izeilen) + "\n");
                            if ((zielzeile==0)&&(firstcell==izeilen)) 
                            {
                            outservermaske.print("<layout.notebook>\n<pane.notebook msgId=\"b825893d-f00a-4500-ae24-c2fe5364ae0e\">\n <layout.grid>\n" +
                            "      <cell gridX=\"0\" gridY=\"0\">\n");
                            }
                            if ((zielzeile==0)&&(lastcell==izeilen)) 
                            {
                            outservermaske.print("</pane.notebook>\n");
                            zielzeile=izeilen;
                            }
                            
                            
                            if ((izeilen == zielzeile)&&(zielzeile !=0)) {
                                line = inmaske.readLine();
                                while (line != null) {
                                    // Zuerst die Datei einlesen und jede Zeile raus schreiben
                                    outservermaske.print(line + "\n");
                                    line = inmaske.readLine();

                                }
                                if (newpanenotebook) outservermaske.print("</layout.notebook>\n </cell>\n </layout.grid> \n" );
                            }
                        }
                        outservermaske.close();
                        
                        
                        // Neue Datei nun auf die alte kopieren
                        Files.move(Paths.get("tmp\\screens\\screen_"+maskennummer+"\\"+prio+"\\Xscreen_" + maskennummer +"_"+ prio +tabellekopf),Paths.get("tmp\\screens\\screen_"+maskennummer+"\\"+prio+"\\screen_" + maskennummer +"_"+ prio +tabellekopf),REPLACE_EXISTING);
                        
                        // Resources Dateien anschauen
                        // Append an die Resources.language
                         // sshexitstatus = sshclient.sendcommand("cat  ./screens/screen_" + maskennummer + "/" + prio + "/ResourcesFW  >> ./screens/screen_" + maskennummer + "/" + prio + "/Resources.language", error, fromServer);
                          String resourcesstring=FileUtils.readFileToString(new File(GlobalVars.dir+ "\\Masken\\" + table.getValueAt(i, 3).toString()), "utf-8");
                          FileWriter fw = new FileWriter(new File("tmp\\screens\\screen_" + maskennummer + "\\" + prio + "\\Resources.language"),true); 
                          fw.write(resourcesstring);//appends the string to the file
                          if (newpanenotebook) fw.write("b825893d-f00a-4500-ae24-c2fe5364ae0e=Allgemein\n");
                          fw.close();
                          //An Resources_*  den String auch noch anhängen
                          // dazu erst mal die Dateien suchen
                          Path dir = Paths.get("tmp\\screens\\screen_" + maskennummer + "\\" + prio );
                          try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "Resources_*")) {
                          for (Path resourcesfile : stream) 
                          {
                              fw = new FileWriter(resourcesfile.toFile(),true);
                              fw.write(resourcesstring);//appends the string to the file
                              if (newpanenotebook) fw.write("b825893d-f00a-4500-ae24-c2fe5364ae0e=Allgemein\n");
                              fw.close();
                          }
                          }
                          
                          
                       
                        // Tgz wieder erzeugen
                        createTarGzip("tmp\\","tmp\\screen."+maskennummer+"."+prio+".tgz");
                        // Maske wieder auf Server schieben
                       
                        
                    sshexitstatus = sshclient.sendfile("\\tmp\\screen."+maskennummer+"."+prio+".tgz", "screen."+maskennummer+"."+prio+".tgz");
                    jTLog.append("Neue Maske " + maskennummer + prio + " wird hochgeladen\n");
                    jTLog.paint(jTLog.getGraphics());
                    // und importieren
                    jTLog.append("Neue Maske " + maskennummer + prio + " wird im Mandanten importiert\n");
                    jTLog.paint(jTLog.getGraphics());
                    sshexitstatus = sshclient.sendcommand("eval `sh denv.sh`;screen_import.sh -n " + maskennummer + " -p " + prio + " " + "screen."+maskennummer+"."+prio+".tgz", error, fromServer);

                    // und generieren
                    jTLog.append("Neue Maske " + maskennummer + prio + " wird generiert\n");
                    jTLog.paint(jTLog.getGraphics());
                    // Maske generieren
                    maskegenerieren(session, maskennummer, prio);

                    jTLog.append(error.toString());
                    jTLog.paint(jTLog.getGraphics());
                        
         
                    }
                    /*
                    //Alter Weg
                    // Maske abholen
                    jTLog.append("Maske " + maskennummer + " " + prio + " wird geladen\n");
                    jTLog.paint(jTLog.getGraphics());
                    //String servermaske = "./screens/screen_" + maskennummer + "/" + prio + "/screen_" + maskennummer + "_" + prio + "_M.xml";
                    servermaske = "./screens/screen_" + maskennummer + "/" + prio + "/screen_" + maskennummer + "_" + prio + tabellekopf;

                    sshexitstatus = sshclient.getfile(servermaske, "tmp\\" + maskennummer + prio + ".xml");
                    if (sshexitstatus == -1) {// Maske wurde erfolgreich geladen
                        //Maske einlesen
                        jTLog.append("Maske " + maskennummer + " " + prio + " wird erweitert\n");
                        jTLog.paint(jTLog.getGraphics());

                        BufferedReader inservermaske = null;
                        inservermaske = new BufferedReader(new FileReader("tmp\\" + maskennummer + prio + ".xml"));
                        BufferedReader inmaske = null;
                        inmaske = new BufferedReader(new FileReader(GlobalVars.dir + "\\Masken\\" + table.getValueAt(i, 2).toString()));
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
                        jTLog.append("Maske " + maskennummer + " " + prio + " wird gesendet\n");
                        jTLog.paint(jTLog.getGraphics());
                        sshexitstatus = sshclient.sendfile("tmp\\" + maskennummer + prio + "X.xml", servermaske);
                        if (sshexitstatus == -1) {// Maske wurde auf Server übertragen
                            // Jetzt noch die Resources Datei an die Resources auf dem server anhängen
                            sshexitstatus = sshclient.sendfile(GlobalVars.dir + "\\Masken\\" + table.getValueAt(i, 3).toString(), "./screens/screen_" + maskennummer + "/" + prio + "/ResourcesFW");
                            if (sshexitstatus == -1) {
                                sshexitstatus = sshclient.sendcommand("cat  ./screens/screen_" + maskennummer + "/" + prio + "/ResourcesFW  >> ./screens/screen_" + maskennummer + "/" + prio + "/Resources.language", error, fromServer);
                                //Resources wurde auf Server übertragen  
                                 jTLog.append(error.toString());
                                jTLog.paint(jTLog.getGraphics());
                                if (sshexitstatus==0)
                                {
                                   sshexitstatus = sshclient.sendcommand("ls screens/screen_" + maskennummer + "/" + prio + "/Resources_* -l", error, fromServer);
                                   jTLog.append(error.toString());
                                    jTLog.paint(jTLog.getGraphics());
                                if (sshexitstatus == 0) {
                                    
                                    resourcesServerString = fromServer.toString();
                                    resourcesServerArray = resourcesServerString.split("\n");
                                    ///Bisher alles gut gelaufen, deswegen failure auf false setzen
                                    failure = false;
                                    for (int iresources = 0; iresources < resourcesServerArray.length; iresources++) {
                                        if (resourcesServerArray[iresources].toString().contains("Resources")) {
                                            sshexitstatus = sshclient.sendcommand("cat  ./screens/screen_" + maskennummer + "/" + prio + "/ResourcesFW  >>" + resourcesServerArray[iresources].toString().substring(resourcesServerArray[iresources].lastIndexOf(" "), resourcesServerArray[iresources].length()), error, fromServer);
                                            if (sshexitstatus != 0) {
                                                failure = true;
                                            }
                                            jTLog.append(error.toString());
                                            jTLog.paint(jTLog.getGraphics());
                                        }

                                    }

                                    jTLog.append("Maske " + maskennummer + " " + prio + " wird generiert\n");

                                    // Maske nun noch generieren
                                    maskegenerieren(session, maskennummer, prio); 
                                }
                               
                                
                                }

                            }

                        }

                    }*/
                    if (failure) {
                        
                        //Alte Maske wieder importieren 
                      //  jTLog.append(error.toString());
                      //  jTLog.append("Gesicherte Maske wird wieder installiert !!! manuelle Nacharbeit notwendig!!!\n");
                      //  jTLog.paint(jTLog.getGraphics());
                      //  sshexitstatus = sshclient.sendcommand("eval `sh denv.sh`;screen_import.sh -n " + maskennummer + " -p " + prio+" screen."+maskennummer+"."+prio+".tgz", error, fromServer);
                    }

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
public static void uncompressTarGZ(File tarFile, File dest) throws IOException {
    dest.mkdir();
    TarArchiveInputStream tarIn = null;

    tarIn = new TarArchiveInputStream(
                new GzipCompressorInputStream(
                    new BufferedInputStream(
                        new FileInputStream(
                            tarFile
                        )
                    )
                )
            );

    TarArchiveEntry tarEntry = tarIn.getNextTarEntry();
    // tarIn is a TarArchiveInputStream
    while (tarEntry != null) {// create a file with the same name as the tarEntry
        File destPath = new File(dest, tarEntry.getName());
        System.out.println("working: " + destPath.getCanonicalPath());
        if (tarEntry.isDirectory()) {
            destPath.mkdirs();
        } else {
            if (!destPath.getParentFile().exists()) { destPath.getParentFile().mkdirs(); } destPath.createNewFile();
            //byte [] btoRead = new byte[(int)tarEntry.getSize()];
            byte [] btoRead = new byte[1024];
            //FileInputStream fin 
            //  = new FileInputStream(destPath.getCanonicalPath());
            BufferedOutputStream bout = 
                new BufferedOutputStream(new FileOutputStream(destPath));
            int len = 0;

            while((len = tarIn.read(btoRead)) != -1)
            {
                bout.write(btoRead,0,len);
            }

            bout.close();
            btoRead = null;

        }
        tarEntry = tarIn.getNextTarEntry();
    }
    tarIn.close();
} 

public static void CreateTarGZ(String dirPath,String tarGzPath)
    
{
    
        FileOutputStream fOut = null;
        try {
            System.out.println(new File(".").getAbsolutePath());
         //   dirPath = "parent/childDirToCompress/";
         //   tarGzPath = "archive.tar.gz";
            fOut = new FileOutputStream(new File(tarGzPath));
            BufferedOutputStream bOut = new BufferedOutputStream(fOut);
            GzipCompressorOutputStream gzOut = new GzipCompressorOutputStream(bOut);
            TarArchiveOutputStream tOut = new TarArchiveOutputStream(gzOut);
            addFileToTarGz(tOut, dirPath, "");
            tOut.finish();
            tOut.close();
            gzOut.close();
            bOut.close();
            fOut.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Masken.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Masken.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fOut.close();
            } catch (IOException ex) {
                Logger.getLogger(Masken.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


public static void addFileToTarGz(TarArchiveOutputStream tOut, String path, String base) throws IOException
 
{
    File f = new File(path);
    System.out.println(f.exists());
    //tmp wegschneiden
  if (base.startsWith("tmp")) 
  {base=base.substring(4,base.length());}
   String entryName = base + f.getName();
   
    TarArchiveEntry tarEntry = new TarArchiveEntry(f, entryName);
   tarEntry.setSize(f.length()); 
    tOut.putArchiveEntry(tarEntry);

    if (f.isFile()) {
IOUtils.copy(new FileInputStream(f), tOut);      
//  FileInputStream in = new FileInputStream(f);
      //  IOUtils.copy(in, tOut);
       // in.close();
        
        
    } else {
        tOut.closeArchiveEntry();
        
        File[] children = f.listFiles();
        if (children != null) {
            for (File child : children) {
                System.out.println(child.getName());
                if (!child.getName().startsWith("screen."))
                {
                addFileToTarGz(tOut, child.getAbsolutePath(), entryName + "/");
                
                }
            }
            
        }
    }
}

 public static void createTarGzip(String dirPath,String tarGzPath) throws IOException {
    File  inputDirectoryPath = new File (dirPath);
    File outputFile = new File(tarGzPath);

    try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(bufferedOutputStream);
            TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(gzipOutputStream)) {

        tarArchiveOutputStream.setBigNumberMode(TarArchiveOutputStream.BIGNUMBER_POSIX);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        List<File> files = new ArrayList<>(FileUtils.listFiles(
                inputDirectoryPath,
                new RegexFileFilter("^(.*?)"),
                DirectoryFileFilter.DIRECTORY
        ));

        for (int i = 0; i < files.size(); i++) {
            
            File currentFile = files.get(i);
            if (!currentFile.getName().contains(".tgz"))
                    {
            String relativeFilePath = new File(inputDirectoryPath.toURI()).toURI().relativize(
                    new File(currentFile.getAbsolutePath()).toURI()).getPath();

            TarArchiveEntry tarEntry = new TarArchiveEntry(currentFile, relativeFilePath);
            tarEntry.setSize(currentFile.length());

            tarArchiveOutputStream.putArchiveEntry(tarEntry);
            FileInputStream in = new FileInputStream(currentFile); 
            //tarArchiveOutputStream.write(IOUtils.toByteArray(new FileInputStream(currentFile)));
            tarArchiveOutputStream.write(IOUtils.toByteArray(in));
            
            tarArchiveOutputStream.closeArchiveEntry();
            in.close();
        }
        }
        tarArchiveOutputStream.close();
        
    }
}

   public static void deleteDir(File path) {
      for (File file : path.listFiles()) {
         if (file.isDirectory())
            deleteDir(file);
         file.delete();
      }
      path.delete();
   }

}
