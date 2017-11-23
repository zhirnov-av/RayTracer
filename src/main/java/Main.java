import base.Point3D;
import lights.AmbientLight;
import lights.DirectLight;
import lights.PointLight;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main extends JFrame{

    Scene scene = new Scene();
    Canvas canvas = new Canvas(500, 500);
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
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        BufferedReader br = new BufferedReader( new InputStreamReader(classLoader.getResourceAsStream("tor_knot.tdf")));
        try {
            br.readLine();
            int numPoints = Integer.parseInt(br.readLine());
            int numTriangles = Integer.parseInt(br.readLine());

            for (int i = 0; i < numPoints; i++){
                String strLine = br.readLine().trim();
                String[] arr = strLine.split("\\s+");
                scene.points.add(new Point3D(Double.parseDouble(arr[0])/10, Double.parseDouble(arr[1])/10, Double.parseDouble(arr[2])/10 + 200));
            }
            for (int i = 0; i < numTriangles; i++){
                String strLine = br.readLine().trim();
                String[] arr = strLine.split("\\s+");
                scene.triangles.add(new Triangle(scene, Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2])));
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        scene.lights.add(new AmbientLight(0.2D));
        scene.lights.add(new PointLight(0.6D, new Point3D(2D, 1D, 0D)));
        scene.lights.add(new DirectLight(0.2D, new Point3D(1D, 4D, 4D)));




        //Triangle3D tr = new Triangle3D(new base.Point3D(-1d, 1d, 10d), new base.Point3D(1d, 1d, 10d), new base.Point3D(-1d, -1d, 10d));
        /*
        scene.addTriangle(new Triangle3D(new Point3D(-1d, -1d, 10d), new Point3D(1d, -1d, 10d), new Point3D(-1d, -3d, 10d)));
        scene.addTriangle(new Triangle3D(new Point3D(1d, -1d, 10d), new Point3D(1d, -3d, 10d), new Point3D(-1d, -3d, 10d)));
        scene.addTriangle(new Triangle3D(new Point3D(-1d, -1d, 10d), new Point3D(-1d, -1d, 20d), new Point3D(1d, -1d, 10d)));
        scene.addTriangle(new Triangle3D(new Point3D(-1d, -1d, 20d), new Point3D(1d, -1d, 20d), new Point3D(1d, -1d, 10d)));
        */

        long start = System.currentTimeMillis();
        canvas.fillCanvasV2(scene);
        long end = System.currentTimeMillis();
        System.out.println(String.format("getIntersection = %d isPointInV2 = %d traceRay = %d fillCanvas = %d", scene.times.get("getIntersection"), scene.times.get("isPointInV2"), scene.times.get("traceRay"), end - start));
        panel.repaint(panel.getBounds());
    }

    public static void main(String[] args) {
        JFrame app = new Main();
        app.setVisible(true);

        System.out.println("\n");
    }
}
