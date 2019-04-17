package ia.customwarrior;

import ia.battle.core.Warrior;
import ia.battle.core.WarriorManager;
import ia.exceptions.RuleException;

public class TeamManager extends WarriorManager {
	public TeamManager(){
	}

	@Override
	public String getName() {
		return "Ananá Kombat";
	}

	@Override
	public Warrior getNextWarrior() throws RuleException {
		return new NormalWarrior();
	}
}
