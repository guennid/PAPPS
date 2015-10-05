






package papps;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import de.abas.ceks.jedp.CantBeginEditException;
import de.abas.ceks.jedp.CantBeginSessionException;
import de.abas.ceks.jedp.CantReadFieldPropertyException;
import de.abas.ceks.jedp.EDPEditor;
import de.abas.ceks.jedp.EDPFactory;
import de.abas.ceks.jedp.EDPQuery;
import de.abas.ceks.jedp.EDPSession;
import de.abas.ceks.jedp.InvalidQueryException;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import static papps.GlobalVars.ZDArray;
import static papps.GlobalVars.ZDArrayDB;
import static papps.GlobalVars.ZDArrayNeu;

/**
 *
 * @author gdenz
 * error stream jsch
 * http://www.programcreek.com/java-api-examples/index.php?api=com.jcraft.jsch.JSchException
 * ByteArrayOutputStream error=new ByteArrayOutputStream();
    executor.setErrStream(error);
 */
public class MainFrame extends javax.swing.JFrame {
 private DefaultMutableTreeNode rootnode;
    private DefaultTreeModel treeModel;
    
    private DefaultMutableTreeNode APPode;
    private DefaultMutableTreeNode SystemNode;
    

    
    /**
     * Creates new form NewApplication
     */
    public MainFrame() {
        
        initComponents();
        rootnode = new DefaultMutableTreeNode("PAPPS");
        treeModel = new DefaultTreeModel(rootnode);
        jTree1.setModel(treeModel);
        DirList(GlobalVars.target,rootnode,true );
        jTLinuxUser.setText("erp");
        jTHost.setText("10.0.3.200");
        jPLinux.setText("erp");
        jTMandant.setText("erp");
        jPMandant.setText("master");
        jTVartab.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE); 
        
    }
private  static class MyCellEditor extends AbstractCellEditor implements TableCellEditor {
// static ?
    
    DefaultCellEditor checkbox = new DefaultCellEditor(new JComboBox(new Object[] {"abc"}));

    private DefaultCellEditor lastSelected;

    @Override
    public Object getCellEditorValue() {

        return lastSelected.getCellEditorValue();
    }

