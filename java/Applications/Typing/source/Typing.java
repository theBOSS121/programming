import java.util.Random;
import java.util.Scanner;

public class Typing {
	Dictionary d = new Dictionary();
	Random rand = new Random();
	Scanner scanner = new Scanner(System.in);
	int i = 0;
	long time;
	double perMinute = 0;
	
	public Typing() {
		gameLoop();
	}
	
	private void gameLoop() {
		long firstTime = System.currentTimeMillis();
		while(true) {			
			String s = d.words[rand.nextInt(d.words.length)];
			System.out.printf("%s\t\t\t%d\n", s,(int) perMinute);
			String inputed = "";
			while(!s.toLowerCase().equals(inputed.toLowerCase())) {
				inputed = scanner.nextLine();
				if(inputed.equals("end program")) break;
			}
			if(inputed.equals("end program")) break;
			i++;
			time = System.currentTimeMillis();
			perMinute = i / (((time - firstTime) / 1000.0) / 60);
		}
		
	}

	public static void main(String[] args) {
		new Typing();
	}
	
}
