import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Pruebas extends JFrame implements ActionListener, java.awt.event.ActionListener {

    private static final long serialVersionUID = 1L;
    private JLabel labelTiempo;
    private Timer timer;
    private int contadorTiempo;

    public Pruebas() {
        super("Cronómetro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crea el JLabel para mostrar el tiempo
        labelTiempo = new JLabel("Tiempo: 00:00:00");
        labelTiempo.setFont(new Font("Verdana", Font.BOLD, 20));
        labelTiempo.setHorizontalAlignment(JLabel.CENTER);
        add(labelTiempo, BorderLayout.CENTER);

        // Crea el Timer para contar el tiempo
        timer = new Timer(1000, this);

        // Configura la ventana
        setSize(300, 150);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        contadorTiempo++;
        int horas = contadorTiempo / 3600;
        int minutos = (contadorTiempo % 3600) / 60;
        int segundos = contadorTiempo % 60;
        String tiempo = String.format("Tiempo: %02d:%02d:%02d", horas, minutos, segundos);
        labelTiempo.setText(tiempo);
    }

    public void iniciarCronometro() {
        timer.start();
    }

    public static void main(String[] args) {
        Pruebas cronometro = new Pruebas();
        cronometro.iniciarCronometro();
    }
}