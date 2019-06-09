package ia.customwarrior;


import ia.battle.core.Warrior;
import ia.battle.core.WarriorManager;
import ia.exceptions.RuleException;

public class DummyTeamManager extends WarriorManager {
	
	public DummyTeamManager(){
	}

	@Override
	public String getName() {
		return "Dummy";
	}

	@Override
	public Warrior getNextWarrior() throws RuleException {		
		return new DummyWarrior("Dummy", 10, 10, 10, 10, 10);
	}
	
}
