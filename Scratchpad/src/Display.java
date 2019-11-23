import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.SliderUI;

public class Display extends Canvas implements Runnable, ActionListener, ChangeListener {

	private static final long serialVersionUID = 1L;

	public static int WIDTH;
	public static int HEIGHT;
	public static final String TITLE = "SCRATCHPAD";

	private Thread thread;
	private BufferedImage img;
	private boolean running = false;
	private int[] pixels;
	private Screen screen;
	private InputHandler input;

	private static JButton[] modes = new JButton[7];
	private String[] modeNames = new String[] { "Points", "Draw", "Erase", "Clear", "○", "/", "□"};
	private Rectangle[] modeSize = new Rectangle[] { new Rectangle(0, 0, 100, 30), new Rectangle(100, 0, 100, 30),
			new Rectangle(200, 0, 100, 30), new Rectangle(300, 0, 100, 30), new Rectangle(100, 30, 100, 30), new Rectangle(200, 30, 100, 30), new Rectangle(300, 30, 100, 30) };
	private boolean[] mode = new boolean[] { true, false, false, false, false, false, false};

	private static JButton grid;
	private boolean gridOn = false;
	public static int gridRow, gridCol;

	private static JSlider radiusSlider;
	private static JSlider colorSlider;
	private static int radius, color;

	public Display(int width, int height, int gridRow, int gridCol) {
		this.WIDTH = width;
		this.HEIGHT = height;
		this.gridRow = gridRow;
		this.gridCol = gridCol;
		img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		input = new InputHandler();
		addKeyListener(input);
		addFocusListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
		screen = new Screen(WIDTH, HEIGHT, input);
		for (int i = 0; i < modes.length; i++) {
			modes[i] = new JButton(modeNames[i]);
			modes[i].setBounds(modeSize[i]);
			modes[i].addActionListener(this);
		}

		grid = new JButton("Grid");
		grid.setBounds(0, 30, 100, 30);
		grid.addActionListener(this);

		radius = 5;
		radiusSlider = new JSlider(1, 30);
		radiusSlider.setBounds(400, 0, 150, 30);
		radiusSlider.addChangeListener(this);

		color = 0;
		colorSlider = new JSlider(0, 16777215);
		colorSlider.setBounds(550, 0, 150, 30);
		colorSlider.addChangeListener(this);
	}

	private void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	private void stop() {
		if (!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void run() {
		int frames = 0;
		double unprocessedSeconds = 0;
		long previousTime = System.nanoTime();
		double secondsPerTick = 1 / 60.0;
		int tickCount = 0;
		boolean ticked = false;

		while (running) {
			long currentTime = System.nanoTime();
			long passedTime = currentTime - previousTime;
			previousTime = currentTime;
			unprocessedSeconds += passedTime / 1000000000.0;
			// wont let you access text field
			// requestFocus();

			while (unprocessedSeconds > secondsPerTick) {
				tick();
				unprocessedSeconds -= secondsPerTick;
				ticked = true;
				tickCount++;
				if (tickCount % 60 == 0) {
					// System.out.println(frames + "fps");
					previousTime += 1000;
					frames = 0;
				}

			}
			if (ticked) {
				render();
				frames++;
			}
			render();
			frames++;
		}
	}

	//private int time;

	private void tick() {
		//colorSlider.setBackground(new Color(color));
	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		screen.render(mode, gridOn, radius, color);

		for (int i = 0; i < WIDTH * HEIGHT; i++) {
			pixels[i] = screen.pixels[i];
		}

		Graphics g = bs.getDrawGraphics();

		g.drawImage(img, 0, 0, null);

		g.dispose();
		bs.show();
	}

	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == radiusSlider) {
			radius = radiusSlider.getValue();
		}
		if (e.getSource() == colorSlider) {
			color = colorSlider.getValue();
		}
	}

	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		for (int i = 0; i < mode.length; i++) {
			if (src == modes[i]) {
				mode[i] = true;
				for (int a = 0; a < mode.length; a++) {
					if (a != i) {
						mode[a] = false;
					}
				}
			}
		}
		if (src == grid) {
			gridOn = !gridOn;
		}
	}

	public static void openWindow(int width, int height, int gridRow, int gridCol) {
		Display game = new Display(width, height, gridRow, gridCol);
		JFrame frame = new JFrame(TITLE);
		for (int i = 0; i < modes.length; i++) {
			frame.add(modes[i]);
		}
		frame.add(radiusSlider);
		frame.add(colorSlider);
		frame.add(grid);
		frame.add(game);
		frame.pack();
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		game.start();
	}

}