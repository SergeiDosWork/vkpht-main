package me.goodt.vkpht.module.notification.application.impl;

import lombok.Getter;
import org.springframework.stereotype.Component;

import me.goodt.vkpht.module.notification.api.*;
import me.goodt.vkpht.module.notification.api.learning.LearningServiceClient;
import me.goodt.vkpht.module.notification.api.monitor.MonitorServiceClient;
import me.goodt.vkpht.module.notification.application.orgstructure.OrgstructureServiceAdapter;
import me.goodt.vkpht.module.notification.api.quiz.QuizServiceClient;
import me.goodt.vkpht.module.notification.api.tasksetting2.TasksettingServiceClient;

@Component
@Getter
public class ResolverServiceContainer {

    private final TasksettingServiceClient tasksettingServiceClient;
    private final OrgstructureServiceAdapter orgstructureServiceAdapter;
    private final CompetenceService competenceService;
    private final AppraisalService appraisalService;
    private final EvaluationService evaluationService;
    private final ScaleService scaleService;
    private final LearningServiceClient learningServiceClient;
    private final MonitorServiceClient monitorServiceClient;
    private final QuizServiceClient quizServiceClient;
    private final SubscribeEmployeeToNotificationService subscribeEmployeeToNotificationService;
    private final KeycloakDataService keycloakDataService;
    private final NotificationTemplateService notificationTemplateService;

    public ResolverServiceContainer(TasksettingServiceClient tasksettingServiceClient,
                                    OrgstructureServiceAdapter orgstructureServiceAdapter,
                                    CompetenceService competenceService,
                                    AppraisalService appraisalService,
                                    EvaluationService evaluationService,
                                    ScaleService scaleService,
                                    LearningServiceClient learningServiceClient,
                                    MonitorServiceClient monitorServiceClient,
                                    QuizServiceClient quizServiceClient,
                                    SubscribeEmployeeToNotificationService subscribeEmployeeToNotificationService,
                                    KeycloakDataService keycloakDataService,
                                    NotificationTemplateService notificationTemplateService) {
        this.tasksettingServiceClient = tasksettingServiceClient;
        this.orgstructureServiceAdapter = orgstructureServiceAdapter;
        this.competenceService = competenceService;
        this.appraisalService = appraisalService;
        this.evaluationService = evaluationService;
        this.scaleService = scaleService;
        this.learningServiceClient = learningServiceClient;
        this.monitorServiceClient = monitorServiceClient;
        this.quizServiceClient = quizServiceClient;
        this.subscribeEmployeeToNotificationService = subscribeEmployeeToNotificationService;
        this.keycloakDataService = keycloakDataService;
        this.notificationTemplateService = notificationTemplateService;
    }
}
