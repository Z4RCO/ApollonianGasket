package org.example;

import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.*;
import java.util.Random;


public class ApollonianGasket extends JPanel {
    private Timer timer;
    private boolean finished = false;
    private static final int width = 800;
    private static final int height = 800;
    private static final double epsilon = 0.1;
    private final List<Circle> allCircles;
    private List<Triplet> queue;

    public ApollonianGasket() throws InterruptedException {
        super();

        setSize(width, height);

        // First circle centered on canvas
        Circle c1 = new Circle((double) -1 / ((double) width / 2), (double) width / 2, (double) height / 2);
        Random r = new Random();
        double r2 = r.nextDouble(100, c1.radius / 2);
        Vector2D v = new Vector2D(c1.radius - r2, Math.PI * 2, true);


        // Second circle positioned randomly within the first
        Circle c2 = new Circle(1 / r2, (double) width / 2 + v.x, (double) height / 2 + v.y);
        var r3 = v.getMagnitude();
        v.rotate(Math.PI);
        v.setMagnitude(c1.radius - r3);

        // Third circle also positioned relative to the first
        Circle c3 = new Circle(1 / r3, (double) width / 2 + v.x, (double) height / 2 + v.y);
        allCircles = new ArrayList<>();
        allCircles.add(c1);
        allCircles.add(c2);
        allCircles.add(c3);
        // Initial triplet for generating next generation of circles
        queue = new ArrayList<>();
        queue.add(new Triplet(c1, c2, c3));


        setVisible(true);

        timer = new Timer(100, e -> repaint());

    }


    private boolean validateCircle(Circle c4, Circle c1, Circle c2, Circle c3) {
        if (c4.radius < 2) return false;
        for (Circle other : allCircles) {
            var d = c4.dist(other);
            var radiusDiff = Math.abs(c4.radius - other.radius);
            if (d < epsilon && radiusDiff < epsilon) {
                return false;
            }
        }
        if (!isTangent(c4, c1)) return false;
        if (!isTangent(c4, c2)) return false;
        return isTangent(c4, c3);
    }

    private static boolean isTangent(Circle c1, Circle c2){
        var d = c1.dist(c2);
        var r1 = c1.radius;
        var r2 = c2.radius;

        var a = Math.abs(d - (r1 + r2)) < epsilon;
        var b = Math.abs(d - Math.abs(r1 - r2)) < epsilon;

        return a || b;
    }

    private void nextGeneration(){
        var nextQueue = new ArrayList<Triplet>();
        for(Triplet triplet : queue){
            Circle c1 = triplet.c1;
            Circle c2 = triplet.c2;
            Circle c3 = triplet.c3;
            ArrayList<Double> k4 = descartes(c1, c2, c3);
            ArrayList<Circle> newCircles = complexDescartes(c1, c2, c3, k4);
            for(Circle newCircle : newCircles){
                if(validateCircle(newCircle, c1, c2, c3)){
                    allCircles.add(newCircle);
                    Triplet t1 = new Triplet(c1, c2, newCircle);
                    Triplet t2 = new Triplet(c1, c3, newCircle);
                    Triplet t3 = new Triplet(c2, c3, newCircle);
                    nextQueue.add(t1);
                    nextQueue.add(t2);
                    nextQueue.add(t3);
                }
            }
        }
        queue = nextQueue;
    }

    ArrayList<Circle> complexDescartes(Circle c1, Circle c2, Circle c3, ArrayList<Double> k4){
        double k1 = c1.bend;
        double k2 = c2.bend;
        double k3 = c3.bend;
        Complex z1 = c1.center;
        Complex z2 = c2.center;
        Complex z3 = c3.center;

        Complex zk1 = z1.scale(k1);
        Complex zk2 = z2.scale(k2);
        Complex zk3 = z3.scale(k3);
        Complex sum = zk1.add(zk2).add(zk3);

        Complex root = zk1.mult(zk2).add(zk2.mult(zk3)).add(zk1.mult(zk3));
        root = root.sqrt().scale(2);
        Complex center1 = sum.add(root).scale(1 / k4.get(0));
        Complex center2 = sum.sub(root).scale(1 / k4.get(0));
        Complex center3 = sum.sub(root).scale(1 / k4.get(1));
        Complex center4 = sum.sub(root).scale(1 / k4.get(1));

        ArrayList<Circle> circles = new ArrayList<>();
        circles.add(new Circle(k4.get(0), center1.a, center1.b));
        circles.add(new Circle(k4.get(0), center2.a, center2.b));
        circles.add(new Circle(k4.get(1), center3.a, center3.b));
        circles.add(new Circle(k4.get(1), center4.a, center4.b));
        return circles;
    }

    private ArrayList<Double> descartes(Circle c1, Circle c2, Circle c3){
        double k1 = c1.bend;
        double k2 = c2.bend;
        double k3 = c3.bend;

        double sum = k1 + k2 + k3;
        double product = Math.abs(k1 * k2 + k2 * k3 + k1 * k3);
        double root = 2 * Math.sqrt(product);
        ArrayList<Double> sol = new ArrayList<>();
        sol.add(sum + root);
        sol.add(sum - root);
        return sol;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if(finished){
            for(Circle c : allCircles) {
                g2d.drawOval((int) (c.center.a - c.radius), (int) (c.center.b - c.radius), (int) (2 * c.radius), (int) (2 * c.radius));
            }
            return;
        }
        super.paintComponent(g);
        int len1 = allCircles.size();
        nextGeneration();
        int len2 = allCircles.size();
        if(len1 == len2){
            finished = true;
            System.out.println("Finished");
            return;
        }
        //g2d.setStroke(new BasicStroke(3));
        for(Circle c : allCircles) {
            g2d.drawOval((int) (c.center.a - c.radius), (int) (c.center.b - c.radius), (int) (2 * c.radius), (int) (2 * c.radius));
        }
    }

    @Override
    public void removeNotify() {
        timer.stop();
        super.removeNotify();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        // Start the timer only when the component is displayed
        timer.start();
    }

    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Apollonian Gasket");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width + 100, height + 100);
        ApollonianGasket gasket = new ApollonianGasket();

        frame.add(gasket);
        frame.setVisible(true);
    }
}