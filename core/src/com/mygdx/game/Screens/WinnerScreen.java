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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Herramientas.ManagerAudio;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Preferences;


/**
 * Clase que representa la pantalla de ganador del juego.
 */
public class WinnerScreen extends ScreenAdapter {

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
    private static ManagerAudio managerAudio;

    /**
     * Constructor de la clase WinnerScreen.
     *
     * @param game Instancia del juego principal.
     */
    public WinnerScreen(Game game){
        this.game=game;
        managerAudio=new ManagerAudio();
    }

    public static ManagerAudio getMangerAudio(){return managerAudio;}

    /**
     * Método llamado cuando la pantalla de ganador se muestra por primera vez.
     */
    @Override
    public void show() {
        if (Preferences.getMusica()){
            managerAudio.startMusicMenuGanador();
        }

        float aspectRatio = pantallaWidth / pantallaHeight;

        int aspectRatioInt1 = (int) (aspectRatio * 35);
        int aspectRatioInt2 = (int) (aspectRatio * 60);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin=new Skin();

        batch = new SpriteBatch();

        Texture backgroundTexture = new Texture(Gdx.files.internal("recursos/fondoGanador.png"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("recursos/8bitlim.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = aspectRatioInt1;
        BitmapFont bitF = generator.generateFont(parameter);

        FreeTypeFontGenerator.FreeTypeFontParameter parameterTitulo = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameterTitulo.size = aspectRatioInt2;
        BitmapFont bitFTitulo=generator.generateFont(parameterTitulo);
        generator.dispose();

        Label.LabelStyle estiloLabel= new Label.LabelStyle(bitFTitulo, Color.GREEN);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = bitF;
        textButtonStyle.fontColor = Color.WHITE;

        skin.add("default", textButtonStyle);

        Table tableTitulo=new Table();
        tableTitulo.top();
        tableTitulo.setFillParent(true);


        Table tabla = new Table();
        tabla.setFillParent(true);

        Label titulo=new Label(MyGdxGame.bundle.get("ganador"),estiloLabel);
        tableTitulo.add(titulo).expandX();

        TextButton botonJugar=new TextButton(MyGdxGame.bundle.get("seleccion"),skin);
        botonJugar.addListener(new ActorGestureListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
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
                if (game instanceof MyGdxGame) {
                    MyGdxGame myGame = (MyGdxGame) game;
                    myGame.setScreen(new MainMenu(myGame));
                }
                dispose();
            }
        });

        TextButton botonRecords=new TextButton(MyGdxGame.bundle.get("records"),skin);
        botonRecords.addListener(new ActorGestureListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (game instanceof MyGdxGame) {
                    MyGdxGame myGame = (MyGdxGame) game;
                    myGame.setScreen(new RecordsScreen(myGame));
                }
                dispose();
            }
        });

        tabla.add(botonJugar).padBottom(20).row();
        tabla.add(botonRecords).padBottom(20).row();
        tabla.add(botonVolver);

        stage.addActor(tableTitulo);
        stage.addActor(tabla);

    }

    /**
     * Método llamado en cada fotograma para renderizar la pantalla del ganador.
     *
     * @param delta El tiempo transcurrido desde el último fotograma en segundos.
     */
    @Override
    public void render(float delta) {
        super.render(delta);
        stage.draw();
    }

    /**
     * Libera los recursos utilizados por la pantalla del ganador cuando ya no es necesaria.
     */
    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        skin.dispose();
    }
}
