package com.gamergeist.gamergeistsurvival.utils;

public class Pair<L, R> {
    private final L first;
    private final R second;

    public Pair(L first, R second) {
        this.first = first;
        this.second = second;
    }

    public L first() {
        return first;
    }

    public R second() {
        return second;
    }
}
