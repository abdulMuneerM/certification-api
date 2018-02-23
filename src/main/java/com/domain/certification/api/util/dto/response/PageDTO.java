package com.domain.certification.api.util.dto.response;

public class PageDTO {

    private long size;

    private long totalElements;

    private int totalPages;

    private int number;

    public PageDTO() {
    }

    public PageDTO(long size, long totalElements, int totalPages, int number) {

        if (size >= totalElements) {
            this.size = totalElements;
        } else {
            this.size = size;
        }

        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.number = number;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
