package com.example.freezetest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4ClassRunner.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity>mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void mainScreen_loads() {

        //Регистрация кол-ва ресурсов в Эспрессо. Вытягиваем значение счетчика из класса MainActivity.
        CountingIdlingResource componentIdlingResource = mainActivityActivityTestRule.getActivity().getIdlingResourceInTest();
        IdlingRegistry.getInstance().register(componentIdlingResource);

        //Сам тест. Эспрессо находит нужный view, и нужный текст в нем.
        onView(withId(R.id.textID)).check(matches(ViewMatchers.withText(R.string.data_loaded)));

        //Утилизация мусора
        IdlingRegistry.getInstance().unregister(componentIdlingResource);
    }
}