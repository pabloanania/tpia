package ia.customwarrior;

import ia.battle.core.ConfigurationManager;
import ia.battle.core.Warrior;
import ia.battle.core.WarriorManager;
import ia.exceptions.RuleException;

public class TeamManager extends WarriorManager {
	private ConfigurationManager configManager;
	
	public TeamManager(){
		configManager = ConfigurationManager.getInstance();
	}

	@Override
	public String getName() {
		return "Ananá Kombat";
	}

	@Override
	public Warrior getNextWarrior() throws RuleException {
		float[] percentages = {0.2f, 0.2f, 0.2f, 0.2f, 0.2f};
		int[] stats = calculatePoints(percentages);
		
		return new NormalWarrior("Balanceado", stats[0], stats[1], stats[2], stats[3], stats[4]);
	}
	
	private int[] calculatePoints(float[] percentages){
		int[] stats = new int[percentages.length];
		int total = configManager.getMaxPointsPerWarrior();
		int available = total;
				
		for (int i=0 ; i<percentages.length; i++){
			if (i != percentages.length - 1){
				int points = (int) (Math.floor(percentages[i] * configManager.getMaxPointsPerWarrior()));
				available -= points;
				stats[i] = points;
			}else{
				stats[i] = available;
			}
		}

		return stats; 
	}
}
