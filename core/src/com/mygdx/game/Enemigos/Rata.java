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
 * Clase que representa a un enemigo tipo Rata en el juego.
 */
public class Rata extends Enemigo{

    /**
     * Región de textura para el estado de "Idle" (reposo) de la rata.
     */
    private final TextureRegion stand;

    /**
     * Enumeración que representa los posibles estados de la rata.
     */
    public enum State {ATTACK,DEATH,GET_HIT,IDLE,WALK};

    /**
     * Estado actual de la rata.
     */
    public State currentState;

    /**
     * Estado anterior de la rata.
     */
    public State previousState;

    /**
     * Animaciónes para los diferentes estados de la rata.
     */
    private Animation<TextureRegion> ataque,muerte,danio,quieto,correr;

    /**
     * Definición del cuerpo de la rata en el mundo.
     */
    private BodyDef bodyDef;

    /**
     * Cuerpo físico de la rata.
     */
    private Body body;

    /**
     * Definición de la fixture de la rata.
     */
    private FixtureDef def;

    /**
     * Forma del cuerpo de la rata.
     */
    private PolygonShape shape;

    /**
     * Estado actual de la animación.
     */
    private float estado;

    /**
     * Tamaño de la imagen en el atlas.
     */
    int imagen=32;

    /**
     * Indica hacia que lado se mueve la rata.
     */
    boolean correDerecha;

    /**
     * Indica si la rata ha sido golpeado.
     */
    boolean hit=false;

    /**
     * Indica si la rata debe ser destruido.
     */
    boolean destroy=false;

    /**
     * Indica si la rata ha sido destruido.
     */
    public boolean destroyed=false;

    /**
     * Indica si se está reproduciendo la animación de ataque.
     */
    boolean attackAni=false;

    /**
     * Vida inicial de la rata.
     */
    private int vida=60;

    /**
     * Contador que permite golpear a la rata si es 1.
     */
    private static int contador=0;

    /**
     * Tiempo de destrucción del gusano.
     */
    float destroyTime;

    /**
     * Posición objetivo de la rata en el eje horizontal.
     * Representa la posición hacia la cual el Mago se está moviendo, es decir hacia el personaje.
     */
    private float target;

    /**
     * Posición actual de la Rata en el eje horizontal.
     * Representa la posición actual de la Rata en el mundo del juego.
     */
    private float currentX;

    /**
     * Constructor de la clase Rata.
     *
     * @param screen La pantalla en la que aparecerá el Mago.
     * @param x      La posición inicial en el eje x.
     * @param y      La posición inicial en el eje y.
     */
    public Rata(Lvl1Screen screen, float x, float y) {
        super(screen, x, y);
        this.vida=vida;
        estado=0;
        TextureAtlas atlas = new TextureAtlas("atlas/rata.atlas");
        Array<TextureRegion> frames= new Array<TextureRegion>();

        for (int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(atlas.findRegion("rat-idle-outline"),i*imagen,0,32,32));
        }
        quieto=new Animation<TextureRegion>(0.1f,frames);
        frames.clear();

        for (int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(atlas.findRegion("rat-attack-outline"),i*imagen,0,32,32));
        }
        ataque=new Animation<TextureRegion>(0.1f,frames);
        frames.clear();

        for (int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(atlas.findRegion("rat-run-outline"),i*imagen,0,32,32));
        }
        correr=new Animation<TextureRegion>(0.1f,frames);
        frames.clear();

        for (int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(atlas.findRegion("rat-death-outline"),i*imagen,0,32,32));
        }
        muerte=new Animation<TextureRegion>(0.1f,frames);
        frames.clear();

        for (int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(atlas.findRegion("rat-hurt-outline"),i*imagen,0,32,32));
        }
        danio=new Animation<TextureRegion>(0.1f,frames);
        frames.clear();

        defineEnemigo();
        stand = new TextureRegion(atlas.findRegion("rat-idle-outline"),0,0,32,32);
        setBounds(getX(),getY(),40/ MyGdxGame.PPM,40/MyGdxGame.PPM);

    }

    /**
     * Actualiza la lógica de la Rata en el juego.
     *
     * @param dt Delta de tiempo entre actualizaciones.
     */
    public void update(float dt){
        target=screen.getPosPers();
        if (correDerecha){
            target=target-.25f;
        }else{
            target=target+.25f;
        }
        currentX=body.getPosition().x;
        setRegion(getFrame(dt));
        setPosition(body.getPosition().x-getWidth()/2,body.getPosition().y-getHeight()/2);

        if (destroy && !destroyed){
            def.filter.maskBits=MyGdxGame.suelo_BIT|MyGdxGame.ataque_BIT;
            body.setLinearVelocity(0,0);
            destroyTime+=dt;
            estado=muerte.getAnimationDuration();
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
                Vector2 vecDer =new Vector2(-8f/MyGdxGame.PPM,0);
                //correDerecha=true;
                body.applyLinearImpulse((vecDer),body.getWorldCenter(),true);
            } else if (target<currentX) {
                Vector2 vecIzq =new Vector2(8f/MyGdxGame.PPM,0);
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
                Gdx.app.log("Tamaño pantalla:",MyGdxGame.pantallaWidth+" "+MyGdxGame.pantallaHeight);
                attackAni=false;
                estado=0;
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
     * Dibuja a la Rata en el batch proporcionado.
     *
     * @param batch batch en el que se dibujará el Mago.
     */
    public void draw(Batch batch){
        if (!destroyed){
            super.draw(batch);
        }
    }

    /**
     * Obtiene la región de textura correspondiente al estado actual de la Rata.
     *
     * @param dt Delta time (tiempo entre frames).
     * @return Región de textura para el estado actual de la rata.
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
     * Obtiene el estado actual de la rata.
     *
     * @return Estado actual de la rata.
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
     * Maneja la lógica cuando la rata es golpeado.
     */
    @Override
    public void golpeado() {
        contador++;
        Gdx.app.log("contador Rata",contador+"");
        if (contador==1){
            this.vida=vida - Princ1.cantAtaque;
            Gdx.app.log("vida Rata",vida+"");
            if (vida>0){
                hit=true;
            }else{
                //Gdx.app.log("Destruye Rata",vida+"");
                MyGdxGame.assetManager.get("sonidos/muerte_enemigo.wav", Sound.class).play();
                destroy=true;
            }
        }

    }

    /**
     * Implementación del método de la clase Enemigo.
     * Maneja la lógica cuando la rata realiza un golpe.
     */
    @Override
    public void golpear() {
        MyGdxGame.assetManager.get("sonidos/ataque_enemigo.wav", Sound.class).play();
        body.setLinearVelocity(0,0);
        attackAni=true;

    }

    /**
     * Implementación del método de la clase Enemigo.
     * Define las propiedades físicas del cuerpo de la rata.
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
        shape.setAsBox(20/MyGdxGame.PPM,13/MyGdxGame.PPM);
        def.filter.categoryBits=MyGdxGame.enemigo_BIT;
        def.filter.maskBits=MyGdxGame.suelo_BIT|MyGdxGame.personaje_BIT|MyGdxGame.ataque_BIT;
        def.shape=shape;
        def.density=8f;
        body.createFixture(def).setUserData(this);

    }
}
