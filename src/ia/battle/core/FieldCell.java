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

public class FieldCell extends Attackable {
	private int x, y;
	private FieldCellType fieldCellType;
	private Grabbable grabbableItem;
	private float cost;
	private int hitPoints;

	FieldCell(FieldCellType type, int x, int y, Grabbable grabbableItem, float cost) {
		this.fieldCellType = type;
		this.x = x;
		this.y = y;
		this.grabbableItem = grabbableItem;
		this.cost = cost;
		this.hitPoints = ConfigurationManager.getInstance().getFieldCellHitPoints();
	}

	public FieldCellType getFieldCellType() {
		return fieldCellType;
	}

	void setFieldCellType(FieldCellType fieldCellType) {
		this.fieldCellType = fieldCellType;
	}

	void setHitPoints(int hitPoints) {
		this.hitPoints = hitPoints;
	}

	Grabbable getItem() {
		return grabbableItem;
	}

	public boolean hasItem() {
		return grabbableItem != null;
	}

	Grabbable removeItem() {
		Grabbable si = grabbableItem;
		grabbableItem = null;

		return si;
	}

	void setItem(Grabbable si) {
		grabbableItem = si;
	}

	public float getCost() {
		return cost;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String toString() {
		String aux = " ";

		switch (fieldCellType) {
		case NORMAL:
			aux = "N";
			break;

		case BLOCKED:
			aux = "B";
			break;
		}

		return aux + " [" + x + ", " + y + "]";
	}

	public boolean equals(Object other) {
		if (other == null)
			return false;

		if (!(other instanceof FieldCell))
			return false;

		if (((FieldCell) other).x != this.x || ((FieldCell) other).y != this.y)
			return false;

		return true;
	}

	@Override
	void receiveDamage(int damage) {
		if (this.hitPoints <= damage) {
			this.hitPoints = 0;
			this.fieldCellType = FieldCellType.NORMAL;
			this.cost = 1f;
		} else
			this.hitPoints -= damage;
	}

	@Override
	public int remainingLive() {
		return this.hitPoints;
	}
}
