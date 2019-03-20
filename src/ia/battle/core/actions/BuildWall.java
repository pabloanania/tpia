package ia.battle.core.actions;

import ia.battle.core.FieldCell;

public final class BuildWall extends Action {
	private FieldCell cellToBuild;
	
	public BuildWall(FieldCell cellToBuild) {
		this.cellToBuild = cellToBuild;
	}
	
	public FieldCell getCellToBuild() {
		return cellToBuild;
	}

}
