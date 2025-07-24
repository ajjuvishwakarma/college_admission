package collegeadmission.com;

public class Documents
{
	private int documentId;
	private int studentId;
	private String documentType;
	private String filePath;
	private String uploadDate;
	
	public Documents(int documentId,int studentId, String documentType, String filePath,String uploadDate)
	{
		this.documentId=documentId;
		this.studentId=studentId;
		this.documentType=documentType;
		this.filePath=filePath;
		this.uploadDate=uploadDate;
	}
	//Getters and Setters
	public int getDocumentId()
	{
		return documentId;
	}
	public void setDocumentId(int documentId)
	{
		this.documentId=documentId;
	}
	public int getStudentId()
	{
		return studentId;
	}
	public void setStudentId(int studentId)
	{
		this.studentId=studentId;
	}
	public String getDocumentType()
	{
		return documentType;
	}
	public void setDocumentType(String documentType)
	{
		this.documentType=documentType;
	}
	public String getFilePath()
	{
		return filePath;
	}
	public void setFilePath(String filePath)
	{
		this.filePath=filePath;
	}
	public String getUploadDate()
	{
		return uploadDate;
	}
	public void setUploadDate(String uploadDate)
	{
		this.uploadDate=uploadDate;
	}
}