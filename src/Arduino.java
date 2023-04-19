import org.firmata4j.firmata.*;
import org.firmata4j.IODevice;
import org.firmata4j.Pin;
import java.io.IOException;

/**
 * Toda esta clase es la original del Arduino, como se tenía planeado hacer, si quiere puede meter los Thread aquí para probar
 */
public class Arduino {
    
    static final String USBPORT = "COM4"; // puerto usb donde esta conectado el arduino
    
    static final int LED = 8; // pin donde esta el LED
    static final int Buzzer = 9; // pin donde esta el LED

    static IODevice myArduino = new FirmataDevice(USBPORT);

    Pin RedLED;
    Pin BuzzerPin;
    
    
    /** 
     * @param args
     * @throws IOException
     * @throws InterruptedException
     */
    public Arduino() throws IOException, InterruptedException {
        //IODevice myArduino = new FirmataDevice(USBPORT); 
        try { // se empieza el arduino
            
            myArduino.start(); 
            myArduino.ensureInitializationIsDone();
            System.out.println("El arduino se ha conectado");
            
        }
        catch (IOException ioexception) { // si no se logra conectar, da este error
            System.out.println("Trouble connecting to board");
        }
        finally { // aqui se pone el codigo que se quiera correr en el arduino
            
            RedLED = myArduino.getPin(LED); // se asigna el pin a una variable
            RedLED.setMode(Pin.Mode.OUTPUT); // se le asigna si es INPUT o OUTPUT, INPUT para botones, OUTPUT para LEDS y buzzer
            BuzzerPin = myArduino.getPin(Buzzer);
            BuzzerPin.setMode(Pin.Mode.PWM);
            
        }
    }
    /**
     * Metodo para hacer que se prenda el led 
     */
    public void led ()
    {
        try {
            RedLED.setValue(1); // Se enciende el LED
            Thread.sleep(500); // Se espera medio segundo
            RedLED.setValue(0); // Se apaga el LED
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Metodo para que el buzzer haga un sonido más bajo cuando se presiona una celda sin mina
     * @throws IllegalStateException
     * @throws IOException
     * @throws InterruptedException
     */
    public void buzzerBajo () throws IllegalStateException, IOException, InterruptedException {
        BuzzerPin.setValue(1); // send 1KHz sound signal to the buzzer
        Thread.sleep(500); // wait for 1 second
        BuzzerPin.setValue(0);
    }
    /**
     * Metodo para que el buzzer haga un sonido más alto si se presiona una mina
     * @throws IllegalStateException
     * @throws IOException
     * @throws InterruptedException
     */
    public void buzzerAlto () throws IllegalStateException, IOException, InterruptedException {
        BuzzerPin.setValue(1005); // send 1KHz sound signal to the buzzer
        Thread.sleep(1500); // wait for 1 second
        BuzzerPin.setValue(0);
    }
    
}