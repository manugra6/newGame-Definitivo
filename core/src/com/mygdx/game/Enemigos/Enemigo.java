package com.mygdx.game.Enemigos;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Screens.Lvl1Screen;

/**
 * Clase abstracta que representa a un enemigo en el juego.
 */
public abstract class Enemigo extends Sprite {

    /**
     * Indica si el enemigo ha sido destruido.
     */
    private boolean destroyed=false;

    /**
     * Vector que representa la velocidad del enemigo.
     */
    private Vector2 velocidad;

    /**
     * Instancia del mundo.
     */
    protected World mundo;

    /**
     * Pantalla del nivel en la que se encuentra el enemigo.
     */
    protected Lvl1Screen screen;

    /**
     * Cuerpo físico del enemigo en el mundo Box2D.
     */
    public Body enemyBody;

    /**
     * Constructor de la clase Enemigo.
     *
     * @param screen Pantalla del nivel en la que se encuentra el enemigo.
     * @param x      Posición en el eje X del enemigo.
     * @param y      Posición en el eje Y del enemigo.
     */
    public Enemigo(Lvl1Screen screen,float x,float y){
        this.mundo=screen.getMundo();
        this.screen=screen;
        setPosition(x,y);
        velocidad=new Vector2(0.5f,0);
    }

    /**
     * Método abstracto para actualizar el estado del enemigo en el juego.
     *
     * @param dt Delta time, tiempo transcurrido desde la última actualización.
     */
    public abstract void update(float dt);

    /**
     * Método abstracto para definir las propiedades físicas del enemigo.
     */
    protected abstract void defineEnemigo();

    /**
     * Método abstracto que representa la acción de ser golpeado por el personaje principal.
     */
    public abstract void golpeado();

    /**
     * Método abstracto que representa la acción de golpear al personaje principal.
     */
    public abstract void golpear();

    /**
     * Invierte la dirección del enemigo en los ejes X e Y según las condiciones dadas.
     *
     * @param x Indica si se debe invertir en el eje X.
     * @param y Indica si se debe invertir en el eje Y.
     */
    public void reves(boolean x, boolean y){
        if(x){
            velocidad.x=-velocidad.x;
        }if (y){
            velocidad.y=-velocidad.y;
        }

    }

    /**
     * Verifica si el enemigo ha sido destruido.
     *
     * @return true si el enemigo ha sido destruido, false de lo contrario.
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * Obtiene el cuerpo físico del enemigo.
     *
     * @return Cuerpo físico del enemigo.
     */
    public Body getBody() {
        return  enemyBody;
    }
}
