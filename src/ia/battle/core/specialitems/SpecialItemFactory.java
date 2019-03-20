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

package ia.battle.core.specialitems;

import java.util.Random;

/*
 			  +       -
 			 90      10

 vida        20      10
 defensa     20      20
 velocidad   20      25
 rango        5       5
 ataque      20      20
 */

public class SpecialItemFactory {
    private Random rnd = new Random();

    public SpecialItem getSpecialItem() {  
        SpecialItem si;

        int i = rnd.nextInt(5);
        int value;

        switch (i) {
        case 0:
            if (rnd.nextFloat() > 0.9f)
                value = -1 * rnd.nextInt(10);
            else
                value = rnd.nextInt(20);
            
            si = new SpecialItemHealth(value);
            break;

        case 1:
            if (rnd.nextFloat() > 0.9f)
                value = -1 * rnd.nextInt(20);
            else
                value = rnd.nextInt(20);

            si = new SpecialItemDefense(value);
            break;

        case 2:
            if (rnd.nextFloat() > 0.9f)
                value = -1 * rnd.nextInt(25);
            else
                value = rnd.nextInt(20);

            si = new SpecialItemSpeed(value);
            break;

        case 3:
            if (rnd.nextFloat() > 0.9f)
                value = -1 * rnd.nextInt(5);
            else
                value = rnd.nextInt(5);

            si = new SpecialItemRange(value);
            break;

        default:
            if (rnd.nextFloat() > 0.9f)
                value = -1 * rnd.nextInt(20);
            else
                value = rnd.nextInt(20);

            si = new SpecialItemAttack(value);
        }

        return si;
    }
}