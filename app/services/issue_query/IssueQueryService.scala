package services.issue_query

import javax.inject.Inject

import domains.issue_query.{IssueQueryEntity, IssueQueryRepository}

trait IssueQueryService {
  def listAll(): List[IssueQueryEntity]

  def fetch(id: Long): Option[IssueQueryEntity]

  def insert(issueQueryEntity: IssueQueryEntity): Option[Long]
}

class IssueQueryServiceImpl @Inject()(issueQueryRepository: IssueQueryRepository)
  extends IssueQueryService {

  override def listAll(): List[IssueQueryEntity] = {
    issueQueryRepository.listAll()
  }

  override def fetch(id: Long): Option[IssueQueryEntity] = {
    issueQueryRepository.fetch(id)
  }

  override def insert(issueQueryEntity: IssueQueryEntity): Option[Long] = {
    issueQueryRepository.insert(issueQueryEntity)
  }
}
