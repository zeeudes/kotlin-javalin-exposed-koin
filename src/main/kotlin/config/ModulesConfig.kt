package config

import events.issue.controller.EventIssueController
import events.issue.endpoint.Router
import events.issue.repository.*
import events.issue.service.EventIssueService
import org.koin.dsl.module.module

object ModulesConfig {
    private val configModule = module {
        single { AppConfig() }
        single { DbConfig(getProperty("jdbc.url"), getProperty("db.username"), getProperty("db.password")).getDataSource() }
        single { Router(get()) }
    }

    private val eventIssueModule = module {
        single { EventIssueController(get()) }
        single { EventIssueService(get()) }
        single { EventIssueRepository(get(),get(),get(),get(),get()) }
        single { ChangeRepository(get(), get()) }
        single { IssueRepository(get(),get(),get(),get()) }
        single { LabelRepository(get()) }
        single { MemberRepository(get()) }
        single { MilestoneReposiotry(get(),get()) }
        single { PermissionRepository(get()) }
        single { RepositoriesRepository(get(),get()) }
    }

    internal val allModules = listOf(ModulesConfig.configModule, ModulesConfig.eventIssueModule)
}