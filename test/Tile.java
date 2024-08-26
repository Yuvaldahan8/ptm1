package test;

import java.util.Random;

public class Tile {
        
    public final char letter;
    public final int score;
    
    private Tile(char letter, int score) {
        this.score=score;
        this.letter=letter;
    }


    public char getLetter() {
        return letter;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + letter;
        result = prime * result + score;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Tile other = (Tile) obj;
        if (letter != other.letter)
            return false;
        if (score != other.score)
            return false;
        return true;
    }

    public static class Bag{
        private final int[] finalamountOflleter;
        private int[] letterAmount;
        private final Tile[] tilesAndScore;
        private static Bag instance = null;

        private Bag(){
            finalamountOflleter = new int[]
            { 9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1 };
            letterAmount = finalamountOflleter.clone();
            
            tilesAndScore = new Tile[26]; 
                tilesAndScore[0] = new Tile('A', 1);
                tilesAndScore[1] = new Tile('B', 3);
                tilesAndScore[2] = new Tile('C', 3);
                tilesAndScore[3] = new Tile('D', 2);
                tilesAndScore[4] = new Tile('E', 1);
                tilesAndScore[5] = new Tile('F', 4);
                tilesAndScore[6] = new Tile('G', 2);
                tilesAndScore[7] = new Tile('H', 4);
                tilesAndScore[8] = new Tile('I', 1);
                tilesAndScore[9] = new Tile('J', 8);
                tilesAndScore[10] = new Tile('K', 5);
                tilesAndScore[11] = new Tile('L', 1);
                tilesAndScore[12] = new Tile('M', 3);
                tilesAndScore[13] = new Tile('N', 1);
                tilesAndScore[14] = new Tile('O', 1);
                tilesAndScore[15] = new Tile('P', 3);
                tilesAndScore[16] = new Tile('Q', 10);
                tilesAndScore[17] = new Tile('R', 1);
                tilesAndScore[18] = new Tile('S', 1);
                tilesAndScore[19] = new Tile('T', 1);
                tilesAndScore[20] = new Tile('U', 1);
                tilesAndScore[21] = new Tile('V', 4);
                tilesAndScore[22] = new Tile('W', 4);
                tilesAndScore[23] = new Tile('X', 8);
                tilesAndScore[24] = new Tile('Y', 4);
                tilesAndScore[25] = new Tile('Z', 10);
        }

        public Tile getRand(){
             int totalTiles = 0;
             Random random = new Random();
             for (int count : letterAmount) {
                 totalTiles += count;
             }
             if (totalTiles == 0) {
                 return null; 
             }
             while (true) {
                 int index = random.nextInt(26); 
                 
                 if (letterAmount[index] > 0) {
                     letterAmount[index]--;
                     return tilesAndScore[index]; 
                 }
             }
         }
 
        Tile getTile(char letter){
            if (letter < 'A' || letter > 'Z') {
                return null;
            }
            else {
                int index= letter -'A';
                if(letterAmount[index]==0){
                    return null;
                }
                else{
                    letterAmount[index]--;
                    return tilesAndScore[index];
                }
            }    
         }
        public void put(Tile tile){
             int index = tile.letter - 'A';
             if(letterAmount[index]<finalamountOflleter[index]){
                 letterAmount[index]++;
             }
         }    
         public int size(){
            int count=0;
            for(int i=0; i<26; i++){
                count+=letterAmount[i];
            }
            return count;
         }
         public int[] getQuantities(){
             return letterAmount.clone();
         }
         
         public static Bag getBag(){
            if (instance == null) {
                instance = new Bag();
            }
            return instance;
         }
    }
}
