package com.wishes.market.vo;

public class TypeTopFive {
    private String typename;
    private Integer countNumber;

    public TypeTopFive(String typename, Integer countNumber) {
        this.typename = typename;
        this.countNumber = countNumber;
    }

    public TypeTopFive() {
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public Integer getCountNumber() {
        return countNumber;
    }

    public void setCountNumber(Integer countNumber) {
        this.countNumber = countNumber;
    }

    @Override
    public String toString() {
        return "TypeTopFive{" +
                "typename='" + typename + '\'' +
                ", count=" + countNumber +
                '}';
    }
}
