package domains.issue

import domains.issue.IssueEntity

trait IssueRepository {
  def listAll(): String
}
