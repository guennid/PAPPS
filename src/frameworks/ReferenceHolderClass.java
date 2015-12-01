/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frameworks;



/**
 *
 * @author gdenz
 */
public class ReferenceHolderClass 
     {
   private static MainFrame activeUI;

   public static void setGUI(MainFrame ui)
   {
     activeUI = ui;
   } 

   public static MainFrame getGUI()
   {
     return activeUI;
   } 
}




