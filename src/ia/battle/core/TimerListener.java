package ia.battle.core;

public interface TimerListener {
	
	public void tickLapsed(long tick);
    
    public void turnLapsed(long tick, int turnNumber, Warrior warrior);
    
}
