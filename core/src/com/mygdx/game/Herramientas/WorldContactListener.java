package com.mygdx.game.Herramientas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.Enemigos.Enemigo;
import com.mygdx.game.Enemigos.Gusano;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Personajes.Princ1;
import com.mygdx.game.Preferences;


/**
 * Clase que implementa la interfaz {@code ContactListener} para gestionar eventos de contacto entre fixtures en el mundo del juego.
 */
public class WorldContactListener implements ContactListener {

    /**
     * Método llamado cuando dos fixtures comienzan a tener contacto.
     *
     * @param contact El objeto que contiene información sobre el contacto.
     */
    @Override
    public void beginContact(Contact contact) {
        Fixture fix1=contact.getFixtureA();
        Fixture fix2= contact.getFixtureB();

        int def=fix1.getFilterData().categoryBits|fix2.getFilterData().categoryBits;

        switch (def){
            case MyGdxGame.enemigo_BIT|MyGdxGame.ataque_BIT:
                if (fix1.getFilterData().categoryBits==MyGdxGame.enemigo_BIT){
                    ((Enemigo)fix1.getUserData()).golpeado();
                }else if (fix2.getFilterData().categoryBits==MyGdxGame.enemigo_BIT
                ){
                    ((Enemigo)fix2.getUserData()).golpeado();
                }
                break;

            case MyGdxGame.personaje_BIT|MyGdxGame.enemigo_BIT:
                Gdx.app.log("golpeEnemigo","Golpe Enemigo");
                if (fix1.getFilterData().categoryBits==MyGdxGame.personaje_BIT){
                    ((Princ1)fix1.getUserData()).golpeado();

                    if (fix2.getUserData() instanceof Enemigo) {
                        ((Enemigo) fix2.getUserData()).golpear();
                    }

                }else if (fix2.getFilterData().categoryBits==MyGdxGame.personaje_BIT
                ){
                    ((Princ1)fix2.getUserData()).golpeado();
                    if (fix1.getUserData() instanceof Enemigo) {
                        ((Enemigo) fix1.getUserData()).golpear();
                    }

                }
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
