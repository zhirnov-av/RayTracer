import base.Triangle;
import base.Vector3d;
import base.Vertex3d;
import base.Color;
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
    //Canvas canvas = new Canvas(200, 200);
    Canvas canvas = new Canvas(900, 900);
    private DrawPanel panel = new DrawPanel(canvas);
    private Timer timer;

    public Main() {
        super("RayTracing...");

        setBounds(200, 200, canvas.getWidth(), canvas.getHeight());
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        createMenu();

        Container container = getContentPane();
        panel.setDoubleBuffered(true);
        container.add(panel);

        timer = new Timer(250, new TimerListener());

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

    private class TimerListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            panel.repaint(panel.getBounds());
        }
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

        /*
        BufferedReader br = new BufferedReader( new InputStreamReader(classLoader.getResourceAsStream("cube.tdf")));
        try {
            br.readLine();
            int numPoints = Integer.parseInt(br.readLine());
            int numTriangles = Integer.parseInt(br.readLine());

            for (int i = 0; i < numPoints; i++){
                String strLine = br.readLine().trim();
                String[] arr = strLine.split("\\s+");
                scene.points.add(new Vertex3d(Float.parseFloat(arr[0])/10, Float.parseFloat(arr[1])/10 - 30, Float.parseFloat(arr[2])/10 + 50));
            }
            for (int i = 0; i < numTriangles; i++){
                String strLine = br.readLine().trim();
                String[] arr = strLine.split("\\s+");
                scene.triangles.add(new Triangle(scene, Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]), new Color(255, 100, 100)));
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
        */

        BufferedReader br = new BufferedReader( new InputStreamReader(classLoader.getResourceAsStream("tor_knot.tdf")));
        try {
            br.readLine();
            int numPoints = Integer.parseInt(br.readLine());
            int numTriangles = Integer.parseInt(br.readLine());

            for (int i = 0; i < numPoints; i++){
                String strLine = br.readLine().trim();
                String[] arr = strLine.split("\\s+");
                scene.points.add(new Vertex3d(Float.parseFloat(arr[0])/6, Float.parseFloat(arr[1])/6 - 50, Float.parseFloat(arr[2])/6 + 200));
            }
            for (int i = 0; i < numTriangles; i++){
                String strLine = br.readLine().trim();
                String[] arr = strLine.split("\\s+");
                scene.triangles.add(new Triangle(scene, Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]), new Color(255, 100, 100)));
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


        int pointOffset = scene.points.size();

        br = new BufferedReader( new InputStreamReader(classLoader.getResourceAsStream("torus.tdf")));
        try {
            br.readLine();
            int numPoints = Integer.parseInt(br.readLine());
            int numTriangles = Integer.parseInt(br.readLine());

            for (int i = 0; i < numPoints; i++){
                String strLine = br.readLine().trim();
                String[] arr = strLine.split("\\s+");
                scene.points.add(new Vertex3d(Float.parseFloat(arr[0]), Float.parseFloat(arr[1]) - 70, Float.parseFloat(arr[2]) + 200));
            }
            for (int i = 0; i < numTriangles; i++){
                String strLine = br.readLine().trim();
                String[] arr = strLine.split("\\s+");
                scene.triangles.add(new Triangle(scene, pointOffset+Integer.parseInt(arr[0]), pointOffset+Integer.parseInt(arr[1]), pointOffset+Integer.parseInt(arr[2]), new Color(100, 255, 100)) );
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


        for(Triangle triangle: scene.triangles){
            triangle.updateNomrs();
        }
        for(Triangle triangle: scene.triangles){
            triangle.doNormalize();
        }


        scene.lights.add(new AmbientLight(0.2D));
        scene.lights.add(new PointLight(0.6D, new Vector3d(2f, 800f, 0f)));
        scene.lights.add(new DirectLight(0.2D, new Vector3d(1f, 4f, 4f)));




        //Triangle3D tr = new Triangle3D(new base.Vertex3d(-1d, 1d, 10d), new base.Vertex3d(1d, 1d, 10d), new base.Vertex3d(-1d, -1d, 10d));
        /*
        scene.addTriangle(new Triangle3D(new Vertex3d(-1d, -1d, 10d), new Vertex3d(1d, -1d, 10d), new Vertex3d(-1d, -3d, 10d)));
        scene.addTriangle(new Triangle3D(new Vertex3d(1d, -1d, 10d), new Vertex3d(1d, -3d, 10d), new Vertex3d(-1d, -3d, 10d)));
        scene.addTriangle(new Triangle3D(new Vertex3d(-1d, -1d, 10d), new Vertex3d(-1d, -1d, 20d), new Vertex3d(1d, -1d, 10d)));
        scene.addTriangle(new Triangle3D(new Vertex3d(-1d, -1d, 20d), new Vertex3d(1d, -1d, 20d), new Vertex3d(1d, -1d, 10d)));
        */
        MainRendererThread rendererThread = new MainRendererThread(canvas, scene);
        rendererThread.start();

        timer.start();

        panel.repaint(panel.getBounds());
    }

    public static void main(String[] args) {
        JFrame app = new Main();
        app.setVisible(true);

        System.out.println("\n");
    }
}
