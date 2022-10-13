import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

class Sim implements Runnable {

    // Global variable declaration.
    protected static boolean noGUI = false;
    protected final String title;
    protected final int width, height;
    protected boolean running = false;
    protected int CNVS_WIDTH;
    protected int CNVS_HEIGHT;

    // Global object declarations.
    protected GUI ui;
    protected BufferStrategy bs;
    protected Graphics gfx;
    protected Thread th;

    protected byte state;
    protected double obsTime;
    protected boolean outOfFuel = false;

    // belowKarman vars
    protected int xRktLaunch = (int) (5.8 * 64), yRktCone = 66 - 40, yRktS3 = 130 - 40, yRktS2 = 258 - 40, yRktS1 = 386 - 40, yRktBurn = 514 - 40, xFlame = (int) (5.8 * 64) + 15;
    protected int yTow = 1 * 64 - 40, yPad = 8 * 64 - 40, ySky = -9232, yStars = 0, yStage = 0;

    // beyondKarman vars: release module
    protected int yCovUp = 160;
    protected int yCovDown = 6 * 64;
    protected int xPayload = -192;
    protected int fx = 0;

    // beyondKarman vars: orbit motion
    protected int xMoon, yMoon;
    protected int xOrbiter, yOrbiter, aOrbiter = 30, bOrbiter = 30, hOrbiter, kOrbiter;
    protected double distOrbiter = 5.0;
    protected double tOrbiter = 0, wOrbiter;

    // lunEntry vars
    protected int yEntry, xEntry = 0;               // x and y, projectile motion trajectory
    protected double entryH, entryR, entryVx;       // param of a projectile motion

    // postLunEntry vars
    protected int yLander;                          // y, linear  downward motion of lander

    // nearLunSurface vars
    protected int yLun;                             // y, linear upward motion of lunar surface

    public Sim(String title, int width, int height) {
        this.state = 0;
        this.title = title;
        this.width = width;
        this.height = height;
    }

