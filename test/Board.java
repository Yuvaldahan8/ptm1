package test;

import java.util.ArrayList;

public class Board {
    private static final int SIZE = 15;
    private static Board instance = null;
    private Tile[][] board;
        boolean legal = true;

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
        Tile[] wordtil= word.getTiles();  
        //is the first placement is on the star ? 
        boolean isItTheFirst = true;
        if(isItTheFirst){
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
        // Does the word fit the board size?
        if (word.isVertical()) { // T = Vertical
            if ((word.getRow() < 0) || (word.getRow() + word.getTiles().length > SIZE)) {
                legal = false;
            }
        } else { // F = Horizontal
            if ((word.getCol() < 0) || (word.getCol() + word.getTiles().length > SIZE)) {
                legal = false;
            }
        }

        // Is the word leaning on an existing letter?
        for (int i = 0; i < word.getTiles().length; i++) {
            int currentRow = word.isVertical() ? word.getRow() + i : word.getRow(); // T = Vertical
            int currentCol = word.isVertical() ? word.getCol() : word.getCol() + i;  // F = Horizontal
            if((currentCol<15 && currentCol>0)&&(currentRow<15 && currentRow>0)){
                // בדיקה אם יש אריח קיים על הלוח
                if (board[currentRow][currentCol] != null) {
                    // אם האות הקיימת בלוח לא תואמת לאות של המילה החדשה
                    if (board[currentRow][currentCol].getLetter() != word.getTiles()[i].getLetter()) {
                        return false; // The word is not legal
                    }
                }
            }
            return false;
        }

        // If the word passed all checks, it's legal
        return legal;
    }
    public boolean dictionaryLegal(Word word){//for now always returning true
        return true;
    }


    public ArrayList<Word> getWords(Word word){

        ArrayList<Word> words = new ArrayList<>();
        words.add(word);
        int row = word.getRow();
        int col = word.getCol();
        Tile[] tiles = word.getTiles();
        boolean vertical = word.isVertical();

        for (int i = 0; i < tiles.length; i++) {
            if (vertical) {
                Word horizontalWord = extractWord(row + i, col, false);
                if (horizontalWord != null) {
                    words.add(horizontalWord);
                }
            } else {
                Word verticalWord = extractWord(row, col + i, true);
                if (verticalWord != null) {
                    words.add(verticalWord);
                }
            }
        }

        return words;
    }

    private Word extractWord(int row, int col, boolean vertical) {
        int startRow = row, startCol = col;

        while (startRow > 0 && board[startRow - 1][col] != null) {
            startRow--;
        }

        while (startCol > 0 && board[row][startCol - 1] != null) {
            startCol--;
        }

        ArrayList<Tile> wordTiles = new ArrayList<>();
        while (startRow < SIZE && startCol < SIZE && board[startRow][startCol] != null) {
            wordTiles.add(board[startRow][startCol]);
            if (vertical) {
                startRow++;
            } else {
                startCol++;
            }
        }

        if (wordTiles.size() > 1) {
            Tile[] wordArray = wordTiles.toArray(new Tile[0]);
            return new Word(wordArray, startRow, startCol, vertical);
        }

        return null;
    }
    public int getScore(Word word) {
        int score = 0;
        int wordMultiplier = 1;
    
        for (int i = 0; i < word.getTiles().length; i++) {
            int curCol = word.isVertical() ? word.getCol() : word.getCol() + i;
            int curRow = word.isVertical() ? word.getRow() + i : word.getRow();
            Tile[] tiles = word.getTiles();
    
            // בדיקה אם זו משבצת של כפל מילה
            int letterMultiplier = getLetterMultiplier(curCol, curRow);
            int curWordMultiplier = getWordMultiplier(curCol, curRow);
    
            // חישוב הניקוד עבור האות הנוכחית עם הכפלת הניקוד שלה
            score += tiles[i].getScore() * letterMultiplier;
    
            // הכפלת הניקוד של המילה בהתאם למשבצת
            wordMultiplier *= curWordMultiplier;
        }
    
        // החזרת הניקוד הסופי לאחר הכפלת המילה
        return score * wordMultiplier;
    }
    
    public int getLetterMultiplier(int col, int row) {
        // בדיקת משבצות שכופלות את האות
        if ((row == 1 && col == 5) || (row == 1 && col == 9) || (row == 5 && col == 1) ||
            (row == 5 && col == 5) || (row == 5 && col == 9) || (row == 5 && col == 13) ||
            (row == 9 && col == 1) || (row == 9 && col == 5) || (row == 9 && col == 9) ||
            (row == 9 && col == 13) || (row == 13 && col == 5) || (row == 13 && col == 9)) {
            return 3; // כפל אות משולש
        } else if ((row == 0 && col == 3) || (row == 0 && col == 11) || (row == 2 && col == 6) ||
                   (row == 2 && col == 8) || (row == 3 && col == 0) || (row == 3 && col == 7) ||
                   (row == 3 && col == 14) || (row == 6 && col == 2) || (row == 6 && col == 6) ||
                   (row == 6 && col == 8) || (row == 6 && col == 12) || (row == 7 && col == 3) ||
                   (row == 7 && col == 11) || (row == 8 && col == 2) || (row == 8 && col == 6) ||
                   (row == 8 && col == 8) || (row == 8 && col == 12) || (row == 11 && col == 0) ||
                   (row == 11 && col == 7) || (row == 11 && col == 14) || (row == 12 && col == 6) ||
                   (row == 12 && col == 8) || (row == 14 && col == 3) || (row == 14 && col == 11)) {
            return 2; // כפל אות כפול
        } else {
            return 1; // משבצת רגילה
        }
    }
    
    public int getWordMultiplier(int col, int row) {
        
        if ((row == 0 && col == 0) || (row == 0 && col == 7) || (row == 0 && col == 14) ||
            (row == 7 && col == 0) || (row == 7 && col == 14) || (row == 14 && col == 0) ||
            (row == 14 && col == 7) || (row == 14 && col == 14)) {
            return 3; 
        } else if ((row == col) && (row == 1 || row == 2 || row == 3 || row == 4 || row == 10 || row == 11 || row == 12 || row == 13) ||
                   (row == 1 && col == 13) || (row == 2 && col == 12) || (row == 3 && col == 11) || (row == 4 && col == 10) ||
                   (row == 10 && col == 4) || (row == 11 && col == 3) || (row == 12 && col == 2) || (row == 13 && col == 1)) {
            return 2; 
        } else {
            return 1; 
        }
    }
    
}