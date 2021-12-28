import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Employee {
    private JPanel Main;
    private JTextField name;
    private JTextField salary;
    private JTextField mobile;
    private JButton saveButton;
    private JTable table1;
    private JButton updateButton;
    private JButton deleteButton;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Employee");
        frame.setContentPane(new Employee().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private JTextField employeeId;
    private JScrollPane table_1;
    private JButton searchButton;

    Connection con;
    PreparedStatement pst;

    public void connect()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_employee", "pmauser","1234");
            System.out.println("Success");
        }
        catch (ClassNotFoundException ex)
        {
            ex.printStackTrace();
            System.out.println("Failed 1");
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            System.out.println("Failed 2");
        }
    }

    public void table_load()
    {
        try
        {
            pst = con.prepareStatement("select * from employee");
            ResultSet rs = pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public Employee() {
        connect();
        table_load();
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String employeeName, employeeSalary, employeeMobile;

                employeeName = name.getText();
                employeeSalary = salary.getText();
                employeeMobile = mobile.getText();

                try {
                    pst = con.prepareStatement("insert into employee(name,salary,mobile) values(?,?,?)");
                    pst.setString(1, employeeName);
                    pst.setString(2, employeeSalary);
                    pst.setString(3, employeeMobile);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Added!");
                    table_load();
                    name.setText("");
                    salary.setText("");
                    mobile.setText("");
                    name.requestFocus();
                }

                catch (SQLException e1)
                {
                    e1.printStackTrace();
                }
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    String empid = employeeId.getText();

                    pst = con.prepareStatement("select name, salary, mobile from employee where id = ?");
                    pst.setString(1, empid);
                    ResultSet rs = pst.executeQuery();

                    if(rs.next()==true)
                    {
                        String empname = rs.getString(1);
                        String emsalary = rs.getString(2);
                        String emmobile = rs.getString(3);

                        name.setText(empname);
                        salary.setText(emsalary);
                        mobile.setText(emmobile);

                    }
                    else
                    {
                        name.setText("");
                        salary.setText("");
                        mobile.setText("");
                        JOptionPane.showMessageDialog(null,"Invalid Employee No");
                    }
                }
                catch (SQLException ex)
                {
                    ex.printStackTrace();
                }
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String empid, employeeName, employeeSalary, employeeMobile;
                employeeName = name.getText();
                employeeSalary = salary.getText();
                employeeMobile = mobile.getText();
                empid = employeeId.getText();

                try {
                    pst = con.prepareStatement("update employee set name = ?, salary = ?, mobile = ? where id = ?");
                    pst.setString(1, employeeName);
                    pst.setString(2, employeeSalary);
                    pst.setString(3, employeeMobile);
                    pst.setString(4, empid);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Update!");
                    table_load();
                    name.setText("");
                    salary.setText("");
                    mobile.setText("");
                    name.requestFocus();
                } catch (SQLException e1)
                {
                    e1.printStackTrace();
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String empid;
                empid = employeeId.getText();

                try {
                    pst = con.prepareStatement("delete from employee  where id = ?");

                    pst.setString(1, empid);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Delete Successfully!");
                    table_load();
                    name.setText("");
                    salary.setText("");
                    mobile.setText("");
                    name.requestFocus();
                }

                catch (SQLException e1)
                {
                    e1.printStackTrace();
                }
            }
        });
    }
}
