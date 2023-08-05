import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

//MENUBAR for controlling the game settings
    public class MenuBar extends JMenuBar {

        JMenu PlayerMode;
        JMenuItem OnePlayer;
        JMenuItem TwoPlayer;
        JMenuItem ThreePlayer;
        JMenuItem FourPlayer;
        JMenu Game;
        JMenuItem New;
        JMenuItem Load;
        JMenuItem Save;


        public MenuBar(){

          Game = new JMenu("Game");
          New = new JMenuItem("New");
          Save = new JMenuItem("Save");
          Load = new JMenuItem("Load");
          PlayerMode = new JMenu("Player Mode");
          OnePlayer = new JMenuItem("One Player");
          TwoPlayer= new JMenuItem("Two Players");
          ThreePlayer = new JMenuItem("Three players");
          FourPlayer = new JMenuItem("Four players");
          Game.add(New);
          Game.addSeparator();
          Game.add(Save);
          Game.addSeparator();
          Game.add(Load);
          PlayerMode.add(OnePlayer);
          PlayerMode.addSeparator();
          PlayerMode.add(TwoPlayer);
          PlayerMode.addSeparator();
          PlayerMode.add(ThreePlayer);
          PlayerMode.addSeparator();
          PlayerMode.add(FourPlayer);
          add(Game);
          add(PlayerMode);
        }
    }