    // Method responsible for updating values.
    protected void update() {
        if (AssetsVars.activity >= Activities.LAUNCH) {
            // Time
            AssetsVars.upTime += 1 / AssetsVars.UPS;
            // Calculations are started after launch. they can be done outside this if as well
            AssetsVars.accG = AssetsVars.G_MEarth / Math.pow(AssetsVars.altitude + AssetsVars.radiusEarth, 2);
            AssetsVars.dM = (AssetsVars.throttle / 100 * AssetsVars.ThrustMAX) / (AssetsVars.Isp * AssetsVars.accG);
            if (AssetsVars.Mt >= AssetsVars.M0 && AssetsVars.Mfuel_t > 0) {
                AssetsVars.Mt -=  AssetsVars.dM;
                AssetsVars.Mfuel_t -= AssetsVars.dM;
                AssetsVars.fuel = AssetsVars.Mfuel_t / AssetsVars.Mfuel0 * 100;
            }
            else if (!outOfFuel) {
                AssetsVars.throttle = 0;
                AssetsVars.Mfuel_t = 0;
                AssetsVars.fuel = 0;
                outOfFuel = true;
                Console.write("\r[E] Out of fuel!");
                System.out.print("\r    Press Enter to continue: ");
            }
        }
        // Event activities, enclosed in if else construct.
        if (AssetsVars.activity == Activities.LAUNCH) {
            state = AssetsVars.activity;
            // The math and the physics
            AssetsVars.totalAcc = (AssetsVars.throttle / 100 * AssetsVars.ThrustMAX) / AssetsVars.M0 - AssetsVars.accG;
            AssetsVars.rktVt += AssetsVars.totalAcc;
            AssetsVars.altitude += (int) (AssetsVars.rktVt);
            yTow += (int) AssetsVars.rktVt;
            yPad += (int) AssetsVars.rktVt;
            ySky += (int) AssetsVars.rktVt;
            switch (AssetsVars.stageDumped) {
                case 1:
                    if (yRktS1 <= 768) {
                        yRktS1 -= 2 * (-9.8);
                    } else {
                        AssetsImg.rktS1 = null;
                    }
                    break;
                case 2:
                    if (yRktS2 <= 768) {
                        yRktS1 -= 2 * (-9.8);
                        yRktS2 -= 2 * (-9.8);
                    } else {
                        AssetsImg.rktS2 = null;
                        AssetsImg.rktS1 = null;
                    }
                    break;
                case 3:
                    if (yRktS3 <= 768) {
                        yRktS1 -= 2 * (-9.8);
                        yRktS2 -= 2 * (-9.8);
                        yRktS3 -= 2 * (-9.8);
                    } else {
                        AssetsImg.rktS3 = null;
                        AssetsImg.rktS2 = null;
                        AssetsImg.rktS1 = null;
                    }
                    break;
            }
            if (AssetsVars.altitude >= 10000) {
                // Following executed to stop above graphics and render low earth orbit beyondKarman
                AssetsVars.activity = Activities.BEYOND_KARMAN;
                AssetsVars.throttle = 0;
                // Writes mission log
                Console.write("\rLAUNCHED SUCCESSFULLY");
                Console.write("\r> Entering orbit.....");
            }
        } else if (AssetsVars.activity == Activities.RELEASE_PAYLOAD) {
            // Side activities so this won't have state backup
            yCovUp -= 1;
            yCovDown += 1;
            if (yCovUp <= 0) {
                xPayload -= 1;
                fx -= 1;
            }
            if (xPayload == -447) {
                Console.write("\rDONE: Module ejected");
                AssetsVars.activity = state;
            }
        } else if (AssetsVars.activity == Activities.BEYOND_KARMAN) {
            // FOR MOON
            state = AssetsVars.activity;
            /** <todo val="Relation of x and y with time"> */
            xMoon = (int) (CNVS_WIDTH / 2 - 37 + 250 * Math.cos(2.663 * Math.pow(10, -6.0) * AssetsVars.upTime));      // Parameter as wt (omega * time) for moon
            yMoon = (int) (CNVS_HEIGHT / 2 - 37 + 250 * Math.sin(2.663 * Math.pow(10, -6.0) * AssetsVars.upTime));     // Parameter as wt (omega * time) for moon
            /** </todo> */
            // for rocket
            distOrbiter = (Math.sqrt(Math.pow((xOrbiter - CNVS_WIDTH / 2), 2) + Math.pow((yOrbiter - CNVS_HEIGHT / 2), 2)));        // Radius of revolution, using dist formula
            // wOrbiter = Math.sqrt(AssetsVars.G * AssetsVars.Mt / Math.pow(distOrbiter, 3));                                         // omega of rocket
            wOrbiter = Math.PI;
            xOrbiter = (int) (hOrbiter + (aOrbiter / 2) * Math.cos(wOrbiter * tOrbiter));                               // Parameter as wt (omega * time) for rocket
            yOrbiter = (int) (kOrbiter + (bOrbiter / 2) * Math.sin(wOrbiter * tOrbiter));                               // Parameter as wt (omega * time) for rocket
            tOrbiter += 0.0004;
            distOrbiter = Math.sqrt(AssetsVars.throttle / Math.pow(Math.PI, 2));
            // Orbit raising code: FIX these two lines:
            aOrbiter += (int) Math.abs(2 * distOrbiter * Math.cos(wOrbiter * tOrbiter));
            bOrbiter += (int) Math.abs(2 * distOrbiter * Math.sin(wOrbiter * tOrbiter));
        } else if (AssetsVars.activity == Activities.LUNAR_ENTRY) {
            state = AssetsVars.activity;
            yEntry = (int) (9.8 / 6 * Math.pow(xEntry, 2) / 2 / Math.pow(entryVx, 2));
            entryH -= yEntry;
            entryR = entryVx * Math.sqrt(2 * entryH * 6 / 9.8);
            xEntry++;
            if (entryH <= 500) {
                AssetsVars.activity = Activities.POST_LUNAR_ENTRY;
            }
        } else if (AssetsVars.activity == Activities.POST_LUNAR_ENTRY) {
            state = AssetsVars.activity;
            yLander += Math.sqrt(2 * (9.8 / 6 - AssetsVars.throttle) * entryH);
            if (entryH <= 100) {
                AssetsVars.activity = Activities.NEAR_LUNAR_SURFACE;
            }
        } else if (AssetsVars.activity == Activities.NEAR_LUNAR_SURFACE) {
            state = AssetsVars.activity;
            yLun += Math.sqrt(2 * (9.8 / 6 - AssetsVars.throttle) * entryH);
            if (entryH <= 0) {
                if (Math.sqrt(2 * (9.8 / 6 - AssetsVars.throttle) * entryH) < 15) {
                    AssetsVars.activity = Activities.MISSION_SUCCESFULL;
                } else {
                    AssetsVars.activity = Activities.MISSION_FAILED;
                }
            }
        } else if (AssetsVars.activity == Activities.MISSION_SUCCESFULL) {
        } else if (AssetsVars.activity == Activities.MISSION_FAILED) {
        }
    }

