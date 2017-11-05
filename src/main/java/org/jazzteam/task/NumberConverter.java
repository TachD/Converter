package org.jazzteam.task;

import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 * Created by TachD on 03.11.2017.
 *
 * @autor Govor Alexander
 */

//    Перевод числа в цифровой записи в строковую. Например 134345 будет "сто
//тридцать четыре тысячи триста сорок пять". * Учесть склонения - разница
//в окончаниях (к примеру, две и два).
//    Алгоритм должен работать для сколько угодно большого числа, соответственно,
//значения степеней - миллион, тысяча, миллиад и т.д. - должны браться их
//справочника, к примеру, текстового файла.
//    Обязательно создать Data Driven Test (я, как пользователь, должен иметь
//возможность ввести множество наборов 1.число 2.правильный эталонный результат,
// тест самостоятельно проверяет все наборы и говорит, что неверное), который
// доказывает, что Ваш алгоритм работает правильно. Использовать JUnit.
//    По возможности, применить ООП.

public class NumberConverter {
    private static final TreeMap<String, String> NUMBERS_MAP = new TreeMap<String, String>() {{
        put("-", "минус");
        put("0", "ноль");

        put("1", "один");
        put("1_", "одна");
        put("2", "два");
        put("2_", "две");
        put("3", "три");
        put("4", "четыре");
        put("5", "пять");
        put("6", "шесть");
        put("7", "семь");
        put("8", "восемь");
        put("9", "девять");
        put("10", "десять");

        put("11", "одиннадцать");
        put("12", "двенадцать");
        put("13", "тринадцать");
        put("14", "четырнадцать");
        put("15", "пятнадцать");
        put("16", "шестнадцать");
        put("17", "семнадцать");
        put("18", "восемнадцать");
        put("19", "девятнадцать");

        put("20", "двадцать");
        put("30", "тридцать");
        put("40", "сорок");
        put("50", "пятьдесят");
        put("60", "шестьдесят");
        put("70", "семьдесят");
        put("80", "восемьдесят");
        put("90", "девяносто");

        put("100", "сто");
        put("200", "двести");
        put("300", "триста");
        put("400", "четыреста");
        put("500", "пятьсот");
        put("600", "шестьсот");
        put("700", "семьсот");
        put("800", "восемьсот");
        put("900", "девятьсот");
    }};

    /**
     * Карта значений степеней, использована американская система чисел.
     */
    private static final TreeMap<String, String> SCALES_MAP = new TreeMap<String, String>() {{
        put("32", "антригинтиллион");
        put("31", "тригинтиллион");
        put("30", "новемвигинтиллион");
        put("29", "октовигинтиллион");
        put("28", "септемвигинтиллион");
        put("27", "сексвигинтиллион");
        put("26", "квинвигинтиллион");
        put("25", "кватторвигинтиллион");
        put("24", "тревигинтиллион");
        put("23", "дуовигинтиллион");
        put("22", "анвигинтиллион");
        put("21", "вигинтиллион");
        put("20", "ундевигинтиллион");
        put("19", "дуодевигинтиллион");
        put("18", "септдециллион");
        put("17", "седециллион");
        put("16", "квиндециллион");
        put("15", "кваттуордециллион");
        put("14", "тредециллион");
        put("13", "додециллион");
        put("12", "ундециллион");
        put("11", "дециллион");
        put("10", "нониллион");
        put("9", "октиллион");
        put("8", "септиллион");
        put("7", "секстиллион");
        put("6", "квинтиллион");
        put("5", "квадриллион");
        put("4", "триллион");
        put("3", "миллиард");
        put("2", "миллион");
        put("1", "тысяч");
        put("0", "");
    }};

    /**
     * Класс содержит методы для преобразования цифровой записи числа в строковую по степени до сотых включительно.
     */
    private static class PartConverter {
        static String convertHundredPart(String hundredPart) {
            return (hundredPart.equals("0")) ? "" : NUMBERS_MAP.get(hundredPart + "00");
        }

