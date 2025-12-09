package Form;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FormInstruktur extends JFrame {

    private JTextField txtNama, txtUsia, txtKeahlian, txtHp;
    private JTable table;
    private DefaultTableModel model;

    public FormInstruktur() {

        setTitle("Data Instruktur Gym");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblNama = new JLabel("Nama:");
        lblNama.setBounds(20, 20, 120, 25);
        add(lblNama);

        txtNama = new JTextField();
        txtNama.setBounds(150, 20, 150, 25);
        add(txtNama);

        JLabel lblUsia = new JLabel("Usia:");
        lblUsia.setBounds(20, 60, 120, 25);
        add(lblUsia);

        txtUsia = new JTextField();
        txtUsia.setBounds(150, 60, 150, 25);
        add(txtUsia);

        JLabel lblKeahlian = new JLabel("Keahlian:");
        lblKeahlian.setBounds(20, 100, 120, 25);
        add(lblKeahlian);

        txtKeahlian = new JTextField();
        txtKeahlian.setBounds(150, 100, 150, 25);
        add(txtKeahlian);

        JLabel lblHp = new JLabel("No HP:");
        lblHp.setBounds(20, 140, 120, 25);
        add(lblHp);

        txtHp = new JTextField();
        txtHp.setBounds(150, 140, 150, 25);
        add(txtHp);

        JButton btnSimpan = new JButton("Simpan");
        btnSimpan.setBounds(20, 190, 100, 30);
        add(btnSimpan);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBounds(130, 190, 100, 30);
        add(btnUpdate);

        JButton btnHapus = new JButton("Hapus");
        btnHapus.setBounds(240, 190, 100, 30);
        add(btnHapus);

        JButton btnReset = new JButton("Reset");
        btnReset.setBounds(350, 190, 100, 30);
        add(btnReset);

        model = new DefaultTableModel();
        table = new JTable(model);

        model.addColumn("ID");
        model.addColumn("Nama");
        model.addColumn("Usia");
        model.addColumn("Keahlian");
        model.addColumn("No HP");

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(320, 20, 450, 350);
        add(sp);

        loadData();

        btnSimpan.addActionListener(e -> simpanData());
        btnUpdate.addActionListener(e -> updateData());
        btnHapus.addActionListener(e -> hapusData());
        btnReset.addActionListener(e -> resetForm());

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                txtNama.setText(model.getValueAt(row, 1).toString());
                txtUsia.setText(model.getValueAt(row, 2).toString());
                txtKeahlian.setText(model.getValueAt(row, 3).toString());
                txtHp.setText(model.getValueAt(row, 4).toString());
            }
        });
    }

    private void loadData() {
        model.setRowCount(0);

        try {
            Connection conn = Koneksi.getKoneksi();
            String sql = "SELECT * FROM instruktur_gym";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id_instruktur"),
                        rs.getString("nama"),
                        rs.getInt("usia"),
                        rs.getString("keahlian"),
                        rs.getString("nohp")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error load: " + e.getMessage());
        }
    }

    private void simpanData() {
        try {
            Connection conn = Koneksi.getKoneksi();
            String sql = "INSERT INTO instruktur_gym(nama, usia, keahlian, nohp) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, txtNama.getText());
            pst.setInt(2, Integer.parseInt(txtUsia.getText()));
            pst.setString(3, txtKeahlian.getText());
            pst.setString(4, txtHp.getText());

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data Berhasil Disimpan!");
            loadData();
            resetForm();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error simpan: " + e.getMessage());
        }
    }

    private void updateData() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data dulu!");
            return;
        }

        int id = (int) table.getValueAt(row, 0);

        try {
            Connection conn = Koneksi.getKoneksi();
            String sql = "UPDATE instruktur_gym SET nama=?, usia=?, keahlian=?, nohp=? WHERE id_instruktur=?";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, txtNama.getText());
            pst.setInt(2, Integer.parseInt(txtUsia.getText()));
            pst.setString(3, txtKeahlian.getText());
            pst.setString(4, txtHp.getText());
            pst.setInt(5, id);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data Berhasil Diupdate");
            loadData();
            resetForm();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error update: " + e.getMessage());
        }
    }

    private void hapusData() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data dulu!");
            return;
        }

        int id = (int) table.getValueAt(row, 0);

        try {
            Connection conn = Koneksi.getKoneksi();
            String sql = "DELETE FROM instruktur_gym WHERE id_instruktur=?";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setInt(1, id);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data Berhasil Dihapus!");
            loadData();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error hapus: " + e.getMessage());
        }
    }

    private void resetForm() {
        txtNama.setText("");
        txtUsia.setText("");
        txtKeahlian.setText("");
        txtHp.setText("");
    }

    public static void main(String[] args) {
        new FormInstruktur().setVisible(true);
    }
}