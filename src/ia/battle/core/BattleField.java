/*
 * Copyright (c) 2012-2017, Ing. Gabriel Barrera <gmbarrera@gmail.com>
 * 
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above 
 * copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES 
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR 
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES 
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN 
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF 
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ia.battle.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ia.battle.core.abilities.Ability;
import ia.battle.core.abilities.StealthAbility;
import ia.battle.core.actions.Action;
import ia.battle.core.actions.Attack;
import ia.battle.core.actions.BuildWall;
import ia.battle.core.actions.Move;
import ia.battle.core.actions.Skip;
import ia.battle.core.actions.Suicide;
import ia.battle.core.specialitems.SpecialItem;
import ia.battle.core.specialitems.SpecialItemFactory;
import ia.exceptions.RuleException;

//TODO: Mines and enemy flags

//TODO: Las cajas no reaparecen al iniciar una nueva batalla sin cerrar la aplicacion

//TODO: Cambio de warriors

public class BattleField {

	private ArrayList<GeneralListener> listeners;
	private ArrayList<TimerListener> timerListeners;

	private static BattleField instance = new BattleField();

	private ConfigurationManager configurationManager;
	private SpecialItemFactory specialItemFactory = new SpecialItemFactory();

	private long tick;
	private boolean inFight;
	private boolean sideToShrink;
	private int leftColumnToShrink, rightColumnToShrink;

	private Hunter hunter;

	private WarriorManager wm1, wm2;
	private WarriorWrapper currentWarriorWrapper, warriorWrapper1, warriorWrapper2, hunterWrapper;
	private HashMap<Warrior, WarriorWrapper> warriors;

	private FieldCell[][] cells;

	private Random random = new Random();

	private BattleField() {
		listeners = new ArrayList<GeneralListener>();
		timerListeners = new ArrayList<TimerListener>();
		configurationManager = ConfigurationManager.getInstance();
	}

	public static BattleField getInstance() {
		return instance;
	}

	public void initCells() {
		int height = configurationManager.getMapHeight();
		int width = configurationManager.getMapWidth();

		cells = new FieldCell[width][height];

		int[][] maze = new TerrainGenerator(width, height).getMaze();

		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++) {

				if (maze[i][j] == 1) // Wall
					cells[i][j] = new FieldCell(FieldCellType.BLOCKED, i, j, null, Float.POSITIVE_INFINITY);
				else {
					float cost = 1f;

					if (maze[i][j] == 2) // Swamp
						cost = 1.5f;
					
					double chance = Math.abs(random.nextGaussian());
					
					Grabbable g = null;
					if (chance > 3.2)
						g = new StealthAbility();
					else
						if (chance > 2.5)
						g = specialItemFactory.getSpecialItem();
					
					cells[i][j] = new FieldCell(FieldCellType.NORMAL, i, j, g, cost);
					
				}
			}
	}
	
	
	public FieldCell[][] getMap() {
		return this.cells;
	}

	/**
	 * Don't Use HasItem(), instead use getSpecialItems() method
	 */
	public FieldCell getFieldCell(int x, int y) { // throws OutOfMapException {

		// if ((x >= cells.length) || (x < 0) || (y >= cells[0].length) || (y < 0))
		// throw new OutOfMapException();

		return cells[x][y];
	}

	/**
	 * Returns basic information about enemy warrior
	 * 
	 * @return
	 */
	public WarriorData getEnemyData() {
		WarriorData enemyData;
		Warrior enemyWarrior;

		if (currentWarriorWrapper == warriorWrapper1)
			enemyWarrior = warriorWrapper2.getWarrior();
		else
			enemyWarrior = warriorWrapper1.getWarrior();

		FieldCell position = enemyWarrior.getPosition();

//		StealthAbility ab = Ability.getStealthAbility(enemyWarrior);
//		if (ab != null)
//			position = ab.getStealthPosition();

		enemyData = new WarriorData(position, enemyWarrior.getHealth(), enemyWarrior.getName(),
				isWarriorInRange(enemyWarrior), enemyWarrior.getWarriorManager().getCount());

		warriorWrapper2.getWarrior().getAbilities();

		return enemyData;
	}

	/**
	 * Returns basic information about the hunter
	 * 
	 * @return
	 */
	public WarriorData getHunterData() {

		WarriorData enemyData;

		enemyData = new WarriorData(hunter.getPosition(), hunter.getHealth(), hunter.getName(),
				isWarriorInRange(hunter), 1);

		return enemyData;
	}

	/**
	 * Este metodo es para uso interno del framework. Su uso es ilegal.
	 * 
	 * @return
	 * @throws RuleException
	 */
	public ArrayList<Warrior> getWarriors() throws RuleException {

		ArrayList<Warrior> warriors = new ArrayList<>();

		warriors.add(warriorWrapper1.getWarrior());
		warriors.add(warriorWrapper2.getWarrior());
		warriors.add(hunter);

		return warriors;
	}

	/**
	 * Devuelve si el warrior esta en el rango de ataque del warrior actual.
	 * 
	 * @param warrior
	 * @return
	 */
	boolean isWarriorInRange(Warrior warrior) {
		return isPositionInRange(warrior.getPosition());
	}

	boolean isPositionInRange(FieldCell field) {
		int centerX = currentWarriorWrapper.getWarrior().getPosition().getX();
		int centerY = currentWarriorWrapper.getWarrior().getPosition().getY();

		int range = currentWarriorWrapper.getWarrior().getRange();

		int x = field.getX();
		int y = field.getY();

		if ((Math.pow(centerX - x, 2)) + (Math.pow(centerY - y, 2)) <= Math.pow(range, 2)) {
			return true;
		}

		return false;
	}

	/**
	 * Devuelve si el warrior2 esta en el rango de ataque del warrior1.
	 * 
	 * @param warrior1
	 *            , warrior2
	 * @return
	 */
	boolean isWarriorInRange(Warrior warrior1, Warrior warrior2) {
		int centerX = warrior1.getPosition().getX();
		int centerY = warrior1.getPosition().getY();

		int range = warrior1.getRange();

		int x = warrior2.getPosition().getX();
		int y = warrior2.getPosition().getY();

		if ((Math.pow(centerX - x, 2)) + (Math.pow(centerY - y, 2)) <= Math.pow(range, 2)) {
			return true;
		}

		return false;
	}

	/**
	 * Returns all cells where are special items
	 * 
	 * @return
	 */
	public ArrayList<FieldCell> getSpecialItems() {
		// TODO: Improve
		ArrayList<FieldCell> items = new ArrayList<FieldCell>();

		for (int i = 0; i < configurationManager.getMapWidth(); i++)
			for (int j = 0; j < configurationManager.getMapHeight(); j++)
				if (cells[i][j].getItem() != null && isPositionInRange(cells[i][j])) {

					items.add(cells[i][j]);
				}

		return items;
	}

	public void addWarriorManager(WarriorManager wm) {

		if (wm1 == null)
			wm1 = wm;
		else
			wm2 = wm;
	}

	private FieldCell getFreeCell() {
		FieldCell fieldCell = null;

		while ((fieldCell = cells[random.nextInt(configurationManager.getMapWidth())][random
				.nextInt(configurationManager.getMapHeight())]).getFieldCellType() == FieldCellType.BLOCKED)
			;

		return fieldCell;
	}

	private FieldCell getFieldCell(FieldCellType type) {
		FieldCell fieldCell = null;

		while ((fieldCell = cells[random.nextInt(configurationManager.getMapWidth())][random
				.nextInt(configurationManager.getMapHeight())]).getFieldCellType() != type)
			;

		return fieldCell;
	}

	private WarriorWrapper requestNextWarrior(WarriorManager wm) {

		WarriorWrapper wwrapper = null;

		try {
			wwrapper = new WarriorWrapper(wm.getNewWarrior());
			System.out.println(wwrapper.getWarrior().getSpeed());
			if (wwrapper.getWarrior().getName() == null)
				wwrapper.getWarrior().setName("Sin Nombre " + wm.hashCode());

		} catch (Exception e1) {
			// TODO: Exception
			e1.printStackTrace();
		}

		warriors.put(wwrapper.getWarrior(), wwrapper);

		try {
			wwrapper.getWarrior().setPosition(getFreeCell());

		} catch (RuleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return wwrapper;
	}

	public void fight() {
		TurnController turnController;
		Action currentWarriorAction;

		tick = 0;
		inFight = true;

		sideToShrink = false;
		leftColumnToShrink = 0;
		rightColumnToShrink = ConfigurationManager.getInstance().getMapWidth() - 1;

		int actionPerTurns = ConfigurationManager.getInstance().getActionsPerTurn();
		int warriorPerBattle = ConfigurationManager.getInstance().getMaxWarriorPerBattle();

		initCells();

		warriors = new HashMap<Warrior, WarriorWrapper>();

		warriorWrapper1 = requestNextWarrior(wm1);
		warriorWrapper2 = requestNextWarrior(wm2);

		// Create the hunter
		try {
			hunter = new Hunter("The Hunter", 10, 10, 10, 10, 5);
			hunterWrapper = new WarriorWrapper(hunter);
		} catch (RuleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		hunter.setPosition(getFreeCell());

		turnController = new TurnController(warriorWrapper1, warriorWrapper2, hunterWrapper);

		for (GeneralListener listener : listeners)
			listener.startFight();

		for (GeneralListener listener : listeners)
			listener.statsChanged(wm1.getName(), wm1.getCount(), wm2.getName(), wm2.getCount());

		do {
			tick++;

			currentWarriorWrapper = turnController.nextWarriorWrapper();

			// System.out.println("Juega: "
			// +currentWarriorWrapper.getWarrior().getName());

			if (currentWarriorWrapper.getWarrior().getHealth() <= 0) {
				// TODO: Change for multiple warriors
				if (currentWarriorWrapper == warriorWrapper1)
					turnController.replaceWarrior(warriorWrapper1, warriorWrapper1 = requestNextWarrior(wm1));
				else if (currentWarriorWrapper == warriorWrapper2)
					turnController.replaceWarrior(warriorWrapper2, warriorWrapper2 = requestNextWarrior(wm2));

				for (GeneralListener listener : listeners)
					listener.statsChanged(wm1.getName(), wm1.getCount(), wm2.getName(), wm2.getCount());

			} else {

				currentWarriorWrapper.startTurn();
				
				currentWarriorWrapper.getWarrior().checkAbilities();

				for (int i = 0; i < actionPerTurns; i++) {

					ExecutorService executor = Executors.newSingleThreadExecutor();
					Future<Action> future = executor.submit(new PlayTurnExecutor(currentWarriorWrapper, tick, i));

					try {
						currentWarriorAction = future.get(2, TimeUnit.SECONDS);						
					} catch (InterruptedException | ExecutionException | TimeoutException e) {
						future.cancel(true);
						System.err.println(e);
						currentWarriorWrapper.receiveDamage(1000);
						System.out.println("KILLED FOR TIMEOUT");
						break;
					}

					executor.shutdownNow();
					
					
					
					if (currentWarriorAction instanceof Move) {
					
						System.err.println("\t" +  currentWarriorWrapper.getWarrior().getName() + "\t" +
								((Move)currentWarriorAction).move() + "\t" +
								currentWarriorAction.getClass().getName() + "\t" + currentWarriorWrapper.getWarrior().getSpeed());
						
						executeMoveAction((Move) currentWarriorAction);
						
					} else if (currentWarriorAction instanceof Attack) {
						try {
							executeAttackAction((Attack) currentWarriorAction);
						} catch(Exception e) {
							System.err.println("Exception by: " + currentWarriorWrapper.getWarrior().getName());
							throw e;
						}
					} else if (currentWarriorAction instanceof Skip) {
						executeSkipAction();
					} else if (currentWarriorAction instanceof Suicide) {
						executeSuicideAction();
					} else if (currentWarriorAction instanceof BuildWall) {
						executeBuildWallAction((BuildWall) currentWarriorAction);
					}

					for (GeneralListener listener : listeners)
						listener.turnLapsed(tick, i, currentWarriorWrapper.getWarrior());

					try {
						Thread.sleep(20);
					} catch (InterruptedException ex) {
						Thread.currentThread().interrupt();
					}
				}
			}

			updateWorld();

			for (GeneralListener listener : listeners)
				inFight &= listener.continueFighting();

			inFight &= (wm1.getCount() <= warriorPerBattle && wm2.getCount() <= warriorPerBattle);

		} while (inFight);

		for (GeneralListener listener : listeners) {
			if (wm1.getCount() < wm2.getCount())
				listener.figthFinished(wm1);
			else
				listener.figthFinished(wm2);
		}
	}

	public void fightInRealTime() {
		TurnController turnController;
		Action currentWarriorAction;

		tick = 0;
		inFight = true;

		sideToShrink = false;
		leftColumnToShrink = 0;
		rightColumnToShrink = ConfigurationManager.getInstance().getMapWidth() - 1;

		int actionPerTurns = ConfigurationManager.getInstance().getActionsPerTurn();
		int warriorPerBattle = ConfigurationManager.getInstance().getMaxWarriorPerBattle();

		initCells();

		warriors = new HashMap<Warrior, WarriorWrapper>();

		warriorWrapper1 = requestNextWarrior(wm1);
		warriorWrapper2 = requestNextWarrior(wm2);

		// Create the hunter
		try {
			hunter = new Hunter("The Hunter", 10, 10, 10, 10, 5);
			hunterWrapper = new WarriorWrapper(hunter);
		} catch (RuleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		hunter.setPosition(getFreeCell());

		turnController = new TurnController(warriorWrapper1, warriorWrapper2, hunterWrapper);

		for (GeneralListener listener : listeners)
			listener.startFight();

		for (GeneralListener listener : listeners)
			listener.statsChanged(wm1.getName(), wm1.getCount(), wm2.getName(), wm2.getCount());

		do {
			tick++;

			currentWarriorWrapper = turnController.nextWarriorWrapper();

			System.out.println("Juega: " + currentWarriorWrapper.getWarrior().getName());

			if (currentWarriorWrapper.getWarrior().getHealth() <= 0) {
				// TODO: Change for multiple warriors
				if (currentWarriorWrapper == warriorWrapper1)
					turnController.replaceWarrior(warriorWrapper1, warriorWrapper1 = requestNextWarrior(wm1));
				else if (currentWarriorWrapper == warriorWrapper2)
					turnController.replaceWarrior(warriorWrapper2, warriorWrapper2 = requestNextWarrior(wm2));

				for (GeneralListener listener : listeners)
					listener.statsChanged(wm1.getName(), wm1.getCount(), wm2.getName(), wm2.getCount());

			} else {

				currentWarriorWrapper.startTurn();

				for (int i = 0; i < actionPerTurns; i++) {

					currentWarriorAction = currentWarriorWrapper.getWarrior().playTurn(tick, i);

					if (currentWarriorAction instanceof Move) {
						executeMoveAction((Move) currentWarriorAction);
					} else if (currentWarriorAction instanceof Attack) {
						executeAttackAction((Attack) currentWarriorAction);
					} else if (currentWarriorAction instanceof Skip) {
						executeSkipAction();
					} else if (currentWarriorAction instanceof Suicide) {
						executeSuicideAction();
					} else if (currentWarriorAction instanceof BuildWall) {
						executeBuildWallAction((BuildWall) currentWarriorAction);
					}

					for (GeneralListener listener : listeners)
						listener.turnLapsed(tick, i, currentWarriorWrapper.getWarrior());

					for (TimerListener listener : listeners)
						listener.turnLapsed(tick, i, currentWarriorWrapper.getWarrior());

					try {
						Thread.sleep(20);
					} catch (InterruptedException ex) {
						Thread.currentThread().interrupt();
					}
				}
			}

			updateWorld();

			for (GeneralListener listener : listeners)
				inFight &= listener.continueFighting();

			inFight &= (wm1.getCount() <= warriorPerBattle && wm2.getCount() <= warriorPerBattle);

		} while (inFight);

		for (GeneralListener listener : listeners) {
			if (wm1.getCount() < wm2.getCount())
				listener.figthFinished(wm1);
			else
				listener.figthFinished(wm2);
		}
	}

	private void executeBuildWallAction(BuildWall build) {

		if (!isPositionInRange(build.getCellToBuild()))
			return;

		if (currentWarriorWrapper.buildWall())
			build.getCellToBuild().setFieldCellType(FieldCellType.BLOCKED);
		else
			return;
	}

	private void updateWorld() {

		// if (random.nextInt(100) < 3)
		// changeWorld();

		if (random.nextInt(100) == 0)
			addNewSpecialItem();

		if (ConfigurationManager.getInstance().getTurnsToShrink() < this.tick
				&& this.tick % ConfigurationManager.getInstance().getShrinkStep() == 0) {
			shrinkWorld();
		}
	}

	private void shrinkWorld() {
		int columnToShrink;

		if (sideToShrink) {
			// Right side
			columnToShrink = rightColumnToShrink--;
		} else {
			// Left side
			columnToShrink = leftColumnToShrink++;
		}

		for (int i = 0; i < ConfigurationManager.getInstance().getMapHeight(); i++) {
			cells[columnToShrink][i].setFieldCellType(FieldCellType.BLOCKED);
			cells[columnToShrink][i].setHitPoints(Integer.MAX_VALUE);

			for (WarriorWrapper w : warriors.values()) {
				if (w.getWarrior().getPosition().equals(cells[columnToShrink][i])) {

					// TODO: Empujar warriors

					w.receiveDamage(1000);
				}
			}
		}

		// TODO:
		if (leftColumnToShrink == rightColumnToShrink) {
			System.out.println("GAME OVER!!!");
		}

		sideToShrink = !sideToShrink;
	}

	private void addNewSpecialItem() {
		FieldCell cell = getFieldCell(FieldCellType.NORMAL);
		cell.setItem(specialItemFactory.getSpecialItem());
	}

	// private void changeWorld() {
	//
	// FieldCell oldCell, newCell;
	//
	// oldCell = getFieldCell(FieldCellType.BLOCKED);
	// newCell = getFieldCell(FieldCellType.NORMAL);
	//
	// boolean validCell = false;
	//
	// while (!validCell) {
	//
	// validCell = true;
	//
	// for (Warrior w : this.warriors.keySet()) {
	// if (newCell.equals(w.getPosition()))
	// validCell = false;
	// }
	// }
	//
	// oldCell.setFieldCellType(FieldCellType.NORMAL);
	// newCell.setFieldCellType(FieldCellType.BLOCKED);
	//
	// for (GeneralListener listener : listeners)
	// listener.worldChanged(oldCell, newCell);
	//
	// for (Warrior w : this.warriors.keySet())
	// w.worldChanged(oldCell, newCell);
	// }

	private void executeSkipAction() {

	}

	private void executeSuicideAction() {
		FieldCell center = currentWarriorWrapper.getWarrior().getPosition();

		List<FieldCell> innerAdjs = BattleField.getInstance().getAdjacentCells(center);
		List<FieldCell> middleAdjs = new ArrayList<FieldCell>();
		List<FieldCell> outterAdjs = new ArrayList<FieldCell>();

		for (FieldCell fc : innerAdjs)
			middleAdjs.addAll(BattleField.getInstance().getAdjacentCells(fc));

		for (FieldCell fc : middleAdjs)
			outterAdjs.addAll(BattleField.getInstance().getAdjacentCells(fc));

		int currentHealth = currentWarriorWrapper.getWarrior().getHealth();
		int totalHealth = currentWarriorWrapper.getWarrior().getInitialHealth();
		int porcentualDamage = (currentHealth * 100) / totalHealth;

		currentWarriorWrapper.getWarrior().setHealth(0);
		for (GeneralListener listener : listeners)
			listener.warriorKilled(currentWarriorWrapper.getWarrior());

		// Destroy walls and special items
		for (FieldCell fc : innerAdjs) {
			if (fc.getFieldCellType() == FieldCellType.BLOCKED) {
				fc.receiveDamage(fc.remainingLive());
			}
			fc.removeItem();
		}

		for (FieldCell fc : middleAdjs) {
			if (fc.getFieldCellType() == FieldCellType.BLOCKED) {
				fc.receiveDamage(fc.remainingLive());
			}
			fc.removeItem();
		}

		for (FieldCell fc : outterAdjs) {
			if (fc.getFieldCellType() == FieldCellType.BLOCKED) {
				fc.receiveDamage(fc.remainingLive());
			}
			fc.removeItem();
		}

		// Damage enemy warrior
		for (Warrior w : this.warriors.keySet()) {
			if (w != currentWarriorWrapper.getWarrior()) {

				int damage = 0;

				if (innerAdjs.contains(w.getPosition()))
					damage = (w.getHealth() * porcentualDamage) / 100;

				if (middleAdjs.contains(w.getPosition()))
					damage = (w.getHealth() * porcentualDamage * 9) / 1000;

				if (outterAdjs.contains(w.getPosition()))
					damage = (w.getHealth() * porcentualDamage * 7) / 1000;

				warriors.get(w).receiveDamage(damage);

				/*
				 * TODO: Code repeated
				 */

				w.wasAttacked((int) damage, center);

				for (GeneralListener listener : listeners)
					listener.warriorAttacked(w, currentWarriorWrapper.getWarrior(), (int) damage);

				if (w.getHealth() <= 0) {

					currentWarriorWrapper.getWarrior().enemyKilled();

					for (GeneralListener listener : listeners)
						listener.warriorKilled(w);
				}
			}
		}
	}

	private void executeAttackAction(Attack attack) {
		Warrior attackedWarrior = findWarriorInMap(attack.getCellToAttack());

		if (attackedWarrior == hunter) {
			hunter.wasAttacked(0, currentWarriorWrapper.getWarrior().getPosition());
			return;
		}

		if (!canFire(currentWarriorWrapper.getWarrior().getPosition(), attackedWarrior.getPosition()))
			return;

		float damage = currentWarriorWrapper.getWarrior().getStrength();

		if (attackedWarrior == null && isPositionInRange(attack.getCellToAttack())) {
			FieldCell attackedFieldCell = attack.getCellToAttack();
			attackedFieldCell.receiveDamage((int) damage);
			return;
		}

		if (!isWarriorInRange(attackedWarrior))
			return;

		float distance = calculateDistance(currentWarriorWrapper.getWarrior().getPosition(),
				attackedWarrior.getPosition());

		float range = currentWarriorWrapper.getWarrior().getRange();

		float distanceFactor = 1 - (distance - 1) / range;

		damage *= distanceFactor;

		float defense = attackedWarrior.getDefense();
		defense = (float) (defense * (1 - Math.abs(random.nextGaussian())));

		damage -= defense;

		if (damage > 0) {

			warriors.get(attackedWarrior).receiveDamage((int) damage);

			attackedWarrior.wasAttacked((int) damage, currentWarriorWrapper.getWarrior().getPosition());

			for (GeneralListener listener : listeners)
				listener.warriorAttacked(attackedWarrior, currentWarriorWrapper.getWarrior(), (int) damage);

			if (attackedWarrior.getHealth() <= 0) {

				currentWarriorWrapper.getWarrior().enemyKilled();

				for (GeneralListener listener : listeners)
					listener.warriorKilled(attackedWarrior);
			}
		}
	}

	private boolean canFire(FieldCell source, FieldCell target) {

		return true;
	}

	private Warrior findWarriorInMap(FieldCell cellToAttack) {

		for (Warrior w : warriors.keySet()) {
			if (w.getPosition() == cellToAttack)
				return w;
		}

		return null;
	}

	public float calculateDistance(FieldCell source, FieldCell target) {
		double distance;

		distance = Math.pow(source.getX() - target.getX(), 2) + Math.pow(source.getY() - target.getY(), 2);
		distance = Math.sqrt(distance);

		return (float) distance;
	}

	public List<FieldCell> getAdjacentCells(FieldCell fieldCell) {
		ArrayList<FieldCell> adjCells = new ArrayList<FieldCell>();

		int x = fieldCell.getX();
		int y = fieldCell.getY();

		if (x < configurationManager.getMapWidth() - 1)
			adjCells.add(cells[x + 1][y]);

		if (y < configurationManager.getMapHeight() - 1)
			adjCells.add(cells[x][y + 1]);

		if (x > 0)
			adjCells.add(cells[x - 1][y]);

		if (y > 0)
			adjCells.add(cells[x][y - 1]);

		if (x > 0 && y > 0)
			adjCells.add(cells[x - 1][y - 1]);

		if (x < configurationManager.getMapWidth() - 1 && y < configurationManager.getMapHeight() - 1)
			adjCells.add(cells[x + 1][y + 1]);

		if (x > 0 && y < configurationManager.getMapHeight() - 1)
			adjCells.add(cells[x - 1][y + 1]);

		if (x < configurationManager.getMapWidth() - 1 && y > 0)
			adjCells.add(cells[x + 1][y - 1]);

		return adjCells;
	}

	private void executeMoveAction(Move action) {
		try {
			ArrayList<FieldCell> currentWarriorActionsMoveCells;
			currentWarriorActionsMoveCells = action.move();

			FieldCell previousCell = currentWarriorWrapper.getWarrior().getPosition();

			for (FieldCell fieldCell : currentWarriorActionsMoveCells) {

				if (!(fieldCell.getX() >= 0 && fieldCell.getX() < ConfigurationManager.getInstance().getMapWidth()
						&& fieldCell.getY() >= 0 && fieldCell.getY() < ConfigurationManager.getInstance().getMapHeight()
						&& (getFieldCell(fieldCell.getX(), fieldCell.getY()))
								.getFieldCellType() == FieldCellType.NORMAL)) {

					break;
				}

				if (fieldCell.getX() == previousCell.getX() || fieldCell.getY() == previousCell.getY())
					currentWarriorWrapper.doStep(fieldCell.getCost());
				else
					currentWarriorWrapper.doStep(fieldCell.getCost() * 1.41f);

				List<FieldCell> adj = getAdjacentCells(previousCell);
				if (!adj.contains(fieldCell)) {
					if (fieldCell != previousCell) {
						currentWarriorWrapper.receiveDamage(currentWarriorWrapper.getWarrior().getHealth());
						System.err.println("Esta trampeando " + currentWarriorWrapper.getWarrior().getName() + "!!!! "
								+ previousCell + " -> " + fieldCell);
						break;
					}

				}

				if (currentWarriorWrapper.getSteps() > currentWarriorWrapper.getWarrior().getSpeed() / 5)
					return;

				previousCell = fieldCell;

				if (fieldCell.getItem() != null) {
					
					if (fieldCell.getItem() instanceof SpecialItem) {
					
						if (currentWarriorWrapper.getWarrior().useSpecialItem()) {
							SpecialItem si = (SpecialItem) fieldCell.removeItem();
							si.affectWarrior(currentWarriorWrapper);
						}
						
					} else if (fieldCell.getItem() instanceof Ability) {
						
						//currentWarriorWrapper.getWarrior().getAbilities().add((Ability)fieldCell.getItem());
						
					}
				}

				for (GeneralListener listener : listeners)
					listener.warriorMoved(currentWarriorWrapper.getWarrior(),
							currentWarriorWrapper.getWarrior().getPosition(), fieldCell);

				try {
					currentWarriorWrapper.getWarrior().setPosition(fieldCell);
				} catch (RuleException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		} catch (Exception e) {
			System.err.println("Fuite amego: " + currentWarriorWrapper.getWarrior().getName());
			throw e;
		}
	}

	Warrior getWarrior1() {
		return this.warriorWrapper1.getWarrior();
	}

	Warrior getWarrior2() {
		return this.warriorWrapper2.getWarrior();
	}

	public long getTick() {
		return this.tick;
	}

	public void showResult() {
		// TODO Auto-generated method stub
		System.out.println("Termino la batalla. " + tick);

	}

	public void addListener(GeneralListener listener) {
		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	public void addListener(TimerListener listener) {
		if (!timerListeners.contains(listener))
			timerListeners.add(listener);

	}

}

class PlayTurnExecutor implements Callable<Action> {
	private WarriorWrapper warriorWrapper;
	private long tick;
	private int actionNumber;

	public PlayTurnExecutor(WarriorWrapper warriorWrapper, long tick, int actionNumber) {
		this.warriorWrapper = warriorWrapper;
		this.tick = tick;
		this.actionNumber = actionNumber;
	}

	@Override
	public Action call() throws Exception {
		return warriorWrapper.getWarrior().playTurn(tick, actionNumber);
	}
}