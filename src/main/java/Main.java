
import base.*;
import base.Canvas;
import base.Color;
import lights.AmbientLight;
import lights.PointLight;
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
    Canvas canvas = new Canvas(500, 500);
    private DrawPanel panel = new DrawPanel(canvas, scene);
    private Timer timer;

    public Main() {
        super("RayTracing...");

        setBounds(200, 200, canvas.getWidth(), canvas.getHeight()+50);
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
        torKnot.loadFromTdf("tor_knot.tdf", 1f/6f, new Vector3d(0, 0, 400), new Color(255, 100, 100));
        torKnot.defineBoundingBox();
        scene.addObject(torKnot);

        Object3d tor = new Object3d();
        tor.setScene(scene);
        tor.loadFromTdf("torus.tdf", 1, new Vector3d(0, -100, 400), new Color(100, 255, 100));
        tor.defineBoundingBox();
        scene.addObject(tor);

        Object3d cube = new Object3d();
        cube.setScene(scene);
        cube.loadFromTdf("cube.tdf", 10, new Vector3d(0, -550, 600), new Color(100, 255, 100));
        cube.defineBoundingBox();
        cube.isNeedToRenderer = true;
        scene.addObject(cube);


        for (Object3d obj: scene.objects) {
            for (Triangle triangle : obj.triangles) {
                triangle.updateNomrs();
            }
            for (Triangle triangle : obj.triangles) {
                triangle.doNormalize();
            }
        }

        long startTime = System.currentTimeMillis();
        scene.defineBoundingBound();
        long endTime = System.currentTimeMillis();

        System.out.println(String.format("Building tree: %d", endTime - startTime));




        //canvas.fillCanvasV3(scene);
        //ArrayList<TreeNode> nodes = null;
        //nodes = scene.fillListNodes(canvas, -2, 0, scene.bBoxes.getRoot(), nodes);


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
