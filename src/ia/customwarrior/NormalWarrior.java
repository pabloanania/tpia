package ia.customwarrior;

import java.util.ArrayList;
import ia.battle.core.BattleField;
import ia.battle.core.ConfigurationManager;
import ia.battle.core.FieldCell;
import ia.battle.core.FieldCellType;
import ia.battle.core.Warrior;
import ia.battle.core.actions.Action;
import ia.battle.core.actions.Attack;
import ia.exceptions.RuleException;

public class NormalWarrior extends Warrior {
	
	private ConfigurationManager configManager;
	private BattleField bField;
	private enum States {
		Idle, Patrol, EscapeFromHunter, EscapeFromEnemy, Search  
	}
	private States state;
	private FieldCell escapeTop;
	private FieldCell escapeBottom;
	
	
	
	public NormalWarrior(String name, int health, int defense, int strength, int speed, int range) throws RuleException {
		super(name, health, defense, strength, speed, range);
		
		configManager = ConfigurationManager.getInstance();
		bField = BattleField.getInstance();
		state = States.Idle;
		escapeTop = null;
		escapeBottom = null;
	}

	@Override
	public Action playTurn(long tick, int actionNumber) {	
		// Verifica si debe cambiar de estados
		stateChangeCheck();
		
		// Actúa según estado
		switch(state){
			case Patrol:
				return patrolRoutine(tick, actionNumber);
			case EscapeFromEnemy: case EscapeFromHunter:
				return escapeRoutine(tick, actionNumber);
			case Search: default:
				return searchRoutine(tick, actionNumber);
		}		
	}

	@Override
	public void wasAttacked(int damage, FieldCell source) {
		// Por cercanía infiero quien atacó ya que source no es exactamente en donde estaba quien atacó
		float enemyDistance = bField.calculateDistance(source, bField.getEnemyData().getFieldCell());
		float hunterDistance = bField.calculateDistance(source, bField.getHunterData().getFieldCell());

		if (hunterDistance < enemyDistance)
			setState(States.EscapeFromHunter);
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
		FieldCell nearestItem = null;
			
		float nearestDist = Float.MAX_VALUE;
		
		for (FieldCell cell : specialItems){
			float dist = bField.calculateDistance(this.getPosition(), cell);
			
			if (dist < nearestDist){
				nearestItem = cell;
				nearestDist = dist;
			}
		}
		
		if (nearestItem == null){
			setState(States.Patrol);
			return patrolRoutine(tick, actionNumber);
		}
		
		return new WarriorMove(this, nearestItem);
	}
	
	private Action escapeRoutine(long tick, int actionNumber){
		FieldCell hunterCell = bField.getHunterData().getFieldCell();
		FieldCell enemyCell = bField.getEnemyData().getFieldCell();
		
		if (state == States.EscapeFromEnemy){		
			return new WarriorMove(this, getEscapeDestination(enemyCell));	
		}
		else{
			return new WarriorMove(this, getEscapeDestination(hunterCell));
		}
	}
	
	private FieldCell getEscapeDestination(FieldCell from){
		int width = configManager.getMapWidth();
		int height = configManager.getMapHeight();
		
		if (escapeBottom == null)
			for (int i=height-1; i>=0; i--)
				if (bField.getFieldCell((width-1)/2, i).getFieldCellType() != FieldCellType.BLOCKED)
					escapeBottom = bField.getFieldCell(width/2, i);
				
		if (escapeTop == null)
			for (int i=0; i<height; i++)
				if (bField.getFieldCell((width-1)/2, i).getFieldCellType() != FieldCellType.BLOCKED)
					escapeTop = bField.getFieldCell(width/2, i);
		
		if (from.getY() < height/2)
			return escapeTop;
		else
			return escapeBottom;
	}
	
	private void stateChangeCheck(){
		switch(state){
			case EscapeFromHunter: case EscapeFromEnemy:
				if (this.getPosition() == escapeTop || this.getPosition() == escapeBottom)
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
