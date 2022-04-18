package baseball;

import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.Randoms;

import java.util.Arrays;


public class Application {

    private enum GAME_RESULT {
        FAIL(0),
        RESTART(1),
        END(2),
        UNKNOWN(999);

        private Integer value;

        GAME_RESULT(int v) {
            value = v;
        }

        public static GAME_RESULT find(Integer value) {
            return Arrays.stream(values()).filter(e ->
                    e.value.equals(value)).findAny().orElse(UNKNOWN);
        }


    } 

    private enum DIGIT {
        RESTART_INPUT(1),
        GAME_INPUT(3);

        private int value;

        public int getValue() {
            return value;
        }

        DIGIT(int v) {
            value = v;
        }
    }

    private enum ERROR_MESSAGE {
        TYPE_ERROR("input must be number"),
        DIGIT_ERROR("Input must be three digits");

        private final String name;

        ERROR_MESSAGE(String s) {
            name = s;
        }

        public String getName() {
            return name;
        }
    }

    private enum INFO_MESSAGE {
        USER_NUMBER_INPUT("숫자를 입력해주세요 : "),
        USER_SUCCESS("3개의 숫자를 모두 맞히셨습니다! 게임 종료"),
        USER_RESTART_INPUT("게임을 새로 시작하려면 1, 종료하려면 2를 입력하세요.");

        private final String name;

        INFO_MESSAGE(String s) {
            name = s;
        }

        public String getName() {
            return name;
        }
    }

    private static final String RESULT_NOTHING = "낫싱";
    private static final String[] RESULT_SET_OF_BALL = {"", "1볼 ", "2볼 ", "3볼"};
    private static final String[] RESULT_SET_OF_STRIKE = {"", "1스트라이크", "2스트라이크"};

    public static void main(String[] args) {
        //TODO: 숫자 야구 게임 구현

        int targetNumber;
        int userInput;
        boolean flag = true;

        targetNumber = generateTargetNumber();
        System.out.println(targetNumber);
        while (flag) {
            System.out.print(INFO_MESSAGE.USER_NUMBER_INPUT.getName());
            userInput = checkInputValidation(Console.readLine(), DIGIT.GAME_INPUT.getValue());

            GAME_RESULT result = getResultOfInput(userInput, targetNumber);
            if (result == GAME_RESULT.END) {
                flag = false;
            }
            if (result == GAME_RESULT.RESTART) {
                targetNumber = generateTargetNumber();
            }
        }
    }

    private static int generateTargetNumber() {
        int result = 0;
        int digit = 3;

        boolean[] isUsedBefore = new boolean[10];

        while (digit-- != 0) {
            result *= 10;
            int picked;
            while (isUsedBefore[picked = Randoms.pickNumberInRange(1, 9)]) ;
            isUsedBefore[picked] = true;
            result += picked;
        }

        return result;
    }

    private static GAME_RESULT getResultOfInput(int input, int target) {
        final String inputString = String.valueOf(input);
        final String targetString = String.valueOf(target);

        int numberOfBall = 0;
        int numberOfStrike = 0;

        boolean[] isExist = new boolean[10];
        boolean[] isDuplicated = new boolean[10];

        for (int i = 0; i < 3; i++) {
            isExist[targetString.charAt(i) - '0'] = true;
        }

        for (int i = 0; i < 3; i++) {
            int inputIndex = inputString.charAt(i) - '0';
            int targetIndex = targetString.charAt(i) - '0';

            if (isDuplicated[inputIndex]) {
                continue;
            }

            isDuplicated[inputIndex] = true;
            if (isExist[inputIndex] && targetIndex == inputIndex) {
                numberOfStrike++;
            }
            if (isExist[inputIndex] && targetIndex != inputIndex) {
                numberOfBall++;
            }
        }

        if (numberOfStrike == 3) {
            return checkRestart();
        }

        if (numberOfBall == 0 && numberOfStrike == 0) {
            System.out.println(RESULT_NOTHING);
        }
        System.out.println(RESULT_SET_OF_BALL[numberOfBall] + RESULT_SET_OF_STRIKE[numberOfStrike]);
        return GAME_RESULT.FAIL;
    }

    private static GAME_RESULT checkRestart() {
        System.out.println(INFO_MESSAGE.USER_SUCCESS.getName());
        System.out.println(INFO_MESSAGE.USER_RESTART_INPUT.getName());

        int input = checkInputValidation(Console.readLine(), DIGIT.RESTART_INPUT.getValue());

        return GAME_RESULT.find(input);
    }


    private static int checkInputValidation(String input, int digit) {
        try {
            return checkInputDigit(checkInputType(input), digit);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private static int checkInputType(String input) throws Exception {
        try {
            return Integer.valueOf(input);
        } catch (Exception e) {
            throw new Exception(ERROR_MESSAGE.TYPE_ERROR.getName());
        }
    }

    private static int checkInputDigit(int input, int digit) throws Exception {
        if (String.valueOf(input).length() != digit) {
            throw new Exception(ERROR_MESSAGE.DIGIT_ERROR.getName());
        } else {
            return input;
        }
    }
}
