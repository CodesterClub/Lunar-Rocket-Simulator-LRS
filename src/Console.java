import java.io.BufferedReader;
import java.util.StringTokenizer;

/*
 * Entire console will be CASE-INSENSITIVE
 *
 * Commands
 * 1.  SELECT [ENGINE NO]
 * 2.  THRUST [% VALUE without % sign]
 * 3.  DROP STAGE [STAGE NO]
 * 4.  TIME WARP [WARP factor]
 * 5.  STATUS
 */

public class Console implements Runnable {
    
    protected Thread th;
    int tmp = 0;
    protected boolean running = false;
    
    private boolean isOn_engS1 = false;
    private boolean isOn_engS2 = false;
    private boolean isOn_engS3 = false;
    
    protected String readCommand () {
        BufferedReader stdin = new BufferedReader (
                new java.io.InputStreamReader (System.in));
        try {
            System.out.print ("\nLRS shell> ");                                   // prompt
            AssetsVars.command = stdin.readLine ();                             // store command in variable, kinda uselss
            return AssetsVars.command;                                          // return that command, also useless
        } catch (java.io.IOException ex) {
            System.err.println (ex.toString());
        }
        return null;
    }

    private void evaluate (String command) throws Exception {
        StringTokenizer sT = new StringTokenizer (command, " \n");              // delimiters
        String cmd = sT.nextToken();
        if (cmd.equalsIgnoreCase ("SELECT")) {
            AssetsVars.engON = Byte.parseByte (sT.nextToken ());
            switch(AssetsVars.engON) {
                case 1:
                    isOn_engS1 = true;
                    break;
                case 2:
                    isOn_engS2 = true;
                    break;
                case 3:
                    isOn_engS3 = true;
                    break;
                default:
                    System.err.println ("[E] unknown error\n");
            }
        }
        else if (cmd.equalsIgnoreCase ("THRUST")) {
            inputThrust(Integer.parseInt (sT.nextToken ()));
        }
        else if (cmd.equalsIgnoreCase ("DROP")) {
            byte stageNo = (byte) Integer.parseInt(sT.nextToken());
            switch(stageNo) {
                case 1:
                    dumpStg1ActionPerformed();
                    break;
                case 2:
                    dumpStg2ActionPerformed();
                    break;
                case 3:
                    ejectPayloadActionPerformed();
                    break;
                default:
                    System.err.println ("[E] unknown error\n");
            }
        }
        else if (cmd.equalsIgnoreCase ("TIME")) {
            if (sT.nextToken ().equalsIgnoreCase ("WARP")) {
                AssetsVars.warpF = Integer.parseInt (sT.nextToken ());
            }
            else throw new Exception ("incomplete command: expected WARP after TIME");
        }
        else if (cmd.equalsIgnoreCase ("STATUS")) {
            System.out.println ("\nFUEL:       " + " %");
            System.out.println ("THRUST:     " + AssetsVars.thrust + "%");
            System.out.println ("ENGINE:     " + AssetsVars.engON + " ON");
            System.out.println ("TIME WARP:  " + AssetsVars.warpF + "x");
            System.out.println ("LAST STAGE: " + (int)(AssetsVars.stageDumped + 1) + " \n");
            
            System.out.println ("VELOCITY:   " + AssetsVars.rktVt + "m/s");
            System.out.println ("DIST from closest body: " + AssetsVars.altitude + "m\n");
        }
        else if (cmd.equalsIgnoreCase ("HELP")) {
            System.out.println ("Commands\n" +
                                " 1.  SELECT [ENGINE NO]\n" +
                                " 2.  THRUST [% VALUE without % sign]\n" +
                                " 3.  DROP STAGE [STAGE NO]\n" +
                                " 4.  WARP TIME [WARP factor]" +
                                " 5.  STATUS");
        }
        else {
            System.err.println ("[E] no such command: " + command);
            System.err.println ("Enter HELP for commands list and syntax");
        }
    }
    
    /**
     * Abstract method: Runs the code required to be run on thread
     */
    @Override
    public void run () {
        while (running) {
            if (!readCommand ().equalsIgnoreCase ("")) {
                try {
                    evaluate(AssetsVars.command);                                    // evaluates the command
                } catch (Exception e) {
                    System.err.println("[E] " + e);
                    System.err.println("User input error. Please try again");
                }
            }
        }
        //Calls new Sim.stop () to close thread.
        stop ();
    }

    //Method responsible for starting thread.
    public synchronized void start () {
        /*Prevents errors by not starting thread if
         *it's already running.
         */
        if (running) {
            return;
        }
        // sets runnig flag true
        running = true;
        //Defines new Thread object
        th = new Thread (this);
        /*Start Thread th, following method exists in a
         *library class, NOT to be confused with mthod
         *public synchronized void start ().
         */
        th.start ();
    }

