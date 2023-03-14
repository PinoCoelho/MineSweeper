import javax.swing.*;

//import org.w3c.dom.css.Counter;

import java.awt.*;
import java.awt.event.*;
//import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
//import javax.swing.SwingUtilities;

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
	JLabel textfield;

    Random random;


    // Variables que se usarán para la lógica
    int size;
    int bombs;

    ArrayList<Integer> xPositions;
    ArrayList<Integer> yPositions;

    boolean flagging;
    int count = 0;
	int lastXchecked;
	int lastYchecked;
    int xZero;
    int yZero;

    public Minesweeper() //Donde se creará toda la interfáz a mostrar
    {
        // Posiciones guardadas en un array
        xPositions = new ArrayList<Integer>();
        yPositions = new ArrayList<Integer>();

        size = 8; //Tamaño de la matriz
        bombs = 10; //Cantidad de bombas

        lastXchecked = size + 1;
        lastYchecked = size + 1;

        flagged = new boolean [size][size];

        flagging = true;

        random = new Random();
        for(int i=0; i<bombs;i++) //Coloca las bombas de forma random en la matriz
        {
            xPositions.add(random.nextInt(size));
            yPositions.add(random.nextInt(size));
        }
        for(int i=0;i<bombs;i++) //Por si se pone dos bombas en un mismo lugar
        {
            for(int j=i+1; j<bombs; j++)
            {
                if(xPositions.get(i)==xPositions.get(j) && yPositions.get(i)==yPositions.get(j)) //Verifica si están en el mismo lugar
                {
                    // Las vuelve a poner de forma random en algún lugar
                    xPositions.set(j, random.nextInt(size));
					yPositions.set(j, random.nextInt(size));
					i=0;
					j=0;
                }
            }
        }
        for (int i=0;i<bombs;i++)
        {
            System.out.println("xPosition of "+ i + " is "+ xPositions.get(i));
            System.out.println("yPosition of "+ i + " is "+ yPositions.get(i));
        }
        //Se crea la pantalla a mostrar
        frame=new JFrame();
		frame.setVisible(true);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

        //Se crea un panel de texto
        textPanel=new JPanel();
		textPanel.setVisible(true);
		textPanel.setBackground(Color.WHITE);

        //Se crea un botón en el panel
        buttonPanel=new JPanel();
		buttonPanel.setVisible(true);
		buttonPanel.setLayout(new GridLayout(size,size));

        //Se crea una campo de texto
        textfield=new JLabel();
		textfield.setHorizontalAlignment(JLabel.CENTER);
		textfield.setFont(new Font("MV Boli",Font.BOLD,20));
		textfield.setForeground(Color.GREEN);
		textfield.setText(bombs+" Bombs");

        resetButton=new JButton();
		resetButton.setForeground(Color.BLUE);
		resetButton.setText("Reset");
		resetButton.setFont(new Font("MV Boli", Font.BOLD, 20));
		resetButton.setBackground(Color.WHITE);
		resetButton.setFocusable(false);
		resetButton.addActionListener(this);


        solution = new int[size][size];
        buttons=new JButton[size][size]; //Creación de una variable de botones

        MouseListener mouseListener = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int modifiers = e.getModifiersEx();
                if ((modifiers & InputEvent.BUTTON3_DOWN_MASK) == InputEvent.BUTTON3_DOWN_MASK) {
                            //Quitar cualquier cosa:
                    if (e.getSource() == resetButton)
                    {
                        frame.dispose();
                        new Minesweeper();
                    }
                    //Recorre la matriz
                    for(int i=0;i<buttons.length;i++)
                    {
                        for(int j=0;j<buttons[0].length;j++)
                        {
                            if(e.getSource()==buttons[i][j]) //Si se presiona cualquier botón
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
		for(int i=0; i<buttons.length; i++)
		{
			for(int j=0; j<buttons[0].length; j++)
			{
                //Se empieza a ubicar los botones en la pantalla
				buttons[i][j]=new JButton();
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
		frame.add(buttonPanel);
		frame.add(textPanel, BorderLayout.NORTH);
        frame.add(resetButton,BorderLayout.SOUTH);

        //Dimensiones de la pantalla, para actualizar la pantalla y centrarla
        frame.setSize(570,570);
        frame.revalidate();
        frame.setLocationRelativeTo(null);

        getSolution(); //Se llama a la función
    }

    public void getSolution() //Función para ver cuantas bombas hay al rededor de un botón y lo mete en una doble lista enlazada
    {
        for(int y=0; y<solution.length; y++)
        {
            for (int x=0; x<solution.length;x++)
            {
                boolean changed = false;
                int bombsAround = 0;

                for(int i=0; i<xPositions.size();i++)
                {
                    if(x == xPositions.get(i) && y == yPositions.get(i))
                    {
                        solution[y][x] = size + 1;
                        changed = true;
                    }
                }
                if(!changed)
                {
                    for(int i = 0; i < xPositions.size(); i++)
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
                    solution[y][x] = bombsAround; //Pone las minas alrededor en una matriz
                }
            }
            
        }
        for(int i=0; i<solution.length; i++)
            {
                for(int j=0; j<solution[0].length; j++)
                    System.out.print(solution[i][j]+ " ");
                System.out.println();
            }
    }

    public void check(int y, int x) //Función par detectar si ganó o perdió
    {
        boolean over=false;

		if(solution[y][x]==(size+1)) //Si tocó una mina entonces se mete aquí
		{
            gameOver(false); //Llama la función cuando pierde
			over = true;
		}
		
		if(!over) //Si ganó entonces se mete aquí
		{
            getColor(y, x);

            if (solution[y][x] == 0)
            {
                xZero = x;
                yZero = y;

                count = 0;

                display();
            } 
            checkWinner(); //Llama a la función cuando gana
		}
	}
    
    public void checkWinner() //Función cuando se gana
	{
		int buttonsleft = 0;
		
        //Recorre la lista en busca de minas
		for(int i=0; i<buttons.length; i++)  
		{
			for(int j=0; j<buttons[0].length; j++)
			{
				if(buttons[i][j].getText()== "") //Si ya no hay
					buttonsleft++; //Aumenta 1 en la variable
			}
		}
		if(buttonsleft == bombs) //Si es igual al número de bombas
			gameOver(true); //Llama a la función de perdida con el el booleano en true, para saber qu ganó
	}

    public void gameOver(boolean won) //Función de que terminó el juego
    {
        if(!won) //Si el booleano es igual a false
        {
            textfield.setForeground(Color.RED);
            textfield.setText("Terminó el juego"); //Manda este mensaje
        }
        else //Si es igual a true
        {
            textfield.setForeground(Color.GREEN);
            textfield.setText("GANASTE"); //manda este mensaje
        }

        for(int i=0; i<buttons.length; i++)
        {
            for(int j=0; j<buttons[0].length; j++)
            {
                buttons[i][j].setBackground(null);
                buttons[i][j].setEnabled(false); //Cuando recorre toda la matriz, hace que no se pueda jugar más

                for (int count = 0; count < xPositions.size(); count++)
                {
                    if (j == xPositions.get(count) && i == yPositions.get(count))
                    {
                        buttons[i][j].setBackground(Color.BLACK);
                        buttons[i][j].setText("*");
                    }
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) //Función que sirve para funciones de eventos al dar click.
    {

        //Quitar cualquier cosa:
        if (e.getSource() == resetButton)
        {
            frame.dispose();
            new Minesweeper();
        }
        //Recorre la matriz
        for(int i=0;i<buttons.length;i++)
        {
            for(int j=0;j<buttons[0].length;j++)
            {
                if(e.getSource()==buttons[i][j]) //Si se presiona cualquier botón
                {
                        if (!flagged[i][j])
                            check(i,j); //Llama la función check para ver si ganó o se perdió   
                }
                   
            }
        }
    }
    public void display ()
    {
        if (count < 1)
        {
            if((xZero - 1) >= 0)
				getColor(yZero, xZero - 1);
			if((xZero + 1) < size)
				getColor(yZero, xZero + 1);
			if((yZero - 1) >= 0)
				getColor(yZero - 1, xZero);
			if((yZero + 1) < size)
				getColor(yZero + 1, xZero);
            if((yZero - 1) >= 0 && (xZero - 1) >= 0)
				getColor(yZero - 1, xZero - 1);
			if((yZero + 1) < size && (xZero + 1) < size)
				getColor(yZero + 1, xZero + 1);
            if((yZero - 1) >= 0 && (xZero + 1) < size)
				getColor(yZero - 1, xZero + 1);
			if((yZero + 1) < size && (xZero - 1) >= 0)
				getColor(yZero + 1, xZero - 1);

            count++;
            display();
        }
        else
		{
			for(int y=0; y<buttons.length; y++)
			{
				for(int x=0; x<buttons[0].length; x++)
				{
					if(buttons[y][x].getText().equals("0"))
					{
						if(y-1>=0)
						{
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
			if(lastXchecked<size+1 && lastYchecked<size+1)
			{				
				xZero=lastXchecked;
				yZero=lastYchecked;
				
				count=0;
				
				lastXchecked=size+1;
				lastYchecked=size+1;
				
				display();			
			}
		}
	}
    public void getColor (int y, int x)
    {
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
}