import java.applet.*;
import java.awt.Color;
import java.util.*;
// VERSION 1.2
// this update creates a static variable for the local environment on each machine
// 1.2 corrects a flaw in randomPicks

public class ToMarUtils
{ 
	private static String localMachine = "mom-pc";
//	private static String localMachine = "dell24";
	public static final Color toMarBackground = new Color (250,250,235); // 29 cream - ToMar background
	
	public static long startTimer()
	{
		return (new Date()).getTime();
	}
	public static long getElapsedSeconds(long startSec)
	{
		return ((new Date()).getTime() - startSec)/1000;
	}
	public static long getMilliSeconds(long startSec)
	{
		return ((new Date()).getTime() - startSec);
	}
	public static String displayTime(long seconds)
	{
		// format seconds into 00:00
		int hrs = (int) seconds/3600;
		int mins = (int) ((seconds/60) % 60);
		int secs = (int) (seconds % 60);
		return (formatNumber(hrs,2) + ":" + formatNumber(mins,2) + ":" + formatNumber(secs,2));
	}
	public static String[] vectorToStringArray(Vector v)
	{
		String[] str = new String[v.size()];
		for (int i = 0; i < v.size(); i++)
		{
			str[i] = (String) v.elementAt(i);
		}
		return str;
	}
	public static Color stringToColor(String paramValue)
	{
		int red;
		int green;
		int blue;

		red = (Integer.decode("0x" + paramValue.substring(0,2))).intValue();
		green = (Integer.decode("0x" + paramValue.substring(2,4))).intValue();
		blue = (Integer.decode("0x" + paramValue.substring(4,6))).intValue();
		System.out.println("red = " + red + ", green = " + green + ", blue = " + blue);	
		return new Color(red,green,blue);
	}
	private static int[] multipliers = {37, 19, 43, 61, 59, 11, 71, 17, 23, 41, 87, 9, 8, 7, 6};
	public static int getCheck(int highScore, String ts)
	{
		int check = 0;
		String scoreIn = "" + highScore;
		int max = (scoreIn.length() > multipliers.length) ? multipliers.length : scoreIn.length();
		for (int i = 0; i < max; i++)
		{
			check += (new Integer(scoreIn.substring(i, i + 1)).intValue()) * multipliers[i];
		}
		for (int i = 0; i < ts.length(); i++)
		{
			check += (new Integer(ts.substring(i, i + 1)).intValue()) * multipliers[i];
		}
		return check;
	}	
	public static int getRnd(int value)
	{
		return (int)(Math.random() * value);
	}    
	
