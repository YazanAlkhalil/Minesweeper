
public class Player {
  private String Color;
  private int score;
  public Player(int ROWS,int COLS){
    score=0;
  }
  public String getColor() {
    return Color;
  }
  public void setColor(String color) {
    Color = color;
  }
  public int getScore() {
    return score;
  }
  public void setScore(int score) {
    this.score = score;
  }
}