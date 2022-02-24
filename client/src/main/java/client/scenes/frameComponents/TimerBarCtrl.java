package client.scenes.frameComponents;

import javafx.animation.TranslateTransition;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.awt.*;
import java.time.Clock;

/**
 * Provides functionality to timer bar
 */
public class TimerBarCtrl {

    private Rectangle timerBar;
    private TranslateTransition animation;

    private double totalProgress;
    private double currentAnimationStartTime;
    private double currentAnimationLength;

    /**
     * Gets controller and instantiates animation, applies basic settings
     * @param timerBar - The component which serves as the timerBar
     */
    public void initialize(Rectangle timerBar) {
        this.timerBar = timerBar;

        animation = new TranslateTransition(Duration.ZERO, timerBar);
        animation.setByX(-1600);

        currentAnimationStartTime = Double.MAX_VALUE;
        currentAnimationLength = 0.0;
    }

    /**
     * Sets remaining time visible on the timer bar
     * @param seconds - The length of the full animation in seconds
     */
    public void setRemainingTime(double seconds) {

        if(animationDone()) {
            Translate reset = new Translate(1600, 0);
            timerBar.getTransforms().add(reset);
        }

        totalProgress = 0.0;
        currentAnimationLength = seconds * 1000;
        playAnimation(0.0);
    }

    /**
     * Plays the animation (progress * 100%) way through
     * @param progress - A real number in range [0, 1] indicating how much of the animation to skip
     */
    private void playAnimation(double progress) {
        currentAnimationStartTime = now();
        animation.jumpTo(Duration.ZERO);
        animation.stop();

        animation.setDuration(new Duration(currentAnimationLength));
        animation.jumpTo(new Duration(currentAnimationLength * progress));
        animation.play();
    }

    /**
     * Halves time as visible by the timer bar - repeatable
     *
     * This is seen by the user as a doubling of the speed of the animation (subject to change)
     */
    public void halveRemainingTime() {
        if(animationDone()) return;

        totalProgress += (now() - currentAnimationStartTime) / currentAnimationLength;
        currentAnimationLength /= 2.0;
        playAnimation(totalProgress);
    }

    /**
     * Tests whether the animation is done
     * @return Whether the animation is done
     */
    public boolean animationDone() {
        return now() - currentAnimationStartTime >= currentAnimationLength;
    }

    /**
     * Gives the time of now
     * @return The time passed in milliseconds since an arbitrary point in the past
     */
    private double now() {
        return Clock.systemDefaultZone().millis();
    }
}
