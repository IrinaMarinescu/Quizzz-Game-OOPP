package server;

import commons.Activity;
import commons.Question;
import java.util.List;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActivityFilter {

    public void runningFilter(List<Question> questions) {

        for (Question currentQuestion : questions) {
            List<Activity> currentActivities = currentQuestion.getActivities();

            switch (currentQuestion.getQuestionType()) {
                case "trueFalseQuestion":
                    changingToUpperCase(currentActivities.get(0));
                    changingToLowerCase(currentActivities.get(1));
                    break;
                case "openQuestion":
                case "oneImageQuestion":
                    changingToLowerCase(currentActivities.get(0));
                    break;
                case "threePicturesQuestion":
                    changingToUpperCase(currentActivities.get(0));
                    changingToUpperCase(currentActivities.get(1));
                    changingToUpperCase(currentActivities.get(2));
                    break;
                case "insteadOfQuestion":
                    changingToLowerCase(currentActivities.get(0));
                    changingToUpperCase(currentActivities.get(1));
                    changingToUpperCase(currentActivities.get(2));
                    changingToUpperCase(currentActivities.get(3));
                    break;
                default:
                    break;
            }

            for (Activity currentActivity : currentActivities) {
                handlingHowQuestions(currentActivity);
                handlingDailyActivities(currentActivity);
                handlingMonthlyActivities(currentActivity);
                handlingPunctuation(currentActivity);
            }
        }
    }

    public void handlingHowQuestions(Activity activity) {
        String[] splitActivity = activity.title.split(" ");
        int j = 0;
        if (splitActivity[0].equals("How") || splitActivity[0].equals("how")) {
            for (int i = 1; i < splitActivity.length; i++) {
                if ((splitActivity[i - 1].equals("used") && splitActivity[i].equals("to"))
                    || (splitActivity[i - 1].equals("needed") && splitActivity[i].equals("to"))
                    || (splitActivity[i - 1].equals("uptake") && splitActivity[i].equals("for"))) {
                    j = i;
                }
            }
        }
        if (j != 0) {
            String[] temp = new String[splitActivity.length - j];
            System.arraycopy(splitActivity, j, temp, 0, splitActivity.length - j);
            String tempAct = "";
            for (String s : temp) {
                tempAct = s + " ";
            }
            activity.title = tempAct;
        }
    }

    public void handlingMonthlyActivities(Activity activity) {
        String[] splitActivity = activity.title.split(" ");
        int j = 0;

        for (int i = 0; i < splitActivity.length; i++) {
            if (splitActivity[i].equals("monthly") || splitActivity[i].equals("Monthly")) {
                splitActivity[i] = null;
                j = 1;
            }
        }

        if (j != 0) {
            String tempAct = "";
            for (String s : splitActivity) {
                if (s != null) {
                    tempAct = s + " ";
                }
            }
            tempAct = tempAct.concat("for a month");
            activity.title = tempAct;
        }
    }

    public void handlingDailyActivities(Activity activity) {
        String[] splitActivity = activity.title.split(" ");
        int j = 0;

        for (int i = 0; i < splitActivity.length; i++) {
            if (splitActivity[i].equals("daily") || splitActivity[i].equals("Daily")) {
                splitActivity[i] = null;
                j = 1;
            }
        }

        if (j != 0) {
            String tempAct = "";
            for (String s : splitActivity) {
                if (s != null) {
                    tempAct = s + " ";
                }
            }
            tempAct = tempAct.concat("for a day");
            activity.title = tempAct;
        }
    }

    public void handlingPunctuation(Activity activity) {
        String[] splitActivity = activity.title.split(" ");
        for (int i = 0; i < splitActivity.length; i++) {
            if (splitActivity[i].contains(".")) {
                splitActivity[i] = splitActivity[i].replace(".", "");
            }
            if (splitActivity[i].contains("?")) {
                splitActivity[i] = splitActivity[i].replace("?", "");
            }
        }

        String tempAct = "";
        for (String s : splitActivity) {
            if (s != null) {
                tempAct = s + " ";
            }
        }
        activity.title = tempAct;
    }

    public void changingToUpperCase(Activity activity) {
        String firstLetter = activity.title.substring(0, 1);
        String leftover = activity.title.substring(1);

        firstLetter = firstLetter.toUpperCase();
        activity.title = firstLetter + leftover;
    }

    public void changingToLowerCase(Activity activity) {
        String firstLetter = activity.title.substring(0, 1);
        String leftover = activity.title.substring(1);

        firstLetter = firstLetter.toLowerCase();
        activity.title = firstLetter + leftover;
    }
}
