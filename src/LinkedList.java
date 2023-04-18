/**
 * Clase de la lista enlazada, de aquí se añade los nodos, se borran los nodos, etc...
 */
public class LinkedList {
    ListNode head;

    /**
     *  Constructor de la clase LinkedList
     */
    public LinkedList() {
        head = null;
    }
    
    /** 
     * Metodo para añadir Nodos a la lista enlazada
     * @param x
     * @param y
     * @param nBombs
     */
    public void addNode(int x, int y, int nBombs) {
        ListNode newNode = new ListNode(x, y, nBombs); //Sse crea el nuevo nodo a añadir
        if (head == null) { //Si head está apuntando a vacío
            head = newNode; //Entonces ahora apunta al nuevo nodo
        } else {
            ListNode temp = head;
            while (temp.next != null) { //Si no es vacío
                temp = temp.next; //Apunta al siguiente
            }
            temp.next = newNode; //Y ese siguiente apunta al nuevo nodo
        }
    }
    /**
     * Metodo para eliminar nodos
     * @param x
     * @param y
     * @param nBombs
     */
    public void deleteNode(int x, int y) {
        if (head == null) { //Si está vacío entonces no haga nada
            return;
        }
        if (head.xList == x && head.yList == y) { //Si es igual
            head = head.next; //Apunte al siguiente
            return;
        }
        ListNode prev = head;
        ListNode curr = head.next;
        while (curr != null) { //Si es diferente a null
            if (curr.xList == x && curr.yList == y) { //Si es igual
                prev.next = curr.next; //Apunte al siguiente
                return;
            }
            prev = curr;
            curr = curr.next; //Se elimina el nodo
        }
    }
    /**
     * Metodo para obtener el tamaño de la lista enlazada
     */
    public int getSize() {
        int size = 0;
        ListNode temp = head;
        while (temp != null) { //Mientras no esté apuntando a null
            size++; //Sume 1 al tamaño
            temp = temp.next; //Pase al siguiente nodo
        }
        return size; //Cuando termine devuelva el tamaño
    }
    /**
     * Metodo para obtener la posición del nodo
     * @param position
     */
    public ListNode getNodeAtPosition(int position) {
        if (position < 0 || position >= getSize()) {
            return null;
        }
        ListNode temp = head;
        for (int i = 0; i < position; i++) {
            temp = temp.next;
        }
        return temp;
    }
    /**
     * Metodo para obtener el nodo
     */
    public ListNode obtenerNodo()
    {
        ListNode temp = head;
        while (temp != null) { //Mientras no esté vacío
            temp = temp.next;
            if (temp.nBombs < 9) //Si es menor a nueve 
            {
                return temp; //Entonces o devuelve
            }
        }
        return null;
    }
    /**
     * Metodo para hacer print de la lista enlazada
     */
    public void printList() {
        ListNode temp = head;
        while (temp != null) { //Mientras no sea null
            System.out.print("(" + temp.xList + ", " + temp.yList + ", " + temp.nBombs + ")"); //Imprime los nodos
            temp = temp.next;
            if (temp != null) { //Y si es null
                System.out.print(" -> "); //Entonces imprime nada más esto
            }
        }
        System.out.println();
    }
}