	public static String getNiceDate(Calendar sysDate)
	{
		String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
		String[] months = {"January", "February", "March", "April", "May", "June", "July", 
				"August", "September", "October", "November", "December"};
		String dayOfWeek = days[sysDate.get(Calendar.DAY_OF_WEEK) - 1];
		String month = months[sysDate.get(Calendar.MONTH)];
		String time = formatNumber(sysDate.get(Calendar.HOUR_OF_DAY), 2) + ":" +
		formatNumber(sysDate.get(Calendar.MINUTE), 2) + ":" +
		formatNumber(sysDate.get(Calendar.SECOND), 2) + " (E";
		if (sysDate.get(Calendar.DST_OFFSET) > 0)
		{
			time += "DT)";
		}
		else
		{
			time += "ST)";
		}	
		return (dayOfWeek + ", " + month + " " + sysDate.get(Calendar.DATE) + ", " +
				sysDate.get(Calendar.YEAR) + " at " + time); 
	}	
	public static String getWeeklyDate()
	{	
		Calendar sysDate = Calendar.getInstance();
		int dw = sysDate.get(Calendar.DAY_OF_WEEK);
		// if it's not Monday, get date of previous Monday
		if  (dw == 1)
		{
			sysDate.add(Calendar.DATE, -6);
		}
		else if  (dw > 2)
		{
			sysDate.add(Calendar.DATE, (2 - dw));
		}
		int yy = sysDate.get(Calendar.YEAR);
		int mm = 1 + sysDate.get(Calendar.MONTH);
		int dd = sysDate.get(Calendar.DATE);
		return ("" + yy).substring(2,4) + formatNumber(mm,2) + formatNumber(dd,2);
	}	
	public static String formatNumber(int numberIn, int numberOfDigits)
	{
		String numberOut = "000000000" + numberIn;
		return numberOut.substring(numberOut.length() - numberOfDigits);
	}	
	public static String formatDecimals(double numberIn,int numberOfDecimals)
	{
		String num = "" + numberIn;
		String beginning = "";
		String decimals = "";
//		log("formatDecimals, numberIn is " + numberIn + ", decimals is " + numberOfDecimals);
		int len = num.length();
		int dec = num.indexOf(".");
		if (dec == -1)
		{
			return formatNumber((int) numberIn, len);
		}
		else if (dec > 0)
		{	
			beginning = num.substring(0, dec);
		}
		decimals = num.substring(dec + 1) + "000000000";
		String decDigits = decimals.substring(0, numberOfDecimals);
		if ("56789".indexOf(decimals.substring(numberOfDecimals, numberOfDecimals + 1)) > -1)
		{
			if (numberOfDecimals == 0)
			{
				int begInt = Integer.parseInt(beginning) + 1;
				beginning = "" + begInt;
			}
			else
			{	
				int decInt = Integer.parseInt(decDigits);
				decDigits = formatNumber(decInt + 1, numberOfDecimals);
			}	
		}
		if (numberOfDecimals == 0)
		{
			return beginning;
		}
		return (beginning + "." + decDigits);
	}	
	public static String getServer(Applet a)
	{
		String where = a.getDocumentBase().toString();
		if (where.indexOf(".com") > -1)
		{	
			return "http://www.tomargames.com";
		}
		return "http://" + localMachine + "/tomargames";
	}
	public static String getServletPath(Applet a)
	{
		String where = a.getCodeBase().toString();
		if (where.indexOf(".com") > -1)
		{	
			return "http://www.tomargames.com/servlets/";
		}
		return "http://" + localMachine + "/tomargames/servlet/";
	}
	public static String sendError(Applet a)
	{
		return getServer(a) + "/error.html";
	}
	public static String getAddWordServletString(Applet a, String name, String word)
	{
		return (getServletPath(a) +
			  	"tmAddWordServlet" +
				"?name=" + name +
				"&word=" + word);
	}
	public static String getHighScoreServletString(Applet a, String name, int score, int index, String returnURL)
	{
		String ts = getDateTimeStamp();
		return (getServletPath(a) +
			  	"tmHighServlet" +
				"?name=" + name +
				"&score=" + score +
				"&index=" + index +
				"&returnURL=" + returnURL +
				"&check=" + getCheck(score, ts) +
				"&ts=" + ts);
	}
	public static String postHighScore(Applet a, String name, int score, String game)
	{
		String ts = getDateTimeStamp();
		return (getServletPath(a) +
			  	"tm2007Servlet" +
				"?name=" + name +
				"&score=" + score +
				"&game=" + game +
				"&function=1" +
				"&check=" + getCheck(score, ts) +
				"&ts=" + ts);
	}
	public static String logEntry(Applet a, String name, String message, String game)
	{
		String ts = getDateTimeStamp();
		return (getServletPath(a) +
			  	"tm2007Servlet" +
				"?name=" + name +
				"&message=" + message +
				"&game=" + game +
				"&function=2" +
				"&ts=" + ts);
	}
	private static int maxPoints(int scoreIn)
	{
		if (scoreIn < 0)
		{
			return 2147483647;
		}
		return scoreIn;
	}
	public static boolean isMaxPoints(int scoreIn)
	{
		if (scoreIn == 2147483647)
		{
			return true;
		}
		return false;
	}
	public static int addToPoints(int scoreIn, int scoreToAdd)
	{
		return maxPoints(scoreIn + scoreToAdd);
	}
	public static int multiplyPoints(int scoreIn, int scoreToMultiplyBy)
	{
		return maxPoints(scoreIn * scoreToMultiplyBy);
	}
	public static void main (String[] args)
	{
		stringToColor("FAFAEB");
	}
	public static int getFontSize(String browser, int fontIn)
	{
		int fontsize = fontIn;
		// if it's not IE or if it's IE with a 1.5 JVM
		if (browser.indexOf("Microsoft") == -1  ||
				((System.getProperty("java.version")).indexOf("1.5") > -1))
		{
			fontsize -= 2;
		}
		return fontsize;
	}	
	public static void arraySort(int[] arrayIn)
	{
		boolean flips = true;
		while (flips == true)
		{
			flips = false;
			for (int i = 0; i < arrayIn.length - 1; i++)
			{
				if (arrayIn[i] > arrayIn[i + 1])
				{
					int temp = arrayIn[i];
					int temp1 = arrayIn[i + 1];
					arrayIn[i] = temp1;
					arrayIn[i + 1] = temp;
					flips = true;
				}
			}
		}	
	}	
	public static void arraySort(String[] arrayIn)
	{
		boolean flips = true;
		while (flips == true)
		{
			flips = false;
			for (int i = 0; i < arrayIn.length - 1; i++)
			{
				if (arrayIn[i].compareTo(arrayIn[i + 1]) > -1)
				{
					String temp = arrayIn[i];
					String temp1 = arrayIn[i + 1];
					arrayIn[i] = temp1;
					arrayIn[i + 1] = temp;
					flips = true;
				}
			}
		}	
	}	
	public static int[] randomPicks(int universeSize, int numberToPick)
	{
		int[] returnArray = new int[numberToPick];
		StringBuffer pickString = new StringBuffer("");
		for (int i = 0; i < universeSize; i++)
		{
			pickString.append(ToMarUtils.formatNumber(i,3));
		}
		for (int i = 0; i < numberToPick; i++)
		{
			int idx = 3 * (ToMarUtils.getRnd((pickString.length() / 3))); // index on pickString
			returnArray[i] = Integer.parseInt((pickString.toString()).substring(idx, idx + 3));
			pickString = new StringBuffer((pickString.toString()).substring(0, idx) + (pickString.toString()).substring(idx + 3));
		}	
		return returnArray;
	}
	public static void arraySort(Object[] arrayIn, String methodIn)
	{
		boolean flips = true;
		while (flips)
		{
			String a = null;
			String b = null;
			flips = false;
			for (int i = 0; i < arrayIn.length - 1; i++)
			{
				try
				{
					a = (String) arrayIn[i].getClass().getMethod(methodIn, null).invoke(arrayIn[i], null);
					b = (String) arrayIn[i+1].getClass().getMethod(methodIn, null).invoke(arrayIn[i+1], null);
				}
				catch (Exception e)
				{
					System.out.println("ERROR!!! a = " + a + ", b = " + b + ", Error: " + e);
					break;
				}
				if (a.compareTo(b) > -1)
				{
					Object temp = arrayIn[i];
					Object temp1 = arrayIn[i + 1];
					arrayIn[i] = temp1;
					arrayIn[i + 1] = temp;
					flips = true;
				}
//				System.out.println("a = " + a + ", b = " + b + ", flips is " + flips);
			}
		}	
	}	
	public static String getDateTimeStamp()
	{
		Calendar sysDate = Calendar.getInstance();
		String month = formatNumber(sysDate.get(Calendar.MONTH) + 1, 2);
		String time = formatNumber(sysDate.get(Calendar.HOUR_OF_DAY), 2) +
			formatNumber(sysDate.get(Calendar.MINUTE), 2) + formatNumber(sysDate.get(Calendar.SECOND), 2);
		return (sysDate.get(Calendar.YEAR) + month + formatNumber(sysDate.get(Calendar.DATE),2) + time);
//		return sysDate.toString();
	}
	public static String escape(String s)
	{
		boolean fixed = true;
		while (fixed == true)
		{
			fixed = false;	
			int space = s.indexOf(" ");
			if (space > -1)
			{
				fixed = true;
				s = s.substring(0, space) + "%20" + s.substring(space + 3);
			}	
		}
		return s;
	}
	public static String unescape(String s)
	{
		s += "             ";
		boolean fixed = true;
		while (fixed == true)
		{
			fixed = false;	
			int space = s.indexOf("%20");
			if (space > -1)
			{
				fixed = true;
				s = s.substring(0, space) + " " + s.substring(space + 3);
			}	
			space = s.indexOf("%21");
			if (space > -1)
			{
				fixed = true;
				s = s.substring(0, space) + "!" + s.substring(space + 3);
			}	
		}
		return s.trim();
	}
	public static int absoluteValue(int numIn)
	{
		if (numIn < 0)
		{
			return numIn * -1;
		}
		return numIn;
	}
	public static String reverseString(String input)
	{
		StringBuffer sb = new StringBuffer("");
		for (int i = input.length() - 1; i > -1; i--)
		{
			sb.append(input.substring(i, i + 1));
		}
		return sb.toString();
	}
}
