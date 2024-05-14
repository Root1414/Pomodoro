public class PomodoroApp {
    public static void main(String[] args) {
        PomodoroUI ui = new PomodoroUI();
        PomodoroTimer timer = new PomodoroTimer(ui);
        TaskManager taskManager = new TaskManager(ui, timer);
    }
}