    // Method is responsible for drawing on Canvas cnvs_space.
    protected void render() {
        Environment env = null;
        if (AssetsVars.activity == Activities.BELOW_KARMAN || AssetsVars.activity == Activities.LAUNCH) {
            if (ySky >= -1 || AssetsImg.skyGrad == null) {
                // env.flush();
                env = Environments.stars(ui.getCanvas(), 0, 0);
            } else {
                // env.flush();
                env = Environments.skyGrad(ui.getCanvas(), 0, ySky);
            }
            if (AssetsVars.throttle > 0) {
		        switch (AssetsVars.stageDumped) {
		        case 0:
			        env.addEntity(Entities.rktBurn(xRktLaunch, yRktBurn));
			        break;
		        case 1:
			        env.addEntity(Entities.rktFlame(xFlame, AssetsVars.yFlame));
			        break;
		        case 2:
			        env.addEntity(Entities.rktFlame(xFlame, AssetsVars.yFlame));
			        break;
		        case 3:
			        env.addEntity(Entities.rktFlame(xFlame, AssetsVars.yFlame));
			        break;
		        }
            }
            if (AssetsImg.rktCone != null) {
                env.addEntity(Entities.rktCone(xRktLaunch, yRktCone));
            }
            if (AssetsImg.rktS3 != null) {
                env.addEntity(Entities.rktS3(xRktLaunch, yRktS3));
            }
            if (AssetsImg.rktS2 != null) {
                env.addEntity(Entities.rktS2(xRktLaunch, yRktS2));
            }
            if (AssetsImg.rktS1 != null) {
                env.addEntity(Entities.rktS1(xRktLaunch, yRktS1));
            }
            if (AssetsImg.lPad != null) {
                env.addEntity(Entities.lPad(0, yPad));
            }
            if (AssetsImg.tower != null) {
                env.addEntity(Entities.tower((int) (4.6 * 64), yTow));
            }
            if (ySky >= 768) {
                Entities.m_lPad.flush();
                Entities.m_tower.flush();
                Environments.m_skyGrad.flush();
            }
        } else if (AssetsVars.activity == Activities.RELEASE_PAYLOAD) {
            // env.flush();
            env = Environments.stars(ui.getCanvas(), 0, 0);
            env.addEntity(Entities.payLoad(192 - 32, 160 + 64));
            env.addEntity(Entities.rkt(xPayload, 160));
            env.addEntity(Entities.rktCovUp(256 + fx, yCovUp));
            env.addEntity(Entities.rktCovDown(256 + fx, yCovDown));
            if (xPayload == -447) {
                Entities.m_rkt.flush();
                Entities.m_rktCovUp.flush();
                Entities.m_rktCovDown.flush();
            }
        } else if (AssetsVars.activity == Activities.BEYOND_KARMAN) {
            // env.flush();
            env = Environments.stars(ui.getCanvas(), 0, 0);
            env.getGfx().setColor(Color.darkGray);
            env.getGfx().drawArc(hOrbiter - aOrbiter / 2, kOrbiter - bOrbiter / 2, aOrbiter, bOrbiter, 0, 360);      // orbit of rocket
            env.getGfx().drawArc(CNVS_WIDTH / 2 - 250, CNVS_HEIGHT / 2 - 250, 500, 500, 0, 360);                     // draw orbit of moon
            env.addEntity(Entities.moon(xMoon, yMoon));                                              // moon with revolution considered
            env.addEntity(Entities.rktDef(xOrbiter, yOrbiter));                                      // our rocket
            env.addEntity(Entities.earth(CNVS_WIDTH / 2 - 8, CNVS_HEIGHT / 2 - 8));                  // earth
        } else if (AssetsVars.activity == Activities.LUNAR_ENTRY) {
            // env.flush();
            env = Environments.stars(ui.getCanvas(), 0, 0);
            env.getGfx().drawArc(0, 0, (int) entryR, (int) entryH, 0, 90);
            env.addEntity(Entities.rktDef(xEntry, yEntry));
            env.getGfx().drawImage(AssetsImg.lunSurface, 0, CNVS_HEIGHT - 64, ui);
        } else if (AssetsVars.activity == Activities.POST_LUNAR_ENTRY) {
            // env.flush();
            env = Environments.stars(ui.getCanvas(), 0, 0);
        } else if (AssetsVars.activity == Activities.NEAR_LUNAR_SURFACE) {
            // env.flush();
            env = Environments.stars(ui.getCanvas(), 0, 0);
        } else if (AssetsVars.activity == Activities.MISSION_SUCCESFULL) {
            // env.flush();
            env = Environments.Success(ui.getCanvas(), 0, 0);
        } else if (AssetsVars.activity == Activities.MISSION_FAILED) {
            // env.flush();
            env = Environments.Failure(ui.getCanvas(), 0, 0);
        }
        if (env != null)
            env.render();
    }

