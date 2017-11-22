import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame{

    Scene scene = new Scene();
    Canvas canvas = new Canvas(300, 300);
    private DrawPanel panel = new DrawPanel(canvas);

    public Main() {
        super("RayTracing...");

        setBounds(200, 200, 300, 300);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        createMenu();

        Container container = getContentPane();
        panel.setDoubleBuffered(true);
        container.add(panel);

        drawScene();
    }

    private void createMenu() {
        JMenuBar menu = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        for (String fileItem : new String [] { "New", "Exit" }) {
            JMenuItem item = new JMenuItem(fileItem);
            item.setActionCommand(fileItem.toLowerCase());
            item.addActionListener(new NewMenuListener());
            fileMenu.add(item);
        }
        fileMenu.insertSeparator(1);

        menu.add(fileMenu);
        setJMenuBar(menu);
    }

    private class NewMenuListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if ("exit".equals(command)) {
                System.exit(0);
            }
            if ("new".equals(command)) {
                drawScene();
            }

        }
    }

    public void drawScene(){
        //Triangle3D tr = new Triangle3D(new Point3D(-1d, 1d, 10d), new Point3D(1d, 1d, 10d), new Point3D(-1d, -1d, 10d));
        scene.addTriangle(new Triangle3D(new Point3D(-1d, -1d, 10d), new Point3D(1d, -1d, 10d), new Point3D(-1d, -3d, 10d)));
        scene.addTriangle(new Triangle3D(new Point3D(1d, -1d, 10d), new Point3D(1d, -3d, 10d), new Point3D(-1d, -3d, 10d)));
        scene.addTriangle(new Triangle3D(new Point3D(-1d, -1d, 10d), new Point3D(-1d, -1d, 20d), new Point3D(1d, -1d, 10d)));
        scene.addTriangle(new Triangle3D(new Point3D(-1d, -1d, 20d), new Point3D(1d, -1d, 20d), new Point3D(1d, -1d, 10d)));

        canvas.fillCanvas(scene);
        panel.repaint(panel.getBounds());
    }

    public static void main(String[] args) {
        JFrame app = new Main();
        app.setVisible(true);

        System.out.println("\n");
    }
}
