package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Herramientas.ManagerAudio;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Preferences;

/**
 * Clase que representa la pantalla de pausa del juego.
 */
public class PausaScreen extends ScreenAdapter {

    /**
     * Ancho de la pantalla.
     */
    float pantallaWidth = Gdx.graphics.getWidth();

    /**
     * Alto de la pantalla.
     */
    float pantallaHeight = Gdx.graphics.getHeight();

    /**
     * Objeto principal del juego.
     */
    private Game game;
    private Lvl1Screen screen;

    /**
     * Escenario donde se colocarán los elementos visuales.
     */
    private Stage stage;

    /**
     * Lote de sprites para dibujar elementos en la pantalla.
     */
    private SpriteBatch batch;

    /**
     * Conjunto de estilos para la interfaz gráfica.
     */
    private Skin skin;

    /**
     * Instancia del administrador de audio para gestionar la reproducción de sonidos y música.
     */
    private final ManagerAudio managerAudio;

    /**
     * Constructor de la clase PausaScreen.
     *
     * @param game Instancia del juego principal.
     */
    public PausaScreen(Game game,Lvl1Screen screen){
        this.game=game;
        this.screen=screen;
        managerAudio=MainMenu.getMangerAudio();
    }

    /**
     * Método llamado cuando la pantalla de Pausa se muestra por primera vez.
     */
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin=new Skin();

        batch = new SpriteBatch();

        float aspectRatio = pantallaWidth / pantallaHeight;

        int aspectRatioInt1 = (int) (aspectRatio * 35);
        int aspectRatioInt2 = (int) (aspectRatio * 60);

        Texture backgroundTexture = new Texture(Gdx.files.internal("recursos/fondoPause.png"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("recursos/8bitlim.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = aspectRatioInt1;
        BitmapFont bitF = generator.generateFont(parameter);


        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = bitF;
        textButtonStyle.fontColor = Color.BLACK;

        skin.add("default", textButtonStyle);

        Table tabla = new Table();
        tabla.setFillParent(true);

        TextButton botonReanudar=new TextButton(MyGdxGame.bundle.get("reanudar"),skin);
        botonReanudar.addListener(new ActorGestureListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                reanudarJuego();
            }
        });


        TextButton botonSeleccion=new TextButton(MyGdxGame.bundle.get("seleccion"),skin);
        botonSeleccion.addListener(new ActorGestureListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (Preferences.getMusica()){
                    managerAudio.stopMusicLvl1();
                }
                if (game instanceof MyGdxGame) {
                    MyGdxGame myGame = (MyGdxGame) game;
                    myGame.setScreen(new SeleccionMapaScreen(myGame));
                }
                dispose();
            }
        });

        TextButton botonVolver=new TextButton(MyGdxGame.bundle.get("volver"),skin);
        botonVolver.addListener(new ActorGestureListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (Preferences.getMusica()){
                    managerAudio.stopMusicLvl1();
                }
                if (game instanceof MyGdxGame) {
                    MyGdxGame myGame = (MyGdxGame) game;
                    myGame.setScreen(new MainMenu(myGame));
                }
                dispose();
            }
        });

        tabla.add(botonReanudar).padBottom(20).row();
        tabla.add(botonSeleccion).padBottom(20).row();
        tabla.add(botonVolver);

        stage.addActor(tabla);

    }

    private void reanudarJuego(){
        if (game instanceof MyGdxGame) {
            MyGdxGame myGame = (MyGdxGame) game;
            Lvl1Screen lvl1Screen = (Lvl1Screen) screen;
            if (screen!=null){
                screen.reanudarJuego();
            }
            myGame.setScreen(screen);
        }
        dispose();
    }

    /**
     * Método llamado en cada fotograma para renderizar la pantalla de pausa.
     *
     * @param delta El tiempo transcurrido desde el último fotograma en segundos.
     */
    @Override
    public void render(float delta) {
        super.render(delta);
        stage.draw();
    }

    /**
     * Libera los recursos utilizados por la pantalla de pausa cuando ya no es necesaria.
     */
    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
