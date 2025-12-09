package Form;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;

public class FormJadwalKelas extends JFrame {

    private JTextField txtHari, txtJam, txtKelas;
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

        JLabel lblHari = new JLabel("Hari:");
        lblHari.setBounds(20, 20, 120, 25);
        add(lblHari);

        txtHari = new JTextField();
        txtHari.setBounds(160, 20, 180, 25);
        add(txtHari);

        JLabel lblJam = new JLabel("Jam:");
        lblJam.setBounds(20, 60, 120, 25);
        add(lblJam);

        txtJam = new JTextField();
        txtJam.setBounds(160, 60, 180, 25);
        add(txtJam);

        JLabel lblKelas = new JLabel("Nama Kelas:");
        lblKelas.setBounds(20, 100, 120, 25);
        add(lblKelas);

        txtKelas = new JTextField();
        txtKelas.setBounds(160, 100, 180, 25);
        add(txtKelas);

        JLabel lblInstruktur = new JLabel("Instruktur:");
        lblInstruktur.setBounds(20, 140, 120, 25);
        add(lblInstruktur);

        cbInstruktur = new JComboBox<>();
        cbInstruktur.setBounds(160, 140, 180, 25);
        add(cbInstruktur);

        JButton btnSimpan = new JButton("Simpan");
        btnSimpan.setBounds(20, 200, 100, 30);
        add(btnSimpan);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBounds(130, 200, 100, 30);
        add(btnUpdate);

        JButton btnHapus = new JButton("Hapus");
        btnHapus.setBounds(240, 200, 100, 30);
        add(btnHapus);

        JButton btnReset = new JButton("Reset");
        btnReset.setBounds(350, 200, 100, 30);
        add(btnReset);

        model = new DefaultTableModel();
        table = new JTable(model);
        model.addColumn("ID Jadwal");
        model.addColumn("Hari");
        model.addColumn("Jam");
        model.addColumn("Kelas");
        model.addColumn("Instruktur");

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(370, 20, 500, 400);
        add(sp);

        loadInstruktur();
        loadData();

        btnSimpan.addActionListener(e -> simpan());
        btnUpdate.addActionListener(e -> update());
        btnHapus.addActionListener(e -> hapus());
        btnReset.addActionListener(e -> resetForm());

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                txtHari.setText(model.getValueAt(row, 1).toString());
                txtJam.setText(model.getValueAt(row, 2).toString());
                txtKelas.setText(model.getValueAt(row, 3).toString());
                cbInstruktur.setSelectedItem(model.getValueAt(row, 4).toString());
            }
        });
    }

    // ====================================================== LOAD DATA =========================================================

    private void loadInstruktur() {
        try {
            Connection conn = Koneksi.getKoneksi();
            String sql = "SELECT * FROM instruktur_gym";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            cbInstruktur.addItem("-- PILIH INSTRUKTUR --");

            while (rs.next()) {
                cbInstruktur.addItem(rs.getString("nama"));
                listIDInstruktur.add(rs.getInt("id_instruktur"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,"Error load instruktur: " + e.getMessage());
        }
    }

    private void loadData() {
        model.setRowCount(0);

        try {
            Connection conn = Koneksi.getKoneksi();
            String sql = "SELECT j.id_jadwal, j.hari, j.jam, j.nama_kelas, i.nama " +
                         "FROM jadwal_kelas j JOIN instruktur_gym i ON j.id_instruktur = i.id_instruktur";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5)
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,"Error load tabel: " + e.getMessage());
        }
    }

    // ====================================================== CRUD =========================================================

    private void simpan() {
        if(cbInstruktur.getSelectedIndex() == 0){
            JOptionPane.showMessageDialog(this,"Pilih instruktur terlebih dahulu!");
            return;
        }

        try {
            Connection conn = Koneksi.getKoneksi();
            String sql = "INSERT INTO jadwal_kelas (hari, jam, nama_kelas, id_instruktur) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, txtHari.getText());
            pst.setString(2, txtJam.getText());
            pst.setString(3, txtKelas.getText());
            pst.setInt(4, listIDInstruktur.get(cbInstruktur.getSelectedIndex()-1));

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this,"Data Berhasil Disimpan!");
            loadData(); resetForm();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,"Error simpan: " + e.getMessage());
        }
    }

    private void update() {
        int row = table.getSelectedRow();
        if(row == -1){ JOptionPane.showMessageDialog(this,"Pilih data di tabel!"); return; }

        int id = Integer.parseInt(model.getValueAt(row,0).toString());

        try {
            Connection conn = Koneksi.getKoneksi();
            String sql = "UPDATE jadwal_kelas SET hari=?, jam=?, nama_kelas=?, id_instruktur=? WHERE id_jadwal=?";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, txtHari.getText());
            pst.setString(2, txtJam.getText());
            pst.setString(3, txtKelas.getText());
            pst.setInt(4, listIDInstruktur.get(cbInstruktur.getSelectedIndex()-1));
            pst.setInt(5, id);

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this,"Data Berhasil Diupdate!");
            loadData(); resetForm();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,"Error update: " + e.getMessage());
        }
    }

    private void hapus() {
        int row = table.getSelectedRow();
        if(row == -1){ JOptionPane.showMessageDialog(this,"Pilih data dulu!"); return; }

        int id = Integer.parseInt(model.getValueAt(row,0).toString());

        try {
            Connection conn = Koneksi.getKoneksi();
            String sql = "DELETE FROM jadwal_kelas WHERE id_jadwal=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1,id);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this,"Data Berhasil Dihapus!");
            loadData(); resetForm();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,"Error hapus: " + e.getMessage());
        }
    }

    private void resetForm() {
        txtHari.setText("");
        txtJam.setText("");
        txtKelas.setText("");
        cbInstruktur.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        new FormJadwalKelas().setVisible(true);
    }
}