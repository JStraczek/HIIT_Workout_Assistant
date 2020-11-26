package com.myapplication.classes;


public class Exercise{
    private String name;
    private int rounds;
    private long interval_time;
    private long rest_time;
    private long cooldown_time;

    public Exercise(String name, int rounds, long interval_time, long rest_time, long cooldown_time) {
        this.name = name;
        this.rounds = rounds;
        this.interval_time = interval_time;
        this.rest_time = rest_time;
        this.cooldown_time = cooldown_time;
    }

    // Getters:
    public String getName(){
        return name;
    }

    public int getRounds() {
        return rounds;
    }

    public long getInterval() {
        return interval_time;
    }

    public long getCooldown() {
        return cooldown_time;
    }

    public long getRest() {
        return rest_time;
    }

}
