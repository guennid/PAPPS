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
    public static int ZD2Vartab (int zdnummer)
    {int vartabnummer=0;
          if (zdnummer==1) vartabnummer=15;
                    if ((zdnummer>1)&&(zdnummer<=10)) vartabnummer=zdnummer+16;
                    if ((zdnummer>10)&&(zdnummer<=19)) vartabnummer=zdnummer+18;
                    if ((zdnummer>19)&&(zdnummer<=30)) vartabnummer=zdnummer+21;
                    if ((zdnummer>30)&&(zdnummer<=39)) vartabnummer=zdnummer+40;
        return vartabnummer;
    }
    public static int Vartab2ZD (int vartabnummer)
    {int zdnummer=1;
         if (vartabnummer==15) zdnummer=1;
        if ((vartabnummer>=18)&&(vartabnummer<=26)) zdnummer=vartabnummer-16;
        if ((vartabnummer>=29)&&(vartabnummer<=37)) zdnummer=vartabnummer-18;
        if ((vartabnummer>=41)&&(vartabnummer<=51)) zdnummer=vartabnummer-21;
        if ((vartabnummer>=71)&&(vartabnummer<=80)) zdnummer=vartabnummer-40;
        return zdnummer;
    }
    
    public int Vartabtranscode(int zdnummer, JTable table)
    {
        int zdnummerret=0;
        int vartab =ZD2Vartab(zdnummer);
        for (int i=0;i<table.getRowCount();i++)
        {
            String zdstring=table.getValueAt(i, 1).toString();
            int zdint=parseInt(zdstring);
            if (zdint==vartab)
                    {
                       vartab=parseInt(table.getValueAt(i, 3).toString());
                       
                    }
        }
        zdnummerret=Vartab2ZD(vartab);
        return zdnummerret;
    }
    public boolean DBini(String filename,JTable table)
    {
       //Zeile einlesen
        
        //Für DB und Gruppe jeweils eine FOP Zeile erzeugen
        
        return true; 
    }
    
    public boolean schreibbetrieb(String filename, EDPEditor edpE1, JTable table)
    {                                                                                             
        try {
            FileReader fr = null;
            boolean stammdaten=false;
            boolean gruppen=false;
            
            String dbnamevar="";
            String dbnummerstr="";
            String[] zdgnliste;
            int imax=0;
            
            edpE1.beginEdit(EDPEditor.EDIT_UPDATE,"12","10",EDPEditor.REFTYPE_NUMSW,"1");
            
            
            
            fr = new FileReader(filename);
            
            BufferedReader br = new BufferedReader(fr);
            //Header1 lesen
            String zeile="";
            
            zeile = br.readLine();
            
            System.out.println(zeile);
            while( (zeile = br.readLine()) != null )
            {
                /*Nummer#Name ZD#stammdaten#gruppen#Namen der Gruppe#
                1#test#ja#ja#testgruppe,,,,,,,,
                2#test#ja#ja#testgruppe,xy,,,,,,,
                32#test#ja#ja#testgruppe,xy,,,,,,,,,,,,,,*/
               
                String[] zeilelist =zeile.split("#",6);
                dbnummerstr=zeilelist[0];
                String name=zeilelist[1];
                if (zeilelist[2].equals("ja") ) stammdaten=true; else stammdaten=false;
                if (zeilelist[3].equals("ja") ) gruppen=true; else gruppen=false;
                String gruppenstring=zeilelist[4];
                int nummer=parseInt(dbnummerstr);
                nummer=Vartabtranscode(nummer, table);
                if (nummer==1) dbnummerstr=""; else dbnummerstr=new Integer(nummer).toString();
                edpE1.setFieldVal(0,"zdname"+dbnummerstr,name);
                if (edpE1.getFieldVal("zdart"+dbnummerstr).equals("ja"))
                {
                    // Stammdaten ist schon belegt und nicht mehr änderbar
                    if (!stammdaten)
                        
                    {
                        // wir haben ein Problem, Stammdaten ist angeklickt, wir wollen aber keine!
                        return false;
                    }
                }
                else
                {
                    if (stammdaten)
                    {
                        // Feld per edp setzen
                        edpE1.setFieldVal(0,"zdart"+dbnummerstr, "ja");
                    }
                }
                if (gruppen)
                {
                     edpE1.setFieldVal(0,"zdgrp"+dbnummerstr, "ja");
                     edpE1.setFieldVal(0,"zdgn"+dbnummerstr,gruppenstring);
                }
                  
                
              
                
                
            }
              edpE1.endEditSave();
                return true;
        } catch (CantBeginEditException ex) {
            Logger.getLogger(Vartab.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Vartab.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(Vartab.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (CantChangeFieldValException ex) {
            Logger.getLogger(Vartab.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (CantReadFieldPropertyException ex) {
            Logger.getLogger(Vartab.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (CantSaveException ex) {
            Logger.getLogger(Vartab.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void schreibvars(EDPEditor edpE1,String vartabfile,int vzeile,boolean Kopf, JTable table)
    {FileReader fr = null;
    boolean status=false;
        try {
            
        
            fr = new FileReader(vartabfile);
            BufferedReader br = new BufferedReader(fr);
            //Header1 lesen
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
        //EDPEditObject eo = null;
        String [] tabFeld=new String[6];
        boolean status=false;
       
        // Betriebsdatensatz beschreiben
            String Betriebsdatenfile=table.getValueAt(0,5).toString();
            Betriebsdatenfile=Betriebsdatenfile.substring(0,Betriebsdatenfile.indexOf("V-"));
            Betriebsdatenfile=Betriebsdatenfile+"Betriebsdatensatz.txt";
            status=schreibbetrieb(Betriebsdatenfile,edpE1, table);
            
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
 
               
            