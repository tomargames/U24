public class Argument
{
	private int num;
	private boolean used;

	public Argument(int num)
	{
		this.num = num;
		this.used = false;
	}

	public int getNum()
	{
		return this.num;
	}

	public void setUsed(boolean used)
	{
		this.used = used;
	}

	public boolean getUsed()
	{
		return this.used;
	}
}

