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

import java.util.ArrayList;
import java.util.Collections;

class TurnController {
	private ArrayList<WarriorWrapper> wrappers;
	private int currentPosition;
	
	TurnController(ArrayList<WarriorWrapper> wrappers) {
		this.wrappers = wrappers;
		Collections.shuffle(wrappers);
	}
	
	TurnController(WarriorWrapper... warriorWrappers ) {
		wrappers = new ArrayList<WarriorWrapper>();
		for(WarriorWrapper ww : warriorWrappers) {
			wrappers.add(ww);
		}
		Collections.shuffle(wrappers);
	}
	
	void addWarriorWrapper(WarriorWrapper warriorWrapper) {
		wrappers.add(warriorWrapper);
		Collections.shuffle(wrappers);
	}

	WarriorWrapper nextWarriorWrapper() {
		currentPosition = (currentPosition + 1) % wrappers.size();
		return wrappers.get(currentPosition);
	}

	void replaceWarrior(WarriorWrapper oldWarrior, WarriorWrapper newWarrior) {
		int position = wrappers.indexOf(oldWarrior);
		wrappers.set(position, newWarrior);
	}
}
