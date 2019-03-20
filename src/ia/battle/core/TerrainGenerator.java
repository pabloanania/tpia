/*
 * Copyright (c) 2012-2016, Ing. Gabriel Barrera <gmbarrera@gmail.com>
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

import java.util.Random;

class TerrainGenerator {
	private int width;
	private int height;

	public TerrainGenerator(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int[][] getMaze() {
		int x, y;
		int[][] maze = new int[width][height];
		Random rnd = new Random();

		int swampPoints = rnd.nextInt(40) + 20;

		for (int i = 0; i < swampPoints; i++) {
			
			if (rnd.nextBoolean()) {
				x = rnd.nextInt(width - 6);
				y = rnd.nextInt(height - 3);
				
				maze[x][y] = 2;	
				
				maze[x + 1][y] = 2;
				maze[x + 2][y] = 2;
				maze[x + 3][y] = 2;
				maze[x + 4][y] = 2;
				maze[x + 5][y] = 2;
				
				maze[x][y + 1] = 2;
				maze[x + 1][y + 1] = 2;
				maze[x + 2][y + 1] = 2;
				maze[x + 3][y + 1] = 2;
				maze[x + 4][y + 1] = 2;
				maze[x + 5][y + 1] = 2;
				
				maze[x][y + 2] = 2;
				maze[x + 1][y + 2] = 2;
				maze[x + 2][y + 2] = 2;
				maze[x + 3][y + 2] = 2;
				maze[x + 4][y + 2] = 2;
				maze[x + 5][y + 2] = 1;
				
			} else {
				x = rnd.nextInt(width - 3);
				y = rnd.nextInt(height - 6);
				
				maze[x][y] = 2;
				
				maze[x][y + 1] = 2;
				maze[x][y + 2] = 2;
				maze[x][y + 3] = 2;
				maze[x][y + 4] = 2;
				maze[x][y + 5] = 2;
				
				maze[x + 1][y] = 2;
				maze[x + 1][y + 1] = 2;
				maze[x + 1][y + 2] = 2;
				maze[x + 1][y + 3] = 2;
				maze[x + 1][y + 4] = 2;
				maze[x + 1][y + 5] = 2;
				
				maze[x + 2][y] = 2;
				maze[x + 2][y + 1] = 2;
				maze[x + 2][y + 2] = 2;
				maze[x + 2][y + 3] = 2;
				maze[x + 2][y + 4] = 2;
				maze[x + 2][y + 5] = 2;
			}
		}
		
		int wallPoints = rnd.nextInt(40) + 20;

		for (int i = 0; i < wallPoints; i++) {
			
			if (rnd.nextBoolean()) {
				x = rnd.nextInt(width - 6);
				y = rnd.nextInt(height - 3);
				
				maze[x][y] = 1;	
				
				maze[x + 1][y] = 1;
				maze[x + 2][y] = 1;
				maze[x + 3][y] = 1;
				maze[x + 4][y] = 1;
				maze[x + 5][y] = 1;
				
				maze[x][y + 1] = 1;
				maze[x + 1][y + 1] = 1;
				maze[x + 2][y + 1] = 1;
				maze[x + 3][y + 1] = 1;
				maze[x + 4][y + 1] = 1;
				maze[x + 5][y + 1] = 1;
				
				maze[x][y + 2] = 1;
				maze[x + 1][y + 2] = 1;
				maze[x + 2][y + 2] = 1;
				maze[x + 3][y + 2] = 1;
				maze[x + 4][y + 2] = 1;
				maze[x + 5][y + 2] = 1;
				
			} else {
				x = rnd.nextInt(width - 3);
				y = rnd.nextInt(height - 6);
				
				maze[x][y] = 1;
				
				maze[x][y + 1] = 1;
				maze[x][y + 2] = 1;
				maze[x][y + 3] = 1;
				maze[x][y + 4] = 1;
				maze[x][y + 5] = 1;
				
				maze[x + 1][y] = 1;
				maze[x + 1][y + 1] = 1;
				maze[x + 1][y + 2] = 1;
				maze[x + 1][y + 3] = 1;
				maze[x + 1][y + 4] = 1;
				maze[x + 1][y + 5] = 1;
				
				maze[x + 2][y] = 1;
				maze[x + 2][y + 1] = 1;
				maze[x + 2][y + 2] = 1;
				maze[x + 2][y + 3] = 1;
				maze[x + 2][y + 4] = 1;
				maze[x + 2][y + 5] = 1;
			}
		}

		return maze;
	}

	public static void main(String[] args) {
		TerrainGenerator tg = new TerrainGenerator(60, 40);

		int[][] m = tg.getMaze();
		for (int i = 0; i < 40; i++) {
			for (int j = 0; j < 60; j++) {
				System.out.print(m[j][i]);
			}
			System.out.println();
		}
	}
}
