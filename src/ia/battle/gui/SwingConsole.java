package ia.battle.gui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class SwingConsole {

	public static void run(final JFrame frame, final int width, final int height, final boolean exitOnClose) {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				if (exitOnClose)
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				frame.setSize(width, height);
				frame.setVisible(true);

			}
		});
	}

	public static void run(final JFrame frame, final int width, final int height, final boolean exitOnClose,
			final String title) {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				if (exitOnClose)
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				frame.setTitle(title);
				frame.setSize(width, height);
				frame.setVisible(true);

			}
		});
	}

}
