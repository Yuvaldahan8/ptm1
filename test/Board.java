package test;

import java.util.ArrayList;

public class Board {
    private static final int SIZE = 15;
    private static Board instance = null;
    private Tile[][] board;
    private int firstTime = 0;
    int totalScore=0; 

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

    public boolean dictionaryLegal(Word word) {
        return true; // לשימוש עתידי, תמיד מחזיר true
    }
    private ArrayList<Word> getAllWords(Tile[][] ts) {
        ArrayList<Word> ws = new ArrayList<>();
    
        // מציאת מילים אופקיות
        for (int i = 0; i < ts.length; i++) {
            int j = 0;
            while (j < ts[i].length) {
                ArrayList<Tile> tal = new ArrayList<>();
                int startCol = j;
                while (j < ts[i].length && ts[i][j] != null) {
                    tal.add(ts[i][j]);
                    j++;
                }
                if (tal.size() > 1) {
                    Tile[] tiles = new Tile[tal.size()];
                    ws.add(new Word(tal.toArray(tiles), i, startCol, false));
                }
                j++;
            }
        }
    
        // מציאת מילים אנכיות
        for (int j = 0; j < ts[0].length; j++) {
            int i = 0;
            while (i < ts.length) {
                ArrayList<Tile> tal = new ArrayList<>();
                int startRow = i;
                while (i < ts.length && ts[i][j] != null) {
                    tal.add(ts[i][j]);
                    i++;
                }
                if (tal.size() > 1) {
                    Tile[] tiles = new Tile[tal.size()];
                    ws.add(new Word(tal.toArray(tiles), startRow, j, true));
                }
                i++;
            }
        }
    
        return ws;
    }
    
    public ArrayList<Word> getWords(Word w) {
        Tile[][] ts = getTiles();
        ArrayList<Word> before = getAllWords(ts);
    
        // הנחת המילה על הלוח
        int row = w.getRow();
        int col = w.getCol();
        for (int i = 0; i < w.getTiles().length; i++) {
            if (w.isVertical()) {
                ts[row][col] = w.getTiles()[i];
                row++;
            } else {
                ts[row][col] = w.getTiles()[i];
                col++;
            }
        }
    
        ArrayList<Word> after = getAllWords(ts);
        after.removeAll(before); // השוואת המילים החדשות לאלו הקיימות
    
        return after;
    }
    
    public int getTotalScore(Word word) {
        int score = 0;
        int wordMultiplier = 1;
        int startRow = word.getRow();
        int startCol = word.getCol();
    
        for (int i = 0; i < word.getTiles().length; i++) {
            Tile tile = word.getTiles()[i];
            int curCol = word.isVertical() ? startCol : startCol + i;
            int curRow = word.isVertical() ? startRow + i : startRow;
                if (tile == null) {
                tile = board[curRow][curCol];
            }
                if (tile == null) {
                continue;
            }
            int letterScore = Tile.Bag.getBag().getScoreOfTile(tile.getLetter());
    
            int letterMultiplier = getLetterMultiplier(curCol, curRow);
            int curWordMultiplier = getWordMultiplier(curCol, curRow);
    
            score += letterScore * letterMultiplier;

            if ((curCol == 7 && curRow == 7)&& (firstTime==0)) {
                firstTime++;
                curWordMultiplier = 2;
            }
            wordMultiplier *=curWordMultiplier;
        }
    
        int totalScore = score * wordMultiplier;  

        System.out.println(score * wordMultiplier );

        return totalScore;
    }
    
    public int getScore(Word word) {
        // חישוב הניקוד של המילה שהונחה
        totalScore = getTotalScore(word);
    
        // מציאת כל המילים החדשות שנוצרו
        ArrayList<Word> newWords = getWords(word);
    
        // חישוב הניקוד של כל מילה חדשה
        for (Word newWord : newWords) {
            if (!newWord.equals(word)) { // ודא שלא מחשבים שוב את המילה המקורית
                totalScore += getTotalScore(newWord);
            }
        }
        System.out.println(totalScore+"here" );

        return totalScore;
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
        
        // הנחת המילה על הלוח
        placeWord(word);
        
        // חישוב הציון עבור כל המילים החדשות שנוצרו
        ArrayList<Word> newWords = getWords(word);
        int totalScore = getScore(word); // ניקוד של המילה הראשית
        
        for (Word w : newWords) {
            if (!w.equals(word)) { // ודא שלא מחשבים שוב את המילה המקורית
                totalScore += getScore(w);
            }
        }
        
        return totalScore;
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
}
