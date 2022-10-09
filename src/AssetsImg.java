import java.awt.image.BufferedImage;

public class AssetsImg {
    /*Global BufferedImage object declarations,
     *each object is used to store 1 image.
     */
    protected static BufferedImage rktS1;           //Earth stg 1
    protected static BufferedImage rktS2;           //Earth stg 2
    protected static BufferedImage rktS3;           //Earth stg 3
    protected static BufferedImage rktCone;         //Earth top cone
    protected static BufferedImage rktBurn;         //Earth rocket fire burn
    protected static BufferedImage rktFlame;        //Rocket flame
    protected static BufferedImage tower;           //Launch tower
    protected static BufferedImage lPad;            //Launch pad

    protected static BufferedImage rktDef;          //Dot representing pos of rocket

    protected static BufferedImage earth;
    protected static BufferedImage moon;

    protected static BufferedImage stars;           //Stars
    protected static BufferedImage skyGrad;         //Sky

    protected static BufferedImage rkt;             //Rocket body while payload ejection
    protected static BufferedImage rktCovUp;        //Payload Upper Cover
    protected static BufferedImage rktCovDown;      //Payload Lower Cover
    protected static BufferedImage payLoad;         //Payload ie Module and lander
    protected static BufferedImage landerBrkOff;    //lander breaks off module
    protected static BufferedImage moduleRsld;      //Module released of lander and left behind
    protected static BufferedImage landerRsldF;     //lander released, eng off
    protected static BufferedImage landerRsldB;     //lander released, eng burn
    protected static BufferedImage lander64F;       //lander lands seen from dist, eng off
    protected static BufferedImage lander64B;       //lander lands seen from dist, eng burn
    protected static BufferedImage lander128F;      //lander lands seen from close, eng off
    protected static BufferedImage lander128B;      //lander lands seen from close, eng burn
    protected static BufferedImage lunSurface;

    protected static BufferedImage Success;
    protected static BufferedImage Failure;

    //Global object declaration of SpriteSheet class.
    private static SpriteSheet lander0x0, launchPad12x2, Launchtower1x7, module4x3, payloadRsl0x0, planets4x4, points, releases4x3, rockets1x7;

    //Global variable declaratons.
    private static final int WIDTH=64, HEIGHT=64;

    public static void initiate(){
        //Define SpriteSheet object.
        launchPad12x2 = new SpriteSheet(ImageLoader.loadImage("res/launchPad12x2.png"));
        Launchtower1x7 = new SpriteSheet(ImageLoader.loadImage("res/Launchtower1x7.png"));
        payloadRsl0x0 = new SpriteSheet(ImageLoader.loadImage("res/payloadRsl0x0.png"));
        points = new SpriteSheet(ImageLoader.loadImage("res/points.png"));
        releases4x3 = new SpriteSheet(ImageLoader.loadImage("res/releases4x3.png"));
        rockets1x7 = new SpriteSheet(ImageLoader.loadImage("res/rockets1x7.png"));

        //Assign images to BufferedImage objects.
        rktCone=rockets1x7.Crop(0*WIDTH, 0*HEIGHT, 1*WIDTH, 1*HEIGHT);
        rktS3=rockets1x7.Crop(0*WIDTH, 1*HEIGHT, 1*WIDTH, 2*HEIGHT);
        rktS2=rockets1x7.Crop(0*WIDTH, 3*HEIGHT, 1*WIDTH, 2*HEIGHT);
        rktS1=rockets1x7.Crop(0*WIDTH, 5*HEIGHT, 1*WIDTH, 2*HEIGHT);
        rktBurn=rockets1x7.Crop(0*WIDTH, 7*HEIGHT, 1*WIDTH, 2*HEIGHT);
        rktFlame=new SpriteSheet(ImageLoader.loadImage("res/flame.png")).Crop(0 * WIDTH, 0 * HEIGHT, 32, 95);

        tower=Launchtower1x7.Crop(0*WIDTH, 0*HEIGHT, 1*WIDTH, 7*HEIGHT);

        lPad=launchPad12x2.Crop(0*WIDTH, 0*HEIGHT, 12*WIDTH, 2*HEIGHT);

        rktDef=points.Crop(0, 0, 4, 4);
        earth=points.Crop(5, 0, 16, 16);
        moon=points.Crop(22, 0, 114, 114);

        stars=ImageLoader.loadImage("res/stars.png");
        skyGrad=ImageLoader.loadImage("res/skyGrad.png");

        rkt=payloadRsl0x0.Crop(0*WIDTH, 0*HEIGHT, 7*WIDTH, 5*HEIGHT);
        rktCovUp=payloadRsl0x0.Crop(7*WIDTH, 0*HEIGHT, 7*WIDTH, 2*HEIGHT);
        rktCovDown=payloadRsl0x0.Crop(7*WIDTH, 3*HEIGHT, 7*WIDTH, 2*HEIGHT);

        payLoad=releases4x3.Crop(0*WIDTH, 0*HEIGHT, 7*WIDTH, 3*HEIGHT);
        landerBrkOff=releases4x3.Crop(1*WIDTH, 3*HEIGHT, 4*WIDTH, 3*HEIGHT);
        moduleRsld=releases4x3.Crop(1*WIDTH, 6*HEIGHT, 4*WIDTH, 3*HEIGHT);

        /*landerRsldF=lander0x0.Crop(1*WIDTH, 7*HEIGHT, 2*WIDTH, 2*HEIGHT);
        landerRsldB=lander0x0.Crop(1*WIDTH, 1*HEIGHT, 2*WIDTH, 2*HEIGHT);
        lander64F=lander0x0.Crop(0*WIDTH, 5*HEIGHT, 1*WIDTH, 1*HEIGHT);
        lander64B=lander0x0.Crop(0*WIDTH, 7*HEIGHT, 1*WIDTH, 1*HEIGHT);
        lander128F=lander0x0.Crop(1*WIDTH, 3*HEIGHT, 2*WIDTH, 2*HEIGHT);
        lander128B=lander0x0.Crop(1*WIDTH, 5*HEIGHT, 2*WIDTH, 2*HEIGHT);*/
    }
}
