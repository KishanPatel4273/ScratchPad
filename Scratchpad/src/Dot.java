import java.util.ArrayList;

public class Dot {

	protected int x, y, radius, color;
	private static int width, height;

	public Dot(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public Dot(int x, int y, int radius, int color) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.color = color;
	}

	public String toString() {
		return "[" + (x-500) + "," + ((1000 - y) -500) + "],";
	}
	
	public boolean isEqual(Dot d) {
		return d.x == x && d.y == y;
	}
	
}