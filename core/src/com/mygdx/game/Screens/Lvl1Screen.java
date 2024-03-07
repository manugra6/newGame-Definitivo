package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Enemigos.Enemigo;
import com.mygdx.game.Enemigos.Gusano;
import com.mygdx.game.Enemigos.Mago;
import com.mygdx.game.Enemigos.Rata;
import com.mygdx.game.Herramientas.CreaBotones;
import com.mygdx.game.Herramientas.CrearMundo;
import com.mygdx.game.Herramientas.ManagerAudio;
import com.mygdx.game.Herramientas.WorldContactListener;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Personajes.Princ1;
import com.mygdx.game.Preferences;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Clase de la pantalla del Gameplay.
 */
public class Lvl1Screen implements Screen {

    /**
     * Juego principal.
     */
    private MyGdxGame game;

    /**
     * Escenario de la pantalla.
     */
    private Stage stage;

    /**
     * Atlas de texturas para el personaje principal.
     */
    private TextureAtlas atlas;

    /**
     * Indica si la tecla de movimiento hacia arriba está presionada.
     */
    boolean ArribaPressed;

    /**
     * Indica si la tecla de movimiento hacia la derecha está presionada.
     */
    boolean DerPressed;

    /**
     * Indica si la tecla de movimiento hacia la izquierda está presionada.
     */
    boolean IzqPressed;

    /**
     * Indica si la tecla de ataque está presionada.
     */
    boolean AtaquePressed;

    /**
     * Indica si el juego está pausado.
     */
    boolean juegoPausado;

    /**
     * Cámara ortográfica utilizada en el juego.
     */
    private OrthographicCamera camera;

    /**
     * Vista del mundo del juego.
     */
    private Viewport viewport;

    /**
     * Renderizador de mapas ortogonales de Box2D.
     */
    private OrthogonalTiledMapRenderer renderer;

    /**
     * Mapa del nivel.
     */
    private TiledMap map;

    /**
     * Mundo de Box2D utilizado en el juego.
     */
    private World mundo;

    /**
     * Depurador de Box2D para visualizar elementos en el mundo.
     */
    private Box2DDebugRenderer debug;

    /**
     * Personaje principal del juego.
     */
    private Princ1 pers;

    /**
     * Lista de enemigos de tipo "Rata" en el juego.
     */
    private Array<Rata> rataList;

    /**
     * Lista de enemigos de tipo "Gusano" en el juego.
     */
    private Array<Gusano> gusanoList;

    /**
     * Lista de enemigos de tipo "Mago" en el juego.
     */
    private Array<Mago> magoList;

    /**
     * Tiempo entre la aparición de enemigos.
     */
    private float tiempoEntreEnemigos;

    /**
     * Temporizador para controlar la aparición de enemigos.
     */
    private float timerEnemigos;

    /**
     * Posición del personaje principal.
     */
    public float posPers;

    /**
     * Gestor de botones en la pantalla.
     */
    private final CreaBotones botones;

    /**
     * Gestor de audio del juego.
     */
    private final ManagerAudio managerAudio;

    /**
     * Puntuación del jugador.
     */
    public int puntos = 0;

    /**
     * Puntuación actual en el nivel 1.
     */
    int puntosActuales1;

    /**
     * Puntuación actual en el nivel 2.
     */
    int puntosActuales2;

    /**
     * Preferencias de juego.
     */
    Preferences prefs;

    /**
     * Nombre del mapa actual.
     */
    String maps;

    /**
     * Constructor de la pantalla del nivel 1.
     *
     * @param game Juego principal.
     * @param mapa Nombre del mapa a cargar.
     */
    public Lvl1Screen(MyGdxGame game,String mapa){
        atlas=new TextureAtlas("atlas/Fantasy_warrior.atlas");

        prefs = new Preferences();
        maps=mapa;
        puntosActuales1 = Preferences.getPuntosMap1();
        puntosActuales2 = Preferences.getPuntosMap2();

        this.game=game;
        managerAudio=MainMenu.getMangerAudio();

        if (Preferences.getMusica()){
           managerAudio.startMusicLvl1();
        }

        this.map = new TmxMapLoader().load(mapa);
        float mapWidth = map.getProperties().get("width", Integer.class) * map.getProperties().get("tilewidth", Integer.class);
        float mapHeight = map.getProperties().get("height", Integer.class) * map.getProperties().get("tileheight", Integer.class);

        camera = new OrthographicCamera(mapWidth,mapHeight);
        camera.setToOrtho(false, mapWidth/MyGdxGame.PPM, mapHeight/MyGdxGame.PPM);
        viewport = new ScreenViewport();
        renderer = new OrthogonalTiledMapRenderer(map,1/MyGdxGame.PPM);

        mundo = new World(new Vector2(0,-2f),true);
        debug = new Box2DDebugRenderer();

        new CrearMundo(this);
        pers=new Princ1(mundo,this);
        rataList=new Array<Rata>();
        gusanoList=new Array<Gusano>();
        magoList=new Array<Mago>();

        botones=new CreaBotones(stage,this);
        stage=botones.creaButtons();
        handleInput();
        mundo.setContactListener(new WorldContactListener());
        botones.actualizaVidas(getVidasPers());

    }
    @Override
    public void show() {

    }

