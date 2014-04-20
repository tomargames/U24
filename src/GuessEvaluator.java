import java.util.Vector;

public class GuessEvaluator extends ToMarEvaluator
{
	boolean extraNumbers;
	
	public double evaluate(String str, Puzzle puzzle)
	{
//		log("evaluating string " + str);
		puzzle.resetUsed();
		extraNumbers = false;
		// create an array of Strings containing the elements of the equation
		numberReceived = false;
		args = new Vector();
		parenCounts[0] = parenCounts[1] = 0;
		multiplier = 1;
		StringBuffer numberInProgress = null;
		boolean operFlag = false;
		setMessage("OK");
		for (int i = 0; i < str.length(); i++)
		{
			String currentChar = str.substring(i, i+1);
//			log("Processing: " + currentChar + ", multiplier is " + multiplier); 
			if (" ".equals(currentChar))
			{
				numberInProgress = dumpNumber(numberInProgress, args, puzzle);
				continue;
			}
			if (PARENS.indexOf(currentChar) > -1)
			{
				numberInProgress = dumpNumber(numberInProgress, args, puzzle);
				parenCounts[PARENS.indexOf(currentChar)] += 1;
				args.addElement(currentChar);
			}
			else if (OPERS.indexOf(currentChar) > -1)
			{
				if (operFlag)
				{
					if (!("-".equals(currentChar)))
					{
						setMessage("Operator not followed by operand");
						return ERRORFLAG;
					}
					else
					{
						multiplier *= -1;
						continue;
					}
				}
				operFlag = true;
				numberInProgress = dumpNumber(numberInProgress, args, puzzle);
				if (!numberReceived)
				{
					if (!("-".equals(currentChar)))
					{
						setMessage("Operator not followed by operand");
						return ERRORFLAG;
					}
					else
					{
						multiplier *= -1;
						continue;
					}
				}	
				args.addElement(currentChar);
			}
			else if (NUMS.indexOf(currentChar) > -1)
			{
				operFlag = false;
				if (numberInProgress != null)
				{
					numberInProgress.append(currentChar);
				}
				else
				{
					numberInProgress = new StringBuffer(currentChar);
				}
			}
			else
			{
				setMessage("Non-numeric character " + currentChar + ".");
				return ERRORFLAG;
			}
		}
		numberInProgress = dumpNumber(numberInProgress, args, puzzle);
		if (operFlag)
		{
			setMessage("Operator not followed by operand");
			return ERRORFLAG;
		}
		// check for balanced parens
		if (parenCounts[0] != parenCounts[1])
		{
			setMessage("Out of balance - " + parenCounts[0] +
					" left parens and " + parenCounts[1] +  
			" right parens.");
//			log(this.message);
			return ERRORFLAG;
		}
//		displayArgs();
		// process stuff inside parentheses
		if(parenCounts[0] > 0) 
		{
			for (int lp = args.size() - 1; lp >= 0; lp--) //work back from the end looking for left paren 
			{
				if (!message.equals(OK))
				{
					break;
				}	
				if (((String) args.elementAt(lp)).equals("("))
				{
					for (int rp = lp + 1; rp < args.size(); rp++)
					{
						if (((String) args.elementAt(rp)).equals(")"))
						{
							args = processElements(lp, rp);
							break;
						}
					}
				}
			}
		}
		// process what's left - result should be the only element left
		while(args.size() > 1 && message.equals(OK))
		{
			args = processElements(0, args.size() - 1);
//			displayArgs();
		}
		for (int i = 0; i < Puzzle.NUMBERS; i++)
		{
			if (puzzle.getArgument(i).getUsed() == false)
			{
				setMessage("Failed to use the number " + puzzle.getArgument(i).getNum() + ".");
				return ERRORFLAG;
			}	
		}
		if (extraNumbers)
		{
			setMessage("Extra numbers used that are not in the puzzle.");
			return ERRORFLAG;
		}
		if (!message.equals(OK))
		{
			return ERRORFLAG;
		}
		return makeDouble((String) args.elementAt(0)) * multiplier;
	}
	protected StringBuffer dumpNumber(StringBuffer numberInProgress, Vector args, Puzzle puzzle)
	{
		if (numberInProgress != null)
		{
//			log("dumping " + numberInProgress);
			args.addElement(numberInProgress.toString());
			int arg = (new Integer(numberInProgress.toString()).intValue());
			numberInProgress = null;
			numberReceived = true;
			if (puzzle != null)
			{
				boolean found = false;
				for (int j = 0; j < Puzzle.NUMBERS; j++)
				{
					if (puzzle.getArgument(j).getUsed() == false && puzzle.getArgument(j).getNum() == arg)
					{
						puzzle.getArgument(j).setUsed(true);
						found = true;
						break;
					}
				}
				if (found == false)
				{
					extraNumbers = true;
				}
			}
		}
		return numberInProgress;
	}
}
