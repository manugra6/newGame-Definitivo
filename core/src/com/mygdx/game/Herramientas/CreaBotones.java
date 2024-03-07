package com.mygdx.game.Herramientas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.Lvl1Screen;
import com.mygdx.game.Screens.MainMenu;

/**
 * Clase que crea y gestiona los controles y la interfaz de usuario en el juego.
 * Incluye botones para moverse, atacar, y muestra información como vidas, tiempo y puntuación.
 */
public class CreaBotones {

    /**
     * Escenario donde se colocarán los botones.
     */
    private Stage stage;

    /**
     * Botón de dirección derecha.
     */
    ImageButton btnDer;

    /**
     * Botón de direccion izquierda.
     */
    ImageButton btnIzq;

    /**
     * Botón de dirección arriba.
     */
    ImageButton btnArr;

    /**
     * Imagen del corazón.
     */
    ImageButton corazon;

    /**
     * Botón de ataque.
     */
    ImageButton btnAtaque;

    /**
     * Etiqueta para mostrar el número de vidas.
     */
    Label vids;

    /**
     * Etiqueta para mostrar el tiempo restante.
     */
    Label tiempo;

    /**
     * Etiqueta para mostrar la puntuación.
     */
    Label puntuacion;

    /**
     * Etiqueta para el texto "Tiempo".
     */
    Label tiempoLabel;

    /**
     * Etiqueta para el texto "Tiempo".
     */
    Label puntuacionLabel;

    /**
     * Ancho de la pantalla del juego.
     */
    float pantallaWidth = Gdx.graphics.getWidth();

    /**
     * Alto de la pantalla del juego.
     */
    float pantallaHeight = Gdx.graphics.getHeight();

    /**
     * Skin para los estilos de los botones y etiquetas.
     */
    private Skin skin;

    /**
     * Contador de tiempo.
     */
    int tiempoCont=100;

    /**
     * Tiempo acumulado del nivel.
     */
    float tiempoNivel;

    /**
     * Botón para pausar el juego.
     */

    TextButton botonPausa;

    /**
     * Referencia a la pantalla del nivel.
     */
    Lvl1Screen screen;

    /**
     * Constructor de la clase CreaBotones.
     *
     * @param stage   Escenario en el que se crean y muestran los elementos.
     * @param screen  Pantalla del juego a la que están asociados los controles.
     */
    public CreaBotones(Stage stage, Lvl1Screen screen) {
        this.stage = stage;
        this.screen=screen;
        creaStage();
        creaButtons();

    }

