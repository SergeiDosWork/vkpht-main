package me.goodt.vkpht.module.notification.api.dto.data;

import java.util.Arrays;

public enum NotificationRecipientType {
    STATIC_EMPLOYEE("static_employee", "Статичный получатель уведомления", true),
    STATIC_DIVISION("static_division", "Статичное подразделение-получатель уведомления", true),
    STATIC_EMAIL("static_email", "Email получателя уведомления", true),
    DYNAMIC("dynamic", null, false);

    private final String name;
    private final String description;
    /**
     * Признак статического типа - их необходимо исключать из списка динамических
     */
    private final boolean isStatic;

    NotificationRecipientType(String name, String description, boolean isStatic) {
        this.name = name;
        this.description = description;
        this.isStatic = isStatic;
    }

    public static String[] getAllStaticTypes() {
        return Arrays.stream(NotificationRecipientType.values())
            .filter(NotificationRecipientType::isStatic)
            .map(NotificationRecipientType::getName)
            .toArray(String[]::new);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isStatic() {
        return isStatic;
    }
}