    protected void initiate() {
        if (!Sim.noGUI) {
            /* Calling instance of GUI to create a window to
             * display the GUI components.
             */
            ui = new GUI(title, width, height);
            // Call initiate() from class Entities.
            Entities.initiate();
            Environments.initiate();
        }
        Console.write("\rEnvironments online...");
        // Get the canvas width and height.
        CNVS_WIDTH = width;
        CNVS_HEIGHT = height;
        hOrbiter = CNVS_WIDTH / 2;
        kOrbiter = CNVS_HEIGHT / 2;
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
            if (deltaFrames >= 1 && !Sim.noGUI) {
                // Calls render() to draw to screen.
                render();
                deltaFrames--;
            }
        }
        // Calls new Sim.stop() to close thread.
        stop();
    }

    // Method responsible for starting thread.
    public synchronized void start() {
        /* Prevents errors by not starting thread if
         * it's already running.
         */
        if (running) {
            return;
        }
        // Sets a flag variable true to denote thread is running.
        running = true;
        // Defines new Thread object
        th = new Thread(this);
        /* Start Thread th, following method exists in a
         * library class, NOT to be confused with mthod
         * public synchronized void start().
         */
        th.start();
    }

    // Method responsible for stopping thread
    public synchronized void stop() {
        /* Prevents errors by not closing thread if
         * it's already not running
         */
        if (!running) {
            return;
        }
        // Sets a flag variable false to denote thread is not running
        running = false;
        /* Safely closes the thread
         * NOTE that stop method is deprecated
         * NOTE join() method throws InterruptedException
         */
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
