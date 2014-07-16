import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Obstacles extends JPanel {
	
	private static final String name = "Lightcontrol";
	
	private static final long serialVersionUID = 1L;
	private static final int borderWidth = 5;
	private static final int maxWidth = 400;
	private static final int maxHeight = maxWidth;
	private static final String initialFilename = "strecke1.jpg";
	private File openPath = new File(".");
	
	private static JFrame frame;
	
	private ImageView srcView;			// source image view
	private int[][] obstacles= new int[10809][2];
	private int[][] obstacleAlternative;
	private int[][] obstacleAlternative2;
	private int[][] redPixelsAlternative;
	private int[][] redPixelsAlternative2;
	private enum FileType {
		NORMAL
	}
	private enum DialogType {
		OPEN
	}
		
	public Obstacles() {
        super(new BorderLayout(borderWidth, borderWidth));

        setBorder(BorderFactory.createEmptyBorder(borderWidth,borderWidth,borderWidth,borderWidth));
 
        // load the default image
        File input = new File(initialFilename);
        if(!input.canRead()) 
        	input = fileDialog(DialogType.OPEN, FileType.NORMAL); // file not found, choose another image
        
        // create image views
        srcView = new ImageView();
        // control panel
        JPanel controls = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0,0,0,0);

		// open image button
        JButton loadSrc = new JButton("Open Source Image");
        loadSrc.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		loadSrcFile(fileDialog(DialogType.OPEN, FileType.NORMAL));
        	}        	
        });
                
        controls.add(loadSrc, c);
        
        // image panel
        JPanel images = new JPanel(new GridLayout(1,2));
        images.add(srcView);
        
        add(controls, BorderLayout.NORTH);
        add(images, BorderLayout.CENTER);
        
        loadSrcFile(input);
	}
	
	private File fileDialog(DialogType dType, FileType fType) {
        JFileChooser chooser = new JFileChooser() {
			private static final long serialVersionUID = 1L;
			@Override
            public void approveSelection() {
                File file = getSelectedFile();
                if(file.exists() && getDialogType() == SAVE_DIALOG){
                    int result = JOptionPane.showConfirmDialog(this,"Overwrite existing file?", "Existing file", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    switch(result){
                        case JOptionPane.YES_OPTION:
                            super.approveSelection();
                            return;
                        default:
                            return;
                    }
                }
                super.approveSelection();
            }        
        };
        
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images (*.jpg, *.png, *.gif)", "jpg", "png", "gif");
        chooser.setFileFilter(filter);
        chooser.setCurrentDirectory(openPath);
        
        int ret;
        if(dType == DialogType.OPEN) 
        	ret = chooser.showOpenDialog(this);
        else
        	ret = chooser.showSaveDialog(this);
        
        if(ret == JFileChooser.APPROVE_OPTION) {
        	openPath = chooser.getSelectedFile().getParentFile();
        	return chooser.getSelectedFile();
        }
        
        return null;		
	}
	
	private void loadSrcFile(File file) {
		if(file == null) return;
		
		srcView.loadImage(file);
		srcView.setMaxSize(new Dimension(maxWidth, maxHeight));
		obstacleAlternative = new int[srcView.getImgHeight()][srcView.getImgWidth()];
		redPixelsAlternative = new int[srcView.getImgHeight()][srcView.getImgWidth()];
		
		obstacleAlternative2 = new int[srcView.getImgHeight()][srcView.getImgWidth()];
		redPixelsAlternative2 = new int[srcView.getImgHeight()][srcView.getImgWidth()];
		fill2DArray(obstacleAlternative2);
		fill2DArray(redPixelsAlternative2);
		findObstacles();
		
		
		frame.pack();
	}
	
	private void findObstacles() {
		int pixels[] = srcView.getPixels();
		int width = srcView.getImgWidth();
		int height = srcView.getImgHeight();
		int xTemp = 0;
		int xTempRed = 0;
		int temp = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int pos = y * width + x;
				
				int r = (pixels[pos] >> 16) & 0xff;
				int g = (pixels[pos] >> 8) & 0xff;
				int b = pixels[pos] & 0xff;
				
				//normalize values
				if(r > 250 && g < 5 && b < 5){
					r = 255; g = 0; b = 0;
				}else if(r < 50 && g < 20 && b < 20){
					r = 0; g = 0; b = 0;
				}else if(r > 240 && g > 220 && b > 220){
					r = 255; g = 255; b = 255;
				}else{
					r = 255; g = 0; b = 0;
				}
				
				if(r == 0 && g == 0 && b == 0){
					int[] t = {x, y};
					obstacles[temp] = t;
					temp++;
					
					
					obstacleAlternative[y][xTemp] = x;
					xTemp++;
					
					obstacleAlternative2[y][x]++;
				}else if(r == 255 && g == 0 && b == 0){
					redPixelsAlternative[y][xTempRed] = x;
					xTempRed++;
					
					redPixelsAlternative2[y][x]++;
					System.out.println("x: "+x+" y: "+y);
				}
				pixels[pos] = (0xFF << 24) | (r << 16) | (g << 8) | b;
			}
			xTemp = 0;
			xTempRed = 0;
		}
		srcView.setPixels(pixels);
		System.out.println(redPixelsAlternative[66][0]);
		try {
			
			FileWrite f = new FileWrite();
//			f.writeObstaclesToFile(obstacles);
//			f.writeObstacleAlternativeToFile(obstacleAlternative);
			f.writeObstacleAlternative2ToFile(obstacleAlternative2);
//			f.writeRedPixelsToFile(redPixels);
			f.writeRedPixelsAlternativeToFile(redPixelsAlternative);
			f.closeEverything();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void fill2DArray(int[][] a){
		for(int i=0;i<srcView.getImgHeight();i++){
			for(int j=0;j<srcView.getImgWidth();j++){
				a[i][j] = 0;
			}
		}
	}
    
	private static void createAndShowGUI() {
		// create and setup the window
		frame = new JFrame("RLE " + name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JComponent newContentPane = new Obstacles();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        // display the window.
        frame.pack();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        frame.setLocation((screenSize.width - frame.getWidth()) / 2, (screenSize.height - frame.getHeight()) / 2);
        frame.setVisible(true);
	}

	public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
	}

}