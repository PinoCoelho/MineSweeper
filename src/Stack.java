/**
 * Clase principal de la pila, aquí se hace los push y pop del Stack
 */
public class Stack {
    private ListNode tope;
    /**
     * Metodo que contiene al constructor de la clase Stack
     */
    public Stack() {
        tope = null;
    }

    
    /** 
     * Metodo para saber si está vacía la pila
     * @return boolean
     */
    public boolean estaVacia() {
        return tope == null;
    }

    /**
     * Método para agregar un elemento a la pila
     * @param x
     * @param y
     * @param nBombs
     */
    public void push(int x, int y, int nBombs) {
        ListNode nuevoNodo = new ListNode(x, y, nBombs); //Se crea el nuevo nodo
        nuevoNodo.next = tope;
        tope = nuevoNodo; //Se agrega el nuevo nodo
    }

    /**
     * Método para eliminar un elemento de la pila
     */
    public ListNode pop() {
        if (estaVacia()) {
            return null;
        }
        ListNode nodoEliminado = tope; //Se pone el nodo que se quiere eliminar
        tope = tope.next; //Se cambia donde está apuntando tope
        nodoEliminado.next = null; //Y se pone apuntar el nodo que se quiere eliminar a vacio
        return nodoEliminado;
    }
}