import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Screen {

	private ArrayList<Dot> points;
	private int width, height;
	private int clickX1, clickY1, clickX2, clickY2;
	private Dot dot;
	private InputHandler input;
	protected int[] pixels;

	public Screen(int width, int height, InputHandler input) {
		this.width = width;
		this.height = height;
		this.input = input;
		pixels = new int[width * height];
		points = new ArrayList<Dot>();
		dot = new Dot(width, height);
	}
	
	int time;
	public void render(boolean[] mode, boolean gridOn, int radius, int color) {
		for (int i = 0; i < width * height; i++) {
			pixels[i] = 16777215;
		}
		handler(mode, gridOn, radius, color);
		plotPoints();
		
		time++;
		if(time > 500 &&input.key[KeyEvent.VK_P]) {
			print();
		}
	}
	
	public int n = 0;	
	public void print() {
		for(int i = 0; i < points.size()-1; i++) {
			for(int j= i+1; j < points.size(); j++) {
				if(points.get(i).isEqual(points.get(j))) {
					points.remove(j);
					j--;
				}
			}
		}
		int i = 0;
		for(Dot d: points) {
			System.out.print(d.toString());
			if(i%20 == 0) {
				System.out.println();
			}
			i++;
		}
	}

	public void handler(boolean[] mode, boolean gridOn, int radius, int color) {
		boolean toggle = false;
		if (mode[0]) {// point
			if (input.pressed == 1) {
				points.add(new Dot(input.mouseX, input.mouseY, radius, color));
				input.pressed = 0;
			}
		}
		if (mode[1]) {// draw
			if (input.pressed == 1) {
				if (!toggle)
					toggle = true;
				else if (toggle)
					toggle = false;
			} else {
				input.pressed = 0;
			}
			if (toggle && input.mouseX != 0 && input.mouseY != 0) {
				points.add(new Dot(input.mouseX, input.mouseY, radius, color));
				input.mouseX = 0;
				input.mouseY = 0;
			}
		}
		if (mode[2]) {// erase
			if (input.pressed == 1) {
				clickX1 = input.mouseX;
				clickY1 = input.mouseY;
				input.pressed++;
			}
			if (clickX1 != 0 || clickY1 != 0) {
				drawFilledRect(clickX1, clickY1, input.mouseX, input.mouseY, 16711680);
			}
			if (input.pressed == 3) {
				clickX2 = input.mouseX;
				clickY2 = input.mouseY;
				input.pressed = 0;
				clear(clickX1, clickY1, clickX2, clickY2);
				clickX1 = 0;
				clickY1 = 0;
				clickX2 = 0;
				clickY2 = 0;
			}
		}
		if (mode[3]) {// clear
			points.clear();
		}
		if (gridOn) {
			grid(Display.gridRow, Display.gridCol);
		}
		if (mode[4]) {// ellipse
			if (input.pressed == 1) {
				clickX1 = input.mouseX;
				clickY1 = input.mouseY;
				input.pressed++;
			}
			if (clickX1 != 0 || clickY1 != 0) {
				drawEllipse(clickX1, clickY1, input.mouseX, input.mouseY, radius, color);
			}
			if (input.pressed == 3) {
				clickX2 = input.mouseX;
				clickY2 = input.mouseY;
				input.pressed = 0;
				plotEllipse(clickX1, clickY1, clickX2, clickY2, radius, color);
				clickX1 = 0;
				clickY1 = 0;
				clickX2 = 0;
				clickY2 = 0;
			}
		}
		if (mode[5]) {// line
			if (input.pressed == 1) {
				clickX1 = input.mouseX;
				clickY1 = input.mouseY;
				input.pressed++;
			}
			if (clickX1 != 0 || clickY1 != 0) {
				drawLine(clickX1, clickY1, input.mouseX, input.mouseY, color);
			}
			if (input.pressed == 3) {
				clickX2 = input.mouseX;
				clickY2 = input.mouseY;
				input.pressed = 0;
				plotLine(clickX1, clickY1, clickX2, clickY2, radius, color);
				clickX1 = 0;
				clickY1 = 0;
				clickX2 = 0;
				clickY2 = 0;
			}
		}
		if (mode[6]) {// rectangle
			if (input.pressed == 1) {
				clickX1 = input.mouseX;
				clickY1 = input.mouseY;
				input.pressed++;
			}
			if (clickX1 != 0 || clickY1 != 0) {
				drawRect(clickX1, clickY1, input.mouseX, input.mouseY, color);
			}
			if (input.pressed == 3) {
				clickX2 = input.mouseX;
				clickY2 = input.mouseY;
				input.pressed = 0;
				plotRect(clickX1, clickY1, clickX2, clickY2, radius, color);
				clickX1 = 0;
				clickY1 = 0;
				clickX2 = 0;
				clickY2 = 0;
			}
		}
	}

	public void grid(int spacingX, int spacingY) {
		int color = 0;
		spacingX = width / ((spacingX / 2) * 2);
		spacingY = height / ((spacingY / 2) * 2);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if ((x != 0 && y != 0) && (x % spacingX == 0 || y % spacingY == 0)) {
					pixels[x + y * width] = color;
				}
				if (x == spacingX * (width / spacingX) / 2) {
					pixels[x + 1 + y * width] = color;
					pixels[x + y * width] = color;
					pixels[x - 1 + y * width] = color;
				}
				if (y == spacingY * (height / spacingY) / 2) {
					pixels[x + (y + 1) * width] = color;
					pixels[x + y * width] = 0;
					pixels[x + (y - 1) * width] = color;
				}
			}
		}
	}

	public void plotPoints() {
		for (Dot p : points) {
			if (p.radius > 1) {
				for (int x = (p.x - p.radius); x < (p.x + p.radius) + 1; x++) {
					drawLine(x, p.y, circle(Math.abs(x - p.x - 1), p.radius) * 2, p.color);
				}
			} else {
				pixels[p.x + p.y * width] = p.color;
			}
		}
	}

	public void plotEllipse(int x1, int y1, int x2, int y2, int radius, int color) {
		for (int x = x1; x < x2; x++) {
			points.add(new Dot(x, ((y2 - y1) / 2) + ellipsePos(x, x1, y1, x2, y2), radius, color));
			points.add(new Dot(x, ((y2 - y1) / 2) + ellipseNeg(x, x1, y1, x2, y2), radius, color));
		}
	}

	public void drawEllipse(int x1, int y1, int x2, int y2, int radius, int color) {
		for (int x = x1; x < x2; x++) {
			pixels[x + (((y2 - y1) / 2) + ellipsePos(x, x1, y1, x2, y2)) * width] = color;
			pixels[x + (((y2 - y1) / 2) + ellipseNeg(x, x1, y1, x2, y2)) * width] = color;
		}
	}

	public int circle(int x, int r) {
		return (int) Math.sqrt(r * r - x * x);
	}

	public int ellipsePos(int x, int x1, int y1, int x2, int y2) {
		double a = (x2 - x1) / 2;
		double b = (y2 - y1) / 2;
		double h = x1 + a;
		double k = y1;// Why is it not y1 - b
		return (int) (k + (b / a) * Math.sqrt(a * a - (x - h) * (x - h)));
	}

	public int ellipseNeg(int x, int x1, int y1, int x2, int y2) {
		double a = (x2 - x1) / 2;
		double b = (y2 - y1) / 2;
		double h = x1 + a;
		double k = y1;// Why is it not y1 - b
		return (int) (k - (b / a) * Math.sqrt(a * a - (x - h) * (x - h)));
	}

	/*
	 * center(x,y)
	 */
	public void drawLine(int x, int y, int length, int color) {
		for (int yy = y - length / 2; yy < y + length / 2; yy++) {
			pixels[x + yy * width] = color;
		}
	}

	public void plotLine(int x1, int y1, int x2, int y2, int radius, int color) {
		if (x1 == x2) {// vertical
			for (int y = y1; y < y2; y++) {
				points.add(new Dot(x1, y, radius, color));
			}
		} else {// horizontal and normal
			for (int x = 0; x < (x2 - x1); x++) {
				double deltY = (y2 - y1);
				double deltX = (x2 - x1);
				points.add(new Dot(x1 + x, (int) (y1 + ((deltY / deltX) * x)), radius, color));
			}
		}
	}

	public void drawLine(int x1, int y1, int x2, int y2, int color) {
		if (x1 == x2) {// vertical
			for (int y = y1; y < y2; y++) {
				pixels[x1 + y * width] = color;
			}
		} else {// horizontal and normal
			for (int x = 0; x < (x2 - x1); x++) {
				double deltY = (y2 - y1);
				double deltX = (x2 - x1);
				pixels[(x1 + x + (int) (y1 + ((deltY / deltX) * x)) * width)] = color;
			}
		}
	}

	public void plotRect(int x1, int y1, int x2, int y2, int radius, int color) {
		plotLine(x1, y1, x2, y1, radius, color);
		plotLine(x1, y2, x2, y2, radius, color);
		plotLine(x1, y1, x1, y2, radius, color);
		plotLine(x2, y1, x2, y2, radius, color);
	}

	public void drawRect(int x1, int y1, int x2, int y2, int color) {
		drawLine(x1, y1, x2, y1, color);
		drawLine(x1, y2, x2, y2, color);
		drawLine(x1, y1, x1, y2, color);
		drawLine(x2, y1, x2, y2, color);
	}
	
	public void drawFilledRect(int x1, int y1, int x2, int y2, int color) {
		for (int r = x1; r < x2; r++) {
			for (int c = y1; c < y2; c++) {
				pixels[r + c * width] = color;
			}
		}
	}

	public void clear(int x, int y, int dx, int dy) {
		for (int i = 0; i < points.size(); i++) {
			int px = points.get(i).x;
			int py = points.get(i).y;
			if ((x <= px && px <= dx) && (y <= py && py <= dy)) {
				points.remove(i);
				i--;
			}
		}
	}
}