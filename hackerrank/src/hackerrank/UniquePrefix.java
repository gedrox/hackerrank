package hackerrank;

import org.junit.Test;
import org.testng.collections.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UniquePrefix {
    
    @Test
    public void test() {
        List<String> ks = Lists.newArrayList(
                "abcdefgv", "abcdefgrr", "abcdefglj", "abcdefgtnsnfwzqfj", "abcdefafadr", "abcdefgwsofsbcnuv", "abcdefghffbsaq", "abcdefgwp", "abcdefgcb", "abcdefgcehch");
        System.out.println(prefix(ks));
    }
            
    public static ArrayList<String> prefix(List<String> original) {
        Node root = new Node();
        WordWithIndex[] wordsWithIndex = new WordWithIndex[original.size()];

        for (int i = 0; i < original.size(); i++) {
            wordsWithIndex[i] = new WordWithIndex(original.get(i), 0);
            root.merge(wordsWithIndex[i], 0);
        }

        ArrayList<String> res = new ArrayList<>(original.size());
        for (WordWithIndex aSi : wordsWithIndex) {
            res.add(aSi.s.substring(0, aSi.matchIndex + 1));
        }
        
        return res;
    }
    
    static class WordWithIndex {
        final String s;
        int matchIndex;
        public WordWithIndex(String s, int matchIndex) {
            this.s = s;
            this.matchIndex = matchIndex;
        }
    }
    
    static class Node implements Cloneable {
        Node[] ch = new Node[26];
        WordWithIndex word;
        int start;
        int end;

        public Node() {
        }

        public Node(WordWithIndex word, int start, int end) {
            this.word = word;
            this.start = start;
            this.end = end;
        }

        public int merge(WordWithIndex word, int index) {
            // not root
            if (this.word != null) {
                // at index they are same
                while (this.word.s.charAt(index) == word.s.charAt(index) && index < end) {
                    index++;
                }
                
                if (this.word.matchIndex < index) {
                    this.word.matchIndex = index;
                }
                word.matchIndex = index;
                
                // need to cut here
                if (index != end) {
                    Node child = new Node(this.word, index, end);
                    child.ch = this.ch;
                    
                    this.end = index;
                    this.ch = new Node[26];
                    
                    this.ch[this.word.s.charAt(index) - 'a'] = child;
                }
            }
            
            char f = word.s.charAt(index);
            if (ch[f - 'a'] == null) {
                ch[f - 'a'] = new Node(word, index, word.s.length());
                return index;
            } else {
                return ch[f - 'a'].merge(word, index);
            }
        }
    }
}
