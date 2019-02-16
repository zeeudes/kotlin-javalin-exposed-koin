package events.issue.domain

data class Permission (val cod: Long? = null,
                       val metadata: String? = null,
                       val contents: String? = null,
                       val issues: String? = null)