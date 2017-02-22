package ru.spbau.mit;

import java.util.ArrayList;


public class StringSetImpl implements StringSet {
    private static final int JUMP_SIZE = 256 * 2;    // Java has 2-byte chars.
    private static final int FAILED_STATE = -1;

    private ArrayList<int[]> nextState = new ArrayList<>();
    private ArrayList<Boolean> isTerminal = new ArrayList<>();
    private ArrayList<Integer> subtreeCount = new ArrayList<>();
    private int lastFreeState;


    public StringSetImpl() {
        lastFreeState = 0;
        createState();
    }

    /**
     * Expected complexity: O(|element|)
     *
     * @return <tt>true</tt> if this set did not already contain the specified
     * element
     */
    @Override
    public boolean add(String element) {
        int state = getStateOrFail(element, false);

        if (isTerminal.get(state)) {
            return false;
        }

        addPrefixVal(element, 1);
        isTerminal.set(state, true);

        return true;
    }

    /**
     * Expected complexity: O(|element|)
     *
     */
    @Override
    public boolean contains(String element) {
        int state = getStateOrFail(element, true);

        return (state != FAILED_STATE && isTerminal.get(state));
    }

    /**
     * Expected complexity: O(|element|)
     *
     * @return <tt>true</tt> if this set contained the specified element
     */
    @Override
    public boolean remove(String element) {
        int state = getStateOrFail(element, true);

        if (state == FAILED_STATE) {
            return false;
        }

        addPrefixVal(element, -1);
        isTerminal.set(state, false);

        return true;
    }

    /**
     * Expected complexity: O(1)
     */
    @Override
    public int size() {
        return subtreeCount.get(0);
    }

    /**
     * Expected complexity: O(|prefix|)
     *
     */
    @Override
    public int howManyStartsWithPrefix(String prefix) {
        int state = getStateOrFail(prefix, true);

        return (state == FAILED_STATE) ? 0 : subtreeCount.get(state);
    }

    private int createState() {
        isTerminal.add(false);
        nextState.add(new int[JUMP_SIZE]);
        subtreeCount.add(0);
        lastFreeState++;
        return lastFreeState - 1;
    }

    private boolean canJump(int stateNumber, char offset) {
        return nextState.get(stateNumber)[offset] != 0;
    }

    private void addPrefixVal(String element, int addend) {
        int curState = 0;

        subtreeCount.set(curState, subtreeCount.get(curState) + addend);
        for (int i = 0; i < element.length(); i++) {
            char offset = element.charAt(i);
            curState = nextState.get(curState)[offset];
            subtreeCount.set(curState, subtreeCount.get(curState) + addend);
        }
    }

    private int getStateOrFail(String element, boolean failOnNoJump) {
        int curState = 0;

        for (int i = 0; i < element.length(); i++) {
            char offset = element.charAt(i);
            if (!canJump(curState, offset)) {
                if (failOnNoJump) {
                    return FAILED_STATE;
                }

                int newState = createState();
                nextState.get(curState)[offset] = newState;
            }
            curState = nextState.get(curState)[offset];
        }

        return curState;
    }
}
