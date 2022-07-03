package com.song.kb.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * test
 * @author 
 */
@Data
public class Test1 implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 密码
     */
    private String password;

    private static final long serialVersionUID = 1L;
}