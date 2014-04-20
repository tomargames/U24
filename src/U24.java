import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.applet.*;
import java.util.*;
import java.net.*;

public class U24 extends Applet implements ActionListener, Runnable
{
	private static final long serialVersionUID = -3575578768327824256L;
	private static final int BUTTONSIZE = 36;
	private static final int TEXTSIZE = 24;
	private static final int MAXPUZZLES = 40;
	int MAXTIME = 180;
	private Puzzle[] puzzles = new Puzzle[MAXPUZZLES];
	private String[] operators = {"+","-","*","/","(",")"};
	private Panel operPanel;
	private Panel puzzlePanel;
	private Panel messagePanel;
	private Panel buttonPanel;
	private Button[] operButtons = new Button[6];
	private Button[] puzzleButtons = new Button[4];
	private Button passButton;
	private Button giveUpButton;
	private Button clearGuessButton;
	private Button backSpaceButton;
	private Button submitButton;
	private TextField guessText;
	private Panel display;
	private displayPanel scorePanel = new displayPanel("Points");
	private displayPanel multiplierPanel = new displayPanel("Multiplier");
	private displayPanel clockPanel = new displayPanel("Time");
	private displayPanel solvedPanel = new displayPanel("Solved");
	private Label messageLabel;
	private boolean started;
	private boolean giveUp;
	private int numberSolved;
	private int currentPuzzle;
	private int multiplier;
	private int points;
	long startSec;
	Puzzle puzzle;
	Thread thread;

	public void init()
	{
		this.setSize(520, 500);
		this.setBackground(ToMarUtils.toMarBackground);
		for (int i = 0; i < MAXPUZZLES; i++)
		{
			puzzles[i] = new Puzzle(this.getParameter("P" + i));
		}
		this.setLayout(new GridLayout(6,1,1,1));
		display = new Panel();
		display.setLayout(new GridLayout(1,4,1,1));
		display.add(scorePanel);
		display.add(multiplierPanel);
		display.add(solvedPanel);
		display.add(clockPanel);
		this.add(display);
		operPanel = new Panel();
		operPanel.setLayout(new GridLayout(1,6,1,1));
		for (int i = 0; i < operators.length; i++)
		{
			operButtons[i] = makeButton(BUTTONSIZE, tmColors.LIGHTBLUE, operators[i], Font.PLAIN, operPanel);
		}
		this.add(operPanel);
		puzzlePanel = new Panel();
		puzzlePanel.setLayout(new GridLayout(1,4,5,5));
		for (int i = 0; i < Puzzle.NUMBERS; i++)
		{
			puzzleButtons[i] = makeButton(BUTTONSIZE, tmColors.GREEN, "??", Font.PLAIN, puzzlePanel);
		}
		this.add(puzzlePanel);
		guessText = new TextField(30);
		guessText.setFont(new Font("Verdana", Font.PLAIN, 36));
		guessText.setBackground(tmColors.OFFWHITE);
		this.add(guessText);
		messagePanel = new Panel();
		messagePanel.setLayout(new GridLayout(2,1,1,1));
		submitButton = makeButton(TEXTSIZE, tmColors.ORANGE, "Start Game", Font.PLAIN, messagePanel);
		String msg = this.getParameter("message");
		msg = (msg == null) ? "Welcome to Ultimate 24!" : msg;
		messageLabel = makeLabel(TEXTSIZE, msg, Label.CENTER, messagePanel, Font.PLAIN, tmColors.DARKGREEN);
		this.add(messagePanel);
		buttonPanel = new Panel();
		passButton = makeButton(TEXTSIZE, tmColors.LIGHTGREEN, "Pass", Font.PLAIN, buttonPanel);
		giveUpButton = makeButton(TEXTSIZE, tmColors.YELLOW, "Give Up", Font.PLAIN, buttonPanel);
		clearGuessButton = makeButton(TEXTSIZE, tmColors.PALEORCHID, "Clear Guess", Font.PLAIN, buttonPanel);
		backSpaceButton = makeButton(TEXTSIZE, tmColors.PALEBLUE, "BackSpace", Font.PLAIN, buttonPanel);
		this.add(buttonPanel);
		started = false;
	}
	private Button makeButton(int textSize, Color bg, String label, int weight, Panel p)
	{
		Button b = new Button();
		b.setLabel(label);
		b.setBackground(bg);
		b.setFont(new Font("Verdana", weight, textSize));
		b.addActionListener(this);
		p.add(b);
		return b;
	}
	private Label makeLabel(int textSize, String label, int align, Panel p, int weight, Color fg)
	{
		Label l = new Label(label, align);
		l.setFont(new Font("Verdana", weight, textSize));
		l.setForeground(fg);
		p.add(l);
		return l;
	}
	public static void log(String m)
	{
		System.out.println(ToMarUtils.getDateTimeStamp()+ ": " + m);
	}
	private void reInit()
	{
		guessText.setText("");
		submitButton.setLabel("Submit");
		numberSolved = currentPuzzle = 0;
		giveUpButton.setLabel("Give Up");
		setCurrentPuzzle();
		for (int i = 0; i < Puzzle.NUMBERS; i++)
		{
			puzzleButtons[i].setEnabled(true);
		}
		startSec = (new Date()).getTime();
		started = true;
		thread = new Thread(this);
		thread.start();
		points = 0;
		multiplier = 1;
	}

