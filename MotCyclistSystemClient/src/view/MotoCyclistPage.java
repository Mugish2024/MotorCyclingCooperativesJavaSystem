/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Cooperatives;
import model.Motorcyclist;
import service.MotorcyclistService;

/**
 *
 * @author user
 */
public class MotoCyclistPage extends javax.swing.JFrame {
    private MotorcyclistService service;
    private DefaultTableModel tableModel;

    /**
     * Creates new form MotoCyclistPage
     */
    public MotoCyclistPage() {
        initComponents();
        initializeService();
      initializeTable();
      loadMotorcyclists();
    }
   private void initializeService() {
    try {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 2325);
        service = (MotorcyclistService) registry.lookup("MotorcyclistService");
        if (service == null) {
            throw new Exception("Failed to lookup MotorcyclistService");
        }
        System.out.println("Connected to MotorcyclistService at 127.0.0.1:2325");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Cannot connect to server: " + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
        System.err.println("Connection failed: " + e.getMessage());
        e.printStackTrace();
        System.exit(1);
    }
}

private void initializeTable() {
    tableModel = new DefaultTableModel(new String[]{"ID", "Full Names", "Licence Number", "Phone Number", "Age", "Cooperative Name"}, 0);
    jTable1.setModel(tableModel);
    jTable1.getColumnModel().getColumn(0).setMinWidth(0); // Hide ID column
    jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
    jTable1.getColumnModel().getColumn(0).setWidth(0);
    jTable1.getSelectionModel().addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting() && jTable1.getSelectedRow() != -1) {
            int row = jTable1.getSelectedRow();
            try {
                int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                Motorcyclist m = service.findById(id);
                if (m != null) {
                    IDTxt.setText(String.valueOf(id));
                    NameTxt.setText(m.getName() != null ? m.getName() : "");
                    LicenceNbrTxt.setText(m.getLicenseNumber() != null ? m.getLicenseNumber() : "");
                    PhonenbrTxt.setText(m.getPhone() != null ? m.getPhone() : "");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    DateofBirthTxt.setText(m.getDateOfBirth() != null ? sdf.format(m.getDateOfBirth()) : "");
                    CooperativeIdTxt.setText(m.getCooperative() != null ? String.valueOf(m.getCooperative().getId()) : "");
                }
            } catch (Exception ex) {
                System.err.println("Error fetching motorcyclist for selection: " + ex.getMessage());
                ex.printStackTrace(); // Add stack trace for debugging
            }
        }
    });
}

