package com.openclassrooms.realestatemanager;

import android.content.Context;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.openclassrooms.realestatemanager.Utils.Utils;
import com.openclassrooms.realestatemanager.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    // Context of the app under test.
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @Test
    public void useAppContext() throws Exception {
        assertEquals("com.openclassrooms.realestatemanager", appContext.getPackageName());
    }

    @Test
    public void checkInternetAvailable() {
        assertEquals(true, Utils.isInternetAvailable(appContext));
    }

    //Navigation tests
    @Test
    public void verifyListIsFirstFragment() {
        //Verify list is shown, so fragmentList is the first one to be displayed after app launch
        onView(ViewMatchers.withId(R.id.navigation_list)).check(matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void verifySearchFragmentDisplayed() {
        onView(ViewMatchers.withId(R.id.navigation_search)).perform(click());
        onView(ViewMatchers.withId(R.id.fabSearchEstate)).check(matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void verifySimulatorFragmentDisplayed() {
        onView(ViewMatchers.withId(R.id.navigation_loan)).perform(click());
        onView(ViewMatchers.withId(R.id.fabLoanResult)).check(matches(ViewMatchers.isDisplayed()));
    }

}
