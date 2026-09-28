import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculator extends JFrame {

    private final JTextField display;

    private double firstNumber = 0;
    private String operator = "";
    private boolean newNumber = true;

    public Calculator() {

        setTitle("Simple Calculator");
        setSize(350, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        display = new JTextField("0");
        display.setFont(new Font("Arial", Font.BOLD, 32));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);

        add(display, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 8, 8));
        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        String[] buttons = {
                "C", "⌫", "%", "÷",
                "7", "8", "9", "×",
                "4", "5", "6", "−",
                "1", "2", "3", "+",
                "0", ".", "=", "√"
        };

        for (String text : buttons) {

            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.setFocusable(false);

            button.addActionListener(e ->
                    processInput(e.getActionCommand())
            );

            buttonPanel.add(button);
        }

        add(buttonPanel, BorderLayout.CENTER);

        // Keyboard support
        setupKeyboard();

        setFocusable(true);
    }

    private void setupKeyboard() {

        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

                char key = e.getKeyChar();

                // Numbers
                if (key >= '0' && key <= '9') {
                    processInput(String.valueOf(key));
                    return;
                }

                // Decimal
                if (key == '.') {
                    processInput(".");
                    return;
                }

                // Operators
                switch (key) {

                    case '+':
                        processInput("+");
                        break;

                    case '-':
                        processInput("−");
                        break;

                    case '*':
                        processInput("×");
                        break;

                    case '/':
                        processInput("÷");
                        break;

                    case '%':
                        processInput("%");
                        break;

                    case 'c':
                    case 'C':
                        processInput("C");
                        break;

                    case 'r':
                    case 'R':
                        processInput("√");
                        break;
                }

                // Enter = Equals
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    processInput("=");
                }

                // Backspace
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    processInput("⌫");
                }

                // Escape = Clear
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    processInput("C");
                }
            }
        });
    }

    private void processInput(String button) {

        // Numbers
        if (button.matches("[0-9]")) {

            if (newNumber || display.getText().equals("0")) {
                display.setText(button);
                newNumber = false;
            } else {
                display.setText(display.getText() + button);
            }

            return;
        }

        // Decimal
        if (button.equals(".")) {

            if (newNumber) {
                display.setText("0.");
                newNumber = false;
            } else if (!display.getText().contains(".")) {
                display.setText(display.getText() + ".");
            }

            return;
        }

        // Clear
        if (button.equals("C")) {

            display.setText("0");
            firstNumber = 0;
            operator = "";
            newNumber = true;

            return;
        }

        // Backspace
        if (button.equals("⌫")) {

            String value = display.getText();

            if (value.length() > 1) {
                display.setText(
                        value.substring(0, value.length() - 1)
                );
            } else {
                display.setText("0");
                newNumber = true;
            }

            return;
        }

        // Square root
        if (button.equals("√")) {

            double number = Double.parseDouble(display.getText());

            if (number < 0) {
                display.setText("Error");
            } else {
                display.setText(format(Math.sqrt(number)));
            }

            newNumber = true;
            return;
        }

        // Percentage
        if (button.equals("%")) {

            double number = Double.parseDouble(display.getText());

            display.setText(format(number / 100));

            newNumber = true;
            return;
        }

        // Operators
        if (button.equals("+") ||
                button.equals("−") ||
                button.equals("×") ||
                button.equals("÷")) {

            firstNumber =
                    Double.parseDouble(display.getText());

            operator = button;
            newNumber = true;

            return;
        }

        // Equals
        if (button.equals("=")) {

            if (operator.isEmpty()) {
                return;
            }

            double secondNumber =
                    Double.parseDouble(display.getText());

            double result;

            switch (operator) {

                case "+":
                    result = firstNumber + secondNumber;
                    break;

                case "−":
                    result = firstNumber - secondNumber;
                    break;

                case "×":
                    result = firstNumber * secondNumber;
                    break;

                case "÷":

                    if (secondNumber == 0) {
                        display.setText("Error");
                        return;
                    }

                    result = firstNumber / secondNumber;
                    break;

                default:
                    return;
            }

            display.setText(format(result));

            firstNumber = result;
            operator = "";
            newNumber = true;
        }
    }

    private String format(double number) {

        if (number == (long) number) {
            return String.valueOf((long) number);
        }

        return String.valueOf(number);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            Calculator calculator = new Calculator();

            calculator.setVisible(true);

            // Important: give keyboard focus to the window
            calculator.requestFocusInWindow();
        });
    }
}