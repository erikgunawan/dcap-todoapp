package id.erikgunawan.todoapp.ui.list

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import id.erikgunawan.todoapp.R
import id.erikgunawan.todoapp.ui.add.AddTaskActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

//TODO 16 : Write UI test to validate when user tap Add Task (+), the AddTaskActivity displayed
@RunWith(AndroidJUnit4::class)
class TaskActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(TaskActivity::class.java)

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun addTaskFab_clicked_opensAddTaskActivity() {
        // Click on the FAB (Add Task button)
        onView(withId(R.id.fab))
            .perform(click())

        // Verify that AddTaskActivity is displayed
        Intents.intended(hasComponent(AddTaskActivity::class.java.name))
        
        // Verify that AddTaskActivity UI elements are displayed
        onView(withId(R.id.add_ed_title))
            .check(matches(isDisplayed()))
    }
}