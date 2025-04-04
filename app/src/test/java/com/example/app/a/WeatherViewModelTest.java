package com.example.app.a;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

import com.example.app.a.model.DayWeather;
import com.example.app.a.model.Period;

public class WeatherViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private WeatherViewModel viewModel;
    private MutableLiveData<List<Period>> testLiveData;

    @Before
    public void setUp() {
        testLiveData = new MutableLiveData<>();
        viewModel = new WeatherViewModel(new TestWeatherRepository(testLiveData));
        viewModel.requestWeatherData();
    }

    @Test
    public void getWeatherData_withNull_returnsEmptyList() {
        // Arrange
        testLiveData.setValue(null);

        // Act
        List<DayWeather> result = viewModel.getWeatherData().getValue();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void getWeatherData_filtersDaytimeOnly() {
        // Arrange
        List<Period> testData = Arrays.asList(
                new Period("2023-06-01T12:00:00Z", true, 68),
                new Period("2023-06-01T00:00:00Z", false, 68)
        );
        testLiveData.setValue(testData);

        // Act
        List<DayWeather> result = viewModel.getWeatherData().getValue();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("01.06.2023 Donnerstag", result.get(0).getDate());
    }

    @Test
    public void getWeatherData_handlesNegativeTemperatures() {
        // Arrange
        List<Period> testData = List.of(
                new Period("2023-01-01T12:00:00Z", true, -4)
        );
        testLiveData.setValue(testData);

        // Act
        List<DayWeather> result = viewModel.getWeatherData().getValue();

        // Assert
        assertNotNull(result);
        assertEquals("-20°C", result.get(0).getTemperature());
    }

    @Test
    public void getWeatherData_formatsDateCorrectly() {
        // Arrange
        List<Period> testData = List.of(
                new Period("2023-06-01T12:00:00Z", true, -4)
        );
        testLiveData.setValue(testData);

        // Act
        List<DayWeather> result = viewModel.getWeatherData().getValue();

        // Assert
        assertNotNull(result);
        assertEquals("01.06.2023 Donnerstag", result.get(0).getDate());
    }


    @Test
    public void getWeatherData_limitsTo7Items() {
        // Arrange
        List<Period> manyPeriods = Arrays.asList(
                new Period("2023-06-01T06:00:00Z", true, 65),
                new Period("2023-06-01T09:00:00Z", true, 67),
                new Period("2023-06-01T12:00:00Z", true, 70),
                new Period("2023-06-01T15:00:00Z", true, 72),
                new Period("2023-06-01T18:00:00Z", true, 68),
                new Period("2023-06-02T06:00:00Z", true, 66),
                new Period("2023-06-02T09:00:00Z", true, 69),
                new Period("2023-06-02T12:00:00Z", true, 71)
        );
        testLiveData.setValue(manyPeriods);

        // Act
        List<DayWeather> result = viewModel.getWeatherData().getValue();

        // Assert
        assertNotNull(result);
        assertEquals(7, result.size());
    }

    private static class TestWeatherRepository extends WeatherRepository {
        private final LiveData<List<Period>> testData;

        TestWeatherRepository(LiveData<List<Period>> testData) {
            this.testData = testData;
        }

        @Override
        public LiveData<List<Period>> requestWeatherData() {
            return testData;
        }
    }
}