    /**
     * Crea un nuevo escenario para los controles.
     */
    public void creaStage(){
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Crea y configura los botones e interfaz de usuario.
     *
     * @return El escenario con los elementos creados.
     */
    public Stage creaButtons(){
        skin=new Skin();

        float aspectRatio = pantallaWidth / pantallaHeight;

        int aspectRatioInt1 = (int) (aspectRatio * 25);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("recursos/8bitlim.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = aspectRatioInt1;
        BitmapFont bitF = generator.generateFont(parameter);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = bitF;
        textButtonStyle.fontColor = Color.WHITE;

        skin.add("default", textButtonStyle);

        //Boton Arriba
        ImageButton.ImageButtonStyle estiloArriba= new ImageButton.ImageButtonStyle();
        estiloArriba.imageUp=new TextureRegionDrawable(new TextureRegion(new Texture("controles/botonArribaNoPulsado.png")));
        estiloArriba.imageDown=new TextureRegionDrawable(new TextureRegion(new Texture("controles/botonArribaPulsado.png")));
        btnArr=new ImageButton(estiloArriba);
        btnArr.setHeight(MyGdxGame.pantallaHeight*0.1f);
        btnArr.setWidth(MyGdxGame.pantallaWidth*0.1f);
        estiloArriba.imageDown.setMinHeight(MyGdxGame.pantallaWidth*0.06f);
        estiloArriba.imageUp.setMinHeight(MyGdxGame.pantallaWidth*0.06f);
        estiloArriba.imageDown.setMinWidth(MyGdxGame.pantallaWidth*0.06f);
        estiloArriba.imageUp.setMinWidth(MyGdxGame.pantallaWidth*0.06f);
        //Boton Derecha
        ImageButton.ImageButtonStyle estiloDerecha= new ImageButton.ImageButtonStyle();
        estiloDerecha.imageUp=new TextureRegionDrawable(new TextureRegion(new Texture("controles/botonDerechaNoPulsado.png")));
        estiloDerecha.imageDown=new TextureRegionDrawable(new TextureRegion(new Texture("controles/botonDerechaPulsado.png")));
        btnDer=new ImageButton(estiloDerecha);
        btnDer.setHeight(MyGdxGame.pantallaHeight*0.1f);
        btnDer.setWidth(MyGdxGame.pantallaWidth*0.1f);
        estiloDerecha.imageDown.setMinHeight(MyGdxGame.pantallaWidth*0.06f);
        estiloDerecha.imageUp.setMinHeight(MyGdxGame.pantallaWidth*0.06f);
        estiloDerecha.imageDown.setMinWidth(MyGdxGame.pantallaWidth*0.06f);
        estiloDerecha.imageUp.setMinWidth(MyGdxGame.pantallaWidth*0.06f);

        //Boton Izquierda
        ImageButton.ImageButtonStyle estiloIzquierda= new ImageButton.ImageButtonStyle();
        estiloIzquierda.imageUp=new TextureRegionDrawable(new TextureRegion(new Texture("controles/botonIzquierdaNoPulsado.png")));
        estiloIzquierda.imageDown=new TextureRegionDrawable(new TextureRegion(new Texture("controles/botonIzquierdaPulsado.png")));
        btnIzq=new ImageButton(estiloIzquierda);
        btnIzq.setHeight(MyGdxGame.pantallaHeight*0.1f);
        btnIzq.setWidth(MyGdxGame.pantallaWidth*0.1f);
        estiloIzquierda.imageDown.setMinHeight(MyGdxGame.pantallaWidth*0.06f);
        estiloIzquierda.imageUp.setMinHeight(MyGdxGame.pantallaWidth*0.06f);
        estiloIzquierda.imageDown.setMinWidth(MyGdxGame.pantallaWidth*0.06f);
        estiloIzquierda.imageUp.setMinWidth(MyGdxGame.pantallaWidth*0.06f);

        //BOTON ATAQUE
        ImageButton.ImageButtonStyle estiloAtaque= new ImageButton.ImageButtonStyle();
        estiloAtaque.imageUp=new TextureRegionDrawable(new TextureRegion(new Texture("controles/botonAtaqueNoPulsado.png")));
        estiloAtaque.imageDown=new TextureRegionDrawable(new TextureRegion(new Texture("controles/botonAtaquePulsado.png")));
        btnAtaque=new ImageButton(estiloAtaque);
        btnAtaque.setHeight(MyGdxGame.pantallaHeight*0.1f);
        btnAtaque.setWidth(MyGdxGame.pantallaWidth*0.1f);
        estiloAtaque.imageDown.setMinHeight(MyGdxGame.pantallaWidth*0.06f);
        estiloAtaque.imageUp.setMinHeight(MyGdxGame.pantallaWidth*0.06f);
        estiloAtaque.imageDown.setMinWidth(MyGdxGame.pantallaWidth*0.06f);
        estiloAtaque.imageUp.setMinWidth(MyGdxGame.pantallaWidth*0.06f);

        //Imagen Corazon
        ImageButton.ImageButtonStyle estiloCorazon= new ImageButton.ImageButtonStyle();
        estiloCorazon.imageUp=new TextureRegionDrawable(new TextureRegion(new Texture("controles/corazones.png")));
        estiloCorazon.imageDown=new TextureRegionDrawable(new TextureRegion(new Texture("controles/corazones.png")));
        corazon=new ImageButton(estiloCorazon);
        corazon.setHeight(MyGdxGame.pantallaHeight*0.1f);
        corazon.setWidth(MyGdxGame.pantallaWidth*0.1f);
        estiloCorazon.imageDown.setMinHeight(MyGdxGame.pantallaWidth*0.05f);
        estiloCorazon.imageUp.setMinHeight(MyGdxGame.pantallaWidth*0.05f);
        estiloCorazon.imageDown.setMinWidth(MyGdxGame.pantallaWidth*0.05f);
        estiloCorazon.imageUp.setMinWidth(MyGdxGame.pantallaWidth*0.05f);

        botonPausa=new TextButton(MyGdxGame.bundle.get("pausa"),skin);
        botonPausa.addListener(new ActorGestureListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                screen.pausarJuego();
            }
        });

        //Label para el numero de vidas tiempo y puntos

        Label.LabelStyle estiloLabel= new Label.LabelStyle(bitF,Color.WHITE);
        vids = new Label("",estiloLabel);
        tiempo= new Label("",estiloLabel);
        puntuacion=new Label("",estiloLabel);
        tiempoLabel= new Label(MyGdxGame.bundle.get("tiempo"),estiloLabel);
        puntuacionLabel=new Label(MyGdxGame.bundle.get("puntos"),estiloLabel);


        Table tablaBotones = new Table();
        tablaBotones.bottom().left();
        tablaBotones.setWidth(800);

        tablaBotones.add(btnIzq).padBottom(10).expandX();
        tablaBotones.add(btnArr).padBottom(10).expandX();
        tablaBotones.add(btnDer).padBottom(10).expandX();
        tablaBotones.setPosition(0,0);

        Table tabAtaque = new Table();
        tabAtaque.bottom();
        tabAtaque.setWidth(300);
        tabAtaque.add(btnAtaque).padBottom(10).expandX().padRight(150);
        tabAtaque.setPosition(MyGdxGame.pantallaWidth-btnAtaque.getWidth(),0);

        Table tabCorazon = new Table();
        tabCorazon.top().left();
        tabCorazon.setFillParent(true);
        tabCorazon.add(corazon).expandX().padTop(10).padLeft(40);
        tabCorazon.add(tiempoLabel).expandX().padTop(10);
        tabCorazon.add(puntuacionLabel).expandX().padTop(10);
        tabCorazon.add(botonPausa).expandX().padTop(10);
        tabCorazon.row();
        tabCorazon.add(vids).expandX().padTop(10).padLeft(10);
        tabCorazon.add(tiempo).expandX().padTop(10).padLeft(10);
        tabCorazon.add(puntuacion).expandX().padTop(10).padLeft(10);
        tabCorazon.add(new Label("",estiloLabel));

        stage.addActor(tablaBotones);
        stage.addActor(tabAtaque);
        stage.addActor(tabCorazon);
        return stage;
    }

