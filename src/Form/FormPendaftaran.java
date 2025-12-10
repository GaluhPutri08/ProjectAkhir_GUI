package Form;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class FormPendaftaran extends JFrame {

        public FormPendaftaran() {

                // tampilan form
                setTitle("Form Pendaftaran Kelas Gym");
                setSize(600, 500);
                setLayout(null);
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                
                // Label dan Input Member
                JLabel lblMember = new JLabel("Member:");
                lblMember.setBounds(20, 20, 120, 25);
                add(lblMember);

                JComboBox<String> cbMember = new JComboBox<>();
                cbMember.setBounds(150, 20, 200, 25);
                add(cbMember);

                
                // Label dan Input Kelas Gym
                
                JLabel lblKelas = new JLabel("Kelas Gym:");
                lblKelas.setBounds(20, 60, 120, 25);
                add(lblKelas);

                JComboBox<String> cbKelas = new JComboBox<>();
                cbKelas.setBounds(150, 60, 200, 25);
                add(cbKelas);

                
                // Tanggal Daftar
                
                JLabel lblTanggal = new JLabel("Tanggal Daftar:");
                lblTanggal.setBounds(20, 100, 120, 25);
                add(lblTanggal);

                JTextField txtTanggal = new JTextField("2025-01-01");
                txtTanggal.setBounds(150, 100, 200, 25);
                add(txtTanggal);

                
                // Catatan
                
                JLabel lblCatatan = new JLabel("Catatan:");
                lblCatatan.setBounds(20, 140, 120, 25);
                add(lblCatatan);

                JTextField txtCatatan = new JTextField();
                txtCatatan.setBounds(150, 140, 200, 25);
                add(txtCatatan);

                
                // TABEL DATA PENDAFTARAN
                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("ID");
                model.addColumn("Member");
                model.addColumn("Kelas");
                model.addColumn("Tanggal");
                model.addColumn("Catatan");

                JTable table = new JTable(model);
                JScrollPane scroll = new JScrollPane(table);
                scroll.setBounds(20, 240, 540, 180);
                add(scroll);

                
                // Tombol Simpan
                JButton btnSimpan = new JButton("Simpan");
                btnSimpan.setBounds(20, 190, 120, 30);
                add(btnSimpan);

                // Tombol Reset
                JButton btnReset = new JButton("Reset");
                btnReset.setBounds(160, 190, 120, 30);
                add(btnReset);

                // Tombol Hapus
                JButton btnHapus = new JButton("Hapus");
                btnHapus.setBounds(300, 190, 120, 30);
                add(btnHapus);

                //KONEKSI DATABASE
                String url = "jdbc:mysql://localhost:3306/gym_management";
                String user = "root";
                String pass = "";

                
                // LOAD MEMBER KE COMBOBOX
                try (Connection conn = DriverManager.getConnection(url, user, pass)) {
                    ResultSet rs = conn.createStatement().executeQuery("SELECT id_member, nama FROM member_gym");
                    while (rs.next()) {
                        cbMember.addItem(rs.getInt("id_member") + " - " + rs.getString("nama"));
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Gagal load member!");
                }


                // LOAD KELAS KE COMBOBOX
                try (Connection conn = DriverManager.getConnection(url, user, pass)) {
                    ResultSet rs = conn.createStatement().executeQuery("SELECT id_kelas, nama_kelas FROM jadwal_kelas");
                    while (rs.next()) {
                        cbKelas.addItem(rs.getInt("id_kelas") + " - " + rs.getString("nama_kelas"));
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Gagal load kelas!");
                }

                // LOAD TABEL PENDAFTARAN
                loadTable(model);

                //EVENT SIMPAN
                btnSimpan.addActionListener(e -> {
                    try {
                        int idMember = Integer.parseInt(cbMember.getSelectedItem().toString().split(" - ")[0]);
                        int idKelas = Integer.parseInt(cbKelas.getSelectedItem().toString().split(" - ")[0]);
                        String tanggal = txtTanggal.getText();
                        String catatan = txtCatatan.getText();

                        Connection conn = DriverManager.getConnection(url, user, pass);
                        String sql = "INSERT INTO pendaftaran_kelas(id_member, id_kelas, tgl_daftar, catatan) VALUES (?, ?, ?, ?)";
                        PreparedStatement stmt = conn.prepareStatement(sql);

                        stmt.setInt(1, idMember);
                        stmt.setInt(2, idKelas);
                        stmt.setString(3, tanggal);
                        stmt.setString(4, catatan);

                        stmt.executeUpdate();
                        conn.close();

                        JOptionPane.showMessageDialog(this, "Pendaftaran berhasil!");

                        loadTable(model);

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Gagal menyimpan!");
                    }
                });

                // RESET FORM
                btnReset.addActionListener(e -> {
                    txtTanggal.setText("");
                    txtCatatan.setText("");
                });

                // HAPUS DATA
                btnHapus.addActionListener(e -> {
                    int row = table.getSelectedRow();
                    if (row == -1) {
                        JOptionPane.showMessageDialog(this, "Pilih data dulu!");
                        return;
                    }

                    int idDelete = Integer.parseInt(model.getValueAt(row, 0).toString());

                    try {
                        Connection conn = DriverManager.getConnection(url, user, pass);
                        conn.createStatement().executeUpdate("DELETE FROM pendaftaran_kelas WHERE id_pendaftaran=" + idDelete);
                        conn.close();

                        loadTable(model);

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Gagal menghapus!");
                    }
                });

                setVisible(true);
            }

            //METHOD UNTUK LOAD TABEL DARI DB
            static void loadTable(DefaultTableModel model) {
                model.setRowCount(0);

                try {
                    Connection conn = DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/gym_management", "root", "");

                    String sql =
                        "SELECT p.id_pendaftaran, m.nama AS member, k.nama_kelas, p.tgl_daftar, p.catatan " +
                        "FROM pendaftaran_kelas p " +
                        "JOIN member_gym m ON p.id_member = m.id_member " +
                        "JOIN jadwal_kelas k ON p.id_kelas = k.id_kelas";

                    ResultSet rs = conn.createStatement().executeQuery(sql);

                    while (rs.next()) {
                        model.addRow(new Object[]{
                                rs.getInt("id_pendaftaran"),
                                rs.getString("member"),
                                rs.getString("nama_kelas"),
                                rs.getString("tgl_daftar"),
                                rs.getString("catatan")
                        });
                    }

                    conn.close();

                } catch (Exception e) {
                    System.out.println("Gagal load tabel");
                }
            
        }
}
