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
import java.util.Iterator;

import ia.battle.core.abilities.Ability;
import ia.battle.core.actions.Action;
import ia.exceptions.RuleException;

public abstract class Warrior {

	private String name;
	private int health, defense, strength, speed, range;
	private int initialHealth, initialDefense, initialStrength, initialSpeed, initialRange;
	private WarriorManager warriorManager;
	private ArrayList<Ability> abilities;

	public final int getInitialHealth() {
		return initialHealth;
	}

	public final int getInitialDefense() {
		return initialDefense;
	}

	public final int getInitialStrength() {
		return initialStrength;
	}

	public final int getInitialSpeed() {
		return initialSpeed;
	}

	public final int getInitialRange() {
		return initialRange;
	}

	public final WarriorManager getWarriorManager() {
		return this.warriorManager;
	}

	void setWarriorManager(WarriorManager warriorManager) {
		this.warriorManager = warriorManager;
	}

	private FieldCell position;

	public Warrior(String name, int health, int defense, int strength, int speed, int range) throws RuleException {

		this.abilities = new ArrayList<Ability>();

		this.name = name;

		this.health = health;
		this.defense = defense;
		this.strength = strength;
		this.speed = speed;
		this.range = range;

		this.initialHealth = health;
		this.initialDefense = defense;
		this.initialStrength = strength;
		this.initialSpeed = speed;
		this.initialRange = range;

		int sum = this.health + this.defense + this.strength + this.speed + this.range;

		if (sum > ConfigurationManager.getInstance().getMaxPointsPerWarrior())
			throw new RuleException();

		if (health <= 0 || defense <= 0 || strength <= 0 || speed <= 0 || range <= 0)
			throw new RuleException();

		int maxRange = ConfigurationManager.getInstance().getMaxRangeForWarrior();
		if (range > maxRange)
			this.range = maxRange;
	}

	public final void setHealth(int health) {
		this.health = health;
	}

	public final void setDefense(int defense) {
		this.defense = defense;
	}

	public final void setStrength(int strength) {
		this.strength = strength;
	}

	public final void setSpeed(int speed) {
		this.speed = speed;
	}

	public final void setRange(int range) {
		this.range = range;
	}

	public String getName() {

		return name;
	}

	public final int getHealth() {
		return health;
	}

	public final int getDefense() {
		return defense;
	}

	public final int getStrength() {
		return strength;
	}

	public final int getSpeed() {
		return speed;
	}

	public final int getRange() {
		return range;
	}

	public FieldCell getPosition() {
		return position;
	}

	void setPosition(FieldCell position) throws RuleException {
		this.position = position;
	}

	public ArrayList<Ability> getAbilities() {
		return this.abilities;
	}

	public void checkAbilities() {

		Iterator<Ability> itr = abilities.iterator();
		while (itr.hasNext()) {
			Ability ability = itr.next();

			if (ability.getTurnsToDisable() <= 0)
				itr.remove();
		}
	}

	public abstract Action playTurn(long tick, int actionNumber);

	public abstract void wasAttacked(int damage, FieldCell source);

	public abstract void enemyKilled();

	public abstract void worldChanged(FieldCell oldCell, FieldCell newCell);

	public boolean useSpecialItem() {
		return true;
	}

	void setName(String name) {
		this.name = name;
	}
}
