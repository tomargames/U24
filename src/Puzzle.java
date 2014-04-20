import java.io.FileOutputStream;
import java.io.PrintWriter;

public class Puzzle
{
	private Argument[] argument;
	private GuessEvaluator guessEvaluator = new GuessEvaluator();
	private boolean solved;
	private String message;
	private int difficulty;
	public static final int NUMBERS = 4;

	public Puzzle(String puzzleString)
	{
		int[] hold1 = new int[Puzzle.NUMBERS];
		int[] hold2 = new int[Puzzle.NUMBERS];
		String s = "0123";
		for (int i = 0; i < 4; i++)
		{
			hold1[i] = (new Integer(puzzleString.substring(i * 2, i * 2 + 2))).intValue();
		}
		for (int i = 0; i < Puzzle.NUMBERS; i++)
		{
			int rnd = ToMarUtils.getRnd(s.length());
			hold2[i] = hold1[(new Integer(s.substring(rnd, rnd + 1))).intValue()];
			s = s.substring(0, rnd) + s.substring(rnd + 1, s.length());
		}
		argument = new Argument[NUMBERS];
		for (int i = 0; i < NUMBERS; i++)
		{
			argument[i] = new Argument(hold2[i]);
		}
		solved = false;
	}
	public void log(String s)
	{
		System.out.println(s);
	}
	public void resetUsed()
	{
		for (int i = 0; i < NUMBERS; i++)
		{
			argument[i].setUsed(false);
		}
	}
	public Argument getArgument(int i)
	{
		return argument[i];
	}

	private static boolean createCombinations(int[] args, boolean oneIsEnough)
	{
		boolean found = false;
		Combination.resetCombinations();
		for (int i1 = 0; i1 < NUMBERS; i1++)
		{
			for (int i2 = 0; i2 < NUMBERS; i2++)
			{
				if (i2 == i1)
				{
					continue;
				}
				for (int i3 = 0; i3 < NUMBERS; i3++)
				{
					if (i3 == i2 || i3 == i1)
					{
						continue;
					}
					for (int i4 = 0; i4 < NUMBERS; i4++)
					{
						if (i4 == i3 || i4 == i2 || i4 == i1)
						{
							continue;
						}
						for (int o1 = 0; o1 < Combination.OPERATORS.length; o1++)
						{
							for (int o2 = 0; o2 < Combination.OPERATORS.length; o2++)
							{
								for (int o3 = 0; o3 < Combination.OPERATORS.length; o3++)
								{
									if (Combination.checkTemplates(args[i1],args[i2],args[i3],args[i4],o1,o2,o3))
									{
										if (oneIsEnough)
										{
											return true;
										}
										found = true;
									}
								}
							}
						}
					}
				}
			}
		}
		return found;
	}


	public double getValue(String expr)
	{
		double value = 0;
		value = guessEvaluator.evaluate(expr, this);
		message = guessEvaluator.getMessage();
		return value;
	}

	/**
	 * @return Returns the solved.
	 */
	public boolean isSolved()
	{
		return solved;
	}

	/**
	 * @param solved The solved to set.
	 */
	public void setSolved(boolean solved)
	{
		this.solved = solved;
	}

	/**
	 * @return Returns the message.
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * @param message The message to set.
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}
	public static void generatePuzzles(int target)
	{
		int[] args = {0,0,0,0};
//		PrintWriter[] outfile = new PrintWriter[5];
		try
		{
			PrintWriter outfile = new PrintWriter(new FileOutputStream("u24.txt"), true);
//			try
//			{
//				for (int i = 0; i < 5; i++)
//				{
//					outfile[i] = new PrintWriter(new FileOutputStream("u24" + i + ".txt"), true);
//				}
//			}
//			catch (Exception e)
//			{
//				System.out.println("Problem opening file: " + e);
//			}
			for (args[0] = 1; args[0] < target + 1; args[0]++)
			{
				for (args[1] = args[0]; args[1] < target + 1; args[1]++)
				{
					for (args[2] = args[1]; args[2] < target + 1; args[2]++)
					{
						for (args[3] = args[2]; args[3] < target + 1; args[3]++)
						{
//							System.out.println("" + args[0] + " " + args[1] + " " + args[2] + " " + args[3]);
							// now you have 4 numbers
							if (createCombinations(args, true))
							{
								String o = "" + args[0] + " " + args[1] + " " + args[2] + " " + args[3] + "\r\n";
								outfile.print(o);
								System.out.println(o);
//								outfile[Combination.evaluateDifficulty() - 1].print("" + args[0] + " " + args[1] + " " + args[2] + " " + args[3] + " " +
//										  ((Combination) (Combination.getCombinations()).get(0)).getExpression() +
//										 "\r\n");
//								System.out.println("Writing: " + args[0] + " " + args[1] + " " + args[2] + " " + args[3] + " " +
//										  ((Combination) (Combination.getCombinations()).get(0)).getExpression() +
//										  	" (" + Combination.getDifficultyLevel() + ")");
							}
						}
					}
				}
			}
			outfile.close();
//			for (int i = 0; i < 5; i++)
//			{
//				outfile[i].close();
//			}
		}
		catch (Exception e)
		{
			System.out.println("File open error = " + e.getMessage());
		}
	}
	public static void main(String[] args)
	{
//		Puzzle.generatePuzzles(24);
/*		int[][] sets = {{1,1,4,4},{6,6,6,6},{4,3,2,1},{3,3,8,8},{3,3,7,7},{19,2,8,10},{5,6,20,24},
						{7,3,11,5},{1,2,13,15},{6,17,12,14},{6,22,14,11},
						{15,12,8,20},{2,7,6,18},{5,6,20,24},{15,3,15,4},{15,14,4,22},{15,15,6,2},
						{15,18,21,12},{14,7,5,23},{12,5,22,14},{13,15,18,21},{11,20,14,14},
						{9,16,22,24},{10,6,8,15},{3,19,13,20}};
*/		int[][] sets = {{4,5,23,18}};
		for (int s = 0; s < sets.length; s++)
		{
			Combination.resetCombinations();
			System.out.println("************Running " + sets[s][0] + " " + sets[s][1] + " " + sets[s][2] + " " + sets[s][3]);
			if (createCombinations(sets[s], false))
			{
				for (int i = 0; i < Combination.getCombinations().size(); i++)
				{
					System.out.println("" + ((Combination)Combination.getCombinations().elementAt(i)).toString());
				}
			}
			System.out.println("Combinations: " + Combination.getCombinations().size());
			System.out.println("Difficulty: " + Combination.evaluateDifficulty());
		}
	}
	public int getDifficulty()
	{
		return difficulty;
	}
	public void setDifficulty(int difficulty)
	{
		this.difficulty = difficulty;
	}
}
