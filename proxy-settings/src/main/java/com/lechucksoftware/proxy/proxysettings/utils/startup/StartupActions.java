package com.lechucksoftware.proxy.proxysettings.utils.startup;

import android.app.Activity;
import android.util.Log;

import com.lechucksoftware.proxy.proxysettings.App;
import com.lechucksoftware.proxy.proxysettings.constants.StartupActionStatus;
import com.lechucksoftware.proxy.proxysettings.constants.StartupActionType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marco on 12/04/14.
 */
public class StartupActions
{
    private static final String TAG = StartupAction.class.getSimpleName();
    private static List<StartupAction> availableActions;

    private static List<StartupAction> buildStartupActions(Activity activity)
    {
        ArrayList<StartupAction> actions = new ArrayList<StartupAction>();

        // SHOW Quick tour at first start
//        StartupAction quickTour = new StartupAction(activity,
//                StartupActionType.FIRST_QUICK_TOUR,
//                StartupActionStatus.NOT_AVAILABLE,
//                new StartupCondition(1, null, null));
//        actions.add(quickTour);

//        // SHOW Whats new at first start for 3.00 version
//        StartupAction whatsNew300 = new StartupAction(activity,
//                StartupActionType.WHATSNEW_300,
//                StartupActionStatus.NOT_AVAILABLE,
//                new StartupCondition(null, null, 1300300));
//        actions.add(whatsNew300);
//
//        // SHOW Whats new at first start for 2.16 version
//        StartupAction whatsNew216 = new StartupAction(activity,
//                StartupActionType.WHATSNEW_216,
//                StartupActionStatus.NOT_AVAILABLE,
//                new StartupCondition(null, null, 1300216));
//        actions.add(whatsNew216);

        StartupAction rating = new StartupAction(activity,
                StartupActionType.RATE_DIALOG,
                StartupActionStatus.NOT_AVAILABLE,
                new StartupCondition(30, null, null),
                new StartupCondition(50, null, null),
                new StartupCondition(70, null, null),
                new StartupCondition(null, 30, null));
        actions.add(rating);

        StartupAction betaTest = new StartupAction(activity,
                StartupActionType.BETA_TEST_DIALOG,
                StartupActionStatus.NOT_AVAILABLE,
                new StartupCondition(80, null, null),
                new StartupCondition(100, null, null),
                new StartupCondition(120, null, null));

        actions.add(betaTest);

        return actions;
    }

    public static List<StartupAction> getAvailableActions(Activity activity)
    {
        if (availableActions == null)
        {
            App.getTraceUtils().startTrace(TAG, "build startup actions list", Log.DEBUG);
            availableActions = buildStartupActions(activity);
            App.getTraceUtils().stopTrace(TAG, "build startup actions list", Log.DEBUG);
        }

        return availableActions;
    }
}
