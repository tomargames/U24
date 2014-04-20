import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;

public class displayPanel extends Panel
{
	private static final long serialVersionUID = -1994764497133406792L;
	private Label heading;
	private Label data;
	
	public displayPanel(String h)
	{
		heading = new Label(h);
		data = new Label();
		this.setBackground(tmColors.CREAM);
		heading.setFont(new Font("Verdana", Font.BOLD, 16));
		heading.setAlignment(Label.CENTER);
		data.setBackground(tmColors.WHITE);
		data.setFont(new Font("Verdana", Font.PLAIN, 32));
		data.setAlignment(Label.CENTER);
		data.setForeground(tmColors.DARKGREEN);
		this.setLayout(new GridLayout(2,1,10,10));
		this.add(heading);
		this.add(data);
	}
	public void turnRed()
	{
		data.setForeground(tmColors.RED);
	}
	public void setData(int dataIn)
	{
		data.setText("" + dataIn);
	}
}