    /**
     * Actualiza el estado del juego en cada fotograma.
     *
     * @param dt Delta time, tiempo transcurrido desde el último fotograma.
     */
    public void handleUpdate(float dt){
        float salto=10f;
        Vector2 vecDer =new Vector2(2.5f/MyGdxGame.PPM,0);
        Vector2 vecIzq =new Vector2(-2.5f/MyGdxGame.PPM,0);
        Vector2 vecSalto = new Vector2(0,salto/MyGdxGame.PPM);

        if (DerPressed){
            pers.body.applyLinearImpulse((vecDer),pers.body.getWorldCenter(),true);
        }
        if (IzqPressed){
            pers.body.applyLinearImpulse((vecIzq),pers.body.getWorldCenter(),true);
        }
        if (ArribaPressed){
            pers.body.applyLinearImpulse((vecSalto),pers.body.getWorldCenter(),true);
        }
        if (AtaquePressed){
            pers.body.setLinearVelocity(0,-0.3f);
        }
    }

    /**
     * Actualiza el estado general del juego.
     *
     * @param dt Delta time, tiempo transcurrido desde el último fotograma.
     */
    public void update(float dt){
        if (!juegoPausado){
            handleUpdate(dt);
            spawnEnemigos(dt);
            mundo.step(1/60f,5,3);
            pers.update(dt);
            for (Rata EnemigoRata:rataList){
                if (!EnemigoRata.isDestroyed()) {
                    EnemigoRata.update(dt);
                }
                else {
                    eliminarEnemigoRata();
                }
            }

            for (Gusano EnemigoGusano:gusanoList){
                if (!EnemigoGusano.isDestroyed()) {
                    EnemigoGusano.update(dt);
                }else{
                    eliminarEnemigoGus();
                }
            }
            for (Mago EnemigoMago:magoList){
                if (!EnemigoMago.isDestroyed()) {
                    EnemigoMago.update(dt);
                }else {
                    eliminarEnemigoMago();
                }
            }
            posPers=pers.getX()+(pers.getWidth()/2);
            botones.actualizaVidas(getVidasPers());
            botones.actualizaTiempo(dt);
            botones.actualizaPuntos(puntos);
            if (pers.destroyed){
                GameOver();
            }
            if (botones.getTiempoCont()<0){
                Winner();
            }
        }

    }

    /**
     * Renderiza el contenido del juego en pantalla.
     *
     * @param delta Delta time, tiempo transcurrido desde el último fotograma.
     */
    @Override
    public void render(float delta) {

        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        renderer.setView(camera);
        renderer.render();

        //debug.render(mundo,camera.combined);

        stage.act(delta);
        stage.draw();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        pers.draw(game.batch);

        for (Rata enemigoRata : rataList) {
            if (!enemigoRata.isDestroyed()) {
                enemigoRata.draw(game.batch);
            }
        }

        for (Gusano enemigoGus : gusanoList) {
            if (!enemigoGus.isDestroyed()) {
                enemigoGus.draw(game.batch);
            }
        }

        for (Enemigo enemigoMago : magoList) {
            if (!enemigoMago.isDestroyed()) {
                enemigoMago.draw(game.batch);
            }
        }
        game.batch.end();

    }

    /**
     * Obtiene el estado de la tecla de movimiento hacia arriba.
     *
     * @return true si la tecla está presionada, false si no.
     */
    public boolean isArribaPressed() {
        return ArribaPressed;
    }

    /**
     * Establece el estado de la tecla de ataque.
     *
     * @param ataquePressed true si la tecla está presionada, false si no.
     */
    public void setAtaquePressed(boolean ataquePressed) {
        AtaquePressed = ataquePressed;
    }

    /**
     * Obtiene el estado de la tecla de ataque.
     *
     * @return true si la tecla está presionada, false si no.
     */
    public boolean isAtaquePressed() {
        return AtaquePressed;
    }

    /**
     * Obtiene el estado de la tecla de movimiento hacia la derecha.
     *
     * @return true si la tecla está presionada, false si no.
     */
    public boolean isDerPressed() {
        return DerPressed;
    }

    /**
     * Obtiene el estado de la tecla de movimiento hacia la izquierda.
     *
     * @return true si la tecla está presionada, false si no.
     */
    public boolean isIzqPressed() {
        return IzqPressed;
    }

