package com.db_engine;
import java.io.Serializable;
import java.util.Objects;
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

    public boolean equals(Object o){
        if (this == o) return true;
        if (o instanceof Pair) {
            Pair pair = (Pair) o;
            if (key != null ? !key.equals(pair.key) : pair.key != null) return false;
            if (value != null ? !value.equals(pair.value) : pair.value != null) return false;
            return true;
        }
        return false;
    }
    



    public int compareTo(Pair<K, V> o) {
        
        return ((Comparable<K>) key).compareTo(o.key);
    }

    public int hashCode() {
        
        int hash = 7;
        hash = 31 * hash + (key != null ? key.hashCode() : 0);
        hash = 31 * hash + (value != null ? value.hashCode() : 0);
        return hash;
    }
}
