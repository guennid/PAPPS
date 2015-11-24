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
public class Masken {
    
    public void maskentranscode(JTable table, String altezd, String neuezd) {
        int neuezdint=0;
        
        for (int i = 0; i < table.getRowCount(); i++) {
            // Tabelle durchlaufen
            // Und die Maske erst mal in ZD umrechnen
            // Schleife über das Maskenarray der DB
            for ( int dbnummer=0;dbnummer<41;dbnummer++)
                {
                    //Schleife üebr die Gruppen
                 for (int grnummer=0;grnummer < 21;grnummer ++)
                 {
                     // Wenn nun die Maskennummer zur aktuellen Tabellenzeile passt
                   if (GlobalVars.MaskArray.equals(table.getValueAt(i, 1)))
                   {
                   // Dann wissen wir die DB und die GR
                       //Falls diese mit der übergeben ZD die geändert wurde übereinstimmt
                     if (altezd.equals(dbnummer))
                     {
                         // dann müssen wir diese transferieren
                         table.setValueAt(i, 1, GlobalVars.MaskArray[neuezd.parseint][grnummer]);
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
    
}
