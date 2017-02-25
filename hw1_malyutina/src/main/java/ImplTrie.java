/**
 * Created by kate on 2/18/17.
 */

public class ImplTrie implements Trie {

    private Node root = new Node();
    private int size;

    @Override
    public boolean add(String element) {
        if (contains(element)) {
            return false;
        }
        size++;
        return root.insertNode(element);
    }

    @Override
    public boolean contains(String element) {
        return root.checkElemInTree(element);
    }

    @Override
    public boolean remove(String element) {
        if (!contains(element)) {
            return false;
        }
        size--;
        return root.deleteNode(element);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        return root.howManyPrefix(prefix);
    }

    private class Node{
        private static final int SIZE = 'Z' + 'z' - 'A' - 'a' + 2;

        private Node[] next = new Node[SIZE];

        private int counter;
        private boolean isTerminate;

        private int index(final char element){
            if (!Character.isLowerCase(element)) {
                return element - 'A' + 'z' - 'a' + 1;
            }
            return element- 'a';
        }

        private boolean checkElemInChild(final char element){
            return next[index(element)] != null;
        }

        private Node getChild(final char element){
            return next[index(element)];
        }

        private void setChild(final char element){
            next[index(element)] = new Node();
        }

        private void delChild(final char element){
            next[index(element)] = null;
        }

        private void succCounter(){
            counter++;
        }

        private void predCounter(){
            counter--;
        }

        private Node findNode(final String str){
            Node thisNode = this;
            for (int i = 0; i < str.length() && thisNode != null; i++) {
                thisNode = thisNode.getChild( str.charAt(i));
            }
            return thisNode;
        }

        private boolean checkElemInTree(String str){
            Node node = findNode(str);
            return node!=null && node.isTerminate ;
        }

        private boolean insertNode(final String str){
            Node thisNode = root;
            thisNode.succCounter();

            for (int i = 0; i < str.length(); i++) {
                if (!thisNode.checkElemInChild(str.charAt(i)) ){
                    thisNode.setChild(str.charAt(i));
                }
                thisNode = thisNode.getChild(str.charAt(i));
                thisNode.succCounter();
            }
            thisNode.isTerminate = true;
            return true;
        }

        private boolean deleteNode(final String str){
            Node thisNode = root;
            thisNode.predCounter();

            for (int i = 0; i < str.length(); i++) {
                thisNode = thisNode.getChild(str.charAt(i));
                thisNode.predCounter();

                if(thisNode.counter == 0 ){
                    thisNode.delChild(str.charAt(i));
                    return true;
                }
            }
            thisNode.isTerminate = false;
            return true;
        }

        private int howManyPrefix(String str){
            Node node = findNode(str);
            if ( node == null ) {
                return 0;
            }
            return node.counter;
        }
    }
}
