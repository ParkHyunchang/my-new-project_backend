package com.hyunchang.newproject.controller;

import com.hyunchang.newproject.entity.Menu;
import com.hyunchang.newproject.repository.MenuRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuRepository menuRepository;

    public MenuController(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @GetMapping
    public List<MenuDto> list() {
        return menuRepository.findAllByOrderBySortOrderAsc().stream()
                .map(m -> new MenuDto(m.getId(), m.getName(), m.getPath()))
                .collect(Collectors.toList());
    }

    public record MenuDto(Long id, String name, String path) {}
}
