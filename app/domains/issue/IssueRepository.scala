package domains.issue

import domains.issue.IssueEntity
import domains.issue_query.IssueQueryEntity

trait IssueRepository {
  def fetchIssues(issueQuery: IssueQueryEntity): String
}
