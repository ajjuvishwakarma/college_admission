package collegeadmission.com;


public class course
{
	private int courseId;
	private String courseName;
	private String duration;
	private double totalFee;
	
	public course(int courseId, String courseName,String duration, double totalFee)
	{
		this.courseId=courseId;
		this.courseName=courseName;
		this.duration=duration;
		this.totalFee=totalFee;
	}
	@Override
	public String toString()
	{
		return "Course ID:" +courseId+"\n"
	  +"Course Name:"+courseName+"\n"+"Duration: "+duration+"\n"
	  +"Total Fee:₹"+totalFee+"\n"
	  +"------------------------\n";
	}
}