/**
 * Espresso UI tests for the MainActivity class.
 */

package com.example.mareu;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.contrib.PickerActions;
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

    /**
     * Test for adding a meeting through UI interaction.
     */
    @Test
    public void addMeetingTest() {

        // UI interaction to add a meeting

        onView(withId(R.id.add_button)).perform(click());

        onView(withId(R.id.button_add_meeting)).check(matches(isDisplayed()));

        onView(withId(R.id.sujet_de_la_reunion)).perform(replaceText("Test"), closeSoftKeyboard());

        // set date to 12/05/2024
        onView(withId(R.id.button_calendar)).perform(click());
        onView(withClassName(equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2024, 5, 12));
        onView(withId(android.R.id.button1)).perform(click());

        // set time to 12:30
        onView(withId(R.id.button_time)).perform(click());
        onView(withClassName(equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(12, 30));
        onView(withId(android.R.id.button1)).perform(click());

        // picking 3rd item in spinner
        onView(withId(R.id.lieu_de_la_reunion)).perform(click());
        onData(anything()).atPosition(2).perform(click());

        onView(withId(R.id.button_add_mail)).perform(click());
        // enter email address in opened dialog, EditText doesn't have an id so we match it by class name
        onView(withClassName(equalTo(android.widget.EditText.class.getName()))).perform(replaceText("test@test.com"), closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.button_add_meeting)).perform(click());

        // Use the RecyclerViewItemCountAssertion assertion to verify that the number of items has increased by 1
        onView(withId(R.id.recycler_view)).check(new RecyclerViewItemCountAssertion(10 + 1));
    }

    /**
     * Test for deleting a meeting through UI interaction.
     */
    @Test
    public void deleteMeetingTest() {

        // UI interaction to delete a meeting

        // Use the RecyclerViewItemCountAssertion assertion to verify that the number of items is equal to 10
        onView(withId(R.id.recycler_view)).check(new RecyclerViewItemCountAssertion(10));

        // Click on the "delete" button of the first element in the list
        onView(childAtPosition(withId(R.id.recycler_view), 0))
                .perform(clickChildViewWithId(R.id.delete_button));

        // Use the RecyclerViewItemCountAssertion assertion to verify that the number of items has decreased by 1
        onView(withId(R.id.recycler_view)).check(new RecyclerViewItemCountAssertion(10 - 1));
    }

    /**
     * Test for filtering meetings by room through UI interaction.
     */
    @Test
    public void filterMeetingsByRoomTest() {

        // UI interaction to filter meetings by room

        onView(withId(R.id.recycler_view)).check(new RecyclerViewItemCountAssertion(10));

        onView(withId(R.id.filter_icon)).perform(click());

        onView(withText("Filtrer par salle")).perform(click());

        onView(withText("Room 3")).perform(click());

        onView(withId(R.id.recycler_view)).check(new RecyclerViewItemCountAssertion(1));

        // Verify that the displayed meeting description contains "Room 3"
        onView(allOf(withId(R.id.main_info), withText(containsString("Room 3"))))
                .check(matches(isDisplayed()));
    }

    /**
     * Test for filtering meetings by date through UI interaction.
     */
    @Test
    public void filterMeetingsByDateTest () {

        // UI interaction to filter meetings by date

        onView(withId(R.id.recycler_view)).check(new RecyclerViewItemCountAssertion(10));

        onView(withId(R.id.add_button)).perform(click());

        onView(withId(R.id.button_add_meeting)).check(matches(isDisplayed()));

        onView(withId(R.id.sujet_de_la_reunion)).perform(replaceText("Test"), closeSoftKeyboard());

        // set date to 12/05/2024
        onView(withId(R.id.button_calendar)).perform(click());
        onView(withClassName(equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2024, 5, 12));
        onView(withId(android.R.id.button1)).perform(click());

        // set time to 12:30
        onView(withId(R.id.button_time)).perform(click());
        onView(withClassName(equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(12, 30));
        onView(withId(android.R.id.button1)).perform(click());

        // picking 3rd item in spinner
        onView(withId(R.id.lieu_de_la_reunion)).perform(click());
        onData(anything()).atPosition(2).perform(click());

        onView(withId(R.id.button_add_mail)).perform(click());
        // enter email address in opened dialog, EditText doesn't have an id so we match it by class name
        onView(withClassName(equalTo(android.widget.EditText.class.getName()))).perform(replaceText("test@test.com"), closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.button_add_meeting)).perform(click());

        // Use the RecyclerViewItemCountAssertion assertion to verify that the number of items has increased by 1
        onView(withId(R.id.recycler_view)).check(new RecyclerViewItemCountAssertion(10 + 1));

        onView(withId(R.id.filter_icon)).perform(click());

        onView(withText("Filtrer par date")).perform(click());

        // set date to 12/05/2024
        onView(withClassName(equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2024, 5, 12));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.recycler_view)).check(new RecyclerViewItemCountAssertion(1));

        // Verify that the displayed meeting description contains "12/05/2024"
        onView(allOf(withId(R.id.main_info), withText(containsString("12/05/2024"))))
                .check(matches(isDisplayed()));
    }

    /**
     * Test for resetting filters through UI interaction.
     */
    @Test
    public void resetFilterTest () {

        // UI interaction to reset filters

        onView(withId(R.id.recycler_view)).check(new RecyclerViewItemCountAssertion(10));

        onView(withId(R.id.filter_icon)).perform(click());

        onView(withText("Filtrer par salle")).perform(click());

        onView(withText("Room 3")).perform(click());

        onView(withId(R.id.recycler_view)).check(new RecyclerViewItemCountAssertion(1));

        onView(withId(R.id.filter_icon)).perform(click());

        onView(withText("Effacer les filtres")).perform(click());

        onView(withId(R.id.recycler_view)).check(new RecyclerViewItemCountAssertion(10));

        onView(withId(R.id.add_button)).perform(click());

        onView(withId(R.id.button_add_meeting)).check(matches(isDisplayed()));

        onView(withId(R.id.sujet_de_la_reunion)).perform(replaceText("Test"), closeSoftKeyboard());

        // set date to 12/05/2024
        onView(withId(R.id.button_calendar)).perform(click());
        onView(withClassName(equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2024, 5, 12));
        onView(withId(android.R.id.button1)).perform(click());

        // set time to 12:30
        onView(withId(R.id.button_time)).perform(click());
        onView(withClassName(equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(12, 30));
        onView(withId(android.R.id.button1)).perform(click());

        // picking 3rd item in spinner
        onView(withId(R.id.lieu_de_la_reunion)).perform(click());
        onData(anything()).atPosition(2).perform(click());

        onView(withId(R.id.button_add_mail)).perform(click());
        // enter email address in opened dialog, EditText doesn't have an id so we match it by class name
        onView(withClassName(equalTo(android.widget.EditText.class.getName()))).perform(replaceText("test@test.com"), closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.button_add_meeting)).perform(click());

        onView(withId(R.id.recycler_view)).check(new RecyclerViewItemCountAssertion(10 + 1));

        onView(withId(R.id.filter_icon)).perform(click());

        onView(withText("Filtrer par date")).perform(click());

        // set date to 12/05/2024
        onView(withClassName(equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2024, 5, 12));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.recycler_view)).check(new RecyclerViewItemCountAssertion(1));

        onView(withId(R.id.filter_icon)).perform(click());

        onView(withText("Effacer les filtres")).perform(click());

        onView(withId(R.id.recycler_view)).check(new RecyclerViewItemCountAssertion(11));
    }

    /**
     * Custom Matcher for locating a child view at a specified position within a parent view.
     *
     * @param parentMatcher The Matcher for the parent view.
     * @param position      The position of the child view in the parent.
     * @return A TypeSafeMatcher<View> for locating a child view at the specified position.
     */
    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher,
            final int position
    ) {

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

    /**
     * Returns a ViewAction that clicks on a child view with the specified ID.
     *
     * @param childId The resource ID of the child view to click.
     * @return A ViewAction for clicking on a child view with the specified ID.
     */
    public static ViewAction clickChildViewWithId(final int childId) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isAssignableFrom(View.class), isDisplayed());
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View childView = view.findViewById(childId);
                if (childView != null) {
                    childView.performClick();
                } else {
                    throw new RuntimeException("Child view with id " + childId + " not found.");
                }
            }
        };
    }
}

/**
 * Custom ViewAssertion for Espresso to check the item count of a RecyclerView.
 */
class RecyclerViewItemCountAssertion implements ViewAssertion {
    private final int expectedCount;

    /**
     * Constructs a RecyclerViewItemCountAssertion with the expected item count.
     *
     * @param expectedCount The expected item count of the RecyclerView.
     */
    public RecyclerViewItemCountAssertion(int expectedCount) {
        this.expectedCount = expectedCount;
    }

    /**
     * Checks whether the actual item count of the RecyclerView matches the expected count.
     *
     * @param view                   The View to be checked, assumed to be a RecyclerView.
     * @param noViewFoundException If no matching view is found.
     * @throws NoMatchingViewException If there is no matching view.
     */
    @Override
    public void check(
            View view,
            NoMatchingViewException noViewFoundException
    ) {
        if (noViewFoundException != null) {
            throw noViewFoundException;
        }

        RecyclerView recyclerView = (RecyclerView) view;
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        assertThat(adapter.getItemCount(), is(expectedCount));
    }

}