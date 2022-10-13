public class AssetsVars {
    // running flag
    protected static boolean running = true;
    //boolean markers
    protected static byte activity = 0;
    //Online engine
    protected static byte engON = 0;
    //Throttle in percentage: range: -100% to 100%
    protected static int throttle = 0;
    //Fuel qty inr %
    protected static double fuel = 100.0;
    //Marker for stage drop 0, 1, 2, 3, etc.
    protected static byte stageDumped = 0;
    //yFlame value for stage drops
    protected static int yFlame = 514 - 40;
    //frames, updates and observation clock
    protected static int FPS = 60;                         //frames per second
    protected static int UPS = 60;                         //updates per second
    protected static double upTime = 1.0;
    //Time warp factor
    protected static int warpF = 1;

    //Variables for Rocket equation
    protected static final double G_MEarth = 6.6726 * Math.exp(-11) * 5.972 * Math.exp(24);  // G * Mass(earth)
    protected static final int radiusEarth = 6371000;     //radius of earth in m, 6.3 million m
    protected static double accG = 9.8;                   //acceleration due to gravity in N/kg, init 9.8 N/kg
    protected static int Isp = 300;                       //Specific impulse in sec
    protected static final int ThrustMAX = 34000000;      //max thrust 34 million N
    protected static double dM = 0.0;                     //mass flow rate in kg/sec
    protected static final int Mdry = 1300000;            //mass dry in kg, 1.3 million kg
    protected static final int Mfuel0 = 1000000;          //mass of fuel in kg, init 1 million kg
    protected static double Mfuel_t = Mfuel0;             //mass of fuel at time t in kg
    protected static final int M0 = Mdry + Mfuel0;        //mass initial in kg, 2.3 million kg
    protected static double Mt = M0;                      //mass at time t
    protected static double scaleF = 0.07f;               //Scale factor, experimental
    protected static double totalAcc;                     //Acceeration at time t
    protected static double rktVt;                        //Velocity at time t

    protected static double altitude = 0.0;               //altitude

    static String command;                                //control command
}