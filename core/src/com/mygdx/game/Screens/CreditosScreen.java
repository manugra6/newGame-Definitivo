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
 * Pantalla que muestra los créditos del juego, incluyendo información sobre los creadores y atribuciones.
 */
public class CreditosScreen extends ScreenAdapter {

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
     * Constructor de la clase CreditosScreen.
     *
     * @param game Instancia del juego principal.
     */
    public CreditosScreen(Game game){
        this.game=game;
        managerAudio=new ManagerAudio();

    }

    /**
     * Método para obtener la instancia del administrador de audio.
     *
     * @return Instancia del administrador de audio.
     */
    public static ManagerAudio getMangerAudio(){return managerAudio;}

    /**
     * Método llamado cuando la pantalla de creditos se muestra por primera vez.
     */
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin=new Skin();

        batch = new SpriteBatch();

        float aspectRatio = pantallaWidth / pantallaHeight;

        int aspectRatioInt1 = (int) (aspectRatio * 35);
        int aspectRatioInt2 = (int) (aspectRatio * 65);
        int aspectRatioInt3 = (int) (aspectRatio * 25);

        Texture backgroundTexture = new Texture(Gdx.files.internal("recursos/fondoMenu.PNG"));
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

        FreeTypeFontGenerator.FreeTypeFontParameter parameterLbl = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameterLbl.size = aspectRatioInt3;
        BitmapFont bitFLbl=generator.generateFont(parameterLbl);
        generator.dispose();

        Label.LabelStyle estiloLabel= new Label.LabelStyle(bitFTitulo, Color.WHITE);
        Label.LabelStyle estiloLabel2= new Label.LabelStyle(bitFLbl, Color.RED);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = bitF;
        textButtonStyle.fontColor = Color.WHITE;

        skin.add("default", textButtonStyle);

        Table tableTitulo=new Table();
        tableTitulo.top();
        tableTitulo.setFillParent(true);


        Table tabla = new Table();
        tabla.setFillParent(true);

        Label titulo=new Label(MyGdxGame.bundle.get("creditos"),estiloLabel);
        tableTitulo.add(titulo).expandX();

        Label rec1=new Label(MyGdxGame.bundle.get("creadoPor")+" Manuel Granha Malvido",estiloLabel2);
        Label rec2=new Label(MyGdxGame.bundle.get("botones")+" Gleao",estiloLabel2);
        Label rec3=new Label(MyGdxGame.bundle.get("pers")+" Luzmelo",estiloLabel2);
        Label rec4=new Label(MyGdxGame.bundle.get("enem")+" Luzmelo, Lilwillydesigns ",estiloLabel2);
        Label rec5=new Label(MyGdxGame.bundle.get("map")+" GothicVania",estiloLabel2);
        Label rec6=new Label(MyGdxGame.bundle.get("mencion")+" Brent Aureli Codes",estiloLabel2);
        Label rec7=new Label(MyGdxGame.bundle.get("music")+" ZapsPlat y Patrick de Artaega",estiloLabel2);


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

        tabla.add(rec1).padBottom(10).row();
        tabla.add(rec2).padBottom(10).row();
        tabla.add(rec3).padBottom(10).row();
        tabla.add(rec4).padBottom(10).row();
        tabla.add(rec5).padBottom(10).row();
        tabla.add(rec7).padBottom(10).row();
        tabla.add(rec6).padBottom(10).row();
        tabla.add(new Label("",estiloLabel)).padBottom(20).row();
        tabla.add(botonVolver);

        stage.addActor(tableTitulo);
        stage.addActor(tabla);

    }

    /**
     * Método llamado en cada fotograma para renderizar la pantalla de creditos.
     *
     * @param delta El tiempo transcurrido desde el último fotograma en segundos.
     */
    @Override
    public void render(float delta) {
        super.render(delta);
        stage.draw();
    }

    /**
     * Método llamado al liberar los recursos asociados a la pantalla de creditos.
     */
    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        skin.dispose();
    }
}
