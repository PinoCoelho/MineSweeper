import org.firmata4j.firmata.*;
import org.firmata4j.IODevice;
import org.firmata4j.Pin;
import java.io.IOException;

/**
 * Toda esta clase es la original del Arduino, como se tenía planeado hacer, si quiere puede meter los Thread aquí para probar
 */
public class Arduino {
    
    static final String USBPORT = "COM4"; // puerto usb donde esta conectado el arduino
    
    static final int ButtonSel = 3; // pin donde esta el boton 
    static final int ButtonRight = 2; // pin donde esta el boton
    static final int LED = 7; // pin donde esta el LED
    
    
    /** 
     * @param args
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        IODevice myArduino = new FirmataDevice(USBPORT); 
        try { // se empieza el arduino
            
            myArduino.start(); 
            myArduino.ensureInitializationIsDone();
            System.out.println("El arduino se ha conectado");
            
        }
        catch (IOException ioexception) { // si no se logra conectar, da este error
            System.out.println("Trouble connecting to board");
        }
        finally { // aqui se pone el codigo que se quiera correr en el arduino
            
            Pin RedLED = myArduino.getPin(LED); // se asigna el pin a una variable
            RedLED.setMode(Pin.Mode.OUTPUT); // se le asigna si es INPUT o OUTPUT, INPUT para botones, OUTPUT para LEDS y buzzer
            
            Pin buttonSel = myArduino.getPin(ButtonSel); // se asigna el pin a una variable
            buttonSel.setMode(Pin.Mode.INPUT); // se le asigna si es INPUT o OUTPUT, INPUT para botones, OUTPUT para LEDS y buzzer
            
            Pin buttonRight = myArduino.getPin(ButtonRight); // se asigna el pin a una variable
            buttonRight.setMode(Pin.Mode.INPUT); // se le asigna si es INPUT o OUTPUT, INPUT para botones, OUTPUT para LEDS y buzzer

            
            while (true) {
                if (buttonSel.getValue() != 0) { // si el boton en el pin 2 se presiona
                    Thread.sleep(500);
                    System.out.println("Presiona");
                    RedLED.setValue(1);// se prende el LED
                    Thread.sleep(500); 
                }
                else if (buttonRight.getValue() != 0) { // si el boton en el pin 3 se presiona
                    System.out.println("Adios");
                    break; // se para el ciclo y se para el codigo 
                }
                else { // si no esta el boton en el pin 2 presionado 
                    RedLED.setValue(0); // se apaga el LED
                }
            }
            
            myArduino.stop(); // se para el arduino
            
        }
    }
    
}