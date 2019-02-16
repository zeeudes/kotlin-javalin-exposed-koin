package events.issue.controller

import events.issue.domain.EventIssue
import events.issue.service.EventIssueService
import io.javalin.Context

class EventIssueController(private val eventIssueService: EventIssueService) {
    fun create(ctx: Context) {
        val event = ctx.body<EventIssue>()
        this.eventIssueService.create(event)
    }

    fun get(ctx: Context) {
        ctx.also {
            ctx.json(this.eventIssueService.getByIssueNumber((ctx.pathParam("id").toInt())))
        }
    }
}