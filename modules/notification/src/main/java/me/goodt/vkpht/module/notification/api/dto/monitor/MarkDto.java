/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.goodt.vkpht.module.notification.api.dto.monitor;

public class MarkDto {

    private Long id;
    private Double mark;

    public MarkDto() {

    }

    public MarkDto(Long id, Double mark) {
        this.id = id;
        this.mark = mark;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMark() {
        return mark;
    }

    public void setMark(Double mark) {
        this.mark = mark;
    }
}
