public class Stack {
    private ListNode tope;

    
    // Constructor de la clase
    public Stack() {
        tope = null;
    }

    public boolean estaVacia() {
        return tope == null;
    }

    // Método para agregar un elemento a la pila
    public void push(int x, int y, int nBombs) {
        ListNode nuevoNodo = new ListNode(x, y, nBombs);
        nuevoNodo.next = tope;
        tope = nuevoNodo;
    }

    // Método para eliminar un elemento de la pila
    public ListNode pop() {
        if (estaVacia()) {
            return null;
        }
        ListNode nodoEliminado = tope;
        tope = tope.next;
        nodoEliminado.next = null;
        return nodoEliminado;
    }
}