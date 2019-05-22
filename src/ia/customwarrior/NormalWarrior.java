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
		Idle, Patrol, Escape, Search  
	}
	private States state;
	private FieldCell escapeSource;
	private FieldCell escapeDestination;
	
	
	
	public NormalWarrior(String name, int health, int defense, int strength, int speed, int range) throws RuleException {
		super(name, health, defense, strength, speed, range);
		
		configManager = ConfigurationManager.getInstance();
		bField = BattleField.getInstance();
		state = States.Idle;
	}

	@Override
	public Action playTurn(long tick, int actionNumber) {
		// Si de casualidad tiene caja la agarra
		if (this.getPosition().hasItem())
			this.useSpecialItem();
			
		// Verifica si debe cambiar de estados
		stateChangeCheck();
		
		// Actúa según estado
		switch(state){
		case Patrol:
			return patrolRoutine(tick, actionNumber);
		case Escape:
			return escapeRoutine(tick, actionNumber);
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
		
		if (bField.getEnemyData().getInRange())
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
		
		// FALTA: NO SE PUEDEN AGARRAR ALGUNAS CAJAS :(
		if (nearestItem.hasItem()){
			this.useSpecialItem();
		}
		
		return new WarriorMove(this, nearestItem);
	}
	
	private Action escapeRoutine(long tick, int actionNumber){
		FieldCell from;
		
		if (bField.getEnemyData().getInRange())
			from = bField.getEnemyData().getFieldCell();
		else
			from = bField.getHunterData().getFieldCell();
		
		return new WarriorMove(this, getEscapeDestination(from));
	}
	
	private FieldCell getEscapeDestination(FieldCell from){
		int width = configManager.getMapWidth();
		int height = configManager.getMapHeight();
		
		if (from.getX() < width/2){
			if (from.getY() < width/2)
				return bField.getFieldCell(width, height);
			else
				return bField.getFieldCell(width, 0);
		}else{
			if (from.getY() < width/2)
				return bField.getFieldCell(0, height);
			else
				return bField.getFieldCell(0, 0);
		}
	}
	
	private void stateChangeCheck(){
		switch(state){
			case Escape:
				if (!bField.getEnemyData().getInRange() && !bField.getHunterData().getInRange())
					state=States.Idle;
				break;
			case Search:
				if (bField.getEnemyData().getInRange())
					state=States.Patrol;
				break;
			case Idle: default:
				if (bField.getEnemyData().getHealth() < this.getHealth())
					state=States.Patrol;
				else
					state=States.Search;
				break;
		}
	}
}
