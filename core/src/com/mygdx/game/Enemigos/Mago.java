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
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Personajes.Princ1;
import com.mygdx.game.Screens.Lvl1Screen;

import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * Clase que representa a un enemigo tipo Mago en el juego.
 */
public class Mago extends Enemigo{

    /**
     * Región de textura para el estado de "Idle" (reposo) del gusano.
     */
    private final TextureRegion stand;

    /**
     * Enumeración que representa los posibles estados del mago.
     */
    public enum State {ATTACK,DEATH,GET_HIT,IDLE,WALK};

    /**
     * Estado actual del mago.
     */
    public State currentState;

    /**
     * Estado anterio del mago.
     */
    public State previousState;

    /**
     * Animaciónes para los diferentes estados del Mago.
     */
    private Animation<TextureRegion> ataque,muerte,danio,quieto,correr;

    /**
     * Definición del cuerpo del mago en el mundo.
     */
    private BodyDef bodyDef;

    /**
     * Cuerpo físico del mago.
     */
    private Body body;

    /**
     * Definición de la fixture del mago.
     */
    private FixtureDef def;

    /**
     * Forma del cuerpo del mago.
     */
    private PolygonShape shape;

    /**
     * Estado actual de la animación.
     */
    private float estado;

    /**
     * Tamaño de la imagen en el atlas.
     */
    int imagen=150;

    /**
     * Indica hacia que lado se mueve el mago.
     */
    boolean correDerecha;

    /**
     * Indica si el mago ha sido golpeado.
     */
    boolean hit=false;

    /**
     * Indica si el mago debe ser destruido.
     */
    boolean destroy=false;

    /**
     * Indica si el mago ha sido destruido.
     */
    boolean destroyed=false;

    /**
     * Indica si se está reproduciendo la animación de ataque.
     */
    boolean attackAni=false;

    /**
     * Vida inicial del mago.
     */
    private int vida=20;

    /**
     * Tiempo de destrucción del mago.
     */
    float destroyTime;

    /**
     * Contador que permite golpear al mago si es 1.
     */
    private static int contador=0;


    /**
     * Constructor de la clase Mago.
     *
     * @param screen La pantalla en la que aparecerá el Mago.
     * @param x      La posición inicial en el eje x.
     * @param y      La posición inicial en el eje y.
     */
    public Mago(Lvl1Screen screen, float x, float y) {
        super(screen, x, y);
        estado=0;
        TextureAtlas atlas = new TextureAtlas("atlas/magoMalo.atlas");
        Array<TextureRegion> frames= new Array<TextureRegion>();

        for (int i = 0; i < 8; i++) {
            frames.add(new TextureRegion(atlas.findRegion("Idle"),i*imagen,0,150,150));
        }
        quieto=new Animation<TextureRegion>(0.1f,frames);
        frames.clear();

        for (int i = 0; i < 9; i++) {
            frames.add(new TextureRegion(atlas.findRegion("Attack"),i*imagen,0,150,150));
        }
        ataque=new Animation<TextureRegion>(0.1f,frames);
        frames.clear();

        for (int i = 0; i < 8; i++) {
            frames.add(new TextureRegion(atlas.findRegion("Move"),i*imagen,0,150,150));
        }
        correr=new Animation<TextureRegion>(0.1f,frames);
        frames.clear();

        for (int i = 0; i < 8; i++) {
            frames.add(new TextureRegion(atlas.findRegion("Death"),i*imagen,0,150,150));
        }
        muerte=new Animation<TextureRegion>(0.1f,frames);
        frames.clear();

        for (int i = 0; i < 3; i++) {
            frames.add(new TextureRegion(atlas.findRegion("Take Hit"),i*imagen,0,150,150));
        }
        danio=new Animation<TextureRegion>(0.1f,frames);
        frames.clear();

        defineEnemigo();
        stand = new TextureRegion(atlas.findRegion("Idle"),0,0,150,150);
        setBounds(getX(),getY(),100/ MyGdxGame.PPM,100/MyGdxGame.PPM);

    }

    /**
     * Actualiza la lógica del Mago en el juego.
     *
     * @param dt Delta de tiempo entre actualizaciones.
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
            destroyTime+=dt;
            body.setLinearVelocity(0,0);
            if (destroyTime>muerte.getAnimationDuration()) {
                MyGdxGame.assetManager.get("sonidos/muerte_enemigo.wav",Sound.class).play(100);
                Array<Fixture> fixtures = body.getFixtureList();
                for (Fixture fixture : fixtures) {
                    body.destroyFixture(fixture);
                }
                screen.puntos+=25;
                mundo.destroyBody(body);
                destroyed=true;
                estado=0;
                contador=0;
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
                //estado=0;
            }
        } else if (!destroy) {
            float vX=0.3f;
            BigDecimal targetBigDecimal = new BigDecimal(Float.toString(target));
            targetBigDecimal = targetBigDecimal.setScale(1, RoundingMode.HALF_UP);
            target = targetBigDecimal.floatValue();

            BigDecimal currentXBigDecimal = new BigDecimal(Float.toString(currentX));
            currentXBigDecimal = currentXBigDecimal.setScale(1, RoundingMode.HALF_UP);
            currentX = currentXBigDecimal.floatValue();

            float direccion;
            if (!hit)   {
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
     * Dibuja al Mago en el batch proporcionado.
     *
     * @param batch batch en el que se dibujará el Mago.
     */
    public void draw(Batch batch){
        if (!destroyed){
            super.draw(batch);
        }
    }

    /**
     * Obtiene la región de textura correspondiente al estado actual del Mago.
     *
     * @param dt Delta time (tiempo entre frames).
     * @return Región de textura para el estado actual del mago.
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
        if ((body.getLinearVelocity().x<-0.2f||!correDerecha)&& !region2.isFlipX()){
            region2.flip(true,false);
            correDerecha=false;

        } else if ((body.getLinearVelocity().x>0.2f||correDerecha) && region2.isFlipX()) {
            region2.flip(true,false);
            correDerecha=true;
        }
        estado=currentState==previousState ? estado+dt:0;
        previousState=currentState;
        return region2;
    }

    /**
     * Obtiene el estado actual del mago.
     *
     * @return Estado actual del mago.
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
     * Maneja la lógica cuando el mago es golpeado.
     */
    @Override
    public void golpeado() {
        contador++;
        if (contador==1){
            vida=vida- Princ1.cantAtaque;
            Gdx.app.log("vida",vida+"");
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
     * Maneja la lógica cuando el mago realiza un golpe.
     */
    @Override
    public void golpear() {
        MyGdxGame.assetManager.get("sonidos/ataque_enemigo.wav", Sound.class).play();
        body.setLinearVelocity(0,0);
        attackAni=true;

    }

    /**
     * Implementación del método de la clase Enemigo.
     * Define las propiedades físicas del cuerpo del mago.
     */
    @Override
    protected void defineEnemigo() {
        bodyDef =new BodyDef();
        contador=0;
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
        shape.setAsBox(20/MyGdxGame.PPM,20/MyGdxGame.PPM);
        def.filter.categoryBits=MyGdxGame.enemigo_BIT;
        def.filter.maskBits=MyGdxGame.suelo_BIT|MyGdxGame.personaje_BIT|MyGdxGame.ataque_BIT;
        def.shape=shape;
        def.density=8f;
        body.createFixture(def).setUserData(this);

    }
}
