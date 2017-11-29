package frameworks;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.jtattoo.plaf.aluminium.AluminiumLookAndFeel;
import de.abas.ceks.jedp.CantBeginEditException;
import de.abas.ceks.jedp.CantBeginSessionException;
import de.abas.ceks.jedp.CantReadFieldPropertyException;
import de.abas.ceks.jedp.CantSaveException;
import de.abas.ceks.jedp.EDPEditor;
import de.abas.ceks.jedp.EDPFactory;
import de.abas.ceks.jedp.EDPQuery;
import de.abas.ceks.jedp.EDPSession;
import de.abas.ceks.jedp.InvalidQueryException;
import java.awt.Component;
import java.awt.Cursor;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Integer.parseInt;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import static frameworks.GlobalVars.ZDArray;
import static frameworks.GlobalVars.ZDArrayDB;
import static frameworks.GlobalVars.ZDArrayNeu;
import java.awt.Desktop;
import java.awt.Frame;
import java.io.FileWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.synth.SynthLookAndFeel;

/**
 *
 * @author gdenz error stream jsch
 * http://www.programcreek.com/java-api-examples/index.php?api=com.jcraft.jsch.JSchException
 * ByteArrayOutputStream error=new ByteArrayOutputStream();
 * executor.setErrStream(error);
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
        try {
            System.setProperty("sun.java2d.dpiaware", "false");
            Properties props = new Properties();
            props.put("logoString", "GD");
            AluminiumLookAndFeel.setCurrentTheme(props);

            UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
            /* SYNTH
             SynthLookAndFeel laf = new SynthLookAndFeel();
             laf.load(MainFrame.class.getResourceAsStream("laf.xml"), MainFrame.class);
             UIManager.setLookAndFeel(laf);
             */
            URL myIconUrl = this.getClass().getResource("puzzle.png");
            this.setIconImage(new ImageIcon(myIconUrl, "Frameworks").getImage());
            initComponents();
            File f = new File("tmp");
            if (!f.exists()) {
                f.mkdir();
            }
            jLversion.setText("Version " + String.valueOf(GlobalVars.version));
            jLuser.setText(InetAddress.getLocalHost().getHostName());
            // Properties lesen
            MyProperties myproperties = new MyProperties();
            myproperties.ReadProps();

            rootnode = new DefaultMutableTreeNode("Frameworks");
            treeModel = new DefaultTreeModel(rootnode);
            jTree1.setModel(treeModel);
            DirList(GlobalVars.target, rootnode, true);
            jTLinuxUser.setText(GlobalVars.LinuxUser);
            jTHost.setText(GlobalVars.Host);
            jPLinux.setText(GlobalVars.LinuxPass);
            jTMandant.setText(GlobalVars.Mandant);
            jPMandant.setText(GlobalVars.Mandantpass);
            jTedpPort.setText(Integer.toString(GlobalVars.edpport));

            jTVartab.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static class MyCellEditor extends AbstractCellEditor implements TableCellEditor {
// static ?

        DefaultCellEditor checkbox = new DefaultCellEditor(new JComboBox(new Object[]{"abc"}));

        private DefaultCellEditor lastSelected;

        @Override
        public Object getCellEditorValue() {

            return lastSelected.getCellEditorValue();
        }

        @Override
        public Component getTableCellEditorComponent(final JTable table,
                final Object value, boolean isSelected, final int row, int column) {
            String db = table.getValueAt(row, 1).toString();
            String grp = table.getValueAt(row, 2).toString();
            int grpint = Integer.parseInt(grp);
            int dbint = Integer.parseInt(db);

            //String grp=table.getValueAt(row, 2).toString();
            String[] comboarray;
            comboarray = checkzd(dbint, grpint, table);
            if (comboarray != null) {
                //checkbox= new DefaultCellEditor(new JComboBox(new Object[] {comboarray}));

                checkbox = new DefaultCellEditor(new JComboBox(comboarray));
                checkbox.addCellEditorListener(new CellEditorListener() {

                    @Override
                    public void editingStopped(ChangeEvent e) {
                        //  System.out.println("Ende");                                         
                        //Den Frei Haken setzen in der Table
                        table.setValueAt(true, row, 4);
                        // die Dateivartab aus der TAbelle holen
                        String suchzd = table.getValueAt(row, 1).toString();

                        for (int i = 0; i < table.getRowCount(); i++) {// falls in der Tabelle der Wert nochmal vorkommt, den auf die selbe ZD setzen
                            if ((i != row) && (suchzd.equals(table.getValueAt(i, 1).toString()))) {
                                // Und nun den Wert eintragen den wir ausgewählt haben 
                                table.setValueAt(table.getCellEditor().getCellEditorValue(), i, 3);
                                table.setValueAt(true, i, 4);
                                //table.setValueAt("15", i, 3);
                            }
                        }
                        // FOPTXT Tablezeilen umsetzen
                        // Mainframe instanzieren
                        MainFrame gui = ReferenceHolderClass.getGUI();
                        JTable jTablefoptxt = gui.getjTFOPTXT();  // hier hast du die TextArea
                        JTable jTschluessel = gui.getjTschluessel();
                        JTable jTmasken = gui.getjTMasken();
                        //            .getjTFOPTXT();
                        Vartab vartab = new Vartab();

                        int altenummer = vartab.Vartab2ZD(Integer.parseInt(suchzd));
                        int neuenummer = vartab.Vartab2ZD(Integer.parseInt(table.getCellEditor().getCellEditorValue().toString()));

                        //FOPS instanzieren
                        FOPS fops = new FOPS();
                        // Tanscoder aufrufen
                        fops.foptxtTranscode(jTablefoptxt, altenummer, neuenummer);
                        //Schluessel instanzieren
                        Schluessel schluessel = new Schluessel();
                        //Transcoder aufrufen für 1. Stufe ( bei Shclüsseln ist es zweistufig, also später bei Import wird der rest transcodiert
                        schluessel.schluesseltranscode1(jTschluessel, suchzd, table.getCellEditor().getCellEditorValue().toString());
                        // Masken instanzieren
                        Masken masken = new Masken();
                        //Transcoder für Maskennumemr aufrufen

                        masken.maskentranscode(jTmasken, suchzd, table.getCellEditor().getCellEditorValue().toString());

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

        jPanel6 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTKurzInfo = new javax.swing.JTextArea();
        jBDoku = new javax.swing.JButton();
        jTFrameworkNo = new javax.swing.JTextField();
        jTFrameworkpreis = new javax.swing.JTextField();
        jBKostenpflicht = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTMaintainer = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTVersion = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTababas = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTbisabas = new javax.swing.JTextField();
        canvas1 = new java.awt.Canvas();
        canvas2 = new java.awt.Canvas();
        jBSinglesync = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTVartab = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTFOP = new javax.swing.JTable();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTFOPTXT = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTMasken = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTAufzaehlungen = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTinfosystem = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTschluessel = new javax.swing.JTable();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTObjekte = new javax.swing.JTable();
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
        jTedpPort = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jBInstallInfosysteme = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTLog = new javax.swing.JTextArea();
        jBinstallFOP = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jBInstallAufzaehlungen = new javax.swing.JButton();
        jBInstallVartab = new javax.swing.JButton();
        jBInstallSchluessel = new javax.swing.JButton();
        jBInstallMasken = new javax.swing.JButton();
        jBPhase1 = new javax.swing.JButton();
        jBPhase2 = new javax.swing.JButton();
        jBInstallAufrufparameter = new javax.swing.JButton();
        jTKunde = new javax.swing.JTextField();
        jTInfos = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jLuser = new javax.swing.JLabel();
        jLversion = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuEinstellungen = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Frameworks");
        setResizable(false);

        jTKurzInfo.setColumns(20);
        jTKurzInfo.setRows(5);
        jScrollPane2.setViewportView(jTKurzInfo);

        jBDoku.setText("Dokumentation");
        jBDoku.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBDokuActionPerformed(evt);
            }
        });

        jTFrameworkNo.setEditable(false);

        jTFrameworkpreis.setEditable(false);

        jBKostenpflicht.setText("Kostenpfichtig");
        jBKostenpflicht.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jBKostenpflicht.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        jLabel8.setText("Framework Nummer");

        jLabel9.setText("Preis");

        jTMaintainer.setEditable(false);

        jLabel10.setText("Maintainer");

        jLabel11.setText("Version");

        jTVersion.setEditable(false);

        jLabel12.setText("ab abas Version");

        jTababas.setEditable(false);

        jLabel13.setText("bis");

        jTbisabas.setEditable(false);

        jBSinglesync.setText("Sync des Frameworks");
        jBSinglesync.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBSinglesyncActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jBDoku)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 913, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTMaintainer, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jTFrameworkNo, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(58, 58, 58)
                                                .addComponent(jLabel11))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jTababas, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel13)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTVersion, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTbisabas, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 252, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jBKostenpflicht)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jBSinglesync)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel9)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jTFrameworkpreis, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(27, 27, 27)))))
                .addGap(19, 19, 19))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jBKostenpflicht)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jTFrameworkpreis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTFrameworkNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel11)
                            .addComponent(jTVersion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTababas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13)
                            .addComponent(jTbisabas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTMaintainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)
                            .addComponent(jBSinglesync))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 641, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jBDoku)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Infos und Doku", jPanel1);

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
        jTVartab.addVetoableChangeListener(new java.beans.VetoableChangeListener() {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {
                jTVartabVetoableChange(evt);
            }
        });
        jScrollPane7.setViewportView(jTVartab);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 906, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 795, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Vartab", jPanel3);

        jTFOP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Verzeichnis", "FOP", "SPX", "Datei", "Lokale Datei"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false
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

        jTFOPTXT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Maske", "Kommando", "Event", "Funktionstaste", "Feld", "K/T", "C/S", "FOP"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane8.setViewportView(jTFOPTXT);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 921, Short.MAX_VALUE)
                    .addComponent(jScrollPane8))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("FOP/SPX/Dateien", jPanel4);

        jTMasken.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Maske", "Neue Maske", "XML Datei", "Resources"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane10.setViewportView(jTMasken);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 909, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 787, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Masken", jPanel5);

        jTAufzaehlungen.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nummer", "Lokale Datei"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(jTAufzaehlungen);
        if (jTAufzaehlungen.getColumnModel().getColumnCount() > 0) {
            jTAufzaehlungen.getColumnModel().getColumn(0).setMinWidth(100);
            jTAufzaehlungen.getColumnModel().getColumn(0).setMaxWidth(200);
        }

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 906, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 792, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Aufzählungen", jPanel11);

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
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 921, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 795, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Infosystem", jPanel2);

        jTschluessel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "nummer", "such", "name", "DB:Gruppe", "Datei"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
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
        jScrollPane9.setViewportView(jTschluessel);
        if (jTschluessel.getColumnModel().getColumnCount() > 0) {
            jTschluessel.getColumnModel().getColumn(0).setResizable(false);
            jTschluessel.getColumnModel().getColumn(1).setResizable(false);
            jTschluessel.getColumnModel().getColumn(2).setResizable(false);
            jTschluessel.getColumnModel().getColumn(3).setResizable(false);
            jTschluessel.getColumnModel().getColumn(4).setResizable(false);
        }

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 921, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 795, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Schlüssel", jPanel8);

        jTObjekte.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Vartab", "Objekt", "Nummer", "Such", "Datei"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        jScrollPane11.setViewportView(jTObjekte);
        if (jTObjekte.getColumnModel().getColumnCount() > 0) {
            jTObjekte.getColumnModel().getColumn(0).setPreferredWidth(10);
            jTObjekte.getColumnModel().getColumn(2).setPreferredWidth(15);
            jTObjekte.getColumnModel().getColumn(3).setPreferredWidth(15);
        }

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 921, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 795, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Objekte", jPanel12);

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

        jTLinuxUser.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTLinuxUserFocusLost(evt);
            }
        });
        jTLinuxUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTLinuxUserActionPerformed(evt);
            }
        });

        jLabel4.setText("Mandant");

        jLabel5.setText("Mandantenpasswort");

        jLabel7.setText("Edp Port");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jBConnectionTest)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTHost, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(jTLinuxUser, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTMandant, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPMandant, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(51, 51, 51)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPLinux, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                            .addComponent(jTedpPort))
                        .addGap(248, 248, 248))))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTMandant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPMandant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jTedpPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jBConnectionTest)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Installation"));

        jBInstallInfosysteme.setText("Infosysteme");
        jBInstallInfosysteme.setEnabled(false);
        jBInstallInfosysteme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBInstallInfosystemeActionPerformed(evt);
            }
        });

        jScrollPane6.setAutoscrolls(true);

        jTLog.setEditable(false);
        jTLog.setColumns(20);
        jTLog.setRows(5);
        jTLog.setName("Log"); // NOI18N
        jScrollPane6.setViewportView(jTLog);

        jBinstallFOP.setText("FOP/SPX/Dateien");
        jBinstallFOP.setEnabled(false);
        jBinstallFOP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBinstallFOPActionPerformed(evt);
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

        jBInstallSchluessel.setText("Schlüssel");
        jBInstallSchluessel.setEnabled(false);
        jBInstallSchluessel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBInstallSchluesselActionPerformed(evt);
            }
        });

        jBInstallMasken.setText("Masken");
        jBInstallMasken.setEnabled(false);
        jBInstallMasken.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBInstallMaskenActionPerformed(evt);
            }
        });

        jBPhase1.setText("Phase 1 komplett starten");
        jBPhase1.setEnabled(false);
        jBPhase1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBPhase1ActionPerformed(evt);
            }
        });

        jBPhase2.setText("Phase 2 komplett starten");
        jBPhase2.setEnabled(false);
        jBPhase2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBPhase2ActionPerformed(evt);
            }
        });

        jBInstallAufrufparameter.setText("Objekte");
        jBInstallAufrufparameter.setEnabled(false);
        jBInstallAufrufparameter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBInstallObjekteActionPerformed(evt);
            }
        });

        jLabel14.setText("Kunde");

        jLabel15.setText("Info");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel10Layout.createSequentialGroup()
                                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(jPanel10Layout.createSequentialGroup()
                                                .addComponent(jBInstallAufzaehlungen)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jBInstallVartab, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jTKunde))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel15)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTInfos, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jBPhase2)
                                    .addComponent(jBPhase1)
                                    .addGroup(jPanel10Layout.createSequentialGroup()
                                        .addComponent(jBInstallSchluessel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jBInstallInfosysteme, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jBinstallFOP)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jBInstallMasken)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jBInstallAufrufparameter)))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTKunde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTInfos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBPhase1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBInstallAufzaehlungen)
                    .addComponent(jBInstallVartab))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBPhase2)
                .addGap(5, 5, 5)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBInstallSchluessel)
                    .addComponent(jBInstallInfosysteme)
                    .addComponent(jBinstallFOP)
                    .addComponent(jBInstallMasken)
                    .addComponent(jBInstallAufrufparameter))
                .addGap(2, 2, 2)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 921, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Installation", jPanel7);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTree1.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree1.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTree1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jTree1);

        jLuser.setText("user");

        jLversion.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLversion.setText("version");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jTabbedPane1))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLuser, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLversion, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addComponent(jTabbedPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLuser)
                    .addComponent(jLversion)))
        );

        menuBar.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        menuBar.setForeground(new java.awt.Color(51, 51, 51));

        fileMenu.setMnemonic('f');
        fileMenu.setText("Datei");

        jMenuItem1.setText("Sync");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem1);

        jMenuEinstellungen.setText("Einstellungen");
        jMenuEinstellungen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuEinstellungenActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuEinstellungen);

        exitMenuItem.setMnemonic('x');
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed

        Thread sync = new Thread() {
            public void run() {
                try {
                    MyLogger mylogger = new MyLogger();
                    mylogger.synclog();
                    Update update = new Update();
                    update.checkupdate();

                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    jTabbedPane1.setSelectedIndex(7);
                    //File source, File destination, boolean smart
                    jTLog.append("Frameworks werden abgeglichen\n");
                    jTLog.append("-------------------------------------\n");
                    jTLog.getGraphics();
                    FileSync.synchronize(GlobalVars.source, GlobalVars.target, true, jTLog);
            // Verzeichnisstruktur der PAPPS einlesen und als Tree darstellen
                    //File dir = new File("C:\\Users\\gdenz.ABAS-PROJEKT\\Documents\\PAPPS");
                    File dir = new File(".\\PAPPS");
                    dir = GlobalVars.target;
                    rootnode = new DefaultMutableTreeNode("Frameworks");
                    treeModel = new DefaultTreeModel(rootnode);
                    jTree1.setModel(treeModel);
                    DirList(dir, rootnode, true);
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                } catch (IOException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }

            }
        };
        sync.start();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jTree1ValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jTree1ValueChanged
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();
        //Alle Install Buttons deaktivieren
        jBInstallInfosysteme.setEnabled(false);
         jBPhase1.setEnabled(false);
        jBPhase2.setEnabled(false);
        jBInstallAufzaehlungen.setEnabled(false);
        jBInstallVartab.setEnabled(false);
        jBInstallSchluessel.setEnabled(false);
        
        jBinstallFOP.setEnabled(false);
        jBInstallMasken.setEnabled(false);
        jBInstallAufrufparameter.setEnabled(false);
        

        if (node == null) {
            //NOthing selected
            return;
        } else {
            Object nodeInfo = node.getUserObject();
            if (node.isLeaf()) {

                if (nodeInfo instanceof PappInfo) {
                    PappInfo pappinfo = (PappInfo) nodeInfo;
                    GlobalVars.appdir = pappinfo.dir.toString();
                    //Kurzinfo einlesen
                    KurzInfoLesen(pappinfo.dir);
                    Frameworklesen(pappinfo.dir);
                    // Fop Tabelle separat löschen da mehrfach verwendet
                    DefaultTableModel model = (DefaultTableModel) jTFOP.getModel();
                    // Tabellenzeilen löschen
                    model.setNumRows(0);
                    DefaultTableModel modelfoptxt = (DefaultTableModel) jTFOPTXT.getModel();
                    modelfoptxt.setNumRows(0);

                    SystemLesen(pappinfo.dir, "Infosysteme");
                    SystemLesen(pappinfo.dir, "Vartab");
                    SystemLesen(pappinfo.dir, "SPX");
                    SystemLesen(pappinfo.dir, "FOP");
                    SystemLesen(pappinfo.dir, "Aufzaehlung");
                    SystemLesen(pappinfo.dir, "Schluessel");
                    SystemLesen(pappinfo.dir, "Masken");
                    SystemLesen(pappinfo.dir, "Files");
                    SystemLesen(pappinfo.dir, "Objekte");
                    GlobalVars.dir = pappinfo.dir;
                    jBDoku.setEnabled(false);
                    File file = new File(pappinfo.dir + "\\Dokumentation\\");

                    if (file.isDirectory()) {

                        if (file.list().length > 0) {
                            jBDoku.setEnabled(true);
                        }
                    }
                }
            }
        }


    }//GEN-LAST:event_jTree1ValueChanged

    private void jMenuEinstellungenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuEinstellungenActionPerformed
        Einstellungen XE = new Einstellungen(this, true, GlobalVars.source.toString(), GlobalVars.updatepfad.toString());
        XE.setVisible(true);
        GlobalVars.source = new File(XE.getFrameworkSourcePath());
        GlobalVars.updatepfad = new String(XE.getUpdatepfad());
        MyProperties myproperties = new MyProperties();
        myproperties.WriteProps();


    }//GEN-LAST:event_jMenuEinstellungenActionPerformed

    private void jBInstallObjekteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBInstallObjekteActionPerformed
        
        if (checkInstall()) {
            Thread sync = new Thread() {
                public void run() {
                Buttonsfree(false);
                    File file = null;
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    String passwd = new String(jPMandant.getPassword());
                    EDPSession session = SessionAufbauen(GlobalVars.Host,GlobalVars.edpport, GlobalVars.Mandant,GlobalVars.Mandantpass);

                    // Aufzählungen Installieren
                    Objekte objekte = new Objekte();
                    boolean status = objekte.Install(  session,jTLog,jTObjekte);
                    if (status) {
                        jTLog.append("Objekte erfolgreich verarbeitet");
                    }
                    jTLog.append("--------------------------------\n");
                    jTLog.paint(jTLog.getGraphics());
                    session.endSession();
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                    MyLogger mylogger = new MyLogger();
                    mylogger.InstallLogWrite(jLuser.getText(), jTKunde.getText(), jTFrameworkNo.getText(), jTInfos.getText(), "Objekte");
                        //Alle Buttons freigeben
                    Buttonsfree(true);
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }

            };
            sync.start();
        }
    }//GEN-LAST:event_jBInstallObjekteActionPerformed

    private void jBPhase2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBPhase2ActionPerformed
        if (checkInstall()) {
            //schlüssel
            if (jBInstallSchluessel.isEnabled()) {
                jBInstallSchluesselActionPerformed(evt);
            }
            if (jBInstallInfosysteme.isEnabled()) {
                jBInstallInfosystemeActionPerformed(evt);
            }
            if (jBinstallFOP.isEnabled()) {
                jBinstallFOPActionPerformed(evt);
            }
            if (jBInstallMasken.isEnabled()) {
                jBInstallMaskenActionPerformed(evt);
            }
            //if (jBInstallAufrufparameter.isEnabled()){jBInstallAufrufparameterActionPerformed(evt);}

        }
    }//GEN-LAST:event_jBPhase2ActionPerformed

    private void jBPhase1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBPhase1ActionPerformed
        if (checkInstall()) {
            // Aufzählungen
            if (jBInstallAufzaehlungen.isEnabled()) {
                jBInstallAufzaehlungenActionPerformed(evt);
            }
            //Vartab
            if (jBInstallVartab.isEnabled()) {
                jBInstallVartabActionPerformed(evt);
            }

        }

    }//GEN-LAST:event_jBPhase1ActionPerformed

    private void jBInstallMaskenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBInstallMaskenActionPerformed
        if (checkInstall()) {
            Thread sync = new Thread() {
                public void run() {
        Buttonsfree(false);
                    File file = null;
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    String passwd = new String(jPMandant.getPassword());
                    EDPSession session = SessionAufbauen(GlobalVars.Host,GlobalVars.edpport, GlobalVars.Mandant,GlobalVars.Mandantpass);

                    // Aufzählungen Installieren
                    Masken masken = new Masken();
                    boolean status = masken.maskenInstall(jTMasken, jTLog, session, jTLinuxUser.getText(), new String(jPLinux.getPassword()), jTHost.getText());
                    if (status) {
                        jTLog.append("Masken erfolgreich verarbeitet");
                    }
                    jTLog.append("--------------------------------\n");
                    jTLog.paint(jTLog.getGraphics());
                    session.endSession();
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                    MyLogger mylogger = new MyLogger();
                    mylogger.InstallLogWrite(jLuser.getText(), jTKunde.getText(), jTFrameworkNo.getText(), jTInfos.getText(), "Masken");
                        //Alle Buttons freigeben
                    Buttonsfree(true);
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }

            };
            sync.start();
        }
    }//GEN-LAST:event_jBInstallMaskenActionPerformed

    private void jBInstallSchluesselActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBInstallSchluesselActionPerformed
        if (checkInstall()) {
            Thread sync = new Thread() {
                public void run() {
           Buttonsfree(false);
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    String passwd = new String(jPMandant.getPassword());
                    EDPSession session = SessionAufbauen(GlobalVars.Host,GlobalVars.edpport,GlobalVars.Mandant,GlobalVars.Mandantpass);
                    if (session != null) {

                        Schluessel schluessel = new Schluessel();
                        boolean status = schluessel.Schluesselinstall(jTschluessel, session, jTLog);

                        session.endSession();
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                        MyLogger mylogger = new MyLogger();
                        mylogger.InstallLogWrite(jLuser.getText(), jTKunde.getText(), jTFrameworkNo.getText(), jTInfos.getText(), "Schlüssel");
                    }
                    //Alle Buttons freigeben
                  Buttonsfree(true);
                }
                 
            };
            sync.start();
        }
    }//GEN-LAST:event_jBInstallSchluesselActionPerformed

    private void jBInstallVartabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBInstallVartabActionPerformed
        if (checkInstall()) {
            Thread sync = new Thread() {
                public void run() {
           Buttonsfree(false);
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    jTLog.append("=============================\n");
                    jTLog.append("Vartab erweitern");
                    //edpSession Aufbauen
                    String passwd = new String(jPMandant.getPassword());
                    EDPSession session = SessionAufbauen(GlobalVars.Host, GlobalVars.edpport,GlobalVars.Mandant,GlobalVars.Mandantpass);
                    //    EDPSession session=SessionAufbauen(jTHost.getText(),6550,jTMandant.getText(),Arrays.toString(jPMandant.getPassword()));
                    if (session != null) {
                        //Install Routine in Klasse Vartab aufrufen
                        //Klasse erzeugen
                        Vartab vartab = new Vartab();

                        vartab.install(jTVartab, session, jTFrameworkNo.getText(), jTLog);
                        session.endSession();
                     
                    }
                        //Alle Buttons freigeben
                   Buttonsfree(true);
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            };
            sync.start();
        }
    }//GEN-LAST:event_jBInstallVartabActionPerformed

    private void jBInstallAufzaehlungenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBInstallAufzaehlungenActionPerformed

        if (checkInstall()) {
            Thread sync = new Thread() {
                public void run() {
                    // Alle Knöpfe deaktivieren
                Buttonsfree(false);
                     //
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    File file = null;
                    String passwd = new String(jPMandant.getPassword());
                    EDPSession session = SessionAufbauen(GlobalVars.Host, GlobalVars.edpport, GlobalVars.Mandant,GlobalVars.Mandantpass);

                    // Aufzählungen Installieren
                    Aufzählungen aufzählungen = new Aufzählungen();
                    for (int i = 0; i < jTAufzaehlungen.getRowCount(); i++) {

                        file = new File(jTAufzaehlungen.getValueAt(i, 1).toString());
                        boolean status = aufzählungen.Install(session, file, jTLog);

                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                        MyLogger mylogger = new MyLogger();

                        mylogger.InstallLogWrite(jLuser.getText(), jTKunde.getText(), jTFrameworkNo.getText(), jTInfos.getText(), "Vartab");
                    }
                    session.endSession();
                    //Alle Buttons freigeben
                    Buttonsfree(true);

                }
            };
            sync.start();

        }
    }//GEN-LAST:event_jBInstallAufzaehlungenActionPerformed

    private void jBinstallFOPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBinstallFOPActionPerformed
        if (checkInstall()) {
            Thread sync = new Thread() {
                public void run() {

       Buttonsfree(false);
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    //SPX und FOP kopieren
                    jTLog.append("FOP's und Spx werden kopiert \n");
                    jTLog.append("=============================\n");

                    FOPS fops = new FOPS();

                    for (int i = 0; i < jTFOP.getRowCount(); i++) {

                        File file = new File(jTFOP.getValueAt(i, 4).toString());
                        jTLog.append(file.getName() + " kopieren \n");
                        jTLog.setCaretPosition(jTLog.getText().length());

                        String dir = (jTFOP.getValueAt(i, 0)).toString();
                        EDPSession session = SessionAufbauen(GlobalVars.Host, GlobalVars.edpport, GlobalVars.Mandant, GlobalVars.Mandantpass);
                        if (jTFOP.getValueAt(i, 3).toString().length() > 0) {
                            // Files werden installiert
                            boolean status = fops.install(jTLinuxUser.getText(), new String(jPLinux.getPassword()), jTHost.getText(), file, dir, session, jTLog, true);
                        } else {//Fops oder spx werden installiert
                            boolean status = fops.install(jTLinuxUser.getText(), new String(jPLinux.getPassword()), jTHost.getText(), file, dir, session, jTLog, false);
                        }

                        session.endSession();

                    }
                    // fop.txt verändern
                    jTLog.append("========FOP.TXT================\n");
                    jTLog.append("FOP.TXT wird erweitert\n");
                    boolean status = fops.foptxtinstall(jTFrameworkNo.getText(), jTLinuxUser.getText(), new String(jPLinux.getPassword()), jTHost.getText(), GlobalVars.dir, jTFOPTXT);
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    MyLogger mylogger = new MyLogger();
                    mylogger.InstallLogWrite(jLuser.getText(), jTKunde.getText(), jTFrameworkNo.getText(), jTInfos.getText(), "Fops");
                        //Alle Buttons freigeben
                    jTLog.append("Fertig\n");
                    jTLog.setCaretPosition(jTLog.getText().length());
                   Buttonsfree(true);
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            };
            sync.start();
        }
    }//GEN-LAST:event_jBinstallFOPActionPerformed

    private void jBInstallInfosystemeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBInstallInfosystemeActionPerformed
        if (checkInstall()) {
            Thread sync = new Thread() {
                public void run() {
        Buttonsfree(false);
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    Infosysteme infosysteme = new Infosysteme();
                    //    (JTable jTinfosystem, JTextArea jTLog,String LinuxUser, String LinuxPass, String Host, String MandantPass)
                    EDPSession session = SessionAufbauen(GlobalVars.Host, GlobalVars.edpport, GlobalVars.Mandant, GlobalVars.Mandantpass);

                    boolean status = infosysteme.install(jTinfosystem, jTLog, session);
                    session.endSession();
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    MyLogger mylogger = new MyLogger();
                    mylogger.InstallLogWrite(jLuser.getText(), jTKunde.getText(), jTFrameworkNo.getText(), jTInfos.getText(), "Infosysteme");
                        //Alle Buttons freigeben
                  Buttonsfree(true);
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            };
            sync.start();
        }
    }//GEN-LAST:event_jBInstallInfosystemeActionPerformed

    private void jBConnectionTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBConnectionTestActionPerformed

        String passwd = new String(jPMandant.getPassword());

        GlobalVars.Host = jTHost.getText();
        GlobalVars.LinuxUser = jTLinuxUser.getText();
        GlobalVars.LinuxPass = new String(jPLinux.getPassword());
        GlobalVars.Mandant = jTMandant.getText();
        GlobalVars.Mandantpass = new String(jPMandant.getPassword());
        GlobalVars.edpport = Integer.parseInt(jTedpPort.getText());

        // Verbindung wird neu getestet also deaktivieren wir alle Instaaller Buttons
        jBPhase1.setEnabled(false);
        jBPhase2.setEnabled(false);
        jBInstallAufzaehlungen.setEnabled(false);
        jBInstallVartab.setEnabled(false);
        jBInstallSchluessel.setEnabled(false);
        jBInstallInfosysteme.setEnabled(false);
        jBinstallFOP.setEnabled(false);
        jBInstallMasken.setEnabled(false);
        jBInstallAufrufparameter.setEnabled(false);
        //

        jTLog.append("----------Verbindungstest----------\n");
        jTLog.append("EDP Verbindung aufbauen");
        jTLog.paint(jTLog.getGraphics());
        EDPSession session = SessionAufbauen(GlobalVars.Host, GlobalVars.edpport, GlobalVars.Mandant, GlobalVars.Mandantpass);
        //    EDPSession session=SessionAufbauen(jTHost.getText(),6550,jTMandant.getText(),Arrays.toString(jPMandant.getPassword()));
        if (session != null) {
            jTLog.append("           OK\n");
            jTLog.paint(jTLog.getGraphics());
            // ZD Belegungen aus Betriebsdatensatz abholen
            EDPQuery edpQ1 = session.createQuery();
            EDPEditor edpE1 = session.createEditor();
            try {
                jTLog.append("Belegung der ZD abholen");
                jTLog.paint(jTLog.getGraphics());
                edpQ1.startQuery("12:10", "", "id");
                if (edpQ1.getNextRecord()) {
                    edpE1.beginView(edpQ1.getField("id"));
                    // Wir sind im Betreibsdatensatz
                    String zdname = "zdname";
                    String zdgrp = "zdgrp";
                    String zdgn = "zdgn";
                    // Und durchlaufen alle Zusatzdatenbanken
                    for (int i = 1; i <= GlobalVars.maxZDB; i++) {
                        Integer meini = new Integer(i);
                        if (i > 1) {
                            // Für fortlaufende Nummerierung bei größer 1
                            zdname = "zdname" + meini.toString();
                            zdgrp = "zdgrp" + meini.toString();
                            zdgn = "zdgn" + meini.toString();
                        }
                        if (!edpE1.getFieldVal(zdname).equals("")) {// Dateiname ist da
                            ZDArrayDB[i] = edpE1.getFieldVal(zdname);
                            if (edpE1.getFieldVal(zdgrp).equals("ja")) {//Gruppen sind da
                                int gruppennummer = 0;
                                String grp = edpE1.getFieldVal(zdgn);
                                while (grp.indexOf(",") != -1) {//Gruppen extrahieren
                                    String gruppe = grp.substring(0, grp.indexOf(","));
                                    if (!gruppe.equals("")) {
                                        ZDArray[i][gruppennummer] = gruppe;
                                    }

                                    grp = grp.substring(grp.indexOf(",") + 1, grp.length());
                                    gruppennummer++;
                                }
                                // ZDArray[0][0]=edpE1.getFieldVal(zdname);
                            } else {// Keine Gruppen da, aber dadurch alles gesperrt
                                for (int y = 0; y < 20; y++) {
                                    ZDArray[i][y] = edpE1.getFieldVal(zdname);
                                }
                            }
                        } else {
                            ZDArrayDB[i] = "";
                            for (int y = 0; y < 20; y++) {
                                ZDArray[i][y] = "";
                            }
                        }
                    }
                }
                // Und nun die Maskenliste abholen
                jTLog.append("             OK\nMaskenzuordnungen abholen");
                jTLog.paint(jTLog.getGraphics());
                edpQ1.startQuery("12:26", "", "such,vmnr1");
                while (edpQ1.getNextRecord()) {
                    if (!edpQ1.getField(1).equals("VVAR")) {
                        String db[] = edpQ1.getField(1).split("-");
                        int dbi = Integer.parseInt(db[1]);
                        if (dbi == 15 || dbi >= 18 && dbi <= 26 || dbi >= 29 && dbi <= 37 || dbi >= 41 && dbi <= 51 || dbi >= 71 && dbi <= 80) // Für Zusatzdatenbanken die Maskennummer in ein Array einlagern für später (fop.txt und Masken)
                        {
                            int zd = Vartab.Vartab2ZD(Integer.parseInt(db[1]));
                            int gruppe = (Integer.parseInt(db[2]));
                            GlobalVars.MaskArray[zd][gruppe] = edpQ1.getField(2).trim();

                        }
                    }
                }
                jTLog.append("     OK\n");
                jTLog.paint(jTLog.getGraphics());
                edpE1.endEditCancel();
                session.endSession();
                // und nun noch auf shell anmelden und edp ausführen
                jTLog.append("Shellzugriff testen ");
                jTLog.paint(jTLog.getGraphics());

                ByteArrayOutputStream error = new ByteArrayOutputStream();
                StringBuilder fromServer = new StringBuilder();
                int sshexitstatus = 0;
                SshClient sshclient = new SshClient();
                sshclient.connect(GlobalVars.LinuxUser, GlobalVars.LinuxPass, GlobalVars.Host, 22);
                sshexitstatus = sshclient.sendcommand("eval `sh denv.sh`;sh edpexport.sh -m " + jTMandant.getText() + " -p " + new String(jPMandant.getPassword()) + " -a T", error, fromServer);
                MyProperties myproperties = new MyProperties();
                myproperties.WriteProps();
                JOptionPane.showMessageDialog(this, "EDP Verbindung und Mandantenzugriff war erfolgreich!\n Bitte nun das gewünschte Framework anwählen !!!", "EDP Verbindung erfolgreich", JOptionPane.INFORMATION_MESSAGE);
                GlobalVars.VerbindungOK = true;
                jTLog.append("                          OK\n");
                jTLog.paint(jTLog.getGraphics());        
          
               // Alles leer machen, damit neu geladen wird
                
               
                
       
            } catch (InvalidQueryException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                jTLog.append("FEHLER !!! - " + ex.getMessage().toString());
                jTLog.paint(jTLog.getGraphics());

                GlobalVars.VerbindungOK = false;
            } catch (CantBeginEditException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                jTLog.append("FEHLER !!! - " + ex.getMessage().toString());
                jTLog.paint(jTLog.getGraphics());
                GlobalVars.VerbindungOK = false;

            } catch (CantReadFieldPropertyException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                jTLog.append("FEHLER !!! - " + ex.getMessage().toString());
                jTLog.paint(jTLog.getGraphics());
                GlobalVars.VerbindungOK = false;

            } catch (JSchException ex) {
                jTLog.append("FEHLER !!! - " + ex.getMessage().toString());
                jTLog.paint(jTLog.getGraphics());
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                GlobalVars.VerbindungOK = false;

            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                jTLog.append("FEHLER !!! - " + ex.getMessage().toString());
                jTLog.paint(jTLog.getGraphics());
                GlobalVars.VerbindungOK = false;

            } catch (InterruptedException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                jTLog.append("FEHLER !!! - " + ex.getMessage().toString());
                jTLog.paint(jTLog.getGraphics());
                GlobalVars.VerbindungOK = false;

            }

            if (!GlobalVars.VerbindungOK) {
                JOptionPane.showMessageDialog(this, "EDP Verbindung und Mandantenzugriff war nicht erfolgreich!\n", "Verbindung fehlerhaft", JOptionPane.ERROR_MESSAGE);

            }
        }

        // session.endSession();

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

    private void jTVartabVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_jTVartabVetoableChange
        // TODO add your handling code here:

        // Wir geben die Datei Vartab rein und bekommen die Dateinummer raus
        // Vartab vartab=new Vartab();
        //int dateinummer=vartab.Vartabtranscode(Integer.parseInt(suchzd), table, true);
    }//GEN-LAST:event_jTVartabVetoableChange

    private void jTVartabFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTVartabFocusLost
        boolean vartuready = true;

        for (int i = 0; i < jTVartab.getRowCount(); i++) {

            if ((boolean) (jTVartab.getValueAt(i, 4)) == false) {
                vartuready = false;
            }

        }
        if (vartuready == true) {

            jBInstallVartab.setEnabled(true);
            jBPhase1.setEnabled(true);
        }

    }//GEN-LAST:event_jTVartabFocusLost

    private void jBDokuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBDokuActionPerformed
        try {
            // TODO add your handling code here:
            Runtime.getRuntime().exec("explorer.exe " + GlobalVars.dir + "\\Dokumentation\\");
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jBDokuActionPerformed

    private void jBSinglesyncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBSinglesyncActionPerformed

        Thread sync = new Thread() {
            public void run() {

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();

                if (node == null) {
                    //NOthing selected
                    return;
                } else {
                    Object nodeInfo = node.getUserObject();
                    if (node.isLeaf()) {

                        if (nodeInfo instanceof PappInfo) {
                            try {
                                PappInfo pappinfo = (PappInfo) nodeInfo;
                                String fwDir = pappinfo.dir.toString();
                                System.out.println("Verzeichnis für Single Sync" +fwDir);
                                
                                // MyLogger mylogger = new MyLogger();
                                // mylogger.synclog();
                                // Auf Update prüfen
                                Update update = new Update();
                                update.checkupdate();
                                // Cursor auf Sanduhr
                                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                                jTabbedPane1.setSelectedIndex(7);
                                
                                
                                jTLog.append("Framework "+ jTFrameworkNo.getText()+" wird abgeglichen\n");
                                jTLog.append("-------------------------------------\n");
                                jTLog.getGraphics();
                                // wir müssen erst mal die Pfade umbauen
                                fwDir=fwDir.substring(fwDir.indexOf("Frameworks")+("Frameworks").length(),fwDir.length());
                                File sourceDir=new File (GlobalVars.source.toString() + fwDir);
                                File targetDir=new File (GlobalVars.target.toString() +fwDir);
                                FileSync.synchronize(sourceDir, targetDir, true, jTLog);
                                  setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                            } catch (IOException ex) {
                                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                        }

                    }
                }

            }
        };
        sync.start();


    }//GEN-LAST:event_jBSinglesyncActionPerformed

    private void jTLinuxUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTLinuxUserActionPerformed
       
    }//GEN-LAST:event_jTLinuxUserActionPerformed

    private void jTLinuxUserFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTLinuxUserFocusLost
       
        if (jTLinuxUser.getText().equals("root"))
       {
           JOptionPane.showMessageDialog(this, "root ist nicht der richtige User, bitte den Mandantenuser wie z.b. demo, erp, entw eintragen", "Falscher Linuxuser", JOptionPane.ERROR_MESSAGE);
           jTLinuxUser.setText("");
       }
    }//GEN-LAST:event_jTLinuxUserFocusLost

    public void SystemLesen(File dir, String system) {
        DefaultTableModel model = null;
        String arbdir;
        String arbdirMain;
        String name;
        String such;
        int dbint = 0;
        int gruppeint = 0;
        int dbneu = 0;
        boolean vartabfrei;
        System.out.println(system);
        if (system.equals("Objekte")) {
            model = (DefaultTableModel) jTObjekte.getModel();
            // Tabellenzeilen löschen
            model.setNumRows(0);
        }
        if (system.equals("Schluessel")) {
            model = (DefaultTableModel) jTschluessel.getModel();
            // Tabellenzeilen löschen
            model.setNumRows(0);
        }
        if (system.equals("Infosysteme")) {
            model = (DefaultTableModel) jTinfosystem.getModel();
            // Tabellenzeilen löschen
            model.setNumRows(0);
        }
        if (system.equals("Vartab")) {
            model = (DefaultTableModel) jTVartab.getModel();
            // Tabellenzeilen löschen
            model.setNumRows(0);
        }
        if (system.equals("SPXSUB")) {
            model = (DefaultTableModel) jTFOP.getModel();
            // Tabellenzeilen löschen
            model.setNumRows(0);
        }
        if (system.equals("FOPSUB")) {
            model = (DefaultTableModel) jTFOP.getModel();
            // Tabellenzeilen löschen
            //model.setNumRows(0);
        }
        if (system.equals("FilesSUB") || system.equals("Files")) {
            model = (DefaultTableModel) jTFOP.getModel();
            // Tabellenzeilen löschen
            //model.setNumRows(0);
        }
        if (system.equals("Aufzaehlung")) {
            model = (DefaultTableModel) jTAufzaehlungen.getModel();
            // Tabellenzeilen löschen
            model.setNumRows(0);
        }
        if (system.equals("Masken")) {
            model = (DefaultTableModel) jTMasken.getModel();
            // Tabellenzeilen löschen
            model.setNumRows(0);
        }
        if (!system.equals("SPXSUB") && !system.equals("FOPSUB") && !system.equals("FilesSUB")) {
            dir = new File(dir.toString() + "\\" + system);
        }
        File[] files = dir.listFiles();
        if (files != null) { // Erforderliche Berechtigungen etc. sind vorhanden
            for (int i = 0; i < files.length; i++) {
                System.out.print(files[i].getName());
                if (files[i].isDirectory()) {
                    System.out.print(" (Ordner)\n");
                  
                    if (system.contains("SPX")) {
                        // In Ordner absteigen rekursiv
                        SystemLesen(files[i], "SPXSUB");
                    }
                    if (system.contains("FOP")) {

                        // In Ordner absteigen rekursiv
                        SystemLesen(files[i], "FOPSUB");
                        jBinstallFOP.setEnabled(true);
                        jBPhase2.setEnabled(true);
                    }
                    if (system.contains("Files")) {

                        // In Ordner absteigen rekursiv
                        SystemLesen(files[i], "FilesSUB");
                        jBinstallFOP.setEnabled(true);
                        jBPhase2.setEnabled(true);
                    }

                } else {
                      if (system.equals("Objekte")) {
                     
                       name=files[i].getName();
                       String vartab=name.substring(0,name.indexOf("."));
                          String objekt = name.substring(name.indexOf(".")+1,name.length()-4);
                           FileReader fr;
                           try {
                               fr = new FileReader(files[i]);
                                BufferedReader br = new BufferedReader(fr);
                                //Header einlesen
                                String zeile = br.readLine();
                                //
                                zeile = br.readLine();
                                 String[] zeilelist = zeile.split("#", 8);
                                 model.addRow(new Object[]{vartab, objekt,zeilelist[0],zeilelist[1], files[i]});
                           } catch (FileNotFoundException ex) {
                              Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                          } catch (IOException ex) {
                              Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                          }
                      
                       
                    }
                    if (system.equals("Infosysteme")) {
                        jBInstallInfosysteme.setEnabled(true);
                        jBPhase2.setEnabled(true);
                        arbdir = files[i].getName();

                        //arbdir = arbdir.substring(3, 7);
                        arbdir = arbdir.substring(3, arbdir.length());
                        arbdir = arbdir.substring(0, arbdir.indexOf("."));
                        name = files[i].getName();
                        name = name.substring(3, name.length());
                        name = name.substring(name.indexOf(".") + 1, name.length() - 4);
                        
                        model.addRow(new Object[]{arbdir, name, files[i]});
                    }
                    if (system.equals("Vartab")) {

                        arbdirMain = files[i].getParent();
                        arbdir = arbdirMain.substring(arbdirMain.lastIndexOf("\\") + 1, arbdirMain.length());
                        name = files[i].getName();
                        if (!name.equals(GlobalVars.Betriebsdatensatztxtfile) && (!name.equals(GlobalVars.DBKonfigtxtfile))) {
                            String db = name.substring(2, name.lastIndexOf("-"));
                            dbint = parseInt(db);
                            String gruppe = name.substring(name.lastIndexOf("-") + 1, name.lastIndexOf("."));
                            gruppeint = parseInt(gruppe);
                            vartabfrei = true;

                            if ((dbint == 15) || (dbint >= 18 && dbint <= 26) || (dbint>=29 &&dbint <=37)||(dbint >= 41 && dbint <= 51) || (dbint >= 71 && dbint <= 80)) {
                                //wir habene ine Zusatzdatenbank
                                // Ob diese frei ist, muss geprüft werden, deshalb erst mal auf False setzen
                                vartabfrei = false;
                                //CheckZD aufrufen
                                String[] comboarray;
                                //getneubelegteZD();
                                comboarray = checkzd(dbint, gruppeint, jTVartab);
                                if (comboarray == null) {
                                    //Rückgabearray nicht vorhanden, also keine probleme mit ZD
                                    dbneu = dbint;
                                    vartabfrei = true;

                                } else {
                                    // EIn Eintrag oder mehrere ?
                                    if (comboarray.length == 1) {
                                        // wenn nur Eintrag im Array, dann ist die ZD fix, also rein schreiben
                                        dbneu = Integer.parseInt(comboarray[0]);
                                        //    dbneu=comboarray[0];                                  
                                    } else {

                                        jTVartab.getColumn("DB Neu").setCellEditor((new MyCellEditor()));

                                    }
                                }
                            }
                            model.addRow(new Object[]{name, db, gruppe, dbneu, vartabfrei, files[i]});
                            // jBInstallVartab.setEnabled(true);
                            // jBPhase1.setEnabled(true);
                        }
                    }
                    if (system.equals("FOP")) {
                        name = files[i].getName();
                        if (name.equals(GlobalVars.foptxtfile)) {
                            // Fop Model erst mal retten
                            DefaultTableModel modelfoptxt = (DefaultTableModel) jTFOPTXT.getModel();
                            modelfoptxt.setNumRows(0);
                            //Fop.txt einlesen 
                            FileReader fr;
                            try {
                                fr = new FileReader(files[i]);
                                BufferedReader br = new BufferedReader(fr);
                                //Header einlesen
                                String zeile = br.readLine();
                                //
                                while ((zeile = br.readLine()) != null) {
                                    if (!zeile.startsWith("#")) {
                                        if (zeile.contains("[C]") || zeile.contains("[S]")) {
                                            //Zeile splitten mit CS
                                            String[] zeilelist = zeile.split("\\s+", 8);
                                            
                                            zeilelist[1] = new String(zeilelist[1].getBytes("windows-1252"), "UTF-8");
                                        // //   zeilelist[1] = zeilelist[1].replaceAll("\\?", "ä");
                                           // Ende
                                                  //  String resultString = subjectString.replaceAll("[^\\x00-\\x7F]", "");
                                            modelfoptxt.addRow(new Object[]{zeilelist[0], zeilelist[1], zeilelist[2], zeilelist[3], zeilelist[4], zeilelist[5], zeilelist[6], zeilelist[7]});
                                            char c = zeilelist[1].charAt(0); // c='a'
                                            int ascii = (int)c;
                                            
                                            System.out.println(c + " - "+ ascii);
                                        } else {
                                            //Zeile splitten ohne CS
                                            String[] zeilelist = zeile.split("\\s", 7);
                                            modelfoptxt.addRow(new Object[]{zeilelist[0], zeilelist[1], zeilelist[2], zeilelist[3], zeilelist[4], zeilelist[5], "", zeilelist[6]});
                                        }
                                    }
                                }
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        } else { // Andere Files im Mandantenverzeichnis
                            model = (DefaultTableModel) jTFOP.getModel();
                            name = files[i].getName();
                            arbdir = "";
                            arbdir = "";
                            //arbdir.substring(arbdir.lastIndexOf("\\") + 1, arbdir.length());
                            name = files[i].getName();
                            model.addRow(new Object[]{arbdir, name, "", "", files[i]});
                            jBinstallFOP.setEnabled(true);
                            jBPhase2.setEnabled(true);
                        }
                    }
                    if (system.equals("SPXSUB")) {
                        arbdir = files[i].getParent();
                        arbdir = arbdir.substring(arbdir.lastIndexOf("\\") + 1, arbdir.length());
                        name = files[i].getName();
                        model.addRow(new Object[]{arbdir, "", name, "", files[i]});
                        jBinstallFOP.setEnabled(true);
                        jBPhase2.setEnabled(true);
                    }
                    if (system.equals("FOPSUB")) {
                        name = files[i].getName();

                        arbdir = files[i].getParent();
                        arbdir = arbdir.substring(arbdir.lastIndexOf("\\") + 1, arbdir.length());
                        name = files[i].getName();
                        model.addRow(new Object[]{arbdir, name, "", "", files[i]});
                        jBinstallFOP.setEnabled(true);
                        jBPhase2.setEnabled(true);

                    }
                    if (system.equals("FilesSUB") || system.equals("Files")) {
                        name = files[i].getName();

                        arbdir = files[i].getParent();
                        if (arbdir.length() > arbdir.lastIndexOf("Files") + 6) {
                            arbdir = arbdir.substring(arbdir.lastIndexOf("Files") + 6, arbdir.length());
                        } else {
                            arbdir = "";
                        }
                        //falls Homedir auftaucht, so muss dies $HOMEDIR sein, damit es in den s3 kopiert wird.
                        arbdir = arbdir.replaceAll("HOMEDIR", "\\$HOMEDIR");
                        arbdir = arbdir.replaceAll("\\\\", "/");
                        name = files[i].getName();
                        model.addRow(new Object[]{arbdir, "", "", name, files[i]});
                        jBinstallFOP.setEnabled(true);
                        jBPhase2.setEnabled(true);

                    }
                    if (system.equals("Aufzaehlung")) {
                        jBInstallAufzaehlungen.setEnabled(true);
                        jBPhase1.setEnabled(true);
                        such = files[i].getName().substring(0, files[i].getName().indexOf("."));
                        //name=files[i].getName().substring(files[i].getName().indexOf(".")+1,files[i].getName().lastIndexOf("."));
                        model.addRow(new Object[]{such, files[i]});

                    }
                    if (system.equals("Masken")) {

                        if (files[i].getName().endsWith(".xml")) {
                            String xml = files[i].getName();
                            String maske = xml.substring(xml.indexOf(".") + 1, xml.length());
                            maske = maske.substring(0, maske.indexOf("."));
                            String resource = xml.substring(0, xml.indexOf("-")) + "-Resources.language";
                            model.addRow(new Object[]{maske, maske, xml, resource});
                        }
                        if (files[i].getName().endsWith(".tgz")) {
                            String xml = files[i].getName();
                            String maske = xml.substring(xml.indexOf(".") + 1, xml.length());
                            maske = maske.substring(0, maske.indexOf("."));
                            model.addRow(new Object[]{maske, maske, xml, "TGZ"});
                        }
                        jBInstallMasken.setEnabled(true);
                        jBPhase2.setEnabled(true);
                    }

                    if (system.equals("Schluessel")) {
                        try {
                            FileReader fr;
                            fr = new FileReader(files[i]);
                            BufferedReader br = new BufferedReader(fr);
                            //Header einlesen
                            String zeile = br.readLine();
                            while ((zeile = br.readLine()) != null) {
                                // if (zeile.startsWith("*FW")) {
                                if (zeile.contains("FW")) {
                                    String[] zeilelist = zeile.split("#", 8);
                                    model.addRow(new Object[]{zeilelist[0], zeilelist[1], zeilelist[2], zeilelist[4], files[i].toString()});
                                }
                            }
                            fr.close();
                            jBInstallSchluessel.setEnabled(true);
                            jBPhase2.setEnabled(true);
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    System.out.print(" (Datei)\n");

                }
            }
        }
        // Zum Schluss müssen wir nun noch schauen ob wir FOPS Schlüssel und Masken transcodieren müssen, falls die ZD schon im Betreibsdatensatz gesetzt wurde
        // Dazu laufen wir nun erst mal die Vartab Table durch und schauen welche Infos vorhanden sind
        // int altenummer=7;
        // int neuenummer=6;

        for (int i = 0; i < jTVartab.getRowCount(); i++) {
            int altenummer = Integer.parseInt(jTVartab.getValueAt(i, 1).toString());
            int neuenummer = Integer.parseInt(jTVartab.getValueAt(i, 3).toString());
            if ((altenummer != neuenummer) && (neuenummer != 0)) {

                //Schluessel instanzieren
                Schluessel schluessel = new Schluessel();
                //Transcoder aufrufen für 1. Stufe ( bei Shclüsseln ist es zweistufig, also später bei Import wird der rest transcodiert
                schluessel.schluesseltranscode1(jTschluessel, String.valueOf(altenummer), String.valueOf(neuenummer));
                // Masken instanzieren
                Masken masken = new Masken();
                        //Transcoder für Maskennumemr aufrufen

                masken.maskentranscode(jTMasken, String.valueOf(altenummer), String.valueOf(neuenummer));
                Vartab vartab = new Vartab();
                // Leider ist die Vartabnummer nicht passend zum FOP transocoder, wir bruachen hier die Datei
                altenummer = vartab.Vartab2ZD(altenummer);
                neuenummer = vartab.Vartab2ZD(neuenummer);
                FOPS fops = new FOPS();
                // Tanscoder aufrufen
                fops.foptxtTranscode(jTFOPTXT, altenummer, neuenummer);
            }
        }

    }

    public static String[] checkzd(int dbint, int gruppeint, JTable table) {
        int dbtable = 0;
        String name = "";
        boolean zdgefunden = false;

//Array mit den in jTVartab belegten ZD aufbauen        
// Aber prüfen ob nicht schon in Table belegt
        for (int i = 1; i <= GlobalVars.maxZDB; i++) {
            ZDArrayNeu[i][0] = "";

        }
        for (int i = 0; i < table.getRowCount(); i++) {
            if (!table.getValueAt(i, 3).toString().equals("0")) {
                // Hier steht ein Wert drin,also wird hier eine db belegt
                dbtable = parseInt(table.getValueAt(i, 3).toString());
                dbtable = Vartab.Vartab2ZD(dbtable);
                /*if ((dbtable>=18)&&(dbtable<=26)) dbtable=dbtable-17;
                 if ((dbtable>=29)&&(dbtable<=37)) dbtable=dbtable-19;
                 if ((dbtable>=41)&&(dbtable<=51)) dbtable=dbtable-22;
                 if ((dbtable>=71)&&(dbtable<=80)) dbtable=dbtable-41;*/
                ZDArrayNeu[dbtable][0] = table.getValueAt(i, 5).toString();
            }
        }

//String[]comboarray;
        List<String> combo = new ArrayList<String>();
        /*  if (dbint==15) dbint=0;
         if ((dbint>=18)&&(dbint<=26)) dbint=dbint-17;
         if ((dbint>=29)&&(dbint<=37)) dbint=dbint-19;
         if ((dbint>=41)&&(dbint<=51)) dbint=dbint-22;
         if ((dbint>=71)&&(dbint<=80)) dbint=dbint-41;*/
        dbint = Vartab.Vartab2ZD(dbint);
        // In alte Nummer steht die ZD drin also zum Beispiel die 6 für die Datei6
        int altenummer = dbint;
        if (ZDArrayDB[dbint] == null || ZDArrayDB[dbint].equals("")) {
            return null;
        } else {

            FileReader fr = null;
            name = "";
            try {
                // Name der ZD abholen die wir setzen müssen, einlesen der Betriebsdaten
                String betriebsdaten = GlobalVars.appdir + "\\Vartab\\" + GlobalVars.Betriebsdatensatztxtfile;
                fr = new FileReader(betriebsdaten);
                BufferedReader br = new BufferedReader(fr);
                String zeile = "";
                // Header Zeile
                zeile = br.readLine();
                // Normale Zeile
                while ((zeile = br.readLine()) != null) {
                    //zeile = br.readLine();
                    String[] zeilelist = zeile.split("#", 6);
                    if (String.valueOf(dbint).equals(zeilelist[0])) {
                        name = zeilelist[1];
                        System.out.println("ZD " + name + "wird gesucht");
                    }
                }
                br.close();

                // Schauen ob die DB schon gesetzt wurde zum Framework
                for (int i = 1; i <= GlobalVars.maxZDB; i++) {
                    System.out.println(ZDArrayDB[i].toString());
                    if (ZDArrayDB[i].equals(name)) {

                        // ZD mit passendem Namen gefunden
                        // In neuenummer steht die Datei drin also zum Beispiel 7 für die Datei7
                        int neuenummer = i;

                        dbint = Vartab.ZD2Vartab(i);
                        System.out.println("ZD " + dbint + "gefunden");

                        combo.add(Integer.toString(dbint));
                        zdgefunden = true;
                        //
                        /*
                         MainFrame gui = ReferenceHolderClass.getGUI();
                         JTable jTablefoptxt = gui.getjTFOPTXT();  
                         JTable jTschluessel = gui.getjTschluessel();
                         JTable jTmasken=gui.getjTMasken();
                         Vartab vartab = new Vartab();
                        
                         //int neuenummer = vartab.Vartab2ZD(Integer.parseInt(table.getCellEditor().getCellEditorValue().toString()));

                        
                         // SChlüssel und fops und Masken transcodieren
                         FOPS fops = new FOPS();
                         // Tanscoder aufrufen
                         fops.foptxtTranscode(jTablefoptxt, altenummer, neuenummer);
                         //Schluessel instanzieren
                         Schluessel schluessel = new Schluessel();
                         //Transcoder aufrufen für 1. Stufe ( bei Shclüsseln ist es zweistufig, also später bei Import wird der rest transcodiert
                         //schluessel.schluesseltranscode1(jTschluessel, Integer.toString(dbint), table.getCellEditor().getCellEditorValue().toString());
                         // Masken instanzieren
                         Masken masken = new Masken();
                         //Transcoder für Maskennumemr aufrufen
                        
                         //masken.maskentranscode(jTmasken, Integer.toString(dbint),table.getCellEditor().getCellEditorValue().toString());
                         */
                    }
                }
                // Schauen welche DB komplett frei ist
                //String[] comboarray= new String[5];
                if (!zdgefunden) {// Zd mit passendenm Namen wurde noch nicht gefunden
                    for (int i = 1; i <= GlobalVars.maxZDB; i++) {
                        if (ZDArrayDB[i].equals("")) {
                            // Im Array ist die db Frei
                            // Aber nun noch schaune ob die evtl. in der jTVartab belegt wurde vom User
                            if (ZDArrayNeu[i][0].equals("")) {
                                // Immer noch frei, also in die Combobox rein schieben
                                /*if (i==0) dbint=15;
                                 if ((i>=1)&&(i<=9)) dbint=i+17;
                                 if ((i>=10)&&(i<=18)) dbint=i+19;
                                 if ((i>=19)&&(i<=29)) dbint=i+22;
                                 if ((i>=30)&&(i<=39)) dbint=i+41;*/
                                dbint = Vartab.ZD2Vartab(i);
                                combo.add(Integer.toString(dbint));
                            }
                        }
                    }
                }
                return combo.toArray(new String[combo.size()]);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fr.close();
                } catch (IOException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        return null;
    }

    public void Frameworklesen(File dir) {
        try {
            FileReader fr = null;
            fr = new FileReader(dir + "\\Framework.txt");
            BufferedReader br = new BufferedReader(fr);
            //Header einlesen
            String zeile = br.readLine();
            // 1. Zeile einlesen
            zeile = br.readLine();
            String[] zeilelist = zeile.split("#", 8);
            jTFrameworkNo.setText(zeilelist[0]);
            if (zeilelist[1].equals("ja")) {
                jBKostenpflicht.setSelected(true);
            } else {
                jBKostenpflicht.setSelected(false);
            }
            jTFrameworkpreis.setText(zeilelist[2]);
            jTMaintainer.setText(zeilelist[3]);
            jTVersion.setText(zeilelist[4]);
            jTababas.setText(zeilelist[5]);
            jTbisabas.setText(zeilelist[6]);
        } catch (FileNotFoundException ex) {
            jTLog.append("Framework.txt fehlt zu diesem Framework");
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void KurzInfoLesen(File dir) {
        FileInputStream fstream = null;
        try {
            jTKurzInfo.setText("");
            fstream = new FileInputStream(dir.toString() + "\\Kurzinfo.txt");
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in,StandardCharsets.UTF_8));

            String strLine = null;
            String newStrLine = null;
            try {
                //Read File Line By Line
                while ((strLine = br.readLine()) != null) {

                    if (strLine.contains("iNCi")) {
                        newStrLine = strLine.replace("iNCi", "");
                    } else {
                        newStrLine = strLine;
                    }

                    String inhalt = jTKurzInfo.getText();
                    jTKurzInfo.setText(inhalt + newStrLine + "\r\n");

                }    //Close the input stream
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                in.close();
                fstream.close();
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            //Datei nicht vorhanden
            jTLog.append("Kurzinfo zu diesem Framework fehlt");
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        }

    }

    public void DirList(File dir, DefaultMutableTreeNode node, boolean absteigen) {
        File sysdir;
        File[] files = dir.listFiles();
        if (files != null) { // Erforderliche Berechtigungen etc. sind vorhanden
            for (int i = 0; i < files.length; i++) {
                System.out.print(files[i].getName());
                if (files[i].isDirectory()) {
                    System.out.print(" (Ordner)\n");
                    //SystemNode =   new DefaultMutableTreeNode(files[i].getName());
                   if (!files[i].getName().equals("develop")&& !files[i].getName().equals("Frameworks"))
                   {
                    SystemNode = new DefaultMutableTreeNode(new PappInfo(files[i].getName(), files[i]));
                    node.add(SystemNode);
                    sysdir = files[i];
                    if (absteigen) {
                        DirList(sysdir, SystemNode, false);
                    }
                   }

                } else {
                    System.out.print(" (Datei)\n");
                }
            }
        }
    }

    private EDPSession SessionAufbauen(String yserver, int yport, String ymandant, String ypasswort) {
        EDPSession session = EDPFactory.createEDPSession();
        try {
            session.beginSession(yserver, yport, ymandant, ypasswort, "JEDP_0001");
        } catch (CantBeginSessionException ex) {
            JOptionPane.showMessageDialog(this, "Verbindungsaufnahme gescheitert\n\n" + ex, "EDP Fehler", JOptionPane.ERROR_MESSAGE);
            //Verbindung nicht erfolgreich
            return null;
        }
        return session;
    }

    public void changeZDinTables(int dateinummer) {
        int i = 0;
        // FOP Tabelle
        for (i = 0; i < jTFOP.getRowCount(); i++) {

        }
        //  
    }

// getter für Instanzierung wegen static/non static
    public JTable getjTFOPTXT() {
        return this.jTFOPTXT;
    }

// getter für Instanzierung wegen static/non static
    public JTable getjTschluessel() {
        return this.jTschluessel;
    }

    // getter für Instanzierung wegen static/non static
    public JTable getjTMasken() {
        return this.jTMasken;
    }

    class PappInfo {

        private String name;
        private File dir;

        public PappInfo(String name,
                File dir) {
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
                //new MainFrame().setVisible(true);
                MainFrame X = new MainFrame();//.setVisible(true);
                // Umgebaut um eine Referenzierung in getrennter Klasse zu halten um das static  und non static Problem zu umschiffen
                X.setVisible(true);
                ReferenceHolderClass.setGUI(X);

            }
        });
    }

    public boolean checkEditVartab(int row, int column) {
        int db = Integer.parseInt(jTVartab.getValueAt(row, 1).toString());
        if ((db == 15) || (db >= 18 && db <= 37) || (db >= 41 && db <= 51) || (db >= 71 && db <= 80)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkInstall() {
        if ((jTKunde.getText().length() > 0) && (jTInfos.getText().length() > 0)) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Bitte Kunde und Infos zum Installationszweck eintragen", "Bitte Eintragen", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public void Buttonsfree(boolean free)
    {
        if (free)
        {
            if (jTVartab.getRowCount()>0)jBInstallVartab.setEnabled(true);
            if (jTAufzaehlungen.getRowCount()>0) jBInstallAufzaehlungen.setEnabled(true);
            if (jTschluessel.getRowCount()>0) jBInstallSchluessel.setEnabled(true);
            if (jTinfosystem.getRowCount()>0)jBInstallInfosysteme.setEnabled(true);
            if (jTFOP.getRowCount()>0)jBinstallFOP.setEnabled(true);
            if (jTMasken.getRowCount()>0)jBInstallMasken.setEnabled(true);
             jBInstallAufrufparameter.setEnabled(false);
            jBPhase1.setEnabled(true);
            jBPhase2.setEnabled(true);
        
        
        
        
        
        
       
        }
        else
        { jBPhase1.setEnabled(false);
        jBPhase2.setEnabled(false);
        jBInstallAufzaehlungen.setEnabled(false);
        jBInstallVartab.setEnabled(false);
        jBInstallSchluessel.setEnabled(false);
        jBInstallInfosysteme.setEnabled(false);
        jBinstallFOP.setEnabled(false);
        jBInstallMasken.setEnabled(false);
        jBInstallAufrufparameter.setEnabled(false);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Canvas canvas1;
    private java.awt.Canvas canvas2;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JButton jBConnectionTest;
    private javax.swing.JButton jBDoku;
    private javax.swing.JButton jBInstallAufrufparameter;
    private javax.swing.JButton jBInstallAufzaehlungen;
    private javax.swing.JButton jBInstallInfosysteme;
    private javax.swing.JButton jBInstallMasken;
    private javax.swing.JButton jBInstallSchluessel;
    private javax.swing.JButton jBInstallVartab;
    private javax.swing.JCheckBox jBKostenpflicht;
    private javax.swing.JButton jBPhase1;
    private javax.swing.JButton jBPhase2;
    private javax.swing.JButton jBSinglesync;
    private javax.swing.JButton jBinstallFOP;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLuser;
    private javax.swing.JLabel jLversion;
    private javax.swing.JMenuItem jMenuEinstellungen;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPasswordField jPLinux;
    private javax.swing.JPasswordField jPMandant;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTable jTAufzaehlungen;
    private javax.swing.JTable jTFOP;
    private javax.swing.JTable jTFOPTXT;
    private javax.swing.JTextField jTFrameworkNo;
    private javax.swing.JTextField jTFrameworkpreis;
    private javax.swing.JTextField jTHost;
    private javax.swing.JTextField jTInfos;
    private javax.swing.JTextField jTKunde;
    private javax.swing.JTextArea jTKurzInfo;
    private javax.swing.JTextField jTLinuxUser;
    private javax.swing.JTextArea jTLog;
    private javax.swing.JTextField jTMaintainer;
    private javax.swing.JTextField jTMandant;
    private javax.swing.JTable jTMasken;
    private javax.swing.JTable jTObjekte;
    private javax.swing.JTable jTVartab;
    private javax.swing.JTextField jTVersion;
    private javax.swing.JTextField jTababas;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTbisabas;
    private javax.swing.JTextField jTedpPort;
    private javax.swing.JTable jTinfosystem;
    private javax.swing.JTree jTree1;
    private javax.swing.JTable jTschluessel;
    private javax.swing.JMenuBar menuBar;
    // End of variables declaration//GEN-END:variables

}
