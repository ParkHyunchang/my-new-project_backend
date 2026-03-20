package com.hyunchang.newproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menu")
@Getter
@NoArgsConstructor
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 100)
    private String path;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean visible = true;

    @Column(length = 100)
    private String component;

    public Menu(String name, String path, Integer sortOrder, String component) {
        this.name = name;
        this.path = path;
        this.sortOrder = sortOrder != null ? sortOrder : 0;
        this.visible = true;
        this.component = component;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setComponent(String component) {
        this.component = component;
    }
}
