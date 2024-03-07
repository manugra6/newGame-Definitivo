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
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Preferences;


/**
 * Pantalla que permite al jugador ajustar diferentes opciones del juego, como la configuración de idioma, vibración y música.
 */
public class OptionsMenu extends ScreenAdapter {

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
     * Indica si el idioma seleccionado es inglés o castellano.
     * Se obtiene a partir de las preferencias del juego.
     */
    public boolean ingles=Preferences.getIngles();

    /**
     * Botón para cambiar el idioma del juego.
     */
    TextButton botonIdioma;

    /**
     * Botón para activar/desactivar la vibración.
     */
    TextButton botonVibracion;

    /**
     * Botón para volver al menú principal.
     */
    TextButton botonVolver;

    /**
     * Botón para activar/desactivar la música.
     */
    TextButton botonMusica;

    /**
     * Preferencias del juego, que incluyen configuraciones como idioma, vibración y música.
     */
    Preferences pref;

    /**
     * Cadena que representa el estado actual de la vibración.
     */
    String vibra;

    /**
     * Cadena que representa el estado actual de la música.
     */
    String musi;

    /**
     * Constructor de la clase del OptionsMenu.
     *
     * @param game Objeto principal del juego.
     */
    public OptionsMenu(Game game){
        this.game=game;
        pref = new Preferences();
    }

    /**
     * Método llamado cuando la pantalla Opciones se muestra por primera vez.
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

        Texture backgroundTexture = new Texture(Gdx.files.internal("recursos/fondoOpciones.jpg"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("recursos/8bitlim.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = aspectRatioInt1;
        final BitmapFont bitF = generator.generateFont(parameter);

        FreeTypeFontGenerator.FreeTypeFontParameter parameterTitulo = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameterTitulo.size = aspectRatioInt2;
        BitmapFont bitFTitulo=generator.generateFont(parameterTitulo);
        generator.dispose();

        Label.LabelStyle estiloLabel= new Label.LabelStyle(bitFTitulo, Color.RED);

        final TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = bitF;
        textButtonStyle.fontColor = Color.WHITE;

        final TextButton.TextButtonStyle textButtonStylePulsado = new TextButton.TextButtonStyle();
        textButtonStylePulsado.font = bitF;
        textButtonStylePulsado.fontColor = Color.RED;

        skin.add("default", textButtonStyle);

        Table tableTitulo=new Table();
        tableTitulo.top();
        tableTitulo.setFillParent(true);


        Table tabla = new Table();
        tabla.setFillParent(true);

        Label titulo=new Label(MyGdxGame.bundle.get("opciones"),estiloLabel);
        tableTitulo.add(titulo).expandX();

        if(Preferences.getVibracion()){
            vibra=MyGdxGame.bundle.get("vibracionTrue");
        }else {
            vibra=MyGdxGame.bundle.get("vibracionFalse");
        }
        botonVibracion=new TextButton(vibra,skin);
        botonVibracion.addListener(new ActorGestureListener(){
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                botonVibracion.setStyle(textButtonStylePulsado);

                if (Preferences.getVibracion()){
                    pref.guardarVibracion(false);
                    botonVibracion.setText(MyGdxGame.bundle.get("vibracionFalse"));
                }else {
                    pref.guardarVibracion(true);
                    botonVibracion.setText(MyGdxGame.bundle.get("vibracionTrue"));
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                botonVibracion.setStyle(textButtonStyle);
            }
        });
        if(Preferences.getMusica()){
            musi=MyGdxGame.bundle.get("musicaTrue");
        }else {
            musi=MyGdxGame.bundle.get("musicaFalse");
        }
        botonMusica=new TextButton(musi,skin);
        botonMusica.addListener(new ActorGestureListener(){
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                botonMusica.setStyle(textButtonStylePulsado);

                if (Preferences.getMusica()){
                    pref.guardarMusica(false);
                    botonMusica.setText(MyGdxGame.bundle.get("musicaFalse"));
                }else {
                    pref.guardarMusica(true);
                    botonMusica.setText(MyGdxGame.bundle.get("musicaTrue"));
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                botonMusica.setStyle(textButtonStyle);
            }
        });

        botonIdioma=new TextButton(MyGdxGame.bundle.get("idioma")+" "+Preferences.getLocale(),skin);
        botonIdioma.addListener(new ActorGestureListener(){
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                botonIdioma.setStyle(textButtonStylePulsado);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                botonIdioma.setStyle(textButtonStyle);
                ingles=!ingles;
                Preferences.setIngles(ingles);
                actualizarIdioma();
            }
        });

        botonVolver=new TextButton(MyGdxGame.bundle.get("volver"),skin);
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


        tabla.add(botonVibracion).padBottom(20).row();
        tabla.add(botonMusica).padBottom(20).row();
        tabla.add(botonIdioma).padBottom(20).row();
        tabla.add(botonVolver);

        stage.addActor(tableTitulo);
        stage.addActor(tabla);

    }

    /**
     * Método llamado en cada fotograma para renderizar la pantalla de opciones.
     *
     * @param delta El tiempo transcurrido desde el último fotograma en segundos.
     */
    @Override
    public void render(float delta) {
        super.render(delta);
        stage.draw();
    }

    /**
     * Libera los recursos utilizados por la pantalla de opciones cuando ya no es necesaria.
     */
    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    /**
     * Actualiza el idioma de los elementos visuales en la pantalla según las preferencias del juego.
     * Actualiza los textos de los botones y etiquetas con las traducciones correspondientes.
     */
    private void actualizarIdioma(){
        if(Preferences.getMusica()){
            botonMusica.setText(MyGdxGame.bundle.get("musicaTrue"));
        }else {

            botonMusica.setText( MyGdxGame.bundle.get("musicaFalse"));
        }

        if(Preferences.getVibracion()){
            botonVibracion.setText(MyGdxGame.bundle.get("vibracionTrue"));
        }else {
            botonVibracion.setText(MyGdxGame.bundle.get("vibracionFalse"));
        }

        MyGdxGame.bundle = I18NBundle.createBundle(Gdx.files.internal("traducciones/"+ Preferences.getPropertyFile()),Preferences.getLocale());
        botonIdioma.setText(MyGdxGame.bundle.get("idioma") + " " + Preferences.getLocale());
        botonVolver.setText(MyGdxGame.bundle.get("volver"));
    }

}
