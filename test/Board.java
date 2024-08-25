package test;

import java.util.ArrayList;

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

    public Tile[][] getTiles() {
        Tile[][] copy = new Tile[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                copy[i][j] = board[i][j];
            }
        }
        return copy;
    }

    public boolean boardLegal(Word word) {
        int startRow= word.getRow();
        int startCol=word.getCol();
        int legalCounter = 0; 
        boolean legalForAll = true;
        boolean forTheRest = false;
        boolean forTheFirst = false;

        for (int i = 0; i < word.getTiles().length; i++) {
            int row = word.isVertical() ? startRow + i : startRow; // T == אנכי, F == אופקי
            int col = word.isVertical() ? startCol  : startCol + i;
        
            // בדיקה אם המיקום הוא מחוץ ללוח
            if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
                legalForAll = false;
                return false;
            }
            
            // בדיקה אם המילה מתחילה במרכז הלוח (למילה הראשונה)
            if ((row == 7 && col == 7) && (board[7][7] == null)) {
                forTheFirst = true;
            }
            else if(word.getTiles()[i] != null && word.getTiles()[i].letter == '_') {

                System.out.println("here 2");
                // בדיקה אם יש כבר אות קיימת בלוח במקום של ה-_
                if (board[row][col] != null) {
                    // החלפת התו '_' באות הקיימת בלוח
                    word.getTiles()[i] = board[row][col];
        
                    // בדיקה אם המילה החדשה חוקית במילון
                    if (dictionaryLegal(word)) {
                        forTheRest = true; // האות החסרה משלימה למילה חוקית
                        System.out.println("here 3");
                    }
                }
            }
            else{
                    // בדיקה אם יש אותות סמוכות במאונך או במאוזן
                    boolean adjacent = (row > 0 && board[row - 1][col] != null) ||
                                       (row < SIZE - 1 && board[row + 1][col] != null) ||
                                       (col > 0 && board[row][col - 1] != null) ||
                                       (col < SIZE - 1 && board[row][col + 1] != null);
                    if (adjacent) {
                        forTheRest = true;
                    }
            }
            
            if(forTheRest ||forTheFirst ){
                legalCounter =1;
            }
            
        }        
        if (legalForAll) {        
            if (legalCounter==1 ) {
                return true;
            }
        }
        return false;
    }        

    public boolean isBoardEmpty() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != null) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean dictionaryLegal(Word word) {
        return true; // לשימוש עתידי, תמיד מחזיר true
    }

    public ArrayList<Word> getWords(Word word) {
        ArrayList<Word> newWords = new ArrayList<>();
        newWords.add(word);
    
        // שמור את מצב הלוח הנוכחי לצורך השוואה
        Tile[][] originalBoard = getTiles();
    
        int row = word.getRow();
        int col = word.getCol();
        Tile[] tiles = word.getTiles();
        boolean vertical = word.isVertical();
    
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i] == null) continue; // דלג על אריחים null
    
            if (vertical) {
                Word horizontalWord = extractWord(row + i, col, false);
                if (horizontalWord != null && !isWordOnBoard(horizontalWord, originalBoard)) {
                    newWords.add(horizontalWord);
                }
            } else {
                Word verticalWord = extractWord(row, col + i, true);
                if (verticalWord != null && !isWordOnBoard(verticalWord, originalBoard)) {
                    newWords.add(verticalWord);
                }
            }
        }
    
        return newWords;
    }
    
    // פונקציה בודקת אם המילה קיימת בלוח המקורי
    private boolean isWordOnBoard(Word word, Tile[][] board) {
        int startRow = word.getRow();
        int startCol = word.getCol();
        Tile[] tiles = word.getTiles();
        boolean vertical = word.isVertical();
    
        for (int i = 0; i < tiles.length; i++) {
            int row = vertical ? startRow + i : startRow;
            int col = vertical ? startCol : startCol + i;
    
            if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
                return false;
            }
            if (board[row][col] == null || !board[row][col].equals(tiles[i])) {
                return false;
            }
        }
        return true;
    }
    

    private Word extractWord(int row, int col, boolean vertical) {
        int startRow = row;
        int startCol = col;

        // מציאת נקודת ההתחלה של המילה
        if (vertical) {
            while (startRow > 0 && board[startRow - 1][col] != null) {
                startRow--;
            }
        } else {
            while (startCol > 0 && board[row][startCol - 1] != null) {
                startCol--;
            }
        }

        ArrayList<Tile> wordTiles = new ArrayList<>();
        int endRow = startRow;
        int endCol = startCol;

        // יצירת המילה מהמיקום ההתחלתי ועד הסוף
        while (endRow < SIZE && endCol < SIZE && board[endRow][endCol] != null) {
            wordTiles.add(board[endRow][endCol]);
            if (vertical) {
                endRow++;
            } else {
                endCol++;
            }
        }

        if (wordTiles.size() > 1) {
            Tile[] wordArray = wordTiles.toArray(new Tile[0]);
            return new Word(wordArray, startRow, startCol, vertical); // החזרת המיקום ההתחלתי המתאים
        }

        return null;
    }

    public int getScore(Word word) {
        int score = 0;
        int wordMultiplier = 1;

        int startrow = word.getRow();
        int startcol = word.getCol();
        for (int i = 0; i < word.getTiles().length; i++) {
            int letterScore = 0; 
            Tile tile = word.getTiles()[i];
          
        
            int curCol = word.isVertical() ? startcol : startcol + i;
            int curRow = word.isVertical() ? startrow + i : startrow;

            if (tile == null) {
                tile=board[curRow][curCol];
            } 
            int letterMultiplier = getLetterMultiplier(curCol, curRow);
            int curWordMultiplier = getWordMultiplier(curCol, curRow);

            Tile.Bag bag = Tile.Bag.getBag(); 
            letterScore = bag.getScoreOfTile(word.getTiles()[i].letter);
            if(word.getTiles()[i].letter=='_'){
                System.out.println("here here");
                letterScore= bag.getScoreOfTile(board[curRow][curCol].getLetter());
            }
            score += letterScore * letterMultiplier;
            if(curCol==7&&curRow==7 ){
                curWordMultiplier=2;
            }             
            wordMultiplier *= curWordMultiplier;   
            System.out.println(score + " "+  wordMultiplier );

        }
        System.out.println(score * wordMultiplier );
        return (score * wordMultiplier);
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
        // בדיקת משבצות שכופלות את המילה
        if ((row == 0 && col == 0) || (row == 0 && col == 7) || (row == 0 && col == 14) ||
            (row == 7 && col == 0) || (row == 7 && col == 14) || (row == 14 && col == 0) ||
            (row == 14 && col == 7) || (row == 14 && col == 14)) {
            return 3; // כפל מילה משולש
        } else if ((row == 1 && col == 1) || (row == 2 && col == 2) || (row == 3 && col == 3) ||
                   (row == 4 && col == 4) || (row == 13 && col == 1) || (row == 12 && col == 2) ||
                   (row == 11 && col == 3) || (row == 10 && col == 4) || (row == 1 && col == 13) ||
                   (row == 2 && col == 12) || (row == 3 && col == 11) || (row == 4 && col == 10) ||
                   (row == 10 && col == 10) || (row == 11 && col == 11) || (row == 12 && col == 12) ||
                   (row == 13 && col == 13)) {
            return 2; // כפל מילה כפול
        } else {
            return 1; // משבצת רגילה
        }
    }
    public int tryPlaceWord(Word word) {
        if (!boardLegal(word)) {
            return 0; // המילה אינה חוקית לפי חוקי הלוח
        }
        
        ArrayList<Word> newWords = getWords(word);
        for (Word w : newWords) {
            if (!dictionaryLegal(w)) {
                return 0; // אחת מהמילים החדשות אינה חוקית לפי המילון
            }
        }
       // הנחת המילה על הלוח
        placeWord(word);
       
        // חישוב הציון עבור כל המילים שנוצרו
        int totalScore = 0;
        for (Word w : newWords) {
            totalScore += getScore(w);
        }        
        return totalScore;
    }
    public void printBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != null) {
                    System.out.print(board[i][j].getLetter() + " ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }

    public void placeWord(Word word) {
        Tile[] tiles = word.getTiles();
        int startCol=word.getCol();
        int startRow = word.getRow();
        for (int i = 0; i < tiles.length; i++) {
            int curRow = word.isVertical() ? startRow + i : startRow;
            int curCol = word.isVertical() ?startCol : startCol+ i ;
            if((tiles[i]!=null)&&(tiles[i].getLetter() =='_') &&(board[curRow][curCol] != null)){
                tiles[i] = board[curRow][curCol];
            }
            // הנחת אריח על הלוח אם המשבצת ריקה
            if (board[curRow][curCol] == null && tiles[i] != null) {
                board[curRow][curCol] = tiles[i];
            }
        }
        printBoard();

    }

}
