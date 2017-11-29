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
import de.abas.ceks.jedp.CantSaveException;
import de.abas.ceks.jedp.EDPEditor;
import de.abas.ceks.jedp.EDPQuery;
import de.abas.ceks.jedp.EDPSession;
import de.abas.ceks.jedp.InvalidQueryException;
import de.abas.ceks.jedp.InvalidRowOperationException;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.JTextArea;

/**
 *
 * @author günter
 */
public class Aufzählungen {
    
    public boolean Install(EDPSession session, File datei, JTextArea jTLog) 
    {  FileReader fr = null;
       String zeile="";
       String zeileaufz;
     String[] zeilelistaufz;      
         EDPQuery edpQW = session.createQuery();
      //   EDPQuery edpQA = session2.createQuery();
         EDPEditor edpEW=session.createEditor();
        // EDPEditor edpEA=session2.createEditor();
         
        
            
        try {
            if (datei.toString().contains("Tabelle"))
                    {
                    // Wertemengen   
            fr = new FileReader(datei);
            BufferedReader br = new BufferedReader(fr);
            //Header einlesen
           // zeileaufz = br.readLine();
           while( (zeileaufz = br.readLine()) != null )
           {
            zeilelistaufz =zeileaufz.split("#",11);
            zeilelistaufz[0]=zeilelistaufz[0].replace("-",":");
            edpQW.startQuery(zeilelistaufz[0],"","nummer=="+ zeilelistaufz[1],"id");
            if (edpQW.getNextRecord())
            {
                               jTLog.append("Wertemenge "+zeilelistaufz[1]+ " schon vorhanden !!!!");
                               jTLog.setCaretPosition(jTLog.getText().length());
                /*
                if (zeilelistaufz[0].equals("109:1"))
                        {
                            jTLog.append("Wertemenge "+zeilelistaufz[1]+ " schon vorhanden !!!!");
                             jTLog.append("Wertemenge "+zeilelistaufz[1]+ " geändert\n");
                // Datensatz gibt es shcon, also editieren
                        edpEW.beginEdit(edpQW.getField("id")); 
                         edpEW.setFieldVal("such", zeilelistaufz[2]);
                         edpEW.setFieldVal("name", zeilelistaufz[3]);
                         edpEW.setFieldVal("name2", zeilelistaufz[4]);
                         edpEW.setFieldVal("besch", zeilelistaufz[5]);
                         edpEW.endEditSave();
                        }
                else
                {    
                jTLog.append("Wertemenge "+zeilelistaufz[1]+ " geändert\n");
                // Datensatz gibt es shcon, also editieren
                        edpEW.beginEdit(edpQW.getField("id")); 
                         edpEW.setFieldVal("such", zeilelistaufz[2]);
                         edpEW.setFieldVal("name", zeilelistaufz[3]);
                         edpEW.setFieldVal("name2", zeilelistaufz[4]);
                         edpEW.setFieldVal("langtxt", zeilelistaufz[5]);
                         edpEW.setFieldVal("kbezbspr", zeilelistaufz[6]);
                         edpEW.setFieldVal("katego", zeilelistaufz[7]);
                         edpEW.setFieldVal("icontxt", zeilelistaufz[8]);
                         edpEW.setFieldVal("besch", zeilelistaufz[9]);
                         edpEW.endEditSave();
                }
                         */
            }
            else
            {
                 // Datensatz Neu anlegen 
                if (zeilelistaufz[0].equals("109:1"))
                {
                     String db=zeilelistaufz[0].substring(0,zeilelistaufz[0].indexOf(":"));
                        String gruppe=zeilelistaufz[0].substring(zeilelistaufz[0].indexOf(":")+1,zeilelistaufz[0].length());
                        edpEW.beginEditNew(db,gruppe);
                         edpEW.setFieldVal("nummer", zeilelistaufz[1]);
                         edpEW.setFieldVal("such", zeilelistaufz[2]);
                         edpEW.setFieldVal("name", zeilelistaufz[3]);
                         edpEW.setFieldVal("name2", zeilelistaufz[4]);
                         edpEW.setFieldVal("besch", zeilelistaufz[5]);
                         edpEW.endEditSave();
                         jTLog.append("Wertemenge "+zeilelistaufz[1]+ " neu angelegt\n");
                         jTLog.setCaretPosition(jTLog.getText().length());
                }
                else
                {
                        String db=zeilelistaufz[0].substring(0,zeilelistaufz[0].indexOf(":"));
                        String gruppe=zeilelistaufz[0].substring(zeilelistaufz[0].indexOf(":")+1,zeilelistaufz[0].length());
                        edpEW.beginEditNew(db,gruppe);
                         edpEW.setFieldVal("nummer", zeilelistaufz[1]);
                         edpEW.setFieldVal("such", zeilelistaufz[2]);
                         edpEW.setFieldVal("name", zeilelistaufz[3]);
                         edpEW.setFieldVal("name2", zeilelistaufz[4]);
                         edpEW.setFieldVal("langtxt", zeilelistaufz[5]);
                         edpEW.setFieldVal("kbezbspr", zeilelistaufz[6]);
                         edpEW.setFieldVal("katego", zeilelistaufz[7]);
                         edpEW.setFieldVal("icontxt", zeilelistaufz[8]);
                         edpEW.setFieldVal("besch", zeilelistaufz[9]);
                         edpEW.endEditSave();
                         jTLog.append("Wertemenge "+zeilelistaufz[1]+ " neu angelegt\n");
                         jTLog.setCaretPosition(jTLog.getText().length());
                       //   xxxxx
                }
            }
           }
            }
            else
            {
                //Aufzählung
                fr = new FileReader(datei);
            BufferedReader br = new BufferedReader(fr);
            //Header einlesen
            zeileaufz = br.readLine();
            zeileaufz = br.readLine();
            zeileaufz = br.readLine();
            zeilelistaufz =zeileaufz.split("#",12);
            edpQW.startQuery("107:01","","nummer=="+zeilelistaufz[1],"id");
            if (edpQW.getNextRecord())
            {
                
                  jTLog.append("Aufzählung "+zeilelistaufz[1]+ " schon vorhanden!!!\n");
                  jTLog.setCaretPosition(jTLog.getText().length());
             /*     
                  jTLog.append("Aufzählung "+zeilelistaufz[1]+ " geändert\n");
                // Datensatz gibt es shcon, aslo editieren
                 edpEW.beginEdit(edpQW.getField("id")); 
                 edpEW.setFieldVal("such",zeilelistaufz[2]);
                edpEW.setFieldVal("name",zeilelistaufz[3]);
                edpEW.setFieldVal("name2",zeilelistaufz[4]);
                edpEW.setFieldVal("classname",zeilelistaufz[5]);
                //edpEW.setFieldVal("tabart",zeilelistaufz[6]);
                edpEW.setFieldVal("fldname",zeilelistaufz[7]);
                edpEW.setFieldVal("fldkname",zeilelistaufz[8]);
                edpEW.setFieldVal("fldbezname",zeilelistaufz[9]);
                edpEW.setFieldVal("fldiname",zeilelistaufz[10]);
                edpEW.deleteAllRows();
                while( (zeileaufz = br.readLine()) != null )
                {
                   
                    zeilelistaufz=zeileaufz.split("#",12);
                    edpEW.insertRow(zeilelistaufz[1]);
                    
                     edpEW.setFieldVal(zeilelistaufz[1],"vaufzelem",zeilelistaufz[2]);
                     edpEW.setFieldVal(zeilelistaufz[1],"aebez",zeilelistaufz[3]);
                     edpEW.setFieldVal(zeilelistaufz[1],"aefix",zeilelistaufz[4]);
                     edpEW.setFieldVal(zeilelistaufz[1],"aekbez",zeilelistaufz[5]);
                     edpEW.setFieldVal(zeilelistaufz[1],"aekfix",zeilelistaufz[6]);
                     edpEW.setFieldVal(zeilelistaufz[1],"aebezeichner",zeilelistaufz[7]);
                     edpEW.setFieldVal(zeilelistaufz[1],"aebezfix",zeilelistaufz[8]);
                     edpEW.setFieldVal(zeilelistaufz[1],"aeaktiv",zeilelistaufz[9]);
                     edpEW.setFieldVal(zeilelistaufz[1],"aedefault",zeilelistaufz[10]);
                     
                     
                }
                 edpEW.endEditSave();*/
            }
            else
            {
                jTLog.append("Aufzählung "+zeilelistaufz[1]+ " neu angelegt\n");
                jTLog.setCaretPosition(jTLog.getText().length());
                //Datensatz neu anlegen
                edpEW.beginEditNew("107","01");
                edpEW.setFieldVal("nummer",zeilelistaufz[1]);
                edpEW.setFieldVal("such",zeilelistaufz[2]);
                edpEW.setFieldVal("name",zeilelistaufz[3]);
                edpEW.setFieldVal("name2",zeilelistaufz[4]);
                edpEW.setFieldVal("classname",zeilelistaufz[5]);
                edpEW.setFieldVal("tabart",zeilelistaufz[6]);
                edpEW.setFieldVal("fldname",zeilelistaufz[7]);
                edpEW.setFieldVal("fldkname",zeilelistaufz[8]);
                edpEW.setFieldVal("fldbezname",zeilelistaufz[9]);
                edpEW.setFieldVal("fldiname",zeilelistaufz[10]);
                while( (zeileaufz = br.readLine()) != null )
                {
                   
                    zeilelistaufz=zeileaufz.split("#",12);
                    edpEW.insertRow(zeilelistaufz[1]);
                    
                     edpEW.setFieldVal(zeilelistaufz[1],"vaufzelem",zeilelistaufz[2]);
                     edpEW.setFieldVal(zeilelistaufz[1],"aebez",zeilelistaufz[3]);
                     edpEW.setFieldVal(zeilelistaufz[1],"aefix",zeilelistaufz[4]);
                     edpEW.setFieldVal(zeilelistaufz[1],"aekbez",zeilelistaufz[5]);
                     edpEW.setFieldVal(zeilelistaufz[1],"aekfix",zeilelistaufz[6]);
                     edpEW.setFieldVal(zeilelistaufz[1],"aebezeichner",zeilelistaufz[7]);
                     edpEW.setFieldVal(zeilelistaufz[1],"aebezfix",zeilelistaufz[8]);
                     edpEW.setFieldVal(zeilelistaufz[1],"aeaktiv",zeilelistaufz[9]);
                     edpEW.setFieldVal(zeilelistaufz[1],"aedefault",zeilelistaufz[10]);
                     
                     
                }
                 edpEW.endEditSave();
                  
                  
            }    
            }
           /*
            // Datei einlesen
            fr = new FileReader(datei);
            BufferedReader br = new BufferedReader(fr);
            //Header einlesen
            zeile = br.readLine();
            // Die Aufzählung ist definiert als:
            zeileaufz=br.readLine();
            String[] zeilelistaufz =zeileaufz.split("#",4);
            // Selektierne nach der Aufzählung
             edpQA.startQuery("107:01","","such=="+zeilelistaufz[0],"id");
              if(edpQA.getNextRecord())
                    {
                        // Datensatz gibt es shcon, also editieren
                        edpEA.beginEdit(edpQA.getField("id"));
                    }
                    else
                    {
                        // Datensatz Neu anlegen 
                        edpEA.beginEditNew("107","01");
                         edpEA.setFieldVal("such", zeilelistaufz[0]);
                         
                         edpEA.setFieldVal("tabart", "P109:2");
                    }
                    //Name
                    edpEA.setFieldVal("name", zeilelistaufz[1]);
                    // Tabelle löschen
                    edpEA.deleteAllRows();
                    // Soweit mit der Aufzählung
                    
            // Erneuter Header
            zeile = br.readLine();
            while( (zeile = br.readLine()) != null )
            {
                // Die Bezeichner einlesen und importieren
                // Split der Zeile
                 String[] zeilelist =zeile.split("#",6);
             // Mit Query schauen ob Suchwort vorhanden
                 
                 edpQW.startQuery("109:02","","such=="+zeilelist[0],"id");
                if(edpQW.getNextRecord())
                    {
                        edpEW.beginEdit(edpQW.getField("id"));
                    }
                    else
                    {
                  edpEW.beginEditNew("109","02");
                  edpEW.setFieldVal("such", zeilelist[0]);
                    }
                //Suchwort beschreiben
                
                // Kategorie beschreiben
                edpEW.setFieldVal("katego", zeilelist[1]);
                //Name beschreiben
                edpEW.setFieldVal("name", zeilelist[2]);
                edpEW.endEditSave();
               // Soweit mit dem Bezeichner, nun noch in die Tabelle der Aufzählugn eintragen
                edpEA.insertRow(edpEA.getRowCount()+1);
               edpEA.setFieldVal(edpEA.getCurrentRow(),"aufzelem" ,zeilelist[0]);
            }
            edpEA.endEditSave();*/
      
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Aufzählungen.class.getName()).log(Level.SEVERE, null, ex);
            jTLog.append(ex.toString());
        } catch (IOException ex) {
            Logger.getLogger(Aufzählungen.class.getName()).log(Level.SEVERE, null, ex);
            jTLog.append(ex.toString());
        } catch (InvalidQueryException ex) {
            Logger.getLogger(Aufzählungen.class.getName()).log(Level.SEVERE, null, ex);
            jTLog.append(ex.toString());
        } catch (CantBeginEditException ex) {
            Logger.getLogger(Aufzählungen.class.getName()).log(Level.SEVERE, null, ex);
            jTLog.append(ex.toString());
        } catch (CantChangeFieldValException ex) {
            Logger.getLogger(Aufzählungen.class.getName()).log(Level.SEVERE, null, ex);
            jTLog.append(ex.toString());
        } catch (CantSaveException ex) {
            Logger.getLogger(Aufzählungen.class.getName()).log(Level.SEVERE, null, ex);
            jTLog.append(ex.toString());
        } catch (InvalidRowOperationException ex) {
            Logger.getLogger(Aufzählungen.class.getName()).log(Level.SEVERE, null, ex);
            jTLog.append(ex.toString());
        }
          
           jTLog.append("Aufzählungen fertig\n");   
        return true;
                
            
        
    }
    
    // Klassenende
}
