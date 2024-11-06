module vkpht.main.app.main {
    requires vkpht.main.common.main;
    requires spring.boot;
    requires spring.boot.autoconfigure;

    requires mod.common.vkpht.main.mod.common.main;
    requires mod.orgstructure.vkpht.main.mod.orgstructure.main;
    requires mod.tasksetting.vkpht.main.mod.tasksetting.main;

    exports me.goodt.vkpht;
}