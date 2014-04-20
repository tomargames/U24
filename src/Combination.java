import java.util.Vector;

public class Combination
{
	private int a = 0;
	private int b = 0;
	private int c = 0;
	private int d = 0;
	private int op1 = 0;
	private int op2 = 0;
	private int op3 = 0;
	private String template = "";
	private String expression = "";
	private int difficulty = 0;
	private int weight = 1;
	private static Vector combinations;
	private static ToMarEvaluator tmEvaluator = new ToMarEvaluator();
	public static final String OPERATORS[] = {" + ", " - ", " * ", " / "};
	public static final int TARGET = 24;
	public static final int ADDITION = 0;
	public static final int SUBTRACTION = 1;
	public static final int MULTIPLICATION = 2;
	public static final int DIVISION = 3;
	public static int difficultyLevel = 0;
	
	public Combination(int a, int b, int c, int d, int op1, int op2, int op3, String template, String expression)
	{
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.op1 = op1;
		this.op2 = op2;
		this.op3 = op3;
		this.template = template;
		this.expression = expression;
	}
	private void addWeight()
	{
		weight += 1;
	}
	private int getWeight()
	{
		return weight;
	}
	public static int evaluateDifficulty()
	{
		// take average level across combinations
		int total = 0;
		int divideBy = 0;
		for (int i = 0; i < combinations.size(); i++)
		{
			total += (((Combination) combinations.get(i)).getDifficulty() * ((Combination) combinations.get(i)).getWeight());
			divideBy += ((Combination) combinations.get(i)).getWeight();
		}
		difficultyLevel = (int)(Math.round((double)((double)total / (double)divideBy)));
//		log("Total: " + total + " DivideBy: " + divideBy + " Average: " + difficultyLevel);
		if (combinations.size() > 25)
		{
			log("decreasing for more than 25 combinations");
			difficultyLevel -= 1;
		}
		if (combinations.size() > 10)
		{
			log("decreasing for more than 10 combinations");
			difficultyLevel -= 1;
		}
		if (combinations.size() == 1 && difficultyLevel < 4 && difficultyLevel > 1)
		{
			log("increasing for only one combination");
			difficultyLevel += 1;
		}	
		difficultyLevel =  (difficultyLevel > 5) ? 5 : (difficultyLevel < 1) ? 1 : difficultyLevel;
		return difficultyLevel;
	}
	public static void evaluate(Combination c)
	{
		if (!uniqueCombination(c))
		{
			return;
		}	
		// assess difficulty of this combination
		int[] opCounts = {0,0,0,0};
		opCounts[c.getOp1()] += 1;
		opCounts[c.getOp2()] += 1;
		opCounts[c.getOp3()] += 1;
		if ("abcd".equals(c.getTemplate()))
		{
			if (c.getOp1() == MULTIPLICATION && c.getOp2() == SUBTRACTION && c.getOp3() == MULTIPLICATION
					&& c.getA() > 1 && c.getB() > 1 && c.getC() > 1 && c.getD() > 1)
			{
				c.setDifficulty(4);
			}
			else if (opCounts[DIVISION] > 0 || opCounts[MULTIPLICATION] > 0)
			{	
				c.setDifficulty(2);
			}
			else
			{	
				c.setDifficulty(1);
			}
		}
		else if ("(abc)d".equals(c.getTemplate()) || "a(bcd)".equals(c.getTemplate()))
		{
			if (opCounts[3] + opCounts[2] > 1)
			{	
				c.setDifficulty(5);
			}
			else if (opCounts[3] > 0)
			{	
				c.setDifficulty(4);
			}
			else
			{
				c.setDifficulty(2);
			}
		}
		else if ("(ab)(cd)".equals(c.getTemplate()) || (opCounts[DIVISION] + opCounts[MULTIPLICATION] > 1 && opCounts[DIVISION] > 0) || opCounts[1] > 1)
		{
			if ((c.getA() == c.getB() && (c.getOp1() == DIVISION || c.getOp1() == SUBTRACTION))
			|| (c.getC() == c.getB() && (c.getOp2() == DIVISION || c.getOp2() == SUBTRACTION))
			|| (c.getC() == c.getD() && (c.getOp3() == DIVISION || c.getOp3() == SUBTRACTION)))
			{
				c.setDifficulty(3);
			}
			else
			{	
				c.setDifficulty(4);
			}	
		}
		else
		{
			if ("(ab)cd".equals(c.getTemplate()))
			{
				if (c.getOp2() == DIVISION)
				{
					c.setDifficulty(4);
				}
				else
				{	
					c.setDifficulty(doParens(c.getA(),c.getOp1(),c.getB()));
				}
			}	
			else if ("a(bc)d".equals(c.getTemplate()))
			{
				if (c.getOp1() == DIVISION || c.getOp3() == DIVISION)
				{
					c.setDifficulty(4);
				}
				else
				{	
					c.setDifficulty(doParens(c.getB(),c.getOp2(),c.getC()));
				}
			}	
			else if ("ab(cd)".equals(c.getTemplate()))
			{
				if (c.getOp3() == DIVISION)
				{
					c.setDifficulty(4);
				}
				else
				{	
					c.setDifficulty(doParens(c.getC(),c.getOp3(),c.getD()));
				}
			}
			else
			{
				c.setDifficulty(3);
			}
		}
		combinations.addElement(c);
		return;
	}
	private static int doParens(int a, int o, int b)
	{
		double inParens = calcInParens(a, o, b);
//		log("doParens value: " + inParens);
		if (inParens == (int) inParens)
		{
			if (inParens > 48)
			{
				return 5;
			}
			else if (inParens > 24)
			{
				return 4;
			}
			else if (inParens > 12)
			{
				return 3;
			}
			else
			{
				return 2;
			}
		}
		if (inParens < .5)
		{
			return 5;
		}
		else if (inParens == .5)
		{
			return 3;
		}
		else 
		{
			return 4;
		}
	}	
	private static double calcInParens(int argA, int op, int argB)
	{
		String expr = "" + argA + OPERATORS[op] + argB;
//		log("Value of expr " + expr + " is " + tmEvaluator.evaluate(expr));
		return tmEvaluator.evaluate(expr);
	}
	
