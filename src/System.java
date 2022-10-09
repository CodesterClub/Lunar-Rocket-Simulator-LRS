import java.awt.image.BufferedImage;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.util.List;
import java.util.ArrayList;

public class System
{
    protected BufferedImage bg;
    protected List<Entity> entities;
    protected Graphics gfx;
    protected Canvas cv;
    protected BufferStrategy bs;

    protected int x, y;

    public System(Canvas canvas, BufferedImage bg, int x, int y)
    {
        this.cv = canvas;
        this.bs = canvas.getBufferStrategy();
        if (this.bs == null) {
            canvas.createBufferStrategy(3);
            this.bs = canvas.getBufferStrategy();
        }
        this.gfx = bs.getDrawGraphics();
        Toolkit.getDefaultToolkit().sync();
        this.bg = bg;
        this.x = x;
        this.y = y;
    }
    public void update(int dx, int dy)
    {
        this.x += dx;
        this.y += dy;
    }
    public void update(int dx, int dy, boolean movEntities)
    {
        this.x += dx;
        this.y += dy;
        if (movEntities)
            for (Entity e : this.entites)
                e.update(dx, dy);
    }
    public void render()
    {
        gfx.clearRect(0, 0, this.cv.getWidth(), this.cv.getHeight());
        gfx.drawImage(this.bg, this.x, this.y, null);
        for (Entity e : this.entites)
            e.render(gfx);
    }
    public void flush()
    {
        bg.flush;
    }
}
