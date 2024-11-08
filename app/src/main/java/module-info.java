module vkpht.main.app {
    requires vkpht.main.common;
    requires spring.boot;
    requires spring.boot.autoconfigure;

    requires vkpht.main.mod.platform;
    requires vkpht.main.mod.orgstructure;
    requires vkpht.main.mod.tasksetting;

    exports me.goodt.vkpht;
}