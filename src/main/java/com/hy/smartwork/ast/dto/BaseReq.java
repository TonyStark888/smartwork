package com.hy.smartwork.ast.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author huangying
 */
@Setter
@Getter
public class BaseReq implements Serializable {
    private String token;
    private Integer page;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date searchDate;
}
