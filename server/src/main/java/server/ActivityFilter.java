package server;

import commons.Activity;
import commons.Question;
import java.util.List;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActivityFilter {

    public void runningActivityFilter(Activity activity) {
        handlingHowQuestions(activity);
        handlingPunctuation(activity);
        handlingDailyActivities(activity);
        handlingMonthlyActivities(activity);
    }

    public List<Question> runningQuestionFilter(List<Question> questions) {

        for (Question currentQuestion : questions) {
            List<Activity> currentActivities = currentQuestion.getActivities();

            switch (currentQuestion.getQuestionType()) {
                case "trueFalseQuestion":
                    changingToUpperCase(currentActivities.get(0));
                    if (currentActivities.size() == 2) {
                        changingToLowerCase(currentActivities.get(1));
                    }
                    break;
                case "openQuestion":
                    changingToLowerCase(currentActivities.get(0));
                    break;
                case "oneImageQuestion":
                case "threePicturesQuestion":
                    changingToUpperCase(currentActivities.get(0));
                    changingToUpperCase(currentActivities.get(1));
                    changingToUpperCase(currentActivities.get(2));
                    break;
                default:
                    break;
            }
        }

        return questions;
    }

    public void handlingHowQuestions(Activity activity) {
        String[] splitActivity = activity.title.split(" ");
        int j = 0;
        if (splitActivity[0].equals("How") || splitActivity[0].equals("how")) {
            for (int i = 1; i < splitActivity.length; i++) {
                if ((splitActivity[i - 1].equals("used") && splitActivity[i].equals("to"))
                    || (splitActivity[i - 1].equals("needed") && splitActivity[i].equals("to"))
                    || (splitActivity[i - 1].equals("uptake") && splitActivity[i].equals("for"))
                    || (splitActivity[i].equals("does"))) {
                    j = i;
                }
            }
        }

        if (splitActivity[splitActivity.length - 1].equals("consumes?")
            || splitActivity[splitActivity.length - 1].equals("consume?")) {
            splitActivity[splitActivity.length - 1] = null;
        }

        if (j != 0) {

            String[] temp = new String[splitActivity.length - j];
            System.arraycopy(splitActivity, j + 1, temp, 0, splitActivity.length - j - 1);
            StringBuilder tempAct = new StringBuilder();
            tempAct.append(temp[0]);
            for (int i = 1; i < temp.length; i++) {
                if (temp[i] != null) {
                    tempAct.append(" ").append(temp[i]);
                }
            }
            activity.title = tempAct.toString();
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
            StringBuilder tempAct = new StringBuilder();
            tempAct.append(splitActivity[0]);
            for (int i = 1; i < splitActivity.length; i++) {
                if (splitActivity[i] != null) {
                    tempAct.append(" ").append(splitActivity[i]);
                }
            }
            tempAct.append(" for a month");
            activity.title = tempAct.toString();
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
            StringBuilder tempAct = new StringBuilder();
            tempAct.append(splitActivity[0]);
            for (int i = 1; i < splitActivity.length; i++) {
                if (splitActivity[i] != null) {
                    tempAct.append(" ").append(splitActivity[i]);
                }
            }
            tempAct.append(" for a day");
            activity.title = tempAct.toString();
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

        StringBuilder tempAct = new StringBuilder();
        tempAct.append(splitActivity[0]);
        for (int i = 1; i < splitActivity.length; i++) {
            if (splitActivity[i] != null) {
                tempAct.append(" ").append(splitActivity[i]);
            }
        }
        activity.title = tempAct.toString();
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
