/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package papps;

import de.abas.ceks.jedp.CantBeginEditException;
import de.abas.ceks.jedp.CantChangeFieldValException;
import de.abas.ceks.jedp.CantInsertRowException;
import de.abas.ceks.jedp.CantReadFieldPropertyException;
import de.abas.ceks.jedp.CantSaveException;
import de.abas.ceks.jedp.EDPEditObject;
import de.abas.ceks.jedp.EDPEditor;
import de.abas.ceks.jedp.EDPQuery;
import de.abas.ceks.jedp.EDPSession;
import de.abas.ceks.jedp.InvalidQueryException;
import de.abas.ceks.jedp.InvalidRowOperationException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import static papps.GlobalVars.ZDArrayNeu;

/**
 *
 * @author gdenz
 */
public class Vartab {
    
    public void schreibvars(EDPEditor edpE1,String vartabfile,int vzeile,boolean Kopf, JTable table)
    {FileReader fr = null;
        try {
            
            fr = new FileReader(vartabfile);
            BufferedReader br = new BufferedReader(fr);
            //Header lesen
            String zeile = br.readLine();
            System.out.println(zeile);
            
               while( (zeile = br.readLine()) != null )
                        {
                        System.out.println(zeile);
                        // Zeile in einzelne Variablen Splitten
                        String[] parts = zeile.split("#");
                        if ((Kopf && parts[2].equals("K")) ||(!Kopf && parts[2].equals("T")))
                            {
                               System.out.println(parts[0]+" "+parts[1]+" "+parts[2]+" "+parts[3]+" "+parts[4]+" "+parts[5]);
                               
                               edpE1.insertRow(vzeile);                              
                               edpE1.setFieldVal(vzeile,"vname", parts[0]);
                               edpE1.setFieldVal(vzeile,"vnname", parts[1]);
                               //edpE1.setFieldVal(vzeile,"vkt", parts[2]);
                               if (parts[3].startsWith("P")) 
                               {
                               
                                String mitdp=parts[3]+":";
                                String vordp=mitdp.substring(0,mitdp.indexOf(":"));
                                String nachdp=mitdp.substring(mitdp.indexOf(":"),parts[3].length());
                                String vdb  = vordp.replaceAll("[^0-9]","");
                                String vV  = vordp.replaceAll("[0-9]","");
                                // vdb muss nun umgesetzt werden auf die gewählte db
                                // Die Info holen wir uns aus der Vartab Table
                                 for (int i=0;i<table.getRowCount();i++)
                                    {
                                    if (table.getValueAt(i, 1).toString().equals(vdb))
                                        {
                                        // Hier steht ein Wert drin,also wird hier eine db belegt
                                        int dbtable = parseInt(table.getValueAt(i,3).toString());
                                        String neuvar=vV+dbtable+nachdp;
                                        edpE1.setFieldVal(vzeile,"vitf", neuvar);
                       
                                        }
                                    }
                                System.out.println(vdb);
 
 
                               }
                               else
                               {    
                               edpE1.setFieldVal(vzeile,"vitf", parts[3]);
                               }
                               edpE1.setFieldVal(vzeile,"vitnf", parts[4]);
                               edpE1.setFieldVal(vzeile,"vskip", parts[5]);
                               edpE1.setFieldVal(vzeile,"vbeds", parts[6]);
                               vzeile++;
                            }
                        }        
                    
                
                br.close();
                
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Vartab.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Vartab.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidRowOperationException ex) {
            Logger.getLogger(Vartab.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CantChangeFieldValException ex) {
            Logger.getLogger(Vartab.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally { 
        try {
            fr.close();
        } catch (IOException ex) {
            Logger.getLogger(Vartab.class.getName()).log(Level.SEVERE, null, ex);
        }
            }
    }
    
    public void install(JTable table, EDPSession session)
    {
      
        EDPEditor edpE1=session.createEditor();
        EDPEditObject eo = null;
        String [] tabFeld=new String[6];
        
       
        
        String vartabfile="";
        String db="";
        String gruppe="";
        String vartab="";
      for (int i=0;i<table.getRowCount();i++)
        {
             
            try {
                vartabfile=table.getValueAt(i, 5).toString();
                db=table.getValueAt(i, 1).toString();
                if (!table.getValueAt(i, 3).toString().equals("0"))
                {
                    //Transfer auf andere Datenbank
                    db=table.getValueAt(i, 3).toString();
                }
                gruppe=table.getValueAt(i, 2).toString();
                // Vartab File einlesen
               
                //per EDP auf die Vartab zugreifen
                vartab="V-"+db+"-"+gruppe;
                edpE1.beginEdit(EDPEditor.EDIT_UPDATE,"12","26",EDPEditor.REFTYPE_NUMSW,vartab);
                // Alle Daten der Vartab ins Objekt laden
                //eo=edpE1.getEditObject();
                
                        
                for (int vzeile=1; vzeile<=edpE1.getRowCount(); vzeile++)
                {
                   System.out.println(edpE1.getFieldVal(vzeile,"vkt")+" "+vzeile);
                   if (edpE1.getFieldVal(vzeile,"vkt").equals("ja"))
                           {
                             //Wir sind am Ende der Kopfvartab  
                               System.out.println("KOPF "+vartab);
                               schreibvars(edpE1,vartabfile,vzeile,true, table);
                               break;
                           }  
                }
                System.out.println("Tabelle "+vartab);
                schreibvars(edpE1,vartabfile,edpE1.getRowCount()+1,false, table);
                //edpE1.updateEditObject(eo);
                edpE1.endEditSave();
                
                    // Zeilen der Datei lesen
                 

              
            } catch (CantBeginEditException ex) {
                Logger.getLogger(Vartab.class.getName()).log(Level.SEVERE, null, ex);
            } catch (CantSaveException ex) {
                Logger.getLogger(Vartab.class.getName()).log(Level.SEVERE, null, ex);
            } catch (CantReadFieldPropertyException ex) {
                Logger.getLogger(Vartab.class.getName()).log(Level.SEVERE, null, ex);
            } 

        }  
    }
    
}
 
               
            