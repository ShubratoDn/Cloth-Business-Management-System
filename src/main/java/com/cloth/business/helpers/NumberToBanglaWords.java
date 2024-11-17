package com.cloth.business.helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.stream.Collectors;

public class NumberToBanglaWords {
    public static String numberToBanglaText(Object numericValue, FORMAT format, NUMBER_DISPLAY numberDisplay) {
        return processNumber(numericValue, format, numberDisplay);
    }

    public static String numberToBanglaText(Object numericValue, FORMAT format) {
        return processNumber(numericValue, format, NUMBER_DISPLAY.DECIMAL);
    }

    public static String numberToBanglaText(Object numericValue, NUMBER_DISPLAY numberDisplay) {
        return processNumber(numericValue, FORMAT.ENGLISH, numberDisplay);
    }

    public static String numberToBanglaText(Object numericValue) {
        return processNumber(numericValue, FORMAT.ENGLISH, NUMBER_DISPLAY.DECIMAL);
    }

    private static String processNumber(Object numericValue, FORMAT format, NUMBER_DISPLAY numberDisplay) {
        String result = "";
        String number = String.valueOf(numericValue);

        if (number.startsWith("-")) {
            number = number.substring(1);
            result = (format.equals(FORMAT.BANGLA) || format.equals(FORMAT.BANGLA_LEGACY)) ? "ঋণাত্মক " : "Minus ";
        }

        int dotIndex = number.indexOf('.');
        String numberBeforeDecimal = (dotIndex != -1) ? number.substring(0, dotIndex) : number;
        String decimalPartInWords = processDecimalNumberInWords(number, format, numberDisplay);

        if (numberBeforeDecimal.equals("0") || numberBeforeDecimal.isEmpty()) {
            result += (format.equals(FORMAT.BANGLA) || format.equals(FORMAT.BANGLA_LEGACY)) ? "শূন্য" : "Zero";
            result += " " + decimalPartInWords;
        } else {
            if (format.equals(FORMAT.BANGLA)) {
                result += traditionalBengaliFormat(numberBeforeDecimal);
            } else if (format.equals(FORMAT.ENGLISH_LEGACY)) {
                result += legacyBengaliFormatInEnglish(numberBeforeDecimal);
            } else if (format.equals(FORMAT.BANGLA_LEGACY)) {
                result += legacyBengaliFormat(numberBeforeDecimal);
            } else {
                result += traditionalBengaliFormatInEnglish(numberBeforeDecimal);
            }
            result += " " + decimalPartInWords;
        }

        return result.trim();
    }

    private static String traditionalBengaliFormatInEnglish(String inputString) {
        int[] pattern = { 2, 2, 3 };
        return convertNumberToWords(pattern, ENGLISH_NUMBER_UNITS, inputString);
    }

