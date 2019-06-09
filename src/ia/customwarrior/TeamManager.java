package ia.customwarrior;

import java.util.Random;

import ia.battle.core.ConfigurationManager;
import ia.battle.core.Warrior;
import ia.battle.core.WarriorManager;
import ia.exceptions.RuleException;

public class TeamManager extends WarriorManager {
	private ConfigurationManager configManager;
	private Random random;
	
	public TeamManager(){
		configManager = ConfigurationManager.getInstance();
		random = new Random();
	}

	@Override
	public String getName() {
		return "Ananá Kombat";
	}

	@Override
	public Warrior getNextWarrior() throws RuleException {
		float[] percentages;
		String name;
		
		// STAT ORDER: 					Health, Def.,	Str.,	Speed,	Range
		float[] balancedPercentages = 	{0.2f, 	0.2f, 	0.2f, 	0.2f, 	0.2f};
		float[] fastPercentages = 		{0.1f, 	0.1f,	0.1f,	0.5f,	0.2f};
		float[] healtyPercentages = 	{0.5f,	0.1f,	0.1f,	0.2f,	0.1f};
		float[] strongPercentages = 	{0.1f,	0.1f,	0.4f,	0.2f,	0.2f};
		float[] rangedPercentages = 	{0.05f,	0.05f,	0.1f,	0.2f,	0.6f};
		
		int rnd = random.nextInt(100);
		
		if (rnd < 40){
			percentages = fastPercentages;
			name = "Hulk";
		}else if (rnd >= 40 && rnd < 75){
			percentages = strongPercentages;
			name = "Sonic";
		}else if (rnd >= 75 && rnd < 90){
			percentages = healtyPercentages;
			name = "Veggie";	
		}else if (rnd >= 90 && rnd < 95){
			percentages = rangedPercentages;
			name = "Sniper";
		}else{
			percentages = balancedPercentages;
			name = "Vanilla";
		}
		
		int[] stats = calculatePoints(percentages);
		
		return new NormalWarrior(name, stats[0], stats[1], stats[2], stats[3], stats[4]);
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
