package Form;

import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FormJadwalKelas extends JFrame {

    private JTextField txtNamaKelas, txtHari, txtJam;
    private JComboBox<String> cbInstruktur;
    private JTable table;
    private DefaultTableModel model;
    private ArrayList<Integer> listIDInstruktur = new ArrayList<>();

    public FormJadwalKelas() {

        setTitle("Form Jadwal Kelas Gym");
        setSize(900, 550);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblNama = new JLabel("Nama Kelas:");
        lblNama.setBounds(20, 20, 120, 25);
        add(lblNama);

        txtNamaKelas = new JTextField();
        txtNamaKelas.setBounds(160, 20, 180, 25);
        add(txtNamaKelas);

        JLabel lblHari = new JLabel("Hari:");
        lblHari.setBounds(20, 60, 120, 25);
        add(lblHari);

        txtHari = new JTextField();
        txtHari.setBounds(160, 60, 180, 25);
        add(txtHari);

        JLabel lblJam = new JLabel("Jam Kelas:");
        lblJam.setBounds(20, 100, 120, 25);
        add(lblJam);

        txtJam = new JTextField();
        txtJam.setBounds(160, 100, 180, 25);
        add(txtJam);

        JLabel lblInstruktur = new JLabel("Instruktur:");
        lblInstruktur.setBounds(20, 140, 120, 25);
        add(lblInstruktur);

        cbInstruktur = new JComboBox<>();
        cbInstruktur.setBounds(160, 140, 180, 25);
        add(cbInstruktur);

        // Tombol CRUD
        JButton btnSimpan = new JButton("Simpan"); btnSimpan.setBounds(20, 200, 100, 30);
        JButton btnUpdate = new JButton("Update"); btnUpdate.setBounds(130, 200, 100, 30);
        JButton btnHapus  = new JButton("Hapus");  btnHapus.setBounds(240, 200, 100, 30);
        JButton btnReset  = new JButton("Reset");  btnReset.setBounds(350, 200, 100, 30);

        add(btnSimpan); add(btnUpdate); add(btnHapus); add(btnReset);

        // ============================ TABLE (ID KELAS PERTAMA) ============================
        model = new DefaultTableModel();
        table = new JTable(model);
        model.addColumn("ID Kelas");
        model.addColumn("Nama Kelas");
        model.addColumn("Hari");
        model.addColumn("Jam Kelas");
        model.addColumn("Instruktur");

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20, 260, 750, 200);
        add(sp);

        loadInstruktur();
        loadData();

        btnSimpan.addActionListener(e -> simpan());
        btnUpdate.addActionListener(e -> update());
        btnHapus.addActionListener(e -> hapus());
        btnReset.addActionListener(e -> resetForm());

        // Click table → tampil ke form
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                txtNamaKelas.setText(model.getValueAt(row,1).toString());
                txtHari.setText(model.getValueAt(row,2).toString());
                txtJam.setText(model.getValueAt(row,3).toString());
                cbInstruktur.setSelectedItem(model.getValueAt(row,4).toString());
            }
        });
    }

    // =============================== LOAD INSTRUKTUR COMBOBOX ===============================
    private void loadInstruktur() {
        try {
            Connection conn = Koneksi.getKoneksi();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM instruktur_gym");

            cbInstruktur.addItem("-- PILIH INSTRUKTUR --");

            while (rs.next()) {
                cbInstruktur.addItem(rs.getString("nama"));
                listIDInstruktur.add(rs.getInt("id_instruktur"));
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(this,"Error load instruktur: "+e.getMessage()); }
    }

    // =============================== LOAD DATA ===============================
    private void loadData() {
        model.setRowCount(0);
        try {
            Connection conn = Koneksi.getKoneksi();
            String sql = "SELECT j.id_kelas, j.nama_kelas, j.hari, j.jam, i.nama " +
                         "FROM jadwal_kelas j JOIN instruktur_gym i ON j.id_instruktur = i.id_instruktur";
            ResultSet rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) model.addRow(new Object[]{ rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5) });

        } catch (Exception e){ JOptionPane.showMessageDialog(this,"Error load tabel: "+e.getMessage()); }
    }

    // =============================== CRUD ===============================
    private void simpan() {
        if (validasi()) return;
        try {
            Connection conn = Koneksi.getKoneksi();
            PreparedStatement pst = conn.prepareStatement("INSERT INTO jadwal_kelas (nama_kelas,hari,jam,id_instruktur) VALUES (?,?,?,?)");

            pst.setString(1, txtNamaKelas.getText());
            pst.setString(2, txtHari.getText());
            pst.setString(3, txtJam.getText());
            pst.setInt(4, listIDInstruktur.get(cbInstruktur.getSelectedIndex()-1));

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this,"Data Berhasil Disimpan!");
            loadData(); resetForm();

        } catch (Exception e){ JOptionPane.showMessageDialog(this,"Error simpan: "+e.getMessage()); }
    }

    private void update() {
        int row = table.getSelectedRow(); if(row==-1){ JOptionPane.showMessageDialog(this,"Pilih data dahulu!"); return; }
        if (validasi()) return;

        int id = Integer.parseInt(model.getValueAt(row,0).toString());

        try{
            Connection conn = Koneksi.getKoneksi();
            PreparedStatement pst = conn.prepareStatement("UPDATE jadwal_kelas SET nama_kelas=?, hari=?, jam=?, id_instruktur=? WHERE id_kelas=?");

            pst.setString(1, txtNamaKelas.getText());
            pst.setString(2, txtHari.getText());
            pst.setString(3, txtJam.getText());
            pst.setInt(4, listIDInstruktur.get(cbInstruktur.getSelectedIndex()-1));
            pst.setInt(5, id);

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this,"Data Berhasil Diupdate!");
            loadData(); resetForm();

        }catch(Exception e){ JOptionPane.showMessageDialog(this,"Error update: "+e.getMessage()); }
    }

    private void hapus() {
        int row = table.getSelectedRow(); if(row==-1){ JOptionPane.showMessageDialog(this,"Pilih data terlebih dahulu!"); return; }

        int id = Integer.parseInt(model.getValueAt(row,0).toString());

        try{
            Connection conn = Koneksi.getKoneksi();
            PreparedStatement pst = conn.prepareStatement("DELETE FROM jadwal_kelas WHERE id_kelas=?");
            pst.setInt(1,id);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this,"Data Berhasil Dihapus!");
            loadData(); resetForm();

        }catch(Exception e){ JOptionPane.showMessageDialog(this,"Error hapus: "+e.getMessage()); }
    }

    private boolean validasi(){
        if(txtNamaKelas.getText().isEmpty() || txtHari.getText().isEmpty() || txtJam.getText().isEmpty()){
            JOptionPane.showMessageDialog(this,"Field tidak boleh kosong!");
            return true;
        }
        if(cbInstruktur.getSelectedIndex()==0){
            JOptionPane.showMessageDialog(this,"Pilih instruktur terlebih dahulu!");
            return true;
        }
        return false;
    }

    private void resetForm(){
        txtNamaKelas.setText("");
        txtHari.setText("");
        txtJam.setText("");
        cbInstruktur.setSelectedIndex(0);
    }

    public static void main(String[] args){ new FormJadwalKelas().setVisible(true); }
}
