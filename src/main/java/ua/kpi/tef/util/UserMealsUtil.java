package ua.kpi.tef.util;

import ua.kpi.tef.model.UserMeal;
import ua.kpi.tef.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        List<UserMealWithExceed> list = getFilteredWithExceeded(mealList, LocalTime.of(0, 0), LocalTime.of(23, 59), 2000);
        list.forEach(System.out::println);
        List<UserMealWithExceed> listStream = getFilteredWithExceededStream(mealList, LocalTime.of(0, 0), LocalTime.of(23, 59), 2000);
        listStream.forEach(System.out::println);

//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        HashMap<LocalDate, Integer> map = new HashMap<>();
        ArrayList<UserMealWithExceed> result = new ArrayList<>();
        for (UserMeal s : mealList) {
            int calories;
            if (TimeUtil.isBetween(s.getDateTime().toLocalTime(), startTime, endTime)) {
                if (map.containsKey(s.getDateTime().toLocalDate())) {
                    calories = map.get(s.getDateTime().toLocalDate());
                    calories += s.getCalories();
                    map.put(s.getDateTime().toLocalDate(), calories);
                } else {
                    map.put(s.getDateTime().toLocalDate(), s.getCalories());
                }
            }
        }
        for (Map.Entry<LocalDate, Integer> pair : map.entrySet()) {
            result.add(new UserMealWithExceed(pair.getKey().atStartOfDay(), "Result", pair.getValue(), pair.getValue() > caloriesPerDay));
        }

        return result;
    }


    public static List<UserMealWithExceed>  getFilteredWithExceededStream(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        ArrayList<UserMealWithExceed> result = new ArrayList<>();
        Map<LocalDate, Integer> map = mealList.stream()
                .filter(u -> TimeUtil.isBetween(u.getDateTime().toLocalTime(), startTime, endTime))
                .collect(Collectors.toMap(u -> u.getDateTime().toLocalDate(), UserMeal::getCalories, (u1, u2) -> u1 + u2));

        for (Map.Entry<LocalDate, Integer> pair : map.entrySet()) {
            result.add(new UserMealWithExceed(pair.getKey().atStartOfDay(), "Result", pair.getValue(), pair.getValue() > caloriesPerDay));
        }

        return result;
    }
}