    /**
     * Maneja las pulsaciones de los botones de la pantalla.
     */
    private void handleInput(){
        botones.getBtnArr().addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ArribaPressed = true;
                return true;

            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ArribaPressed=false;
            }
        });
        botones.getBtnDer().addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                DerPressed = true;
                AtaquePressed=false;
                return true;

            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                DerPressed=false;
            }
        });
        botones.getBtnIzq().addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                IzqPressed = true;
                AtaquePressed=false;
                return true;

            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                IzqPressed=false;
                AtaquePressed=false;
            }
        });
        botones.getBtnAtaque().addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                AtaquePressed = true;

                return true;

            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //AtaquePressed=false;
            }
        });

    }

    /**
     * Redimensiona la vista de la pantalla.
     *
     * @param width  Nuevo ancho de la pantalla.
     * @param height Nuevo alto de la pantalla.
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    /**
     * Obtiene el mundo utilizado en el juego.
     *
     * @return Mundo.
     */
    public World getMundo(){
        return mundo;
    }

    /**
     * Obtiene la posición del personaje principal.
     *
     * @return Posición del personaje principal.
     */
    public float getPosPers() {
        return posPers;
    }

    /**
     * Genera la aparición de enemigos en el juego.
     *
     * @param dt Delta time, tiempo transcurrido desde el último fotograma.
     */
    private void spawnEnemigos(float dt){
        float mapWidth = map.getProperties().get("width", Integer.class) * map.getProperties().get("tilewidth", Integer.class);
        timerEnemigos+=dt;
        tiempoEntreEnemigos= MathUtils.random(7,9);
        if (timerEnemigos>tiempoEntreEnemigos){
            timerEnemigos=0;
            int tipoEnemigo = MathUtils.random(0, 2);
            float xEnemy=MathUtils.random(1f,mapWidth);
            Rata nuevoEnemigoRata;
            Gusano nuevoEnemigoGusano;
            Mago nuevoEnemigoMago;
            switch (tipoEnemigo) {
                case 0:
                    nuevoEnemigoRata = new Rata(this, xEnemy, 100/MyGdxGame.PPM);
                    rataList.add(nuevoEnemigoRata);
                    break;
                case 1:
                    nuevoEnemigoGusano = new Gusano(this, xEnemy, 100/MyGdxGame.PPM);
                    gusanoList.add(nuevoEnemigoGusano);
                    break;
                case 2:
                    nuevoEnemigoMago = new Mago(this, xEnemy, 100/MyGdxGame.PPM);
                    magoList.add(nuevoEnemigoMago);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    /**
     * Libera los recursos utilizados por la pantalla.
     */
    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        mundo.dispose();
        debug.dispose();
    }

    /**
     * Pausa el juego y muestra la pantalla de pausa.
     */
    public void pausarJuego() {
        juegoPausado = true;
        game.setScreen(new PausaScreen(game, this));
    }

    /**
     * Reanuda el juego después de haber sido pausado.
     */
    public void reanudarJuego() {
        juegoPausado = false;
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Obtiene el mapa del nivel.
     *
     * @return Mapa del nivel.
     */
    public TiledMap getMap() {
        return map;
    }


    /**
     * Obtiene la cantidad de vidas restantes del personaje principal.
     *
     * @return Cantidad de vidas del personaje principal.
     */
    public int getVidasPers(){
        return pers.getVida();
    }

    /**
     * Muestra la pantalla de Game Over cuando el personaje principal es destruido.
     */
    public void GameOver(){
        if (Preferences.getMusica()){
            managerAudio.stopMusicLvl1();
        }
        if (game instanceof MyGdxGame) {
            MyGdxGame myGame = (MyGdxGame) game;
            myGame.setScreen(new ScreenMuerte(myGame));
        }
    }

    /**
     * Muestra la pantalla de victoria cuando se completan los objetivos del nivel.
     */
    public void Winner(){
        if (Preferences.getMusica()){
            managerAudio.stopMusicLvl1();
        }
        if (maps.contains("cementerio2.tmx")){
            if (puntos > puntosActuales1) {
                prefs.guardarPuntosMap1(puntos);
            }
        } else if (maps.contains("castillo.tmx")) {
            if (puntos > puntosActuales2) {
                prefs.guardarPuntosMap2(puntos);
            }
        }

        if (game instanceof MyGdxGame) {
            MyGdxGame myGame = (MyGdxGame) game;
            myGame.setScreen(new WinnerScreen(myGame));
        }
    }

    /**
     * Elimina los enemigos de tipo "Rata" que han sido destruidos.
     */
    public void eliminarEnemigoRata() {
        for (Rata e:rataList) {
            if (e.isDestroyed()){
                rataList.removeValue(e,true);
            }
        }
    }

    /**
     * Elimina los enemigos de tipo "Gusano" que han sido destruidos.
     */
    public void eliminarEnemigoGus() {
        for (Gusano e:gusanoList) {
            if (e.isDestroyed()){
                gusanoList.removeValue(e,true);
            }
        }
    }

    /**
     * Elimina los enemigos de tipo "Mago" que han sido destruidos.
     */
    public void eliminarEnemigoMago() {
        for (Mago e:magoList) {
            if (e.isDestroyed()){
                magoList.removeValue(e,true);
            }
        }
    }
}
