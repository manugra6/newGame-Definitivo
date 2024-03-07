package com.mygdx.game.Herramientas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.mygdx.game.Preferences;

/**
 * Clase encargada de gestionar la reproducción y detención de la música en el juego.
 */
public class ManagerAudio {

    private Music menu;
    private Music lvl1;
    private Music menuGanador;

    /**
     * Inicia la reproducción de la música del menú principal, si la configuración de música está habilitada.
     */
    public void startMusicMenu() {
        if (Preferences.getMusica()) {
            menu = Gdx.audio.newMusic(Gdx.files.internal("musica/MusicaMenus.ogg"));
            menu.setLooping(true);
            menu.play();
        }
    }

    /**
     * Detiene la reproducción de la música del menú principal.
     */
    public void stopMusicMenu() {
        menu.stop();
    }

    /**
     * Inicia la reproducción de la música del menú de ganador, si la configuración de música está habilitada.
     */
    public void startMusicMenuGanador() {
        if (Preferences.getMusica()) {
            menuGanador = Gdx.audio.newMusic(Gdx.files.internal("musica/MusicaMenuGanador.mp3"));
            menuGanador.play();
        }
    }

    /**
     * Inicia la reproducción de la música del nivel, si la configuración de música está habilitada.
     */
    public void startMusicLvl1() {
        if (Preferences.getMusica()) {
            lvl1 = Gdx.audio.newMusic(Gdx.files.internal("musica/MusicaLvl1.mp3"));
            lvl1.setLooping(true);
            lvl1.play();
        }
    }
    /**
     * Detiene la reproducción de la música del nivel.
     */
    public void stopMusicLvl1() {
        lvl1.stop();
    }

}
