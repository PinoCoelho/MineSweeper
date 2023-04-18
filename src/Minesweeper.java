import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JFrame;

//import org.firmata4j.firmata.*;
//import org.firmata4j.IODevice;
//import org.firmata4j.Pin;
//import java.io.IOException;

/**
 * Clase principal que contiene todo el juego MineSweeper
 */
public class Minesweeper implements ActionListener, java.awt.event.ActionListener 
{
    //Creación de las variables que se van a mostrar en pantalla
	JFrame frame;
	JPanel textPanel;
	JPanel buttonPanel;
    int[][] solution;
	JButton[][] buttons;
    boolean[][] flagged;
    JButton resetButton;
    JButton flag;	
    JButton advancedButton;
    JButton sugeButton;
	JLabel textfield;
    JLabel textfield_minas;
    JLabel timerfield;
    JPanel textPanel2;


    Random random; //Variable random

    Boolean level = false;

    //Listas enlazadas del Advanced Level
    LinkedList ListaGeneral = new LinkedList();
    LinkedList ListaSegura = new LinkedList();
    LinkedList ListaIncertidumbre = new LinkedList();

    //Variables de la pila
    Stack pila = new Stack();
    int contadorPila = 0;

    //Variables del contador
    Timer timer;
    int contadorTiempo;

    int mina = 0;
    Boolean turnojugador = true;

    //Variable del Arduino
    //Arduino arduino = new Arduino();

    
    int size; //Variable del tamaño del tablero
    int bombs; //Variable de la cantidad de minas

    // Variables para tener la posición de las minas cuando se coloquen de forma random
    ArrayList<Integer> xPositions;
    ArrayList<Integer> yPositions;


    boolean flagging; //Variable para saber si hay una bandera puesta en el tablero
    int count = 0;
	int lastXchecked;
	int lastYchecked;
    int xZero;
    int yZero;

    //Arduino arduino = new Arduino();

    //String USBPORT = "COM4"; // Variable con el puerto USB donde está conectado el arduino
    
    //IODevice myArduino = new FirmataDevice(USBPORT);  //Variable para iniciar Firmata con mi arduino

    //Variables donde están los botones y el led del arduino
    /* 
    int ButtonSel = 2; // Variable con el pin donde esta el boton seleccionar
    int ButtonRight = 4; // Variable con el pin donde esta el boton derecho
    int ButtonLeft = 7; // Variable con el pin donde esta el boton izquierdo
    int ButtonDown = 8; // Variable con el pin donde esta el boton abajo 
    int ButtonUp = 12; // Variable con el pin donde esta el boton arriba
    int LED = 13; // Variable con el pin donde esta el LED
    
    //Variables con los pines que definimos arriba
    Pin RedLED;
    Pin buttonSel;
    Pin buttonRight;
    Pin buttonLeft;
    Pin buttonUp;
    Pin buttonDown;
    */

