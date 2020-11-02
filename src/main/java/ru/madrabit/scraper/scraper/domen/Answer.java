package ru.madrabit.scraper.scraper.domen;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@EqualsAndHashCode
@Slf4j
@ToString
public class Answer {
    private int id;
    private String text;
    private int question_id;
    private boolean right;

    public Answer(int id, String text, int question_id) {
        this.id = id;
        this.text = text;
        this.question_id = question_id;
    }
}
