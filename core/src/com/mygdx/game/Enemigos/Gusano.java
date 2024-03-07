package com.mygdx.game.Enemigos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Herramientas.WorldContactListener;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Personajes.Princ1;
import com.mygdx.game.Screens.Lvl1Screen;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Clase que representa al enemigo tipo "Gusano" en el juego.
 * Extiende de la clase Enemigo.
 */
public class Gusano extends Enemigo{

    /**
     * Región de textura para el estado de "Idle" (reposo) del gusano.
     */
    private final TextureRegion stand;

    /**
     * Enumeración que representa los posibles estados del gusano.
     */
    public enum State {ATTACK,DEATH,GET_HIT,IDLE,WALK};

    /**
     * Estado actual del gusano.
     */
    public State currentState;

    /**
     * Estado anterior del gusano.
     */
    public State previousState;

    /**
     * Animaciónes para los diferentes estados del Gusano.
     */
    private Animation<TextureRegion> ataque,muerte,danio,quieto,correr;

    /**
     * Definición del cuerpo del gusano en el mundo.
     */
    private BodyDef bodyDef;

    /**
     * Cuerpo físico del gusano.
     */
    private Body body;

    /**
     * Definición de la fixture del gusano.
     */
    private FixtureDef def;

    /**
     * Forma del cuerpo del gusano.
     */
    private PolygonShape shape;

    /**
     * Estado actual de la animación.
     */
    private float estado;

    /**
     * Tamaño de la imagen en el atlas.
     */
    int imagen=92;

    /**
     * Indica hacia que lado se mueve el gusano.
     */
    boolean correDerecha;

    /**
     * Indica si el gusano ha sido golpeado.
     */
    boolean hit=false;

    /**
     * Indica si el gusano debe ser destruido.
     */
    boolean destroy=false;

    /**
     * Indica si el gusano ha sido destruido.
     */
    public boolean destroyed=false;

    /**
     * Indica si se está reproduciendo la animación de ataque.
     */
    boolean attackAni=false;

    /**
     * Vida inicial del gusano.
     */
    private int vida=50;

    /**
     * Tiempo de destrucción del gusano.
     */
    float destroyTime;

    /**
     * Contador que permite golpear al gusano si es 1.
     */
    private static int contador=0;

    /**
     * Atlas de texturas para el gusano.
     */
    TextureAtlas atlas;

    /**
     * Constructor de la clase Gusano.
     *
     * @param screen Instancia de la pantalla del nivel.
     * @param x      Posición inicial en el eje X.
     * @param y      Posición inicial en el eje Y.
     */
    public Gusano(Lvl1Screen screen, float x, float y) {
        super(screen, x, y);
        estado=0;
        atlas = new TextureAtlas("atlas/Gusano.atlas");
        Array<TextureRegion> frames= new Array<TextureRegion>();

        for (int i = 0; i < 8; i++) {
            frames.add(new TextureRegion(atlas.findRegion("Idle"),i*imagen,0,92,92));
        }
        quieto=new Animation<TextureRegion>(0.1f,frames);
        frames.clear();

        for (int i = 0; i < 9; i++) {
            frames.add(new TextureRegion(atlas.findRegion("Attack"),i*imagen,0,92,92));
        }
        ataque=new Animation<TextureRegion>(0.1f,frames);
        frames.clear();

        for (int i = 0; i < 8; i++) {
            frames.add(new TextureRegion(atlas.findRegion("Walk"),i*imagen,0,92,92));
        }
        correr=new Animation<TextureRegion>(0.1f,frames);
        frames.clear();

        for (int i = 0; i < 8; i++) {
            frames.add(new TextureRegion(atlas.findRegion("Death"),i*imagen,0,92,92));
        }
        muerte=new Animation<TextureRegion>(0.1f,frames);
        frames.clear();

        for (int i = 0; i < 3; i++) {
            frames.add(new TextureRegion(atlas.findRegion("Get Hit"),i*imagen,0,92,92));
        }
        danio=new Animation<TextureRegion>(0.1f,frames);
        frames.clear();

        defineEnemigo();
        stand = new TextureRegion(atlas.findRegion("Idle"),0,0,92,92);
        setBounds(getX(),getY(),65/MyGdxGame.PPM,65/MyGdxGame.PPM);

    }