    /**
     * Este es el método constructor, aquí se construye todo lo que se muestra en pantalla o lo que se quiere que se inicie de un solo.
     */
    public Minesweeper()
    {
        /* 
        // Esto es para iniciar todo lo correspondiente al Arduino
        try { // Se empieza el arduino
            
            myArduino.start(); 
            myArduino.ensureInitializationIsDone();
            System.out.println("El arduino se ha conectado"); // Si se inicia, entonces sale este print
            
        }
        catch (Exception Exception) { //Si no se logra conectar, da este error
            System.out.println("Trouble connecting to board");
        }
        finally { // Aqui se pone el código que se quiera correr en el arduino
            
            try {
                RedLED = myArduino.getPin(LED); //Se asigna el pin a una variable
                RedLED.setMode(Pin.Mode.OUTPUT); //Se le asigna OUPUT, ya que es un LED
                
                buttonSel = myArduino.getPin(ButtonSel); //Se asigna el pin a una variable
                buttonSel.setMode(Pin.Mode.INPUT); //Se le asigna INPUT, ya que es un botón
                
                buttonRight = myArduino.getPin(ButtonRight); //Se asigna el pin a una variable
                buttonRight.setMode(Pin.Mode.INPUT); //Se le asigna INPUT, ya que es un botón
    
                buttonLeft = myArduino.getPin(ButtonLeft); //Se asigna el pin a una variable
                buttonLeft.setMode(Pin.Mode.INPUT); // Se le asigna INPUT, ya que es un botón
    
                buttonUp = myArduino.getPin(ButtonUp); //Se asigna el pin a una variable
                buttonUp.setMode(Pin.Mode.INPUT); // Se le asigna INPUT, ya que es un botón
    
                buttonDown = myArduino.getPin(ButtonDown); //Se asigna el pin a una variable
                buttonDown.setMode(Pin.Mode.INPUT); //Se le asigna INPUT, ya que es un botón
            }
            // Si no puede hacer el try, entonces se mete al catch
            catch (IOException IOException) {
                System.out.print("Error setting up Pins");
            }
        

        }
        */

        // Posiciones guardadas en un array
        xPositions = new ArrayList<Integer>();
        yPositions = new ArrayList<Integer>();

        size = 8; //Tamaño de la matriz
        bombs = 10; //Cantidad de bombas

        lastXchecked = size + 1; //Se pone 9 al número de la bomba para diferenciar, en la posición X
        lastYchecked = size + 1; //Se pone 9 al número de la bomba para diferenciar, en la posición Y

        flagged = new boolean [size][size]; //Si tiene bandera la posición

        flagging = true;

        timer = new Timer(1000, this); //Se inicializa el contador

        random = new Random();  //Se inicializa el random

        solution = new int[size][size]; //Se crea la variable solution como una matriz para almacenar toda la matriz del juego
        buttons = new JButton[size][size]; //Creación de una variable de botones

        for(int i = 0; i < bombs; i++) //Coloca las bombas de forma random en la matriz
        {
            xPositions.add(random.nextInt(size));
            yPositions.add(random.nextInt(size));
        }
        for(int i = 0; i < bombs; i++) //Por si se pone dos bombas en un mismo lugar
        {
            for(int j = i + 1; j < bombs; j++)
            {
                if(xPositions.get(i) == xPositions.get(j) && yPositions.get(i) == yPositions.get(j)) //Verifica si están en el mismo lugar
                {
                    // Las vuelve a poner de forma random en algún lugar
                    xPositions.set(j, random.nextInt(size));
					yPositions.set(j, random.nextInt(size));
					i = 0;
					j = 0;
                }
            }
        }
        /*
        * for (int i = 0; i < bombs; i++)
        {
            System.out.println("xPosition of "+ i + " is "+ xPositions.get(i));
            System.out.println("yPosition of "+ i + " is "+ yPositions.get(i));
        }
        */
        
        //Se crea la pantalla a mostrar
        frame = new JFrame();
		frame.setVisible(true);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

        //Se crea un panel de texto en la parte de arriba
        textPanel = new JPanel();
		textPanel.setVisible(true);
		textPanel.setBackground(Color.WHITE);
        textPanel.setLayout(new GridLayout(1, 3));

        //Se crea un panel de texto en la parte de abajo
        textPanel2 = new JPanel();
		textPanel2.setVisible(true);
		textPanel2.setBackground(Color.WHITE);
        textPanel2.setLayout(new GridLayout(1, 3));

        //Se crea un botón en el panel
        buttonPanel = new JPanel();
		buttonPanel.setVisible(true);
		buttonPanel.setLayout(new GridLayout(size,size));

        //Se crea el label del cronómetro
        timerfield = new JLabel();
		timerfield.setHorizontalAlignment(JLabel.LEFT);
		timerfield.setFont(new Font("MV Boli",Font.BOLD,20));
		timerfield.setForeground(Color.GREEN);
		timerfield.setText("Tiempo: 00:00:00");

        //Se crea una campo de texto
        textfield = new JLabel();
		textfield.setHorizontalAlignment(JLabel.LEFT);
		textfield.setFont(new Font("MV Boli",Font.BOLD,20));
		textfield.setForeground(Color.GREEN);
		textfield.setText(bombs + " Bombs");

        //Se crea el label para mostrar las minas encontradas
        textfield_minas = new JLabel();
        textfield_minas.setBounds(-500, 50, 100, 30);
		textfield_minas.setFont(new Font("MV Boli",Font.BOLD,20));
		textfield_minas.setForeground(Color.GREEN);
		textfield_minas.setText("Encontradas: ");

        //Se crea el botón Advanced Level para jugar
        advancedButton = new JButton();
		advancedButton.setForeground(Color.WHITE);
		advancedButton.setText("Advanced");
		advancedButton.setFont(new Font("MV Boli", Font.BOLD, 10));
		advancedButton.setBackground(Color.BLACK);
		advancedButton.setFocusable(false);
		advancedButton.addActionListener(this);

        //Se crea el botón Sugerencia
        sugeButton = new JButton();
		sugeButton.setForeground(Color.BLUE);
		sugeButton.setText("Sugerencia");
		sugeButton.setFont(new Font("MV Boli", Font.BOLD, 20));
		sugeButton.setBackground(Color.GREEN);
		sugeButton.setFocusable(false);
		sugeButton.addActionListener(this);

        //Se crea el botón resetear
        resetButton = new JButton();
		resetButton.setForeground(Color.BLUE);
		resetButton.setText("Reset");
		resetButton.setFont(new Font("MV Boli", Font.BOLD, 20));
		resetButton.setBackground(Color.WHITE);
		resetButton.setFocusable(false);
		resetButton.addActionListener(this);
       

        MouseListener mouseListener = new MouseAdapter() {
            /**
             * Metodo para obtener el click del botón derecho del mouse
             */
            public void mousePressed(MouseEvent e) {
                int modifiers = e.getModifiersEx();
                if ((modifiers & InputEvent.BUTTON3_DOWN_MASK) == InputEvent.BUTTON3_DOWN_MASK) { //Si se presiona el botón derecho
                    if (e.getSource() == resetButton)
                    {
                        frame.dispose();
                        new Minesweeper();
                    }
                    //Recorre la matriz
                    for(int i=0;i<buttons.length;i++)
                    {
                        for(int j = 0; j < buttons[0].length; j++)
                        {
                            if(e.getSource() == buttons[i][j]) //Si se presiona cualquier botón
                            {

                                if (flagging)
                                {
                                    if (flagged[i][j])
                                    {
                                        buttons[i][j].setText("");
                                        buttons[i][j].setBackground(null);
                                        flagged[i][j] = false;
                                    }
                                    else
                                    {
                                        buttons[i][j].setText("|>");
                                        buttons[i][j].setBackground(Color.RED);
                                        buttons[i][j].setForeground(Color.ORANGE);
                                        flagged[i][j] = true;
                                        mina++;
                                        textfield_minas.setText("Encontradas: " + String.valueOf(mina));
                                        

                                    }
                                }
                                else
                                {
                                    if (!flagged[i][j])
                                        check(i,j); //Llama la función check para ver si ganó o se perdió   
                                }
                            }
                            
                        }
                    }
                }
            }
        };

        //Se hará la matriz de 8x8 en forma de botones
		for(int i = 0; i < buttons.length; i++)
		{
			for(int j = 0; j < buttons[0].length; j++)
			{
                //Se empieza a ubicar los botones en la pantalla
				buttons[i][j] = new JButton();
				buttons[i][j].setFocusable(false);
				buttons[i][j].setFont(new Font("MV Boli",Font.BOLD,12));
				buttons[i][j].addActionListener(this);
				buttons[i][j].setText("");
                buttons[i][j].addMouseListener(mouseListener);
				buttonPanel.add(buttons[i][j]);
			}
		}
        //Implementación en la pantalla de las variables creadas
        textPanel.add(textfield);
        textPanel.add(timerfield);
		frame.add(buttonPanel);
		frame.add(textPanel, BorderLayout.NORTH);
        frame.add(textPanel2, BorderLayout.SOUTH);
        textPanel2.add(resetButton);
        textPanel2.add(advancedButton);
        textPanel2.add(sugeButton);
        textPanel.add(textfield_minas);
        

        //Dimensiones de la pantalla, para actualizar la pantalla y centrarla
        frame.setSize(700,700);
        frame.revalidate();
        frame.setLocationRelativeTo(null);

        getSolution(); //Se llama a la función

    }
    /**
     * Metodo que lo que hace es obtener las posiciones de la matriz, hace que ponga el número a los botones en caso de tener minas alrededor
     */
    public void getSolution() //Función para ver cuantas bombas hay al rededor de un botón y lo mete en una doble lista enlazada
    {
        //Recorre la matriz
        for(int y=0; y<solution.length; y++)
        {
            for (int x = 0; x < solution.length; x++)
            {
                boolean changed = false;
                int bombsAround = 0;

                for(int i = 0; i < xPositions.size(); i++) //Si es menor a la posición de la bomba entonces se mete
                {
                    if(x == xPositions.get(i) && y == yPositions.get(i)) //Si es igual a la posición de la bomba
                    {
                        solution[y][x] = size + 1; //Guarda la bomba como el tamaño + 1, o sea 9
                        changed = true;
                        ListaGeneral.addNode(x, y, size + 1); //Lo añade a la lista general como 9 para referenciar a la mina, en la posición de nBombs
                    }
                }
                if(!changed) //Si changed cambió entonces se mete aquí
                {
                    for(int i = 0; i < xPositions.size(); i++) // Se recorre la matriz
                    {
                        if(x-1 == xPositions.get(i) && y == yPositions.get(i)) // Se verifica si las posiciones alrededor son una bomba o no. Verifica las cordenadas de todas las 8 posiciones
                          bombsAround++;
                        if(x+1 == xPositions.get(i) && y == yPositions.get(i)) //Una posición por delante
                            bombsAround++; 
                        if(x == xPositions.get(i) && y-1 == yPositions.get(i)) //Una posición arriba
                            bombsAround++;
                        if(x == xPositions.get(i) && y+1 == yPositions.get(i)) // Una posición por debajo
                            bombsAround++; 
                        if(x+1 == xPositions.get(i) && y+1 == yPositions.get(i)) // Una posición abajo a la derecha
                            bombsAround++;
                        if(x-1 == xPositions.get(i) && y-1 == yPositions.get(i)) // Una posición arriba a la izquierda
                            bombsAround++;
                        if(x-1 == xPositions.get(i) && y+1 == yPositions.get(i)) //Una posición abajo a la izquierda
                            bombsAround++;
                        if(x+1 == xPositions.get(i) && y-1 == yPositions.get(i)) // Una posición arriba a la derecha
                            bombsAround++;
                    }
                    solution[y][x] = bombsAround; //Pone los números de las bombas alrededor y de las minas
                    ListaGeneral.addNode(y, x, bombsAround); //Añade el resto a la lista General
                }
            }
            
        }
        //Función para imprimir la matriz
        for(int i = 0; i < solution.length; i++)
            {
                for(int j = 0; j < solution[0].length; j++)
                    System.out.print(solution[i][j] + " ");
                System.out.println();
            }
    }
    /**
     * Metodo para borrar las celdas que ya se han abierto
     */
    public void borrarCeldas ()
    {
        ListNode temp = ListaGeneral.head;
        while (temp != null) { //Mientras temp no sea nulo
            ListNode next = temp.next;
            if (buttons[temp.xList][temp.yList].getText() != "") //Se pone el largo al que se quiere llegar
            {
                ListaGeneral.deleteNode(temp.xList, temp.yList); //Se elimina de la lista General
            } 
            temp = next;
            }
    }
    /**
     * Metodo del advanced level para imprimir las listas en consola, cambiar las los nodos seguros a la lista Segura y donde haya mina a la incertidumbre
     */
    public void advancedLevel()
    {
        if (ListaGeneral.head != null) //Si es diferente de null
        {
            int xSize = random.nextInt(ListaGeneral.getSize()); //Agarra un número random del tamaño de la lista general
        
            if (ListaGeneral.getNodeAtPosition(xSize).nBombs == 9) //Si en la posición a la hora de agarrar el nodo, en nBombs es igual a 9, etonces hay mina
            {
                ListNode temporal = ListaGeneral.getNodeAtPosition(xSize); 
                ListaIncertidumbre.addNode(temporal.xList, temporal.yList, temporal.nBombs); //Se agrega a la lista incertidumbre
                ListaGeneral.deleteNode(temporal.yList, temporal.xList); //Se elimina de la Lista General
                System.out.print("Lista general: "); 
                ListaGeneral.printList(); //Se imprime en consola el estado de la lista General
                System.out.print("Lista Incertidumbre: ");
                ListaIncertidumbre.printList(); //Se imprime en consola el estado de la Lista Incertidumbre
                advancedLevel(); //Se llama de nuevo a AdvancedLevel
            }
            else //Si no es una mina entonces se mete aquí
            {
                ListNode temporal2 = ListaGeneral.getNodeAtPosition(xSize);
                ListaSegura.addNode(temporal2.xList,temporal2.yList, temporal2.nBombs); //Se agrega a lista Segura
                ListaGeneral.deleteNode(temporal2.yList, temporal2.xList); //Se elimina de ListaGenera
                System.out.print("Lista general: ");
                ListaGeneral.printList(); //Se imprime en consola la Lista General
                System.out.print("Lista Segura: ");
                ListaSegura.printList(); //Se imprime en consola la Lista Segura
                turnocomputadoraAdvanced(); //Se llama a turnocomputadoraAdvanced
            }
        }
        else
        {

        }
    }
    /**
     * Metodo para agregar la SUgerencia a la pila
     */
    public void agregarSug()
    {
        ListNode temp = ListaGeneral.obtenerNodo();
        pila.push(temp.xList, temp.yList, temp.nBombs); //Se agrega a la pila
    }
    
