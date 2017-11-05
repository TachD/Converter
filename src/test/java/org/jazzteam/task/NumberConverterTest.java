package org.jazzteam.task;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class NumberConverterTest {

    @Test(expected = Exception.class)
    public void testNumberToWordsZeroEmptyString() throws Exception {
        NumberConverter.NumberToWords("");
        Assert.fail("Исключение не выброшено. Метод \"съел\" пустую строку");
    }

    @Test(expected = Exception.class)
    public void testNumberToWordsLongest() throws Exception {
        NumberConverter.NumberToWords("23456789876543456789876543234567898765433456789987654324568753525734734365327250435675467546346363463464");
        Assert.fail("Исключение не выброшено. Метод \"съел\" слишком длинную строку");
    }

    @Test(expected = NullPointerException.class)
    public void testNumberToWordsNull() throws Exception {
        System.out.println(NumberConverter.NumberToWords(null));
        Assert.fail("Исключение не выброшено. Метод \"съел\" null");
    }

    @Test(expected = Exception.class)
    public void testNumberToWordsPattern() throws Exception {
        NumberConverter.NumberToWords("someNumber");
        Assert.fail("Исключение не выброшено. Метод \"съел\" строку неправильного формата");
    }

    @Test
    public void testNumberToWordsNumbers() {
        String[][] data = {{"0", "-00", "6483413345", "wt70ght9wg5ho954", "-86453212446", "3432675543567546", "4554364354-", "23456789876543456789876543234567898765433456789987654324568753525734734365327250435675467546346363463464", null, "", "-00000001"},
                {"ноль", "ноль", "шесть миллиардов четыреста восемьдесят три миллиона четыреста тринадцать тысяч триста сорок пять", "Неправильный формат строки. Допускаются только числа и символ \"-\" в начале строки", "минус восемьдесят шесть миллиардов четыреста пятьдесят три миллиона двести двенадцать тысяч четыреста сорок шесть", "три квадриллиона четыреста тридцать два триллиона шестьсот семьдесят пять миллиардов пятьсот сорок три миллиона пятьсот шестьдесят семь тысяч пятьсот сорок шесть", "Неправильный формат строки. Допускаются только числа и символ \"-\" в начале строки", "Слишком длинный аргумент", null, "Аргумент является пустой строкой", "минус один"}};

        for (int i = 0; i < data.length; ++i)
            try {
                String result = NumberConverter.NumberToWords(data[0][i]);
                Assert.assertEquals("Ошибка, данные не совпали", data[1][i], result);
            } catch (Exception expectedException) {
                Assert.assertEquals("Ошибка, данные исключения не совпали!", data[1][i], expectedException.getMessage());
            }
    }

    @Test
    public void testNumberToWordsData() {
        ArrayList<ArrayList<String>> data;

        try {
            data = getTestDataFromFile();
        } catch (IOException e) {
            Assert.fail("Ошибка чтения тестовых данных: " + e.getMessage());
            return;
        }

        for (int i = 0; i < data.get(0).size(); ++i)
            try {
                String result = NumberConverter.NumberToWords(data.get(0).get(i));
                Assert.assertEquals("Ошибка, данные не совпали", data.get(1).get(i), result);
            } catch (Exception expectedException) {
                Assert.assertEquals("Ошибка, данные исключения не совпали!", data.get(1).get(i), expectedException.getMessage());
            }
    }

    /**
     * Чтение тестировочных данных.
     *
     * @return список, содержащий 2 списка данных. Первый внутренний список содержит аргументы, второй - эталонные результаты
     * @throws IOException если происходит ошибка чтения из файла
     */
    private static ArrayList<ArrayList<String>> getTestDataFromFile() throws IOException {
        ArrayList<ArrayList<String>> testData = new ArrayList<>(2) {{
            add(new ArrayList<>());
            add(new ArrayList<>());
        }};

        FileInputStream fis = new FileInputStream("src/test/resources/testData.xls");
        HSSFWorkbook wordBook = new HSSFWorkbook(fis);

        Sheet sheet = wordBook.getSheetAt(0);

        for (Row row : sheet)
            for (Cell cell : row)
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_NUMERIC:
                        testData.get(0).add(String.valueOf((int) cell.getNumericCellValue()));
                        break;

                    case Cell.CELL_TYPE_STRING:
                        testData.get(1).add((cell.getStringCellValue()));
                        break;
                    default:
                }

        return testData;
    }
}
