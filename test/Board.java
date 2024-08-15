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
        boolean legal = true;
        //is the first placement is on the star ? 
        boolean isItTheFirst = true;
        while(isItTheFirst){
            legal=false;
            for(int i=0;i<word.getTiles().length; i++){
                if((word.getCol()+i==SIZE/2)&&(word.getRow()+i==SIZE/2)){
                        legal=true;
                        break;
                }
            }
            isItTheFirst=false;
        }

        //is the word fit the board size ?
        if(word.isVertical()){//T = אנכית
            if((word.getRow()<0)||(word.getRow()+word.getTiles().length>SIZE)){
                legal= false;
            }
        }
        else{//F= אופקית
            if((word.getCol()<0)||(word.getCol()+word.getTiles().length>SIZE)){
                legal= false;
            }
        }

        //is the word leaning on an exists letter?
        for(int i=0; i<word.getTiles().length; i++){
            int currentRow = word.isVertical() ? word.getRow() + i : word.getRow();  
            int currentCol = word.isVertical() ? word.getCol() : word.getCol() + i;
            if(board[currentRow][currentCol]!=null)
            {
                legal=true;
                break;            
            }
            legal=false;
        }

        return legal;
    }

}
