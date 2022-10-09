import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.util.List;
import java.util.ArrayList;

public class Entity
{
    public int x, y;
    public int xBound, yBound;
    protected BufferedImage entityImg;
    protected List<Entity> attatchments;

    protected System system;
    protected Entity parent;

    /**
     * A soft entity cannot interact with any other entity.
     * Comes into play during collision detection.
     */
    protected boolean isSoft;

    /**
     * An entity is an existence, visualised by its image attribute.
     * @param BufferedImage The attribute image
     * @param int x coordinate entity
     * @param int y coordinate entity
     */
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
    /**
     * Updates posn of an entity along with its attatchments.
     * @param int delta-x
     * @param int delta-y
     */
    public void update(int dx, int dy)
    {
        this.x += dx;
        this.y += dy;
        for (Entity e : this.attatchments) {
            e.x += dx;
            e.y += dy;
        }
    }
    /**
     * Moves an entity to absolute posn along with its attatchments.
     * @param int new-x
     * @param int new-y
     */
    public void move(int x, int y)
    {
        int dx = x - this.x;
        int dy = y - this.y;
        this.update(dx, dy);
    }
    /**
     * Draw the entity along with its attatchments.
     * @param Graphics the graphics object used for drawing
     */
    public void render(Graphics gfx)
    {
        gfx.drawImage(this.entityImg, this.x, this.y, null);
        for (Entity e : this.attatchments)
            gfx.drawImage(e.entityImg, e.x, e.y, null);
    }
    /**
     * Attatch multiple entities to create a larger compound.
     * Do NOTE that newly attatched entities get drawn over older ones.
     * Also, the parent entity is drawn before its attatchments.
     */
    public void attatch(Entity e)
    {
        e.parent = this;
        if (this.attatchments == null)
            this.attatchments = new ArrayList<Entity>();
        this.attatchments.add(e);
    }
    /**
     * Flush entity resources along with its attatchments
     */
    public void flush()
    {
        this.parent.attatchments.remove(this);
        this.system.attatchments.remove(this);
        this.entityImg.flush();
        this.entityImg = null;
        if (this.attatchments != null)
            for (Entity e : this.attatchments)
                e.flush();
        this.attatchments = null;
    }
}
