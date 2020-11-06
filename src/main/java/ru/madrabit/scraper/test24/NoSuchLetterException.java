package ru.madrabit.scraper.test24;

public class NoSuchLetterException extends Exception {
    public NoSuchLetterException(String errorMessage) {
        super(errorMessage);
    }
}
