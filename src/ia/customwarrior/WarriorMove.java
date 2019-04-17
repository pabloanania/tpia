package ia.customwarrior;

import java.util.ArrayList;

import ia.battle.core.BattleField;
import ia.battle.core.ConfigurationManager;
import ia.battle.core.FieldCell;
import ia.battle.core.Warrior;
import ia.battle.core.actions.Move;
import astar.*;

public class WarriorMove extends Move{
	
	private Warrior warrior;
	private ConfigurationManager configManager;
	private BattleField bField;

	
	public WarriorMove(Warrior warrior){
		this.warrior = warrior;
		this.configManager = ConfigurationManager.getInstance();
		this.bField = BattleField.getInstance();
	}
	
	@Override
	public ArrayList<FieldCell> move() {
		int enemyX = bField.getEnemyData().getFieldCell().getX();
		int enemyY = bField.getEnemyData().getFieldCell().getY();
		
		return getMovementPath(enemyX,enemyY);
	}
	
	
	public ArrayList<FieldCell> getMovementPath(int destX, int destY){
		ArrayList<FieldCell> fieldPath = new ArrayList<FieldCell>();
		int warriorX = warrior.getPosition().getX();
		int warriorY = warrior.getPosition().getY();
		
		int[][] map = convertAStarMapFromIAMap();
		AStar aStar = new AStar(map);
		ArrayList<Node> nodePath = aStar.findPath(warriorX, warriorY, destX, destY);
		
		for(Node n : nodePath)
			fieldPath.add(convertFromNode(n));
		
		return fieldPath;
	}
	
	/*
	 * Esta clase hay que moverla a ADAPTERS
	 */
	public int[][] convertAStarMapFromIAMap(){
		int width = configManager.getMapWidth();
		int height = configManager.getMapHeight();
		int map[][] = new int[width][height];
		
		for (int x = 0; x < width; x++){
			for (int y = 0; y < height; y++) {
				float cost = bField.getFieldCell(x, y).getCost();

				if (cost <2f)
					map[x][y] = 0;
				else
					map[x][y] = 1;
			}
		}
		
		return map; 
	}
	
	
	/*
	 * Esta clase hay que moverla a ADAPTERS
	 */
	public FieldCell convertFromNode(Node node){
		return bField.getFieldCell(node.getX(), node.getY());
	}
}
