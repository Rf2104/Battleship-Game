package game.batalhanaval;

public class Barco {

    public int tamBarco;
    public boolean vertical = true; //vertical-true, horizontal-false
    private int vida;

    private int x;
    private int y;

    public Barco(int tamBarco, boolean vertical){
        this.tamBarco = tamBarco;
        this.vertical = vertical;
        this.x = x;
        this.y = y;
        vida = tamBarco;
    }

    public void hit(){
        vida--;
    }

    public boolean isAlive(){
        return vida>0;
    }
}
