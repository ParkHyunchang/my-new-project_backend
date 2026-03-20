package com.hyunchang.newproject.controller;

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
        return menuRepository.findAllByVisibleTrueOrderBySortOrderAsc().stream()
                .map(m -> new MenuDto(m.getId(), m.getName(), m.getPath()))
                .collect(Collectors.toList());
    }

    // 라우터용: 컴포넌트가 있는 메뉴 전체 반환 (visible 필터 없음 - 라우트는 항상 등록)
    @GetMapping("/routes")
    public List<RouteDto> routes() {
        return menuRepository.findAllByOrderBySortOrderAsc().stream()
                .filter(m -> m.getComponent() != null && !m.getComponent().isBlank())
                .map(m -> new RouteDto(m.getName(), m.getPath(), m.getComponent()))
                .collect(Collectors.toList());
    }

    public record MenuDto(Long id, String name, String path) {}
    public record RouteDto(String name, String path, String component) {}
}
