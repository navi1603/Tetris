import model.ui.Window;

public class Tetris {
    public static void main(String[] args) {
        Window window = new Window();
        javax.swing.SwingUtilities.invokeLater(window);
        window.addFigure();
    }

}
