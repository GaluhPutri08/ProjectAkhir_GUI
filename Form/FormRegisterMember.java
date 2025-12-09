package Form;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;

public class FormRegisterMember {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Form Registrasi Member Gym");
        frame.setSize(650, 520);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ============================
        // LABEL DAN INPUT
        // ============================

        JLabel lblNama = new JLabel("Nama Member:");
        lblNama.setBounds(20, 20, 120, 25);
        frame.add(lblNama);

        JTextField txtNama = new JTextField();
        txtNama.setBounds(150, 20, 200, 25);
        frame.add(txtNama);

        JLabel lblUsia = new JLabel("Usia:");
        lblUsia.setBounds(20, 60, 120, 25);
        frame.add(lblUsia);

        JTextField txtUsia = new JTextField();
        txtUsia.setBounds(150, 60, 200, 25);
        frame.add(txtUsia);

        JLabel lblGender = new JLabel("Gender:");
        lblGender.setBounds(20, 100, 120, 25);
        frame.add(lblGender);

        JComboBox<String> cbGender = new JComboBox<>(
                new String[]{"Laki-laki", "Perempuan"}
        );
        cbGender.setBounds(150, 100, 200, 25);
        frame.add(cbGender);

        JLabel lblHP = new JLabel("No HP:");
        lblHP.setBounds(20, 140, 120, 25);
        frame.add(lblHP);

        JTextField txtHP = new JTextField();
        txtHP.setBounds(150, 140, 200, 25);
        frame.add(txtHP);

        JLabel lblAlamat = new JLabel("Alamat:");
        lblAlamat.setBounds(20, 180, 120, 25);
        frame.add(lblAlamat);

        JTextField txtAlamat = new JTextField();
        txtAlamat.setBounds(150, 180, 350, 25);
        frame.add(txtAlamat);

        // ============================
        // TABEL
        // ============================

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nama");
        model.addColumn("Usia");
        model.addColumn("Gender");
        model.addColumn("No HP");
        model.addColumn("Alamat");

        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 300, 600, 150);
        frame.add(scroll);

        // ============================
        // BUTTON
        // ============================

        JButton btnSimpan = new JButton("Simpan");
        btnSimpan.setBounds(20, 230, 120, 30);
        frame.add(btnSimpan);

        JButton btnHapus = new JButton("Hapus");
        btnHapus.setBounds(160, 230, 120, 30);
        frame.add(btnHapus);

        JButton btnReset = new JButton("Reset");
        btnReset.setBounds(300, 230, 120, 30);
        frame.add(btnReset);

        // ============================
        // KONEKSI DATABASE
        // ============================

        String url = "jdbc:mysql://localhost:3306/gym_management";
        String user = "root";
        String pass = "";

        // ============================
        // LOAD DATA KE TABEL
        // ============================

        loadTable(model);

        // ============================
        // EVENT SIMPAN
        // ============================

        btnSimpan.addActionListener(e -> {
            try {
                String nama = txtNama.getText();
                String usiaStr = txtUsia.getText();
                String gender = cbGender.getSelectedItem().toString();
                String nohp = txtHP.getText();
                String alamat = txtAlamat.getText();

                if (nama.isEmpty() || usiaStr.isEmpty() || nohp.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Nama, usia dan No HP wajib diisi!");
                    return;
                }

                int usia = Integer.parseInt(usiaStr);

                Connection conn = DriverManager.getConnection(url, user, pass);
                String sql = "INSERT INTO member_gym(nama, usia, gender, nohp, alamat) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);

                stmt.setString(1, nama);
                stmt.setInt(2, usia);
                stmt.setString(3, gender);
                stmt.setString(4, nohp);
                stmt.setString(5, alamat);

                stmt.executeUpdate();
                conn.close();

                JOptionPane.showMessageDialog(frame, "Data berhasil disimpan!");
                loadTable(model);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, 
                 "Gagal menyimpan!\n" + ex.getMessage());
                }

        });

        // ============================
        // EVENT HAPUS
        // ============================

        btnHapus.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(frame, "Pilih data dulu!");
                return;
            }

            int id = Integer.parseInt(model.getValueAt(row, 0).toString());

            try {
                Connection conn = DriverManager.getConnection(url, user, pass);
                conn.createStatement().executeUpdate(
                        "DELETE FROM member_gym WHERE id_member=" + id
                );
                conn.close();

                JOptionPane.showMessageDialog(frame, "Data berhasil dihapus!");
                loadTable(model);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Gagal menghapus!");
            }
        });

        // ============================
        // EVENT RESET
        // ============================

        btnReset.addActionListener(e -> {
            txtNama.setText("");
            txtUsia.setText("");
            txtHP.setText("");
            txtAlamat.setText("");
            cbGender.setSelectedIndex(0);
        });

        frame.setVisible(true);
    }

    // ============================
    // LOAD TABEL
    // ============================

    static void loadTable(DefaultTableModel model) {
        model.setRowCount(0);

        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/gym_management", "root", "");

            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM member_gym");

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id_member"),
                        rs.getString("nama"),
                        rs.getInt("usia"),
                        rs.getString("gender"),
                        rs.getString("nohp"),
                        rs.getString("alamat")
                });
            }

            conn.close();

        } catch (Exception e) {
            System.out.println("Gagal load data!");
        }
    }
}