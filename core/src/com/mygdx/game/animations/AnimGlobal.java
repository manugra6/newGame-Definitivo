package com.mygdx.game.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class AnimGlobal extends Actor {
    //CLASE PARA TODAS LAS ANIMACIONES DE LOS PERSONAJES/ENEMIGOS
        int x,y;
        Animation animation;
        public float tiempo;
        TextureRegion[] regionsMovimiento;
        Texture characTxture;
        TextureRegion frameActual;
        boolean loop;

        public AnimGlobal(int x , int y, int nAnimaciones,String rutaInterna,boolean loop){
            this.x=x;
            this.y=y;
            this.loop=loop;

            characTxture = new Texture(rutaInterna);
            TextureRegion [][] tmp= TextureRegion.split(characTxture,
                    characTxture.getWidth()/nAnimaciones, characTxture.getHeight());
            regionsMovimiento = new TextureRegion[nAnimaciones];

            for (int i = 0; i <nAnimaciones; i++) {
                regionsMovimiento[i]=tmp[0][i];
                animation=new Animation(1/10f,regionsMovimiento);
                tiempo=0f;
            }
        }
        public Batch render(final SpriteBatch batch) {
            //batch.draw(textureRegion,getX(), getY());
            tiempo+= Gdx.graphics.getDeltaTime();
            frameActual= (TextureRegion) animation.getKeyFrame(tiempo,loop);
            batch.draw(frameActual,x,y);
            return batch;
        }

        public TextureRegion getFrameActual(){
            return frameActual;
        }
    }


