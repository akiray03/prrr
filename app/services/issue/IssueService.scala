package services.issue

import javax.inject.Inject

import domains.issue.{IssueEntity, IssueRepository}

trait IssueService {
  def listAll(): String
}


class IssueServiceImpl @Inject()(issueRepository: IssueRepository) extends IssueService {
  override def listAll(): String = {
    issueRepository.listAll()
  }
}
