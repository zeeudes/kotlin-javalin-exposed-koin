package events.issue.domain

data class EventIssue (
        val cod: Long? = null,
        val action: String? = null,
        val issue: Issue? = null,
        val changes: Change? = null,
        val repository: Repository? = null,
        val sender: Member? = null
)