    @Override
    public Component getTableCellEditorComponent(final JTable  table,
            Object value, boolean isSelected, final int row, int column) {
            String db=table.getValueAt(row, 1).toString();
            String grp=table.getValueAt(row, 2).toString();
            int grpint=Integer.parseInt(grp);
            int dbint=Integer.parseInt(db);
            
        
            //String grp=table.getValueAt(row, 2).toString();
             String[] comboarray;
             comboarray=checkzd(dbint,grpint, table);
            if (comboarray!=null ) 
                                      {
                                      //checkbox= new DefaultCellEditor(new JComboBox(new Object[] {comboarray}));
                                       checkbox= new DefaultCellEditor(new JComboBox(comboarray));  
                                       checkbox.addCellEditorListener(new CellEditorListener()
                                               {

                                           @Override
                                           public void editingStopped(ChangeEvent e) 
                                           {
                                         //  System.out.println("Ende");                                         
                                         //Den Frei Haken setzen in der Table
                                           table.setValueAt(true, row, 4); 
                                           }

                                           @Override
                                           public void editingCanceled(ChangeEvent e) {
                                          //      System.out.println("Cancel");
                                          // Brauchen wir nicht     
                                           }
                                                   
                                               }
                                               );
         
                                      }
             
            
            
            lastSelected = checkbox;
            return checkbox.getTableCellEditorComponent(table, value, isSelected, row, column);
        
    }

}
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTKurzInfo = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTinfosystem = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTAufzaehlungen = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTVartab = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTFOP = new javax.swing.JTable();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jBConnectionTest = new javax.swing.JButton();
        jTHost = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTLinuxUser = new javax.swing.JTextField();
        jPLinux = new javax.swing.JPasswordField();
        jTMandant = new javax.swing.JTextField();
        jPMandant = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jBInstallInfosysteme = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTLog = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jBInstallAufzaehlungen = new javax.swing.JButton();
        jBInstallVartab = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Project Apps");
        setResizable(false);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTree1.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree1.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTree1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jTree1);

        jTKurzInfo.setColumns(20);
        jTKurzInfo.setRows(5);
        jScrollPane2.setViewportView(jTKurzInfo);

        jButton2.setText("Dokumentation");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jButton2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE)))
                .addGap(21, 21, 21))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 653, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Infos und Doku", jPanel1);

        jTinfosystem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Arbeitsverzeichnis", "Name", "Lokale Datei"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTinfosystem);
        if (jTinfosystem.getColumnModel().getColumnCount() > 0) {
            jTinfosystem.getColumnModel().getColumn(0).setPreferredWidth(100);
            jTinfosystem.getColumnModel().getColumn(0).setMaxWidth(150);
            jTinfosystem.getColumnModel().getColumn(1).setPreferredWidth(100);
            jTinfosystem.getColumnModel().getColumn(1).setMaxWidth(250);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 669, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(522, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Infosystem", jPanel2);

        jTAufzaehlungen.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Such", "Name", "Nummer", "Installiert", "Lokale Datei"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(jTAufzaehlungen);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 639, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(528, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Aufzählungen", jPanel11);

        jTVartab.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Vartab", "DB Orig", "Gruppe Orig", "DB Neu", "Vartab frei", "Lokale Datei"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                boolean canornot=false;
                canornot=checkEditVartab(rowIndex,columnIndex);
                return canornot;

            }
        });
        jTVartab.getTableHeader().setReorderingAllowed(false);
        jTVartab.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTVartabFocusLost(evt);
            }
        });
        jScrollPane7.setViewportView(jTVartab);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 621, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(47, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(489, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Vartab", jPanel3);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 689, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 709, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Schlüssel", jPanel8);

        jTFOP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Verzeichnis", "FOP", "SPX", "Lokale Datei"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTFOP.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(jTFOP);
        if (jTFOP.getColumnModel().getColumnCount() > 0) {
            jTFOP.getColumnModel().getColumn(0).setPreferredWidth(80);
            jTFOP.getColumnModel().getColumn(0).setMaxWidth(150);
            jTFOP.getColumnModel().getColumn(1).setPreferredWidth(100);
            jTFOP.getColumnModel().getColumn(1).setMaxWidth(1000);
            jTFOP.getColumnModel().getColumn(2).setPreferredWidth(100);
            jTFOP.getColumnModel().getColumn(2).setMaxWidth(1000);
        }

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane8.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 669, Short.MAX_VALUE)
                    .addComponent(jScrollPane8))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(221, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("FOP", jPanel4);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 689, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 709, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Masken", jPanel5);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 689, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 709, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Aufrufparameter", jPanel6);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Verbindungsdaten"));

        jBConnectionTest.setText("Verbindung testen");
        jBConnectionTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBConnectionTestActionPerformed(evt);
            }
        });

        jLabel1.setText("abas Server");

        jLabel2.setText("Linuxuser");

        jLabel3.setText("Linuxpasswort");

        jLabel4.setText("Mandant");

        jLabel5.setText("Mandantenpasswort");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTHost, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jBConnectionTest))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTLinuxUser, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTMandant, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPMandant)
                    .addComponent(jPLinux, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(248, 248, 248))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jTLinuxUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jPLinux, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTMandant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPMandant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jBConnectionTest))
                .addContainerGap())
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Installation"));

        jBInstallInfosysteme.setText("Infosysteme");
        jBInstallInfosysteme.setEnabled(false);
        jBInstallInfosysteme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBInstallInfosystemeActionPerformed(evt);
            }
        });

        jTLog.setEditable(false);
        jTLog.setColumns(20);
        jTLog.setRows(5);
        jTLog.setName("Log"); // NOI18N
        jScrollPane6.setViewportView(jTLog);

        jButton1.setText("FOP");
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel6.setText("Log");

        jBInstallAufzaehlungen.setText("Aufzählungen");
        jBInstallAufzaehlungen.setEnabled(false);
        jBInstallAufzaehlungen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBInstallAufzaehlungenActionPerformed(evt);
            }
        });

        jBInstallVartab.setText("Vartab");
        jBInstallVartab.setEnabled(false);
        jBInstallVartab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBInstallVartabActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane6))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel6))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(jBInstallInfosysteme)
                                .addGap(38, 38, 38)
                                .addComponent(jBInstallAufzaehlungen)
                                .addGap(37, 37, 37)
                                .addComponent(jBInstallVartab)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1)))
                        .addGap(69, 69, 69)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBInstallInfosysteme)
                    .addComponent(jButton1)
                    .addComponent(jBInstallAufzaehlungen)
                    .addComponent(jBInstallVartab))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 294, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 669, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Installation", jPanel7);

        fileMenu.setMnemonic('f');
        fileMenu.setText("File");

        jMenuItem1.setText("Sync");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem1);

        exitMenuItem.setMnemonic('x');
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setMnemonic('h');
        helpMenu.setText("Help");

        aboutMenuItem.setMnemonic('a');
        aboutMenuItem.setText("About");
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPane1)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

   
    
    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try {
                     
            
            //File source, File destination, boolean smart
            FileSync.synchronize(GlobalVars.source, GlobalVars.target, true);
            // Verzeichnisstruktur der PAPPS einlesen und als Tree darstellen
            //File dir = new File("C:\\Users\\gdenz.ABAS-PROJEKT\\Documents\\PAPPS");
            File dir = new File(".\\PAPPS");
            rootnode = new DefaultMutableTreeNode("PAPPS");
            treeModel = new DefaultTreeModel(rootnode);
            jTree1.setModel(treeModel);
            DirList(dir,rootnode,true );
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

	
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jTree1ValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jTree1ValueChanged
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();
    //Alle Install Buttons deaktivieren
    jBInstallInfosysteme.setEnabled(false); 
            
            if (node == null) {
                //NOthing selected
                return;
            } else {
                Object nodeInfo = node.getUserObject();
                if (node.isLeaf()) {
                   
                        if (nodeInfo instanceof PappInfo) {
                            PappInfo pappinfo = (PappInfo) nodeInfo;
                            
                            //Kurzinfo einlesen
                            KurzInfoLesen(pappinfo.dir);
                            SystemLesen(pappinfo.dir,"Infosystem");
                            SystemLesen(pappinfo.dir,"Vartab");
                            SystemLesen(pappinfo.dir,"SPX");
                            SystemLesen(pappinfo.dir,"Aufzaehlungen");
                            
                        }
                    }
                }
    

    }//GEN-LAST:event_jTree1ValueChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //SPX kopieren

        //FOP kopieren
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jBInstallInfosystemeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBInstallInfosystemeActionPerformed
        String arb="";
        String name="";
        File file;
        StringBuilder fromServer=new StringBuilder();
        ByteArrayOutputStream error= new ByteArrayOutputStream();
        int sshexitstatus;
        //Infosysteme installieren
        for(int i=0;i<jTinfosystem.getRowCount();i++)
        {
            try {
                arb=jTinfosystem.getValueAt(i, 0).toString();
                name=jTinfosystem.getValueAt(i, 1).toString();
                file= new File (jTinfosystem.getValueAt(i, 2).toString());
                System.out.println(name);
                // Verbindung aufnehmen
                SshClient sshclient=new SshClient();
                //Infosystem kopieren
                jTLog.append("Infosystem "+file.getName()+" kopieren");
                jTLog.update(jTLog.getGraphics());
                sshclient.connect(jTLinuxUser.getText(), new String(jPLinux.getPassword()), jTHost.getText(), 22);
                sshexitstatus=sshclient.sendfile(file.toString(), file.getName());
                jTLog.append("                                         OK\n");
                jTLog.update(jTLog.getGraphics());
                //Infosystem auspacken
                jTLog.append("Infosystem "+file.getName()+" auspacken\n");
                jTLog.append("=======================================\n");
                jTLog.update(jTLog.getGraphics());
                sshexitstatus=sshclient.sendcommand("tar xzvf "+file.getName(), error, fromServer);
                if (sshexitstatus==0)
                {
                    jTLog.append(fromServer.toString());
                    jTLog.append("=======================================\n");
                    jTLog.update(jTLog.getGraphics());
                    //Infosystem installieren
                    jTLog.append("Infosystem "+file.getName()+" installieren\n");
                    jTLog.update(jTLog.getGraphics());
                    sshexitstatus=sshclient.sendcommand("eval `sh denv.sh`;sh loadinfosys.sh -p "+new String(jPMandant.getPassword())+" -a IMPORT -s "+name+" -w "+arb, error, fromServer);
                    // sshexitstatus=sshclient.sendcommand("ls", error, fromServer);
                    if (sshexitstatus==0)
                    {
                        jTLog.append(fromServer.toString());
                    }
                    else
                    {
                        jTLog.append(error.toString());
                    }
                }
                else
                {
                    jTLog.append(error.toString());
                }

                // Session beenden
                sshclient.sessiondisconnect();
            } catch (InterruptedException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SftpException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JSchException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jBInstallInfosystemeActionPerformed

    private void jBConnectionTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBConnectionTestActionPerformed
      
         String passwd = new String(jPMandant.getPassword());
        EDPSession session=SessionAufbauen(jTHost.getText(),6550,jTMandant.getText(),passwd);                                         
        //    EDPSession session=SessionAufbauen(jTHost.getText(),6550,jTMandant.getText(),Arrays.toString(jPMandant.getPassword()));                                         
            if (session !=null)
            {
               
            EDPQuery edpQ1 = session.createQuery();
            EDPEditor edpE1=session.createEditor();
             try {
                edpQ1.startQuery("12:10","","id");
               if(edpQ1.getNextRecord())
               {
                    edpE1.beginView(edpQ1.getField("id"));
                    // Wir sind im Betreibsdatensatz
                    String zdname="zdname"; 
                    String zdgrp="zdgrp"; 
                    String zdgn="zdgn"; 
                    // Und durchlaufen alle Zusatzdatenbanken
                    for (int i=1;i<=40;i++)
                    {    
                        Integer meini =new Integer(i);
                        if (i> 1) 
                            {
                             // Für fortlaufende Nummerierung bei größer 1   
                            zdname="zdname"+meini.toString();
                            zdgrp="zdgrp"+meini.toString();
                            zdgn="zdgn"+meini.toString();
                            }
                        if (!edpE1.getFieldVal(zdname).equals(""))
                        {// Dateiname ist da
                            ZDArrayDB[i-1]=edpE1.getFieldVal(zdname);
                            if (edpE1.getFieldVal(zdgrp).equals("ja"))
                            {//Gruppen sind da
                                int gruppennummer=0;
                                String grp=edpE1.getFieldVal(zdgn);
                                while (grp.indexOf(",")!=-1)
                                {//Gruppen extrahieren
                                    String gruppe=grp.substring(0,grp.indexOf(","));
                                    if (!gruppe.equals(""))
                                    {
                                        ZDArray[i-1][gruppennummer]=gruppe;
                                    }
                                    
                                grp=grp.substring(grp.indexOf(",")+1,grp.length());
                                gruppennummer++;  
                                }
                           // ZDArray[0][0]=edpE1.getFieldVal(zdname);         
                            }
                            else
                            {// Keine Gruppen da, aber dadurch alles gesperrt
                                for (int y=0;y<20;y++)
                                {
                                ZDArray[i-1][y]=edpE1.getFieldVal(zdname);       
                                }
                            }
                        }
                        else
                            {
                                ZDArrayDB[i-1]="";
                            for (int y=0;y<20;y++)
                                {
                                ZDArray[i-1][y]="";   
                                }
                            }
                    }
               }
               
            } catch (InvalidQueryException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (CantBeginEditException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (CantReadFieldPropertyException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
              JOptionPane.showMessageDialog (null, "EDP Verbindung und Mandantenzugriff war erfolgreich!", "EDP Verbindung erfolgreich",JOptionPane.INFORMATION_MESSAGE);
             
            }
            session.endSession();
                
    /*
        try {
            ByteArrayOutputStream error= new ByteArrayOutputStream();
            StringBuilder fromServer=new StringBuilder();
            int sshexitstatus=0;
            SshClient sshclient=new SshClient();
            sshclient.connect(jTLinuxUser.getText(), new String(jPLinux.getPassword()), jTHost.getText(), 22);
            sshexitstatus=sshclient.sendcommand("eval `sh denv.sh`;sh edpexport.sh -m "+jTMandant.getText()+" -p "+new String (jPMandant.getPassword())+" -a T",error,fromServer);
            //edpexport.sh -l 12:10 -f zdgn,zdgn2
           // sshexitstatus=sshclient.sendcommand("eval `sh denv.sh`;sh edpexport.sh -m "+jTMandant.getText()+" -p "+new String (jPMandant.getPassword())+" -l 12:10 -f zdgn,zdgn2,zdgn3,zdgn4,zdgn5,zdgn6,zdgn7,zdgn8,zdgn8,zdgn9,zdgn10",error,fromServer);

            if ((fromServer.indexOf("Kunde:Kunde")==0)&&(sshexitstatus==0))
            {
                //Verbindung erfoglreich
                JOptionPane.showMessageDialog (null, "Ssh Verbindung und Mandantenzugriff war erfolgreich!", "Ssh Verbindung erfolgreich",JOptionPane.INFORMATION_MESSAGE);
            
           
                
   // ZD's abholen
                sshexitstatus=sshclient.sendcommand("eval `sh denv.sh`;sh edpexport.sh -m "+jTMandant.getText()+" -p "+new String (jPMandant.getPassword())+" -l 12:10 -f zdname,zdname2,zdname3,zdname4,zdname5,zdname6,zdname7,zdname8,zdname9,zdname10,zdname11,,zdname12,zdname13,zdname14,zdname15,zdname16,zdname17,zdname18,zdname19,zdname20,zdname21,zdname22,zdname23,zdname24,zdname25,zdname26,zdname27,zdname28,zdname29,zdname30,zdname31,zdname32,zdname33,zdname34,zdname35,zdname36,zdname37,zdname38,zdname39,zdname40",error,fromServer);
                if (sshexitstatus==0)
                {
                int zdnummer=0;    
                String zdantwort=fromServer.toString();    
                while (zdantwort.indexOf(";")!=-1)
                        {   
                       // Noch ne ZD raus schneiden mit allen Gruppen
                       String zd =    zdantwort.substring(0,zdantwort.indexOf(";"));
                        //Die Gruppen rausschneiden
                       if (!zd.equals(""))
                       {
                       ZDArray[zdnummer][0]=zd;
                       ZDArray[zdnummer][1]=zd;
                       ZDArray[zdnummer][2]=zd;
                       ZDArray[zdnummer][3]=zd;
                       ZDArray[zdnummer][4]=zd;
                       ZDArray[zdnummer][5]=zd;
                       ZDArray[zdnummer][6]=zd;
                       ZDArray[zdnummer][7]=zd;
                       ZDArray[zdnummer][8]=zd;
                       ZDArray[zdnummer][9]=zd;
                       ZDArray[zdnummer][10]=zd;
                       ZDArray[zdnummer][11]=zd;
                       ZDArray[zdnummer][12]=zd;
                       ZDArray[zdnummer][13]=zd;
                       ZDArray[zdnummer][14]=zd;
                       ZDArray[zdnummer][15]=zd;
                       ZDArray[zdnummer][16]=zd;
                       ZDArray[zdnummer][17]=zd;
                       ZDArray[zdnummer][18]=zd;
                       ZDArray[zdnummer][19]=zd;
                       }
                       
                       // Und den Reststring kürzen um die erste ZD
                       zdantwort=zdantwort.substring(zdantwort.indexOf(";")+1,zdantwort.length());
                         zdnummer++;
                        }
                }
             
                
                // ZD's die Gruppen  abholen
                sshexitstatus=sshclient.sendcommand("eval `sh denv.sh`;sh edpexport.sh -m "+jTMandant.getText()+" -p "+new String (jPMandant.getPassword())+" -l 12:10 -f zdgn,zdgn2,zdgn3,zdgn4,zdgn5,zdgn6,zdgn7,zdgn8,zdgn9,zdgn10,zdgn11,,zdgn12,zdgn13,zdgn14,zdgn15,zdgn16,zdgn17,zdgn18,zdgn19,zdgn20,zdgn21,zdgn22,zdgn23,zdgn24,zdgn25,zdgn26,zdgn27,zdgn28,zdgn29,zdgn30,zdgn31,zdgn32,zdgn33,zdgn34,zdgn35,zdgn36,zdgn37,zdgn38,zdgn39,zdgn40",error,fromServer);
                if (sshexitstatus==0)
                {
                int zdnummer=0;    
                String zdantwort=fromServer.toString();    
                while (zdantwort.indexOf(";")!=-1)
                        {   
                       // Noch ne ZD raus schneiden mit allen Gruppen
                       String zd =    zdantwort.substring(0,zdantwort.indexOf(";"));
                        //Die Gruppen rausschneiden
                       int gruppennummer=0;
                       while (zd.indexOf(",")!=-1)
                            {
                              String gruppe=zd.substring(0,zd.indexOf(","));
                             // if (gruppe.equals(null)) gruppe="";
                              if (!gruppe.equals(""))
                              {
                              ZDArray[zdnummer][gruppennummer]=gruppe;
                              }
                              zd=zd.substring(zd.indexOf(",")+1,zd.length());
                               gruppennummer++;  
                            }
                       // Und den Reststring kürzen um die erste ZD
                       zdantwort=zdantwort.substring(zdantwort.indexOf(";")+1,zdantwort.length());
                         zdnummer++;
                        }
                }
            }
            else
            {
                JOptionPane.showMessageDialog (null, "Mandantenzugriff fehlerhaft!\n\n"+error, "Mandantenzugriff gescheitert", JOptionPane.ERROR_MESSAGE);
            }
            System.out.println(fromServer);
            System.out.println(error);
            sshclient.sessiondisconnect();
            // fromServer.close();
        } catch (InterruptedException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSchException ex) {
            JOptionPane.showMessageDialog (null, "Fehler bei Aufbau der Ssh Verbindung:\n"+ex, "Ssh Verbdindung gescheitert", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }//GEN-LAST:event_jBConnectionTestActionPerformed

    private void jBInstallAufzaehlungenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBInstallAufzaehlungenActionPerformed
        File file=null;
         StringBuilder fromServer=new StringBuilder();
        ByteArrayOutputStream error= new ByteArrayOutputStream();
        // Aufzählungen Installieren
        Aufzählungen aufzählungen=new Aufzählungen();
         for(int i=0;i<jTAufzaehlungen.getRowCount();i++)
         {
            try {
                file= new File (jTinfosystem.getValueAt(i, 4).toString());
                try {
                    aufzählungen.Install(jTLinuxUser.getText(), new String(jPLinux.getPassword()), jTHost.getText(),new String (jPMandant.getPassword()),file, error,fromServer);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (JSchException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SftpException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
             
         }
                 
    }//GEN-LAST:event_jBInstallAufzaehlungenActionPerformed

    private void jTVartabFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTVartabFocusLost
   boolean vartuready=true;  
   
   
        for (int i=0;i<jTVartab.getRowCount();i++)
                                                {
                                                 
                                                 if ((boolean)(jTVartab.getValueAt(i, 4))==false)
                                                 {
                                                     vartuready=false;
                                                 }
                                                
                                                }       
                                                    if (vartuready==true)
                                                 {
                                                    jBInstallVartab.setEnabled(true);
                                                 }
    }//GEN-LAST:event_jTVartabFocusLost

    private void jBInstallVartabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBInstallVartabActionPerformed
        //edpSession Aufbauen
        String passwd = new String(jPMandant.getPassword());
        EDPSession session=SessionAufbauen(jTHost.getText(),6550,jTMandant.getText(),passwd);                                         
        //    EDPSession session=SessionAufbauen(jTHost.getText(),6550,jTMandant.getText(),Arrays.toString(jPMandant.getPassword()));                                         
            if (session !=null)
            {
        //Install Routine in Klasse Vartab aufrufen
        //Klasse erzeugen
        Vartab vartab=new Vartab(); 
        vartab.install(jTVartab,session);        
            session.endSession();
            }
        
    }//GEN-LAST:event_jBInstallVartabActionPerformed
   
    public void SystemLesen(File dir, String system)
    {DefaultTableModel model=null;
    String arbdir;
    String name;
    String such;
    int dbint=0;
    int gruppeint=0;
    int dbneu=0;
    boolean vartabfrei;
  
        if (system.equals("Infosystem")) 
        {
            model =(DefaultTableModel) jTinfosystem.getModel();
            // Tabellenzeilen löschen
            model.setNumRows(0);
        }
         if (system.equals("Vartab")) 
        {
            model =(DefaultTableModel) jTVartab.getModel();
            // Tabellenzeilen löschen
            model.setNumRows(0);
        }
         if (system.equals("SPXSUB")) 
        {
            model =(DefaultTableModel) jTFOP.getModel();
            // Tabellenzeilen löschen
            model.setNumRows(0);
        }
        if (system.equals("Aufzaehlungen")) 
        {
            model =(DefaultTableModel) jTAufzaehlungen.getModel();
            // Tabellenzeilen löschen
            model.setNumRows(0);
        }
      if (!system.equals("SPXSUB")) 
      {
          dir=new File(dir.toString()+"\\"+system);
      }
        File[] files = dir.listFiles();
        if (files != null) { // Erforderliche Berechtigungen etc. sind vorhanden
		for (int i = 0; i < files.length; i++) {
                    System.out.print(files[i].getName());
                    if (files[i].isDirectory()) {
                        System.out.print(" (Ordner)\n");
                        if (system.contains("SPX"))
                        {
                            // In Ordner absteigen rekursiv
                            SystemLesen(files[i],"SPXSUB");
                        }
                    }
                    else {
                        if (system.equals("Infosystem"))
                        {
                           jBInstallInfosysteme.setEnabled(true); 
                           arbdir=files[i].getName();
                           arbdir=arbdir.substring(3,7);
                           name=files[i].getName();
                           name=name.substring(8,name.length()-4);
                           model.addRow(new Object[]{arbdir,name,files[i]});   
                        }
                        if (system.equals("Vartab"))
                        {
                          
                           arbdir=files[i].getParent();
                           arbdir=arbdir.substring(arbdir.lastIndexOf("\\")+1,arbdir.length());
                           name=files[i].getName();
                           String db=name.substring(2,name.lastIndexOf("-"));
                           dbint=parseInt(db);
                           String gruppe=name.substring(name.lastIndexOf("-")+1,name.lastIndexOf("."));
                           gruppeint=parseInt(gruppe);
                           vartabfrei=true;
                           
                           if ((dbint==15)||(dbint>=18&&dbint<=37)||(dbint>=41&&dbint<=51)||(dbint>=71&&dbint<=80))
                                   {
                                     //wir habene ine Zusatzdatenbank
                                       // Frei muss geprüft werden, deshalberst mal auf False setzen
                                       vartabfrei=false;  
                                       //CheckZD aufrufen
                                      String[] comboarray;
                                      //getneubelegteZD();
                                      comboarray=checkzd(dbint,gruppeint, jTVartab);
                                      if (comboarray==null ) 
                                      {
                                       //Rückgabearray nicht vorhanden, also keine probleme mit ZD
                                      dbneu=dbint;    
                                      vartabfrei=true;
                                      
                                      }
                                      else
                                      { 
                                     // JComboBox combo = new JComboBox(comboarray);
                                      //jTVartab.getColumn("DB Neu").setCellEditor(new DefaultCellEditor(combo));
                                      jTVartab.getColumn("DB Neu").setCellEditor((new MyCellEditor()));
                                   //   TableColumn mengenspalte=jTVartab.getColumnModel().getColumn("DB Neu");
                                   //   mengenspalte.setCellEditor(new MyCellEditor());
                                      }
                                   }
                           model.addRow(new Object[]{name,db,gruppe,dbneu,vartabfrei,files[i]});   
                        }    
                        if (system.equals("SPXSUB"))
                        {
                           arbdir=files[i].getParent();
                           arbdir=arbdir.substring(arbdir.lastIndexOf("\\")+1,arbdir.length());
                           name=files[i].getName();
                           model.addRow(new Object[]{arbdir,"",name,files[i]});   
                        }
                          if (system.equals("Aufzaehlungen"))
                        {
                            if (files[i].getName().contains(".csv"))
                            {
                                jBInstallAufzaehlungen.setEnabled(true); 
                                such=files[i].getName().substring(0,files[i].getName().indexOf("."));
                                name=files[i].getName().substring(files[i].getName().indexOf(".")+1,files[i].getName().lastIndexOf("."));
                                model.addRow(new Object[]{such,name,"",Boolean.FALSE,files[i]});   
                            }
                        }
                        System.out.print(" (Datei)\n");
                    }
		}
	}
    }

 
    public static String[] checkzd(int dbint,int gruppeint, JTable table)
    {
int dbtable=0;
//Array mit den in jTVartab belegten ZD aufbauen        
// Aber prüfen ob nicht schon in Table belegt
        for (int i=0;i<40;i++)
        {
            ZDArrayNeu[i][0]="";
             
        }
        for (int i=0;i<table.getRowCount();i++)
            {
                if (!table.getValueAt(i, 3).toString().equals("0"))
                    {
                       // Hier steht ein Wert drin,also wird hier eine db belegt
                       dbtable = parseInt(table.getValueAt(i,3).toString());
                       dbtable=Vartab.Vartab2ZD(dbtable);
                        /*if ((dbtable>=18)&&(dbtable<=26)) dbtable=dbtable-17;
                        if ((dbtable>=29)&&(dbtable<=37)) dbtable=dbtable-19;
                        if ((dbtable>=41)&&(dbtable<=51)) dbtable=dbtable-22;
                        if ((dbtable>=71)&&(dbtable<=80)) dbtable=dbtable-41;*/
                       ZDArrayNeu[dbtable][0]=table.getValueAt(i, 5).toString();
                    }
            }

//String[]comboarray;
        List<String> combo = new ArrayList<String>();
      /*  if (dbint==15) dbint=0;
        if ((dbint>=18)&&(dbint<=26)) dbint=dbint-17;
        if ((dbint>=29)&&(dbint<=37)) dbint=dbint-19;
        if ((dbint>=41)&&(dbint<=51)) dbint=dbint-22;
        if ((dbint>=71)&&(dbint<=80)) dbint=dbint-41;*/
        dbint=Vartab.Vartab2ZD(dbint);
        
        
        if (ZDArrayDB[dbint]==null||ZDArrayDB[dbint].equals(""))
        {
            return null; 
        }
        else 
        {
            // Schauen welche DB komplett frei ist
            //String[] comboarray= new String[5];
            for (int i=0;i<40;i++)
            {
                if (ZDArrayDB[i].equals(""))
                {
                    // Im Array ist die db Frei
                    // Aber nun noch schaune ob die evtl. in der jTVartab belegt wurde vom User
                    if (ZDArrayNeu[i][0].equals("")) 
                    {     
                        // Immer noch frei, also in die Combobox rein schieben
                    /*if (i==0) dbint=15;
                    if ((i>=1)&&(i<=9)) dbint=i+17;
                    if ((i>=10)&&(i<=18)) dbint=i+19;
                    if ((i>=19)&&(i<=29)) dbint=i+22;
                    if ((i>=30)&&(i<=39)) dbint=i+41;*/
                    dbint=Vartab.ZD2Vartab(i);
                        combo.add(Integer.toString(dbint));
                    }
                }
            }
            
             
        
            return combo.toArray(new String[combo.size()]);
        }
        
        
            
    }
    
    
    public void KurzInfoLesen(File dir)
    {
      FileInputStream fstream = null;
     try {
         fstream = new FileInputStream(dir.toString()+"\\Kurzinfo.txt");
         // Get the object of DataInputStream
         DataInputStream in = new DataInputStream(fstream);
         BufferedReader br = new BufferedReader(new InputStreamReader(in));
         String strLine = null;
         String newStrLine = null;
          try {
              //Read File Line By Line
              while ((strLine = br.readLine()) !=null)   {
                  
                  if (strLine.contains("iNCi")) {
                      newStrLine=strLine.replace("iNCi", "");
                  }
                  else {
                      newStrLine=strLine;
                  }
                  
                  String inhalt = jTKurzInfo.getText();
                  jTKurzInfo.setText(inhalt+newStrLine+"\r\n");
                  
              }    //Close the input stream
          } catch (IOException ex) {
              Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
          }
          try {
              in.close();
          } catch (IOException ex) {
              Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
          }
     } catch (FileNotFoundException ex) {
         Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
     } finally {
         try {
             fstream.close();
         } catch (IOException ex) {
             Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
         }
     }
  
    }
    
    public void DirList(File dir, DefaultMutableTreeNode node, boolean absteigen)
    {
        File sysdir;
            File[] files = dir.listFiles();
	if (files != null) { // Erforderliche Berechtigungen etc. sind vorhanden
		for (int i = 0; i < files.length; i++) {
                    System.out.print(files[i].getName());
                    if (files[i].isDirectory()) {
                        System.out.print(" (Ordner)\n");
                        //SystemNode =   new DefaultMutableTreeNode(files[i].getName());
                        SystemNode =   new DefaultMutableTreeNode(new PappInfo(files[i].getName(),files[i]));
                        node.add(SystemNode);
                        sysdir=files[i];
                        if (absteigen){DirList(sysdir,SystemNode, false);}
                        
                    }
                    else {
                        System.out.print(" (Datei)\n");
                    }
		}
	}
    }
    
    
     private EDPSession SessionAufbauen(String yserver, int yport, String ymandant, String ypasswort) 
    { 
      EDPSession  session = EDPFactory.createEDPSession ();
        try {
            session.beginSession(yserver, yport,ymandant, ypasswort, "JEDP_0001");
            } catch (CantBeginSessionException ex) 
                {
                    JOptionPane.showMessageDialog(this, "Verbindungsaufnahme gescheitert\n\n" + ex, "EDP Fehler", JOptionPane.ERROR_MESSAGE);
                //Verbindung nicht erfolgreich
               return null;
                }
      return session;
    }
     
     
    
    class PappInfo {

       
        private String name;
        private File dir;
        
        

        public  PappInfo(String name,
                File dir) 
        {
            this.name = name;
            this.dir = dir;
        }
        
        @Override
        public String toString() {
            String retValue = "";
            
                retValue = name;
                        return retValue;
            }
            
        
   }
   
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
    
     public boolean checkEditVartab(int row,int column)
    {
     int db =Integer.parseInt(jTVartab.getValueAt(row, 1).toString());
     if ((db==15)||(db>=18&&db<=37)||(db>=41&&db<=51)||(db>=71&&db<=80))
        {
            return true;
        }
     else 
     {      
     return false;   
     }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JButton jBConnectionTest;
    private javax.swing.JButton jBInstallAufzaehlungen;
    private javax.swing.JButton jBInstallInfosysteme;
    private javax.swing.JButton jBInstallVartab;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPasswordField jPLinux;
    private javax.swing.JPasswordField jPMandant;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTable jTAufzaehlungen;
    private javax.swing.JTable jTFOP;
    private javax.swing.JTextField jTHost;
    private javax.swing.JTextArea jTKurzInfo;
    private javax.swing.JTextField jTLinuxUser;
    private javax.swing.JTextArea jTLog;
    private javax.swing.JTextField jTMandant;
    private javax.swing.JTable jTVartab;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTinfosystem;
    private javax.swing.JTree jTree1;
    private javax.swing.JMenuBar menuBar;
    // End of variables declaration//GEN-END:variables

}