    /** 
     * Metodo para verificar si se ganó o se perdió
     * @param y
     * @param x
     */
    public void check(int y, int x) //Función par detectar si ganó o perdió
    {
        boolean over = false;

        if(solution[y][x] == (size + 1)) //Si tocó una mina entonces se mete aquí
		{
        gameOver(false); //Llama la función cuando pierde
		over = true;
		}
		
		if(!over) //Si ganó entonces se mete aquí
		{
            getColor(y, x); //Llama a la función para poner colores a los botones

            if (solution[y][x] == 0)
            {
                xZero = x;
                yZero = y;

                count = 0;


                display(); //Llama la función para revelar los botones

            } 
            checkWinner(); //Llama a la función para verificar si se gana
        }
    }
    /**
     * Metodo cuando se gana en el juego
     */
    public void checkWinner() //Función cuando se gana
	{
		int buttonsleft = 0;
		
        //Recorre la matriz
		for(int i = 0; i < buttons.length; i++)  
		{
			for(int j = 0; j < buttons[0].length; j++)
			{
				if(buttons[i][j].getText() == "") //Si ya se reveló
					buttonsleft++; //Aumenta 1 en la variable
			}
		}
		if(buttonsleft == bombs) //Si es igual al número de bombas
			gameOver(true); //Llama a la función de perdida con el booleano en true
	}
    /**
     * Metodo para cuando se pierde o se gana en el juego
     * @param won
     */
    public void gameOver(boolean won) //Función de que terminó el juego
    {
        if(!won) //Si el booleano es igual a false
        {
            textfield.setForeground(Color.RED);
            textfield.setText("Terminó el juego"); //Manda este mensaje
            timer.stop(); //Se detiene el timer
        }
        else //Si es igual a true
        {
            textfield.setForeground(Color.GREEN);
            textfield.setText("GANASTE"); //manda este mensaje
            timer.stop(); //Se detiene el timer
        }
        //Se recorre la matriz
        for(int i = 0; i < buttons.length; i++)
        {
            for(int j = 0; j < buttons[0].length; j++)
            {
                buttons[i][j].setBackground(null);
                buttons[i][j].setEnabled(false); //Cuando recorre toda la matriz, hace que no se pueda jugar más

                for (int count = 0; count < xPositions.size(); count++)
                {
                    if (j == xPositions.get(count) && i == yPositions.get(count)) //Si se presiona una mina
                    {
                        buttons[i][j].setBackground(Color.BLACK);
                        buttons[i][j].setText("*"); //Se cambia el texto del botón
                        timer.stop(); //Se para el timer
                    }
                }
            }
        }
    }

