package events.issue.domain

data class Milestone(val id: Long? = null,
                     val url: String? = null,
                     val htmlURL: String? = null,
                     val labelsURL: String? = null,
                     val nodeID: String? = null,
                     val number: Long? = null,
                     val title: String? = null,
                     val description: String? = null,
                     val creator: Member? = null,
                     val openIssues: Long? = null,
                     val closedIssues: Long? = null,
                     val state: String? = null,
                     val createdAt: String? = null,
                     val updatedAt: String? = null,
                     val dueOn: String? = null,
                     val closedAt: Member? = null)