    //Method responsible for stopping thread
    public synchronized void stop () {
        /*Prevents errors by not closing thread if
         *it's already not running
         */
        if (!running) {
            return;
        }
        //Sets a flag variable false to denote thread is not running
        running = false;
        /*Safely closes the thread
         *NOTE that stop method is deprecated
         *NOTE join () method always throws InterruptedException
         */
        try {
            th.join ();
        } catch (InterruptedException e) {
            System.err.print (e.toString ());
            System.exit (0);
        }
    }
    
    //Event actions
    private void inputThrust(int thrust) {
        if ((AssetsVars.engON == 1 && AssetsVars.stageDumped == 0) || (AssetsVars.engON == 2 && AssetsVars.stageDumped == 1) || (AssetsVars.engON == 3 && AssetsVars.stageDumped == 2)) {
            if (!(AssetsVars.activity >= Activities.LAUNCH)) {
                try {
                    AssetsVars.thrust = thrust;
                } catch (Exception ex) {
                    System.out.println("[E] INVALID ENTRY FOR THRUST");
                }
                if (AssetsVars.thrust < 0) {
                    System.out.println("[E] NO ENGINE FOR ANTI THRUST");
                } else if (AssetsVars.thrust < 10) {
                    System.out.println("> Analyzing thrust...");
                    System.out.println("[W] LOW THRUST");
                } else if (AssetsVars.thrust >= 101) {
                    System.out.println("> Analyzing thrust...");
                    System.out.println("[E] INVALID COMMAND FOR: ");
                    System.out.println("THRUST BEYOND 100");
                } else if (AssetsVars.thrust >= 51) {
                    AssetsVars.activity = Activities.LAUNCH;
                    System.out.println("> Analyzing thrust...");
                    System.out.println("[W] THRUST TOO HIGH\n[W] CREW WON'T SURVIVE");
                } else {
                    AssetsVars.activity = Activities.LAUNCH;
                    System.out.println("> Analyzing thrust...");
                    System.out.println("THRUST OPTIMUM");
                    System.out.println("Rocket launched thrusting at: " + AssetsVars.thrust);
                }
            } else {
                tmp = AssetsVars.thrust;
                AssetsVars.thrust = thrust;
                if (AssetsVars.thrust < 0) {
                    AssetsVars.thrust = tmp;
                    System.out.println("[E] INVALID ENTRY FOR THRUST");
                } else if (AssetsVars.thrust >= 101) {
                    AssetsVars.thrust = tmp;
                    System.out.println("> Analyzing thrust...");
                    System.out.println("[E] INVALID COMMAND FOR: ");
                    System.out.println("THRUST BEYOND 100");
                } else {
                    System.out.println("STAGE " + (AssetsVars.stageDumped + 1) + ": Thrust entered: " + AssetsVars.thrust);
                }
            }
        } else {
            System.out.println("Stage " + (AssetsVars.stageDumped + 1) + " Engine offline");
        }
    }
    
    private void dumpStg1ActionPerformed() {
        if (AssetsVars.stageDumped == 0) {
            AssetsVars.stageDumped = 1;
            isOn_engS1 = false;
            System.out.println("Dumped stage 1");
            AssetsVars.thrust = 0;
            AssetsVars.yFlame -= 128;
        } else {
            System.out.println("[E] STAGE DOESN'T EXIST");
        }
    }

    private void dumpStg2ActionPerformed() {
        if (AssetsVars.stageDumped == 0 || AssetsVars.stageDumped == 1) {
            isOn_engS1 = false;
            isOn_engS2 = false;
            System.out.println("Dumped stage 2");
            AssetsVars.thrust = 0;
            AssetsVars.yFlame -= 128;
            if (AssetsVars.stageDumped == 0) {
                AssetsVars.yFlame -= 128;
            }
            AssetsVars.stageDumped = 2;
        } else {
            System.out.println("[E] STAGE DOESN'T EXIST");
        }
    }

    private void ejectPayloadActionPerformed() {
        if (AssetsVars.activity >= Activities.BEYOND_KARMAN) {
            if (AssetsVars.stageDumped == 0 || AssetsVars.stageDumped == 1 || AssetsVars.stageDumped == 2) {
                isOn_engS1 = false;
                isOn_engS2 = false;
                isOn_engS3 = false;
                System.out.println("Dumped stage 3");
                System.out.println("Ejecting Module....");
                AssetsVars.thrust = 0;
                AssetsVars.activity = Activities.RELEASE_PAYLOAD;
                if (AssetsVars.stageDumped == 0) {
                    AssetsVars.yFlame -= 128;
                    AssetsVars.stageDumped = 1;
                }
                if (AssetsVars.stageDumped == 1) {
                    AssetsVars.yFlame -= 128;
                    AssetsVars.stageDumped = 2;
                }
                if (AssetsVars.stageDumped == 2) {
                    AssetsVars.yFlame -= 128;
                }
                AssetsVars.stageDumped = 3;
            } else {
                System.out.println("[E] COMMAND INVALID");
            }
        } else {
            System.out.println("[E] CAN'T REMOVE FAIRINGS");
        }
    }

    private void warpTimeActionPerformed(int warpF) {
        AssetsVars.warpF = warpF;
    }
}
