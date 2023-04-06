public class LinkedList {
    ListNode head;

    public LinkedList() {
        head = null;
    }

    public void addNode(int x, int y, int nBombs) {
        ListNode newNode = new ListNode(x, y, nBombs);
        if (head == null) {
            head = newNode;
        } else {
            ListNode temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }
    }
    public void deleteNode(int x, int y) {
        if (head == null) {
            return;
        }
        if (head.xList == x && head.yList == y) {
            head = head.next;
            return;
        }
        ListNode prev = head;
        ListNode curr = head.next;
        while (curr != null) {
            if (curr.xList == x && curr.yList == y) {
                prev.next = curr.next;
                return;
            }
            prev = curr;
            curr = curr.next;
        }
    }
    public int getSize() {
        int size = 0;
        ListNode temp = head;
        while (temp != null) {
            size++;
            temp = temp.next;
        }
        return size;
    }
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

    public void printList() {
        ListNode temp = head;
        while (temp != null) {
            System.out.print("(" + temp.xList + ", " + temp.yList + ", " + temp.nBombs + ")");
            temp = temp.next;
            if (temp != null) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
    }
}