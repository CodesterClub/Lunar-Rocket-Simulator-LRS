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
    public Entity(BufferedImage img, int x, int y)
    {
        this.isSoft = false;
        this.x = x;
        this.y = y;
        if (img != null) this.entityImg = img;
        else throw new NullPointerException("Entity: image is null");
        this.xBound = img.getWidth();
        this.yBound = img.getHeight();
        this.attatchments = null;
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
    public void move(int x, int y)
    {
        int dx = x - this.x;
        int dy = y - this.y;
        this.update(dx, dy);
    }
    /** Draw this and its attatchments */
    public void render(Graphics gfx)
    {
        gfx.drawImage(this.entityImg, this.x, this.y, null);
        for (Entity e : this.attatchments)
            gfx.drawImage(e.entityImg, e.x, e.y, null);
    }
    /**
     * Attatch multiple entities to create a larger compound
     * Do note that newly attatched entities get drawn over older ones
     * Also, the parent entity is drawn before its attatchments
     */
    public void attatch(Entity e)
    {
        if (this.attatchments == null)
            this.attatchments = new ArrayList<Entity>();
        this.attatchments.add(e);
    }
    public void flush()
    {
        this.entityImg.flush();
        this.entityImg = null;
        for (Entity e : this.attatchments)
            e.flush();
    }
}