        static String convertDecimalPart(String decimalPart, String unitPart, String scale) {
            switch (decimalPart) {
                case "0":
                    return convertUnitPart(unitPart, scale);
                case "1":
                    return NUMBERS_MAP.get(decimalPart + unitPart);
                default:
                    // если в переменной unitPart не содержится "0", то вызывается convertUnitPart, иначе вовзращаем строку с целой десятичной частью
                    return NUMBERS_MAP.get(decimalPart + "0") + ((unitPart.equals("0")) ? "" : " " + convertUnitPart(unitPart, scale));
            }
        }

        static String convertUnitPart(String unitPart, String scale) {
            // если степень содержит в себе подстроку "тысяч" - используется 2 форма записи чисел 1 и 2
            switch (unitPart) {
                case "0":
                    return "";
                case "1":
                    return NUMBERS_MAP.get(scale.contains("тысяч") ? "1_" : "1");
                case "2":
                    return NUMBERS_MAP.get(scale.contains("тысяч") ? "2_" : "2");
                default:
                    return NUMBERS_MAP.get(unitPart);
            }
        }
    }

    /**
     * Разбитие строки на подстроки, каждая из которых содержит по 3 символа.
     * Если для 3-символьной подстроки не хватает элементов - создается 1/2-символьная строка.
     *
     * @param sourceString строка, переданная для разбиения на подстроки.
     * @return массив подстрок из строки-аргумента. Каждая подстрока состоит из 3 символов. Последняя подстрока может состоять из 1/2/3 симовлов.
     */
    private static String[] stringSplit(String sourceString) {
        int lastElement = sourceString.length();

        // В случае не кратности 3 - дополнительно увеличиваем размер массива на 1 для подстроки с количеством элементов < 3
        String[] subNumbers = new String[(lastElement % 3 == 0) ? lastElement / 3 : lastElement / 3 + 1];

        // возможный выход за границы строки - поход к последней подстроке с менее чем 3 символами,
        // в подобной ситуации создаем подстроку с индекса 0, которая будет содержать оставшиеся символы
        for (int i = 0; i < subNumbers.length; ++i, lastElement -= 3)
            subNumbers[i] = sourceString.substring((lastElement - 3 >= 0) ? lastElement - 3 : 0, lastElement);

        return subNumbers;
    }

    /**
     * Изменение падежа степени в зависимости от чисел.
     *
     * @param scale    степень.
     * @param formCode код фармы падежа.
     * @return степень числа с определенным окончанием. В зависимости от степени-аргумента может вернуть пустую строку.
     */
    private static String getScaleForm(String scale, int formCode) {
        if (scale.equals(""))
            return "";

        switch (formCode) {
            case 1:
                return scale + (scale.equals("тысяч") ? "а" : "");
            case 2:
                return scale + (scale.equals("тысяч") ? "и" : "а");
            case 3:
                return scale + (scale.equals("тысяч") ? "" : "ов");
            default:
                return "";
        }
    }

    /**
     * Расчет падежа для степени числа.
     *
     * @param decimalSymbol десятая часть числа.
     * @param unitSymbol    единична часть числа.
     * @return код падежа, от 1 до 3.
     */
    private static int getFormCode(String decimalSymbol, String unitSymbol) {
        switch (decimalSymbol) {
            case "1":
                return 3;
            default:
                switch (unitSymbol) {
                    case "1":
                        return 1;
                    case "2":
                    case "3":
                    case "4":
                        return 2;
                    default:
                        return 3;
                }
        }
    }