	private void setCurrentPuzzle()
	{
		messageLabel.setText("");
		puzzle = puzzles[currentPuzzle];
		guessText.setText("");
		for (int i = 0; i < Puzzle.NUMBERS; i++)
		{
			puzzleButtons[i].setLabel("" + puzzle.getArgument(i).getNum());
		}
	}
	public void endGame()
	{
		String encName = (this.getParameter("nm")).replaceAll(" ", "%20");
		String fwd = "";
		try
		{
			if (giveUp)
			{
				fwd = this.getParameter("site") + "U24?id=" + this.getParameter("id") + "&nm=" + encName + "&score=0";
			}
			else if (started)
			{
				started = false;
				submitButton.setEnabled(false);
				for (int i = 0; i < operButtons.length; i++)
				{
					operButtons[i].setEnabled(false);
				}
				for (int i = 0; i < puzzleButtons.length; i++)
				{
					puzzleButtons[i].setEnabled(false);
				}
				messageLabel.setText("Game over. Score is " + points + "!");
				Thread.sleep(1500);
				fwd = this.getParameter("site") + "U24?score=" + points + "&id=" + this.getParameter("id") + "&nm=" + encName + "&tsp=" + ToMarUtils.getDateTimeStamp();
			}
			this.getAppletContext().showDocument(new URL(fwd));
		}
		catch(Exception e)
		{
			log("Error 1: " + e);
		}
	}
	private void nextPuzzle()
	{
		while(true)
		{
			if (++currentPuzzle == MAXPUZZLES)
			{
				currentPuzzle = 0;
			}
			if (!puzzles[currentPuzzle].isSolved())
			{
				break;
			}
		}
		setCurrentPuzzle();
	}

	public void actionPerformed(ActionEvent event)
	{
		if (!started && event.getSource() == submitButton)
		{
			reInit();
			return;
		}
		if (started)
		{
			if (event.getSource() == submitButton)
			{
				double value = puzzle.getValue(guessText.getText());
				if (value == 24)
				{
					puzzles[currentPuzzle].setSolved(true);
					points += multiplier * timeLeft();
					multiplier *= 2;
					if (++numberSolved == MAXPUZZLES)
					{
						points += 1000 * multiplier;
						endGame();
					}
					else
					{
						nextPuzzle();
					}
				}
				else
				{
					if (puzzle.getMessage().equals("OK"))
					{
						messageLabel.setText("No, that equals " + value + ". Try again!");
					}
					else
					{
						messageLabel.setText(puzzle.getMessage());
					}
				}
			}
			else if (event.getSource() == passButton)
			{
				if (giveUp)
				{
					unSetGiveUp();
				}
				else
				{
					multiplier = (multiplier > 1) ? multiplier/2 : 1;
					nextPuzzle();
				}
			}
			else if (event.getSource() == clearGuessButton)
			{
				guessText.setText("");
			}
			else if (event.getSource() == giveUpButton)
			{
				if (giveUp)
				{
					endGame();
					unSetGiveUp();
				}
				else
				{
					setGiveUp();
				}
			}
			else if (event.getSource() == backSpaceButton)
			{
				String s = guessText.getText();
				s = s.substring(0, s.length() - 2);
				guessText.setText(s);
			}
			else
			{
				boolean found = false;
				for (int i = 0; i < operators.length; i++)
				{
					if (event.getSource() == operButtons[i])
					{
						guessText.setText(guessText.getText() + " " + operators[i]);
						found = true;
						break;
					}
				}
				if (!found)
				{
					for (int i = 0; i < Puzzle.NUMBERS; i++)
					{
						if (event.getSource() == puzzleButtons[i])
						{
							guessText.setText(guessText.getText() + " " + puzzle.getArgument(i).getNum());
							break;
						}
					}
				}
			}
		}
	}
	private int timeLeft()
	{
		return MAXTIME - (int)((new Date()).getTime() - startSec)/1000;
	}
	private void setGiveUp()
	{
		giveUp = true;
		giveUpButton.setLabel("Confirm");
		passButton.setLabel("Reset");
	}
	private void unSetGiveUp()
	{
		giveUpButton.setLabel("Give Up");
		passButton.setLabel("Pass");
		giveUp = false;
	}
	public void run()
	{
		while (started)
		{
			int secs = timeLeft();
	    	clockPanel.setData(secs);
			if (secs > 0)
			{
		    	solvedPanel.setData(numberSolved);
		    	multiplierPanel.setData(multiplier);
		    	scorePanel.setData(points);
		    	if (secs < 11)
		    	{
		    		clockPanel.turnRed();
		    	}
			}
			else
			{
				endGame();
			}
		}
	}
}
