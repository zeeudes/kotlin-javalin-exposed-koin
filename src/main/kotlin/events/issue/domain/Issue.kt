package events.issue.domain

data class Issue (
        val id: Long? = null,
        val url: String? = null,
        val repositoryURL: String? = null,
        val labelsURL: String? = null,
        val commentsURL: String? = null,
        val eventsURL: String? = null,
        val htmlURL: String? = null,
        val nodeID: String? = null,
        val number: Int? = null,
        val title: String? = null,
        val user: Member? = null,
        val labels: List<Label>? = null,
        val state: String? = null,
        val locked: Boolean? = null,
        val assignee: Member? = null,
        val assignees: List<Member>? = null,
        val milestone: Milestone? = null,
        val comments: Int? = null,
        val createdAt: String? = null,
        val updatedAt: String? = null,
        val closedAt: String? = null,
        val authorAssociation: String? = null,
        val body: String? = null
)