	public int getA()
	{
		return a;
	}

	public void setA(int a)
	{
		this.a = a;
	}

	public int getB()
	{
		return b;
	}

	public void setB(int b)
	{
		this.b = b;
	}

	public int getC()
	{
		return c;
	}

	public void setC(int c)
	{
		this.c = c;
	}

	public static void resetCombinations()
	{
		combinations = new Vector();
		difficultyLevel = 0;
	}
	public static Vector getCombinations()
	{
		return combinations;
	}

	public int getD()
	{
		return d;
	}

	public void setD(int d)
	{
		this.d = d;
	}

	public String getExpression()
	{
		return expression;
	}

	public void setExpression(String expression)
	{
		this.expression = expression;
	}

	public int getOp1()
	{
		return op1;
	}

	public void setOp1(int op1)
	{
		this.op1 = op1;
	}

	public int getOp2()
	{
		return op2;
	}

	public void setOp2(int op2)
	{
		this.op2 = op2;
	}

	public int getOp3()
	{
		return op3;
	}

	public void setOp3(int op3)
	{
		this.op3 = op3;
	}

	public String getTemplate()
	{
		return template;
	}

	public void setTemplate(String template)
	{
		this.template = template;
	}
	public int getDifficulty()
	{
		return difficulty;
	}
	public void setDifficulty(int difficulty)
	{
		this.difficulty = difficulty;
	}
	public String toString()
	{
		return ("Difficulty: " + this.difficulty + " Weight: " + this.weight + "    " + this.expression);
	}
	public static int getDifficultyLevel()
	{
		return difficultyLevel;
	}
	public static void setDifficultyLevel(int difficultyLevel)
	{
		Combination.difficultyLevel = difficultyLevel;
	}
	private static void log(String x)
	{
		System.out.println(x);
	}
	
