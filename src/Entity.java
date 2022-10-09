import java.awt.image.BufferedImage;
import java.awt.Graphics;

public class Entity
{
    public int x, y;
    public int xBound, yBound;
    protected BufferedImage entityImg;
    public Entity(int x, int y, BufferedImage img)
    {
        this.x = x;
        this.y = y;
        if (img != null) this.entityImg = img;
        else throw new NullPointerException("Entity: image is null");
        this.xBound = img.getWidth();
        this.yBound = img.getHeight();
    }
    public void render(Graphics gfx)
    {
        gfx.drawImage(this.entityImg, this.x, this.y);
    }
    public void flush()
    {
        this.entityImg.flush();
        this.entityImg = null;
    }
}
