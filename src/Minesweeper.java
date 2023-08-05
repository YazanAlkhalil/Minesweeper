

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;


public class Minesweeper extends JFrame implements ActionListener {
  // Constants for the game board dimensions
  private static final int ROWS = 15;
  private static final int COLS = 20;
  private static final int numMines = 40;
  private int numMinesOnDisplay = numMines;
  private int screenWidth = 1280;
  private int screenHeight = 800;
  private Boolean gameStarted = false;
  // 2D array to store the game board
  private Cell[][] board;

  // The buttons and panels needed
  private JButton[] playerNumButton;
  private JLabel numMinesDisplay;
  private JLabel title;
  private JLabel playerLabel;
  private JLabel timeLabel;
  private mainMenu mainMenu;
  private JPanel panel1;
  private JPanel panel2;
  private MenuBar menubar;

  // Timer to keep track of each player's time

  private Timer timer;
  private int time;

  // images
  ImageIcon flag = new ImageIcon(System.getProperty("user.dir")+"/lib/resources/flag.png");
  ImageIcon mine = new ImageIcon(System.getProperty("user.dir")+"/lib/resources/9.png");
  ImageIcon background = new ImageIcon(System.getProperty("user.dir")+"/lib/resources/background.jpg");

  // players
  private Player []player;
  private int currentPlayer;
  private int playerNum;
  private String[] colors = new String[4];


  

