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

/**
 *
 * @author günter
 */
public class Aufzählungen {
    
    public boolean Install(EDPSession session, EDPSession session2,File datei) 
    {  FileReader fr = null;
       String zeile="";
       String zeileaufz;
            
         EDPQuery edpQW = session.createQuery();
         EDPQuery edpQA = session2.createQuery();
         EDPEditor edpEW=session.createEditor();
         EDPEditor edpEA=session2.createEditor();
         
        
            
        try {
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
            edpEA.endEditSave();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Aufzählungen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Aufzählungen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CantBeginEditException ex) {
            Logger.getLogger(Aufzählungen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidQueryException ex) {
            Logger.getLogger(Aufzählungen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CantChangeFieldValException ex) {
            Logger.getLogger(Aufzählungen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CantSaveException ex) {
            Logger.getLogger(Aufzählungen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidRowOperationException ex) {
            Logger.getLogger(Aufzählungen.class.getName()).log(Level.SEVERE, null, ex);
        }
          
        
        return true;
                
            
        
    }
    
    // Klassenende
}
