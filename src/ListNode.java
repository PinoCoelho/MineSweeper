/*
 * Clase donde se crea la lista enlazada y se pasan los parametros de los nodos
 */
public class ListNode {
    int xList;
    int yList;
    int nBombs;
    ListNode next;

    /*
     * Metodo para instaciar la lista arreglada con las variables que se quiere tener
     * @param x,y,nbombs 
     */
    public ListNode(int x, int y, int nBombs) {
        this.xList = x;
        this.yList = y;
        this.nBombs = nBombs;
        next = null;
    }
}