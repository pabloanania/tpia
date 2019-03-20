/*
 * Copyright (c) 2012-2014, Ing. Gabriel Barrera <gmbarrera@gmail.com>
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

public class WarriorWrapper extends Attackable {
	private Warrior warrior;
	private float stepsInTurn;
	private boolean hasBuildWall;

	WarriorWrapper(Warrior warrior) {
		this.warrior = warrior;
	}

	public Warrior getWarrior() {
		return warrior;
	}

	void startTurn() {
		stepsInTurn = 0;
		hasBuildWall = false;
	}

	void doStep() {
		stepsInTurn++;
	}

	void doStep(float step) {
		stepsInTurn += step;
	}

	public float getSteps() {
		return stepsInTurn;
	}

	@Override
	void receiveDamage(int damage) {
		if (warrior.getHealth() <= damage)
			warrior.setHealth(0);
		else
			warrior.setHealth(warrior.getHealth() - damage);
	}

	@Override
	public int remainingLive() {
		return warrior.getHealth();
	}

	public boolean buildWall() {
		boolean previousValue = hasBuildWall;
		hasBuildWall = true;
		return !previousValue;
	}
}
