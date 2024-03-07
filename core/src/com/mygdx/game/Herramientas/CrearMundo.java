package com.mygdx.game.Herramientas;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.Lvl1Screen;

/**
 * Clase encargada de crear los elementos del mundo en el nivel.
 */
public class CrearMundo {

    /**
     * Constructor de la clase que recibe la pantalla del nivel 1.
     *
     * @param screen La pantalla del nivel 1.
     */
    public CrearMundo(Lvl1Screen screen){
        World mundo = screen.getMundo();
        TiledMap map=screen.getMap();
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef=new FixtureDef();
        Body body;

        //SUELO
        for(MapObject obj:map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject)obj).getRectangle();
            bodyDef.type=BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX()+rectangle.getWidth()/2)/ MyGdxGame.PPM,
                    (rectangle.getY()+rectangle.getHeight()/2)/MyGdxGame.PPM);
            body= mundo.createBody(bodyDef);
            shape.setAsBox(rectangle.getWidth()/2/MyGdxGame.PPM,rectangle.getHeight()/2/MyGdxGame.PPM);
            fixtureDef.shape=shape;
            fixtureDef.filter.categoryBits=MyGdxGame.suelo_BIT;
            body.createFixture(fixtureDef);
        }
        //OBJETOS
        for(MapObject obj:map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject)obj).getRectangle();
            bodyDef.type=BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX()+rectangle.getWidth()/2)/MyGdxGame.PPM,
                    (rectangle.getY()+rectangle.getHeight()/2)/MyGdxGame.PPM);
            body= mundo.createBody(bodyDef);
            shape.setAsBox(rectangle.getWidth()/2/MyGdxGame.PPM,rectangle.getHeight()/2/MyGdxGame.PPM);
            fixtureDef.shape=shape;
            fixtureDef.filter.categoryBits=MyGdxGame.objetos_BIT;
            body.createFixture(fixtureDef);
        }
        //Limites mapa
        for(MapObject obj:map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject)obj).getRectangle();
            bodyDef.type=BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX()+rectangle.getWidth()/2)/MyGdxGame.PPM,
                    (rectangle.getY()+rectangle.getHeight()/2)/MyGdxGame.PPM);
            body= mundo.createBody(bodyDef);
            shape.setAsBox(rectangle.getWidth()/2/MyGdxGame.PPM,rectangle.getHeight()/2/MyGdxGame.PPM);
            fixtureDef.shape=shape;
            fixtureDef.filter.categoryBits=MyGdxGame.limites_BIT;
            body.createFixture(fixtureDef);
        }
    }
}