	public static boolean checkTemplates(int i1, int i2, int i3, int i4, int o1, int o2, int o3)
	{	
		String expr = "";
		// no parens
		expr = "" + i1 + OPERATORS[o1] + i2 + OPERATORS[o2] + i3 + OPERATORS[o3] + i4;
		if (tmEvaluator.evaluate(expr) == TARGET) 
		{
			Combination.evaluate(new Combination(i1, i2, i3, i4, o1, o2, o3, "abcd", expr));
			return true;
		}
		// (a,b) c d
		expr = "(" + i1 + OPERATORS[o1] + i2 + ")" + OPERATORS[o2] + i3 + OPERATORS[o3] + i4;
		if (tmEvaluator.evaluate(expr) == TARGET)
		{
			Combination.evaluate(new Combination(i1, i2, i3, i4, o1, o2, o3, "(ab)cd", expr));
			return true;
		}
		// a (b,c) d
		expr = "" + i1 + OPERATORS[o1] + "(" + i2 + OPERATORS[o2] + i3 + ")" + OPERATORS[o3] + i4;
		if (tmEvaluator.evaluate(expr) == TARGET)
		{
			Combination.evaluate(new Combination(i1, i2, i3, i4, o1, o2, o3, "a(bc)d", expr));
			return true;
		}
		// a b (c,d)
		expr = "" + i1 + OPERATORS[o1] + i2 + OPERATORS[o2] + "(" + i3 + OPERATORS[o3] + i4 + ")";
		if (tmEvaluator.evaluate(expr) == TARGET)
		{
			Combination.evaluate(new Combination(i1, i2, i3, i4, o1, o2, o3, "ab(cd)", expr));
			return true;
		}
		// (a,b)(c,d)
		expr = "(" + i1 + OPERATORS[o1] + i2 + ")" + OPERATORS[o2] + "(" + i3 + OPERATORS[o3] + i4 + ")";
		if (tmEvaluator.evaluate(expr) == TARGET)
		{
			Combination.evaluate(new Combination(i1, i2, i3, i4, o1, o2, o3, "(ab)(cd)", expr));
			return true;
		}
		// (a,b,c) d
		expr = "(" + i1 + OPERATORS[o1] + i2 + OPERATORS[o2] + i3 + ")" + OPERATORS[o3] + i4; 
		if (tmEvaluator.evaluate(expr) == TARGET) 
		{
			Combination.evaluate(new Combination(i1, i2, i3, i4, o1, o2, o3, "(abc)d", expr));
			return true;
		}
		// a (b,c,d)
		expr = "" + i1 + OPERATORS[o1] + "(" + i2 + OPERATORS[o2] + i3 + OPERATORS[o3] + i4 + ")";
		if (tmEvaluator.evaluate(expr) == TARGET)
		{
			Combination.evaluate(new Combination(i1, i2, i3, i4, o1, o2, o3, "a(bcd)", expr));
			return true;
		}
		return false;
	}
	private static boolean uniqueCombination(Combination c)
	{
		for (int i = 0; i < combinations.size(); i++)
		{
			Combination s = (Combination) combinations.get(i);
			if (c.expression.equals(s.getExpression()))
			{
				s.addWeight();
				return false;
			}
			if ("abcd".equals(c.template) && "abcd".equals(s.template))
			{
				s.addWeight();
				return false;
			}
			if (("(abc)d".equals(c.template) && "(abc)d".equals(s.template) && c.getD() == s.getD() && c.getOp3() == s.getOp3())
			||  ("(abc)d".equals(c.template) && "a(bcd)".equals(s.template) && c.getD() == s.getA() && c.getOp3() == s.getOp1())
			||  ("a(bcd)".equals(c.template) && "(abc)d".equals(s.template) && c.getA() == s.getD() && c.getOp1() == s.getOp3())
			||  ("a(bcd)".equals(c.template) && "a(bcd)".equals(s.template) && c.getA() == s.getA() && c.getOp1() == s.getOp1()))
			{
				s.addWeight();
				return false;
			}
			if ("a(bc)d".equals(c.template) && "a(bc)d".equals(s.template) 
			&& ((c.getD() == s.getD() && c.getA() == s.getA())
			||	(c.getA() == s.getD() && c.getD() == s.getA()))	
			&& (calcInParens(c.getB(),c.getOp2(),c.getC())) ==
				calcInParens(s.getB(),s.getOp2(),s.getC())) 
			{
				s.addWeight();
				return false;
			}
			if ("(ab)cd".equals(c.template) && "(ab)cd".equals(s.template) 
			&& ((c.getD() == s.getD() && c.getC() == s.getC())
			||	(c.getC() == s.getD() && c.getD() == s.getC()))	
			&& (calcInParens(c.getA(),c.getOp1(),c.getB())) ==
				calcInParens(s.getA(),s.getOp1(),s.getB())) 
			{
				s.addWeight();
				return false;
			}
			if ("ab(cd)".equals(c.template) && "ab(cd)".equals(s.template) 
			&& ((c.getB() == s.getB() && c.getA() == s.getA())
			||	(c.getA() == s.getB() && c.getB() == s.getA()))	
			&& (calcInParens(c.getC(),c.getOp3(),c.getD())) ==
				calcInParens(s.getC(),s.getOp3(),s.getD())) 
			{
				s.addWeight();
				return false;
			}
			if ("(ab)(cd)".equals(c.template) && "(ab)(cd)".equals(s.template)
			&& c.getOp2() == s.getOp2()
			&& (tmEvaluator.evaluate(c.getA() + OPERATORS[c.getOp1()] + c.getB()) == tmEvaluator.evaluate(s.getA() + OPERATORS[s.getOp1()] + s.getB())
			&&  tmEvaluator.evaluate(c.getC() + OPERATORS[c.getOp3()] + c.getD()) == tmEvaluator.evaluate(s.getC() + OPERATORS[s.getOp3()] + s.getD()))
			|| (tmEvaluator.evaluate(c.getA() + OPERATORS[c.getOp1()] + c.getB()) == tmEvaluator.evaluate(s.getC() + OPERATORS[s.getOp3()] + s.getD())
			&&  tmEvaluator.evaluate(c.getC() + OPERATORS[c.getOp3()] + c.getD()) == tmEvaluator.evaluate(s.getA() + OPERATORS[s.getOp1()] + s.getB())))
			{
				s.addWeight();
				return false;
			}
		}	
		return true;
	}
}
