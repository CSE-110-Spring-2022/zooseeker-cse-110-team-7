package com.example.zooseekercse110team7;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * The purpose of this class is to actually try and break the app. No assertions are made just like
 * `SimpleCrashTest` the purpose of this test is to make sure the app does not crash with wrong
 * inputs alongside random button presses.
 * */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class BreakTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION",
                    "android.permission.ACCESS_COARSE_LOCATION");

    @Test
    public void breakTest() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.mock_btn), withText("Mock"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                2),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction editText = onView(
                allOf(childAtPosition(
                                allOf(withId(android.R.id.custom),
                                        childAtPosition(
                                                withClassName(is("android.widget.FrameLayout")),
                                                0)),
                                0),
                        isDisplayed()));
        editText.perform(replaceText("ydfuuvv"), closeSoftKeyboard());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.planner_button), withText("Planner"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                0),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.imageButton), withContentDescription("SearchExhibits"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                6),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatCheckBox = onView(
                allOf(withId(R.id.checkBox),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.search_node_viewer),
                                        1),
                                2),
                        isDisplayed()));
        appCompatCheckBox.perform(click());

        ViewInteraction appCompatCheckBox2 = onView(
                allOf(withId(R.id.checkBox),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.search_node_viewer),
                                        3),
                                2),
                        isDisplayed()));
        appCompatCheckBox2.perform(click());

        ViewInteraction appCompatCheckBox3 = onView(
                allOf(withId(R.id.checkBox),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.search_node_viewer),
                                        6),
                                2),
                        isDisplayed()));
        appCompatCheckBox3.perform(click());

        ViewInteraction appCompatCheckBox4 = onView(
                allOf(withId(R.id.checkBox),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.search_node_viewer),
                                        7),
                                2),
                        isDisplayed()));
        appCompatCheckBox4.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.clearAllBtn), withText("Clear All"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                1),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatCheckBox5 = onView(
                allOf(withId(R.id.checkBox),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.search_node_viewer),
                                        1),
                                2),
                        isDisplayed()));
        appCompatCheckBox5.perform(click());

        ViewInteraction appCompatCheckBox6 = onView(
                allOf(withId(R.id.checkBox),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.search_node_viewer),
                                        8),
                                2),
                        isDisplayed()));
        appCompatCheckBox6.perform(click());

        ViewInteraction appCompatCheckBox7 = onView(
                allOf(withId(R.id.checkBox),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.search_node_viewer),
                                        4),
                                2),
                        isDisplayed()));
        appCompatCheckBox7.perform(click());

        pressBack();

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.planner_button), withText("Planner"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                0),
                        isDisplayed()));
        appCompatButton5.perform(click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.to_map), withText("Map"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatButton6.perform(click());

        ViewInteraction switch_ = onView(
                allOf(withId(R.id.brief_directions_switch), withText("Brief Directions"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        1),
                                1),
                        isDisplayed()));
        switch_.perform(click());

        ViewInteraction switch_2 = onView(
                allOf(withId(R.id.brief_directions_switch), withText("Brief Directions"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        1),
                                1),
                        isDisplayed()));
        switch_2.perform(click());

        ViewInteraction appCompatButton7 = onView(
                allOf(withId(R.id.next_directions_btn), withText("Next"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        2),
                                0),
                        isDisplayed()));
        appCompatButton7.perform(click());

        ViewInteraction appCompatButton8 = onView(
                allOf(withId(R.id.skip_directions_btn), withText("Skip"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        2),
                                1),
                        isDisplayed()));
        appCompatButton8.perform(click());

        ViewInteraction appCompatButton9 = onView(
                allOf(withId(R.id.skip_directions_btn), withText("Skip"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        2),
                                1),
                        isDisplayed()));
        appCompatButton9.perform(click());

        ViewInteraction appCompatButton10 = onView(
                allOf(withId(R.id.skip_directions_btn), withText("Skip"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        2),
                                1),
                        isDisplayed()));
        appCompatButton10.perform(click());

        ViewInteraction appCompatButton11 = onView(
                allOf(withId(R.id.skip_directions_btn), withText("Skip"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        2),
                                1),
                        isDisplayed()));
        appCompatButton11.perform(click());


        ViewInteraction appCompatButton19 = onView(
                allOf(withId(R.id.skip_directions_btn), withText("Skip"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        2),
                                1),
                        isDisplayed()));
        appCompatButton19.perform(click());

        ViewInteraction appCompatButton20 = onView(
                allOf(withId(R.id.skip_directions_btn), withText("Skip"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        2),
                                1),
                        isDisplayed()));
        appCompatButton20.perform(click());

        ViewInteraction appCompatButton21 = onView(
                allOf(withId(R.id.skip_directions_btn), withText("Skip"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        2),
                                1),
                        isDisplayed()));
        appCompatButton21.perform(click());

        ViewInteraction appCompatButton22 = onView(
                allOf(withId(R.id.skip_directions_btn), withText("Skip"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        2),
                                1),
                        isDisplayed()));
        appCompatButton22.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.reroute_btn),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        1),
                                2),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatButton23 = onView(
                allOf(withId(R.id.mock_next_btn), withText("MOCK NEXT"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        0),
                                0),
                        isDisplayed()));
        appCompatButton23.perform(click());

        ViewInteraction appCompatButton24 = onView(
                allOf(withId(R.id.mock_back_btn), withText("Mock Back"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        0),
                                1),
                        isDisplayed()));
        appCompatButton24.perform(click());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withId(R.id.reroute_btn),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        1),
                                2),
                        isDisplayed()));
        appCompatImageButton3.perform(click());

        ViewInteraction appCompatButton25 = onView(
                allOf(withId(R.id.back_directions_btn), withText("Back"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        2),
                                2),
                        isDisplayed()));
        appCompatButton25.perform(click());

        ViewInteraction appCompatButton26 = onView(
                allOf(withId(R.id.back_directions_btn), withText("Back"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        2),
                                2),
                        isDisplayed()));
        appCompatButton26.perform(click());

        ViewInteraction appCompatButton27 = onView(
                allOf(withId(R.id.next_directions_btn), withText("Next"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        2),
                                0),
                        isDisplayed()));
        appCompatButton27.perform(click());

        ViewInteraction appCompatButton28 = onView(
                allOf(withId(R.id.next_directions_btn), withText("Next"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        2),
                                0),
                        isDisplayed()));
        appCompatButton28.perform(click());

        ViewInteraction switch_3 = onView(
                allOf(withId(R.id.brief_directions_switch), withText("Brief Directions"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        1),
                                1),
                        isDisplayed()));
        switch_3.perform(click());

        ViewInteraction appCompatButton29 = onView(
                allOf(withId(R.id.planner_button), withText("Planner"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                0),
                        isDisplayed()));
        appCompatButton29.perform(click());

        ViewInteraction appCompatButton30 = onView(
                allOf(withId(R.id.clearAllBtn), withText("Clear All"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                4),
                        isDisplayed()));
        appCompatButton30.perform(click());

        ViewInteraction appCompatButton31 = onView(
                allOf(withId(R.id.to_map), withText("Map"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatButton31.perform(click());

        ViewInteraction appCompatButton32 = onView(
                allOf(withId(R.id.next_directions_btn), withText("Next"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        2),
                                0),
                        isDisplayed()));
        appCompatButton32.perform(click());

        ViewInteraction appCompatButton33 = onView(
                allOf(withId(R.id.next_directions_btn), withText("Next"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        2),
                                0),
                        isDisplayed()));
        appCompatButton33.perform(click());

        ViewInteraction appCompatButton34 = onView(
                allOf(withId(R.id.next_directions_btn), withText("Next"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        2),
                                0),
                        isDisplayed()));
        appCompatButton34.perform(click());

        ViewInteraction appCompatButton35 = onView(
                allOf(withId(R.id.next_directions_btn), withText("Next"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        2),
                                0),
                        isDisplayed()));
        appCompatButton35.perform(click());

        ViewInteraction appCompatButton36 = onView(
                allOf(withId(R.id.back_directions_btn), withText("Back"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        2),
                                2),
                        isDisplayed()));
        appCompatButton36.perform(click());

        ViewInteraction appCompatButton37 = onView(
                allOf(withId(R.id.skip_directions_btn), withText("Skip"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        2),
                                1),
                        isDisplayed()));
        appCompatButton37.perform(click());

        ViewInteraction appCompatButton38 = onView(
                allOf(withId(R.id.next_directions_btn), withText("Next"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        2),
                                0),
                        isDisplayed()));
        appCompatButton38.perform(click());

        ViewInteraction appCompatButton39 = onView(
                allOf(withId(R.id.skip_directions_btn), withText("Skip"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        2),
                                1),
                        isDisplayed()));
        appCompatButton39.perform(click());

        ViewInteraction switch_4 = onView(
                allOf(withId(R.id.brief_directions_switch), withText("Brief Directions"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        1),
                                1),
                        isDisplayed()));
        switch_4.perform(click());

        ViewInteraction appCompatButton40 = onView(
                allOf(withId(R.id.refresh_directions_btn), withText("Refresh Directions"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        1),
                                0),
                        isDisplayed()));
        appCompatButton40.perform(click());

        ViewInteraction appCompatButton41 = onView(
                allOf(withId(R.id.mock_btn), withText("Mock"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                2),
                        isDisplayed()));
        appCompatButton41.perform(click());

        ViewInteraction editText2 = onView(
                allOf(childAtPosition(
                                allOf(withId(android.R.id.custom),
                                        childAtPosition(
                                                withClassName(is("android.widget.FrameLayout")),
                                                0)),
                                0),
                        isDisplayed()));
        editText2.perform(replaceText("shhcstuopjhgdthv. dgjnn.json"), closeSoftKeyboard());

        ViewInteraction appCompatButton42 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton42.perform(scrollTo(), click());

        ViewInteraction switch_5 = onView(
                allOf(withId(R.id.brief_directions_switch), withText("Brief Directions"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        1),
                                1),
                        isDisplayed()));
        switch_5.perform(click());

        ViewInteraction switch_6 = onView(
                allOf(withId(R.id.brief_directions_switch), withText("Brief Directions"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        1),
                                1),
                        isDisplayed()));
        switch_6.perform(click());

        ViewInteraction appCompatButton43 = onView(
                allOf(withId(R.id.next_directions_btn), withText("Next"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        2),
                                0),
                        isDisplayed()));
        appCompatButton43.perform(click());

        ViewInteraction appCompatButton44 = onView(
                allOf(withId(R.id.search_button), withText("Search"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                1),
                        isDisplayed()));
        appCompatButton44.perform(click());

        ViewInteraction appCompatButton45 = onView(
                allOf(withId(R.id.clearAllBtn), withText("Clear All"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                1),
                        isDisplayed()));
        appCompatButton45.perform(click());

        ViewInteraction appCompatImageView = onView(
                allOf(withClassName(is("androidx.appcompat.widget.AppCompatImageView")), withContentDescription("Search"),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.LinearLayout")),
                                        childAtPosition(
                                                withId(R.id.search),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageView.perform(click());

        ViewInteraction searchAutoComplete = onView(
                allOf(withClassName(is("android.widget.SearchView$SearchAutoComplete")),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.LinearLayout")),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        searchAutoComplete.perform(replaceText("g"), closeSoftKeyboard());

        ViewInteraction appCompatCheckBox8 = onView(
                allOf(withId(R.id.checkBox),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.search_node_viewer),
                                        2),
                                2),
                        isDisplayed()));
        appCompatCheckBox8.perform(click());

        pressBack();

        pressBack();

        pressBack();

        ViewInteraction appCompatButton46 = onView(
                allOf(withId(R.id.mock_next_btn), withText("MOCK NEXT"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        0),
                                0),
                        isDisplayed()));
        appCompatButton46.perform(click());

        ViewInteraction appCompatButton47 = onView(
                allOf(withId(R.id.next_directions_btn), withText("Next"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        2),
                                0),
                        isDisplayed()));
        appCompatButton47.perform(click());

        ViewInteraction appCompatButton48 = onView(
                allOf(withId(R.id.skip_directions_btn), withText("Skip"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        2),
                                1),
                        isDisplayed()));
        appCompatButton48.perform(click());

        ViewInteraction appCompatButton49 = onView(
                allOf(withId(R.id.planner_button), withText("Planner"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                0),
                        isDisplayed()));
        appCompatButton49.perform(click());

        pressBack();

        ViewInteraction appCompatButton50 = onView(
                allOf(withId(R.id.skip_directions_btn), withText("Skip"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.validity),
                                        2),
                                1),
                        isDisplayed()));
        appCompatButton50.perform(click());
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
