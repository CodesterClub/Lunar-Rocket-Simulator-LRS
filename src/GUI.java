import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

final class GUI extends JFrame{
    //Variable declarations.
    protected final String title;
    protected final int width,height;

    //UI object declarations.
    protected Canvas cnvs_space;

    public GUI(String title,int width, int height){
        this.title = title;
        this.width = width;
        this.height = height;

        //set LookAndFeel, using default look and feel, gnome and windows supported
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");     //LINUX UI
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex){
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");     //Windows UI
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {}
        }
        createUI();
    }

    //Creates the UI with window and components
    protected void createUI(){

        //UI object definations.
        cnvs_space = new Canvas();

        //JFrame frame properties.
        setTitle("Lunar Rocket Sim LRS");
        BufferedImage AppIcon = new SpriteSheet(ImageLoader.loadImage("res/AppIcon.png")).Crop(0, 0, 48, 48);
        setIconImage(AppIcon);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width,height);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);

        //Canvas cnvs_space properties.
        cnvs_space.setBounds(0, 0, width, height);
        cnvs_space.setMinimumSize(new Dimension(width-256, height-128));
        cnvs_space.setBackground(Color.black);

        //JFrame frame adding components.
        add(cnvs_space);
        setVisible(true);
    }

    //Getter for Canvas cnvs_space.
    protected Canvas getCanvas(){
        return cnvs_space;
    }
}
