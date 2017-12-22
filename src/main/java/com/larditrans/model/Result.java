package com.larditrans.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by sunny on 21.12.17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private int pages;
    private int page;
    private int count;
    private String paginator;
    private String table;
}
