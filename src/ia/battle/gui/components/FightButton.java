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

package ia.battle.gui.components;

import ia.battle.gui.ClassFinder;
import ia.battle.gui.ClassFinder.ClassFinderObserver;

import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;

public class FightButton extends JButton implements ClassFinderObserver {

	private static final long serialVersionUID = 4015918365089316502L;
	private List<ClassFinder> classFinders;

	public FightButton(ClassFinder... classFinders) {
		super("Fight!");
		this.classFinders = Arrays.asList(classFinders);
		for (ClassFinder classFinder: classFinders)
			classFinder.addSelectionChangeObserver(this);
		setEnabled(false);
	}

	public void updateEnabledStatus() {
		Boolean newStatus = true;
		for (ClassFinder classFinder : classFinders) {
			if (!(classFinder.selectedJarExists() && classFinder
					.hasSelectedClass()))
				newStatus = false;
		}
		setEnabled(newStatus);
	}

	@Override
	public void fileSelectionChange(String filename) {
		updateEnabledStatus();
	}

	@Override
	public void classSelectionChange(String classname) {
		updateEnabledStatus();
	}

}
