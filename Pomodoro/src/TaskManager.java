import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TaskManager {
    private PomodoroUI ui;
    private PomodoroTimer timer;

    public TaskManager(PomodoroUI ui, PomodoroTimer timer) {
        this.ui = ui;
        this.timer = timer;
        addListeners();
    }

    private void addListeners() {
        ui.getStartButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ui.getStartButton().setVisible(false);
                ui.getPauseButton().setEnabled(true);
                ui.getPauseButton().setVisible(true);
                ui.getStopButton().setEnabled(true);
                timer.setRemainingTime((ui.getStatusText(PomodoroUI.PomodoroState.PAUSED).equals(ui.getStatusLabel().getText())) ? timer.getRemainingTime() : (ui.getSessionTime() * 60));
                ui.setCurrentState(ui.isSession() ? PomodoroUI.PomodoroState.FOCUS : PomodoroUI.PomodoroState.BREAK);
                ui.getStatusLabel().setText(ui.getStatusText(ui.getCurrentState()));
                timer.start();
                ui.setDarkMode(true);
            }
        });

        ui.getPauseButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.pause();
                ui.setCurrentState(PomodoroUI.PomodoroState.PAUSED);
                ui.getStatusLabel().setText(ui.getStatusText(ui.getCurrentState()));
                ui.getStartButton().setVisible(true);
                ui.getStartButton().setToolTipText("Resume");
                ui.getStartButton().setEnabled(true);
                ui.getPauseButton().setEnabled(false);
                ui.getPauseButton().setVisible(false);
                ui.setDarkMode(false);
            }
        });

        ui.getStopButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                ui.setCurrentState(PomodoroUI.PomodoroState.READY);
                ui.getStatusLabel().setText(ui.getStatusText(ui.getCurrentState()));
                ui.getStartButton().setVisible(true);
                ui.getStartButton().setToolTipText("Start Session");
                ui.getStartButton().setEnabled(true);
                ui.getPauseButton().setEnabled(false);
                ui.getPauseButton().setVisible(false);
                ui.getStopButton().setEnabled(false);
                ui.setSessionNumber(1);
                ui.getSessionLabel().setText("Session Number: " + ui.getSessionNumber());
                timer.setRemainingTime(ui.getSessionTime() * 60);
                ui.setDarkMode(false);
            }
        });

        ui.getSessionTimeDropdown().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ui.setSessionDuration((int) ui.getSessionTimeDropdown().getSelectedItem());
                if (ui.isSession()) {
                    timer.setRemainingTime(ui.getSessionDuration() * 60);
                }
            }
        });

        ui.getBreakTimeDropdown().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ui.setBreakDuration((int) ui.getBreakTimeDropdown().getSelectedItem());
            }
        });

        ui.getTaskList().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int selectedIndex = ui.getTaskList().getSelectedIndex();
                if (selectedIndex != -1) {
                    int dialogResult = JOptionPane.showConfirmDialog(ui,
                            "Mark this task as completed?",
                            "Task Completion",
                            JOptionPane.YES_NO_OPTION);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        ui.markTaskAsCompleted(selectedIndex);
                    }
                }
            }
        });
    }
}
