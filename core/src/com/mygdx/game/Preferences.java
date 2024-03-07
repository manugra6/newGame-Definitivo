package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;
import com.mygdx.game.Screens.OptionsMenu;

import java.util.Locale;

/**
 * Clase que gestiona las preferencias del juego, almacenando datos persistentes como configuraciones
 * de idioma, archivos de texto y puntuaciones en distintos mapas.
 */
public class Preferences {

    /**
     * Preferencias de juego que almacenan datos persistentes, como puntuaciones en distintos mapas.
     * Utiliza el nombre "JuegoPrefs" para identificar las preferencias.
     */
    private static com.badlogic.gdx.Preferences preferences= Gdx.app.getPreferences("JuegoPrefs");

    /**
     * Guarda la puntuación obtenida en el mapa 1 en las preferencias.
     *
     * @param puntos Puntuación a guardar en el mapa 1.
     */
    public void guardarPuntosMap1(int puntos) {
        preferences.putInteger("puntos1", puntos);
        preferences.flush();
    }

    /**
     * Obtiene la puntuación almacenada en las preferencias para el mapa 1.
     *
     * @return Puntuación almacenada para el mapa 1, o 0 si no hay ninguna puntuación almacenada.
     */
    public static int getPuntosMap1() {
        if (!preferences.contains("puntos1")) {
            return 0;
        }
        return preferences.getInteger("puntos1");
    }

    /**
     * Guarda la puntuación obtenida en el mapa 2 en las preferencias.
     *
     * @param puntos Puntuación a guardar en el mapa 2.
     */
    public void guardarPuntosMap2(int puntos) {
        preferences.putInteger("puntos2", puntos);
        preferences.flush();
    }

    /**
     * Obtiene la puntuación almacenada en las preferencias para el mapa 2.
     *
     * @return Puntuación almacenada para el mapa 2, o 0 si no hay ninguna puntuación almacenada.
     */
    public static int getPuntosMap2() {
        if (!preferences.contains("puntos2")) {
            return 0;
        }
        return preferences.getInteger("puntos2");
    }

    /**
     * Guarda la preferencia de música.
     *
     * @param musica La preferencia de música que se va a guardar.
     */
    public void guardarMusica(boolean musica) {
        preferences.putBoolean("musica", musica);
        preferences.flush();
    }

    /**
     * Obtiene la preferencia de música.
     *
     * @return La preferencia de música si está disponible, de lo contrario, devuelve true.
     */
    public static boolean getMusica() {
        if (!preferences.contains("musica")) {
            preferences.putBoolean("musica",true);
            preferences.flush();
            return true;
        }
        return preferences.getBoolean("musica");
    }


    /**
     * Guarda la preferencia de vibración.
     *
     * @param vibracion La preferencia de vibración que se va a guardar.
     */
    public void guardarVibracion(boolean vibracion) {
        if (!preferences.contains("vibracion")) {
            preferences.putBoolean("vibracion", true);
            preferences.flush();
        } else {
            preferences.putBoolean("vibracion", vibracion);
            preferences.flush();
        }
    }

    /**
     * Obtiene la preferencia de vibración.
     *
     * @return La preferencia de vibración si está disponible, de lo contrario, devuelve true.
     */
    public static boolean getVibracion() {
        if (!preferences.contains("vibracion")) {
            return true;
        }
        return preferences.getBoolean("vibracion");
    }

    /**
     * Establece la preferencia del idioma inglés o español.
     *
     * @param ing Valor booleano que indica si se debe establecer el idioma inglés.
     */
    public static void setIngles(boolean ing){
        preferences.putBoolean("ingles",ing);
        preferences.flush();
    }

    /**
     * Obtiene la preferencia del idioma actual.
     *
     * @return True si el idioma es inglés, false si es español.
     */
    public static boolean getIngles(){
        return preferences.getBoolean("ingles",true);
    }

    /**
     * Obtiene el nombre del archivo de propiedades correspondiente al idioma actual.
     *
     * @return Nombre del archivo de propiedades (textos_en o textos_es).
     */
    public static String getPropertyFile(){
        return getIngles() ? "textos_en":"textos_es";
    }

    /**
     * Obtiene el objeto Locale correspondiente al idioma actual.
     *
     * @return Objeto Locale para el idioma actual (en para inglés, es para español).
     */
    public static Locale getLocale(){
        return getIngles() ? new Locale("en"):new Locale("es");
    }

}
