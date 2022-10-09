import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.util.List;
import java.util.ArrayList;

public class Entity
{
    public int x, y;
    public int xBound, yBound;
    boolean isSoft; // a soft entity cannot interact with any other entity
    protected BufferedImage entityImg;
    protected List<Entity> attatchments;
    public Entity(int x, int y, BufferedImage img)
    {
        this.isSoft = false;
        this.x = x;
        this.y = y;
        if (img != null) this.entityImg = img;
        else throw new NullPointerException("Entity: image is null");
        this.xBound = img.getWidth();
        this.yBound = img.getHeight();
        this.attatchments = new ArrayList<Entity>();
    }
    public void update(int dx, int dy)
    {
        this.x += dx;
        this.y += dy;
        for (Entity e : this.attatchments) {
            e.x += dx;
            e.y += dy;
        }
    }
    public void render(Graphics gfx)
    {
        gfx.drawImage(this.entityImg, this.x, this.y, null);
    }
    public void attatch(Entity e)
    {
        this.attatchments.add(e);
    }
    public void flush()
    {
        this.entityImg.flush();
        this.entityImg = null;
    }
}
