
public class AssetsVars {
    //boolean markers
    protected static byte activity = 0;
    //Online engine
    protected static byte engON = 0;
    //Thrust variable: range: -100% to 100%
    protected static int thrust = 0;
    //Fuel qty
    protected static double fuel = 100.0;
    //Marker for stage drop 0, 1, 2, 3, etc.
    protected static byte stageDumped = 0;
    //yFlame value for stage drops
    protected static int yFlame = 514 - 40;
    //observation clock
    protected static int updates = 0;
    protected static double obsTime = 1.0;
    //Time warp factor
    protected static int warpF = 1;
    //Variables for Rocket equation
    protected static double G = 6.6726 * Math.exp(-11);   //Gravitational const
    protected static double dM = 15000;                   //drop in mass in kg
    protected static final int M0 = 3000000;              //mass initial in kg
    protected static final int Mdry = 1000000;            //mass dry in kg
    protected static double Mt = M0;                      //Mass at time t seconds
    protected static float scaleF = 0.07f;                //Scale factor, experimental
    protected static double Isp = 4400;                   //Specific impulse in sec
    protected static double Ve;                           //effective exhaust velocity in m/s
    protected static double rktAcc;                       //Acceeration at t time
    protected static float rktVt;                         //Velocity at t time
    protected static int altitude = 0;                    //altitude
    
    static String command;                                //control command
}
