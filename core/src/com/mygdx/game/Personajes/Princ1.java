package com.mygdx.game.Personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Enemigos.Gusano;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Preferences;
import com.mygdx.game.Screens.Lvl1Screen;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Princ1 extends Sprite {
    /**
     * Enumeración que representa los estados posibles del personaje.
     */
    public enum State {ATTACK1,ATTACK2,ATTACK3,SALTANDO,CAIDA,CORRIENDO,DAMAGE,MUERTE,QUIETO};

    /**
     * Estado actual del personaje.
     */
    public State currentState;

    /**
     * Estado anterior del personaje.
     */
    public State previousState;

    /**
     * Animaciones para diferentes acciones del personaje.
     */
    private Animation<TextureRegion> ataque1,ataque2,ataque3,salto,caer,correr,danio,muerte,quieto;

    /**
     * Temporizador para controlar el tiempo en un estado específico de la animación.
     */
    private float estadoTimer;

    /**
     * Booleano que indica si el personaje está corriendo hacia que direccion corre.
     */
    boolean correDerecha;

    /**
     * Instancia del mundo.
     */
    public World mundo;

    /**
     * Cuerpo físico del personaje.
     */
    public Body body;
    /**
     * Textura que representa al personaje quieto.
     */
    public TextureRegion stand;

    /**
     * Valor utilizado en la para dividir los sprites del atlas.
     */
    int imagen=162;

    /**
     * Definición de la forma del cuerpo físico.
     */
    FixtureDef def;

    /**
     * Definición del cuerpo del personaje.
     */
    BodyDef bodyDef;

    /**
     *  Forma del cuerpo del personaje.
     */
    PolygonShape shape;

    /** Referencia a la pantalla del nivel en la que se encuentra el personaje. */
    Lvl1Screen screen;

    /** Definición de la forma de ataque del personaje. */
    FixtureDef ataqueDef;

    /** Forma de ataque del personaje. */
    PolygonShape ataqueShape;

    /** Fixture que representa el ataque del personaje. */
    Fixture ataque;

    /** Contador utilizado en la creación de algunas animaciones. */
    int cuenta=0;

    /** Lista de texturas utilizadas en las animaciones. */
    Array<TextureRegion> frames;

    /** Booleano que indica si el personaje ha sido golpeado. */
    boolean hit=false;

    /** Booleano que indica si el personaje está siendo destruido. */
    boolean destroy=false;

    /** Booleano que indica si el personaje ha sido destruido. */
    public boolean destroyed=false;

    /** Booleano que indica si el personaje puede ser golpeado. */
    private boolean canBeHit = true;

    /** Temporizador para controlar el tiempo entre golpes. */
    private float hitTimer = 0;

    /** Tiempo de enfriamiento entre golpes. */
    private final float hitCooldown = 2f;

    /** Vida inicial del personaje. */
    private int vida=3;

    /** Cantidad de daño del ataque que se realiza. */
    public static int cantAtaque;

    /** Temporizador para que la colision del ataque aparezca mas tarde */
    public float timer = 0;

    /** Booleano que indica si el personaje está realizando un ataque. */
    public boolean atacando = false;

    /** Tiempo de duración del ataque por boton. */
    float ataqueTime;

    /** Tiempo de duración del ataque agitando el dispositivo. */
    float ataqueTimeAgita;

    /** Booleano que indica si se ha reproducido el sonido del rayo durante el ataque. */
    boolean sonidoRayo=false;

    /** Valor de aceleración en el eje Z del acelerómetro. */
    float acelerometroZ;

    /** Booleano que indica si el personaje está realizando un ataque agitando el dispositivo. */
    boolean ataqueAgita=false;

    /** Umbral de detección para el ataque agitando el dispositivo. */
    private static final float DetectAcelerometro = 12;

    /**
     * Constructor de la clase Princ1.
     *
     * @param mundo Instancia del mundo en Box2D.
     * @param screen Referencia a la pantalla del nivel en la que se encuentra el personaje.
     */
    public Princ1(World mundo, Lvl1Screen screen){
        TextureAtlas atlas = new TextureAtlas("atlas/Fantasy_warrior.atlas");
        this.screen=screen;
        this.mundo=mundo;
        currentState=State.QUIETO;
        previousState=State.QUIETO;
        estadoTimer=0;
        correDerecha=true;

        frames= new Array<TextureRegion>();

        for (int i = 0; i < 9; i++) {
            frames.add(new TextureRegion(atlas.findRegion("Idle"),i*imagen,0,162,162));
        }
        quieto=new Animation<TextureRegion>(0.1f,frames);
        frames.clear();

        for (int i = 0; i < 7; i++) {
            frames.add(new TextureRegion(atlas.findRegion("Attack1"),i*imagen,0,162,162));
        }
        ataque1=new Animation<TextureRegion>(0.1f,frames);
        frames.clear();

        for (int i = 0; i < 7; i++) {
            frames.add(new TextureRegion(atlas.findRegion("Attack2"),i*imagen,0,162,162));

        }
        ataque2=new Animation<TextureRegion>(0.1f,frames);
        frames.clear();

        for (int i = 0; i < 8; i++) {
            frames.add(new TextureRegion(atlas.findRegion("Attack3"),i*imagen,0,162,162));
            cuenta+=i;

        }
        ataque3=new Animation<TextureRegion>(0.1f,frames);
        //  cuenta=0;
        frames.clear();

        for (int i = 0; i < 8; i++) {
            frames.add(new TextureRegion(atlas.findRegion("Run"),i*imagen,0,162,162));
        }
        correr=new Animation<TextureRegion>(0.1f,frames);
        frames.clear();

        for (int i = 0; i < 3; i++) {
            frames.add(new TextureRegion(atlas.findRegion("Jump"),i*imagen,0,162,162));
        }
        salto=new Animation<TextureRegion>(0.1f,frames);
        frames.clear();

        for (int i = 0; i < 7; i++) {
            frames.add(new TextureRegion(atlas.findRegion("Death"),i*imagen,0,162,162));
        }
        muerte=new Animation<TextureRegion>(0.1f,frames);
        frames.clear();

        for (int i = 0; i < 3; i++) {
            frames.add(new TextureRegion(atlas.findRegion("Take hit"),i*imagen,2,162,162));
        }
        danio=new Animation<TextureRegion>(0.1f,frames);
        frames.clear();

        for (int i = 0; i < 3; i++) {
            frames.add(new TextureRegion(atlas.findRegion("Fall"),i*imagen,0,162,162));

        }
        caer=new Animation<TextureRegion>(0.1f,frames);
        frames.clear();


        definePrinc1();
        stand = new TextureRegion(atlas.findRegion("Idle"),0,0,162,162);
        setBounds(0,0,162/MyGdxGame.PPM,162/MyGdxGame.PPM);
        //setRegion(stand);

    }

    /**
     * Actualiza el estado y la posición del personaje en cada fotograma.
     *
     * @param dt Delta time, tiempo transcurrido desde el último fotograma.
     */
    public void update(float dt){
        setPosition(body.getPosition().x-getWidth()/2,body.getPosition().y-getHeight()/2);
        setRegion(getFrame(dt));
        if (destroy&&!destroyed){
            def.filter.maskBits=MyGdxGame.suelo_BIT|MyGdxGame.objetos_BIT|MyGdxGame.limites_BIT;
            body.getFixtureList().first().setFilterData(def.filter);
            body.setLinearVelocity(0,0);
            if (muerte.isAnimationFinished(estadoTimer)){
                mundo.destroyBody(body);
                destroyed=true;
                estadoTimer=0;
            }
        }else if((hit&&!destroy)){
            //body.setLinearVelocity(0,0);
            if (canBeHit){
                body.setLinearVelocity(0,0);
                Gdx.app.log("p","pppppp");
                canBeHit=false;
                hitTimer=0;
            }
            if (danio.isAnimationFinished(estadoTimer)){
                hit=false;
            }

        }
        if (!canBeHit||atacando) {
            def.filter.maskBits=MyGdxGame.suelo_BIT|MyGdxGame.objetos_BIT|MyGdxGame.limites_BIT;
            body.getFixtureList().first().setFilterData(def.filter);
            hitTimer+=dt;
            if (hitTimer >= hitCooldown) {
                hitTimer=0;
                canBeHit = true;
                def.filter.maskBits=MyGdxGame.enemigo_BIT|
                        MyGdxGame.suelo_BIT|MyGdxGame.objetos_BIT|MyGdxGame.limites_BIT|MyGdxGame.cabezaEne_BIT;
                body.getFixtureList().first().setFilterData(def.filter);
            }
        }
        DetectaAclerometro();

    }

    /**
     * Obtiene el frame correspondiente según el estado actual del personaje.
     *
     * @param dt Delta time, tiempo transcurrido desde el último fotograma.
     * @return Textura correspondiente al frame actual.
     */
    private TextureRegion getFrame(float dt) {
        currentState=getState();
        TextureRegion region2;
        switch (currentState){
            case SALTANDO:
                region2= (TextureRegion) salto.getKeyFrame(estadoTimer);
                break;
            case CORRIENDO:
                region2= (TextureRegion) correr.getKeyFrame(estadoTimer,true);
                break;
            case ATTACK1:
                region2= (TextureRegion) ataque1.getKeyFrame(estadoTimer);
                break;
            case ATTACK2:
                region2= (TextureRegion) ataque2.getKeyFrame(estadoTimer);
                break;
            case ATTACK3:
                region2= (TextureRegion) ataque3.getKeyFrame(estadoTimer);
                break;
            case DAMAGE:
                region2= (TextureRegion) danio.getKeyFrame(estadoTimer);
                break;
            case CAIDA:
                region2= (TextureRegion) caer.getKeyFrame(estadoTimer,true);
                break;
            case MUERTE:
                region2= (TextureRegion)muerte.getKeyFrame(estadoTimer);
                break;
            default:
                region2=stand;
                break;
        }
        if ((body.getLinearVelocity().x<0||!correDerecha)&& !region2.isFlipX()){
            region2.flip(true,false);
            correDerecha=false;
        } else if ((body.getLinearVelocity().x>0||correDerecha) && region2.isFlipX()) {
            region2.flip(true,false);
            correDerecha=true;
        }
        estadoTimer=currentState==previousState ? estadoTimer+dt:0;
        previousState=currentState;
        return region2;
    }

    /**
     * Establece la forma de ataque del personaje.
     * Se utiliza en el proceso de realizar un ataque.
     */
    private void setAtaqueShape() {
        //Gdx.app.log("aaa",ataque3.getAnimationDuration()+"");

            ataqueDef = new FixtureDef();
            ataqueShape = new PolygonShape();
            if(correDerecha){
                ataqueShape.setAsBox(25/MyGdxGame.PPM,5/MyGdxGame.PPM,new Vector2(25/MyGdxGame.PPM,0),0);
            }else{
                ataqueShape.setAsBox(25/MyGdxGame.PPM,5/MyGdxGame.PPM,new Vector2(-25/MyGdxGame.PPM,0),0);
            }
            ataqueDef.shape = ataqueShape;
            //body.destroyFixture(body.getFixtureList().get(0));
            ataqueDef.filter.categoryBits=MyGdxGame.ataque_BIT;
            ataqueDef.filter.maskBits=MyGdxGame.enemigo_BIT;
            ataqueDef.isSensor=true;
            ataque=body.createFixture(ataqueDef);
            ataque.setUserData("1");
            def.filter.maskBits=MyGdxGame.suelo_BIT|MyGdxGame.objetos_BIT|MyGdxGame.limites_BIT;
            body.getFixtureList().first().setFilterData(def.filter);
            atacando=true;
            if (!sonidoRayo){
                MyGdxGame.assetManager.get("sonidos/rayo.wav", Sound.class).play(40);
            }
            sonidoRayo=true;



    }

    /**
     * Restaura la forma original del personaje después de realizar un ataque.
     * Se utiliza para finalizar el estado de ataque del personaje.
     */
    private void setOriginalShape(){
        def.filter.maskBits=MyGdxGame.enemigo_BIT|
                MyGdxGame.suelo_BIT|MyGdxGame.objetos_BIT|MyGdxGame.limites_BIT|MyGdxGame.cabezaEne_BIT;
        body.getFixtureList().first().setFilterData(def.filter);
        Array<Fixture> fixtures = body.getFixtureList();
        for (Fixture fixture : fixtures) {
            if (fixture.getUserData()=="1"){
                body.destroyFixture(fixture);
            }
        }
        sonidoRayo=false;
        atacando=false;
    }

    /**
     * Obtiene el estado actual del personaje en función de varios factores.
     *
     * @return Estado actual del personaje.
     */
    private State getState() {
        if((body.getLinearVelocity().y<0&& !screen.isAtaquePressed())&&!destroyed&&!destroy){
            setOriginalShape();
            return  State.CAIDA;
        }else
        if(body.getLinearVelocity().y>0&&!destroyed&&!destroy){
            setOriginalShape();
            return  State.SALTANDO;
        }else
        if ((body.getLinearVelocity().x>0||body.getLinearVelocity().x<0)&&!destroyed&&!destroy){
            setOriginalShape();
            return State.CORRIENDO;
        }else if(screen.isAtaquePressed()&&!ataque1.isAnimationFinished(estadoTimer)&&!destroy)
        {
            //hit=false;
            atacando=true;
            timer+=Gdx.graphics.getDeltaTime();
            ataqueTime+=Gdx.graphics.getDeltaTime();
            cantAtaque=40;
            if (timer>=0.4f){
                setAtaqueShape();
            }
            return State.ATTACK1;

        }else if(ataqueTime>ataque1.getAnimationDuration()&&!destroyed&&!destroy)
        {
            atacando=false;
            timer=0;
            ataqueTime=0;
            screen.setAtaquePressed(false);
            setOriginalShape();
            return State.QUIETO;

        } else if (ataqueAgita&&!ataque3.isAnimationFinished(estadoTimer)&&!destroy) {
            atacando=true;
            timer+=Gdx.graphics.getDeltaTime();
            ataqueTimeAgita+=Gdx.graphics.getDeltaTime();
            cantAtaque=61;
            if (timer>=0.4f){
                setAtaqueShape();
            }
            return State.ATTACK3;
        } else if (ataqueTimeAgita>ataque3.getAnimationDuration()&&!destroyed&&!destroy) {
            Gdx.app.log("ataqueAqitaFinised","False");
            atacando=false;
            ataqueAgita=false;
            timer=0;
            ataqueTimeAgita=0;
            setOriginalShape();
            return State.QUIETO;
        } else if (hit) {
            return  State.DAMAGE;
        } else if (destroy) {

            return  State.MUERTE;
        } else  {
            timer=0;
            setOriginalShape();
            return State.QUIETO;
        }
    }

    /**
     * Detecta la aceleración en el eje Z del acelerómetro y activa el ataque agitando el dispositivo.
     */
    public void DetectaAclerometro(){
        acelerometroZ=Gdx.input.getAccelerometerZ();
        if (acelerometroZ>DetectAcelerometro){
            acelerometroZ=0;
            ataqueAgita=true;
        }
    }

    /**
     * Establece la cantidad de vida del personaje.
     *
     * @param vida Cantidad de vida a establecer.
     */
    public void setVida(int vida) {
        this.vida = vida;
    }

    /**
     * Obtiene la cantidad actual de vida del personaje.
     *
     * @return Cantidad actual de vida del personaje.
     */
    public int getVida() {
        return vida;
    }

    /**
     * Reduce la vida del personaje cuando es golpeado.
     * Reproduce sonidos y efectos según la vida del personaje.
     */
    public void golpeado(){
        if (!atacando){
            vida--;
            if (vida>0){
                if (Preferences.getVibracion()) {
                    Gdx.input.vibrate(55);
                }
                MyGdxGame.assetManager.get("sonidos/hit.wav",Sound.class).play();
                hit=true;
            }else {
                if (Preferences.getVibracion()) {
                    Gdx.input.vibrate(55);
                }
                vida=0;
                destroy=true;
            }
        }

    }

    /**
     * Dibuja el personaje en el batch si no ha sido destruido.
     *
     * @param batch Batch en el que se realiza el dibujo.
     */
    @Override
    public void draw(Batch batch) {
        if (!destroyed){
            super.draw(batch);
        }

    }

    /**
     * Define las propiedades iniciales del personaje, como posición y forma física.
     * Utilizado al iniciar el personaje en el mundo del juego.
     */
    public void definePrinc1(){
        bodyDef =new BodyDef();
        bodyDef.position.set(80/ MyGdxGame.PPM,80/MyGdxGame.PPM);
        bodyDef.type=BodyDef.BodyType.DynamicBody;
        body=mundo.createBody(bodyDef);

        def = new FixtureDef();
        shape = new PolygonShape();
        shape.setAsBox(20/MyGdxGame.PPM,20/MyGdxGame.PPM);
        def.filter.categoryBits=MyGdxGame.personaje_BIT;
        def.filter.maskBits=MyGdxGame.enemigo_BIT|
                MyGdxGame.suelo_BIT|MyGdxGame.objetos_BIT|MyGdxGame.limites_BIT|MyGdxGame.cabezaEne_BIT;
        def.shape=shape;
        body.createFixture(def).setUserData(this);
        setVida(3);

    }
}
