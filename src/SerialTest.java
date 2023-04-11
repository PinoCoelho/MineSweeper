import java.io.InputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import gnu.io.*;

public class SerialTest implements SerialPortEventListener {
    SerialPort serialPort;
    private static final String PORT_NAME = "COM4"; // Cambiar el nombre del puerto según corresponda
    private static final int TIME_OUT = 2000;
    private static final int DATA_RATE = 9600;

    private InputStream input;

    public void initialize() {
        CommPortIdentifier portId = null;
        Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers(PORT_NAME);

        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = portEnum.nextElement();
            if (currPortId.getName().equals(PORT_NAME)) {
                portId = currPortId;
                break;
            }
        }

        if (portId == null) {
            System.out.println("No se puede conectar al puerto " + PORT_NAME);
            return;
        }

        try {
            serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            input = serialPort.getInputStream();

            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (PortInUseException | UnsupportedCommOperationException | TooManyListenersException e) {
            System.err.println(e.toString());
        }
    }

    public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    @Override
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                String inputLine = input.readLine();
                System.out.println(inputLine);
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
    }
}
