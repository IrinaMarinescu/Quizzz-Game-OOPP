package client.scenes.framecomponents;

import client.utils.TimeUtils;
import javafx.animation.TranslateTransition;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

/**
 * Provides functionality to timer bar
 */
public class TimerBarCtrl {

    public boolean test = false;

    public Rectangle timerBar;
    public TranslateTransition animation;
    public TimeUtils timeUtils;

    public double totalProgress;
    public double currentAnimationStartTime;
    public double currentAnimationLength;
    public boolean animationPlaying;
    private int displayWidth;

    public int relativePos = -2000;

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
    public void initialize(Rectangle timerBar, TimeUtils timeUtils) {
        this.timerBar = timerBar;
        this.timeUtils = timeUtils;
        timerBar.getTransforms().add(new Translate(relativePos, 0));

        animation = new TranslateTransition(Duration.ZERO, timerBar);
        animation.setOnFinished(event -> {
            timerBar.getTransforms().clear();
            relativePos += displayWidth;
            timerBar.getTransforms().add(new Translate(displayWidth, 0));
            animationPlaying = false;
        });

        currentAnimationStartTime = Double.MAX_VALUE;
        currentAnimationLength = 0.0;
        totalProgress = 0.0;
        displayWidth = 1600;
        animation.setByX(-displayWidth);
        animationPlaying = false;
    }

    /**
     * Sets remaining time visible on the timer bar
     *
     * @param seconds The length of the full animation in seconds
     */
    public void setRemainingTime(double seconds) {
        timerBar.getStyleClass().remove("fast");
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
        animation.jumpTo(Duration.ZERO);
        animation.stop();

        animation.setDuration(new Duration(currentAnimationLength));
        animation.jumpTo(new Duration(currentAnimationLength * totalProgress));
        if (!test) {
            animation.play();
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
        playAnimation();
    }

    /**
     * Changes width of timerBar in response to a change in window's size
     *
     * @param newSize New width of window in px
     * @param change  Change of width of window in px
     */
    public void resize(int newSize, int change) {
        if (-change == newSize) {
            return;
        }
        
        displayWidth = newSize;
        totalProgress += (now() - currentAnimationStartTime) / currentAnimationLength;
        animation.setByX(-newSize);
        relativePos -= change;
        timerBar.getTransforms().clear();
        timerBar.getTransforms().add(new Translate(relativePos, 0));
        if (animationPlaying) {
            playAnimation();
        }
    }
}
