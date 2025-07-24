package collegeadmission.com;

public class Admission
{
	private int formId;
	private int studentId;
	private String applicationDate;
	private String status;
	
	//Constructor
	public Admission(int formId,int studentId,String applicationDate,String status)
	{
		this.formId=formId;
		this.studentId=studentId;
		this.applicationDate=applicationDate;
		this.status=status;
	}
	//Getters
	public int getFormId()
	{
		return formId;
	}
	public void setFormId(int formId)
	{
		this.formId=formId;
	}
	
	
		public int getStudentId()
		{
			return studentId;
		}
		public void setStudentId(int studentId)
		{
			this.studentId=studentId;
		}
		public String getApplicationDate()
		{
			return applicationDate;
		}
		public void setApplicationDate(String applicationDate)
		{
			this.applicationDate=applicationDate;
	}
		public String getStatus()
		{
			return status;
		}
		public void setStatus(String status)
		{
			this.status=status;
		}
}