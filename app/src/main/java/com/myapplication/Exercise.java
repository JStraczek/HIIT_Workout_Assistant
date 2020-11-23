 public class Exercise {
    private int rounds;
    private long warmup;
    private long interval;
    private long rest;
    private long cooldown;
    private long currentTimer;

    private long timeLeft;

    public Exercise(long rounds, long warmup, long interval, long rest, long cooldown){
        this.rounds = (int) rounds;
        this.warmup = warmup;
        this.interval = interval;
        this.rest = rest;
        this.cooldown = cooldown;
    }

}
