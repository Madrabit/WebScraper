package ru.madrabit.scraper.domen;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
@Slf4j
@EqualsAndHashCode
@ToString
public class Question {
    private String id;
    private String text;
    private Set<Answer> answerSet = new TreeSet<>(new Comparator<Answer>() {
        @Override
        public int compare(Answer o1, Answer o2) {
            return o1.getId() - o2.getId();
        }
    });
    private Set<Integer> answerNumber = new HashSet<>();

    public Question(String id, String text) {
        this.id = id;
        this.text = text;
    }
}
