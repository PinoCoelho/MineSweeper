import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.util.TooManyListenersException;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Pruebas extends JFrame implements ActionListener, SerialPortEventListener {

    private static final String PORT_NAME = "COM4"; // Cambiar según el puerto de tu Arduino
    private static final int BAUD_RATE = 9600;

    private JButton buttonOn;
    private JButton buttonOff;
    private SerialPort serialPort;

    public Pruebas() {
        super("Control de LED con Arduino");

        // Configurar botones
        buttonOn = new JButton("Encender");
        buttonOn.addActionListener(this);
        buttonOff = new JButton("Apagar");
        buttonOff.addActionListener(this);

        // Agregar botones a la ventana
        add(buttonOn);
        add(buttonOff);

        // Configurar la ventana
        setLayout(new FlowLayout());
        setSize(300, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Configurar el puerto serie
        try {
            CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(PORT_NAME);
            serialPort = (SerialPort) portId.open("ArduinoLED", 2000);
            serialPort.setSerialPortParams(BAUD_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (NoSuchPortException | PortInUseException | UnsupportedCommOperationException | TooManyListenersException ex) {
            System.err.println("Error al abrir el puerto serie: " + ex.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == buttonOn) {
            // Encender el LED enviando el carácter '1' al Arduino
            try {
                OutputStream outputStream = serialPort.getOutputStream();
                outputStream.write('1');
                outputStream.flush();
            } catch (IOException ex) {
                System.err.println("Error al enviar datos al Arduino: " + ex.getMessage());
            }
        } else if (event.getSource() == buttonOff) {
            // Apagar el LED enviando el carácter '0' al Arduino
            try {
                OutputStream outputStream = serialPort.getOutputStream();
                outputStream.write('0');
                outputStream.flush();
            } catch (IOException ex) {
                System.err.println("Error al enviar datos al Arduino: " + ex.getMessage());
            }
        }
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        // No es necesario hacer nada aquí, pero debemos implementar el método
    }

    public static void main(String[] args) {
        new Pruebas();
    }
}