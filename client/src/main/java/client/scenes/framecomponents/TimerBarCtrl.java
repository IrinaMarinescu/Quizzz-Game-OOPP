package client.scenes.framecomponents;

import client.utils.TimeUtils;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

/**
 * Provides functionality to timer bar
 */
public class TimerBarCtrl {

    public boolean test = false;

    public ProgressBar timerBar;
    public Timeline animation;
    public TimeUtils timeUtils;

    public double totalProgress;
    public double currentAnimationStartTime;
    public double currentAnimationLength;
    public boolean animationPlaying;

    /**
     * Gives the time of now
     *
     * @return The time passed in milliseconds since an arbitrary point in the past
     */
    public double now() {
        return timeUtils.now();
    }

    /**
     * Gets controller and instantiates animation, applies basic settings
     *
     * @param timerBar The component which serves as the timerBar
     */
    public void initialize(ProgressBar timerBar, TimeUtils timeUtils) {
        this.timerBar = timerBar;
        this.timeUtils = timeUtils;

        animation = new Timeline(
            new KeyFrame(Duration.ZERO),
            new KeyFrame(
                Duration.ZERO,
                new KeyValue(timerBar.progressProperty(), 1)
            )
        );
        setAnimationLength(0.0);
        animation.setOnFinished(event -> animationPlaying = false);
    }

    /**
     * Sets remaining time visible on the timer bar
     *
     * @param seconds The length of the full animation in seconds
     */
    public void setRemainingTime(double seconds) {
        timerBar.getStyleClass().remove("fast");

        setAnimationLength(seconds);
        animationPlaying = true;
        totalProgress = 0.0;
        currentAnimationLength = seconds * 1000;
        playAnimation();
    }

    /**
     * Plays the animation (progress * 100%) way through
     */
    public void playAnimation() {
        currentAnimationStartTime = now();
        animation.stop();

        if (!test) {
            animation.playFrom(new Duration(currentAnimationLength * totalProgress));
        }
    }

    /**
     * Halves time as visible by the timer bar - repeatable
     * <p>
     * This is seen by the user as a doubling of the speed of the animation (subject to change)
     */
    public void halveRemainingTime() {
        if (!animationPlaying) {
            return;
        }

        if (totalProgress == 0.0) {
            timerBar.getStyleClass().add("fast");
        }

        totalProgress += (now() - currentAnimationStartTime) / currentAnimationLength;
        currentAnimationLength /= 2.0;
        setAnimationLength(currentAnimationLength / 1000.0);

        playAnimation();
    }

    /**
     * Changes width of timerBar in response to a change in window's size
     *
     * @param newWidth New width of window in px
     */
    public void resize(double newWidth) {
        if (newWidth >= 1600.0) {
            timerBar.setPrefWidth(newWidth);
        }
    }

    /**
     * Sets the length of the timer bar animation
     *
     * @param seconds The length of the animation
     */
    void setAnimationLength(double seconds) {
        animation.getKeyFrames().set(0, new KeyFrame(
            Duration.seconds(seconds),
            new KeyValue(timerBar.progressProperty(), 0)
        ));
    }
}
