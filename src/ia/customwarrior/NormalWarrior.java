package ia.customwarrior;

import ia.battle.core.BattleField;
import ia.battle.core.ConfigurationManager;
import ia.battle.core.FieldCell;
import ia.battle.core.Warrior;
import ia.battle.core.actions.Action;
import ia.battle.core.actions.Attack;
import ia.exceptions.RuleException;

public class NormalWarrior extends Warrior {
	
	private ConfigurationManager configManager;
	private BattleField bField;
	
	
	public NormalWarrior() throws RuleException {
		super("Balanceado", 20, 5, 5, 20, 5);
	}

	@Override
	public Action playTurn(long tick, int actionNumber) {
		// Distancia al enemigo
		FieldCell enemyCell = bField.getInstance().getEnemyData().getFieldCell();
		float enemyDist = bField.getInstance().calculateDistance(this.getPosition(), enemyCell);
		
		if (enemyDist < this.getRange())
			return new Attack(enemyCell);
		else		
			return new WarriorMove(this);
	}

	@Override
	public void wasAttacked(int damage, FieldCell source) {
		// TODO Auto-generated method stub
		
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
