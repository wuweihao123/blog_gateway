package com.wwh.blog.config.predicate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

/**
 * @author: wwh
 * @date: 2022/8/18
 * @description:
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeBetweenConfig {

    private LocalTime start;

    private LocalTime end;
}
