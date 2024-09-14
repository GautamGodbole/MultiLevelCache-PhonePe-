package org.example.multilevelcache.statistics;

/*
 * Class : CacheParameters
 *
 * Keeps track of main parameters of cache
 */
public class CacheParameters {

    protected int usage;
    protected int capacity;
    protected int hit;
    protected int miss;

    public CacheParameters(int capacity) {
        this.capacity = capacity;
        this.usage = this.hit = this.miss = 0;
    }

    public int getUsage() {
        return usage;
    }

    public void setUsage(int usage) {
        this.usage = usage;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getHit() {
        return hit;
    }

    public void incrementHit() {
        this.hit++;
    }

    public int getMiss() {
        return miss;
    }

    public void incrementMiss() {
        this.miss++;
    }

    @Override
    public String toString() {
        return "{" +
                "usage=" + usage +
                ", capacity=" + capacity +
                ", hit=" + hit +
                ", miss=" + miss +
                '}';
    }
}