    /**
     * Metodo que nos ayuda a poder saber cuando un botón está siendo presionado
     */
    @Override
    public void actionPerformed(ActionEvent e) //Función que sirve para funciones de eventos al dar click.
    {
        //Se crea el cronómetro
        contadorTiempo++;
        int horas = contadorTiempo / 3600; //Variable con las horas
        int minutos = (contadorTiempo % 3600) / 60; //Variable con los minutos
        int segundos = contadorTiempo % 60; //Variable con los segundos
        String tiempo = String.format("Tiempo: %02d:%02d:%02d", horas, minutos, segundos); //Se le da el formato
        timerfield.setText(tiempo); //Se cambia el texto a la variable
        iniciarCronometro(); //Se inicia el timer
        

        //Para resetear
        if (e.getSource() == resetButton) //Si se presiona el botón
        {
            /* 
            try {
                myArduino.stop(); //Se para el arduino
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            */
            frame.dispose(); //Se para el frame
            new Minesweeper(); //Se vuelve a llamar al contructor para que ejecute tra vez todo
            iniciarCronometro(); //Se inicia el cronómetro
            
        }
        if (e.getSource() == sugeButton) // Si se presiona el botón de sugerencia
        {
            mostrarSug(); //Se llama a la función para mostrar la sugerencia

            /* 
            try {
                arduino(0, 0);
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            */
        }

        if (e.getSource() == advancedButton) // Si se presiona el botón AdvancedLevel
        {
            System.out.println("Advanced Level TRUE");
            level = true; //Se vuelve true la variable level para saber que se está jugando en advanced
        }
        if (turnojugador == true) //Si es el turno del jugador
        {
            //Recorre la matriz
            for(int i = 0; i < buttons.length; i++)
            {
                for(int j = 0; j < buttons[0].length; j++)
                {
                    if(e.getSource() == buttons[i][j]) //Si se presiona cualquier botón
                    {
                        if (!flagged[i][j])
                            check(i,j); //Llama la función check para ver si ganó o se perdió   
                            turnojugador = false; //Se convierte en false el turno del jugador para que juegue la compu
                            ListaGeneral.deleteNode(i, j); //Se elimina el nodo que seleccioné
                            contadorPila++; //Se incremeta en 1 el contador de la Pila
                            if (contadorPila == 5) //Si el contador de la pila llega a 5
                            {
                                agregarSug(); //Agrega la sugerencia a la pila
                                contadorPila = 0; //Se reinicia el contador
                            }
                    }
                            
                }
            }
        }
        else if(turnojugador == false && level == true) //Si el turno del jugador es false y el level es true
        {
            borrarCeldas();
            advancedLevel(); //Se llama aadvancedLevel para que imprimi las listas y juegue
            turnojugador = true; //Y se vuelve a activar el turno del jugador
        }
        else if (turnojugador == false && level == false) //Si todo es falso
        {
            turnocomputadora(); //Se llama al turnocomputadora
            turnojugador = true; //Y el turnojugador se vuelve a activar
        }
    }
    /**
     * Metodo para mostrar la sugerencia, si es que hay o ya se han jugado más de 5 veces
     */
    public void mostrarSug()
    {
        if (pila.estaVacia() == true) //Si está vacía la pila
        {
            //Muestra este mensaje
            JOptionPane.showMessageDialog(null, "No hay sugerencias", "Sugerencia vacía", JOptionPane.ERROR_MESSAGE);
        }    
        else //Si no está vacía
        {
            ListNode temp = pila.pop();
            System.out.println("Fila: " + (7 - temp.xList) + "\n Columna: " + (7 - temp.yList)); //Muestra este mensaje
            if (buttons[7 - temp.xList][7 - temp.yList].getText() == "") //Si el botón ya se reveló
            {
                System.out.println(buttons[7 - temp.xList][7 - temp.yList]);
                JOptionPane.showMessageDialog(null, "Fila: " + (7 - temp.xList) + "\n Columna: " + (7 - temp.yList) , "Sugerencia", JOptionPane.ERROR_MESSAGE); //Muestra este mensaje
            }
            else //Si no se ha revelado
            {
                mostrarSug(); //LLama a la función otra vez
            }
            
        }
    }
    /**
     * Metodo cuando es el turno de la computadora en modo advanced
     */
    public void turnocomputadoraAdvanced()
    {
        borrarCeldas();
        ListNode listaCompu;
        if (ListaSegura.head != null) //Si no está vacía la lista segura
        {
            listaCompu = ListaSegura.head; //Juega el nodo de Lista Segura
            ListaSegura.head = ListaSegura.head.next;
        }
        else //Si está vacía
        {
            listaCompu = ListaIncertidumbre.head; //Juega el nodo de Lista Incertidumbre
            ListaIncertidumbre.head = ListaIncertidumbre.head.next;
        }

        //Variables para obtener las posiciones en x y y
        int x = listaCompu.xList;
        int y = listaCompu.yList;

        if (!flagged[x][y]) //Si no hay bandera
        {
            check(x,y); //Llama la función check para ver si ganó o se perdió   
            borrarCeldas(); //Borra las celdas
        }
    }
    /**
     * Metodo que contiene el turno de la computadora en modo DummyLevel
     */
    public void turnocomputadora()
    {
        //Se crean variables con números randoms en x y y menores a 8
        int x = random.nextInt(8);
        int y = random.nextInt(8);
        if (!flagged[x][y]) //Si no hay bandera
        {
            check(x,y); //Llama la función check para ver si ganó o se perdió
            borrarCeldas(); //Borra las celdas ya jugadas
        }
        else
        {
            turnocomputadora(); //Se vuelve a llamar
        }

    }
    /**
     * Metodo que contiene toda las funciones para revelar los botones cada vez que se presionan
     */
    public void display ()
    {
        if (count < 1) //Si el contador es menor a 1
        {
            if((xZero - 1) >= 0)
				getColor(yZero, xZero - 1); //Llama a la función getColor para modificar su color
			if((xZero + 1) < size)
				getColor(yZero, xZero + 1); //Llama a la función getColor para modificar su color
			if((yZero - 1) >= 0) 
				getColor(yZero - 1, xZero); //Llama a la función getColor para modificar su color
			if((yZero + 1) < size) 
				getColor(yZero + 1, xZero); //Llama a la función getColor para modificar su color
            if((yZero - 1) >= 0 && (xZero - 1) >= 0)
				getColor(yZero - 1, xZero - 1); //Llama a la función getColor para modificar su color
			if((yZero + 1) < size && (xZero + 1) < size)
				getColor(yZero + 1, xZero + 1); //Llama a la función getColor para modificar su color
            if((yZero - 1) >= 0 && (xZero + 1) < size)
				getColor(yZero - 1, xZero + 1); //Llama a la función getColor para modificar su color
			if((yZero + 1) < size && (xZero - 1) >= 0)
				getColor(yZero + 1, xZero - 1); //Llama a la función getColor para modificar su color

            count++;
            display();
        }
        else
		{
            //Se recorre la matriz
			for(int y=0; y<buttons.length; y++)
			{
				for(int x=0; x<buttons[0].length; x++)
				{
					if(buttons[y][x].getText().equals("0")) //Si el texto dentro del botón es 0
					{
                        //Toda esta funciónes y if anidados es para saber si alrededor de esos ceros hay más ceros, si los hay se revelan
						if(y-1>=0)
						{
                            //Y estas funciones es para saber si los botones al rededor ya están abiertos o tienen banderas
							if(buttons[y-1][x].getText().equals("") || buttons[y-1][x].getText().equals("1>"))
							{
								lastXchecked=x;
								lastYchecked=y;
							}
						}
						if(y+1<size)
						{
							if(buttons[y+1][x].getText().equals("") || buttons[y+1][x].getText().equals("1>"))
							{
								lastXchecked=x;
								lastYchecked=y;
							}
						}
						if(x-1>=0)
						{
							if(buttons[y][x-1].getText().equals("") || buttons[y][x-1].getText().equals("1>"))
							{
								lastXchecked=x;
								lastYchecked=y;
							}
						}
						if(x+1<size)
						{
							if(buttons[y][x+1].getText().equals("") || buttons[y][x+1].getText().equals("1>"))
							{
								lastXchecked=x;
								lastYchecked=y;
							}
						}
						if(x-1>=0 && y-1>=0)
						{
							if(buttons[y-1][x-1].getText().equals("") || buttons[y-1][x-1].getText().equals("1>"))
							{
								lastXchecked=x;
								lastYchecked=y;
							}
						}
						if(x+1<size && y+1<size)
						{
							if(buttons[y+1][x+1].getText().equals("") || buttons[y+1][x+1].getText().equals("1>"))
							{
								lastXchecked=x;
								lastYchecked=y;
							}
						}
						if(x+1<size && y-1>=0)
						{
							if(buttons[y-1][x+1].getText().equals("") || buttons[y-1][x+1].getText().equals("1>"))
							{
								lastXchecked=x;
								lastYchecked=y;
							}
						}
						if(x-1>=0 && y+1<size)
						{
							if(buttons[y+1][x-1].getText().equals("") || buttons[y+1][x-1].getText().equals("1>"))
							{
								lastXchecked=x;
								lastYchecked=y;
							}
						}
					}
				}
			}			
			if(lastXchecked < size + 1 && lastYchecked < size + 1) //Si no es una mina
			{				
                //Se agrega a estas variables
				xZero = lastXchecked; 
				yZero = lastYchecked;
				
				count = 0; //Contador vuelve a 0
				
                //Las minas se agregan a estas variables
				lastXchecked = size + 1;
				lastYchecked = size + 1;

                ListaGeneral.deleteNode(lastXchecked, lastYchecked); //Se elimina de la lista General
				
				display();//Se vuelve a llamar al método		
			}
		}

	}
    /**
     * Metodo para cambiar de color los textos que revelan los botones
     * @param y
     * @param x
     */
    public void getColor (int y, int x)
    {
        //Todos estos if son para cambiar de color a cada uno de los número que salgan en pantalla
        if(solution[y][x] == 0)
			buttons[y][x].setEnabled(false);
		if(solution[y][x] == 1)
			buttons[y][x].setForeground(Color.BLUE);
		if(solution[y][x] == 2)
			buttons[y][x].setForeground(Color.GREEN);
		if(solution[y][x] == 3)
			buttons[y][x].setForeground(Color.RED);
		if(solution[y][x] == 4)
			buttons[y][x].setForeground(Color.MAGENTA);
		if(solution[y][x] == 5)
			buttons[y][x].setForeground(new Color(128,0,128));
		if(solution[y][x] == 6)
			buttons[y][x].setForeground(Color.CYAN);
		if(solution[y][x] == 7)
			buttons[y][x].setForeground(new Color(42, 13, 93));
		if(solution[y][x] == 8)
			buttons[y][x].setForeground(Color.lightGray);
		
		buttons[y][x].setBackground(null);
		buttons[y][x].setText(String.valueOf(solution[y][x]));
	}
    /**
     * Metodo para iniciar el cronómetro
     */
    public void iniciarCronometro() {
        timer.start(); //Se inicia el cronómetro
    }
    
