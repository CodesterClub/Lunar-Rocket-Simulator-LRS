import java.awt.image.BufferedImage;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.util.List;
import java.util.ArrayList;

public class System
{
    protected int width, height;
    protected BufferedImage bg;
    protected BufferStrategy bs;

    protected int x, y;
    protected List<Entity> entities;

    /**
     * A system is a collection of many entites
     * @param Canvas The canvas where the system is drawn
     * @param BufferedImage The background image
     * @param int x coordinate of system bg
     * @param int y coordinate of system bg
     */
    public System(Canvas canvas, BufferedImage bg, int x, int y)
    {
        this.bg = bg;
        this.x = x;
        this.y = y;
        this.width = canvas.getWidth();
        this.height = canvas.getHeight();
        this.bs = canvas.getBufferStrategy();
        if (this.bs == null) {
            canvas.createBufferStrategy(3);
            this.bs = canvas.getBufferStrategy();
        }
        this.entities = null;
    }
    /**
     * add an entity to the system
     * @param Entity
     */
    public void addEntity(Entity e)
    {
        if (this.entities == null)
            this.entities = new ArrayList<Entity>();
        this.entities.add(e);
    }
    /**
     * update the system, keeps entities static
     * useful for motion in POV of entities
     * @param int delta-x
     * @param int delta-y
     */
    public void update(int dx, int dy)
    {
        this.x += dx;
        this.y += dy;
    }
    /**
     * update the system with entites
     * useful for motion in POV of player
     * @param int delta-x
     * @param int delta-y
     * @param boolean movEntities
     */
    public void update(int dx, int dy, boolean movEntities)
    {
        this.x += dx;
        this.y += dy;
        if (movEntities)
            for (Entity e : this.entites)
                e.update(dx, dy);
    }
    /**
     * update the system by absolute position
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
     * update the system with enties by absolute position
     * @param int new-x
     * @param int new-y
     * @param boolean movEntities
     */
    public void move(int x, int y, boolean movEntities)
    {
        int dx = x - this.x;
        int dy = y - this.y;
        this.update(dx, dy, true);
    }
    /**
     * render system with the entities
     */
    public void render()
    {
        Graphics gfx = this.bs.getDrawGraphics();
        gfx.clearRect(0, 0, this.width, this.height);
        gfx.drawImage(this.bg, this.x, this.y, null);
        for (Entity e : this.entites)
            e.render(gfx);
        this.bs.show();
        Toolkit.getDefaultToolkit().sync();
        gfx.dispose();
    }
    /**
     * flush system with bg and all resources of its entities
     */
    public void flush()
    {
        this.bg.flush();
        for (Entity e : this.entities)
            e.flush();
    }
}
