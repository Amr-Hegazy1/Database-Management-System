package com.db_engine;
import java.io.Serializable;

public class Pair<K, V> implements Serializable, Comparable<Pair<K, V>>{
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
    public K getKey() {
        return key;
    }
    public V getValue() {
        return value;
    }
    public void setKey(K key) {
        this.key = key;
    }
    public void setValue(V value) {
        this.value = value;
    }
    public String toString() {
        return key + "=" + value;
    }
    



public int compareTo(Pair<K, V> o) {
    return ((Comparable<K>) key).compareTo(o.key);
}
}
