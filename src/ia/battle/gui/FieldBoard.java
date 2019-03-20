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

package ia.battle.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import ia.battle.core.BattleField;
import ia.battle.core.ConfigurationManager;
import ia.battle.core.FieldCellType;

public class FieldBoard extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = -1351031450255485371L;
    private static final Image grass = new ImageIcon(FieldBoard.class.getResource("grass.jpg")).getImage();
    private static final Image swamp = new ImageIcon(FieldBoard.class.getResource("swamp.jpg")).getImage();
    private static final Image rocks = new ImageIcon(FieldBoard.class.getResource("wall.png")).getImage();
    
    //TODO: Move to a List<Warrior>
    private static final Image warrior1 = new ImageIcon(FieldBoard.class.getResource("warrior1.png")).getImage();
    private static final Image warrior2 = new ImageIcon(FieldBoard.class.getResource("warrior2.png")).getImage();
    private static final Image hunter = new ImageIcon(FieldBoard.class.getResource("hunter.png")).getImage();
    
    private static final Image fogOfWar = new ImageIcon(FieldBoard.class.getResource("fogOfWar.png")).getImage();
    private static final Image HPorange = new ImageIcon(FieldBoard.class.getResource("HPorange.png")).getImage();
    private static final Image HPviolet = new ImageIcon(FieldBoard.class.getResource("HPviolet.png")).getImage();
    private static final Image HPempty = new ImageIcon(FieldBoard.class.getResource("HPempty.png")).getImage();
    private static final Image box = new ImageIcon(FieldBoard.class.getResource("box.png")).getImage();

    private static final Integer cellWidth = 16;
    private static final Integer cellHeight = 16;

    private BattleField battleField;
    private int offset_x, offset_y;

    private String warriorManagerName1;
    private int warriorsKilled1;
    private String warriorManagerName2;
    private int warriorsKilled2;
    

	public FieldBoard(BattleField battleField, int offset_x, int offset_y) {

        this.battleField = battleField;
        this.offset_x = offset_x;
        this.offset_y = offset_y;

    }

    String getWarriorManagerName1() {
		return warriorManagerName1;
	}

	void setWarriorManagerName1(String warriorManagerName1) {
		this.warriorManagerName1 = warriorManagerName1;
	}

	int getWarriorsKilled1() {
		return warriorsKilled1;
	}

	void setWarriorsKilled1(int warriorsKilled1) {
		this.warriorsKilled1 = warriorsKilled1;
	}

	String getWarriorManagerName2() {
		return warriorManagerName2;
	}

	void setWarriorManagerName2(String warriorManagerName2) {
		this.warriorManagerName2 = warriorManagerName2;
	}

	int getWarriorsKilled2() {
		return warriorsKilled2;
	}

	void setWarriorsKilled2(int warriorsKilled2) {
		this.warriorsKilled2 = warriorsKilled2;
	}
	
    public void paint(Graphics g) {
        int i, j;
        super.paint(g);

        int width = ConfigurationManager.getInstance().getMapWidth();
        int height = ConfigurationManager.getInstance().getMapHeight();

        int vision1 = 1;
        int vision2 = 1;
        int vision3 = 1;

        int health1 = 100;
        int actualHealth1 = 100;

        int health2 = 100;
        int actualHealth2 = 100;
        
        int health3 = 100;
        int actualHealth3 = 100;

        int x1 = 0, y1 = 0, x2 = 0, y2 = 0, x3 = 0, y3 = 0;

        String nombreW1 = "1";
        String nombreW2 = "2";
        String nombreW3 = "Hunter";

        Font ft = new Font("Times New Roman", Font.PLAIN, 12);
        g.setColor(Color.RED);
        g.setFont(ft);
        
        try {
	        for (i = 0; i < width; i++) {
	            for (j = 0; j < height; j++) {
	            	
	            	if (battleField.getFieldCell(i, j).getCost() > 1f)
	            		g.drawImage(swamp, (i) * cellWidth + offset_x, (j) * cellHeight + offset_y, cellWidth, cellHeight, this);
	            	else
	            		g.drawImage(grass, (i) * cellWidth + offset_x, (j) * cellHeight + offset_y, cellWidth, cellHeight, this);
	                
                    if (battleField.getFieldCell(i, j).getFieldCellType() == FieldCellType.BLOCKED) {
                        g.drawImage(rocks, i * cellWidth + offset_x, j * cellHeight + offset_y, cellWidth, cellHeight,
                                this);
                    } else {
                        
                        if (battleField.getFieldCell(i, j).hasItem())
                            g.drawImage(box, i * cellWidth + offset_x, j * cellHeight + offset_y, cellWidth, cellHeight, this);
                    }
	            }
	        }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Font f = new Font("Times New Roman", Font.PLAIN, 20);
        g.setFont(f);
        
        try {

            x1 = battleField.getWarriors().get(0).getPosition().getX();
            y1 = battleField.getWarriors().get(0).getPosition().getY();

            x2 = battleField.getWarriors().get(1).getPosition().getX();
            y2 = battleField.getWarriors().get(1).getPosition().getY();

            x3 = battleField.getWarriors().get(2).getPosition().getX();
            y3 = battleField.getWarriors().get(2).getPosition().getY();
            
            nombreW1 = battleField.getWarriors().get(0).getName();
            nombreW2 = battleField.getWarriors().get(1).getName();
            nombreW3 = battleField.getWarriors().get(2).getName();

            vision1 = battleField.getWarriors().get(0).getRange();
            vision2 = battleField.getWarriors().get(1).getRange();
            vision3 = battleField.getWarriors().get(2).getRange();

            health1 = battleField.getWarriors().get(0).getInitialHealth();
            health2 = battleField.getWarriors().get(1).getInitialHealth();
            health3 = battleField.getWarriors().get(2).getInitialHealth();

            actualHealth1 = battleField.getWarriors().get(0).getHealth();
            actualHealth2 = battleField.getWarriors().get(1).getHealth();
            actualHealth3 = battleField.getWarriors().get(2).getHealth();

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Draw the first warrior
        if (actualHealth1 > 0) {
            g.drawImage(warrior1, x1 * cellWidth + offset_x, y1 * cellHeight + offset_y, cellWidth, cellHeight, this);

            g.setColor(Color.MAGENTA);
            g.drawString(nombreW1, (x1 * cellWidth) - (45 - 7) + offset_x, (y1 * cellHeight) - 25 + offset_y);
            g.drawImage(HPempty, (x1 * cellWidth) - (45 - 7) + offset_x, (y1 * cellHeight) - 25 + offset_y, 90, 5, this);
            g.drawImage(HPviolet, (x1 * cellWidth) - (45 - 7) + offset_x, (y1 * cellHeight) - 25 + offset_y,
                    actualHealth1 * 90 / health1, 5, this);

        }

        // Draw the second warrior
        if (actualHealth2 > 0) {
            g.drawImage(warrior2, x2 * cellWidth + offset_x, y2 * cellHeight + offset_y, cellWidth, cellHeight, this);

            g.setColor(Color.ORANGE);
            g.drawString(nombreW2, (x2 * cellWidth) - (45 - 7) + offset_x, (y2 * cellHeight) - 30 + offset_y);
            g.drawImage(HPempty, (x2 * cellWidth) - (45 - 7) + offset_x, (y2 * cellHeight) - 30 + offset_y, 90, 5, this);
            g.drawImage(HPorange, (x2 * cellWidth) - (45 - 7) + offset_x, (y2 * cellHeight) - 30 + offset_y,
                    actualHealth2 * 90 / health2, 5, this);
        }

        
        // Draw the hunter
        if (actualHealth3 > 0) {
            g.drawImage(hunter, x3 * cellWidth + offset_x, y3 * cellHeight + offset_y, cellWidth, cellHeight, this);

            g.setColor(Color.RED);
            g.drawString(nombreW3, (x3 * cellWidth) - (45 - 7) + offset_x, (y3 * cellHeight) - 30 + offset_y);
            g.drawImage(HPempty, (x3 * cellWidth) - (45 - 7) + offset_x, (y3 * cellHeight) - 30 + offset_y, 90, 5, this);
            g.drawImage(HPorange, (x3 * cellWidth) - (45 - 7) + offset_x, (y3 * cellHeight) - 30 + offset_y,
                    actualHealth3 * 90 / health3, 5, this);
        }
        
        
        
        for (i = 0; i < width; i++) {

            for (j = 0; j < height; j++) {

                if (!(((Math.pow(i - x1, 2)) + (Math.pow(j - y1, 2)) <= Math.pow(vision1, 2) && (actualHealth1 > 0))
                   || ((Math.pow(i - x2, 2)) + (Math.pow(j - y2, 2)) <= Math.pow(vision2, 2) && (actualHealth2 > 0))
                   || ((Math.pow(i - x3, 2)) + (Math.pow(j - y3, 2)) <= Math.pow(vision3, 2) && (actualHealth3 > 0))))

                    g.drawImage(fogOfWar, (i) * cellWidth + offset_x, (j) * cellHeight + offset_y, cellWidth,
                            cellHeight, this);

            }
        }

        g.setColor(Color.BLACK);
        
        
        
        g.drawString(battleField.getTick() + "/10000", +ConfigurationManager.getInstance().getMapHeight() * cellHeight
                + offset_x, +offset_y);
        
        g.drawString(warriorManagerName1 + ": " + Integer.toString(warriorsKilled1), +ConfigurationManager.getInstance().getMapHeight() * cellHeight
                - 200 + offset_x, +offset_y);
        
        g.drawString(warriorManagerName2 + ": " + Integer.toString(warriorsKilled2), +ConfigurationManager.getInstance().getMapHeight() * cellHeight
                - 500 + offset_x, +offset_y);
        
        
    }
}
