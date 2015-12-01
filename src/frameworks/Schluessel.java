/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frameworks;

import de.abas.ceks.jedp.CantBeginEditException;
import de.abas.ceks.jedp.CantChangeFieldValException;
import de.abas.ceks.jedp.CantSaveException;
import de.abas.ceks.jedp.EDPEditor;
import de.abas.ceks.jedp.EDPQuery;
import de.abas.ceks.jedp.EDPSession;
import de.abas.ceks.jedp.InvalidQueryException;
import de.abas.ceks.jedp.InvalidRowOperationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;

/**
 *
 * @author gdenz
 */
public class Schluessel {

    public void schluesseltranscode1(JTable table, String altezd, String neuezd) {
        String db = "";
        String dbgr = "";
        String gr = "";
        for (int i = 0; i < table.getRowCount(); i++) {
            dbgr = table.getValueAt(i, 3).toString();
            db = dbgr.substring(0, dbgr.indexOf(":"));
            gr = dbgr.substring(dbgr.indexOf(":") + 1, dbgr.length());
            if (db.equals(altezd)) {
                table.setValueAt(neuezd + ":" + gr, i, 3);
            }
        }
    }

    public boolean Schluesselinstall(JTable table,EDPSession session) {
      FileReader fr = null;
        String dbgr="";
        String zeile="";
      String datei="";
      EDPQuery edpSQ = session.createQuery();
      EDPEditor edpSE=session.createEditor();
      
        for (int i=0;i<table.getRowCount();i++)  
      {
          try {
              // Wohin soll denn der Schlüssel
              dbgr=table.getValueAt(i,3).toString();
              //Dateinamen des zu installierenden Schlüssel
              datei=table.getValueAt(i, 4).toString();
              //Import des Schlüssels
              // Zuerst mal nach Schlüssel selektieren
              edpSQ.startQuery("12:31","","nummer=="+table.getValueAt(i, 0),"id");
               if(edpSQ.getNextRecord())
                   {
                        // Datensatz gibt es shcon, also editieren
                        edpSE.beginEdit(edpSQ.getField("id"));
                    }
               else
                    {
                        // Datensatz Neu anlegen 
                        edpSE.beginEditNew("12","31");
                         edpSE.setFieldVal("nummer", table.getValueAt(i,0).toString());

                    }

                         edpSE.setFieldVal("such", table.getValueAt(i,1).toString());
                         edpSE.setFieldVal("name", table.getValueAt(i,2).toString());
                         edpSE.setFieldVal("bgrliste", table.getValueAt(i,3).toString());
                         // Den Rest der Infos holen wir aus der Textdatei
                         File file = new File(table.getValueAt(i, 4).toString());
                         fr = new FileReader(file);
                      //    fr = new FileReader(GlobalVars.dir.toString()+"\\"+table.getValueAt(i, 4).toString());
                           BufferedReader br = new BufferedReader(fr);
                           //Header einlesen
                            zeile = br.readLine();
                            //2. Header einlesen
                            zeile = br.readLine();
                           // Kopfdaten des Schlüssels lesen
                            zeile = br.readLine();
                            String[] zeilelist =zeile.split("#",10);
                           edpSE.setFieldVal("bname", zeilelist[3]);
                           edpSE.setFieldVal("bart", zeilelist[5]);
                          if (!zeilelist[6].equals("")) {
                           edpSE.setFieldVal("bbvname", zeilelist[6]);
                          }
                           edpSE.setFieldVal("baart", zeilelist[7]);
                           edpSE.setFieldVal("bsel", zeilelist[8]);
                          // Und jetzt die Schleife über die Tabelle
                           edpSE.deleteAllRows();
                           while( (zeile = br.readLine()) != null )
                           {
                            zeilelist =zeile.split("#",3);
                            edpSE.insertRow(edpSE.getRowCount()+1);
                            edpSE.setFieldVal(zeilelist[0],"bvname", zeilelist[1]);
                               
                           }
                           //Fertig, drum Speichern
                             edpSE.endEditSave();
          } catch (InvalidQueryException ex) {
              Logger.getLogger(Schluessel.class.getName()).log(Level.SEVERE, null, ex);
          } catch (CantBeginEditException ex) {
              Logger.getLogger(Schluessel.class.getName()).log(Level.SEVERE, null, ex);
          } catch (CantChangeFieldValException ex) {
              Logger.getLogger(Schluessel.class.getName()).log(Level.SEVERE, null, ex);
          } catch (FileNotFoundException ex) {
              Logger.getLogger(Schluessel.class.getName()).log(Level.SEVERE, null, ex);
          } catch (IOException ex) {
              Logger.getLogger(Schluessel.class.getName()).log(Level.SEVERE, null, ex);
          } catch (InvalidRowOperationException ex) {
              Logger.getLogger(Schluessel.class.getName()).log(Level.SEVERE, null, ex);
          } catch (CantSaveException ex) {
              Logger.getLogger(Schluessel.class.getName()).log(Level.SEVERE, null, ex);
          }
        
      }
     return true;   
    }
}