    /**
     * Actualiza la lógica del gusano.
     *
     * @param dt Delta time (tiempo entre frames).
     */
    public void update(float dt){
        float target=screen.getPosPers();
        if (correDerecha){
            target=target-.25f;
        }else{
            target=target+.25f;
        }
        //float target=screen.getPosPers();
        float currentX=body.getPosition().x;
        setRegion(getFrame(dt));
        setPosition(body.getPosition().x-getWidth()/2,body.getPosition().y-getHeight()/2);

        if (destroy && !destroyed){
            def.filter.maskBits=MyGdxGame.suelo_BIT|MyGdxGame.ataque_BIT;
            body.setLinearVelocity(0,0);
            destroyTime+=dt;
            if (destroyTime>muerte.getAnimationDuration()) {
                MyGdxGame.assetManager.get("sonidos/muerte_enemigo.wav",Sound.class).play(100);
                Array<Fixture> fixtures = body.getFixtureList();
                for (Fixture fixture : fixtures) {
                    body.destroyFixture(fixture);
                }
                screen.puntos+=40;
                mundo.destroyBody(body);
                destroyed=true;
                estado=0;
                contador=0;
                //screen.eliminarEnemigo(this);
            }
        } else if (hit) {
            if (target>currentX){
                Vector2 vecDer =new Vector2(-5f/MyGdxGame.PPM,0);
                //correDerecha=true;
                body.applyLinearImpulse((vecDer),body.getWorldCenter(),true);
            } else if (target<currentX) {
                Vector2 vecIzq =new Vector2(5f/MyGdxGame.PPM,0);
                //correDerecha=false;
                body.applyLinearImpulse((vecIzq),body.getWorldCenter(),true);
            }

            if (danio.isAnimationFinished(estado)){
                hit = false;
            }

        } else if (attackAni) {
            body.setLinearVelocity(0,0);
                if (ataque.isAnimationFinished(estado)){
                    Gdx.app.log("AttackAni","Enemigo ataca");
                    attackAni=false;
                    estado=0;

            }
        } else if (!destroy) {
            float vX=0.25f;
            BigDecimal targetBigDecimal = new BigDecimal(Float.toString(target));
            targetBigDecimal = targetBigDecimal.setScale(1, RoundingMode.HALF_UP);
            target = targetBigDecimal.floatValue();

            BigDecimal currentXBigDecimal = new BigDecimal(Float.toString(currentX));
            currentXBigDecimal = currentXBigDecimal.setScale(1, RoundingMode.HALF_UP);
            currentX = currentXBigDecimal.floatValue();

            float direccion;
            if (!hit)   {
                contador=0;
                if (target>currentX){
                    direccion=1;
                    body.setLinearVelocity(direccion*vX,0);
                }else if (target==currentX){
                    direccion=0;
                    attackAni=true;
                    body.setLinearVelocity(direccion*vX,0);
                }else{
                    direccion=-1;
                    body.setLinearVelocity(direccion*vX,0);
                }
            }else {
                direccion=0;
            }
            setPosition(body.getPosition().x-getWidth()/2,body.getPosition().y-getHeight()/2);
        }

    }

    /**
     * Dibuja el gusano en el batch proporcionado.
     *
     * @param batch Batch en el que dibujar el gusano.
     */
    public void draw(Batch batch){
        if (!destroyed){
            super.draw(batch);
        }
    }

