
import java.io.File;

// Class for saving game data
public class Game_files {
     File last_game = new File("last_game.txt");
     int ROWS,COLS, numMines[][],numMinesOnDisplay,currentPlayer,playerNum,score[],whoRevealed[][];
     char mine[][];
     char revealed[][];
     char flagged[][];


    // the constructor takes data of the game and saves it in a formate that can be saved(int and boolean)
    Game_files(int ROWS,int COLS,int playerNum,int numMinesOnDisplay,int currentPlayer,Cell[][] board,Player player[]){
        this.ROWS = ROWS;
        this.COLS = COLS;
        this.playerNum = playerNum;
        this.numMinesOnDisplay = numMinesOnDisplay;
        this.currentPlayer=currentPlayer;
        
        numMines = new int[ROWS][COLS];
        whoRevealed = new int[ROWS][COLS];
        score = new int[playerNum];
        mine = new char[ROWS][COLS];
        revealed = new char[ROWS][COLS];
        flagged = new char[ROWS][COLS];
        for(int i=0;i<playerNum;i++){
            score[i]=player[i].getScore();
        }
        for(int row = 0; row < ROWS ; row++)
            for(int col = 0; col < COLS ; col++){
                numMines[row][col] = board[row][col].getNumMines();
                if(board[row][col].isMine())
                    mine[row][col] = '1';
                else
                    mine[row][col] = '0';
                if(board[row][col].isRevealed())
                    revealed[row][col] = '1';
                else
                    revealed[row][col] = '0';
                if(board[row][col].isFlagged())
                    flagged[row][col] = '1';
                else
                    flagged[row][col] = '0';
                whoRevealed[row][col] =board[row][col].getWhoRevealed();
            }
    }
    
    
}