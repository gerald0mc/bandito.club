package club.bandito.client.gui;

public class Rect {

    public int x;
    public int y;
    public int width;
    public int height;

    public Rect(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean isInside(int posX, int posY) {
        return posX > x && posY > y && posX < x + width && posY < y + height;
    }
}
