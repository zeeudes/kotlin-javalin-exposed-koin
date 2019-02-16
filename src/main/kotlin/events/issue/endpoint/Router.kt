package events.issue.endpoint

import events.issue.controller.EventIssueController
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import org.koin.standalone.KoinComponent

class Router(private val eventIssueController: EventIssueController): KoinComponent{

    fun register(app: Javalin) {
        app.routes {
            post("events", eventIssueController::create)
            get(":id/events", eventIssueController::get)
        }
    }
}