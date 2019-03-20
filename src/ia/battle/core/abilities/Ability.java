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

package ia.battle.core.abilities;

import java.util.List;
import java.util.stream.Collectors;

import ia.battle.core.BattleField;
import ia.battle.core.Grabbable;
import ia.battle.core.TimerListener;
import ia.battle.core.Warrior;

public abstract class Ability implements Grabbable {
	private int turnsToDisable = 20;
	private boolean isActive;

	public Ability() {

		BattleField.getInstance().addListener(new TimerListener() {

			@Override
			public void turnLapsed(long tick, int turnNumber, Warrior warrior) {
				if (isActive) {
					turnsToDisable--;
					if (turnsToDisable <= 0) {
						isActive = false;
					}
				}
			}

			@Override
			public void tickLapsed(long tick) {
			}
		});
	}

	public int getTurnsToDisable() {
		return this.turnsToDisable;
	}

	public final boolean activate() {
		isActive = (turnsToDisable > 0);
		return isActive;
	}

	public final void deactivate() {
		isActive = false;
	}

	public boolean isActive() {
		return isActive;
	}

	/**
	 * Return the first Stealth Ability collected
	 * @param warrior
	 * @return
	 */
	public static StealthAbility getStealthAbility(Warrior warrior) {

		List<Ability> ab = warrior.getAbilities().stream().filter(u -> u instanceof StealthAbility)
				.collect(Collectors.toList());
		if (ab.size() > 0)
			return (StealthAbility) ab.get(0);

		return null;
	}
}
