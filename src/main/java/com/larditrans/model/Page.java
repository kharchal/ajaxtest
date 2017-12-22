package com.larditrans.model;

import lombok.Data;

import java.util.List;

/**
 * Created by sunny on 19.12.17
 */
@Data
public class Page<T> {

    private List<T> list;
    private int pages;
    private int perpage;
    private int page;
    private int count;
}