    /**
     * Obtiene la región de textura correspondiente al estado actual del gusano.
     *
     * @param dt Delta time (tiempo entre frames).
     * @return Región de textura para el estado actual del gusano.
     */
    private TextureRegion getFrame(float dt) {
        currentState=getState();
        TextureRegion region2;
        switch (currentState){
            case WALK:
                region2= (TextureRegion) correr.getKeyFrame(estado,true);
                break;
            case ATTACK:
                region2= (TextureRegion) ataque.getKeyFrame(estado);
                break;
            case DEATH:
                region2= (TextureRegion) muerte.getKeyFrame(estado);
                break;
            case IDLE:
                region2= (TextureRegion) quieto.getKeyFrame(estado);
                break;
            case GET_HIT:
                region2= (TextureRegion) danio.getKeyFrame(estado,true);
                break;
            default:
                region2=stand;
                break;
        }
        if ((body.getLinearVelocity().x<-0.1f||!correDerecha)&& !region2.isFlipX()){
                region2.flip(true,false);
                correDerecha=false;

        } else if ((body.getLinearVelocity().x>0.1f||correDerecha) && region2.isFlipX()) {
                region2.flip(true,false);
                correDerecha=true;
        }
        estado=currentState==previousState ? estado+dt:0;
        previousState=currentState;
        return region2;
    }

    /**
     * Obtiene el estado actual del gusano.
     *
     * @return Estado actual del gusano.
     */
    private State getState() {
        if ((body.getLinearVelocity().x>0||body.getLinearVelocity().x<0)&&!hit){
            return State.WALK;
        }else if (destroy){
            return State.DEATH;
        }else if (hit){
            //hit=false;
            return State.GET_HIT;
        } else if (attackAni) {
            return State.ATTACK;
        } else{
            return State.IDLE;
        }
    }

    /**
     * Implementación del método de la clase Enemigo.
     * Maneja la lógica cuando el gusano es golpeado.
     */
    @Override
    public void golpeado() {
        contador++;
        if (contador==1){
            vida=vida-Princ1.cantAtaque;
            Gdx.app.log("ataque",Princ1.cantAtaque+"");
            if (vida>0){

                hit=true;
            }else{
                MyGdxGame.assetManager.get("sonidos/muerte_enemigo.wav", Sound.class).play();
                destroy=true;
            }
        }

    }


    /**
     * Implementación del método de la clase Enemigo.
     * Maneja la lógica cuando el gusano realiza un golpe.
     */
    @Override
    public void golpear() {
        MyGdxGame.assetManager.get("sonidos/ataque_enemigo.wav", Sound.class).play();
        body.setLinearVelocity(0,0);
        attackAni=true;

    }

    /**
     * Implementación del método de la clase Enemigo.
     * Define las propiedades físicas del cuerpo del gusano.
     */
    @Override
    protected void defineEnemigo() {
        contador=0;
        bodyDef =new BodyDef();
        float x = MathUtils.random(10/MyGdxGame.PPM, 600/MyGdxGame.PPM);
        bodyDef.position.set(x,50/MyGdxGame.PPM);
        bodyDef.type=BodyDef.BodyType.DynamicBody;
        body=mundo.createBody(bodyDef);

        FixtureDef cabezaDef = new FixtureDef();
        PolygonShape cabeza = new PolygonShape();
        cabeza.setAsBox(5/MyGdxGame.PPM,1/MyGdxGame.PPM,new Vector2(0,11/MyGdxGame.PPM),1/MyGdxGame.PPM);
        cabezaDef.filter.categoryBits=MyGdxGame.cabezaEne_BIT;
        cabezaDef.filter.maskBits=MyGdxGame.personaje_BIT;
        cabezaDef.shape=cabeza;
        cabezaDef.restitution=1f;
        body.createFixture(cabezaDef).setUserData(this);

        def = new FixtureDef();
        shape = new PolygonShape();
        shape.setAsBox(13/MyGdxGame.PPM,10/MyGdxGame.PPM);
        def.filter.categoryBits=MyGdxGame.enemigo_BIT;
        def.filter.maskBits=MyGdxGame.suelo_BIT|MyGdxGame.personaje_BIT|MyGdxGame.ataque_BIT;
        def.shape=shape;
        def.density=8f;
        body.createFixture(def).setUserData(this);

    }
}
