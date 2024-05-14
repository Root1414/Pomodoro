import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PomodoroUI extends JFrame {
    private final Color customDarkBackground = new Color(0, 0, 0);
    private final Color customLightBackground = new Color(186, 73, 73);
    private final Color customPrimaryText = new Color(170, 170, 170);
    private final Color customSecondaryText = new Color(255, 255, 255);

    public enum PomodoroState {
        READY, FOCUS, BREAK, PAUSED
    }

    private int sessionNumber = 1;
    private int sessionDuration = 25;
    private int breakDuration = 5;
    private int remainingTime;
    private boolean isSession = true;
    private PomodoroState currentState = PomodoroState.READY;

    private JLabel sessionLabel;
    private JLabel timerLabel;
    private JLabel statusLabel;
    private JButton startButton;
    private JButton stopButton;
    private JButton pauseButton;
    private JComboBox<Integer> sessionTimeDropdown;
    private JComboBox<Integer> breakTimeDropdown;

    private List<Task> tasks;
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;
    private JTextField taskInputField;
    private JButton addTaskButton;

    public PomodoroUI() {
        setTitle("Pomodoro Timer");
        setSize(1100, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        tasks = new ArrayList<>();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        sessionLabel = new JLabel("Session Number: " + sessionNumber, SwingConstants.RIGHT);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(sessionLabel, gbc);

        timerLabel = new JLabel(formatTime(sessionDuration * 60), SwingConstants.RIGHT);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 99));
        gbc.gridy = 1;
        add(timerLabel, gbc);

        statusLabel = new JLabel(getStatusText(currentState), SwingConstants.RIGHT);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridy = 2;
        add(statusLabel, gbc);

        ImageIcon startIcon = resizeIcon(new ImageIcon("src/start-icon.png"), 40, 40);
        ImageIcon pauseIcon = resizeIcon(new ImageIcon("src/pause-icon.png"), 40, 40);
        ImageIcon stopIcon = resizeIcon(new ImageIcon("src/stop-icon.png"), 40, 40);

        startButton = new JButton(startIcon);
        startButton.setToolTipText("Start Session");
        startButton.setContentAreaFilled(false);
        startButton.setFocusPainted(false);
        gbc.gridy = 4;
        add(startButton, gbc);

        pauseButton = new JButton(pauseIcon);
        pauseButton.setToolTipText("Pause");
        pauseButton.setContentAreaFilled(false);
        pauseButton.setFocusPainted(false);
        pauseButton.setEnabled(false);
        pauseButton.setVisible(false);
        gbc.gridy = 4;
        add(pauseButton, gbc);

        stopButton = new JButton(stopIcon);
        stopButton.setToolTipText("Stop");
        stopButton.setContentAreaFilled(false);
        stopButton.setFocusPainted(false);
        stopButton.setEnabled(false);
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(stopButton, gbc);

        sessionTimeDropdown = new JComboBox<>(new Integer[]{1, 25, 30, 35, 40, 45});
        sessionTimeDropdown.setSelectedItem(25);
        sessionTimeDropdown.setOpaque(false);
        sessionTimeDropdown.setBackground(new Color(0, 0, 0, 0));
        Border dottedBorder = BorderFactory.createDashedBorder(customPrimaryText, 1, 5);
        sessionTimeDropdown.setBorder(dottedBorder);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        add(new JLabel("Session Time (minutes):", SwingConstants.RIGHT), gbc);
        gbc.gridy = 7;
        gbc.gridx = 1;
        add(sessionTimeDropdown, gbc);

        breakTimeDropdown = new JComboBox<>(new Integer[]{5, 10, 15});
        breakTimeDropdown.setSelectedItem(5);
        breakTimeDropdown.setOpaque(false);
        breakTimeDropdown.setBackground(new Color(0, 0, 0, 0));
        breakTimeDropdown.setBorder(dottedBorder);
        gbc.gridx = 0;
        gbc.gridy = 8;
        add(new JLabel("Break Time (minutes):", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        add(breakTimeDropdown, gbc);


        taskInputField = new JTextField(15);
        taskInputField.setOpaque(false);
        taskInputField.setBackground(new Color(0, 0, 0, 0));
        taskInputField.setBorder(dottedBorder);

        addTaskButton = new JButton("Add Task");
        addTaskButton.setContentAreaFilled(false);
        addTaskButton.setFocusPainted(false);
        addTaskButton.setOpaque(false);
        addTaskButton.setBorder(dottedBorder);

        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);

        addTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String taskText = taskInputField.getText();
                if (!taskText.isEmpty()) {
                    tasks.add(new Task(taskText));
                    taskListModel.addElement(taskText);
                    taskInputField.setText("");
                }
            }
        });

        taskList.setOpaque(false);
        taskList.setBackground(new Color(0, 0, 0, 0));
        taskList.setForeground(customPrimaryText);
        taskList.setBorder(dottedBorder);

        JScrollPane taskScrollPane = new JScrollPane(taskList);
        taskScrollPane.setOpaque(false);
        taskScrollPane.getViewport().setOpaque(false);
        taskScrollPane.setBorder(dottedBorder);

        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        add(taskInputField, gbc);
        gbc.gridy = 12;
        add(addTaskButton, gbc);
        gbc.gridy = 13;
        add(taskScrollPane, gbc);

        setDarkMode(false);
        setVisible(true);
    }

    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }

    public JButton getStartButton() {
        return startButton;
    }

    public JButton getStopButton() {
        return stopButton;
    }

    public JButton getPauseButton() {
        return pauseButton;
    }

    public JLabel getTimerLabel() {
        return timerLabel;
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }

    public JLabel getSessionLabel() {
        return sessionLabel;
    }

    public int getSessionTime() {
        return (int) sessionTimeDropdown.getSelectedItem();
    }

    public int getBreakTime() {
        return (int) breakTimeDropdown.getSelectedItem();
    }

    public JComboBox<Integer> getSessionTimeDropdown() {
        return sessionTimeDropdown;
    }

    public JComboBox<Integer> getBreakTimeDropdown() {
        return breakTimeDropdown;
    }

    public int getSessionDuration() {
        return sessionDuration;
    }

    public void setDarkMode(boolean darkMode) {
        Color background = darkMode ? customDarkBackground : customLightBackground;
        Color foreground = darkMode ? customPrimaryText : customSecondaryText;
        getContentPane().setBackground(background);
        sessionLabel.setForeground(foreground);
        timerLabel.setForeground(foreground);
        statusLabel.setForeground(foreground);
    }

    public String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public String getStatusText(PomodoroState state) {
        switch (state) {
            case READY:
                return "Ready to start";
            case FOCUS:
                return "Focus session";
            case BREAK:
                return "Break time";
            case PAUSED:
                return "Paused";
            default:
                return "";
        }
    }

    public int getSessionNumber() {
        return sessionNumber;
    }

    public void setSessionNumber(int sessionNumber) {
        this.sessionNumber = sessionNumber;
    }

    public void setSessionDuration(int sessionDuration) {
        this.sessionDuration = sessionDuration;
    }

    public void setBreakDuration(int breakDuration) {
        this.breakDuration = breakDuration;
    }

    public boolean isSession() {
        return isSession;
    }

    public void setSession(boolean session) {
        isSession = session;
    }

    public PomodoroState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(PomodoroState currentState) {
        this.currentState = currentState;
    }

    public void markTaskAsCompleted(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.get(index).setCompleted(true);
            taskListModel.set(index, tasks.get(index).toString());
        }
    }

    public void removeTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
            taskListModel.remove(index);
        }
    }

    public JList<String> getTaskList() {
        return taskList;
    }
}
