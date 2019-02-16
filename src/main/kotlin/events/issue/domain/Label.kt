package events.issue.domain

data class Label (
          val id: Long? = null,
          val nodeID: String? = null,
          val url: String? = null,
          val name: String? = null,
          val color: String? = null,
          val labelDefault: Boolean? = null,
          val issue: Issue? = null)