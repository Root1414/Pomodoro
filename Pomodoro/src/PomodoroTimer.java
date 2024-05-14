import java.util.Timer;
import java.util.TimerTask;

public class PomodoroTimer {
    private int remainingTime;
    private Timer timer;
    private PomodoroUI ui;

    public PomodoroTimer(PomodoroUI ui) {
        this.ui = ui;
        this.remainingTime = ui.getSessionTime() * 60;
    }

    public void start() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (remainingTime > 0) {
                    remainingTime--;
                    updateTimerLabel();
                } else {
                    timer.cancel();
                    toggleSession();
                }
            }
        }, 0, 1000);
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public void pause() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public void reset() {
        stop();
        if (ui.isSession()) {
            remainingTime = ui.getSessionTime() * 60;
        } else {
            remainingTime = ui.getBreakTime() * 60;
        }
        updateTimerLabel();
    }

    private void updateTimerLabel() {
        ui.getTimerLabel().setText(ui.formatTime(remainingTime));
    }

    private void toggleSession() {
        ui.setSession(!ui.isSession());
        reset();
        start();
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
        updateTimerLabel();
    }

    public int getRemainingTime() {
        return remainingTime;
    }
}
