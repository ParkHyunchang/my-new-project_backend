package com.hyunchang.newproject.controller;

import com.hyunchang.newproject.repository.MenuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/admin/menus")
public class AdminMenuController {

    private final MenuRepository menuRepository;

    public AdminMenuController(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @GetMapping
    public List<AdminMenuDto> list() {
        return menuRepository.findAllByOrderBySortOrderAsc().stream()
                .map(m -> new AdminMenuDto(m.getId(), m.getName(), m.getPath(), m.getSortOrder(), m.isVisible(), m.getComponent()))
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminMenuDto> update(@PathVariable Long id, @RequestBody UpdateMenuRequest body) {
        return menuRepository.findById(id)
                .map(menu -> {
                    if (body.visible() != null) menu.setVisible(body.visible());
                    if (body.component() != null) menu.setComponent(body.component());
                    menuRepository.save(menu);
                    log.info("[관리자] 메뉴 수정, id={}, name={}, visible={}, component={}", menu.getId(), menu.getName(), menu.isVisible(), menu.getComponent());
                    return ResponseEntity.ok(new AdminMenuDto(menu.getId(), menu.getName(), menu.getPath(), menu.getSortOrder(), menu.isVisible(), menu.getComponent()));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public record AdminMenuDto(Long id, String name, String path, Integer sortOrder, boolean visible, String component) {}
    public record UpdateMenuRequest(Boolean visible, String component) {}
}
