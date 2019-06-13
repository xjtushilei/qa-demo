package com.xjtu.dlc.autoqa.qa.entity;

import java.util.UUID;

public class QAEntity {
    private String id;

    private Long number;

    private String className;

    private String question;

    private String answer;

    public QAEntity(Long number, String className, String question, String answer) {
        this.number = number;
        this.className = className;
        this.question = question;
        this.answer = answer;
        this.id = UUID.randomUUID().toString();
    }

    public QAEntity() {
    }

    @Override
    public String toString() {
        return "QAEntity{" +
                "id='" + id + '\'' +
                ", number='" + number + '\'' +
                ", className='" + className + '\'' +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
