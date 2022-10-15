import java.awt.Color;
import java.awt.Canvas;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

public class Sim implements Runnable {

    // Global variable declaration.
    protected final String title;
    protected final int width, height;
    protected static boolean noGUI = false;
    protected boolean running = false;

    // Global object declarations.
    protected GUI ui;
    protected Canvas cv;
    protected Thread th;
    protected Environment env;

    // belowKarman vars

    // beyondKarman vars: release module

    // beyondKarman vars: orbit motion

    // lunEntry vars

    // postLunEntry vars

    // nearLunSurface vars

    public Sim(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
    }

    // Method responsible for updating values.
    protected void update() {
        switch (AssetsVars.activity) {
            case Activities.BELOW_KARMAN: {
                env = Environments.skyGrad(this.cv, 0, this.height - AssetsImg.skyGrad.getHeight());
                // env.addEntity(Entities.);
                break;
            }
        }
    }

    // Method is responsible for drawing on canvas.
    protected void render() {
        if (env != null)
            env.render();
    }

    protected void initiate() {
        ui = null;
        env = null;
        if (!this.noGUI) {
            ui = new GUI(title, width, height);
            cv = ui.getCanvas();
            Entities.initiate();
            Environments.initiate();
        }
        Console.write("\rSystems online...");
    }

    /**
     * Abstract method: Runs the code required to be run on thread
     */
    @Override
    public void run() {
        initiate();
        double deltaUpdate = 0, deltaFrames = 0;
        long now;
        long lastTime = System.nanoTime();
        double timePerFrame = 1000000000 / AssetsVars.FPS;
        double timePerUpdate = 1000000000 / (AssetsVars.UPS * (AssetsVars.warpF > 0 ? AssetsVars.warpF : 1));
        while (running) {
            now = System.nanoTime();
            deltaUpdate += (now - lastTime) / timePerUpdate;
            deltaFrames += (now - lastTime) / timePerFrame;
            lastTime = now;
            // Value updater
            if (deltaUpdate >= 1 && AssetsVars.warpF > 0) {
                // Calls update() to update values.
                update();
                deltaUpdate--;
            }
            // Frames per second, higher frames makes display smooth
            if (deltaFrames >= 1 && !noGUI) {
                // Calls render() to draw to screen.
                render();
                deltaFrames--;
            }
            if (AssetsVars.quit) running = false;
        }
        // Calls new this.stop() to close thread.
        stop();
    }

    // Method responsible for starting thread.
    public synchronized void start() {
        if (running)
            return;
        running = true;
        th = new Thread(this);
        th.start();
    }

    // Method responsible for stopping thread
    public synchronized void stop() {
        if (!running)
            return;
        running = false;
        if (running) try {
            th.join();
            System.exit(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
