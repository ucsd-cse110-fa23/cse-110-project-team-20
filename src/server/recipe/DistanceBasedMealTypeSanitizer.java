package server.recipe;

import java.util.Arrays;
import java.util.List;

public class DistanceBasedMealTypeSanitizer implements IMealTypeSanitizer {
    private List<String> allowedList;

    public DistanceBasedMealTypeSanitizer(List<String> allowedList)
    {
        this.allowedList = allowedList;
    }

    @Override
    public String
    apply(String value)
    {
        String result = null;
        int distance = Integer.MAX_VALUE;
        for (String candidate : allowedList) {
            int candidateDistance = calculate(candidate, value.toLowerCase());
            if (distance > candidateDistance) {
                distance = candidateDistance;
                result = candidate;
            }
        }
        return result;
    }

    // check distance between string and decide closest meal type
    // ref: https://www.baeldung.com/java-levenshtein-distance
    private static int
    calculate(String x, String y)
    {
        int[][] dp = new int[x.length() + 1][y.length() + 1];

        for (int i = 0; i <= x.length(); i++) {
            for (int j = 0; j <= y.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] =
                        min(dp[i - 1][j - 1] + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)),
                            dp[i - 1][j] + 1, dp[i][j - 1] + 1);
                }
            }
        }

        return dp[x.length()][y.length()];
    }

    private static int
    costOfSubstitution(char a, char b)
    {
        return a == b ? 0 : 1;
    }

    private static int
    min(int... numbers)
    {
        return Arrays.stream(numbers).min().orElse(Integer.MAX_VALUE);
    }
}
