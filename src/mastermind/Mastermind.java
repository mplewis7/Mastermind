package mastermind;

import java.util.Random;

import processing.core.PApplet;

public class Mastermind extends PApplet {

	// Saves the RGB values for all 6 colored pegs in order
	// Indices: red = 0, orange = 1, yellow = 2, green = 3, cyan = 4, purple = 5
	int[][] colors = { { 255, 0, 0 }, { 255, 125, 0 }, { 250, 250, 0 }, { 0, 255, 0 }, { 0, 255, 255 },
			{ 150, 0, 220 } };

	// The number of guesses the user has made
	int guesses = 0;

	// Saves the layout of the board, -1 = empty slot, otherwise the number
	// corresponds to the index from array colors
	int[][] board = { { -1, -1, -1, -1 }, { -1, -1, -1, -1 }, { -1, -1, -1, -1 }, { -1, -1, -1, -1 },
			{ -1, -1, -1, -1 }, { -1, -1, -1, -1 }, { -1, -1, -1, -1 }, { -1, -1, -1, -1 }, { -1, -1, -1, -1 },
			{ -1, -1, -1, -1 } };

	// Saves the locations of the response pegs, 0 = none, 1 = white, 2 = black
	int[][] scores = { { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 },
			{ 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 },
			{ 0, 0 } };

	Random rand = new Random();
	// Saves the password as an array of numbers, with the same convention as in
	// array colors
	int[] password = generatePass();

	// the color shift of the "submit" button, makes it change colors when
	// pressed
	int colorShiftSubmit = 0;
	// the color shift of the "new game" button
	int colorShiftNewGame = 0;
	// the color shift of the "code-maker" button
	int colorShiftMaker = 0;
	// the color shift of the "code-breaker" button
	int colorShiftBreaker = 0;

	// becomes true when the player has won the game
	boolean winner = false;

	// true when the user is guessing the password
	boolean codebreaker = true;

	// stores the number of pegs the computer has correctly guessed
	int computerCorrect = 0;

	public void setup() {
		size(2000, 1990);
		background(255, 255, 255); // (red, green, blue)
	}

	public void draw() {
		background(255, 255, 255); // reset the background color to overwrite
									// any previous images
		board(); // draws the board, with the appropriate pegs on it
		instructions(); // writes the instructions to the side of the game
		winOrLose();
	}

	void instructions() {
		textSize(32);
		fill(0, 0, 0);
		text("Click on a circle to place ", 1550, 50);
		text("or change the color of a", 1550, 100);
		text("peg. Then hit \"Submit\" to ", 1550, 150);
		text("play your guess.", 1550, 200);
		text("Pegs:", 1550, 300);
		text("Responses:", 1550, 700);
		textSize(30);
		text("- correct color and place", 1618, 755);
		text("- correct color, wrong", 1618, 825);
		text("place", 1646, 875);

		// informs player of current role
		textSize(38);
		text("Current role:", 1550, 1100);
		textSize(34);
		String role = "";
		if (codebreaker)
			role = "codebreaker";
		else
			role = "codemaker";
		text(role, 1600, 1160);

		// draws the 6 peg colors for reference
		fill(colors[0][0], colors[0][1], colors[0][2]);
		ellipse(1600, 400, 115, 115);
		fill(colors[1][0], colors[1][1], colors[1][2]);
		ellipse(1750, 400, 115, 115);
		fill(colors[2][0], colors[2][1], colors[2][2]);
		ellipse(1900, 400, 115, 115);
		fill(colors[3][0], colors[3][1], colors[3][2]);
		ellipse(1600, 550, 115, 115);
		fill(colors[4][0], colors[4][1], colors[4][2]);
		ellipse(1750, 550, 115, 115);
		fill(colors[5][0], colors[5][1], colors[5][2]);
		ellipse(1900, 550, 115, 115);

		fill(0, 0, 0);
		ellipse(1580, 750, 60, 60);
		fill(255, 255, 255);
		ellipse(1580, 820, 60, 60);

		// draws the “code-break” button
		fill(150 + colorShiftBreaker, 0, 255 + colorShiftBreaker);
		rect(1520, 1350, 440, 120);
		// draws the “code-make” button
		fill(150 + colorShiftMaker, 0, 255 + colorShiftMaker);
		rect(1520, 1500, 440, 120);
		// draws the “submit” button
		fill(150 + colorShiftSubmit, 0, 255 + colorShiftSubmit);
		rect(1520, 1650, 440, 120);
		// draws the “new game” button
		fill(150 + colorShiftNewGame, 0, 255 + colorShiftNewGame);
		rect(1520, 1800, 440, 120);

		fill(255, 255, 255);
		textSize(70);
		text("Codebreaker", 1520, 1430);
		text("Codemaker", 1560, 1580);
		text("Submit", 1630, 1730);
		text("New Game", 1570, 1880);
	}

