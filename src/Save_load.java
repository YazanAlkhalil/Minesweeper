import java.io.*;
public class Save_load {
    // Check if there is a saved game
    static File last_game = new File("last_game.txt");
    static boolean found() {
        if (last_game.exists() && !last_game.isDirectory()) {
            return true;
        } else return false;
    }

    static void create_file() {
        try {
            last_game.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // Delete file
    static void delete_file() {
        last_game.delete();
    }

    // Save game
    static void save_game(Game_files gameFile) {
        PrintWriter s;
        try {
            s = new PrintWriter("last_game.txt");
            s.println(gameFile.playerNum);
            s.println(gameFile.numMinesOnDisplay);
            s.println(gameFile.currentPlayer);
            for(int row=0;row<gameFile.ROWS;row++)
                for(int col=0;col<gameFile.COLS;col++){
                    s.print(gameFile.numMines[row][col]);
                }
            s.println();
            for(int row=0;row<gameFile.ROWS;row++)
                for(int col=0;col<gameFile.COLS;col++){
                    s.print(gameFile.mine[row][col]);
                }
            s.println();
            for(int row=0;row<gameFile.ROWS;row++)
                for(int col=0;col<gameFile.COLS;col++){
                    s.print(gameFile.revealed[row][col]);
                }
            s.println();
            for(int row=0;row<gameFile.ROWS;row++)
                for(int col=0;col<gameFile.COLS;col++){
                    s.print(gameFile.whoRevealed[row][col]);
                }
            s.println();
            for(int row=0;row<gameFile.ROWS;row++)
                for(int col=0;col<gameFile.COLS;col++){
                    s.print(gameFile.flagged[row][col]);
                }
            s.println();
            for(int i =0;i<gameFile.playerNum;i++){
                s.println(gameFile.score[i]);
            }
            s.close();
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    // Load game
    static File load() {
            return last_game;
    }
}


