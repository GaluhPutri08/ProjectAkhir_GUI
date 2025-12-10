package Form;
import javax.swing.*;

public class MainMenu {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Menu Utama - Manajemen Gym");
        frame.setSize(400, 450);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        JLabel lblTitle = new JLabel("SISTEM MANAJEMEN GYM", SwingConstants.CENTER);
        lblTitle.setBounds(0, 20, 400, 40);
        frame.add(lblTitle);

        JButton btnMember = new JButton("Registrasi Member");
        btnMember.setBounds(70, 80, 250, 40);
        frame.add(btnMember);

        JButton btnInstruktur = new JButton("Data Instruktur");
        btnInstruktur.setBounds(70, 140, 250, 40);
        frame.add(btnInstruktur);

        JButton btnJadwal = new JButton("Jadwal Kelas");
        btnJadwal.setBounds(70, 200, 250, 40);
        frame.add(btnJadwal);

        JButton btnDaftar = new JButton("Form Pendaftaran Kelas");
        btnDaftar.setBounds(70, 260, 250, 40);
        frame.add(btnDaftar);

        JButton btnKeluar = new JButton("Keluar");
        btnKeluar.setBounds(70, 320, 250, 40);
        frame.add(btnKeluar);

        // ---- Action Listener ----
        btnMember.addActionListener(e -> new FormRegistrasiMember().setVisible(true));
        btnInstruktur.addActionListener(e -> new FormInstruktur().setVisible(true));
        btnJadwal.addActionListener(e -> new FormJadwalKelas().setVisible(true));
        btnDaftar.addActionListener(e -> new FormPendaftaran().setVisible(true));
        btnKeluar.addActionListener(e -> System.exit(0));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
