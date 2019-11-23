import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Start implements ActionListener{
	
	private static int width = 400, height = 400;
	private String w = "1000",h = "1000",r = "10",c = "10";
	private static JButton startB;
	private static JTextField widthB, heightB, gridR, gridC;
	private static JLabel[] label = new JLabel[5];
	
	public Start(){
		label[0] = new JLabel("Width");
		label[1] = new JLabel("Height");
		label[2] = new JLabel("GridRow");
		label[3] = new JLabel("GridCol");
		label[4] = new JLabel();
		
		
		widthB = new JTextField();
		heightB = new JTextField();
		gridR = new JTextField();
		gridC = new JTextField();
		
		widthB.setBounds(70, 10, 75, 30);
		label[0].setBounds(10, 10, 75, 30);
		heightB.setBounds(70, 50, 75, 30);
		label[1].setBounds(10, 50, 75, 30);
		gridR.setBounds(70, 90, 75, 30);
		label[2].setBounds(10, 90, 75, 30);
		gridC.setBounds(70, 130, 75, 30);
		label[3].setBounds(10, 130, 75, 30);
		
		
		
		widthB.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent event) {
	    		w = widthB.getText();
	    }
	});
		heightB.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent event) {
		    	h = heightB.getText();
	    }
	});

		gridR.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent event) {
		    	r = gridR.getText();
	    }
	});

		gridC.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent event) {
		    	c = gridC.getText();
	    }
	});

		
		
		
		startB = new JButton("Start");
		startB.setBounds(10, 170, 75, 30);
		startB.addActionListener(this);
	}
	
	public static void main(String[] args){
		Start start = new Start();
		JFrame frame = new JFrame("New ScratchPad");
		//imports
		frame.add(startB);
		frame.add(widthB);
		frame.add(heightB);
		frame.add(gridR);
		frame.add(gridC);
		frame.add(label[0]);
		frame.add(label[1]);
		frame.add(label[2]);
		frame.add(label[3]);
		frame.add(label[4]);
		frame.pack();
		frame.setSize(width, height);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == startB){
			Display.openWindow(Integer.valueOf(w), Integer.valueOf(h), Integer.valueOf(r), Integer.valueOf(c));
		}
	}
	
}