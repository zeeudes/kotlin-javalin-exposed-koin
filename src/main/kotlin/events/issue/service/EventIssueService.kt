package events.issue.service

import events.issue.domain.EventIssue
import events.issue.repository.EventIssueRepository

class EventIssueService(private val eventIssueRepository: EventIssueRepository) {
    fun create(eventIssue: EventIssue) {
        eventIssueRepository.create(eventIssue)
    }

    fun getByIssueNumber(number: Int): List<EventIssue> {
        return eventIssueRepository.getByIssueNumber(number)
    }
}