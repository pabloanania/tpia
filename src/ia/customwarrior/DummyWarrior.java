package ia.customwarrior;

import ia.battle.core.FieldCell;
import ia.battle.core.Warrior;
import ia.battle.core.actions.Action;
import ia.exceptions.RuleException;

public class DummyWarrior extends Warrior {
	
	
	
	public DummyWarrior(String name, int health, int defense, int strength, int speed, int range) throws RuleException {
		super(name, health, defense, strength, speed, range);
	}

	@Override
	public Action playTurn(long tick, int actionNumber) {	
		return new WarriorMove(this, this.getPosition());	
	}

	@Override
	public void wasAttacked(int damage, FieldCell source) {
	}

	@Override
	public void enemyKilled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void worldChanged(FieldCell oldCell, FieldCell newCell) {
		// TODO Auto-generated method stub
		
	}
}
