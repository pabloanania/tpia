package ia.battle.gui.sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class DefaultSoundPlayer implements SoundPlayer {

	@Override
	public void playAttack() {
		try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(SoundPlayer.class.getResourceAsStream("hit.wav"));
			clip.open(inputStream);
			clip.start();
		} catch (Exception ex) {
			System.err.println(ex);
		}
	}

	@Override
	public void playBotKilled() {
		try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(SoundPlayer.class.getResourceAsStream("killed.wav"));
			clip.open(inputStream);
			clip.start();
		} catch (Exception ex) {
			System.err.println(ex);
		}

	}

}
