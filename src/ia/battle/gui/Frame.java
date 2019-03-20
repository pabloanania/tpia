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

package ia.battle.gui;

import ia.battle.core.BattleField;

import javax.swing.JFrame;

public class Frame extends JFrame {

    private static final long serialVersionUID = 4075492093234749056L;
    private FieldBoard mainBoard;

    FieldBoard getFieldBoard() {
    	return this.mainBoard;
    }
    
	public Frame(BattleField battleField, int offset_x, int offset_y) {

		mainBoard = new FieldBoard(battleField, offset_x, offset_y);
		setTitle("Battle Camp");
		setContentPane(mainBoard);
		setResizable(false);
	}

}