package com.theblind.privatenotes.core.endurance;

public interface Endurance<K,V> {

    void save(V v);

    V get(K k);

}
