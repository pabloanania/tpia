package ia.customwarrior;

import java.util.ArrayList;

import ia.battle.core.BattleField;
import ia.battle.core.ConfigurationManager;
import ia.battle.core.FieldCell;
import ia.battle.core.Warrior;
import ia.battle.core.abilities.Ability;
import ia.battle.core.actions.Action;
import ia.battle.core.actions.Attack;
import ia.exceptions.RuleException;

public class NormalWarrior extends Warrior {
	
	private ConfigurationManager configManager;
	private BattleField bField;
	private int pointsLeft = ConfigurationManager.getInstance().getMaxPointsPerWarrior();
	private enum States {
		Patrol, Escape, Search  
	}
	private States state;
	
	
	
	public NormalWarrior(String name, int health, int defense, int strength, int speed, int range) throws RuleException {
		super(name, health, defense, strength, speed, range);
		
		configManager = ConfigurationManager.getInstance();
		bField = BattleField.getInstance();
		state = States.Search;
	}

	@Override
	public Action playTurn(long tick, int actionNumber) {
		// FALTA METODO DE CAMBIO DE ESTADOS!
		// stateChangeCheck();
		
		// Actúa según estado
		switch(state){
		case Patrol:
			return patrolRoutine(tick, actionNumber);
		case Escape:
			//return escapeRoutine(tick, actionNumber);
			return patrolRoutine(tick, actionNumber);
		case Search: default:
			return searchRoutine(tick, actionNumber);
		}
	}

	@Override
	public void wasAttacked(int damage, FieldCell source) {
		// Si atacó el enemigo
		if (source == bField.getEnemyData().getFieldCell()){
			if (damage > this.getStrength())
				setState(States.Escape);
			else
				setState(States.Patrol);
		}else{
			setState(States.Escape);
		}
	}

	@Override
	public void enemyKilled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void worldChanged(FieldCell oldCell, FieldCell newCell) {
		// TODO Auto-generated method stub
		
	}
	
	private void setState(States newState){
		this.state = newState;
	}
	
	private Action patrolRoutine(long tick, int actionNumber){
		FieldCell enemyCell = bField.getEnemyData().getFieldCell();
		float enemyDist = bField.calculateDistance(this.getPosition(), enemyCell);
		
		if (enemyDist < this.getRange())
			return new Attack(enemyCell);
		else		
			return new WarriorMove(this, enemyCell);
	}
	
	private Action searchRoutine(long tick, int actionNumber){
		ArrayList<FieldCell> specialItems = bField.getSpecialItems();
		float nearestDist = Float.MAX_VALUE;
		FieldCell nearestItem = null;
		
		for (FieldCell cell : specialItems){
			float dist = bField.calculateDistance(this.getPosition(), cell);
			
			if (dist < nearestDist){
				nearestItem = cell;
				nearestDist = dist;
			}
		}
		
		if (nearestItem == this.getPosition())
			this.useSpecialItem();
		
		return new WarriorMove(this, nearestItem);
	}
}
