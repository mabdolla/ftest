package sample.Board;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Brett {
    public GraphicsContext gc;
    private int rad;
    private int kolonne;
    private int[][] brett;
    private int celleSTR = 15;
    private int gameSpeed = 40;

    //Konstruktør
    public Brett(int rad, int kolonne, GraphicsContext gc) {
        this.rad = rad;
        this.kolonne = kolonne;
        this.gc = gc;
        brett = new int [rad][kolonne];
        draw();
    }

    public Brett(int rad, int kolonne){
        this.rad = rad;
        this.kolonne = kolonne;

    }



   /* public void test(){
        brett[0][0]=1;
    }*/

    /*test mønster
    public void testArray(){
        brett[15][11]=1;
        brett[15][12]=1;
        brett[15][13]=1;
        brett[16][12]=1;
        brett[15][15]=1;
        brett[16][16]=1;
        brett[20][8]=1;
        brett[20][9]=1;
        brett[20][10]=1;
        brett[21][9]=1;

    }*/

    public void background() {

        gc.setFill(Color.YELLOWGREEN);
        gc.fillRect(0, 0, brett.length * celleSTR, brett[0].length * celleSTR);
    }

    public void draw() {
        background();
        for (int j = 0; j < brett.length; j++) {
            for (int i = 0; i < brett[0].length; i++) {
                if (brett[j][i] == 1) {
                    gc.setFill(Color.BLACK);
                    gc.fillRect(j * celleSTR, i * celleSTR, celleSTR - 1, celleSTR - 1);
                } else {
                    gc.setFill(Color.WHITE);
                    gc.fillRect(j * celleSTR, i * celleSTR, celleSTR - 1, celleSTR - 1);
                }

            }
        }
    }


    public void nextGeneration() {
        int[][] nesteBrett = new int[rad][kolonne];


        //beregning
        for (int x = 0; x < brett.length; x++) {
            for (int y = 0; y < brett[0].length; y++) {
                nesteBrett[x][y] = setCellRules(brett[x][y], getNeighbours(x, y));
            }
        }
        brett = nesteBrett;
        nesteBrett = null;





    }

    public int[][] getBrett() {
        return brett;
    }

    public int setCellRules(int isAlive, int naboer) {

        //Fucked up regler for spillet

        /*int[][] rules = {{0, 1, 1, 0, 0, 0, 0, 0, 0},
                {1, 1, 0, 0, 0, 0, 0, 0, 0}};
        return rules[isAlive][naboer];*/


       if (isAlive == 0 && naboer <= 2)
            return 0;
        if (isAlive == 0 && naboer == 3)
            return 1;
        if(isAlive == 0 && naboer >= 4)
            return 0;
        if(isAlive == 1 && naboer <= 1)
            return 0;
        if(isAlive == 1 && naboer == 2 || naboer == 3)
            return 1;
        if(isAlive == 1 && naboer >= 4)
            return 0;

        return 0;

    }


    public int getNeighbours(int x, int y) {
        //x = rader
        //y = kolonner

        int antallNaboer = 0;


        if (!(x - 1 == -1 || y - 1 == -1) && brett[x - 1][y - 1] == 1) antallNaboer++; //Oppe venstre
        if (!(y - 1 == -1) && brett[x][y - 1] == 1) antallNaboer++;              //Oppe midten
        if (!(x + 1 == brett.length || y - 1 == -1) && brett[x + 1][y - 1] == 1) antallNaboer++; //Oppe høyre
        if (!(x + 1 == brett.length || y + 1 == brett.length) && brett[x + 1][y + 1] == 1) antallNaboer++; //Nede høyre
        if (!(x - 1 == -1 || y + 1 == brett.length) && brett[x - 1][y + 1] == 1) antallNaboer++; //Nede venstre
        if (!(x + 1 == brett.length) && brett[x + 1][y] == 1) antallNaboer++;              //Midten høyre
        if (!(y + 1 == brett.length) && brett[x][y + 1] == 1) antallNaboer++;              //Nede midten
        if (!(x - 1 == -1) && brett[x - 1][y] == 1) antallNaboer++;               //Venstre midten

//        if (antallNaboer>0)
//            System.out.println(x+","+y+" har "+antallNaboer+" naboer");


       /*try {

       } catch (ArrayIndexOutOfBoundsException e){
            System.out.println("FEILET på "+x+", "+y+" : UTTAFOR");
        }*/
        return antallNaboer;
    }

    public void setCelleSTR(int CSTR) {

        this.celleSTR = CSTR;
    }

    public void setBrett(int[][] brett) {

        this.brett = brett;
    }

    public int getCelleSTR() {

        return celleSTR;
    }

    public int getRad() {

        return rad;
    }

    public void setRad(int rad) {

        this.rad = rad;
    }

    public int getKolonne() {

        return kolonne;
    }

    public void setKolonne(int kolonne) {

        this.kolonne = kolonne;
    }

    public int getGameSpeed() {

        return gameSpeed;
    }

    public void setGameSpeed(int gameSpeed) {

        this.gameSpeed = gameSpeed;
    }


    //Opprettett til BrettTest.java
   @Override
    public String toString() {
        String msg = "";
        for (int kolonne = 0; kolonne < brett.length; kolonne++) {
            for (int rad =0; rad < brett[0].length; rad++) {
                if (brett[rad][kolonne] == 0) {
                    msg = msg + "0";
                } else {
                    msg = msg + "1";
                }
            }
        }
        return msg;
    }
}