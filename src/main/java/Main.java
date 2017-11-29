
import base.*;
import base.Canvas;
import base.Color;
import lights.AmbientLight;
import lights.DirectLight;
import lights.PointLight;
import tree.BoundingBox;
import tree.TreeNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main extends JFrame{

    Scene scene = new Scene();
    //base.Canvas canvas = new base.Canvas(200, 200);
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
                scene.triangles.add(new base.Triangle(scene, Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]), new Color(255, 100, 100)));
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

        Object3d torKnot = new Object3d();
        torKnot.setScene(scene);
        BufferedReader br = new BufferedReader( new InputStreamReader(classLoader.getResourceAsStream("tor_knot.tdf")));
        try {
            br.readLine();
            int numPoints = Integer.parseInt(br.readLine());
            int numTriangles = Integer.parseInt(br.readLine());

            for (int i = 0; i < numPoints; i++){
                String strLine = br.readLine().trim();
                String[] arr = strLine.split("\\s+");
                torKnot.points.add(new Vertex3d(Float.parseFloat(arr[0])/6, Float.parseFloat(arr[1])/6, Float.parseFloat(arr[2])/6 + 200));
            }
            for (int i = 0; i < numTriangles; i++){
                String strLine = br.readLine().trim();
                String[] arr = strLine.split("\\s+");
                torKnot.triangles.add(new Triangle(torKnot, Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]), new Color(255, 100, 100)));
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
        scene.addObject(torKnot);
        torKnot.defineBoundingBox();



        Object3d tor = new Object3d();
        tor.setScene(scene);
        br = new BufferedReader( new InputStreamReader(classLoader.getResourceAsStream("torus.tdf")));
        try {
            br.readLine();
            int numPoints = Integer.parseInt(br.readLine());
            int numTriangles = Integer.parseInt(br.readLine());

            for (int i = 0; i < numPoints; i++){
                String strLine = br.readLine().trim();
                String[] arr = strLine.split("\\s+");
                tor.points.add(new Vertex3d(Float.parseFloat(arr[0]), Float.parseFloat(arr[1]) - 50, Float.parseFloat(arr[2]) + 200));
            }
            for (int i = 0; i < numTriangles; i++){
                String strLine = br.readLine().trim();
                String[] arr = strLine.split("\\s+");
                tor.triangles.add(new Triangle(tor, Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]), new Color(100, 255, 100)) );
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
        scene.addObject(tor);
        tor.defineBoundingBox();


        for (Object3d obj: scene.objects) {
            for (Triangle triangle : obj.triangles) {
                triangle.updateNomrs();
            }
            for (Triangle triangle : obj.triangles) {
                triangle.doNormalize();
            }
        }

        scene.defineBoundingBound();


        //canvas.fillCanvasV3(scene);
        ArrayList<TreeNode> nodes = null;
        nodes = scene.fillListNodes(canvas, -2, 0, scene.bBoxes.getRoot(), nodes);


        scene.addLight(new AmbientLight(0.2d));
        scene.addLight(new PointLight(0.6D, new Vector3d(2f, 400f, 100f)));
        scene.addLight(new PointLight(0.9D, new Vector3d(0f, 0f, -50f)));
        //scene.addLight(new DirectLight(0.8D, new Vector3d(0f, 4f, 4f)));




        //base.Triangle3D tr = new base.Triangle3D(new base.Vertex3d(-1d, 1d, 10d), new base.Vertex3d(1d, 1d, 10d), new base.Vertex3d(-1d, -1d, 10d));
        /*
        scene.addTriangle(new base.Triangle3D(new Vertex3d(-1d, -1d, 10d), new Vertex3d(1d, -1d, 10d), new Vertex3d(-1d, -3d, 10d)));
        scene.addTriangle(new base.Triangle3D(new Vertex3d(1d, -1d, 10d), new Vertex3d(1d, -3d, 10d), new Vertex3d(-1d, -3d, 10d)));
        scene.addTriangle(new base.Triangle3D(new Vertex3d(-1d, -1d, 10d), new Vertex3d(-1d, -1d, 20d), new Vertex3d(1d, -1d, 10d)));
        scene.addTriangle(new base.Triangle3D(new Vertex3d(-1d, -1d, 20d), new Vertex3d(1d, -1d, 20d), new Vertex3d(1d, -1d, 10d)));
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
