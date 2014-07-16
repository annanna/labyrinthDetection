import java.io.*;

public class FileWrite {

	private FileWriter fstream;
	private BufferedWriter out;
	
	public FileWrite() throws IOException{
//		fstream = new FileWriter("/Users/anna/lightcontrol/public/javascripts/obstacles.js");
		fstream = new FileWriter("C:/Users/anna/Documents/Uni/5. Semester/Projekt/lightcontrol/public/javascripts/obstacles.js");
		out = new BufferedWriter(fstream);
		
	}
	
	public void writeObstaclesToFile(int[][] o) throws IOException{
		
		for(int i=0;i<o.length;i++){
			int[] t = o[i];
			if(t[0] == 0 && t[1] == 0){
				
			}else{
				if(i>0){
					out.write(", ");
				}
				out.write("[" + t[0] + ", " + t[1] + "]");
			}
		}
		closeEverything();
	}
	
	public void closeEverything() throws IOException{
		System.out.println("closing");
		
		out.close();
		fstream.close();
	}

	public void writeObstacleAlternativeToFile(int[][] o) throws IOException {
		for(int i=0;i<o.length;i++){
			int[] t = o[i];

			if(i>0){
				out.write(", ");
			}
			String s = "[";
			for(int j = 0;j<t.length;j++){
				if(t[j] > 0){
					if(j>0){
						s+= ", ";
					}
					s+= t[j];
				}
			}
			s+="]";
//			System.out.println(s);
			out.write(s);
		}
		closeEverything();
	}

	public void writeObstacleAlternative2ToFile(int[][] o) throws IOException {
		out.write("var obstaclesFromFile = [");
		for(int i=0;i<o.length;i++){
			int[] t = o[i];

			if(i>0){
				out.write(", ");
			}
			String s = "[";
			for(int j = 0;j<t.length;j++){
					if(j>0){
						s+= ", ";
					}
					s+= t[j];
			}
			s+="]";
//			System.out.println(s);
			out.write(s);
		}
		out.write("];");
		
	}

	public void writeRedPixelsToFile(int[][] o) throws IOException {
		out.newLine();
		out.write("var startLine = [");
		for(int i=0;i<o.length;i++){
			int[] t = o[i];

			if(i>0){
				out.write(", ");
			}
			String s = "[";
			for(int j = 0;j<t.length;j++){
					if(j>0){
						s+= ", ";
					}
					s+= t[j];
			}
			s+="]";
//			System.out.println(s);
			out.write(s);
		}
		out.write("];");
	}

	public void writeRedPixelsAlternativeToFile(int[][] o) throws IOException {
		out.newLine();
		out.write("var startLine = {");
		int y = 0;
		String s = "";
		for(int i=0;i<o.length;i++){
			int[] t = o[i];
			for(int j = 0;j<t.length;j++){
				if(t[j] > 0){
					y = i;
//					out.write("y: " + i);
//					System.out.println(i);
					if(j>1){
						s+= ", ";
					}
					s+= t[j];
				}
			}
			
			
			
//			System.out.println(s);
//			out.write(s);
		}
		out.write("x: [" + s + "], ");
		out.write("y: " + y);
		out.write("};");
	}
}