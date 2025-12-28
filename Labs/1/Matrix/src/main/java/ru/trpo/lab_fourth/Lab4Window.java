package ru.trpo.lab_fourth;

import java.awt.BorderLayout;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import ru.trpo.lab_third.UserType;
import ru.trpo.lab_third.VectorOfListsUserType;

public class Lab4Window extends JFrame {

    private final UserFactory factory = new UserFactory();
    private UserType currentType;

    private final VectorOfListsUserType data =
            new VectorOfListsUserType(3);

    public Lab4Window() {
        setTitle("Lab 4");
        setSize(550, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JComboBox<String> typeBox =
                new JComboBox<>(factory.getTypeNameList().toArray(new String[0]));

        JComboBox<Integer> blockBox =
                new JComboBox<>(new Integer[]{0, 1, 2});

        JTextArea input = new JTextArea(1, 20);
        JButton addBtn = new JButton("Add");
        JButton sortBtn = new JButton("Sort");
        JTextArea output = new JTextArea();
        output.setEditable(false);

        currentType = factory.getBuilderByName(
                (String) typeBox.getSelectedItem()
        );

        typeBox.addActionListener(e ->
                currentType = factory.getBuilderByName(
                        (String) typeBox.getSelectedItem()
                )
        );

        addBtn.addActionListener(e -> {
            try {
                UserType obj = (UserType) currentType.clone();

                InputStreamReader reader =
                        new InputStreamReader(
                                new ByteArrayInputStream(
                                        input.getText().getBytes()
                                )
                        );

                obj.readValue(reader);

                int blockIndex = (Integer) blockBox.getSelectedItem();
                data.addLast(blockIndex, obj);

                output.setText(data.toString());

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        sortBtn.addActionListener(e -> {
            data.lexicographicalSort();
            output.setText(data.toString());
        });

        JPanel top = new JPanel();
        top.add(new JLabel("Type:"));
        top.add(typeBox);
        top.add(new JLabel("Block:"));
        top.add(blockBox);
        top.add(input);
        top.add(addBtn);
        top.add(sortBtn);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(output), BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new Lab4Window().setVisible(true)
        );
    }
}
