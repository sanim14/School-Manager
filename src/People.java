public class People
{
    private String firstName;
    private String lastName;
    private int iD;

    public People (String fName, String lName, int id)
    {
        firstName = fName;
        lastName = lName;
        iD = id;
    }

    public void setFName (String firstName)
    {
        this.firstName = firstName;
    }

    public void setLName (String lastName)
    {
        this.lastName = lastName;
    }

    public String getFName()
    {
        return firstName;
    }

    public String getLName()
    {
        return lastName;
    }

    public int getID()
    {
        return iD;
    }

    public String toString()
    {
        return iD + " - " + lastName + ", " + firstName;
    }
}
