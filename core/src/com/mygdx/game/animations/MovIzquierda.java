package com.mygdx.game.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.Actor;

//CLASE PARA TODAS LAS ANIMACIONES DE LOS PERSONAJES/ENEMIGOS
public class MovIzquierda extends Actor {
    int x,y;
    Animation animation;
    float tiempo;
    TextureRegion[] regionsMovimiento;
    Texture characTxture;
    TextureRegion frameActual;

    public MovIzquierda(int x , int y, int nAnimaciones){
        this.x=x;
        this.y=y;

        characTxture = new Texture("personajes/Hero Knight/Sprites/RunBackwards.png");
        TextureRegion [][] tmp= TextureRegion.split(characTxture,
                characTxture.getWidth()/nAnimaciones, characTxture.getHeight());
        regionsMovimiento = new TextureRegion[nAnimaciones];

        for (int i = 0; i <nAnimaciones; i++) {
            regionsMovimiento[i]=tmp[0][i];
            animation=new Animation(1/10f,regionsMovimiento);
            tiempo=0f;
        }
    }
    public void render(final SpriteBatch batch) {
        //batch.draw(textureRegion,getX(), getY());
        tiempo+= Gdx.graphics.getDeltaTime();
        frameActual= (TextureRegion) animation.getKeyFrame(tiempo,true);
        x-=2;
        batch.draw(frameActual,x,y);
    }
}