    /**
     * Запись первой подстроки числа словами.
     *
     * @param subNumber строка, содержащая цифры, которые небоходимо записать словами.
     * @param scale     степень числа.
     * @return число, преобразованное в строку.
     */
    private static String firstSubNumberToWords(String subNumber, String scale) {
        StringBuilder numberInWords = new StringBuilder();

        // проверка на то, что в строке только "-" или "0", а также их комбинации
        switch (subNumber.length()) {
            case 1:
                switch (subNumber) {
                    case "-":
                        return NUMBERS_MAP.get("-");
                    case "0":
                        // если степень подстроки от тысяч - возвращаем пустую строку
                        return scale.equals("") ? NUMBERS_MAP.get("0") : "";
                }
                break;
            default:
                if (subNumber.contains("-")) {
                    if ((subNumber.contains("0") && subNumber.length() == 2) || (subNumber.contains("00") && subNumber.length() == 3))
                        // если степень подстроки от тысяч - возвращаем "минус"
                        return scale.equals("") ? NUMBERS_MAP.get("0") : NUMBERS_MAP.get("-");

                    numberInWords.append(NUMBERS_MAP.get("-")).append(" ");

                    subNumber = subNumber.substring(1, subNumber.length());
                }
        }

        StringBuilder tempNumber = new StringBuilder();

        for (int i = 3 - subNumber.length(); i != 0; --i)
            tempNumber.append("0");

        numberInWords.append(subNumberToWords(tempNumber + subNumber, scale));

        return numberInWords.toString();
    }

    /**
     * Запись подстроки числа словами.
     *
     * @param subNumber строка, содержащая цифры, которые небоходимо записать словами.
     * @param scale     степень числа.
     * @return число, преобразованное в строку.
     */
    private static String subNumberToWords(String subNumber, String scale) {
        String firstSymbol = subNumber.substring(0, 1);
        String secondSymbol = subNumber.substring(1, 2);
        String thirdSymbol = subNumber.substring(2, 3);

        StringBuilder numberInWords = new StringBuilder();

        numberInWords.append(PartConverter.convertHundredPart(firstSymbol));

        if (numberInWords.length() != 0 && !secondSymbol.equals("0") && !thirdSymbol.equals("0"))
            numberInWords.append(" ");

        // определение формы окончаний степени чисел на основе 2 и 3 символа в подстроке.
        int formCode = getFormCode(secondSymbol, thirdSymbol);

        // подготовка нужной формы степени числа
        scale = getScaleForm(scale, formCode);

        numberInWords.append(PartConverter.convertDecimalPart(secondSymbol, thirdSymbol, scale));

        // если subNumber.equals(000)
        if (numberInWords.toString().equals(""))
            return "";

        if (!scale.equals(""))
            numberInWords.append(" ").append(scale);

        return numberInWords.toString();
    }

    /**
     * Перевод числа в цифровой записи в строковую.
     *
     * @param sourceNumber строка чисел, которая будет преобразована в слова.
     * @return число, преобразованное в слова.
     * @throws NullPointerException если аргумент - null.
     * @throws Exception            если длинна аргумента == 0 или длинна аргумента > 100 или формат строки не совпадает с регулярным выражением "-?[0-9]+".
     */
    public static String NumberToWords(String sourceNumber) throws Exception {
        if (sourceNumber == null)
            throw new NullPointerException();

        if (sourceNumber.length() == 0)
            throw new Exception("Аргумент является пустой строкой");

        if (sourceNumber.length() > 100)
            throw new Exception("Слишком длинный аргумент");

        if (!Pattern.compile("-?[0-9]+").matcher(sourceNumber).matches())
            throw new Exception("Неправильный формат строки. Допускаются только числа и символ \"-\" в начале строки");

        String[] subNumbers = stringSplit(sourceNumber);

        StringBuilder convertedNumber = new StringBuilder();

        // Преборазование первой подстроки, которая может иметь меньше 3-х символов и символ "-"
        convertedNumber.append(firstSubNumberToWords(subNumbers[subNumbers.length - 1], SCALES_MAP.get(String.valueOf(subNumbers.length - 1))));

        for (int i = subNumbers.length - 2; i >= 0; --i) {
            String subNuberInWord = subNumberToWords(subNumbers[i], SCALES_MAP.get(String.valueOf(i)));
            // проверка на возможное содержание подстрок только с нулями
            if (subNuberInWord.length() != 0)
                convertedNumber.append(" ").append(subNuberInWord);
        }


        return convertedNumber.toString();
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.print("Число: ");
        String number = sc.nextLine();

        System.out.println(number + ": " + NumberConverter.NumberToWords(number));
    }
}