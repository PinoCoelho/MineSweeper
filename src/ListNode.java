public class ListNode {
    int xList;
    int yList;
    int nBombs;
    ListNode next;

    public ListNode(int x, int y, int nBombs) {
        this.xList = x;
        this.yList = y;
        this.nBombs = nBombs;
        next = null;
    }
}