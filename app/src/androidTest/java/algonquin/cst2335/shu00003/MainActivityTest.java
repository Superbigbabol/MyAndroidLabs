package algonquin.cst2335.shu00003;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(withId(R.id.cityText));
        appCompatEditText.perform(replaceText("12345"), closeSoftKeyboard());


        ViewInteraction materialButton = onView(withId(R.id.forecastButton));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * test typing password without an upper case letter
     */
    @Test
    public void testFindMissingUpperCase(){
        ViewInteraction appCompatEditText = onView(withId(R.id.cityText));
        appCompatEditText.perform(replaceText("password123#$*"));

        ViewInteraction materialButton = onView(withId(R.id.forecastButton));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * test typing password without a lower case letter
     */
    @Test
    public void testFindMissingLowerCase(){
        ViewInteraction appCompatEditText = onView(withId(R.id.cityText));
        appCompatEditText.perform(replaceText("PASS123#$*"));

        ViewInteraction materialButton = onView(withId(R.id.forecastButton));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * test typing password without a number
     */
    @Test
    public void testFindMissingNumber(){
        ViewInteraction appCompatEditText = onView(withId(R.id.cityText));
        appCompatEditText.perform(replaceText("PASSword#$*"));

        ViewInteraction materialButton = onView(withId(R.id.forecastButton));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * test typing password without a special symbol
     */
    @Test
    public void testFindMissingSpecialChar(){
        ViewInteraction appCompatEditText = onView(withId(R.id.cityText));
        appCompatEditText.perform(replaceText("PASSword123"));

        ViewInteraction materialButton = onView(withId(R.id.forecastButton));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * test a password that should be good to pass all requirements
     */
    @Test
    public void testGoodPass(){
        ViewInteraction appCompatEditText = onView(withId(R.id.cityText));
        appCompatEditText.perform(replaceText("PASSword123!@#"));

        ViewInteraction materialButton = onView(withId(R.id.forecastButton));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText("Your password meets the requirements")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