    /*
    public void arduino (int xAr, int yAr) throws IOException, InterruptedException
    {
         
        if (buttonSel.getValue() != 0) 
        { 
            System.out.println("Adiós");
        }
        else if (buttonRight.getValue() != 0) // si el boton en el pin 3 se presiona
        { 
            Thread.sleep(500);
            System.out.println("" + xAr + yAr);
            Thread.sleep(500); 
            buttons[yAr][xAr].setBackground(Color.GREEN);
            buttons[yAr][xAr].setText("Holi");
            if (xAr == 7)
            {
                xAr = 0;
            }
            else
            {
                xAr += 1;

            }
        }
        else if (buttonLeft.getValue() != 0)
        {
            System.out.println("Pene");
            if (xAr == 0)
            {
                xAr = 7;
            }
            else
            {
                xAr -= 1;
            }
        }
        else if (buttonUp.getValue() != 0)
        {
            System.out.println("Pene");
            if (yAr == 0)
            {
                yAr = 7;
            }
            else
            {
                yAr += 1;
            }
        }
        else if (buttonDown.getValue() != 0)
        {
            System.out.println("Pene");
            if (yAr == 7)
            {
                yAr = 0;
            }
            else
            {
                yAr -= 1;
            }
        }
        

        //Este es el loop infinito para que se esté ejecutando todo lo que quiero del arduino
        while(true) {
            if (buttonRight.getValue() != 0) { //Si se presiona el botón derecho
                System.out.println(xAr); //Para saber si se modifica la posición
                xAr++; //Se aumenta en 1 la posción
                Thread.sleep(1000); //Pausar la ejecución
                break;
            }
        }
        arduino(xAr+1, yAr+1); //Manda los valores
    }
    */
    
}