public class Section
{
    private int teacher_iD;
    private int course_iD;
    private int iD;

    public Section (int teacher_ID, int course_ID, int id)
    {
        teacher_iD = teacher_ID;
        course_iD = course_ID;
        iD = id;
    }

    public void setID (int iD)
    {
        this.iD = iD;
    }

    public int getID()
    {
        return iD;
    }

    public int getCourse_iD ()
    {
        return course_iD;
    }

    public int getTeacher_iD()
    {
        return teacher_iD;
    }

    public String toString()
    {
        return iD + " - " + course_iD + ", " + teacher_iD;
    }
}
