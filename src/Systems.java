

public class Systems
{
    protected static boolean init = false;
    protected static System m_stars;           // Stars
    protected static System m_skyGrad;         // Sky
    protected static System m_Success;
    protected static System m_Failure;

    public static void intialise()
    {
        if (init) return;
        else init = true;
        AssetsImg.initiate();
        m_stars = null;
        m_skyGrad = null;
        m_Success = null;
        m_Failure = null;
    }

    public static System stars(int x, int y)
    {
        if (m_stars == null) return m_stars = new System(x, y, AssetsImg.stars);
        else {
            m_stars.x = x;
            m_stars.y = y;
            return m_stars;
        }
    }

    public static System skyGrad(int x, int y)
    {
        if (m_skyGrad == null) return m_skyGrad = new System(x, y, AssetsImg.skyGrad);
        else {
            m_skyGrad.x = x;
            m_skyGrad.y = y;
            return m_skyGrad;
        }
    }

    public static System Success(int x, int y)
    {
        if (m_Success == null) {
            m_Success = new System(x, y, AssetsImg.Success);
            return m_Success;
        } else {
            m_Success.x = x;
            m_Success.y = y;
            return m_Success;
        }
    }

    public static System Failure(int x, int y)
    {
        if (m_Failure == null) {
            m_Failure = new System(x, y, AssetsImg.Failure);
            return m_Failure;
        } else {
            m_Failure.x = x;
            m_Failure.y = y;
            return m_Failure;
        }
    }
}
