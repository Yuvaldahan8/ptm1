package test;


public class Board {
    private static final int SIZE = 15;
    private static Board instance = null;
    private Tile[][] board;

    private Board() {
        board = new Tile[SIZE][SIZE];
    }

    // Singleton instance getter
    public static Board getBoard() {
        if (instance == null) {
            instance = new Board();
        }
        return instance;
    }
    public Tile[][] getTiles(){//to chaeck if it puts null were there is nothing on the board
        Tile[][] copy = new Tile[SIZE][SIZE];
        for( int i=0; i<SIZE ; i++){
            for(int j=0; j<SIZE; j++){
                copy[i][j]=board[i][j];
            }
        }
        return copy;
    }
    public boolean boardLegal(Word word){
        //is the word entering the board ?
        if(word.isVertical()){//T = אנכית
            if((word.getRow()>=0)&&(word.getRow()+word.getTiles().length<SIZE)){
                return true;
            }
            return false;
        }
        else{//F= אופקית
            if((word.getCol()>=0)&&(word.getCol()+word.getTiles().length<SIZE)){
                return true;
            }
            return false;
        }

        //is the bord 
    }

}
