import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;


class Employee
{
    int id;
    String name;
    double salary;
    String department;


    Employee()
    {
        id = 0;
        name = "";
        salary = 0;
        department = "";
    }

    Employee(int id, String name, double salary, String department)
    {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.department = department;
    }


    Employee(Employee e)
    {
        id = e.id;
        name = e.name;
        salary = e.salary;
        department = e.department;
    }

    double netSalary()
    {
        return salary + 3000 - 1000;
    }

    String getData()
    {
        return id + "," + name + "," + salary + "," + department;
    }
}


public class EmployeePayroll extends JFrame implements ActionListener
{
    JTextField idField, nameField, salaryField;
    JComboBox<String> deptBox;
    JTextArea area;

    JButton addBtn, saveBtn, loadBtn, viewBtn;

    ArrayList<Employee> list = new ArrayList<Employee>();

    
    EmployeePayroll()
    {
        setTitle("Employee Payroll System");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));

        form.add(new JLabel("Employee ID:"));
        idField = new JTextField();
        form.add(idField);

        form.add(new JLabel("Employee Name:"));
        nameField = new JTextField();
        form.add(nameField);

        form.add(new JLabel("Basic Salary:"));
        salaryField = new JTextField();
        form.add(salaryField);

        form.add(new JLabel("Department:"));
        String[] departments = {"IT", "HR", "Sales", "Finance"};
        deptBox = new JComboBox<String>(departments);
        form.add(deptBox);

        add(form, BorderLayout.NORTH);

        JPanel buttons = new JPanel();

        addBtn = new JButton("Add Employee");
        saveBtn = new JButton("Save");
        loadBtn = new JButton("Load");
        viewBtn = new JButton("View All");

        buttons.add(addBtn);
        buttons.add(saveBtn);
        buttons.add(loadBtn);
        buttons.add(viewBtn);

        add(buttons, BorderLayout.CENTER);

        area = new JTextArea(10, 40);
        area.setEditable(false);

        add(new JScrollPane(area), BorderLayout.SOUTH);

        addBtn.addActionListener(this);
        saveBtn.addActionListener(this);
        loadBtn.addActionListener(this);
        viewBtn.addActionListener(this);

        setLocationRelativeTo(null);
        setVisible(true);
    }

   
    public void actionPerformed(ActionEvent e)
    {
        try
        {
            
            if (e.getSource() == addBtn)
            {
                int id = Integer.parseInt(idField.getText().trim());
                String name = nameField.getText().trim();
                double salary = Double.parseDouble(salaryField.getText().trim());
                String dept = (String) deptBox.getSelectedItem();

                if (id <= 0 || name.isEmpty() ||
                    salary < 0 || Double.isNaN(salary) ||
                    Double.isInfinite(salary) ||
                    name.contains(","))
                {
                    JOptionPane.showMessageDialog(this,
                        "Enter valid employee details. Do not use comma in name.");
                    return;
                }

              
                for (Employee emp : list)
                {
                    if (emp.id == id)
                    {
                        JOptionPane.showMessageDialog(this,
                            "This Employee ID already exists.");
                        return;
                    }
                }

                Employee emp = new Employee(id, name, salary, dept);

                
                Employee copy = new Employee(emp);
                list.add(copy);

                JOptionPane.showMessageDialog(this,
                    "Employee Added Successfully");

                area.setText("Net Salary: Rs. " + copy.netSalary());

                idField.setText("");
                nameField.setText("");
                salaryField.setText("");
            }

            
            else if (e.getSource() == saveBtn)
            {
                try (BufferedWriter bw = new BufferedWriter(
                    new FileWriter("employees.txt")))
                {
                    for (Employee emp : list)
                    {
                        bw.write(emp.getData());
                        bw.newLine();
                    }
                }

                JOptionPane.showMessageDialog(this,
                    "Records Saved Successfully");
            }

            
            else if (e.getSource() == loadBtn)
            {
                ArrayList<Employee> temp = new ArrayList<Employee>();

                try (BufferedReader br = new BufferedReader(
                    new FileReader("employees.txt")))
                {
                    String line;

                    while ((line = br.readLine()) != null)
                    {
                        String[] data = line.split(",", -1);

                        if (data.length != 4)
                        {
                            throw new IOException("Invalid record in file");
                        }

                        Employee emp = new Employee(
                            Integer.parseInt(data[0]),
                            data[1],
                            Double.parseDouble(data[2]),
                            data[3]);

                        temp.add(emp);
                    }
                }

                list.clear();
                list.addAll(temp);

                JOptionPane.showMessageDialog(this,
                    "Records Loaded Successfully");
            }

            
            else if (e.getSource() == viewBtn)
            {
                area.setText("");

                if (list.isEmpty())
                {
                    area.setText("No employee records found.");
                }

                for (Employee emp : list)
                {
                    area.append(
                        "Employee ID: " + emp.id +
                        "\nName: " + emp.name +
                        "\nDepartment: " + emp.department +
                        "\nBasic Salary: Rs. " + emp.salary +
                        "\nNet Salary: Rs. " + emp.netSalary() +
                        "\n-----------------------------\n");
                }
            }
        }
        catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(this,
                "Enter ID as a whole number and salary as a valid number.");
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(this,
                "File not found or file has an error.");
        }
    }

    
    public static void main(String[] args)
    {
        new EmployeePayroll();
    }
}
