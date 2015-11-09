/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package papps;

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

    public boolean Schluesselinstall(JTable table) {
      String dbgr="";
      String datei="";
        for (int i=0;i<table.getRowCount();i++)  
      {
        // Wohin soll denn der Schlüssel
        dbgr=table.getValueAt(i,3).toString();
        //Dateinamen des zu installierenden Schlüssel
        datei=table.getValueAt(i, 4).toString();
        
        Hier gehts weiter
        
      }
     return true;   
    }
}
