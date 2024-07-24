import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class SQLFrame extends JFrame
{
    JScrollPane s;
    ArrayList<People> p = new ArrayList<>();
    ArrayList<Courses> courseArray = new ArrayList<>();

    public SQLFrame(String title) {
        super(title);
        JFrame frame = new JFrame("SQL_Frame");

        JPanel panel = new JPanel();
        frame.setSize(650, 500);
        frame.setLayout(null);

        JList people = new JList();
        JList studentL =  new JList();
        JList courseL =  new JList();
        courseL.setBounds(10, 10, 250, 440);
        studentL.setBounds(10, 10, 250, 440);
        people.setBounds(10, 10, 250, 440);

        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu view = new JMenu("View");
        JMenu help = new JMenu("Help");
        frame.add(file);
        file.setVisible(true);
        frame.add(view);
        view.setVisible(true);
        frame.add(help);
        help.setVisible(true);

        JMenuItem exportData = new JMenuItem("Export Data");
        JMenuItem importData = new JMenuItem("Import Data");
        JMenuItem purge = new JMenuItem("Purge");
        JMenuItem exit = new JMenuItem("Exit");
        file.add(exportData);
        file.add(importData);
        file.add(purge);
        file.add(exit);

        JMenuItem teacher = new JMenuItem("Teacher");
        JMenuItem student = new JMenuItem("Student");
        JMenuItem course = new JMenuItem("Course");
        JMenuItem section = new JMenuItem("Section");
        view.add(teacher);
        view.add(student);
        view.add(course);
        view.add(section);

        JMenuItem about = new JMenuItem("About");
        help.add(about);

        menuBar.add(file);
        menuBar.add(view);
        menuBar.add(help);
        frame.setJMenuBar(menuBar);

        frame.add(panel);

        exportData.addActionListener(i ->
        {
            createNewFileT();
            createNewFileS();
            createNewFileC();
            updateTeacherFile(createNewFileT());
            updateCourseFile(createNewFileC());
            JOptionPane.showMessageDialog(frame, "Congratulations! You have successfully exported!");

        });

        importData.addActionListener(e->
        {
            loadData(createNewFileT());
            JFileChooser j = new JFileChooser();
            j.showSaveDialog(null);
            JOptionPane.showMessageDialog(frame, "Congratulations! You have successfully imported!");
        });

        exit.addActionListener(e->
        {
            int x = 0;
            System.exit(x = 0);
        });

        purge.addActionListener(e ->
        {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection con=DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/p2","root","password");
                Statement stmt = con.createStatement();

                stmt.execute("DROP TABLE teacher");
                stmt.execute("DROP TABLE student");
                stmt.execute("DROP TABLE course");
                con.close();
            }catch(Exception c)
            {
                System.out.println(c);
            }

            int x = 0;
            System.exit(x = 0);
        });

        about.addActionListener(e ->
        {
            JOptionPane.showMessageDialog(frame, "IntelliJ IDEA 2022.2 (Community Edition)\n" +
                    "Build #IC-222.3345.118, built on July 26, 2022\n" +
                    "Runtime version: 17.0.3+7-b469.32 amd64\n" +
                    "VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.\n" +
                    "Windows 10 10.0\n" +
                    "GC: G1 Young Generation, G1 Old Generation\n" +
                    "Memory: 2034M\n" +
                    "Cores: 4\n" +
                    "\n" +
                    "Kotlin: 222-1.7.10-release-334-IJ3345.118");
        });

        student.addActionListener(e ->
        {

            panel.removeAll();

            people.setVisible(false);
            panel.add(studentL);
            studentL.setVisible(true);
            ArrayList<People> students = new ArrayList<>();
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection con=DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/p2","root","password");
                Statement stmt = con.createStatement();

                stmt.execute("CREATE TABLE IF NOT EXISTS student(\n" +
                        "id INTEGER NOT NULL AUTO_INCREMENT,\n" +
                        "first_name TEXT,\n" +
                        "last_name TEXT,\n" +
                        "PRIMARY KEY(id)\n" +
                        ");\n");

                students.clear();
                ResultSet resultSet = stmt.executeQuery("SELECT * FROM student");
                while (resultSet.next()) {
                    students.add(new People(resultSet.getString("first_name"), resultSet.getString("last_name"), resultSet.getInt("id")));
                }

                studentL.setListData(students.toArray());

                for (int x = 0; x <= 1; x++)
                {
                    int y = 25;
                    JTextField display = new JTextField();
                    display.setBounds(300, y + 40*(x), 135, 23);
                    display.setBorder(new LineBorder(Color.black, 2));
                    display.setEditable(false);
                    panel.add(display);

                    if (x == 0)
                    {
                        display.setText("Student First Name: ");
                    }
                    else if (x == 1)
                    {
                        display.setText("Student Last Name: ");
                    }
                    display.setVisible(true);
                }

                panel.setSize(650, 500);
                panel.setLayout(null);

                JTextField fName = new JTextField (" ");
                fName.setBounds(480, 25, 135, 23);
                fName.setBorder(new LineBorder(Color.black, 1));
                fName.setEditable(true);
                panel.add(fName);

                JTextField lName = new JTextField (" ");
                lName.setBounds(480, 65, 135, 23);
                lName.setBorder(new LineBorder(Color.black, 1));
                lName.setEditable(true);
                panel.add(lName);


                JButton button = new JButton("Add");
                button.setBounds(310, 130, 130, 23);
                panel.add(button);
                button.setVisible(true);

                JButton b = new JButton("Save Changes");
                b.setBounds(310, 160, 130, 23);
                panel.add(b);
                b.setVisible(true);

                JButton c = new JButton("Delete");
                c.setBounds(480, 160, 130, 23);
                panel.add(c);
                c.setVisible(true);

                JButton n = new JButton("New");
                n.setBounds(480, 130, 130, 23);
                panel.add(n);
                n.setVisible(true);


                c.addActionListener(k ->
                {
                    People selectedPerson = students.remove(studentL.getSelectedIndex());
                    try{
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection co=DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/p2","root","password");
                        Statement st = co.createStatement();

                        String sql = "DELETE FROM student WHERE first_name = ? AND last_name = ?";
                        PreparedStatement statement = co.prepareStatement(sql);
                        statement.setString(1, fName.getText());
                        statement.setString(2, lName.getText());
                        statement.executeUpdate();

                        students.clear();
                        ResultSet rs = st.executeQuery("SELECT * FROM student");
                        while (rs.next()) {
                            students.add(new People(rs.getString("first_name"), rs.getString("last_name"), rs.getInt("id")));
                        }

                        studentL.setListData(students.toArray());
                    }catch(Exception d)
                    {
                        System.out.println(d);
                    }


                    Collections.sort(students, new Comparator<People>() {
                        @Override
                        public int compare(People p1, People p2) {
                            return Integer.compare(p1.getID(), p2.getID());
                        }
                    });

                    fName.setText("");
                    lName.setText("");
                });

                n.addActionListener(i ->
                {
                    studentL.clearSelection();
                    fName.setText("");
                    lName.setText("");
                });

                studentL.addListSelectionListener(o->
                {
                    People person = students.get(0);
                    for (int x = 0; x < students.size(); x++)
                    {
                        if (studentL.getSelectedValue() != null && studentL.getSelectedValue().toString().equals(students.get(x).toString()))
                        {
                            person = students.get(x);
                            break;
                        }
                    }

                    fName.setText(person.getFName());
                    lName.setText(person.getLName());
                });

                b.addActionListener(k ->
                {
                    People selectedPerson = students.remove(studentL.getSelectedIndex());
                    People newPerson = new People(fName.getText(), lName.getText(), selectedPerson.getID());
                    students.add(newPerson);
                    try{
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection co=DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/p2","root","password");
                        Statement st = co.createStatement();

                        String sql = "UPDATE student SET first_name = ?, last_name = ? WHERE id = ?";
                        PreparedStatement statement = co.prepareStatement(sql);
                        statement.setString(1, fName.getText());
                        statement.setString(2, lName.getText());
                        statement.setInt(3, newPerson.getID());
                        statement.executeUpdate();

                        students.clear();
                        ResultSet rs = st.executeQuery("SELECT * FROM student");
                        while (rs.next()) {
                            students.add(new People(rs.getString("first_name"), rs.getString("last_name"), rs.getInt("id")));
                        }

                        studentL.setListData(students.toArray());
                    }catch(Exception d)
                    {
                        System.out.println(d);
                    }


                    Collections.sort(students, new Comparator<People>() {
                        @Override
                        public int compare(People p1, People p2) {
                            return Integer.compare(p1.getID(), p2.getID());
                        }
                    });

                    fName.setText("");
                    lName.setText("");
                });

                button.addActionListener(l ->
                {
                    try{
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection co=DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/p2","root","password");
                        Statement st = co.createStatement();

                        st.execute("CREATE TABLE IF NOT EXISTS student(\n" +
                                "id INTEGER NOT NULL AUTO_INCREMENT,\n" +
                                "first_name TEXT,\n" +
                                "last_name TEXT,\n" +
                                "PRIMARY KEY(id)\n" +
                                ");\n");

                        String fN = fName.getText();
                        String lN = lName.getText();
                        PreparedStatement pstmt = co.prepareStatement(
                                "INSERT INTO student (first_name, last_name) VALUES (?, ?)");
                        pstmt.setString(1, fN);
                        pstmt.setString(2, lN);
                        pstmt.executeUpdate();
                        pstmt.close();

                        students.clear();
                        ResultSet rs = st.executeQuery("SELECT * FROM student");
                        while (rs.next()) {
                            students.add(new People(rs.getString("first_name"), rs.getString("last_name"), rs.getInt("id")));
                        }

                        studentL.setListData(students.toArray());
                    }catch(Exception d)
                    {
                        System.out.println(d);
                    }
                });

            }catch(Exception c)
            {
                System.out.println(c);
            }
        });

        course.addActionListener(e ->
        {
            panel.removeAll();

            people.setVisible(false);
            panel.add(courseL);
            courseL.setVisible(true);

            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection con=DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/p2","root","password");
                Statement stmt = con.createStatement();

                stmt.execute("CREATE TABLE IF NOT EXISTS course(\n" +
                        "id INTEGER NOT NULL AUTO_INCREMENT,\n" +
                        "title TEXT NOT NULL,\n" +
                        "type INTEGER NOT NULL,\n" +
                        "PRIMARY KEY(id)\n" +
                        ");\n");

                courseArray.clear();
                ResultSet resultSet = stmt.executeQuery("SELECT * FROM course");
                while (resultSet.next()) {
                    courseArray.add(new Courses(resultSet.getString("title"), resultSet.getInt("type"), resultSet.getInt("id")));
                }

                courseL.setListData(courseArray.toArray());

                for (int x = 0; x <= 1; x++)
                {
                    int y = 25;
                    JTextField display = new JTextField();
                    display.setBounds(300, y + 40*(x), 135, 23);
                    display.setBorder(new LineBorder(Color.black, 2));
                    display.setEditable(false);
                    panel.add(display);

                    if (x == 0)
                    {
                        display.setText("Course Title: ");
                    }
                    else if (x == 1)
                    {
                        display.setText("Course Type: ");
                    }
                    display.setVisible(true);
                }

                panel.setSize(650, 500);
                panel.setLayout(null);

                JTextField titleC = new JTextField (" ");
                titleC.setBounds(480, 25, 135, 23);
                titleC.setBorder(new LineBorder(Color.black, 1));
                titleC.setEditable(true);
                panel.add(titleC);

                JComboBox<Integer> terms = new JComboBox<Integer>();
                terms.setBounds(480, 65, 135, 23);
                terms.addItem(0);
                terms.addItem(1);
                terms.addItem(2);
                panel.add(terms);


                JButton button = new JButton("Add");
                button.setBounds(310, 130, 130, 23);
                panel.add(button);
                button.setVisible(true);

                JButton b = new JButton("Save Changes");
                b.setBounds(310, 160, 130, 23);
                panel.add(b);
                b.setVisible(true);

                JButton c = new JButton("Delete");
                c.setBounds(480, 160, 130, 23);
                panel.add(c);
                c.setVisible(true);

                JButton n = new JButton("New");
                n.setBounds(480, 130, 130, 23);
                panel.add(n);
                n.setVisible(true);


                c.addActionListener(k ->
                {
                    Courses selectedPerson = courseArray.remove(courseL.getSelectedIndex());
                    try{
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection co=DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/p2","root","password");
                        Statement st = co.createStatement();

                        String sql = "DELETE FROM course WHERE title = ? AND type = ?";
                        PreparedStatement statement = co.prepareStatement(sql);
                        statement.setString(1, titleC.getText());
                        statement.setInt(2, (int) terms.getSelectedItem());
                        statement.executeUpdate();

                        courseArray.clear();
                        ResultSet rs = st.executeQuery("SELECT * FROM course");
                        while (rs.next()) {
                            courseArray.add(new Courses(rs.getString("title"), rs.getInt("type"), rs.getInt("id")));
                        }

                        courseL.setListData(courseArray.toArray());
                    }catch(Exception d)
                    {
                        System.out.println(d);
                    }


                    Collections.sort(courseArray, new Comparator<Courses>() {
                        @Override
                        public int compare(Courses p1, Courses p2) {
                            return Integer.compare(p1.getID(), p2.getID());
                        }
                    });

                    titleC.setText("");
                    terms.setSelectedItem("");

                });

                n.addActionListener(i ->
                {
                    courseL.clearSelection();
                    titleC.setText("");
                    terms.setSelectedItem("");
                });

                courseL.addListSelectionListener(o->
                {
                    Courses person = courseArray.get(0);
                    for (int x = 0; x < courseArray.size(); x++)
                    {
                        if (courseL.getSelectedValue() != null && courseL.getSelectedValue().toString().equals(courseArray.get(x).toString()))
                        {
                            person = courseArray.get(x);
                            break;
                        }
                    }

                    titleC.setText(person.getTitle());
                    terms.setSelectedItem(person.getID());
                });

                b.addActionListener(k ->
                {
                    Courses selectedPerson = courseArray.remove(courseL.getSelectedIndex());
                    Courses newPerson = new Courses(titleC.getText(), (int) terms.getSelectedItem(), selectedPerson.getID());
                    courseArray.add(newPerson);
                    try{
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection co=DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/p2","root","password");
                        Statement st = co.createStatement();

                        String sql = "UPDATE course SET title = ?, type = ? WHERE id = ?";
                        PreparedStatement statement = co.prepareStatement(sql);
                        statement.setString(1, titleC.getText());
                        statement.setInt(2, (int) terms.getSelectedItem());
                        statement.setInt(3, newPerson.getID());
                        statement.executeUpdate();

                        courseArray.clear();
                        ResultSet rs = st.executeQuery("SELECT * FROM course");
                        while (rs.next()) {
                            courseArray.add(new Courses(rs.getString("title"), rs.getInt("type"), rs.getInt("id")));
                        }

                        courseL.setListData(courseArray.toArray());
                    }catch(Exception d)
                    {
                        System.out.println(d);
                    }


                    Collections.sort(courseArray, new Comparator<Courses>() {
                        @Override
                        public int compare(Courses p1, Courses p2) {
                            return Integer.compare(p1.getID(), p2.getID());
                        }
                    });

                    titleC.setText("");
                    terms.setSelectedItem("");
                });

                button.addActionListener(l ->
                {
                    try{
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection co=DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/p2","root","password");
                        Statement st = co.createStatement();

                        st.execute("CREATE TABLE IF NOT EXISTS course(\n" +
                                "id INTEGER NOT NULL AUTO_INCREMENT,\n" +
                                "first_name TEXT,\n" +
                                "last_name TEXT,\n" +
                                "PRIMARY KEY(id)\n" +
                                ");\n");

                        String fN = titleC.getText();
                        int lN = (int) terms.getSelectedItem();
                        PreparedStatement pstmt = co.prepareStatement(
                                "INSERT INTO course (title, type) VALUES (?, ?)");
                        pstmt.setString(1, fN);
                        pstmt.setInt(2, lN);
                        pstmt.executeUpdate();
                        pstmt.close();

                        courseArray.clear();
                        ResultSet rs = st.executeQuery("SELECT * FROM course");
                        while (rs.next()) {
                            courseArray.add(new Courses(rs.getString("title"), rs.getInt("type"), rs.getInt("id")));
                        }

                        courseL.setListData(courseArray.toArray());
                    }catch(Exception d)
                    {
                        System.out.println(d);
                    }
                });
            }catch(Exception c)
            {
                System.out.println(c);
            }

        });

        section.addActionListener(r ->
        {
            panel.removeAll();
            panel.add(people);
            people.setVisible(true);
            ArrayList<Section> peeps = new ArrayList<>();

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/p2", "root", "password");
                Statement stmt = con.createStatement();

                stmt.execute("CREATE TABLE IF NOT EXISTS section(\n" +
                        "id INTEGER NOT NULL AUTO_INCREMENT,\n" +
                        "course_id INTEGER NOT NULL,\n" +
                        "teacher_id INTEGER NOT NULL,\n" +
                        "PRIMARY KEY(id)\n" +
                        ");\n");

                peeps.clear();
                ResultSet resultSet = stmt.executeQuery("SELECT * FROM section");
                while (resultSet.next()) {
                    peeps.add(new Section(resultSet.getInt("teacher_id"), resultSet.getInt("course_id"), resultSet.getInt("ID")));
                }

                people.setListData(peeps.toArray());

                for (int x = 0; x <= 1; x++)
                {
                    int y = 25;
                    JTextField display = new JTextField();
                    display.setBounds(300, y + 40 * (x), 135, 23);
                    display.setBorder(new LineBorder(Color.black, 2));
                    display.setEditable(false);
                    panel.add(display);

                    if (x == 0) {
                        display.setText("Choose Course: ");
                    } else if (x == 1) {
                        display.setText("Choose Teacher: ");
                    }
                    display.setVisible(true);
                }

                JComboBox<String> teach = new JComboBox<String>();
                teach.setBounds(480, 65, 135, 23);
                for (int x = 0; x < p.size(); x++)
                {
                    teach.addItem(p.get(x).getFName());
                }
                panel.add(teach);

                JComboBox<String> courseTerm = new JComboBox<String>();
                courseTerm.setBounds(480, 25, 135, 23);
                for (int x = 0; x < courseArray.size(); x++)
                {
                    courseTerm.addItem(courseArray.get(x).getTitle());
                }
                panel.add(courseTerm);

                JButton button = new JButton("Add");
                button.setBounds(310, 130, 130, 23);
                panel.add(button);
                button.setVisible(true);

                button.addActionListener(w ->
                {
                    try{
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection co=DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/p2","root","password");
                        Statement st = co.createStatement();

                        st.execute("CREATE TABLE IF NOT EXISTS section(\n" +
                                "id INTEGER NOT NULL AUTO_INCREMENT,\n" +
                                "course_id INTEGER NOT NULL,\n" +
                                "teacher_id INTEGER NOT NULL,\n" +
                                "PRIMARY KEY(id)\n" +
                                ");\n");

                        String fN = (String) teach.getSelectedItem();
                        int fNInt = 0;
                        int lNInt = 0;
                        String lN = (String) courseTerm.getSelectedItem();

                        for (int x = 0; x < p.size(); x++)
                        {
                            if (fN.equals(p.get(x).getFName()))
                            {
                                fNInt = p.get(x).getID();
                            }
                        }

                        for (int x = 0; x < courseArray.size(); x++)
                        {
                            if (lN.equals(courseArray.get(x).getTitle()))
                            {
                                lNInt = courseArray.get(x).getID();
                            }
                        }

                        PreparedStatement pstmt = co.prepareStatement(
                                "INSERT INTO section (course_id, teacher_id) VALUES (?, ?)");
                        pstmt.setInt(1, lNInt);
                        pstmt.setInt(2, fNInt);
                        pstmt.executeUpdate();
                        pstmt.close();

                        peeps.clear();
                        ResultSet rs = st.executeQuery("SELECT * FROM section");
                        while (rs.next()) {
                            peeps.add(new Section(rs.getInt("teacher_id"), rs.getInt("course_id"), rs.getInt("ID")));
                        }

                        people.setListData(peeps.toArray());
                    }catch(Exception d)
                    {
                        System.out.println(d);
                    }
                });

                JButton b = new JButton("Save Changes");
                b.setBounds(310, 160, 130, 23);
                panel.add(b);
                b.setVisible(true);

                JButton c = new JButton("Delete");
                c.setBounds(480, 160, 130, 23);
                panel.add(c);
                c.setVisible(true);

                JButton n = new JButton("New");
                n.setBounds(480, 130, 130, 23);
                panel.add(n);
                n.setVisible(true);

                n.addActionListener(i ->
                {
                    people.clearSelection();
                    teach.setSelectedItem("");
                    courseTerm.setSelectedItem("");
                });

                c.addActionListener(k ->
                {
                    Section selectedPerson = peeps.remove(people.getSelectedIndex());
                    try{
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection co=DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/p2","root","password");
                        Statement st = co.createStatement();

                        String fN = (String) teach.getSelectedItem();
                        int fNInt = 0;
                        int lNInt = 0;
                        String lN = (String) courseTerm.getSelectedItem();

                        for (int x = 0; x < p.size(); x++)
                        {
                            if (fN.equals(p.get(x).getFName()))
                            {
                                fNInt = p.get(x).getID();
                            }
                        }

                        for (int x = 0; x < courseArray.size(); x++)
                        {
                            if (lN.equals(courseArray.get(x).getTitle()))
                            {
                                lNInt = courseArray.get(x).getID();
                            }
                        }
                        String sql = "DELETE FROM section WHERE course_id = ? AND teacher_id = ?";
                        PreparedStatement statement = co.prepareStatement(sql);
                        statement.setInt(1, lNInt);
                        statement.setInt(2, fNInt);
                        statement.executeUpdate();

                        peeps.clear();
                        ResultSet rs = st.executeQuery("SELECT * FROM section");
                        while (rs.next()) {
                            peeps.add(new Section(rs.getInt("teacher_id"), rs.getInt("course_id"), rs.getInt("ID")));
                        }

                        people.setListData(peeps.toArray());
                        }catch(Exception d)
                        {
                            System.out.println(d);
                        }


                        Collections.sort(peeps, new Comparator<Section>() {
                        @Override
                        public int compare(Section p1, Section p2) {
                            return Integer.compare(p1.getID(), p2.getID());
                        }
                    });

                    b.addActionListener(v ->
                    {
                        Section selectedP = peeps.remove(people.getSelectedIndex());
                        String fN = (String) teach.getSelectedItem();
                        int fNInt = 0;
                        int lNInt = 0;
                        String lN = (String) courseTerm.getSelectedItem();

                        for (int x = 0; x < p.size(); x++)
                        {
                            if (fN.equals(p.get(x).getFName()))
                            {
                                fNInt = p.get(x).getID();
                            }
                        }

                        for (int x = 0; x < courseArray.size(); x++)
                        {
                            if (lN.equals(courseArray.get(x).getTitle()))
                            {
                                lNInt = courseArray.get(x).getID();
                            }
                        }
                        Section newPerson = new Section(fNInt, lNInt, selectedP.getID());
                        peeps.add(newPerson);
                        try{
                            Class.forName("com.mysql.jdbc.Driver");
                            Connection co=DriverManager.getConnection(
                                    "jdbc:mysql://localhost:3306/p2","root","password");
                            Statement st = co.createStatement();

                            String sql = "UPDATE section SET course_id = ?, teacher_id = ? WHERE id = ?";
                            PreparedStatement statement = co.prepareStatement(sql);
                            statement.setInt(1, lNInt);
                            statement.setInt(2, fNInt);
                            statement.setInt(3, selectedP.getID());
                            statement.executeUpdate();
                            statement.close();

                            peeps.clear();
                            ResultSet rs = st.executeQuery("SELECT * FROM section");
                            while (rs.next()) {
                                peeps.add(new Section(rs.getInt("teacher_id"), rs.getInt("course_id"), rs.getInt("ID")));
                            }

                            people.setListData(peeps.toArray());
                        }catch(Exception d)
                        {
                            System.out.println(d);
                        }


                        Collections.sort(peeps, new Comparator<Section>() {
                            @Override
                            public int compare(Section p1, Section p2) {
                                return Integer.compare(p1.getID(), p2.getID());
                            }
                        });

                    });

                });

            }catch (Exception c)
            {
                System.out.println(c);
            }

        });

        teacher.addActionListener(e ->
        {
            panel.removeAll();

            panel.add(people);
            people.setVisible(true);


            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection con=DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/p2","root","password");
                Statement stmt = con.createStatement();

                stmt.execute("CREATE TABLE IF NOT EXISTS teacher(\n" +
                        "id INTEGER NOT NULL AUTO_INCREMENT,\n" +
                        "first_name TEXT,\n" +
                        "last_name TEXT,\n" +
                        "PRIMARY KEY(id)\n" +
                        ");\n");

                p.clear();
                ResultSet resultSet = stmt.executeQuery("SELECT * FROM teacher");
                while (resultSet.next()) {
                    p.add(new People(resultSet.getString("first_name"), resultSet.getString("last_name"), resultSet.getInt("id")));
                }

                people.setListData(p.toArray());

                for (int x = 0; x <= 1; x++)
                {
                    int y = 25;
                    JTextField display = new JTextField();
                    display.setBounds(300, y + 40*(x), 135, 23);
                    display.setBorder(new LineBorder(Color.black, 2));
                    display.setEditable(false);
                    panel.add(display);

                    if (x == 0)
                    {
                        display.setText("Teacher First Name: ");
                    }
                    else if (x == 1)
                    {
                        display.setText("Teacher Last Name: ");
                    }
                    display.setVisible(true);
                }

                panel.setSize(650, 500);
                panel.setLayout(null);

                JTextField firstName = new JTextField (" ");
                firstName.setBounds(480, 25, 135, 23);
                firstName.setBorder(new LineBorder(Color.black, 1));
                firstName.setEditable(true);
                panel.add(firstName);

                JTextField lastName = new JTextField (" ");
                lastName.setBounds(480, 65, 135, 23);
                lastName.setBorder(new LineBorder(Color.black, 1));
                lastName.setEditable(true);
                panel.add(lastName);

                JButton button = new JButton("Add");
                button.setBounds(310, 130, 130, 23);
                panel.add(button);
                button.setVisible(true);

                JButton b = new JButton("Save Changes");
                b.setBounds(310, 160, 130, 23);
                panel.add(b);
                b.setVisible(true);

                JButton c = new JButton("Delete");
                c.setBounds(480, 160, 130, 23);
                panel.add(c);
                c.setVisible(true);

                JButton n = new JButton("New");
                n.setBounds(480, 130, 130, 23);
                panel.add(n);
                n.setVisible(true);


                c.addActionListener(k ->
                {
                    People selectedPerson = p.remove(people.getSelectedIndex());
                    try{
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection co=DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/p2","root","password");
                        Statement st = co.createStatement();

                        String sql = "DELETE FROM teacher WHERE first_name = ? AND last_name = ?";
                        PreparedStatement statement = co.prepareStatement(sql);
                        statement.setString(1, firstName.getText());
                        statement.setString(2, lastName.getText());
                        statement.executeUpdate();

                        p.clear();
                        ResultSet rs = st.executeQuery("SELECT * FROM teacher");
                        while (rs.next()) {
                            p.add(new People(rs.getString("first_name"), rs.getString("last_name"), rs.getInt("id")));
                        }

                        people.setListData(p.toArray());
                    }catch(Exception d)
                    {
                        System.out.println(d);
                    }


                    Collections.sort(p, new Comparator<People>() {
                        @Override
                        public int compare(People p1, People p2) {
                            return Integer.compare(p1.getID(), p2.getID());
                        }
                    });

                    firstName.setText("");
                    lastName.setText("");

                    //change the value in the arraylist
                });

                n.addActionListener(i ->
                {
                    people.clearSelection();
                    firstName.setText("");
                    lastName.setText("");
                });

                people.addListSelectionListener(o->
                {
                    People person = p.get(0);
                    for (int x = 0; x < p.size(); x++)
                    {
                        if (people.getSelectedValue() != null && people.getSelectedValue().toString().equals(p.get(x).toString()))
                        {
                            person = p.get(x);
                            break;
                        }
                    }

                    firstName.setText(person.getFName());
                    lastName.setText(person.getLName());
                    // add save changes and delete contact button
                });

                b.addActionListener(k ->
                {
                    People selectedPerson = p.remove(people.getSelectedIndex());
                    People newPerson = new People(firstName.getText(), lastName.getText(), selectedPerson.getID());
                    p.add(newPerson);
                    try{
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection co=DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/p2","root","password");
                        Statement st = co.createStatement();

                        String sql = "UPDATE teacher SET first_name = ?, last_name = ? WHERE id = ?";
                        PreparedStatement statement = co.prepareStatement(sql);
                        statement.setString(1, firstName.getText());
                        statement.setString(2, lastName.getText());
                        statement.setInt(3, newPerson.getID());
                        statement.executeUpdate();

                        p.clear();
                        ResultSet rs = st.executeQuery("SELECT * FROM teacher");
                        while (rs.next()) {
                            p.add(new People(rs.getString("first_name"), rs.getString("last_name"), rs.getInt("id")));
                        }

                        people.setListData(p.toArray());
                    }catch(Exception d)
                    {
                        System.out.println(d);
                    }


                    Collections.sort(p, new Comparator<People>() {
                        @Override
                        public int compare(People p1, People p2) {
                            return Integer.compare(p1.getID(), p2.getID());
                        }
                    });

                    firstName.setText("");
                    lastName.setText("");

                    //change the value in the arraylist
                });

                button.addActionListener(l ->
                {
                    try{
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection co=DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/p2","root","password");
                        Statement st = co.createStatement();

                        st.execute("CREATE TABLE IF NOT EXISTS teacher(\n" +
                                "id INTEGER NOT NULL AUTO_INCREMENT,\n" +
                                "first_name TEXT,\n" +
                                "last_name TEXT,\n" +
                                "PRIMARY KEY(id)\n" +
                                ");\n");

                        String fN = firstName.getText();
                        String lN = lastName.getText();
                        PreparedStatement pstmt = co.prepareStatement(
                                "INSERT INTO teacher (first_name, last_name) VALUES (?, ?)");
                        pstmt.setString(1, fN);
                        pstmt.setString(2, lN);
                        pstmt.executeUpdate();
                        pstmt.close();

                        // Clear the ArrayList before adding new data
                        p.clear();
                        ResultSet rs = st.executeQuery("SELECT * FROM teacher");
                        while (rs.next()) {
                            p.add(new People(rs.getString("first_name"), rs.getString("last_name"), rs.getInt("id")));
                        }

                        people.setListData(p.toArray());
                    }catch(Exception d)
                    {
                        System.out.println(d);
                    }
                });
                //firstName.setVisible(false);
                //lastName.setVisible(false);
            }catch(Exception c)
            {
                System.out.println(c);
            }
        });

        //finished about and exit
        frame.setVisible(true);
    }

    public File createNewFileT()
    {
        try
        {
            File TeachertxtFile = new File("Teacher.txt");
            if(TeachertxtFile.createNewFile())
            {
                return TeachertxtFile;
            }
            else
            {
                return TeachertxtFile;
            }
        } catch(IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            File StudenttxtFile = new File("Student.txt");
            if(StudenttxtFile.createNewFile())
            {
                return StudenttxtFile;
            }
            else
            {
                return StudenttxtFile;
            }
        } catch(IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public File createNewFileS()
    {
        try
        {
            File StudenttxtFile = new File("Student.txt");
            if(StudenttxtFile.createNewFile())
            {
                return StudenttxtFile;
            }
            else
            {
                return StudenttxtFile;
            }
        } catch(IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public File createNewFileC()
    {
        try
        {
            File CoursetxtFile = new File("Course.txt");
            if(CoursetxtFile.createNewFile())
            {
                return CoursetxtFile;
            }
            else
            {
                return CoursetxtFile;
            }
        } catch(IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }


    public void updateTeacherFile(File file)
    {
        String data = "";
        for(int x = 0; x<p.size(); x++)
        {
            if(x==0)
            {
                data = p.get(x).getID() + "=x=x=" + p.get(x).getFName() + "=x=x=" + p.get(x).getLName();
            }
            else
            {
                data = data + "\n" + p.get(x).getID() + "=x=x=" + p.get(x).getFName() + "=x=x=" + p.get(x).getLName();
            }
        }
        try
        {
            FileWriter pen = new FileWriter(file.getPath(), false);

            pen.write(data);
            pen.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void updateCourseFile(File file)
    {
        String data = "";
        for(int x = 0; x<courseArray.size(); x++)
        {
            if(x==0)
            {
                data = courseArray.get(x).getID() + "=x=x=" + courseArray.get(x).getTitle() + "=x=x=" + courseArray.get(x).getType();
            }
            else
            {
                data = data + "\n" +  courseArray.get(x).getID() + "=x=x=" + courseArray.get(x).getTitle() + "=x=x=" + courseArray.get(x).getType();
            }
        }
        try
        {
            FileWriter pen = new FileWriter(file.getPath(), false);

            pen.write(data);
            pen.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void loadData(File file)
    {
        String data = "";
        try
        {
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine())
            {
                data = reader.nextLine();
                String[] split1 = data.split("=x=x=");
                //people.add(new Contact(split1[0],split1[1],split1[2], split1[3]));

            }
            reader.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


}
