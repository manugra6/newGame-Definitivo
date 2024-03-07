package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.I18NBundle;
import com.mygdx.game.Screens.Lvl1Screen;
import com.mygdx.game.Screens.MainMenu;
import com.mygdx.game.Screens.ScreenMuerte;

/**
 * Clase principal del juego que extiende la clase Game de libGDX.
 */
public class MyGdxGame extends Game {

	/**
	 * Factor de conversión de unidades de mundo a unidades de pantalla.
	 */
	public static final float PPM=100;

	/**
	 * Códigos de bits para las categorías de colisiones.
	 */
	public static final short suelo_BIT=1;
	public static final short personaje_BIT=2;
	public static final short enemigo_BIT=4;
	public static final short objetos_BIT=8;
	public static final short limites_BIT=16;
	public static final short ataque_BIT=32;
	public static final short cabezaEne_BIT=64;

	/**
	 * Ancho de la pantalla.
	 */
	public static float pantallaWidth;

	/**
	 * Altura de la pantalla.
	 */
	public static float pantallaHeight;

	/**
	 * Lote de sprites para dibujar elementos en la pantalla.
	 */
	public SpriteBatch batch;

	/**
	 * Conjunto de traducciones para el juego.
	 */
	public static I18NBundle bundle;

	/**
	 * Lote de sprites para dibujar elementos en la pantalla.
	 */
	public static AssetManager assetManager;

	/**
	 * Método llamado al iniciar la aplicación para realizar la configuración inicial.
	 */
	@Override
	public void create() {
		pantallaWidth = Gdx.graphics.getWidth();
		pantallaHeight = Gdx.graphics.getHeight();

		batch=new SpriteBatch();

		assetManager=new AssetManager();
		assetManager.load("musica/MusicaMenus.ogg", Music.class);
		assetManager.load("musica/MusicaLvl1.mp3", Music.class);
		assetManager.load("musica/MusicaMenuGanador.mp3", Music.class);
		assetManager.load("sonidos/ataque_enemigo.wav", Sound.class);
		assetManager.load("sonidos/ataque_enemigo2.wav", Sound.class);
		assetManager.load("sonidos/corte.wav", Sound.class);
		assetManager.load("sonidos/hit.wav", Sound.class);
		assetManager.load("sonidos/muerte_enemigo.wav", Sound.class);
		assetManager.load("sonidos/rayo.wav", Sound.class);
		assetManager.load("sonidos/salto.wav", Sound.class);
		assetManager.finishLoading();

		FileHandle fileHandle= Gdx.files.internal("traducciones/"+Preferences.getPropertyFile());
		bundle=I18NBundle.createBundle(fileHandle,Preferences.getLocale());
		setScreen(new MainMenu(this));

	}

	/**
	 * Método llamado en cada fotograma para renderizar la pantalla actual.
	 */
	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {


	}

	/**
	 * Método llamado al cerrar la aplicación para liberar recursos.
	 */
	@Override
	public void dispose() {
		batch.dispose();
		assetManager.dispose();
		super.dispose();
	}
}