private void loadMotorcyclists() {
    try {
        if (service == null) {
            JOptionPane.showMessageDialog(this, "Service not initialized. Please restart the application.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        tableModel.setRowCount(0);
        List<Motorcyclist> motorcyclists = service.findAll();
        if (motorcyclists != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (Motorcyclist m : motorcyclists) {
                if (m != null) {
                    int age = 0;
                    if (m.getDateOfBirth() != null) {
                        try {
                            String dobStr = sdf.format(m.getDateOfBirth());
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            LocalDate birthDate = LocalDate.parse(dobStr, formatter);
                            LocalDate currentDate = LocalDate.now();
                            age = Period.between(birthDate, currentDate).getYears();
                        } catch (Exception ex) {
                            System.err.println("Invalid DOB format for motorcyclist: " + m.getId());
                        }
                    }
                    tableModel.addRow(new Object[]{
                        m.getId(), // Add ID
                        m.getName() != null ? m.getName() : "",
                        m.getLicenseNumber() != null ? m.getLicenseNumber() : "",
                        m.getPhone() != null ? m.getPhone() : "",
                        age,
                        m.getCooperative() != null ? m.getCooperative().getName() : ""
                    });
                }
            }
        } else {
            System.out.println("No motorcyclists found or service returned null.");
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error loading motorcyclists: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        System.err.println("Load failed: " + ex.getMessage());
        ex.printStackTrace();
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

        menuBar1 = new java.awt.MenuBar();
        menu1 = new java.awt.Menu();
        menu2 = new java.awt.Menu();
        jPanel1 = new javax.swing.JPanel();
        BackButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        IDTxt = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        NameTxt = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        LicenceNbrTxt = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        PhonenbrTxt = new javax.swing.JTextField();
        DateofBirthTxt = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        CooperativeIdTxt = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        SearchIdTxt = new javax.swing.JTextField();
        SearchByIdButton = new javax.swing.JButton();
        SaveButton = new javax.swing.JButton();
        UpdateButton = new javax.swing.JButton();
        DeleteButton = new javax.swing.JButton();

        menu1.setLabel("File");
        menuBar1.add(menu1);

        menu2.setLabel("Edit");
        menuBar1.add(menu2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));

        BackButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/backIcon.PNG"))); // NOI18N
        BackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BackButton)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 61, Short.MAX_VALUE)
                .addComponent(BackButton))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51), 5));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/details Icon.PNG"))); // NOI18N
        jLabel1.setText("Riders Details");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/updkey.PNG"))); // NOI18N
        jLabel3.setText("ID");

        IDTxt.setEditable(false);
        IDTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IDTxtActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Rider's Name");

        NameTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NameTxtActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("LicenceNumber");

        LicenceNbrTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LicenceNbrTxtActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Phone Number");

        PhonenbrTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PhonenbrTxtActionPerformed(evt);
            }
        });

        DateofBirthTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DateofBirthTxtActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("Date of Birth(YYY-MM-DD)");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setText("Cooperative Id");

        CooperativeIdTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CooperativeIdTxtActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(103, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(67, 67, 67))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(IDTxt)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(NameTxt)
                    .addComponent(LicenceNbrTxt)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                    .addComponent(PhonenbrTxt)
                    .addComponent(DateofBirthTxt)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(CooperativeIdTxt))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(IDTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LicenceNbrTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PhonenbrTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(DateofBirthTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(CooperativeIdTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51), 5));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/databaseIcon_1.PNG"))); // NOI18N
        jLabel6.setText("Riders information Database");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Full Names", "Licence Number", "Phone Number", "Age", "Cooperative Namew"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/searchIcon.PNG"))); // NOI18N
        jLabel7.setText("Enter Id to Search");

        SearchIdTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchIdTxtActionPerformed(evt);
            }
        });

        SearchByIdButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        SearchByIdButton.setText("Search");
        SearchByIdButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchByIdButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(SearchIdTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58)
                        .addComponent(SearchByIdButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(104, 104, 104)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(SearchIdTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SearchByIdButton))
                .addGap(110, 110, 110))
        );

        SaveButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        SaveButton.setText("Save");
        SaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveButtonActionPerformed(evt);
            }
        });

        UpdateButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        UpdateButton.setText("Update");
        UpdateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateButtonActionPerformed(evt);
            }
        });

        DeleteButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        DeleteButton.setText("Delete");
        DeleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(77, 77, 77)
                        .addComponent(SaveButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(UpdateButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(DeleteButton)
                        .addGap(70, 70, 70))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(SaveButton)
                            .addComponent(UpdateButton)
                            .addComponent(DeleteButton))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void IDTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IDTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IDTxtActionPerformed

    private void NameTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NameTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NameTxtActionPerformed

    private void LicenceNbrTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LicenceNbrTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LicenceNbrTxtActionPerformed

    private void PhonenbrTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PhonenbrTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PhonenbrTxtActionPerformed

    private void SearchIdTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchIdTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SearchIdTxtActionPerformed

    private void DateofBirthTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DateofBirthTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DateofBirthTxtActionPerformed

    private void SaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveButtonActionPerformed
        // TODO add your handling code here:
       try {
        if (NameTxt.getText().trim().isEmpty() || LicenceNbrTxt.getText().trim().isEmpty() ||
            PhonenbrTxt.getText().trim().isEmpty() || DateofBirthTxt.getText().trim().isEmpty() ||
            CooperativeIdTxt.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String phone = PhonenbrTxt.getText().trim();
        if (!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Phone number must be exactly 10 digits.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String licenseNumber = LicenceNbrTxt.getText().trim();
        if (!licenseNumber.matches("\\d{16}")) {
            JOptionPane.showMessageDialog(this, "License number must be exactly 16 digits.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String dobStr = DateofBirthTxt.getText().trim();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dob;
        try {
            dob = sdf.parse(dobStr);
            LocalDate birthDate = LocalDate.parse(dobStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate currentDate = LocalDate.now();
            if (birthDate.isAfter(currentDate)) {
                JOptionPane.showMessageDialog(this, "Date of birth cannot be in the future.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid date of birth format. Use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int cooperativeId;
        try {
            cooperativeId = Integer.parseInt(CooperativeIdTxt.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cooperative ID must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Motorcyclist motorcyclist = new Motorcyclist();
        motorcyclist.setName(NameTxt.getText().trim());
        motorcyclist.setLicenseNumber(licenseNumber);
        motorcyclist.setPhone(phone);
        motorcyclist.setDateOfBirth(dob);
        Cooperatives cooperative = new Cooperatives();
        cooperative.setId(cooperativeId);
        motorcyclist.setCooperative(cooperative);
        service.save(motorcyclist);
        JOptionPane.showMessageDialog(this, "Motorcyclist saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        NameTxt.setText("");
        LicenceNbrTxt.setText("");
        PhonenbrTxt.setText("");
        DateofBirthTxt.setText("");
        CooperativeIdTxt.setText("");
        SearchIdTxt.setText("");
        loadMotorcyclists();
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        System.err.println("Save failed: " + ex.getMessage());
        ex.printStackTrace();
    }

    }//GEN-LAST:event_SaveButtonActionPerformed

    private void UpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateButtonActionPerformed
        // TODO add your handling code here:
        try {
        if (IDTxt.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a motorcyclist to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (NameTxt.getText().trim().isEmpty() || LicenceNbrTxt.getText().trim().isEmpty() ||
            PhonenbrTxt.getText().trim().isEmpty() || DateofBirthTxt.getText().trim().isEmpty() ||
            CooperativeIdTxt.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String phone = PhonenbrTxt.getText().trim();
        if (!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Phone number must be exactly 10 digits.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String licenseNumber = LicenceNbrTxt.getText().trim();
        if (!licenseNumber.matches("\\d{16}")) {
            JOptionPane.showMessageDialog(this, "License number must be exactly 16 digits.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String dobStr = DateofBirthTxt.getText().trim();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dob;
        try {
            dob = sdf.parse(dobStr);
            LocalDate birthDate = LocalDate.parse(dobStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate currentDate = LocalDate.now();
            if (birthDate.isAfter(currentDate)) {
                JOptionPane.showMessageDialog(this, "Date of birth cannot be in the future.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid date of birth format. Use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int cooperativeId, id;
        try {
            id = Integer.parseInt(IDTxt.getText().trim());
            cooperativeId = Integer.parseInt(CooperativeIdTxt.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID and Cooperative ID must be numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Motorcyclist motorcyclist = new Motorcyclist();
        motorcyclist.setId(id);
        motorcyclist.setName(NameTxt.getText().trim());
        motorcyclist.setLicenseNumber(licenseNumber);
        motorcyclist.setPhone(phone);
        motorcyclist.setDateOfBirth(dob);
        Cooperatives cooperative = new Cooperatives();
        cooperative.setId(cooperativeId);
        motorcyclist.setCooperative(cooperative);
        service.update(motorcyclist);
        JOptionPane.showMessageDialog(this, "Motorcyclist updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        IDTxt.setText("");
        NameTxt.setText("");
        LicenceNbrTxt.setText("");
        PhonenbrTxt.setText("");
        DateofBirthTxt.setText("");
        CooperativeIdTxt.setText("");
        SearchIdTxt.setText("");
        loadMotorcyclists();
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        System.err.println("Update failed: " + ex.getMessage());
        ex.printStackTrace();
    }
    }//GEN-LAST:event_UpdateButtonActionPerformed

    private void DeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteButtonActionPerformed
        // TODO add your handling code here:
        try {
        if (IDTxt.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a motorcyclist to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this motorcyclist?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(IDTxt.getText().trim());
            Motorcyclist motorcyclist = service.findById(id);
            if (motorcyclist == null) {
                JOptionPane.showMessageDialog(this, "Motorcyclist not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            service.delete(motorcyclist);
            JOptionPane.showMessageDialog(this, "Motorcyclist deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            IDTxt.setText("");
            NameTxt.setText("");
            LicenceNbrTxt.setText("");
            PhonenbrTxt.setText("");
            DateofBirthTxt.setText("");
            CooperativeIdTxt.setText("");
            SearchIdTxt.setText("");
            loadMotorcyclists();
        }
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Invalid ID: Must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
        System.err.println("Invalid input: " + ex.getMessage());
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error deleting motorcyclist: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        System.err.println("Delete failed: " + ex.getMessage());
        ex.printStackTrace();
    }
    }//GEN-LAST:event_DeleteButtonActionPerformed

    private void SearchByIdButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchByIdButtonActionPerformed
        // TODO add your handling code here:
      try {
        String searchId = SearchIdTxt.getText().trim();
        if (searchId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter an ID to search.", "Error", JOptionPane.ERROR_MESSAGE);
            loadMotorcyclists();
            return;
        }
        int id = Integer.parseInt(searchId);
        Motorcyclist motorcyclist = service.findById(id);
        tableModel.setRowCount(0);
        if (motorcyclist != null) {
            int age = 0;
            if (motorcyclist.getDateOfBirth() != null) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String dobStr = sdf.format(motorcyclist.getDateOfBirth());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate birthDate = LocalDate.parse(dobStr, formatter);
                    LocalDate currentDate = LocalDate.now();
                    age = Period.between(birthDate, currentDate).getYears();
                } catch (Exception ex) {
                    System.err.println("Invalid DOB format for motorcyclist: " + motorcyclist.getId());
                }
            }
            tableModel.addRow(new Object[]{
                motorcyclist.getId(), // Add ID
                motorcyclist.getName() != null ? motorcyclist.getName() : "",
                motorcyclist.getLicenseNumber() != null ? motorcyclist.getLicenseNumber() : "",
                motorcyclist.getPhone() != null ? motorcyclist.getPhone() : "",
                age,
                motorcyclist.getCooperative() != null ? motorcyclist.getCooperative().getName() : ""
            });
        } else {
            JOptionPane.showMessageDialog(this, "No motorcyclist found with ID " + id, "Info", JOptionPane.INFORMATION_MESSAGE);
        }
        SearchIdTxt.setText("");
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Invalid ID: Must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
        System.err.println("Invalid input: " + ex.getMessage());
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error searching motorcyclist: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        System.err.println("Search failed: " + ex.getMessage());
        ex.printStackTrace();
    }
    }//GEN-LAST:event_SearchByIdButtonActionPerformed

    private void CooperativeIdTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CooperativeIdTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CooperativeIdTxtActionPerformed

    private void BackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackButtonActionPerformed
        // TODO add your handling code here:
          new DashBoard1().setVisible(true); // Open SignupFrame
        dispose();
    }//GEN-LAST:event_BackButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BackButton;
    private javax.swing.JTextField CooperativeIdTxt;
    private javax.swing.JTextField DateofBirthTxt;
    private javax.swing.JButton DeleteButton;
    private javax.swing.JTextField IDTxt;
    private javax.swing.JTextField LicenceNbrTxt;
    private javax.swing.JTextField NameTxt;
    private javax.swing.JTextField PhonenbrTxt;
    private javax.swing.JButton SaveButton;
    private javax.swing.JButton SearchByIdButton;
    private javax.swing.JTextField SearchIdTxt;
    private javax.swing.JButton UpdateButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private java.awt.Menu menu1;
    private java.awt.Menu menu2;
    private java.awt.MenuBar menuBar1;
    // End of variables declaration//GEN-END:variables
}
