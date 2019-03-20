/*
 * Copyright (c) 2012-2015, Ing. Gabriel Barrera <gmbarrera@gmail.com>
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

import ia.battle.core.actions.Action;
import ia.battle.core.actions.Attack;
import ia.battle.core.actions.Move;
import ia.exceptions.RuleException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Hunter extends Warrior {
	private FieldCell position;
	private Warrior targetWarrior;
	private int attention;
	
	Hunter(String name, int health, int defense, int strength, int speed,
			int range) throws RuleException {
		super(name, health, defense, strength, speed, range);
		
	}

	public FieldCell getPosition() {
		return position;
	}

	void setPosition(FieldCell position) {
		this.position = position;
	}

	public Action playTurn(long tick, int actionNumber) {
		Action action;
		
		int closerDistance = Integer.MAX_VALUE, distance;

		if (attention == 0) {
			try {
				for (Warrior warrior : BattleField.getInstance().getWarriors()) {
					if (warrior != this) {
						distance = computeDistance(this.position, warrior.getPosition());
						if (closerDistance > distance) {
							closerDistance = distance;
							targetWarrior = warrior;
						}
					}
				}
				attention = new Random().nextInt(100) + 50;

			} catch (RuleException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		attention--;
		
		if (BattleField.getInstance().isWarriorInRange(targetWarrior)) {

			action = new Attack(targetWarrior.getPosition());
			
		} else {
			
			List<FieldCell> adj = BattleField.getInstance().getAdjacentCells(this.position);
			FieldCell nextMove = this.position;
			closerDistance = Integer.MAX_VALUE;
			
			for(FieldCell cell : adj) {
				distance = computeDistance(cell, targetWarrior.getPosition());
				if ((closerDistance > distance) && (cell.getFieldCellType() != FieldCellType.BLOCKED) &&
						!cell.equals(this.position)) {
					nextMove = cell;
					closerDistance = distance;
				}
			}
			
			action = new HunterMove(nextMove);
		}

		return action;
	}

	private int computeDistance(FieldCell source, FieldCell target) {
		int distance = 0;

		distance = Math.abs(target.getX() - source.getX());
		distance += Math.abs(target.getY() - source.getY());

		return distance;
	}

	@Override
	public void wasAttacked(int damage, FieldCell source) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enemyKilled() {
		System.out.println("Warrior Killed by the Hunter!");
		attention = 0;
	}

	@Override
	public void worldChanged(FieldCell oldCell, FieldCell newCell) {
		// TODO Auto-generated method stub

	}
}

class HunterMove extends Move {

	private FieldCell nextMove;
	
	HunterMove (FieldCell cell) {
		nextMove = cell;
	}

	@Override
	public ArrayList<FieldCell> move() {
		ArrayList<FieldCell> cells = new ArrayList<FieldCell>();
		cells.add(nextMove);
		return cells;
	}
}

