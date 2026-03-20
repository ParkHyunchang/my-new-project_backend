package com.hyunchang.newproject.config;

import com.hyunchang.newproject.entity.Menu;
import com.hyunchang.newproject.repository.MenuRepository;
import com.hyunchang.newproject.service.AuthService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DataInitializer implements ApplicationRunner {

    private final AuthService authService;
    private final MenuRepository menuRepository;

    public DataInitializer(AuthService authService, MenuRepository menuRepository) {
        this.authService = authService;
        this.menuRepository = menuRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        authService.seedDefaultUserIfEmpty();
        seedDefaultMenusIfEmpty();
    }

    private static final Map<String, String> DEFAULT_COMPONENTS = Map.of(
            "/", "HomeView",
            "/test", "TestView"
    );

    private void seedDefaultMenusIfEmpty() {
        if (menuRepository.count() == 0) {
            menuRepository.saveAll(List.of(
                    new Menu("Home", "/", 1, "HomeView"),
                    new Menu("Test", "/test", 2, "TestView")
            ));
            return;
        }
        // 기존 메뉴에 component가 없으면 기본값 설정
        menuRepository.findAll().forEach(menu -> {
            if (menu.getComponent() == null || menu.getComponent().isBlank()) {
                String comp = DEFAULT_COMPONENTS.get(menu.getPath());
                if (comp != null) {
                    menu.setComponent(comp);
                    menuRepository.save(menu);
                }
            }
        });
    }
}