  public Minesweeper() {


    // Set up the game board and initialize the cells
    board = new Cell[ROWS][COLS];
    for (int row = 0; row < ROWS; row++) {
      for (int col = 0; col < COLS; col++) {
        board[row][col] = new Cell(row, col);
      }  
    }

    


    // Set players colors
    currentPlayer = 1;
    colors[0] = "#0000FF";
    colors[1] = "#FF0000";
    colors[2] = "#00FF00";
    colors[3] = "#FFFF00";
    player = new Player[4];
    for(int i=0; i< 4; i++){
      player[i] = new Player(ROWS,COLS);
      player[i].setColor(colors[i]);
    }

    // Set up the GUI
    setTitle("Minesweeper");
    setSize(screenWidth, screenHeight);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(null);
    

    // menus and panels declaration
    menubar = new MenuBar();
    mainMenu = new mainMenu(background.getImage());
    panel1 = new JPanel();
    panel2 = new JPanel();

    mainMenu.setLayout(new GridBagLayout());
    panel1.setLayout(new GridBagLayout());
    panel2.setLayout(new GridLayout(ROWS,COLS));

    add(mainMenu);

    title = new JLabel();
    numMinesDisplay = new JLabel();
    playerLabel = new JLabel();
    timeLabel = new JLabel();




    // Adding main menu buttons
    GridBagConstraints con = new GridBagConstraints();
    title.setText("Minesweeper");
    title.setForeground(Color.darkGray);
    title.setFont(new Font("Veranda",Font.BOLD,40));
    con.insets = new Insets(0,0,30,0);
    mainMenu.add(title,con);
    con.insets = new Insets(10,0,10,0);
    con.ipadx=100;
    con.ipady=30;
    
    
    // Setting up main menu buttons
    playerNumButton = new JButton[4];
    for (int i=0;i<4;i++){
      playerNumButton[i] = new JButton("Players: "+ (i+1));
      playerNumButton[i].setFocusPainted(false);
      playerNumButton[i].addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
          mainMenu.setVisible(false);
          setJMenuBar(menubar);
          add(panel1);
          add(panel2);

          // Save the number of players
          playerNum = Integer.parseInt(((JButton) (e.getSource())).getText().replaceAll("[^0-9]", ""));
          if(playerNum==1){
            player[0].setColor("#FFFFFF");
          }
        } 
      });;
    }   

    for(int i=0;i<4;i++){
      con.gridy = i+1;
      mainMenu.add(playerNumButton[i],con);
    }
    
 

    // New game
    menubar.New.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
        resetGame();
      } 
    });

    // Save game
    menubar.Save.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
        Thread saver = new Thread(new Runnable() {
          @Override
          public void run() {            
            Game_files gameFile = new Game_files(ROWS,COLS,playerNum,numMinesOnDisplay,currentPlayer,board,player);
            Save_load.save_game(gameFile);
          }
          
        });
        saver.start();
      } 
    });

    // Load game
    menubar.Load.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
        File gameFile = Save_load.load();
        BufferedReader br;
        try {

          // Resetting the cells
          for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
              board[row][col].reset();
            }
          }
          time = 0;
          timeLabel.setText(Integer.toString(time));
          timer.stop();          
          br = new BufferedReader(new FileReader(gameFile));
          String str;
          int n = 0;
          while ((str = br.readLine()) != null) {
              n++;
              if (n == 1) {
                playerNum = Integer.parseInt(str);
                if(playerNum==1)
                  player[0].setColor("#FFFFFF");
                else
                  player[0].setColor(colors[0]);
              }
              //
              else if (n == 2) {
                numMinesOnDisplay = Integer.parseInt(str);
                numMinesDisplay.setText(Integer.toString(numMinesOnDisplay));
              }
              else if (n == 3) {
                currentPlayer = Integer.parseInt(str);
                playerLabel.setText("Player "+currentPlayer);
              }
              else if(n==4){
                int k=0;
                for (int row = 0; row < ROWS; row++)
                  for (int col = 0; col < COLS; col++)
                    board[row][col].setNumMines(Character.getNumericValue(str.charAt(k++)));
              }
              else if(n==5){
                int k =0;
                for (int row = 0; row < ROWS; row++)
                  for (int col = 0; col < COLS; col++){
                    if(str.charAt(k++) == '1')
                      board[row][col].setMine(true);
                  }
              }
              else if(n==6){
                int k =0;
                for (int row = 0; row < ROWS; row++)
                  for (int col = 0; col < COLS; col++){
                    if(str.charAt(k++) == '1'){
                      board[row][col].setRevealed(true);
                      if(board[row][col].getNumMines()==0){
                        board[row][col].setEnabled(false);
                      }
                      else{
                        board[row][col].setText(Integer.toString(board[row][col].getNumMines()));
                        board[row][col].setFont(new Font("Arial", Font.PLAIN, board[row][col].getWidth()*55/100));
                      }
                    }
                  }  
              }
              else if(n==7){
                int k =0;
                for (int row = 0; row < ROWS; row++)
                  for (int col = 0; col < COLS; col++){
                    if(board[row][col].isRevealed()){
                      board[row][col].setBackground(Color.decode(player[Character.getNumericValue(str.charAt(k))-1].getColor()));
                    }
                    k++;
                  }
              }
              else if(n==8){
                int k =0;
                for (int row = 0; row < ROWS; row++)
                  for (int col = 0; col < COLS; col++){
                    if(str.charAt(k++) == '1'){
                      board[row][col].setFlagged(true);
                      board[row][col].setIcon(resizeImageIcon(flag,board[row][col].getWidth()/2,board[row][col].getHeight()*90/100));
                    }
                  }
              }
              else if(n>8){
                  player[n-9].setScore(Integer.parseInt(str));
              }
          }
          br.close();
        } 
        catch (FileNotFoundException e1) {
          String message = "Saved game couldn't be found";
          JOptionPane.showMessageDialog(null, message);
          e1.printStackTrace();
        } catch (NumberFormatException e1) {
          e1.printStackTrace();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
    });


    // Number of players changers
    menubar.OnePlayer.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
        playerNum =1;
        player[0].setColor("#FFFFFF");
        resetGame();
      } 
    });
    menubar.TwoPlayer.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
        playerNum =2;
        player[0].setColor(colors[0]);
        resetGame();
      } 
    });
    menubar.ThreePlayer.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
        playerNum =3;
        player[0].setColor(colors[0]);
        resetGame();
      } 
    });
    menubar.FourPlayer.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
        playerNum =4;
        player[0].setColor(colors[0]);
        resetGame();
      } 
    });
    

    // upper panel Display

    // Mines number display
    numMinesDisplay.setText(Integer.toString(numMinesOnDisplay));
    numMinesDisplay.setHorizontalAlignment(SwingConstants.CENTER);
    numMinesDisplay.setBorder(BorderFactory.createBevelBorder(1));
    numMinesDisplay.setSize(new Dimension(45,45));
    numMinesDisplay.setFont(new Font("Veranda",Font.PLAIN,screenWidth*3/100));
    numMinesDisplay.setIcon(resizeImageIcon(new ImageIcon(System.getProperty("user.dir")+"/lib/resources/mine (2).png"),numMinesDisplay.getSize().width,numMinesDisplay.getSize().height));

    // Player display
    playerLabel.setText("Player 1");
    playerLabel.setBorder(BorderFactory.createBevelBorder(1));
    playerLabel.setFont(new Font("Veranda",Font.PLAIN,screenWidth*3/100));
    playerLabel.setHorizontalAlignment(SwingConstants.CENTER);
    
    // Timer display
    timeLabel.setText("0");
    timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
    timeLabel.setFont(new Font("Veranda",Font.PLAIN,screenWidth*3/100));
    timeLabel.setBorder(BorderFactory.createBevelBorder(1 ));

    
    // order setup
    GridBagConstraints c = new GridBagConstraints();
    c.anchor = GridBagConstraints.LINE_START;
    c.ipadx = 50;  
    panel1.add(numMinesDisplay,c);
    c.insets = new Insets(0,(int) (screenWidth/15),0,0);
    panel1.add(playerLabel,c);
    panel1.add(timeLabel,c);



    //listen for screen size change
    addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
          screenWidth = getSize().width;
          screenHeight = getSize().height;
          mainMenu.setBounds(0,0,screenWidth,screenHeight);
          panel1.setBounds(0,0,screenWidth,screenHeight*10/100);
          panel2.setBounds(-5,screenHeight*10/100,screenWidth,screenHeight-screenHeight*175/1000);
      }
    });

    // listen for left and right clicks
    for (int row = 0; row < ROWS; row++) {
      for (int col = 0; col < COLS; col++) {
        panel2.add(board[row][col]);
        board[row][col].addActionListener(this);
        board[row][col].setFocusPainted(false);
        board[row][col].addMouseListener(new MouseAdapter(){
          public void mouseClicked(MouseEvent e){
            if (SwingUtilities.isRightMouseButton(e)){ 
              Cell cell = (Cell) e.getSource();
              int row = cell.getRow();
              int col = cell.getCol();
              if(!board[row][col].isRevealed()){
                if(board[row][col].isFlagged()){
                  numMinesOnDisplay++;
                  numMinesDisplay.setText(Integer.toString(numMinesOnDisplay));
                  flagSubtractScore(currentPlayer,row,col);
                  board[row][col].setFlagged(false);
                  board[row][col].setIcon(null);
                }
                else{
                  numMinesOnDisplay--;
                  numMinesDisplay.setText(Integer.toString(numMinesOnDisplay));
                  flagAddScore(currentPlayer,row,col);
                  board[row][col].setFlagged(true);
                  board[row][col].setIcon(resizeImageIcon(flag,board[row][col].getWidth()/2,board[row][col].getHeight()*90/100));
                }
              }
            }
          }
        });
      }
    }
    

    // Set up the timer
    time = 0;
    timer = new Timer(1000, new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
        if(time==10){
          // Switch players
          nextPlayer();
          playerLabel.setText("Player " + currentPlayer);
          time=0;
        }
        timeLabel.setText(Integer.toString(time));
        time++;
      }



    });
    timer.setInitialDelay(0);
  }



  // Randomly assign mines to the board
  public void mineGeneration(int ROW,int COL){
    Random rand = new Random();
    for (int i = 0; i < numMines; i++) {
      int row = rand.nextInt(ROWS);
      int col = rand.nextInt(COLS);
      if (board[row][col].isMine() || (row==ROW && col == COL)) {
        // If this cell already has a mine,or the first cell try again
        i--;
      } else {
        board[row][col].setMine(true);
      }
    }
  }  

  // image size fixer
  private static ImageIcon resizeImageIcon(ImageIcon ii, int width, int height){
    ImageIcon imageIcon = new ImageIcon(ii.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
    return imageIcon;
}

  // Calculate the number of mines surrounding each cell
  private void mineCalculate(){
    for (int row = 0; row < ROWS; row++) {
      for (int col = 0; col < COLS; col++) {
        if (!board[row][col].isMine()) {
          board[row][col].setNumMines(getNumMines(row, col));
        }
      }
    }
  }
  
  // Next player method
  private void nextPlayer(){
    if(currentPlayer==playerNum)
      currentPlayer=1;
    else
      currentPlayer++;
  }

  // Calculates the number of mines surrounding the cell at the given position
  private int getNumMines(int row, int col) {
    int numMines = 0;
    for (int i = row - 1; i <= row + 1; i++) {
      for (int j = col - 1; j <= col + 1; j++) {
        if (i >= 0 && i < ROWS && j >= 0 && j < COLS && board[i][j].isMine()) {
          numMines++;
        }
      }
    }
    return numMines;
  }






  // Handles player input when a button is clicked
  public void actionPerformed(ActionEvent e) {
    // Get the cell that was clicked
    Cell cell = (Cell) e.getSource();
    int row = cell.getRow();
    int col = cell.getCol();
    // Check if cell is revealed or flagged
    if(!board[row][col].isRevealed() && !board[row][col].isFlagged()){

      // If the timer has not been started, start it
      if (!timer.isRunning() && playerNum!=1) {
        timer.start();
      }
      
    // Check if the fame is started then generating and calculating the mines
    if(!gameStarted){
      mineGeneration(row,col);
      mineCalculate();
      gameStarted = true;
    }
      
      // Check if the cell is a mine
      if (cell.isMine()) {
        // Game over
        endGame(false);
      } else {
        // Reveal the cell and its surrounding cells if there are no mines
        reveal(row, col);
        if (isGameWon())
        // Game won
        endGame(true);
          else if(playerNum!=1){
          // Switch to the other player
          nextPlayer();
          playerLabel.setText("Player " + currentPlayer);
          // Reset the timer for the next player's turn
          time = 0;
          timer.restart();
        }
      }
    }
  }
  

     


  // scores calculations
  private void addScore(int currentPlayer,int row,int col){
    int score =player[currentPlayer-1].getScore();
    if(!board[row][col].isMine()){
      if(getNumMines(row, col) ==0){
        player[currentPlayer-1].setScore(++score);
        for(int i=0;i<playerNum;i++){
          // System.out.println("Player " + (i+1) + " score :" + player[i].getScore());
        }     
      }
      else{
        player[currentPlayer-1].setScore(score+=getNumMines(row, col));
        for(int i=0;i<playerNum;i++){
          // System.out.println("Player " + (i+1) + " score :" + player[i].getScore());
        }     
      }
    }
  }
  private void flagAddScore(int currentPlayer,int row,int col){
    int score =player[currentPlayer-1].getScore();
    if(board[row][col].isMine()){
      player[currentPlayer-1].setScore(score+=5);
      for(int i=0;i<playerNum;i++){
        // System.out.println("Player " + (i+1) + " score :" + player[i].getScore());
      } 
    }
    else{
      player[currentPlayer-1].setScore(--score);
      for(int i=0;i<playerNum;i++){
        // System.out.println("Player " + (i+1) + " score :" + player[i].getScore());
      } 
    }
  }


  private void flagSubtractScore(int currentPlayer,int row,int col){
    int score =player[currentPlayer-1].getScore();
    if(board[row][col].isMine()){
      player[currentPlayer-1].setScore(score-=5);
      for(int i=0;i<playerNum;i++){
        // System.out.println("Player " + (i+1) + " score :" + player[i].getScore());
      }    
    }
    else{
      player[currentPlayer-1].setScore(++score);
      for(int i=0;i<playerNum;i++){
        // System.out.println("Player " + (i+1) + " score :" + player[i].getScore());
      }    
    }
  }
  
  
  // Recursive function to reveal the cell at the given position and its surrounding cells if there are no mines
  private void reveal(int row, int col) {
    Cell cell = board[row][col];
    if (!cell.isRevealed() && !cell.isFlagged()) {
      cell.setRevealed(true);
      //add score to the player
      cell.setWhoRevealed(currentPlayer);
      addScore(currentPlayer,row,col);
      if (cell.getNumMines() == 0) {
        cell.setBackground(Color.decode(player[currentPlayer-1].getColor()));
        cell.setEnabled(false);
        for (int i = row - 1; i <= row + 1; i++) {
          for (int j = col - 1; j <= col + 1; j++) {
            if (i >= 0 && i < ROWS && j >= 0 && j < COLS) {
              reveal(i, j);
            }
          }
        }
      }
      else{
        cell.setBackground(Color.decode(player[currentPlayer-1].getColor()));
        cell.setText(Integer.toString(cell.getNumMines()));
        cell.setFont(new Font("Arial", Font.PLAIN, cell.getWidth()*55/100));
      }
    }
  }

  // Returns true if all non-mine cells have been revealed

  private boolean isGameWon() {
    for (int row = 0; row < ROWS; row++) {
      for (int col = 0; col < COLS; col++) {
        if (!board[row][col].isMine() && !board[row][col].isRevealed()) {
          return false;
        }
      }
    }
    return true;
  }
    
  // Reset the game
  private void resetGame(){
    for (int row = 0; row < ROWS; row++) {
      for (int col = 0; col < COLS; col++) {
        board[row][col].reset();
      }
    }

    gameStarted =false;
    time = 0;
    timeLabel.setText(Integer.toString(time));
    timer.stop();
    numMinesOnDisplay = numMines;
    numMinesDisplay.setText(Integer.toString(numMinesOnDisplay));
    currentPlayer = 1;
    playerLabel.setText("Player "+currentPlayer);
    for(int i =0;i<playerNum;i++){
      player[i].setScore(0);
    }
  }
        
  // Ends the game and displays a message
  private void endGame(boolean won) {
    timer.stop();
    gameStarted = false;
    int notFlaggedMines=0;
    for (int row = 0; row < ROWS; row++) {
      for (int col = 0; col < COLS; col++) {
        if(board[row][col].isMine()){
          board[row][col].setBackground(Color.decode("#eeeeee"));
          board[row][col].setIcon(mine);
          board[row][col].setIcon(resizeImageIcon(mine,board[row][col].getWidth()/2,board[row][col].getHeight()*75/100));
          if(!board[row][col].isFlagged());
          notFlaggedMines++;
        }
      }
    }

    String message;
    int winnerPlayer = 0;
    if (won) {
      int score = player[currentPlayer-1].getScore();
      score+=100*notFlaggedMines;
      player[currentPlayer-1].setScore(score);
      for(int i=1;i<playerNum;i++){
        if(player[i].getScore()>player[winnerPlayer].getScore())
          winnerPlayer = i;
      }
    } 
    else {
      // Make sure the first player isn't the one who lost
      if(currentPlayer==1)
        winnerPlayer=1;
      for(int i=1;i<playerNum;i++){
        if(player[i].getScore()>player[winnerPlayer].getScore() && currentPlayer-1!=i)
          winnerPlayer = i;
      }
      message = "Game over";
      JOptionPane.showMessageDialog(this, message);    
    }
    if(playerNum!=1)
      message = "Player " + (winnerPlayer+1) + " wins!";
    else
      message = "";
    for(int i=0;i<playerNum;i++){
      message+="\nPlayer " + (i+1) + " score : " + player[i].getScore();
    } 
    JOptionPane.showMessageDialog(this, message);

    resetGame();
  }


  // the main method
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Thread(new Runnable(){

      @Override
      public void run() {
        Minesweeper game = new Minesweeper();
        game.setVisible(true);
        
      }
    }));
  }
}
