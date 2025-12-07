import javax.swing.*;
import Form.*;

public class MainMenu {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Menu Utama - Manajemen Gym");
        frame.setSize(350, 300);
        frame.setLayout(null);

        JButton btn1 = new JButton("Form Member");
        JButton btn2 = new JButton("Form Instruktur");
        JButton btn3 = new JButton("Form Jadwal Kelas");
        JButton btn4 = new JButton("Form Pendaftaran");

        btn1.setBounds(70, 40, 200, 30);
        btn2.setBounds(70, 90, 200, 30);
        btn3.setBounds(70, 140, 200, 30);
        btn4.setBounds(70, 190, 200, 30);

        btn1.addActionListener(e -> new FormMember());
        btn2.addActionListener(e -> new FormInstruktur());
        btn3.addActionListener(e -> new FormJadwalKelas());
        btn4.addActionListener(e -> new FormPendaftaran());

        frame.add(btn1);
        frame.add(btn2);
        frame.add(btn3);
        frame.add(btn4);

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