	void winOrLose() {
		if (winner || (guesses >= 11 && !codebreaker)) {
			textSize(400);
			fill(50, 255, 190);
			text("You Win!!!", 50, 1100);
		}
		if ((guesses >= 10 && codebreaker) || computerCorrect >= 4) { // game
																		// was
																		// lost
			// winner = true;
			textSize(300);
			fill(200, 0, 140);
			text("You lost :(", 50, 1100);
			fill(255, 255, 255, 150);
			rect(250, 1300, 750, 200);
			for (int x = 0; x < password.length; x++) {
				int curPeg = password[x];
				fill(colors[curPeg][0], colors[curPeg][1], colors[curPeg][2]);
				ellipse(350 + 185 * x, 1400, 155, 155);
			}
		}
	}

	void board() {
		fill(20, 110, 200);// 0, 150, 250
		rect(1150, 25, 350, 1940, 0, 80, 80, 0);// Blue area
		fill(255, 255, 255);
		fill(60, 60, 60);// Grey
		rect(25, 25, 1150, 1940, 80, 0, 0, 80);// Main board
		circles();
		lines();
		fill(20, 80, 170);// Darker blue; 20, 110, 200
		textSize(200);
		text("M", 1243, 230);
		text("a", 1270, 400);
		text("s", 1275, 570);
		text("t", 1290, 740);
		text("e", 1275, 910);
		text("r", 1288, 1080);
		text("M", 1243, 1330);
		text("i", 1298, 1500);
		text("n", 1265, 1670);
		text("d", 1265, 1840);

		fill(15, 15, 15);
		textSize(60);
		for (int x = 1; x < 10; x++) {
			text(x, 60, 160 + (190 * (x - 1)));
		}
		text("10", 35, 1870);
	}

	void lines() {
		fill(45, 45, 45);
		for (int y = 230; y < 1900; y += 190) {
			rect(60, y, 1030, 10);
		}
	}

	void circles() {
		int xx = 0;
		int yy = 0;
		for (int x = 190; x < 800; x += 200) { // password pegs
			yy = 0;
			for (int y = 140; y < 1900; y += 190) {
				int curColor = board[yy][xx];
				if (curColor != -1) {
					fill(colors[curColor][0], colors[curColor][1], colors[curColor][2]);
					ellipse(x, y, 115, 115);
				} else {
					fill(45, 45, 45);
					ellipse(x, y, 75, 75);// 145 width
					fill(10, 10, 10);
					ellipse(x, y, 60, 60);// 130
				}
				yy++;
			}
			xx++;
		}

		xx = 0;
		yy = 0;
		for (int x = 950; x < 1100; x += 100) { // response pegs
			yy = 0;
			for (int y = 100; y <= 1900; y += 190) {
				if (scores[yy][xx] == 2) {
					fill(0, 0, 0);
					ellipse(x, y, 60, 60);
				} else if (scores[yy][xx] == 1) {
					fill(255, 255, 255);
					ellipse(x, y, 60, 60);
				} else {
					fill(45, 45, 45);
					ellipse(x, y, 40, 40);// 60
					fill(10, 10, 10);
					ellipse(x, y, 30, 30);// 50
				}
				yy++;
				if (scores[yy][xx] == 2) {
					fill(0, 0, 0);
					ellipse(x, y + 80, 60, 60);
				} else if (scores[yy][xx] == 1) {
					fill(255, 255, 255);
					ellipse(x, y + 80, 60, 60);
				} else {
					fill(45, 45, 45);
					ellipse(x, y + 80, 40, 40);// 60
					fill(10, 10, 10);
					ellipse(x, y + 80, 30, 30);// 50
				}
				// fill(45, 45, 45);
				// ellipse(x, y, 40, 40);//60
				// ellipse(x, y+80, 40, 40);//60
				// fill(10, 10, 10);
				// ellipse(x, y, 30, 30);//50
				// ellipse(x, y+80, 30, 30);//50
				yy++;
			}
			xx++;
		}
	}

