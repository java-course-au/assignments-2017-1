/**
 * Created by kate on 2/18/17.
 */

import org.junit.Assert;
import org.junit.Test;
import ru.spbau.mit.TrieImpl;


public class TrieTest {

    private void setWordsInTrie(TrieImpl trieWords, String[] words){
        for(String str : words){
            trieWords.add(str);
        }
    }

    @Test
    public void containsWords(){

        TrieImpl trie = new TrieImpl();

        String[] words = {"aaa" , "bbb", "ccc", "abc", "abs", "bas", "baa"};
        setWordsInTrie(trie,words);

        for (String str : words){
            Assert.assertTrue(trie.contains(str));
        }

        Assert.assertFalse(trie.contains("eee"));
    }

    @Test
    public void deleteWords(){
        TrieImpl impltrie = new TrieImpl();

        String[] words = {"aaa", "aab", "aac" , "aabc"};

        setWordsInTrie(impltrie, words);

        for (String word : words){
            Assert.assertTrue(impltrie.remove(word));
        }

        Assert.assertFalse(impltrie.remove("a"));

    }

    @Test
    public void checkSize(){
        TrieImpl impltrie = new TrieImpl();
        String[] words = {"a", "b", "c"};

        setWordsInTrie(impltrie,words);

        Assert.assertEquals( words.length, impltrie.size());

        String[] newWords = {"d", "e", "f"};

        setWordsInTrie(impltrie, newWords);

        Assert.assertEquals( words.length + newWords.length, impltrie.size());

    }

    @Test
    public void checkPrefix(){
        TrieImpl impltrie = new TrieImpl();

        String[] words = {"aab", "aaa", "aac"};
        setWordsInTrie(impltrie, words);

        Assert.assertEquals(3, impltrie.howManyStartsWithPrefix("aa"));
        Assert.assertEquals(3, impltrie.howManyStartsWithPrefix("a"));
        Assert.assertEquals(0, impltrie.howManyStartsWithPrefix("b"));
    }

    @Test
    public void checkTypicalCases(){

        TrieImpl implTrie = new TrieImpl();
        String[] words = {"aaa", "bbb", "a", "b", "c", "ac", "ab", "ba", "bb" };
        setWordsInTrie(implTrie, words);

        Assert.assertTrue(implTrie.contains("a"));
        Assert.assertEquals(words.length , implTrie.size());

        implTrie.remove("a");

        Assert.assertFalse(implTrie.contains("a"));
        Assert.assertEquals(words.length - 1, implTrie.size());

        Assert.assertEquals(1, implTrie.howManyStartsWithPrefix("c"));

        implTrie.remove("c");

        Assert.assertEquals(0, implTrie.howManyStartsWithPrefix("c"));
        Assert.assertEquals(words.length - 2, implTrie.size());

    }


}
