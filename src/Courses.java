public class Courses
{
    private String title;
    private int type;
    private int iD;

    public Courses (String titleC, int typeC, int id)
    {
        title = titleC;
        type = typeC;
        iD = id;
    }

    public int getID()
    {
        return iD;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public void setType (int type)
    {
        this.type = type;
    }

    public int getType()
    {
        return type;
    }

    public String getTitle()
    {
        return title;
    }


    public String toString()
    {
        if (type == 0)
        {
            return iD + " - " + title + ", Academic";
        }
        else if (type == 1)
        {
            return iD + " - " + title + ", KAP";
        }
        else
        {
            return iD + " - " + title + ", AP";
        }
    }
}