	public void mousePressed() {
		if (mouseX > 1550 && mouseX < 1950 && mouseY > 1800 && mouseY < 1920) { // new game
			newGame();
			colorShiftNewGame = -25;
		} 
		else if (mouseX > 1550 && mouseX < 1950 && mouseY > 1650 && mouseY < 1770) { // submit
			colorShiftSubmit = -25;
			if (codebreaker && (board[guesses][0] != -1 && board[guesses][1] != -1 && board[guesses][2] != -1
					&& board[guesses][3] != -1)) {
				scoreGuess();
				guesses++;
			}
		} 
		else if (mouseX > 1550 && mouseX < 1950 && mouseY > 1350 && mouseY < 1470) { // code-breaker
			colorShiftBreaker = -25;
			codebreaker = true;
			newGame();
		} 
		else if (mouseX > 1550 && mouseX < 1950 && mouseY > 1500 && mouseY < 1620) { // code-maker
			colorShiftMaker = -25;
			codebreaker = false;
			newGame();
		} 
		else {
			if (codebreaker) {
				int xx = 0;
				for (int x = 190; x < 800; x += 200) {// x=140
					if (distTo(mouseX, mouseY, x, 140 + 190 * (guesses)) <= (float) (115.0 / 2.0)) {
						board[guesses][xx] = (board[guesses][xx] + 2) % 7 - 1;
					}
					xx++;
				}
			} 
			else {
				int xx = 0;
				for (int x = 950; x < 1100; x += 100) {
					if (distTo(mouseX, mouseY, x, 100 + 190 * (guesses)) <= (float) (60.0 / 2.0)) {
						scores[2 * guesses][xx] = (scores[2 * guesses][xx] + 1) % 3;
					}
					if (distTo(mouseX, mouseY, x, 180 + 190 * (guesses)) <= (float) (60.0 / 2.0)) {
						scores[2 * guesses + 1][xx] = (scores[2 * guesses + 1][xx] + 1) % 3;
					}
					xx++;
				}
			}
		}
	} //end of mousePressed

	public void mouseReleased() {
		colorShiftSubmit = 0;
		colorShiftNewGame = 0;
		colorShiftBreaker = 0;
		colorShiftMaker = 0;
	} //end of mouseReleased

	// snarl rawr -Dean Weatherbie
	float distTo(int x, int y, int xd, int yd) {
		float s1 = (float) abs(x - xd);
		float s2 = (float) abs(y - yd);
		return sqrt(s1 * s1 + s2 * s2);
	}

	int[] generatePass() {
		int[] pass = { rand.nextInt(6), rand.nextInt(6), rand.nextInt(6), rand.nextInt(6) };
		return pass;
	}

	// finds the 
	int[] scoreGuess() {
		int black = 0;
		int white = 0;
		boolean[] usedGuess = { false, false, false, false };
		boolean[] usedPass = { false, false, false, false };
		for (int x = 0; x < 4; x++) {
			if (board[guesses][x] == password[x]) {
				black++;
				usedGuess[x] = true;
				usedPass[x] = true;
			}
		}
		for (int g = 0; g < 4; g++) {
			for (int p = 0; p < 4; p++) {
				if (password[p] == board[guesses][g] && !usedGuess[g] && !usedPass[p]) {
					white += 1;
					usedGuess[g] = true;
					usedPass[p] = true;
				}
			}
		}

		if (black == 4)
			winner = true;

		// put nums in scores
		for (int y = 0; y < 2; y++) {
			for (int x = 0; x < 2; x++) {
				if (black > 0) {
					scores[y + (2 * guesses)][x] = 2;
					black--;
				} else if (white > 0) {
					scores[y + (2 * guesses)][x] = 1;
					white--;
				}
			}
		}
		int[] score = {black, white};
		return score;
	} //end of scoreGuess
	
	public void newGame(){
		password = generatePass();
		guesses = 0;
		winner = false;
		// System.out.println(password[0]+" "+password[1]+" "+password[2]+"
		// "+password[3]);
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 10; y++) {
				board[y][x] = -1;
			}
		}
		for (int x = 0; x < 2; x++) {
			for (int y = 0; y < 20; y++) {
				scores[y][x] = -0;
			}
		}
	} // end of newGame

}