    private static String legacyBengaliFormatInEnglish(String inputString) {
        int[] pattern = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3 };
        return convertNumberToWords(pattern, ENGLISH_LEGACY_NUMBER_UNITS, inputString);
    }

    private static String traditionalBengaliFormat(String inputString) {
        int[] pattern = { 2, 2, 3 };
        return convertNumberToWords(pattern, BENGALI_NUMBER_UNITS, inputString);
    }

    private static String legacyBengaliFormat(String inputString) {
        int[] pattern = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3 };
        return convertNumberToWords(pattern, BENGALI_LEGACY_NUMBER_UNITS, inputString);
    }

    private static String decimalToTextBengali(String decimalPart) {
        return decimalPart.chars().mapToObj(c -> BENGALI_ZERO_TO_HUNDRED[Character.getNumericValue(c)])
                .collect(Collectors.joining(" "));
    }

    private static String decimalToTextEnglish(String decimalPart) {
        return decimalPart.chars().mapToObj(c -> ONES[Character.getNumericValue(c)]).collect(Collectors.joining(" "));
    }

    private static String processDecimalNumberInWords(String number, FORMAT format, NUMBER_DISPLAY numberDisplay) {
        int dotIndex = number.indexOf('.');

        if (numberDisplay.equals(NUMBER_DISPLAY.DECIMAL)) {
            if (format.equals(FORMAT.BANGLA) || format.equals(FORMAT.BANGLA_LEGACY)) {
                return (dotIndex == -1) ? ""
                        : DOSHOMIK_BENGALI + " "
                        + decimalToTextBengali(number.substring(dotIndex + 1).replaceAll("0*$", ""));
            } else {
                return (dotIndex == -1) ? ""
                        : POINT + " " + decimalToTextEnglish(number.substring(dotIndex + 1).replaceAll("0*$", ""));

            }
        } else if (numberDisplay.equals(NUMBER_DISPLAY.CURRENCY)) {
            String currencyUnit;
            int roundedDecimalPart = makeRoundedDecimal(number);
            if (format.equals(FORMAT.BANGLA) || format.equals(FORMAT.BANGLA_LEGACY)) {
                currencyUnit = (dotIndex == -1) || roundedDecimalPart == 0 ? TAKA_BENGALI
                        : TAKA_BENGALI + " " + BENGALI_ZERO_TO_HUNDRED[roundedDecimalPart] + " " + PAISA_BENGALI;
            } else {
                currencyUnit = (dotIndex == -1) || roundedDecimalPart == 0 ? TAKA
                        : TAKA + " " + convertLessThanThousand(roundedDecimalPart) + PAISA;
            }
            return currencyUnit;
        } else {
            return "";
        }
    }

    private static int makeRoundedDecimal(String number) {
        BigDecimal bigDecimal = new BigDecimal(number);
        BigDecimal rounded = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        DecimalFormat df = new DecimalFormat("#.00");
        String formattedRounded = df.format(rounded);
        int roundedDecimalPart = Integer.parseInt(formattedRounded.substring(formattedRounded.indexOf('.') + 1));
        return roundedDecimalPart;
    }

    private static String convertLessThanThousandToWords(int num, String[] units) {
        if (units == BENGALI_NUMBER_UNITS || units == BENGALI_LEGACY_NUMBER_UNITS) {
            return convertLessThanThousandToBangla(num);
        } else {
            return convertLessThanThousand(num);
        }
    }

    private static String convertLessThanThousandToBangla(int num) {
        if (num == 0) {
            return "";
        } else if (num < 100) {
            return BENGALI_ZERO_TO_HUNDRED[num] + " ";
        } else {
            return BENGALI_ZERO_TO_HUNDRED[num / 100] + " শত " + convertLessThanThousandToBangla(num % 100);
        }
    }

    private static String convertLessThanThousand(int num) {
        if (num == 0) {
            return "";
        } else if (num < 10) {
            return ONES[num] + " ";
        } else if (num < 20) {
            return TEENS[num - 10] + " ";
        } else if (num < 100) {
            return TENS[num / 10] + " " + ONES[num % 10] + " ";
        } else {
            return ONES[num / 100] + " Hundred " + convertLessThanThousand(num % 100);
        }
    }

    private static String convertNumberToWords(int[] pattern, String[] units, String inputString) {
        String words = "";
        int currentIndex = inputString.length();

        boolean isUnitEnabled = false;
        boolean shouldBreak = false;
        while (currentIndex > 0) {
            int index = 0;
            for (int i = pattern.length - 1; i >= 0; i--) {
                int length = pattern[i];
                if (currentIndex > 0) {
                    String substring = (currentIndex - length >= 0)
                            ? inputString.substring(currentIndex - length, currentIndex)
                            : inputString.substring(0, currentIndex);
                    int number = Integer.parseInt(substring);

                    if (isUnitEnabled && i == pattern.length - 1) {
                        words = convertLessThanThousandToWords(number, units) + units[index] + units[pattern.length]
                                + " " + words;
                    } else {
                        words = convertLessThanThousandToWords(number, units) + units[index] + " " + words;
                    }

                    currentIndex -= length;
                    index++;
                } else {
                    shouldBreak = true;
                    break;
                }
            }
            if (shouldBreak) {
                break;
            }
            isUnitEnabled = true;
        }

        return words.trim();
    }

    public static enum FORMAT {
        BANGLA, BANGLA_LEGACY, ENGLISH, ENGLISH_LEGACY
    }

    public static enum NUMBER_DISPLAY {
        DECIMAL, CURRENCY
    }

    private static String DOSHOMIK_BENGALI = "দশমিক";
    private static String TAKA_BENGALI = "টাকা";
    private static String PAISA_BENGALI = "পয়সা";
    private static String POINT = "Point";
    private static String TAKA = "Taka";
    private static String PAISA = "Paisa";

    private static final String[] ONES = { "Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight",
            "Nine" };

    private static final String[] TEENS = { "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen",
            "Seventeen", "Eighteen", "Nineteen" };

    private static final String[] TENS = { "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty",
            "Ninety" };

    private static final String[] ENGLISH_NUMBER_UNITS = { "", "Thousand", "Lakh", "Crore", "Arab", "Kharab", "Neel",
            "Padma", "Shankh", "Padra", "Kharab", "Shankh", "Kuti", "Kharab", "Shankh", "Mahashankh" };
    private static final String[] ENGLISH_LEGACY_NUMBER_UNITS = { "", "Thousand", "Ojut", "Laksh", "Niyut", "Koti",
            "Arbyud", "Padma", "Kharba", "Nikharba", "MahaPadma", "Shanku", "Jaladhi", "Antyo", "Moddho", "Parardha" };

    private static final String[] BENGALI_ZERO_TO_HUNDRED = { "শূন্য", "এক", "দুই", "তিন", "চার", "পাঁচ", "ছয়", "সাত",
            "আট", "নয়", "দশ", "এগারো", "বারো", "তেরো", "চৌদ্দ", "পনেরো", "ষোল", "সতেরো", "আঠারো", "ঊনিশ", "বিশ",
            "একুশ", "বাইশ", "তেইশ", "চব্বিশ", "পঁচিশ", "ছাব্বিশ", "সাতাশ", "আটাশ", "ঊনত্রিশ", "ত্রিশ", "একত্রিশ",
            "বত্রিশ", "তেত্রিশ", "চৌত্রিশ", "পঁয়ত্রিশ", "ছত্রিশ", "সাঁইত্রিশ", "আটত্রিশ", "ঊনচল্লিশ", "চল্লিশ",
            "একচল্লিশ", "বিয়াল্লিশ", "তেতাল্লিশ", "চুয়াল্লিশ", "পঁয়তাল্লিশ", "ছেচল্লিশ", "সাতচল্লিশ", "আটচল্লিশ",
            "ঊনপঞ্চাশ", "পঞ্চাশ", "একান্ন", "বায়ান্ন", "তিপ্পান্ন", "চুয়ান্ন", "পঞ্চান্ন", "ছাপ্পান্ন", "সাতান্ন",
            "আটান্ন", "ঊনষাট", "ষাট", "একষট্টি", "বাষট্টি", "তেষট্টি", "চৌষট্টি", "পঁয়ষট্টি", "ছেষট্টি", "সাতষট্টি",
            "আটষট্টি", "ঊনসত্তর", "সত্তর", "একাত্তর", "বাহাত্তর", "তিয়াত্তর", "চুয়াত্তর", "পঁচাত্তর", "ছিয়াত্তর",
            "সাতাত্তর", "আটাত্তর", "ঊননব্বই", "নব্বই", "একানব্বই", "বিরানব্বই", "তিরানব্বই", "চুরানব্বই", "পঁচানব্বই",
            "ছিয়ানব্বই", "সাতানব্বই", "আটানব্বই", "ঊননব্বই", "নব্বই", "একানব্বই", "বিরানব্বই", "তিরানব্বই",
            "চুরানব্বই", "পঁচানব্বই", "ছিয়ানব্বই", "সাতানব্বই", "আটানব্বই", "নিরানব্বই" };

    private static final String[] BENGALI_NUMBER_UNITS = { "", "হাজার", "লক্ষ", "কোটি", "পদ্ম", "খর্ব", "নিখর্ব",
            "মহাপদ্ম", "শঙ্কু", "জলধি", "অন্ত্য", "মধ্য", "পরার্ধ" };
    private static final String[] BENGALI_LEGACY_NUMBER_UNITS = { "", "হাজার", "অযুত", "লক্ষ", "নিযুত", "কোটি",
            "অর্বুদ", "পদ্ম", "খর্ব", "নিখর্ব", "মহাপদ্ম", "শঙ্কু", "জলধি", "অন্ত্য", "মধ্য", "পরার্ধ" };
}