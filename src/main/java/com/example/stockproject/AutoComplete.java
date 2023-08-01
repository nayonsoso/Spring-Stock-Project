package com.example.stockproject;

import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;

public class AutoComplete {
    private Trie trie = new PatriciaTrie();

    public void add(String s){
        this.trie.put(s, "world"); // 입력받은 문자열에 world라는 value를 저장
    }

    public Object get(String s){
        return this.trie.get(s); // 입력받은 문자열의 value를 반환
    }

}