    /**
     * Obtiene la referencia al botón de movimiento hacia arriba.
     *
     * @return El botón de movimiento hacia arriba.
     */
    public ImageButton getBtnArr() {
        return btnArr;
    }


    /**
     * Obtiene la referencia al botón de movimiento hacia la derecha.
     *
     * @return El botón de movimiento hacia la derecha.
     */
    public ImageButton getBtnDer() {
        return btnDer;
    }

    /**
     * Obtiene la referencia al botón de movimiento hacia la izquierda.
     *
     * @return El botón de movimiento hacia la izquierda.
     */
    public ImageButton getBtnIzq() {
        return btnIzq;
    }

    /**
     * Obtiene la referencia al botón de ataque.
     *
     * @return El botón de ataque.
     */
    public ImageButton getBtnAtaque() {
        return btnAtaque;
    }

    /**
     * Obtiene el contador de tiempo restante.
     *
     * @return El contador de tiempo restante.
     */
    public int getTiempoCont() {
        return tiempoCont;
    }

    /**
     * Actualiza la etiqueta que muestra el número de vidas.
     *
     * @param vidas Cantidad actual de vidas.
     */
    public void actualizaVidas(int vidas){
        vids.setText(String.valueOf(vidas));
    }

    /**
     * Actualiza el tiempo restante y la etiqueta correspondiente.
     *
     * @param dt El tiempo transcurrido desde la última actualización.
     */
    public void actualizaTiempo(float dt){
        tiempoNivel+=dt;

        if (tiempoNivel>=1){
            tiempoCont--;
            tiempo.setText(tiempoCont+"");
            tiempoNivel=0;
        }
    }

    /**
     * Actualiza la etiqueta que muestra la puntuación.
     *
     * @param puntos La puntuación actual.
     */
    public void actualizaPuntos(int puntos){
        puntuacion.setText(puntos+"");
    }
}


