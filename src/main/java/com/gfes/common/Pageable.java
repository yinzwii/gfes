package com.gfes.common;

public interface Pageable {

    void showPage(int page);

    int getCurrentPage();

    int getLastPage();

    long getTotalElements();

    void setCurrentPage(int page);
}
